package com.jobtracker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class JobTrackerAPI {
    public static void main(String[] args) throws IOException {
        String portEnv = System.getenv("PORT");
        int port = (portEnv != null) ? Integer.parseInt(portEnv) : 10000;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/applications", new ApplicationHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("SUCCESS: API Engine live on port " + port);
    }

    static class ApplicationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 1. CORS Headers (Allow Frontend Access)
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

            String method = exchange.getRequestMethod();

            // Handle Preflight Check
            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // 2. Route the Request to the correct SQL action
            try {
                if ("GET".equalsIgnoreCase(method)) {
                    handleGet(exchange);
                } else if ("POST".equalsIgnoreCase(method)) {
                    handlePost(exchange);
                } else if ("PUT".equalsIgnoreCase(method)) {
                    handlePut(exchange);
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    handleDelete(exchange);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Internal Server Error\"}");
            }
        }

        // --- READ: Fetch Data ---
        private void handleGet(HttpExchange exchange) throws Exception {
            StringBuilder jsonResponse = new StringBuilder("[");
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 // Added ORDER BY id DESC to show newest applications first!
                 ResultSet rs = stmt.executeQuery("SELECT * FROM applications ORDER BY id DESC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) jsonResponse.append(",");
                    Application app = new Application(
                        rs.getInt("id"),
                        rs.getString("company_name"),
                        rs.getString("job_role"),
                        rs.getString("status"),
                        rs.getString("applied_date"),
                        rs.getString("deadline"),
                        rs.getString("location")
                    );
                    jsonResponse.append(app.toJson());
                    first = false;
                }
            }
            jsonResponse.append("]");
            sendResponse(exchange, 200, jsonResponse.toString());
        }

        // --- CREATE: Insert New Application ---
        private void handlePost(HttpExchange exchange) throws Exception {
            String body = getRequestBody(exchange);
            
            System.out.println("INCOMING POST REQUEST: " + body);
            
            // Extract values from the JSON string sent by the frontend
            String companyName = extractJsonString(body, "companyName");
            String jobRole = extractJsonString(body, "jobRole");
            String location = extractJsonString(body, "location");
            String appliedDate = extractJsonString(body, "appliedDate");
            String deadline = extractJsonString(body, "deadline");

            String sql = "INSERT INTO applications (company_name, job_role, location, applied_date, deadline, status) VALUES (?, ?, ?, ?, ?, 'Pending')";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, companyName);
                pstmt.setString(2, jobRole);
                pstmt.setString(3, location);
                pstmt.setString(4, appliedDate.isEmpty() ? null : appliedDate);
                pstmt.setString(5, deadline.isEmpty() ? null : deadline);
                pstmt.executeUpdate();
            }
            sendResponse(exchange, 201, "{\"message\":\"Application saved!\"}");
        }

        // --- UPDATE: Change Status ---
        private void handlePut(HttpExchange exchange) throws Exception {
            String body = getRequestBody(exchange);
            String idStr = extractJsonString(body, "id");
            String status = extractJsonString(body, "status");

            if (!idStr.isEmpty() && !status.isEmpty()) {
                String sql = "UPDATE applications SET status = ? WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, status);
                    pstmt.setInt(2, Integer.parseInt(idStr));
                    pstmt.executeUpdate();
                }
            }
            sendResponse(exchange, 200, "{\"message\":\"Status updated!\"}");
        }

        // --- DELETE: Remove Application ---
        private void handleDelete(HttpExchange exchange) throws Exception {
            String body = getRequestBody(exchange);
            String idStr = extractJsonString(body, "id");

            if (!idStr.isEmpty()) {
                String sql = "DELETE FROM applications WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(idStr));
                    pstmt.executeUpdate();
                }
            }
            sendResponse(exchange, 200, "{\"message\":\"Application deleted!\"}");
        }

        // --- UTILITY METHODS ---
        
        // Reads the incoming JSON package from the browser
        private String getRequestBody(HttpExchange exchange) throws IOException {
            try (InputStream is = exchange.getRequestBody()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        // --- UTILITY METHODS ---
        
        private String extractJsonString(String json, String key) {
            // This regex handles spaces, quotes, and formatting differences automatically
            String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
            java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
            if (m.find()) {
                return m.group(1);
            }
            return "";
        }

        // Sends the final HTTP response back to the browser
        private void sendResponse(HttpExchange exchange, int statusCode, String responseData) throws IOException {
            byte[] responseBytes = responseData.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}