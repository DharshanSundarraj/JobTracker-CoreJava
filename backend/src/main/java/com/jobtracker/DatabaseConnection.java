package com.jobtracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Replace with your actual Aiven connection string if different
    private static final String URL = "jdbc:mysql://core-pipeline-db-fullstack-services.g.aivencloud.com:24710/defaultdb?sslMode=REQUIRED";
    private static final String USER = "avnadmin";
    
    // Gets password from Render Environment Variables
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new SQLException("DB_PASSWORD environment variable is missing!");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}