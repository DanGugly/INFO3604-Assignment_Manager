package info3604.assignment_organizer.models;

import java.io.Serializable;
import java.util.Date;

public class Checkpoint implements Serializable {

    private int checkpointID;

    private int assignmentID;

    private String title;

    private String dueDate;

    private String notes;

    public Checkpoint(int assignmentID, String title, String dueDate, String notes){
        this.assignmentID = assignmentID;
        this.title = title;
        this.dueDate = dueDate;
        this.notes = notes;
    }

    public int getCheckpointID() { return checkpointID; }

    public int getAssignmentID() { return assignmentID; }

    public String getTitle() { return title; }

    public String getDueDate() { return dueDate; }

    public String getNotes() { return notes; }

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
