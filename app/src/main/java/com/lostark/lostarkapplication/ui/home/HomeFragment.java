package com.lostark.lostarkapplication.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lostark.lostarkapplication.AlertReceiver;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.IslandAwardToast;
import com.lostark.lostarkapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.WebActivity;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;

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
    private ScrollView scrollView;

    private DisplayImageView[] imgIsland = new DisplayImageView[ISLAND_LENGTH];
    private TextView[] txtIsland = new TextView[ISLAND_LENGTH];
    private SquareImageView[][] imgIslandAwards = new SquareImageView[3][8];
    private LinearLayout[] layoutIsland = new LinearLayout[3];
    private TextView txtIslandDate;
    private String[][] islandAwards = new String[3][8];
    private int[][] islandAwardsImages = new int[3][8];
    private TextView txtTime, txtEventCount;
    public static Switch swtIslandAlarm;
    private Handler handler;

    private DisplayImageView[] imgBoss = new DisplayImageView[BOSS_LENGTH];
    private TextView[] txtBoss = new TextView[BOSS_LENGTH];
    private TextView[] txtStartBoss = new TextView[BOSS_LENGTH];
    private TextView[] txtEndBoss = new TextView[BOSS_LENGTH];

    private TextView txtStartDungeon, txtEndDungeon, txtFirstDungeon, txtSecondDungeon;
    private DungeonDisplayImageView imgDungeon;

    private TimerThread timer;

    private ListView listUpdate;
    private ArrayList<Update> updates;
    private ArrayList<String> update_dates;
    private ArrayAdapter updateAdapter;

    private RecyclerView listEvent;
    private EventListAdapter eventAdapter;
    private ArrayList<Event> events;

    private TextView txtAlarm;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference islandReference, bossReference, dungeonReference, updateReference, eventReference, andReference;

    private CustomToast customToast;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    public void onResume() {
        super.onResume();
        islandReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timer.interrupt();
                swtIslandAlarm.setChecked(pref.getBoolean("island_alarm", false));
                setIslandAlarm(swtIslandAlarm.isChecked());
                String startdate = "";
                List<String> list = Arrays.asList(getResources().getStringArray(R.array.awards));
                for (DataSnapshot data :snapshot.getChildren()) {
                    String name = data.child("name").getValue().toString();
                    String award = data.child("award").getValue().toString();
                    String image = data.child("image").getValue().toString();
                    int index = 0;
                    if (data.getKey().equals("first_island")) index = 0;
                    else if (data.getKey().equals("second_island")) index = 1;
                    else index = 2;
                    imgIsland[index].setImageResource(getActivity().getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
                    txtIsland[index].setText(name);
                    String[] awards = award.split("\\|");
                    if (awards.length > 4) layoutIsland[index].setVisibility(View.VISIBLE);
                    else layoutIsland[index].setVisibility(View.GONE);
                    int position = 0;
                    String arr = "";
                    for (int i = 0; i < awards.length; i++) {
                        arr += awards[i]+"   ";
                        if (list.indexOf(awards[i]) != -1) {
                            islandAwards[index][position] = awards[i];
                            imgIslandAwards[index][position].setImageResource(getActivity().getResources().getIdentifier("ii"+(list.indexOf(awards[i])+1), "drawable", getActivity().getPackageName()));
                            islandAwardsImages[index][position] = getActivity().getResources().getIdentifier("ii"+(list.indexOf(awards[i])+1), "drawable", getActivity().getPackageName());
                            position++;
                            /*storageRef.child("IslandAwards/ii"+(list.indexOf(awards[i])+1)+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        Glide.with(getActivity()).load(uri).into(imgIslandAwards[now_index][now_position]);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                        imgIslandAwards[now_index][now_position].setImageResource(R.drawable.close_eye);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    imgIslandAwards[now_index][now_position].setImageResource(R.drawable.close_eye);
                                }
                            });*/
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                for (ImageView imgView : imgIsland) imgView.setImageResource(R.drawable.noemptyboss);
            }
        });
        Calendar calendar = Calendar.getInstance();
        //Test Start
        //calendar.set(Calendar.DAY_OF_MONTH, 4);
        //calendar.set(Calendar.DAY_OF_MONTH, 14);
        //calendar.set(Calendar.HOUR_OF_DAY, 23);
        //Test End
        Date now = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
        DateFormat hourFormat = new SimpleDateFormat("HH");
        int now_hour = Integer.parseInt(hourFormat.format(now));
        int next_island_hour = 0;
        if (now_hour < 9) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) next_island_hour = 9;
            else next_island_hour = 11;
        }
        else if (now_hour >= 9 && now_hour < 11) next_island_hour = 11;
        else if (now_hour >= 11 && now_hour < 13) next_island_hour = 13;
        else if (now_hour >= 13 && now_hour < 19) next_island_hour = 19;
        else if (now_hour >= 19 && now_hour < 21) next_island_hour = 21;
        else if (now_hour >= 21 && now_hour < 23) next_island_hour = 23;
        else {
            calendar.add(Calendar.DATE,1);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) next_island_hour = 9;
            else next_island_hour = 11;
        }
        calendar.set(Calendar.HOUR_OF_DAY, next_island_hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date result_date = calendar.getTime();
        String result = df.format(result_date);
        result += " "+next_island_hour+"시에 시작";
        txtIslandDate.setText(result);

        Calendar now_calendar = Calendar.getInstance();

        long time = calendar.getTime().getTime() - now_calendar.getTime().getTime();
        long seconds = time / 1000;
        seconds = Math.abs(seconds);

        timer.setTime(seconds);
        if (timer.getState() == Thread.State.NEW) {
            timer.start();
        }

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
                }
                Collections.sort(updates);
                for (Update update : updates) {
                    update_dates.add(update.getDate()+" 업데이트 내역");
                }
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
                int cnt = (int)snapshot.getChildrenCount();
                txtEventCount.setText(cnt+"개");
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

        scrollView = root.findViewById(R.id.scrollView);

        handler = new Handler();
        pref = getActivity().getSharedPreferences("island_file", MODE_PRIVATE);
        editor = pref.edit();

        txtIslandDate = root.findViewById(R.id.txtIslandDate);
        txtTime = root.findViewById(R.id.txtTime);
        swtIslandAlarm = root.findViewById(R.id.swtIslandAlarm);
        txtEventCount = root.findViewById(R.id.txtEventCount);
        for (int i = 0; i < ISLAND_LENGTH; i++) {
            imgIsland[i] = root.findViewById(getActivity().getResources().getIdentifier("imgIsland"+(i+1), "id", getActivity().getPackageName()));
            txtIsland[i] = root.findViewById(getActivity().getResources().getIdentifier("txtIsland"+(i+1), "id", getActivity().getPackageName()));
            //txtIslandAward[i] = root.findViewById(getActivity().getResources().getIdentifier("txtIslandAward"+(i+1), "id", getActivity().getPackageName()));
            //imgIsland[i].setBackground(round_drawable);
            //imgIsland[i].setClipToOutline(true);
            layoutIsland[i] = root.findViewById(getActivity().getResources().getIdentifier("layoutIsland"+(i+1), "id", getActivity().getPackageName()));
            for (int j = 0; j < imgIslandAwards[i].length; j++) {
                imgIslandAwards[i][j] = root.findViewById(getActivity().getResources().getIdentifier("imgIsland"+(i+1)+"_"+(j+1), "id", getActivity().getPackageName()));
                final int x = i;
                final int y = j;
                imgIslandAwards[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (islandAwards[x][y] == null) return;
                        //Toast.makeText(getActivity(), "보상 내용 : "+islandAwards[x][y], Toast.LENGTH_SHORT).show();
                        IslandAwardToast islandAwardToast = new IslandAwardToast(getActivity());
                        islandAwardToast.createToast(islandAwards[x][y], Toast.LENGTH_SHORT, islandAwardsImages[x][y]);
                        islandAwardToast.show();
                    }
                });
            }
        }

        swtIslandAlarm.setChecked(pref.getBoolean("island_alarm", false));
        swtIslandAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setIslandAlarm(isChecked);
                editor.putBoolean("island_alarm", isChecked);
                editor.commit();
            }
        });

        for (int i = 0; i < BOSS_LENGTH; i++) {
            imgBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("imgBoss"+(i+1), "id", getActivity().getPackageName()));
            txtBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("txtBoss"+(i+1), "id", getActivity().getPackageName()));
            txtStartBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("txtStartBoss"+(i+1), "id", getActivity().getPackageName()));
            txtEndBoss[i] = root.findViewById(getActivity().getResources().getIdentifier("txtEndBoss"+(i+1), "id", getActivity().getPackageName()));
            //imgBoss[i].setBackground(round_drawable);
            //imgBoss[i].setClipToOutline(true);
        }

        txtStartDungeon = root.findViewById(R.id.txtStartDungeon);
        txtEndDungeon = root.findViewById(R.id.txtEndDungeon);
        txtFirstDungeon = root.findViewById(R.id.txtFirstDungeon);
        txtSecondDungeon = root.findViewById(R.id.txtSecondDungeon);
        imgDungeon = root.findViewById(R.id.imgDungeon);
        //imgDungeon.setBackground(round_drawable);
        //imgDungeon.setClipToOutline(true);

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
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updates.get(position).getUrl())));
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("url", updates.get(position).getUrl()));
            }
        });
        listUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
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

        storage = FirebaseStorage.getInstance("gs://lostarkhub-cbe60.appspot.com/");
        storageRef = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance();
        islandReference = mDatabase.getReference("island");
        bossReference = mDatabase.getReference("boss");
        dungeonReference = mDatabase.getReference("dungeon");
        updateReference = mDatabase.getReference("update");
        eventReference = mDatabase.getReference("event");
        andReference = mDatabase.getReference("Andsoon");

        customToast = new CustomToast(getActivity());
        timer = new TimerThread(txtTime, handler);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.interrupt();
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), IslandAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 2, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), IslandAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 2, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void setIslandAlarm(boolean isEnable) {
        cancelAlarm();
        if (isEnable) {
            Calendar calendar = Calendar.getInstance();
            //Test Start
            //calendar.set(Calendar.DAY_OF_MONTH, 4);
            //calendar.set(Calendar.DAY_OF_MONTH, 14);
            //calendar.set(Calendar.HOUR_OF_DAY, 23);
            //Test End
            Date now = calendar.getTime();
            DateFormat hourFormat = new SimpleDateFormat("HH");
            int now_hour = Integer.parseInt(hourFormat.format(now));
            int next_island_hour = 0;
            if (now_hour < 9) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) next_island_hour = 9;
                else next_island_hour = 11;
            }
            else if (now_hour >= 9 && now_hour < 11) next_island_hour = 11;
            else if (now_hour >= 11 && now_hour < 13) next_island_hour = 13;
            else if (now_hour >= 13 && now_hour < 19) next_island_hour = 19;
            else if (now_hour >= 19 && now_hour < 21) next_island_hour = 21;
            else if (now_hour >= 21 && now_hour < 23) next_island_hour = 23;
            else {
                calendar.add(Calendar.DATE,1);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) next_island_hour = 9;
                else next_island_hour = 11;
            }
            calendar.set(Calendar.HOUR_OF_DAY, next_island_hour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.MINUTE, -5);

            /*Calendar test = Calendar.getInstance();
            test.add(Calendar.SECOND, 5);
            startAlarm(test);*/

            startAlarm(calendar);
        }
    }

    public static void offSwitchAlarm() {
        swtIslandAlarm.setChecked(false);
    }
}
