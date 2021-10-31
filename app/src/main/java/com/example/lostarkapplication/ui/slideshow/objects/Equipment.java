package com.example.lostarkapplication.ui.slideshow.objects;

public class Equipment {
    private String name, date;
    private int index;

    public Equipment(int index, String name, String date) {
        this.index = index;
        this.name = name;
        this.date = date;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
