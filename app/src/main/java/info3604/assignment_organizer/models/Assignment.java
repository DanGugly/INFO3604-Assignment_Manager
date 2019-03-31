package info3604.assignment_organizer.models;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.io.Serializable;
import java.util.Locale;

public class Assignment implements Serializable{

    private int assignmentID;

    private String courseID;

    private String title;

    private String startDate;

    private String dueDate;

    private String notes;

    private int progress;

    public Assignment (String courseID, String title, String dueDate, String notes){
        this.courseID = courseID;
        this.title = title;
        this.dueDate = dueDate;
        this.notes = notes;
        this.progress = 0;
        this.startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
    }

    public Assignment() { }

    public int getAssignmentID(){ return assignmentID; }

    public String getCourseID(){ return courseID; }

    public String getTitle(){ return title; }

    public String getDueDate() { return dueDate; }

    public String getNotes(){ return this.notes; }

    public int getProgress() { return progress; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setProgress(int progress) { this.progress = progress; }

    public void setAssignmentID(int assignmentID) { this.assignmentID = assignmentID; }

    public void setAssID(int assignmentID) { this.assignmentID = assignmentID; }

    public void setCourseID(String courseID) { this.courseID = courseID; }

    public void setTitle(String title) { this.title = title; }

    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public void setNotes(String notes) { this.notes = notes; }

    public boolean isPastDueDate(){
        Date due = new Date();
        try{
            due = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dueDate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        Date curr = new Date();
        try{
            if(curr.compareTo(due) > 0)
                return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getAssignmentID() == ((Assignment)obj).getAssignmentID();
    }
}
