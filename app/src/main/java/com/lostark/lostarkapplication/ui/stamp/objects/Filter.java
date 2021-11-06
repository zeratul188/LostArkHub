package com.lostark.lostarkapplication.ui.stamp.objects;

import android.widget.CheckBox;

public class Filter {
    private String filter;
    private CheckBox chkBox;
    private int index;

    public Filter(String filter, CheckBox chkBox, int index) {
        this.filter = filter;
        this.chkBox = chkBox;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public CheckBox getChkBox() {
        return chkBox;
    }

    public void setChkBox(CheckBox chkBox) {
        this.chkBox = chkBox;
    }
}
