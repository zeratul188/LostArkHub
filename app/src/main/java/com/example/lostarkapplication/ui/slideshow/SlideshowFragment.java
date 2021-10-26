package com.example.lostarkapplication.ui.slideshow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lostarkapplication.R;
import com.example.lostarkapplication.database.StampDBAdapter;
import com.example.lostarkapplication.ui.gallery.Stamp;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {
    private static final int STAMP_LENGTH = 3;
    
    private LinearLayout layoutStamp;
    private ImageView imgNeck, imgEarring1, imgEarring2, imgRing1, imgRing2;
    private TextView txtNeck, txtEarring1, txtEarring2, txtRing1, txtRing2;
    private ImageView[] imgNecks = new ImageView[STAMP_LENGTH];
    private Spinner[] sprNecks = new Spinner[STAMP_LENGTH];
    private EditText[] edtNecks = new EditText[STAMP_LENGTH];
    private ImageView[] imgEarring1s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprEarring1s = new Spinner[STAMP_LENGTH];
    private EditText[] edtEarring1s = new EditText[STAMP_LENGTH];
    private ImageView[] imgEarring2s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprEarring2s = new Spinner[STAMP_LENGTH];
    private EditText[] edtEarring2s = new EditText[STAMP_LENGTH];
    private ImageView[] imgRing1s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprRing1s = new Spinner[STAMP_LENGTH];
    private EditText[] edtRing1s = new EditText[STAMP_LENGTH];
    private ImageView[] imgRing2s = new ImageView[STAMP_LENGTH];
    private Spinner[] sprRing2s = new Spinner[STAMP_LENGTH];
    private EditText[] edtRing2s = new EditText[STAMP_LENGTH];
    private ImageView[] imgStones = new ImageView[STAMP_LENGTH];
    private Spinner[] sprStones = new Spinner[STAMP_LENGTH];
    private EditText[] edtStones = new EditText[STAMP_LENGTH];

    private int neck_index = 0, earring1_index = 0, earring2_index = 0, ring1_index = 0, ring2_index = 0;

    private StampDBAdapter stampDBAdapter;
    private ArrayList<Stamp> stamps;

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

        layoutStamp = root.findViewById(R.id.layoutStamp);
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
            edtNecks[i] = root.findViewById(getActivity().getResources().getIdentifier("edtNeck"+(i+1), "id", getActivity().getPackageName()));
            imgEarring1s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgEarring1"+(i+1), "id", getActivity().getPackageName()));
            sprEarring1s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprEarring1"+(i+1), "id", getActivity().getPackageName()));
            edtEarring1s[i] = root.findViewById(getActivity().getResources().getIdentifier("edtEarring1"+(i+1), "id", getActivity().getPackageName()));
            imgEarring2s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgEarring2"+(i+1), "id", getActivity().getPackageName()));
            sprEarring2s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprEarring2"+(i+1), "id", getActivity().getPackageName()));
            edtEarring2s[i] = root.findViewById(getActivity().getResources().getIdentifier("edtEarring2"+(i+1), "id", getActivity().getPackageName()));
            imgRing1s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgRing1"+(i+1), "id", getActivity().getPackageName()));
            sprRing1s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprRing1"+(i+1), "id", getActivity().getPackageName()));
            edtRing1s[i] = root.findViewById(getActivity().getResources().getIdentifier("edtRing1"+(i+1), "id", getActivity().getPackageName()));
            imgRing2s[i] = root.findViewById(getActivity().getResources().getIdentifier("imgRing2"+(i+1), "id", getActivity().getPackageName()));
            sprRing2s[i] = root.findViewById(getActivity().getResources().getIdentifier("sprRing2"+(i+1), "id", getActivity().getPackageName()));
            edtRing2s[i] = root.findViewById(getActivity().getResources().getIdentifier("edtRing2"+(i+1), "id", getActivity().getPackageName()));
            imgStones[i] = root.findViewById(getActivity().getResources().getIdentifier("imgStone"+(i+1), "id", getActivity().getPackageName()));
            sprStones[i] = root.findViewById(getActivity().getResources().getIdentifier("sprStone"+(i+1), "id", getActivity().getPackageName()));
            edtStones[i] = root.findViewById(getActivity().getResources().getIdentifier("edtStone"+(i+1), "id", getActivity().getPackageName()));
        }

        stampDBAdapter = new StampDBAdapter(getActivity());
        stamps = new ArrayList<>();
        for (int i = 0; i < 89; i++) {
            stamps.add(new Stamp(stampDBAdapter.readData(i)[0], stampDBAdapter.readData(i)[1]));
        }
        ArrayList<String> burfs = new ArrayList<>();
        ArrayList<String> deburfs = new ArrayList<>();
        for (int i = 0; i < 85; i++) burfs.add(stampDBAdapter.readData(i)[0]);
        for (int i = 85; i < 89; i++) deburfs.add(stampDBAdapter.readData(i)[0]);
        ArrayAdapter<String> burf_adapter = new ArrayAdapter<String>(getActivity(), R.layout.stampitem, burfs);
        ArrayAdapter<String> deburf_adapter = new ArrayAdapter<String>(getActivity(), R.layout.stampitem, deburfs);
        burf_adapter.setDropDownViewResource(R.layout.stampitem);
        deburf_adapter.setDropDownViewResource(R.layout.stampitem);
        for (int i = 0; i < 3; i++) {
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
            final int index = i;
            sprNecks[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (index == 2) position += 85;
                    imgNecks[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprEarring1s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (index == 2) position += 85;
                    imgEarring1s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprEarring2s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (index == 2) position += 85;
                    imgEarring2s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprRing1s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (index == 2) position += 85;
                    imgRing1s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprRing2s[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (index == 2) position += 85;
                    imgRing2s[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sprStones[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (index == 2) position += 85;
                    imgStones[index].setImageResource(getActivity().getResources().getIdentifier(stamps.get(position).getImage(), "drawable", getActivity().getPackageName()));
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
                        txtNeck.setTextColor(Color.parseColor("#9B53D2"));
                        txtNeck.setBackgroundResource(R.drawable.equipmentstyle1);
                        break;
                    case 1:
                        imgNeck.setImageResource(R.drawable.neck_1);
                        txtNeck.setText("전설 목걸이");
                        txtNeck.setTextColor(Color.parseColor("#C2873B"));
                        txtNeck.setBackgroundResource(R.drawable.equipmentstyle2);
                        break;
                    case 2:
                        imgNeck.setImageResource(R.drawable.neck_2);
                        txtNeck.setText("유물 목걸이");
                        txtNeck.setTextColor(Color.parseColor("#BF5700"));
                        txtNeck.setBackgroundResource(R.drawable.equipmentstyle3);
                        break;
                    case 3:
                        imgNeck.setImageResource(R.drawable.neck_3);
                        txtNeck.setText("고대 목걸이");
                        txtNeck.setTextColor(Color.parseColor("#F5E4B8"));
                        txtNeck.setBackgroundResource(R.drawable.equipmentstyle4);
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
                        txtEarring1.setTextColor(Color.parseColor("#9B53D2"));
                        txtEarring1.setBackgroundResource(R.drawable.equipmentstyle1);
                        break;
                    case 1:
                        imgEarring1.setImageResource(R.drawable.earring1_1);
                        txtEarring1.setText("전설 귀걸이1");
                        txtEarring1.setTextColor(Color.parseColor("#C2873B"));
                        txtEarring1.setBackgroundResource(R.drawable.equipmentstyle2);
                        break;
                    case 2:
                        imgEarring1.setImageResource(R.drawable.earring1_2);
                        txtEarring1.setText("유물 귀걸이1");
                        txtEarring1.setTextColor(Color.parseColor("#BF5700"));
                        txtEarring1.setBackgroundResource(R.drawable.equipmentstyle3);
                        break;
                    case 3:
                        imgEarring1.setImageResource(R.drawable.earring1_3);
                        txtEarring1.setText("고대 귀걸이1");
                        txtEarring1.setTextColor(Color.parseColor("#F5E4B8"));
                        txtEarring1.setBackgroundResource(R.drawable.equipmentstyle4);
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
                        txtEarring2.setTextColor(Color.parseColor("#9B53D2"));
                        txtEarring2.setBackgroundResource(R.drawable.equipmentstyle1);
                        break;
                    case 1:
                        imgEarring2.setImageResource(R.drawable.earring2_1);
                        txtEarring2.setText("전설 귀걸이2");
                        txtEarring2.setTextColor(Color.parseColor("#C2873B"));
                        txtEarring2.setBackgroundResource(R.drawable.equipmentstyle2);
                        break;
                    case 2:
                        imgEarring2.setImageResource(R.drawable.earring2_2);
                        txtEarring2.setText("유물 귀걸이2");
                        txtEarring2.setTextColor(Color.parseColor("#BF5700"));
                        txtEarring2.setBackgroundResource(R.drawable.equipmentstyle3);
                        break;
                    case 3:
                        imgEarring2.setImageResource(R.drawable.earring2_3);
                        txtEarring2.setText("고대 귀걸이2");
                        txtEarring2.setTextColor(Color.parseColor("#F5E4B8"));
                        txtEarring2.setBackgroundResource(R.drawable.equipmentstyle4);
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
                        txtRing1.setTextColor(Color.parseColor("#9B53D2"));
                        txtRing1.setBackgroundResource(R.drawable.equipmentstyle1);
                        break;
                    case 1:
                        imgRing1.setImageResource(R.drawable.ring1_1);
                        txtRing1.setText("전설 반지1");
                        txtRing1.setTextColor(Color.parseColor("#C2873B"));
                        txtRing1.setBackgroundResource(R.drawable.equipmentstyle2);
                        break;
                    case 2:
                        imgRing1.setImageResource(R.drawable.ring1_2);
                        txtRing1.setText("유물 반지1");
                        txtRing1.setTextColor(Color.parseColor("#BF5700"));
                        txtRing1.setBackgroundResource(R.drawable.equipmentstyle3);
                        break;
                    case 3:
                        imgRing1.setImageResource(R.drawable.ring1_3);
                        txtRing1.setText("고대 반지1");
                        txtRing1.setTextColor(Color.parseColor("#F5E4B8"));
                        txtRing1.setBackgroundResource(R.drawable.equipmentstyle4);
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
                        txtRing2.setTextColor(Color.parseColor("#9B53D2"));
                        txtRing2.setBackgroundResource(R.drawable.equipmentstyle1);
                        break;
                    case 1:
                        imgRing2.setImageResource(R.drawable.ring2_1);
                        txtRing2.setText("전설 반지2");
                        txtRing2.setTextColor(Color.parseColor("#C2873B"));
                        txtRing2.setBackgroundResource(R.drawable.equipmentstyle2);
                        break;
                    case 2:
                        imgRing2.setImageResource(R.drawable.ring2_2);
                        txtRing2.setText("유물 반지2");
                        txtRing2.setTextColor(Color.parseColor("#BF5700"));
                        txtRing2.setBackgroundResource(R.drawable.equipmentstyle3);
                        break;
                    case 3:
                        imgRing2.setImageResource(R.drawable.ring2_3);
                        txtRing2.setText("고대 반지2");
                        txtRing2.setTextColor(Color.parseColor("#F5E4B8"));
                        txtRing2.setBackgroundResource(R.drawable.equipmentstyle4);
                        break;
                }
            }
        });

        return root;
    }
}
