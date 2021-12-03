package com.lostark.lostarkapplication.ui.home;

import java.util.Comparator;

public class UpdateComparator implements Comparator<Update> {
    @Override
    public int compare(Update o1, Update o2) {
        if (o1.getYear() <= o2.getYear()) {
            if (o1.getMonth() <= o2.getMonth()) {
                if (o1.getDay() < o2.getDay()) return 1;
                else if (o1.getDay() == o2.getDay()) return 0;
                else return -1;
            } else return -1;
        } else return -1;
    }
}
