package com.jobtracker;

public class Application {
    private int id;
    private String companyName;
    private String jobRole;
    private String status;
    private String appliedDate;
    private String deadline;
    private String location;

    public Application(int id, String companyName, String jobRole, String status, 
                       String appliedDate, String deadline, String location) {
        this.id = id;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.status = status;
        this.appliedDate = appliedDate;
        this.deadline = deadline;
        this.location = location;
    }

    // JSON Translator
    public String toJson() {
        return String.format(
            "{\"id\": %d, \"companyName\": \"%s\", \"jobRole\": \"%s\", \"status\": \"%s\", \"location\": \"%s\", \"appliedDate\": \"%s\", \"deadline\": \"%s\"}",
            id, 
            companyName != null ? companyName : "", 
            jobRole != null ? jobRole : "", 
            status != null ? status : "", 
            location != null ? location : "", 
            appliedDate != null ? appliedDate : "", 
            deadline != null ? deadline : ""
        );
    }
}