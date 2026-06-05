package com.jobtracker;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class JobTrackerAPI {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/applications", new ApplicationHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("SUCCESS: Core Java Web Server is ALIVE!");
        } catch (IOException e) { e.printStackTrace(); }
    }

    static class ApplicationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
    
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            String method = exchange.getRequestMethod();

            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equals(method)) {
                String requestBody = readRequestBody(exchange);
                String company = extractJsonValue(requestBody, "companyName");
                String role = extractJsonValue(requestBody, "jobRole");
                String appliedDate = extractJsonValue(requestBody, "appliedDate");
                String deadline = extractJsonValue(requestBody, "deadline");
                String location = extractJsonValue(requestBody, "location");
                
                DatabaseConnection.addApplication(company, role, appliedDate, deadline, location);
                sendResponse(exchange, 201, "{\"status\":\"success\"}");
                return;
            }

            if ("PUT".equals(method)) {
                String requestBody = readRequestBody(exchange);
                String idString = extractJsonValue(requestBody, "id");
                String newStatus = extractJsonValue(requestBody, "status");
                DatabaseConnection.updateStatus(Integer.parseInt(idString), newStatus);
                sendResponse(exchange, 200, "{\"status\":\"updated\"}");
                return;
            }

            if ("DELETE".equals(method)) {
                String requestBody = readRequestBody(exchange);
                String idString = extractJsonValue(requestBody, "id");
                DatabaseConnection.deleteApplication(Integer.parseInt(idString));
                sendResponse(exchange, 200, "{\"status\":\"deleted\"}");
                return;
            }

            if ("GET".equals(method)) {
                List<Application> apps = DatabaseConnection.getAllApplications();

                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < apps.size(); i++) {
                    Application a = apps.get(i);
                    json.append("{")
                        .append("\"id\":").append(a.getId()).append(",")
                        .append("\"companyName\":\"").append(a.getCompanyName()).append("\",")
                        .append("\"jobRole\":\"").append(a.getJobRole()).append("\",")
                        .append("\"status\":\"").append(a.getStatus()).append("\",")
                        .append("\"appliedDate\":\"").append(cleanNull(a.getAppliedDate())).append("\",")
                        .append("\"deadline\":\"").append(cleanNull(a.getDeadline())).append("\",")
                        .append("\"location\":\"").append(cleanNull(a.getLocation())).append("\"")
                        .append("}");
                    if (i < apps.size() - 1) json.append(",");
                }
                json.append("]");

                sendResponse(exchange, 200, json.toString());
            }
        }

        private String readRequestBody(HttpExchange exchange) throws IOException {
            java.io.InputStream is = exchange.getRequestBody();
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String extractJsonValue(String json, String key) {
            String searchKey = "\"" + key + "\":\"";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return "";
            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex > -1) return json.substring(startIndex, endIndex);
            return "";
        }

        private String cleanNull(String value) { return value == null ? "" : value; }
    }
}