package com.example.lostarkapplication.ui.home;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lostarkapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private static final int ISLAND_LENGTH = 3;
    private static final int BOSS_LENGTH = 3;

    private HomeViewModel homeViewModel;

    private ImageView[] imgIsland = new ImageView[ISLAND_LENGTH];
    private TextView[] txtIsland = new TextView[ISLAND_LENGTH];
    private TextView[] txtIslandAward = new TextView[ISLAND_LENGTH];
    private TextView txtIslandDate;

    private ImageView[] imgBoss = new ImageView[BOSS_LENGTH];
    private TextView[] txtBoss = new TextView[BOSS_LENGTH];
    private TextView[] txtStartBoss = new TextView[BOSS_LENGTH];
    private TextView[] txtEndBoss = new TextView[BOSS_LENGTH];

    private FirebaseDatabase mDatabase;
    private DatabaseReference islandReference, bossReference;

    @Override
    public void onResume() {
        super.onResume();
        islandReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String startdate = "", enddate = "";
                for (DataSnapshot data :snapshot.getChildren()) {
                    if (data.getKey().equals("startdate")) startdate = data.getValue().toString();
                    else if (data.getKey().equals("enddate")) enddate = data.getValue().toString();
                    else {
                        String name = data.child("name").getValue().toString();
                        String award = data.child("award").getValue().toString();
                        String image = data.child("image").getValue().toString();
                        int index = 0;
                        if (data.getKey().equals("first_island")) index = 0;
                        else if (data.getKey().equals("second_island")) index = 1;
                        else index = 2;
                        imgIsland[index].setImageResource(getActivity().getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
                        txtIsland[index].setText(name);
                        txtIslandAward[index].setText(award);
                    }
                }
                txtIslandDate.setText(startdate+" ~ "+enddate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bossReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue().toString();
                    String image = data.child("image").getValue().toString();
                    String startdate = data.child("startdate").getValue().toString();
                    String enddate = data.child("enddate").getValue().toString();
                    int index = 0;
                    if (data.getKey().equals("firstboss")) index = 0;
                    else if (data.getKey().equals("secondboss")) index = 1;
                    else index = 2;
                    imgBoss[index].setImageResource(getActivity().getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
                    txtBoss[index].setText(name);
                    txtStartBoss[index].setText(startdate);
                    txtEndBoss[index].setText(enddate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        GradientDrawable round_drawable = (GradientDrawable) getActivity().getDrawable(R.drawable.roundimage);

        txtIslandDate = root.findViewById(R.id.txtIslandDate);
        for (int i = 0; i < ISLAND_LENGTH; i++) {
            imgIsland[i] = root.findViewById(getActivity().getResources().getIdentifier("imgIsland"+(i+1), "id", getActivity().getPackageName()));
            txtIsland[i] = root.findViewById(getActivity().getResources().getIdentifier("txtIsland"+(i+1), "id", getActivity().getPackageName()));
            txtIslandAward[i] = root.findViewById(getActivity().getResources().getIdentifier("txtIslandAward"+(i+1), "id", getActivity().getPackageName()));
            imgIsland[i].setBackground(round_drawable);
            imgIsland[i].setClipToOutline(true);
        }

        for (int i = 0; i < BOSS_LENGTH; i++) {
            imgBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("imgBoss"+(i+1), "id", getActivity().getPackageName()));
            txtBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("txtBoss"+(i+1), "id", getActivity().getPackageName()));
            txtStartBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("txtStartBoss"+(i+1), "id", getActivity().getPackageName()));
            txtEndBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("txtEndBoss"+(i+1), "id", getActivity().getPackageName()));
            imgBoss[i].setBackground(round_drawable);
            imgBoss[i].setClipToOutline(true);
        }

        mDatabase = FirebaseDatabase.getInstance();
        islandReference =mDatabase.getReference("island");
        bossReference = mDatabase.getReference("boss");

        return root;
    }


}
