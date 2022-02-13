package com.lostark.lostarkapplication.ui.gallery;

public class Stone {
    private int rowID;
    private String grade, history;
    private String[] stamp = new String[3];
    private int[] cnt = new int[3];

    public Stone(String grade, String[] stamp, int[] cnt, String history, int rowID) {
        this.grade = grade;
        this.stamp = stamp;
        this.cnt = cnt;
        this.history = history;
        this.rowID = rowID;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
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
