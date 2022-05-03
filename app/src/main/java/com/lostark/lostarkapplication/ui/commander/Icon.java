package com.lostark.lostarkapplication.ui.commander;

public class Icon {
    private int index;
    private boolean isSelect;

    public Icon(int index) {
        this.index = index;
        isSelect = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
