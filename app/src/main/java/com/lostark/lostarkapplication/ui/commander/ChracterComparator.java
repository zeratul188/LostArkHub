package com.lostark.lostarkapplication.ui.commander;

import java.util.Comparator;

public class ChracterComparator implements Comparator<Chracter> {
    @Override
    public int compare(Chracter chracter, Chracter t1) {
        if (chracter.getLevel() < t1.getLevel()) return 1;
        else if (chracter.getLevel() == t1.getLevel()) return 0;
        else return -1;
    }
}
