package com.lostark.lostarkapplication.ui.stamp.objects;

public class StampList {
    private String name, image, type;
    private String[] contents = new String[3];
    private boolean isOpen;

    public StampList(String name, String image, String type, String[] contents) {
        this.name = name;
        this.image = image;
        this.type = type;
        this.contents = contents;
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }
}
