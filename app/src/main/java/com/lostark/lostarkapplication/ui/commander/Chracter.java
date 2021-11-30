package com.lostark.lostarkapplication.ui.commander;

public class Chracter {
    private String name, job, server;
    private int level;
    private boolean isAlarm;

    public Chracter(String name, String job, String server, int level, boolean isAlarm) {
        this.name = name;
        this.job = job;
        this.server = server;
        this.level = level;
        this.isAlarm = isAlarm;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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
