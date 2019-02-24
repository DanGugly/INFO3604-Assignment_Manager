package info3604.assignment_organizer.models;

import java.util.Date;
import java.io.Serializable;

public class Assignment implements Serializable{

    private String assID;

    private String courseID;

    private String title;

    private Date due;

    private String notes;

    public Assignment (String courseID, String title, Date due, String notes){
        this.courseID = courseID;
        this.title = title;
        this.due = due;
        this.notes = notes;
    }

    public String getAssID(){ return assID; }

    public String getCourseID(){ return courseID; }

    public String getTitle(){ return title; }

    public Date getDue() { return due; }

    public String getNotes(){ return this.notes; }

    public void setAssID(String assID) { this.assID = assID; }

    public void setCourseID(String courseID) { this.courseID = courseID; }

    public void setTitle(String title) { this.title = title; }

    public void setDue(Date due) { this.due = due; }

    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object obj) {
        return this.getAssID().equals(((Assignment)obj).getAssID());
    }
}
