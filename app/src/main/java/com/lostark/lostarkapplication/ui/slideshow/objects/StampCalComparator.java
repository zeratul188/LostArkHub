package com.lostark.lostarkapplication.ui.slideshow.objects;

import java.util.Comparator;

public class StampCalComparator implements Comparator<StampCal> {
    @Override
    public int compare(StampCal stampCal, StampCal t1) {
        if (stampCal.getCnt() < t1.getCnt()) return 1;
        else if (stampCal.getCnt() == t1.getCnt()) return 0;
        else return -1;
    }
}
