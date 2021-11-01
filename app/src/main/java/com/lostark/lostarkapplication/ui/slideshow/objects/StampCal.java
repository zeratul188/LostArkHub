package com.lostark.lostarkapplication.ui.slideshow.objects;

public class StampCal {
    private String name;
    private int cnt;

    public StampCal(String name, int cnt) {
        this.name = name;
        this.cnt = cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
