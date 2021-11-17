package com.lostark.lostarkapplication.ui.skill;

public class SkillPreset {
    private String name;
    private int now, rune;
    private int[] tripods;

    public SkillPreset(String name, int now, int rune, int[] tripods) {
        this.name = name;
        this.now = now;
        this.rune = rune;
        this.tripods = tripods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public int getRune() {
        return rune;
    }

    public void setRune(int rune) {
        this.rune = rune;
    }

    public int[] getTripods() {
        return tripods;
    }

    public void setTripods(int[] tripods) {
        this.tripods = tripods;
    }
}
