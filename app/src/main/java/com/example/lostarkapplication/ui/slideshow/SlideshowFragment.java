package com.example.lostarkapplication.ui.slideshow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lostarkapplication.R;
import com.example.lostarkapplication.database.StampDBAdapter;
import com.example.lostarkapplication.ui.gallery.Stamp;
import com.example.lostarkapplication.ui.slideshow.objects.StampCal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {
    private static final int STAMP_LENGTH = 3;
    private static final int STAT_LENGTH = 2;

    private ListView listView;
    private ImageView imgNeck, imgEarring1, imgEarring2, imgRing1, imgRing2;
    private TextView txtNeck, txtEarring1, txtEarring2, txtRing1, txtRing2;
    private ImageView[] imgNecks = new ImageView[STAMP_LENGTH];
    private Spinner[] sprNecks = new Spinner[STAMP_LENGTH];
    private Spinner[] sprNeckCnts = new Spinner[STAMP_LENGTH];
    private ImageView[] imgEarring1s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprEarring1s = new Spinner[STAMP_LENGTH];
    private Spinner[] sprEarring1Cnts = new Spinner[STAMP_LENGTH];
    private ImageView[] imgEarring2s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprEarring2s = new Spinner[STAMP_LENGTH];
    private Spinner[] sprEarring2Cnts = new Spinner[STAMP_LENGTH];
    private ImageView[] imgRing1s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprRing1s = new Spinner[STAMP_LENGTH];
    private Spinner[] sprRing1Cnts = new Spinner[STAMP_LENGTH];
    private ImageView[] imgRing2s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprRing2s = new Spinner[STAMP_LENGTH];
    private Spinner[] sprRing2Cnts = new Spinner[STAMP_LENGTH];
    private ImageView[] imgStones = new ImageView[STAMP_LENGTH];
    private Spinner[] sprStones = new Spinner[STAMP_LENGTH];
    private Spinner[] sprStoneCnts = new Spinner[STAMP_LENGTH];
    private ImageView[] imgStats = new ImageView[STAT_LENGTH];
    private Spinner[] sprStats = new Spinner[STAT_LENGTH];
    private Spinner[] sprStatCnts = new Spinner[STAT_LENGTH];

    private int neck_index = 0, earring1_index = 0, earring2_index = 0, ring1_index = 0, ring2_index = 0;

    private StampDBAdapter stampDBAdapter;
    private ArrayList<Stamp> stamps;
    private StampCalAdapter stampCalAdapter;
    private ArrayList<StampCal> stampCals;

    private Map<String, Integer> stampMap;

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        listView = root.findViewById(R.id.listView);
        imgNeck = root.findViewById(R.id.imgNeck);
        imgEarring1 = root.findViewById(R.id.imgEarring1);
        imgEarring2 = root.findViewById(R.id.imgEarring2);
        imgRing1 = root.findViewById(R.id.imgRing1);
        imgRing2 = root.findViewById(R.id.imgRing2);
        txtNeck = root.findViewById(R.id.txtNeck);
        txtEarring1 = root.findViewById(R.id.txtEarring1);
        txtEarring2 = root.findViewById(R.id.txtEarring2);
        txtRing1 = root.findViewById(R.id.txtRing1);
        txtRing2 = root.findViewById(R.id.txtRing2);
        for (int i = 0; i < STAMP_LENGTH; i++) {
            imgNecks[i] = root.findViewById(getActivity().getResources().getIdentifier("imgNeck"+(i+1), "id", getActivity().getPackageName()));
            sprNecks[i] = root.findViewById(getActivity().getResources().getIdentifier("sprNeck"+(i+1), "id", getActivity().getPackageName()));
            imgEarring1s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgEarring1"+(i+1), "id", getActivity().getPackageName()));
            sprEarring1s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprEarring1"+(i+1), "id", getActivity().getPackageName()));
            imgEarring2s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgEarring2"+(i+1), "id", getActivity().getPackageName()));
            sprEarring2s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprEarring2"+(i+1), "id", getActivity().getPackageName()));
            imgRing1s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgRing1"+(i+1), "id", getActivity().getPackageName()));
            sprRing1s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprRing1"+(i+1), "id", getActivity().getPackageName()));
            imgRing2s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgRing2"+(i+1), "id", getActivity().getPackageName()));
            sprRing2s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprRing2"+(i+1), "id", getActivity().getPackageName()));
            imgStones[i] = root.findViewById(getActivity().getResources().getIdentifier("imgStone"+(i+1), "id", getActivity().getPackageName()));
            sprStones[i] = root.findViewById(getActivity().getResources().getIdentifier("sprStone"+(i+1), "id", getActivity().getPackageName()));
            sprNeckCnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprNeckCnt"+(i+1), "id", getActivity().getPackageName()));
            sprEarring1Cnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprEarring1Cnt"+(i+1), "id", getActivity().getPackageName()));
            sprEarring2Cnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprEarring2Cnt"+(i+1), "id", getActivity().getPackageName()));
            sprRing1Cnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprRing1Cnt"+(i+1), "id", getActivity().getPackageName()));
            sprRing2Cnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprRing2Cnt"+(i+1), "id", getActivity().getPackageName()));
            sprStoneCnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprStoneCnt"+(i+1), "id", getActivity().getPackageName()));
        }
        for (int i = 0; i < STAT_LENGTH; i++) {
            imgStats[i] = root.findViewById(getActivity().getResources().getIdentifier("imgStat"+(i+1), "id", getActivity().getPackageName()));
            sprStats[i] = root.findViewById(getActivity().getResources().getIdentifier("sprStat"+(i+1), "id", getActivity().getPackageName()));
            sprStatCnts[i] = root.findViewById(getActivity().getResources().getIdentifier("sprStatCnt"+(i+1), "id", getActivity().getPackageName()));
        }

        stampDBAdapter = new StampDBAdapter(getActivity());
        stamps = new ArrayList<>();
        for (int i = 0; i < 89; i++) {
            stamps.add(new Stamp(stampDBAdapter.readData(i)[0], stampDBAdapter.readData(i)[1]));
        }
        ArrayList<String> burfs = new ArrayList<>();
        ArrayList<String> deburfs = new ArrayList<>();
        burfs.add("없음");
        deburfs.add("없음");
        for (int i = 0; i < 85; i++) burfs.add(stampDBAdapter.readData(i)[0]);
        for (int i = 85; i < 89; i++) deburfs.add(stampDBAdapter.readData(i)[0]);
        ArrayAdapter<String> burf_adapter = new ArrayAdapter<String>(getActivity(), R.layout.stampitem, burfs);
        ArrayAdapter<String> deburf_adapter = new ArrayAdapter<String>(getActivity(), R.layout.stampitem, deburfs);
        burf_adapter.setDropDownViewResource(R.layout.stampitem);
        deburf_adapter.setDropDownViewResource(R.layout.stampitem);

        ArrayList<String> equipment_count = new ArrayList<>();
        ArrayList<String> stone_count = new ArrayList<>();
        ArrayList<String> stat_count = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (i <= 6) equipment_count.add(Integer.toString(i));
            stone_count.add(Integer.toString(i));
        }
        stat_count.add("3");
        stat_count.add("6");
        stat_count.add("9");
        stat_count.add("12");
        ArrayAdapter<String> equipment_adapter = new ArrayAdapter<>(getActivity(), R.layout.stampitem, equipment_count);
        ArrayAdapter<String> stone_adapter = new ArrayAdapter<>(getActivity(), R.layout.stampitem, stone_count);
        ArrayAdapter<String> stat_adapter = new ArrayAdapter<>(getActivity(), R.layout.stampitem, stat_count);
        equipment_adapter.setDropDownViewResource(R.layout.stampitem);
        stone_adapter.setDropDownViewResource(R.layout.stampitem);
        stat_adapter.setDropDownViewResource(R.layout.stampitem);

        stampCals = new ArrayList<>();
        stampCalAdapter = new StampCalAdapter(getActivity(), stampCals, getActivity());
        listView.setAdapter(stampCalAdapter);

        stampMap = new HashMap<>();

        for (int i = 0; i < 3; i++) {
            sprNeckCnts[i].setAdapter(equipment_adapter);
            sprEarring1Cnts[i].setAdapter(equipment_adapter);
            sprEarring2Cnts[i].setAdapter(equipment_adapter);
            sprRing1Cnts[i].setAdapter(equipment_adapter);
            sprRing2Cnts[i].setAdapter(equipment_adapter);
            sprStoneCnts[i].setAdapter(stone_adapter);
            if (i == 2) {
                sprStones[i].setAdapter(deburf_adapter);
                sprRing2s[i].setAdapter(deburf_adapter);
                sprRing1s[i].setAdapter(deburf_adapter);
                sprEarring1s[i].setAdapter(deburf_adapter);
                sprNecks[i].setAdapter(deburf_adapter);
                sprEarring2s[i].setAdapter(deburf_adapter);
            } else {
                sprStones[i].setAdapter(burf_adapter);
                sprRing2s[i].setAdapter(burf_adapter);
                sprRing1s[i].setAdapter(burf_adapter);
                sprEarring1s[i].setAdapter(burf_adapter);
                sprNecks[i].setAdapter(burf_adapter);
                sprEarring2s[i].setAdapter(burf_adapter);
            }
            sprNeckCnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sprEarring1Cnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sprEarring2Cnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sprRing1Cnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sprRing2Cnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sprStoneCnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final int index = i;
            sprNecks[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgNecks[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgNecks[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprEarring1s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgEarring1s[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgEarring1s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprEarring2s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgEarring2s[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgEarring2s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprRing1s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgRing1s[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgRing1s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprRing2s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgRing2s[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgRing2s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprStones[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgStones[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgStones[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        for (int i = 0; i < STAT_LENGTH; i++) {
            sprStatCnts[i].setAdapter(stat_adapter);
            sprStats[i].setAdapter(burf_adapter);
            sprStatCnts[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final int index = i;
            sprStats[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calStamp();
                    if (position == 0) {
                        imgStats[index].setImageResource(R.drawable.none_stamp);
                        return;
                    }
                    if (index == 2) position += 85;
                    position--;
                    imgStats[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        imgNeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (neck_index == 3) neck_index = 0;
                else neck_index++;
                switch (neck_index) {
                    case 0:
                        imgNeck.setImageResource(R.drawable.neck_0);
                        txtNeck.setText("영웅 목걸이");
                        break;
                    case 1:
                        imgNeck.setImageResource(R.drawable.neck_1);
                        txtNeck.setText("전설 목걸이");
                        break;
                    case 2:
                        imgNeck.setImageResource(R.drawable.neck_2);
                        txtNeck.setText("유물 목걸이");
                        break;
                    case 3:
                        imgNeck.setImageResource(R.drawable.neck_3);
                        txtNeck.setText("고대 목걸이");
                        break;
                }
            }
        });
        
        imgEarring1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (earring1_index == 3) earring1_index = 0;
                else earring1_index++;
                switch (earring1_index) {
                    case 0:
                        imgEarring1.setImageResource(R.drawable.earring1_0);
                        txtEarring1.setText("영웅 귀걸이1");
                        break;
                    case 1:
                        imgEarring1.setImageResource(R.drawable.earring1_1);
                        txtEarring1.setText("전설 귀걸이1");
                        break;
                    case 2:
                        imgEarring1.setImageResource(R.drawable.earring1_2);
                        txtEarring1.setText("유물 귀걸이1");
                        break;
                    case 3:
                        imgEarring1.setImageResource(R.drawable.earring1_3);
                        txtEarring1.setText("고대 귀걸이1");
                        break;
                }
            }
        });
        
        imgEarring2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (earring2_index == 3) earring2_index = 0;
                else earring2_index++;
                switch (earring2_index) {
                    case 0:
                        imgEarring2.setImageResource(R.drawable.earring2_0);
                        txtEarring2.setText("영웅 귀걸이2");
                        break;
                    case 1:
                        imgEarring2.setImageResource(R.drawable.earring2_1);
                        txtEarring2.setText("전설 귀걸이2");
                        break;
                    case 2:
                        imgEarring2.setImageResource(R.drawable.earring2_2);
                        txtEarring2.setText("유물 귀걸이2");
                        break;
                    case 3:
                        imgEarring2.setImageResource(R.drawable.earring2_3);
                        txtEarring2.setText("고대 귀걸이2");
                        break;
                }
            }
        });
        
        imgRing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ring1_index == 3) ring1_index = 0;
                else ring1_index++;
                switch (ring1_index) {
                    case 0:
                        imgRing1.setImageResource(R.drawable.ring1_0);
                        txtRing1.setText("영웅 반지1");
                        break;
                    case 1:
                        imgRing1.setImageResource(R.drawable.ring1_1);
                        txtRing1.setText("전설 반지1");
                        break;
                    case 2:
                        imgRing1.setImageResource(R.drawable.ring1_2);
                        txtRing1.setText("유물 반지1");
                        break;
                    case 3:
                        imgRing1.setImageResource(R.drawable.ring1_3);
                        txtRing1.setText("고대 반지1");
                        break;
                }
            }
        });
        
        imgRing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ring2_index == 3) ring2_index = 0;
                else ring2_index++;
                switch (ring2_index) {
                    case 0:
                        imgRing2.setImageResource(R.drawable.ring2_0);
                        txtRing2.setText("영웅 반지2");
                        break;
                    case 1:
                        imgRing2.setImageResource(R.drawable.ring2_1);
                        txtRing2.setText("전설 반지2");
                        break;
                    case 2:
                        imgRing2.setImageResource(R.drawable.ring2_2);
                        txtRing2.setText("유물 반지2");
                        break;
                    case 3:
                        imgRing2.setImageResource(R.drawable.ring2_3);
                        txtRing2.setText("고대 반지2");
                        break;
                }
            }
        });

        return root;
    }

    private void calStamp() {
        stampMap.clear();
        for (int i = 0; i < STAT_LENGTH; i++) {
            String name;
            int cnt;
            name = sprStats[i].getSelectedItem().toString();
            cnt = Integer.parseInt(sprStatCnts[i].getSelectedItem().toString());
            if (!sprStats[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
        }
        for (int i = 0; i < STAMP_LENGTH; i++) {
            String name;
            int cnt;
            name = sprNecks[i].getSelectedItem().toString();
            cnt = sprNeckCnts[i].getSelectedItemPosition()+1;
            if (!sprNecks[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
            name = sprEarring1s[i].getSelectedItem().toString();
            cnt = sprEarring1Cnts[i].getSelectedItemPosition()+1;
            if (!sprEarring1s[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
            name = sprEarring2s[i].getSelectedItem().toString();
            cnt = sprEarring2Cnts[i].getSelectedItemPosition()+1;
            if (!sprEarring2s[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
            name = sprRing1s[i].getSelectedItem().toString();
            cnt = sprRing1Cnts[i].getSelectedItemPosition()+1;
            if (!sprRing1s[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
            name = sprRing2s[i].getSelectedItem().toString();
            cnt = sprRing2Cnts[i].getSelectedItemPosition()+1;
            if (!sprRing2s[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
            name = sprStones[i].getSelectedItem().toString();
            cnt = sprStoneCnts[i].getSelectedItemPosition()+1;
            if (!sprStones[i].getSelectedItem().toString().equals("없음")) {
                if (stampMap.containsKey(name)) stampMap.put(name, stampMap.get(name)+cnt);
                else stampMap.put(name, cnt);
            }
        }

        stampCals.clear();
        for (Map.Entry<String, Integer> entry : stampMap.entrySet()) {
            if (isDeburf(entry.getKey())) stampCals.add(new StampCal(entry.getKey(), entry.getValue()));
            else stampCals.add(0, new StampCal(entry.getKey(), entry.getValue()));
        }
        stampCalAdapter.notifyDataSetChanged();
    }

    private boolean isDeburf(String name) {
        String[] args = stampDBAdapter.readData(name);
        if (args[2].equals("감소")) return true;
        else return false;
    }
}
