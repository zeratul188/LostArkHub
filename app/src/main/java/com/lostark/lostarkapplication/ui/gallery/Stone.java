package com.lostark.lostarkapplication.ui.gallery;

public class Stone {
    private String grade, history;
    private String[] stamp = new String[3];
    private int[] cnt = new int[3];

    public Stone(String grade, String[] stamp, int[] cnt, String history) {
        this.grade = grade;
        this.stamp = stamp;
        this.cnt = cnt;
        this.history = history;
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

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
