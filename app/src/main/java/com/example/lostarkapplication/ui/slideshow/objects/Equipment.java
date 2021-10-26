package com.example.lostarkapplication.ui.slideshow.objects;

public class Equipment {
    private String grade;
    private String[] stamps = new String[3];
    private int[] cnts = new int[3];

    public Equipment(String grade, String[] stamps, int[] cnts) {
        this.grade = grade;
        this.stamps = stamps;
        this.cnts = cnts;
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

    public int[] getCnts() {
        return cnts;
    }

    public void setCnts(int[] cnts) {
        this.cnts = cnts;
    }
}
