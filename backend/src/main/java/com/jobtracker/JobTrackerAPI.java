package com.jobtracker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class JobTrackerAPI {
    public static void main(String[] args) throws IOException {
        String portEnv = System.getenv("PORT");
        int port = (portEnv != null) ? Integer.parseInt(portEnv) : 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/applications", new ApplicationHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("SUCCESS: Core Java Engine running on port " + port);
    }

    static class ApplicationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS HEADERS (Mandatory for Frontend communication)
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

            // Handle Preflight request
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // Logic for GET requests
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                // Fetch data from database
                // List<Application> apps = DatabaseConnection.getAllApplications(); 
                
                // For demonstration, here is the JSON list structure
                String jsonResponse = "[{\"id\": 1, \"companyName\": \"Example Corp\", \"jobRole\": \"Developer\", \"status\": \"Pending\", \"location\": \"Remote\", \"appliedDate\": \"2026-06-05\", \"deadline\": \"2026-06-20\"}]";
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(jsonResponse.getBytes());
                }
            }
        }
    }
}