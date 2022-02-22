package com.lostark.lostarkapplication.ui.gallery;

public class Preset {
    private int id;
    private String name, grade;
    private String[] stamps;

    public Preset(int id, String name, String grade, String[] stamps) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.stamps = stamps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String[] getStamps() {
        return stamps;
    }

    public void setStamps(String[] stamps) {
        this.stamps = stamps;
    }
}
