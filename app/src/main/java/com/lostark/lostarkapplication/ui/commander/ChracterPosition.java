package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.Nullable;

public class ChracterPosition implements Comparable<ChracterPosition> {
    private String name;
    private int level;

    public ChracterPosition(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int compareTo(ChracterPosition o) {
        if (level < o.getLevel()) return 1;
        else if (level == o.getLevel()) return 0;
        else return -1;
    }
}
