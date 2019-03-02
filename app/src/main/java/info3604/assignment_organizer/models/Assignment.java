package info3604.assignment_organizer.models;

import java.io.Serializable;

public class Assignment implements Serializable{

    private String assignmentID;

    private String courseID;

    private String title;

    private String dueDate;

    private String notes;

    public Assignment (String courseID, String title, String dueDate, String notes){
        this.courseID = courseID;
        this.title = title;
        this.dueDate = dueDate;
        this.notes = notes;
    }

    public String getAssignmentID(){ return assignmentID; }

    public String getCourseID(){ return courseID; }

    public String getTitle(){ return title; }

    public String getDueDate() { return dueDate; }

    public String getNotes(){ return this.notes; }

    public void setAssID(String assignmentID) { this.assignmentID = assignmentID; }

    public void setCourseID(String courseID) { this.courseID = courseID; }

    public void setTitle(String title) { this.title = title; }

    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object obj) {
        return this.getAssignmentID().equals(((Assignment)obj).getAssignmentID());
    }
}