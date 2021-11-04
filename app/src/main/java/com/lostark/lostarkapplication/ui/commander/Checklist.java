package com.lostark.lostarkapplication.ui.commander;

public class Checklist {
    private String name, type;
    private int now, max;
    private boolean isAlarm;

    public Checklist(String name, String type, int now, int max, boolean isAlarm) {
        this.name = name;
        this.type = type;
        this.now = now;
        this.max = max;
        this.isAlarm = isAlarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }
}
