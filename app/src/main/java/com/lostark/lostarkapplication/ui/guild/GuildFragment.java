package com.lostark.lostarkapplication.ui.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lostark.lostarkapplication.R;

public class GuildFragment extends Fragment {
    private GuildViewModel guildViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        guildViewModel = ViewModelProviders.of(this).get(GuildViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);



        return root;
    }
}
