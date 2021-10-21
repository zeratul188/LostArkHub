package com.example.lostarkapplication.ui.home;

public class Island {
    private String name, award, image;

    public Island(String name, String award, String image) {
        this.name = name;
        this.award = award;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
