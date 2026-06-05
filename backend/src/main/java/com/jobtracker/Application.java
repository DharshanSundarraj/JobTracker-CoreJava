package com.jobtracker;

public class Application {
    private int id;
    private String companyName;
    private String jobRole;
    private String status;
    private String assessmentLink;
    private String interviewSchedule;
    private String notes;
    private String appliedDate;
    private String deadline;
    private String location;

    public Application(int id, String companyName, String jobRole, String status, 
                       String assessmentLink, String interviewSchedule, String notes,
                       String appliedDate, String deadline, String location) {
        this.id = id;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.status = status;
        this.assessmentLink = assessmentLink;
        this.interviewSchedule = interviewSchedule;
        this.notes = notes;
        this.appliedDate = appliedDate;
        this.deadline = deadline;
        this.location = location;
    }

    // This converts Java Object into a JSON string
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

    // Getters
    public int getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getJobRole() { return jobRole; }
    public String getStatus() { return status; }
    public String getAssessmentLink() { return assessmentLink; }
    public String getInterviewSchedule() { return interviewSchedule; }
    public String getNotes() { return notes; }
    public String getAppliedDate() { return appliedDate; }
    public String getDeadline() { return deadline; }
    public String getLocation() { return location; }
}