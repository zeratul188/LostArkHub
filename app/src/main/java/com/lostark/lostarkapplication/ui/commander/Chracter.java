package com.lostark.lostarkapplication.ui.commander;

public class Chracter implements Comparable<Chracter> {
    private String name, job, server;
    private int level, favorite;
    private boolean isAlarm;

    public Chracter(String name, String job, String server, int level, int favorite, boolean isAlarm) {
        this.name = name;
        this.job = job;
        this.server = server;
        this.level = level;
        this.favorite = favorite;
        this.isAlarm = isAlarm;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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

    @Override
    public int compareTo(Chracter o) {
        if (favorite < o.getFavorite()) return 1;
        else if (favorite == o.getFavorite()) {
            if (level < o.getLevel()) return 1;
            else if (level == o.getLevel()) return 0;
            else return -1;
        } else return -1;
    }
}
