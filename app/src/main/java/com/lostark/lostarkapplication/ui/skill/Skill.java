package com.lostark.lostarkapplication.ui.skill;

public class Skill {
    private String name, strike, attack_type, destroy_level, content, url;
    private int level, max_level, time;
    private int[] tripods;

    public Skill(String name, String strike, String attack_type, String destroy_level, String content, String url, int level, int max_level, int time, int[] tripods) {
        this.name = name;
        this.strike = strike;
        this.attack_type = attack_type;
        this.destroy_level = destroy_level;
        this.content = content;
        this.url = url;
        this.level = level;
        this.max_level = max_level;
        this.time = time;
        this.tripods = tripods;
    }

    public String getStrike() {
        return strike;
    }

    public void setStrike(String strike) {
        this.strike = strike;
    }

    public String getAttack_type() {
        return attack_type;
    }

    public void setAttack_type(String attack_type) {
        this.attack_type = attack_type;
    }

    public String getDestroy_level() {
        return destroy_level;
    }

    public void setDestroy_level(String destroy_level) {
        this.destroy_level = destroy_level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMax_level() {
        return max_level;
    }

    public void setMax_level(int max_level) {
        this.max_level = max_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[] getTripods() {
        return tripods;
    }

    /*public void setTripods(int[] tripods) {
        this.tripods = tripods;
    }*/

    public void setTripods(int[] checklist) {
        for (int i = 0; i < tripods.length; i++) {
            tripods[i] = checklist[i];
        }
    }
}
