package com.example.lostarkapplication.ui.slideshow.objects;

public class Accessory {
    private String grade;
    private String[] stamp = new String[3];
    private int[] cnt = new int[3];

    public Accessory(String grade, String[] stamp, int[] cnt) {
        this.grade = grade;
        this.stamp = stamp;
        this.cnt = cnt;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String[] getStamp() {
        return stamp;
    }

    public void setStamp(String[] stamp) {
        this.stamp = stamp;
    }

    public int[] getCnt() {
        return cnt;
    }

    public void setCnt(int[] cnt) {
        this.cnt = cnt;
    }
}
