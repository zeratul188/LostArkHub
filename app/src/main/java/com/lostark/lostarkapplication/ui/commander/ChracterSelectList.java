package com.lostark.lostarkapplication.ui.commander;

public class ChracterSelectList implements Comparable<ChracterSelectList> {
    private String name, job, server;
    private int level;

    public ChracterSelectList(String name, String job, String server, int level) {
        this.name = name;
        this.job = job;
        this.server = server;
        this.level = level;
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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int compareTo(ChracterSelectList o) {
        if (level < o.getLevel()) return 1;
        else if (level == o.getLevel()) return 0;
        else return -1;
    }
}
