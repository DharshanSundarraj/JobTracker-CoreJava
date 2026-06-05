package com.jobtracker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
            // CORS Headers
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                StringBuilder jsonResponse = new StringBuilder("[");
                
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM applications")) {
                    
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonResponse.append("]");

                byte[] responseBytes = jsonResponse.toString().getBytes();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            }
        }
    }
}