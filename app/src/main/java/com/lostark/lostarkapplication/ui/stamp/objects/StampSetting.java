package com.lostark.lostarkapplication.ui.stamp.objects;

public class StampSetting {
    private String name, image;
    private boolean isActivate;

    public StampSetting(String name, String image, boolean isActivate) {
        this.name = name;
        this.image = image;
        this.isActivate = isActivate;
    }

    public void toggle() {
        if (isActivate) isActivate = false;
        else isActivate = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }
}
