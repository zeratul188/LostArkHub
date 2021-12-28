package com.lostark.lostarkapplication.objects;

public class HomeworkStatue {
    private int number, value;

    public HomeworkStatue(int number, int value) {
        this.number = number;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
