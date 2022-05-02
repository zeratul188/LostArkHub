package com.lostark.lostarkapplication.ui.guild;

public class Guild implements Comparable<Guild> {
    private String id, name, boss, condition, solution, content, date, link, server;
    private int level, min, index, number, statue;

    public Guild(int number, String id, String name, String boss, String condition, String solution, String content, String date, int level, int min, int index, int statue, String server) {
        this.number = number;
        this.id = id;
        this.name = name;
        this.boss = boss;
        this.condition = condition;
        this.solution = solution;
        this.content = content;
        this.date = date;
        this.level = level;
        this.min = min;
        this.index = index;
        this.statue = statue;
        this.server = server;
        this.link = "none";
    }

    public Guild(int number, String id, String name, String boss, String condition, String solution, String content, String date, String link, int level, int min, int index, int statue, String server) {
        this.number = number;
        this.id = id;
        this.name = name;
        this.boss = boss;
        this.condition = condition;
        this.solution = solution;
        this.content = content;
        this.date = date;
        this.link = link;
        this.level = level;
        this.min = min;
        this.index = index;
        this.statue = statue;
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Guild o) {
        if (index < o.index) {
            return 1;
        } else if (index == o.index) {
            return 0;
        } else {
            return -1;
        }
    }
}
