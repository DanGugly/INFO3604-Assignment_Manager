package info3604.assignment_organizer.models;

import java.io.Serializable;
import java.util.Date;

public class Checkpoint implements Serializable {

    private String checkID;

    private String assID;

    private String title;

    private Date expected;

    private String notes;

    public Checkpoint(String assID, String title, Date expected, String notes){
        this.assID = assID;
        this.title = title;
        this.expected = expected;
        this.notes = notes;
    }

    public String getCheckID() { return checkID; }

    public String getAssID() { return assID; }

    public String getTitle() { return title; }

    public Date getExpected() { return expected; }

    public String getNotes() { return notes; }

    public void setCheckID(String checkID) { this.checkID = checkID; }

    public void setAssID(String assID) { this.assID = assID; }

    public void setTitle(String title) { this.title = title; }

    public void setExpected(Date expected) { this.expected = expected; }

    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object obj) {
        return this.getCheckID().equals(((Checkpoint)obj).getCheckID());
    }
}
