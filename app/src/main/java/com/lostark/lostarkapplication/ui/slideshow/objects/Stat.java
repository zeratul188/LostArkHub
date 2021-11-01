package com.lostark.lostarkapplication.ui.slideshow.objects;

public class Stat {
    private String grade;
    private String[] stamp = new String[2];
    private int[] cnt = new int[2];

    public Stat(String grade, String[] stamp, int[] cnt) {
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
