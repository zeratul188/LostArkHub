package com.lostark.lostarkapplication.ui.commander;

public class Chracter {
    private String name, job;
    private int level;
    private boolean isAlarm;

    public Chracter(String name, String job, int level, boolean isAlarm) {
        this.name = name;
        this.job = job;
        this.level = level;
        this.isAlarm = isAlarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }
}
