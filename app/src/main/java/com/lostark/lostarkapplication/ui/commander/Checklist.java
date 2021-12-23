package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.Nullable;

public class Checklist {
    private String name, type, content;
    private int now, max, history;
    private boolean isAlarm;

    public Checklist(String name, String type, String content, int now, int max, boolean isAlarm, int history) {
        this.name = name;
        this.type = type;
        this.content = content;
        this.now = now;
        this.max = max;
        this.isAlarm = isAlarm;
        this.history = history;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public Checklist(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return name.equals(((Checklist)obj).getName());
    }
}
