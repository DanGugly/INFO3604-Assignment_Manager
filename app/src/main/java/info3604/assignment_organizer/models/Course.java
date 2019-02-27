package info3604.assignment_organizer.models;

import java.io.Serializable;

public class Course implements Serializable {

    private String code;

    private String name;

    private int credits;

    private int level;

    public Course(){ }

    public Course(String code, String name, int credits, int level){
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.level = level;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public int getCredits() { return credits; }

    public int getLevel() { return level; }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString(){ return code + " " + name + " " + credits + " " + level; }

    @Override
    public boolean equals(Object obj) {
        return this.getCode().equals(((Course)obj).getCode());
    }
}

