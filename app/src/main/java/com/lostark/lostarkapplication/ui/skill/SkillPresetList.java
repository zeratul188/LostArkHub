package com.lostark.lostarkapplication.ui.skill;

public class SkillPresetList {
    private String name, job;
    private int id, skillpoint, max;

    public SkillPresetList(String name, String job, int id, int skillpoint, int max) {
        this.name = name;
        this.job = job;
        this.id = id;
        this.skillpoint = skillpoint;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSkillpoint() {
        return skillpoint;
    }

    public void setSkillpoint(int skillpoint) {
        this.skillpoint = skillpoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
