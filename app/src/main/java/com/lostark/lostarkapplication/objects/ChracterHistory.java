package com.lostark.lostarkapplication.objects;

public class ChracterHistory implements Comparable<ChracterHistory>{
    private String name, server, job;
    private int level, dungeon, boss, quest;

    public ChracterHistory(String name, String server, String job, int level, int dungeon, int boss, int quest) {
        this.name = name;
        this.server = server;
        this.job = job;
        this.level = level;
        this.dungeon = dungeon;
        this.boss = boss;
        this.quest = quest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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

    public int getDungeon() {
        return dungeon;
    }

    public void setDungeon(int dungeon) {
        this.dungeon = dungeon;
    }

    public int getBoss() {
        return boss;
    }

    public void setBoss(int boss) {
        this.boss = boss;
    }

    public int getQuest() {
        return quest;
    }

    public void setQuest(int quest) {
        this.quest = quest;
    }

    @Override
    public int compareTo(ChracterHistory o) {
        if (level < o.getLevel()) return 1;
        else if (level == o.getLevel()) return 0;
        else return -1;
    }
}
