package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EmptyPageViewAdapter extends FragmentPagerAdapter {
    private int size = 0;

    public EmptyPageViewAdapter(@NonNull FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new Fragment();
    }

    @Override
    public int getCount() {
        return size;
    }
}
