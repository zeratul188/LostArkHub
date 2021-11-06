package com.lostark.lostarkapplication.ui.stamp.objects;

public class StampChecklist {
    private boolean isIncrease, isDecrease;
    private boolean[] isClasses = new boolean[21];

    public StampChecklist() {
        isIncrease = false;
        isDecrease = false;
        for (int i = 0; i < isClasses.length; i++) isClasses[i] = false;
    }

    public boolean isIncrease() {
        return isIncrease;
    }

    public void setIncrease(boolean increase) {
        isIncrease = increase;
    }

    public boolean isDecrease() {
        return isDecrease;
    }

    public void setDecrease(boolean decrease) {
        isDecrease = decrease;
    }

    public void setClass(int position, boolean result) {
        isClasses[position] = result;
    }

    public boolean[] getIsClasses() {
        return isClasses;
    }

    public void setIsClasses(boolean[] isClasses) {
        this.isClasses = isClasses;
    }

    public void allCheck(boolean flag) {
        isIncrease = flag;
        isDecrease = flag;
        for (int i = 0; i < isClasses.length; i++) isClasses[i] = flag;
    }
}
