package com.jobtracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://core-pipeline-db-fullstack-services.g.aivencloud.com:24710/defaultdb?sslMode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null 
    ? System.getenv("DB_PASSWORD") 
    : "admin2026";

    public static Connection getConnection() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } 
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Application> getAllApplications() {
        List<Application> appList = new ArrayList<>();
        String query = "SELECT * FROM applications";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Application app = new Application(
                    rs.getInt("id"),
                    rs.getString("company_name"),
                    rs.getString("job_role"),
                    rs.getString("status"),
                    rs.getString("assessment_link"),
                    rs.getString("interview_schedule"),
                    rs.getString("notes"),
                    rs.getString("applied_date"),
                    rs.getString("deadline"),
                    rs.getString("location")
                );
                appList.add(app);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return appList;
    }

    // UPDATED: Now accepts 5 parameters instead of 2
    public static void addApplication(String company, String role, String appliedDate, String deadline, String location) {
        String query = "INSERT INTO applications (company_name, job_role, status, applied_date, deadline, location) VALUES (?, ?, 'Pending', ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, company);
            pstmt.setString(2, role);
            
            // Handle empty dates cleanly
            if(appliedDate == null || appliedDate.trim().isEmpty()) pstmt.setNull(3, java.sql.Types.DATE);
            else pstmt.setString(3, appliedDate);
            
            if(deadline == null || deadline.trim().isEmpty()) pstmt.setNull(4, java.sql.Types.DATE);
            else pstmt.setString(4, deadline);
            
            pstmt.setString(5, location);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void updateStatus(int id, String newStatus) {
        String query = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteApplication(int id) {
        String query = "DELETE FROM applications WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}