package com.example.lostarkapplication.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostarkapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private TextView txtStartDungeon, txtEndDungeon, txtFirstDungeon, txtSecondDungeon;
    private ImageView imgDungeon;

    private ListView listUpdate;
    private ArrayList<Update> updates;
    private ArrayList<String> update_dates;
    private ArrayAdapter updateAdapter;

    private RecyclerView listEvent;
    private EventListAdapter eventAdapter;
    private ArrayList<Event> events;

    private TextView txtAlarm;

    private FirebaseDatabase mDatabase;
    private DatabaseReference islandReference, bossReference, dungeonReference, updateReference, eventReference, andReference;

    @Override
    public void onResume() {
        super.onResume();
        islandReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String startdate = "";
                for (DataSnapshot data :snapshot.getChildren()) {
                    if (data.getKey().equals("startdate")) startdate = data.getValue().toString();
                    else if (data.getKey().equals("enddate")) continue;
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
                txtIslandDate.setText(startdate+"에 시작");
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

        dungeonReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> dungeons = Arrays.asList(getActivity().getResources().getStringArray(R.array.dungeon));
                String date = "";
                int recycle = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("date")) date = data.getValue().toString();
                    else recycle = Integer.parseInt(data.getValue().toString());
                }
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = null;
                try {
                    dt = fm.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                if (dt != null) calendar.setTime(dt);
                DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
                DateFormat input_df = new SimpleDateFormat("yyyy-MM-dd");

                Date before = calendar.getTime();
                calendar.add(Calendar.DATE, 14);
                Date after = calendar.getTime();
                Date now = new Date();

                int compare = now.compareTo(after);
                if (compare >= 0) {
                    if (recycle == 3) recycle = 1;
                    else recycle++;
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("date", input_df.format(after));
                    taskMap.put("recycle", recycle);
                    dungeonReference.updateChildren(taskMap);
                    txtStartDungeon.setText(df.format(after));
                    calendar.add(Calendar.DATE, 14);
                    txtEndDungeon.setText(df.format(calendar.getTime()));
                } else {
                    txtStartDungeon.setText(df.format(before));
                    txtEndDungeon.setText(df.format(after));
                }

                switch (recycle) {
                    case 1:
                        imgDungeon.setImageResource(R.drawable.dg1);
                        txtFirstDungeon.setText(dungeons.get(0));
                        txtSecondDungeon.setText(dungeons.get(1));
                        break;
                    case 2:
                        imgDungeon.setImageResource(R.drawable.dg2);
                        txtFirstDungeon.setText(dungeons.get(2));
                        txtSecondDungeon.setText(dungeons.get(3));
                        break;
                    case 3:
                        imgDungeon.setImageResource(R.drawable.dg3);
                        txtFirstDungeon.setText(dungeons.get(4));
                        txtSecondDungeon.setText(dungeons.get(5));
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updates.clear();
                update_dates.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String date = data.child("date").getValue().toString();
                    String url = data.child("url").getValue().toString();
                    updates.add(new Update(date, url));
                    update_dates.add(date+" 업데이트 내역");
                }
                Collections.reverse(update_dates);
                Collections.reverse(updates);
                updateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String startdate = data.child("startdate").getValue().toString();
                    String enddate = data.child("enddate").getValue().toString();
                    int number = Integer.parseInt(data.child("number").getValue().toString());
                    String url = data.child("url").getValue().toString();
                    events.add(new Event(number, startdate, enddate, url));
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        andReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String alarm = data.getValue().toString();
                    txtAlarm.setText(alarm);
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

        txtStartDungeon = root.findViewById(R.id.txtStartDungeon);
        txtEndDungeon = root.findViewById(R.id.txtEndDungeon);
        txtFirstDungeon = root.findViewById(R.id.txtFirstDungeon);
        txtSecondDungeon = root.findViewById(R.id.txtSecondDungeon);
        imgDungeon = root.findViewById(R.id.imgDungeon);
        imgDungeon.setBackground(round_drawable);
        imgDungeon.setClipToOutline(true);

        listUpdate = root.findViewById(R.id.listUpdate);
        updates = new ArrayList<>();
        update_dates = new ArrayList<>();
        updateAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, update_dates){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };
        listUpdate.setAdapter(updateAdapter);
        listUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updates.get(position).getUrl())));
            }
        });

        listEvent = root.findViewById(R.id.listEvent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listEvent.setLayoutManager(layoutManager);
        events = new ArrayList<>();
        eventAdapter = new EventListAdapter(events, getActivity());
        listEvent.setAdapter(eventAdapter);
        EventDecoration decoration = new EventDecoration();
        listEvent.addItemDecoration(decoration);

        txtAlarm = root.findViewById(R.id.txtAlarm);

        mDatabase = FirebaseDatabase.getInstance();
        islandReference =mDatabase.getReference("island");
        bossReference = mDatabase.getReference("boss");
        dungeonReference = mDatabase.getReference("dungeon");
        updateReference = mDatabase.getReference("update");
        eventReference = mDatabase.getReference("event");
        andReference = mDatabase.getReference("Andsoon");

        return root;
    }


}
