package com.lostark.lostarkapplication.objects;

public class Report {
    private long number;
    private String date, version, device, os, content, id;
    private boolean isRead, isAnswer;

    public Report(long number, String date, String version, String device, String os, String content, boolean isRead, String id, boolean isAnswer) {
        this.number = number;
        this.date = date;
        this.version = version;
        this.device = device;
        this.os = os;
        this.content = content;
        this.isRead = isRead;
        this.id = id;
        this.isAnswer = isAnswer;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
