package com.lostark.lostarkapplication.ui.home.objects;

import com.lostark.lostarkapplication.ui.commander.Chracter;

public class Homework implements Comparable<Homework> {
    private String name, job, server;
    private int level, dungeon, boss, quest, max, now;

    public Homework(String name, String job, String server, int level, int dungeon, int boss, int quest, int now, int max) {
        this.name = name;
        this.job = job;
        this.server = server;
        this.level = level;
        this.dungeon = dungeon;
        this.boss = boss;
        this.quest = quest;
        this.max = max;
        this.now = now;
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    @Override
    public int compareTo(Homework o) {
        if (level < o.getLevel()) return 1;
        else if (level == o.getLevel()) return 0;
        else return -1;
    }
}
