package com.lostark.lostarkapplication.ui.skill;

public class Rune {
    private String name, content, url;
    private int grade;

    public Rune(String name, String content, String url, int grade) {
        this.name = name;
        this.content = content;
        this.url = url;
        this.grade = grade;
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
