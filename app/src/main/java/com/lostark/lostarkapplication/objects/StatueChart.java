package com.lostark.lostarkapplication.objects;

public class StatueChart {
    private String title;
    private int value;

    public StatueChart(String title, int value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
