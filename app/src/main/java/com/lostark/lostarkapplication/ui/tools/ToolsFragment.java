package com.lostark.lostarkapplication.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class ToolsFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private ViewPager layoutFrame;
    private ToolsViewModel toolsViewModel;

    private BingoFragment bingoFragment;
    private ArrayList<Fragment> fragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ToolsPageViewAdapter toolsPageViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolsViewModel = ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        bottomNavigationView = root.findViewById(R.id.bottomNavigationView);
        layoutFrame = root.findViewById(R.id.layoutFrame);

        fragmentManager = getChildFragmentManager();
        bingoFragment = new BingoFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragments = new ArrayList<>();
        fragments.add(bingoFragment);
        toolsPageViewAdapter = new ToolsPageViewAdapter(fragmentManager, fragments);
        layoutFrame.setAdapter(toolsPageViewAdapter);
        layoutFrame.setOffscreenPageLimit(fragments.size());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bingo:
                        //setFrag(0);
                        layoutFrame.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });

        return root;
    }
}
