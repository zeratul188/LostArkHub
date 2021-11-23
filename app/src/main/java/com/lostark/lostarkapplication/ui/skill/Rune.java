package com.lostark.lostarkapplication.ui.skill;

public class Rune {
    private String name, content, url;
    private int grade, index, count;

    public Rune(String name, String content, String url, int grade, int index, int count) {
        this.name = name;
        this.content = content;
        this.url = url;
        this.grade = grade;
        this.index = index;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
