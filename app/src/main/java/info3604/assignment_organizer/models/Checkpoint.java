package info3604.assignment_organizer.models;

import android.icu.text.SimpleDateFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class Checkpoint implements Serializable {

    private int checkpointID;

    private int assignmentID;

    private String title;

    private String startDate;

    private String dueDate;

    private String notes;

    private int progress;

    public Checkpoint(int assignmentID, String title, String dueDate, String notes){
        this.assignmentID = assignmentID;
        this.title = title;
        this.dueDate = dueDate;
        this.notes = notes;
        this.progress = 0;
        this.startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
    }

    public Checkpoint(int checkpointID) { }

    public int getCheckpointID() { return checkpointID; }

    public int getAssignmentID() { return assignmentID; }

    public String getTitle() { return title; }

    public String getDueDate() { return dueDate; }

    public String getNotes() { return notes; }

    public int getProgress() { return progress; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setProgress(int progress) { this.progress = progress; }

    public void setCheckID(int checkpointID) { this.checkpointID = checkpointID; }

    public void setAssignmentID(int assignmentID) { this.assignmentID = assignmentID; }

    public void setTitle(String title) { this.title = title; }

    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object obj) {
        return this.getCheckpointID() == ((Checkpoint)obj).getCheckpointID();
    }
}