package com.lostark.lostarkapplication.ui.gallery;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.StampDBAdapter;
import com.lostark.lostarkapplication.database.StoneDBAdapter;
import com.lostark.lostarkapplication.database.StonePresetDBAdapter;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {
    private static final int AI_LENGTH = 3;

    private GalleryViewModel galleryViewModel;
    private ImageView imgStone, imgBurf1, imgBurf2, imgDeburf;
    private TextView txtTitle, txtGrade, txtBurf1, txtBurf2, txtDeburf, txtPercent, txtCount1, txtCount2, txtCount3, txtFail;
    private Button btnConfirm, btnReset, btnPreset;
    private ImageButton btnBurf1, btnBurf2, btnDeburf;
    private ImageView[] imgFirst = new ImageView[10];
    private ImageView[] imgSecond = new ImageView[10];
    private ImageView[] imgThird = new ImageView[10];
    private FloatingActionButton fabList;
    private Switch swtAI, swtSF;
    private LinearLayout layoutAI;
    private TextView[] txtAInumber = new TextView[AI_LENGTH];
    private Button[] btnAIup = new Button[AI_LENGTH];
    private Button[] btnAIdown = new Button[AI_LENGTH];
    private TextView[] txtPick = new TextView[AI_LENGTH];
    private LinearLayout[] layoutButtons = new LinearLayout[AI_LENGTH];
    private Button[] btnSuccess = new Button[AI_LENGTH];
    private Button[] btnFail = new Button[AI_LENGTH];

    private StampDBAdapter stampDBAdapter;
    private StoneDBAdapter stoneDBAdapter;
    private StoneAdapter stoneAdapter;
    private AlertDialog stampDialog, alertDialog;
    private DataNetwork dn;
    private ArrayList<Stone> stones;
    private CustomToast customToast;
    private Thread th = null;
    private StoneAI ai;
    private StonePresetDBAdapter presetDBAdapter;
    private StonePresetAdapter presetAdapter;
    private ArrayList<Preset> presets;

    private SharedPreferences pref;

    private int max = 0, type = 0;
    private int burf1 = 0, burf2 = 0, deburf = 0;
    private int burf1_cnt = 0, burf2_cnt = 0, deburf_cnt = 0;
    private int percent = 75;
    private String history = "";

    private boolean isStop() {
        if (burf1_cnt == 0 && burf2_cnt == 0 && deburf_cnt == 0) {
            return true;
        }
        return false;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        /*final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        dn = new DataNetwork();
        stones = new ArrayList<>();
        customToast = new CustomToast(getActivity());
        ai = new StoneAI();

        imgStone = root.findViewById(R.id.imgStone);
        imgBurf1 = root.findViewById(R.id.imgBurf1);
        imgBurf2 = root.findViewById(R.id.imgBurf2);
        imgDeburf = root.findViewById(R.id.imgDeburf);
        txtTitle = root.findViewById(R.id.txtTitle);
        txtGrade = root.findViewById(R.id.txtGrade);
        txtBurf1 = root.findViewById(R.id.txtBurf1);
        txtBurf2 = root.findViewById(R.id.txtBurf2);
        txtDeburf = root.findViewById(R.id.txtDeburf);
        txtPercent = root.findViewById(R.id.txtPercent);
        btnBurf1 = root.findViewById(R.id.btnBurf1);
        btnBurf2 = root.findViewById(R.id.btnBurf2);
        btnDeburf = root.findViewById(R.id.btnDeburf);
        txtCount1 = root.findViewById(R.id.txtCount1);
        txtCount2 = root.findViewById(R.id.txtCount2);
        txtCount3 = root.findViewById(R.id.txtCount3);
        btnConfirm = root.findViewById(R.id.btnConfirm);
        btnReset = root.findViewById(R.id.btnReset);
        fabList = root.findViewById(R.id.fabList);
        swtAI = root.findViewById(R.id.swtAI);
        layoutAI = root.findViewById(R.id.layoutAI);
        swtSF = root.findViewById(R.id.swtSF);
        txtFail = root.findViewById(R.id.txtFail);
        btnPreset = root.findViewById(R.id.btnPreset);
        for (int i = 0; i < imgFirst.length; i++) {
            imgFirst[i] = root.findViewById(getActivity().getResources().getIdentifier("imgFirst"+(i+1), "id", getActivity().getPackageName()));
            imgSecond[i] = root.findViewById(getActivity().getResources().getIdentifier("imgSecond"+(i+1), "id", getActivity().getPackageName()));
            imgThird[i] = root.findViewById(getActivity().getResources().getIdentifier("imgThird"+(i+1), "id", getActivity().getPackageName()));
        }
        for (int i = 0; i < AI_LENGTH; i++) {
            txtAInumber[i] = root.findViewById(getActivity().getResources().getIdentifier("txtAInumber"+(i+1), "id", getActivity().getPackageName()));
            btnAIup[i] = root.findViewById(getActivity().getResources().getIdentifier("btnAIup"+(i+1), "id", getActivity().getPackageName()));
            btnAIdown[i] = root.findViewById(getActivity().getResources().getIdentifier("btnAIdown"+(i+1), "id", getActivity().getPackageName()));
            final int position = i;
            btnAIup[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ai.targetUp(position);
                    if (ai.getTarget(position) >= max) {
                        btnAIup[position].setEnabled(false);
                    }
                    txtAInumber[position].setText(Integer.toString(ai.getTarget(position)));
                    btnAIdown[position].setEnabled(true);
                }
            });
            btnAIdown[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ai.targetDown(position);
                    if (ai.getTarget(position) <= 0) {
                        btnAIdown[position].setEnabled(false);
                    }
                    txtAInumber[position].setText(Integer.toString(ai.getTarget(position)));
                    btnAIup[position].setEnabled(true);
                }
            });
            txtPick[i] = root.findViewById(getActivity().getResources().getIdentifier("txtPick"+(i+1), "id", getActivity().getPackageName()));
            layoutButtons[i] = root.findViewById(getActivity().getResources().getIdentifier("layoutButtons"+(i+1), "id", getActivity().getPackageName()));
            btnSuccess[i] = root.findViewById(getActivity().getResources().getIdentifier("btnSuccess"+(i+1), "id", getActivity().getPackageName()));
            btnFail[i] = root.findViewById(getActivity().getResources().getIdentifier("btnFail"+(i+1), "id", getActivity().getPackageName()));
            btnSuccess[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = "success";
                    int now_percent = percent;
                    if (position == 0) {
                        imgFirst[burf1].setImageResource(R.drawable.success);
                        burf1++;
                        burf1_cnt++;
                        txtCount1.setText(Integer.toString(burf1_cnt));
                        if (burf1 == max) {
                            btnBurf1.setEnabled(false);
                            btnSuccess[position].setEnabled(false);
                            btnFail[position].setEnabled(false);
                        }
                        if (!history.equals("")) history += "|";
                        history += "1/\""+txtBurf1.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                    } else if (position == 1) {
                        imgSecond[burf2].setImageResource(R.drawable.success);
                        burf2++;
                        burf2_cnt++;
                        txtCount2.setText(Integer.toString(burf2_cnt));
                        if (burf2 == max) {
                            btnBurf2.setEnabled(false);
                            btnSuccess[position].setEnabled(false);
                            btnFail[position].setEnabled(false);
                        }
                        if (!history.equals("")) history += "|";
                        history += "2/\""+txtBurf2.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                    } else {
                        imgThird[deburf].setImageResource(R.drawable.deburf_success);
                        deburf++;
                        deburf_cnt++;
                        txtCount3.setText(Integer.toString(deburf_cnt));
                        if (deburf == max) {
                            btnDeburf.setEnabled(false);
                            btnSuccess[position].setEnabled(false);
                            btnFail[position].setEnabled(false);
                        }
                        if (!history.equals("")) history += "|";
                        history += "3/\""+txtDeburf.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                    }
                    if (percent > 25) percent -= 10;
                    ai.upNow(position);
                    ai.upCnt(position);
                    txtPercent.setText(Integer.toString(percent));

                    if (allMax()) {
                        btnConfirm.setEnabled(true);
                    }

                    asyncAI();
                }
            });
            btnFail[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = "fail";
                    int now_percent = percent;
                    if (position == 0) {
                        imgFirst[burf1].setImageResource(R.drawable.fail);
                        burf1++;
                        txtCount1.setText(Integer.toString(burf1_cnt));
                        if (burf1 == max) {
                            btnBurf1.setEnabled(false);
                            btnSuccess[position].setEnabled(false);
                            btnFail[position].setEnabled(false);
                        }
                        if (!history.equals("")) history += "|";
                        history += "1/\""+txtBurf1.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                    } else if (position == 1) {
                        imgSecond[burf2].setImageResource(R.drawable.fail);
                        burf2++;
                        txtCount2.setText(Integer.toString(burf2_cnt));
                        if (burf2 == max) {
                            btnBurf2.setEnabled(false);
                            btnSuccess[position].setEnabled(false);
                            btnFail[position].setEnabled(false);
                        }
                        if (!history.equals("")) history += "|";
                        history += "2/\""+txtBurf2.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                    } else {
                        imgThird[deburf].setImageResource(R.drawable.deburf_fail);
                        deburf++;
                        txtCount3.setText(Integer.toString(deburf_cnt));
                        if (deburf == max) {
                            btnDeburf.setEnabled(false);
                            btnSuccess[position].setEnabled(false);
                            btnFail[position].setEnabled(false);
                        }
                        if (!history.equals("")) history += "|";
                        history += "3/\""+txtDeburf.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                    }
                    if (percent < 75) percent += 10;
                    ai.upCnt(position);
                    txtPercent.setText(Integer.toString(percent));

                    if (allMax()) {
                        btnConfirm.setEnabled(true);
                    }

                    asyncAI();
                }
            });
        }

        presetDBAdapter = new StonePresetDBAdapter(getActivity());
        btnPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStop()) {
                    customToast.createToast("이미 스톤이 세공중입니다. 스톤 세공을 취소 또는 완료 후 다시 시도해주세요.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }

                View view = getLayoutInflater().inflate(R.layout.stone_preset_layout, null);

                TextView txtLimit = view.findViewById(R.id.txtLimit);
                ListView listView = view.findViewById(R.id.listView);
                Button btnSave = view.findViewById(R.id.btnSave);

                presets = new ArrayList<>();
                presetDBAdapter.open();
                txtLimit.setText("("+presetDBAdapter.getCount()+"/20");
                Cursor cursor = presetDBAdapter.fetchAllData();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String grade = cursor.getString(2);
                    String[] stamps = cursor.getString(3).split(",");

                    presets.add(new Preset(id, name, grade, stamps));
                    cursor.moveToNext();
                }
                presetDBAdapter.close();

                presetAdapter = new StonePresetAdapter(getActivity(), presets, getActivity());
                listView.setAdapter(presetAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (presets.get(position).getName()) {
                            case "비상의 돌":
                                txtTitle.setText("비상의 돌");
                                txtGrade.setText("희귀");
                                txtGrade.setTextColor(Color.parseColor("#2093A8"));
                                imgStone.setImageResource(R.drawable.stone1);
                                type = 0;
                                max = 6;
                                break;
                            case "뛰어난 비상의 돌":
                                txtTitle.setText("뛰어난 비상의 돌");
                                txtGrade.setText("영웅");
                                txtGrade.setTextColor(Color.parseColor("#9B53D2"));
                                imgStone.setImageResource(R.drawable.stone2);
                                type = 1;
                                max = 8;
                                break;
                            case "강력한 비상의 돌":
                                txtTitle.setText("강력한 비상의 돌");
                                txtGrade.setText("전설");
                                txtGrade.setTextColor(Color.parseColor("#C2873B"));
                                imgStone.setImageResource(R.drawable.stone3);
                                type = 2;
                                max = 9;
                                break;
                            case "고고한 비상의 돌":
                                txtTitle.setText("고고한 비상의 돌");
                                txtGrade.setText("유물");
                                txtGrade.setTextColor(Color.parseColor("#BF5700"));
                                imgStone.setImageResource(R.drawable.stone4);
                                type = 3;
                                max = 10;
                                break;
                        }
                        ai.setMax(max);
                        ai.resetTarget();
                        for (int i = 0; i < AI_LENGTH; i++) {
                            txtAInumber[i].setText(Integer.toString(ai.getTarget(i)));
                            if (i == AI_LENGTH-1) {
                                btnAIup[i].setEnabled(true);
                                btnAIdown[i].setEnabled(false);
                            } else {
                                btnAIup[i].setEnabled(false);
                                btnAIdown[i].setEnabled(true);
                            }
                        }
                        for (int i = 0; i < imgFirst.length; i++) {
                            if (i >= max) {
                                imgFirst[i].setVisibility(View.INVISIBLE);
                                imgSecond[i].setVisibility(View.INVISIBLE);
                                imgThird[i].setVisibility(View.INVISIBLE);
                            } else {
                                imgFirst[i].setVisibility(View.VISIBLE);
                                imgSecond[i].setVisibility(View.VISIBLE);
                                imgThird[i].setVisibility(View.VISIBLE);
                            }
                        }
                        String[] arr1 = stampDBAdapter.readData(presets.get(position).getStamps()[0]);
                        imgBurf1.setImageResource(getActivity().getResources().getIdentifier(arr1[1], "drawable", getActivity().getPackageName()));
                        txtBurf1.setText(arr1[0]);
                        String[] arr2 = stampDBAdapter.readData(presets.get(position).getStamps()[1]);
                        imgBurf2.setImageResource(getActivity().getResources().getIdentifier(arr2[1], "drawable", getActivity().getPackageName()));
                        txtBurf2.setText(arr2[0]);
                        String[] arr3 = stampDBAdapter.readData(presets.get(position).getStamps()[2]);
                        imgDeburf.setImageResource(getActivity().getResources().getIdentifier(arr3[1], "drawable", getActivity().getPackageName()));
                        txtDeburf.setText(arr3[0]);

                        customToast.createToast("프리셋을 적용하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = txtTitle.getText().toString();
                        String grade = txtGrade.getText().toString();
                        String[] stamps = new String[3];
                        stamps[0] = txtBurf1.getText().toString();
                        stamps[1] = txtBurf2.getText().toString();
                        stamps[2] = txtDeburf.getText().toString();
                        presetDBAdapter.open();
                        int id = presetDBAdapter.getNextID();

                        Preset preset = new Preset(id, name, grade, stamps);
                        presets.add(preset);
                        presetDBAdapter.insertData(preset);
                        txtLimit.setText("("+presetDBAdapter.getCount()+"/20");
                        presetDBAdapter.close();

                        customToast.createToast("프리셋을 추가하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        presetAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        stampDBAdapter = new StampDBAdapter(getActivity());
        stoneAdapter = new StoneAdapter(stones, getActivity());

        swtAI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutAI.setVisibility(View.VISIBLE);
                    asyncAI();
                } else {
                    layoutAI.setVisibility(View.GONE);
                    txtFail.setVisibility(View.GONE);
                    for (int i = 0; i < txtPick.length; i++) {
                        txtPick[i].setVisibility(View.GONE);
                    }
                }
            }
        });

        swtSF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < layoutButtons.length; i++) {
                        layoutButtons[i].setVisibility(View.VISIBLE);
                    }
                    btnBurf1.setVisibility(View.GONE);
                    btnBurf2.setVisibility(View.GONE);
                    btnDeburf.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < layoutButtons.length; i++) {
                        layoutButtons[i].setVisibility(View.GONE);
                    }
                    btnBurf1.setVisibility(View.VISIBLE);
                    btnBurf2.setVisibility(View.VISIBLE);
                    btnDeburf.setVisibility(View.VISIBLE);
                }
            }
        });

        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.stone_history_dialog, null);

                ListView listView = view.findViewById(R.id.listView);

                listView.setAdapter(stoneAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.stone_information_dialog, null);

                        ImageView imgStone = dialog_view.findViewById(R.id.imgStone);
                        TextView txtStone = dialog_view.findViewById(R.id.txtStone);
                        ImageView imgBurf1 = dialog_view.findViewById(R.id.imgBurf1);
                        ImageView imgBurf2 = dialog_view.findViewById(R.id.imgBurf2);
                        ImageView imgDeburf = dialog_view.findViewById(R.id.imgDeburf);
                        TextView txtBurf1 = dialog_view.findViewById(R.id.txtBurf1);
                        TextView txtBurf2 = dialog_view.findViewById(R.id.txtBurf2);
                        TextView txtDeburf = dialog_view.findViewById(R.id.txtDeburf);
                        ListView listHistory = dialog_view.findViewById(R.id.listHistory);
                        Button btnStart = dialog_view.findViewById(R.id.btnStart);
                        Button btnDelete = dialog_view.findViewById(R.id.btnDelete);
                        TextView txtPercent = dialog_view.findViewById(R.id.txtPercent);
                        TextView[] txtCount = new TextView[3];
                        ImageView[] imgFirst = new ImageView[10];
                        ImageView[] imgSecond = new ImageView[10];
                        ImageView[] imgThird = new ImageView[10];
                        for (int i = 0; i < txtCount.length; i++) {
                            txtCount[i] = dialog_view.findViewById(getActivity().getResources().getIdentifier("txtCount"+(i+1), "id", getActivity().getPackageName()));
                        }
                        for (int i = 0; i < imgFirst.length; i++) {
                            imgFirst[i] = dialog_view.findViewById(getActivity().getResources().getIdentifier("imgFirst"+(i+1), "id", getActivity().getPackageName()));
                            imgSecond[i] = dialog_view.findViewById(getActivity().getResources().getIdentifier("imgSecond"+(i+1), "id", getActivity().getPackageName()));
                            imgThird[i] = dialog_view.findViewById(getActivity().getResources().getIdentifier("imgThird"+(i+1), "id", getActivity().getPackageName()));
                        }

                        switch (stones.get(position).getGrade()) {
                            case "희귀":
                                txtStone.setText("비상의 돌");
                                txtStone.setTextColor(Color.parseColor("#2093A8"));
                                imgStone.setImageResource(R.drawable.stone1);
                                break;
                            case "영웅":
                                txtStone.setText("뛰어난 비상의 돌");
                                txtStone.setTextColor(Color.parseColor("#9B53D2"));
                                imgStone.setImageResource(R.drawable.stone2);
                                break;
                            case "전설":
                                txtStone.setText("강력한 비상의 돌");
                                txtStone.setTextColor(Color.parseColor("#C2873B"));
                                imgStone.setImageResource(R.drawable.stone3);
                                break;
                            case "유물":
                                txtStone.setText("고고한 비상의 돌");
                                txtStone.setTextColor(Color.parseColor("#BF5700"));
                                imgStone.setImageResource(R.drawable.stone4);
                                break;
                        }

                        txtBurf1.setText(stones.get(position).getStamp()[0]);
                        txtBurf2.setText(stones.get(position).getStamp()[1]);
                        txtDeburf.setText(stones.get(position).getStamp()[2]);

                        String[] arg1 = stampDBAdapter.readData(stones.get(position).getStamp()[0]);
                        imgBurf1.setImageResource(getActivity().getResources().getIdentifier(arg1[1], "drawable", getActivity().getPackageName()));
                        String[] arg2 = stampDBAdapter.readData(stones.get(position).getStamp()[1]);
                        imgBurf2.setImageResource(getActivity().getResources().getIdentifier(arg2[1], "drawable", getActivity().getPackageName()));
                        String[] arg3 = stampDBAdapter.readData(stones.get(position).getStamp()[2]);
                        imgDeburf.setImageResource(getActivity().getResources().getIdentifier(arg3[1], "drawable", getActivity().getPackageName()));

                        int[] counts = stones.get(position).getCnt();
                        for (int i = 0; i < txtCount.length; i++) {
                            txtCount[i].setText(Integer.toString(counts[i]));
                        }

                        for (int i = 0; i < imgFirst.length; i++) {
                            if (i < counts[0]) imgFirst[i].setVisibility(View.VISIBLE);
                            else imgFirst[i].setVisibility(View.INVISIBLE);
                            if (i < counts[1]) imgSecond[i].setVisibility(View.VISIBLE);
                            else imgSecond[i].setVisibility(View.INVISIBLE);
                            if (i < counts[2]) imgThird[i].setVisibility(View.VISIBLE);
                            else imgThird[i].setVisibility(View.INVISIBLE);
                        }

                        ArrayList<StoneHistory> histories = new ArrayList<>();
                        String[] historys = stones.get(position).getHistory().split("\\|");
                        for (String str : historys) {
                            String[] args = str.split("/");
                            int num = Integer.parseInt(args[0]);
                            String content = args[1];
                            boolean isSuccess;
                            if (args[2].equals("success")) isSuccess = true;
                            else isSuccess = false;
                            histories.add(new StoneHistory(num, content, isSuccess));
                        }
                        StoneHistoryAdapter adapter = new StoneHistoryAdapter(getActivity(), histories);
                        listHistory.setAdapter(adapter);

                        final StoneHistoryThread thread = new StoneHistoryThread(new Handler(), imgFirst, imgSecond, imgThird, histories, btnStart, txtPercent);
                        th = new Thread(thread);

                        btnStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                thread.setSecond(0.6);
                                if (btnStart.getText().toString().equals("시작")) {
                                    btnStart.setBackgroundResource(R.drawable.nobuttonstyle);
                                    btnStart.setText("중지");
                                    if (th.getState() == Thread.State.NEW) {
                                        th.start();
                                    } else if (th.getState() == Thread.State.TERMINATED) {
                                        th = new Thread(thread);
                                        th.start();
                                    }
                                } else {
                                    th.interrupt();
                                }
                            }
                        });

                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                stoneDBAdapter.open();
                                stoneDBAdapter.deleteData(stones.get(position).getRowID());
                                stoneDBAdapter.close();
                                stones.remove(position);
                                stoneAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                                customToast.createToast("스톤을 삭제하였습니다.", Toast.LENGTH_SHORT);
                                customToast.show();
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(dialog_view);

                        alertDialog = builder.create();

                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                th.interrupt();
                            }
                        });

                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
        stoneDBAdapter = new StoneDBAdapter(getActivity());

        txtTitle.setText("비상의 돌");
        txtGrade.setText("희귀");
        txtGrade.setTextColor(Color.parseColor("#2093A8"));
        imgStone.setImageResource(R.drawable.stone1);
        max = 6;

        for (int i = 0; i < imgFirst.length; i++) {
            if (i >= max) {
                imgFirst[i].setVisibility(View.INVISIBLE);
                imgSecond[i].setVisibility(View.INVISIBLE);
                imgThird[i].setVisibility(View.INVISIBLE);
            } else {
                imgFirst[i].setVisibility(View.VISIBLE);
                imgSecond[i].setVisibility(View.VISIBLE);
                imgThird[i].setVisibility(View.VISIBLE);
            }
        }

        imgStone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStop()) {
                    customToast.createToast("이미 스톤이 세공중입니다. 스톤 세공을 취소 또는 완료 후 다시 시도해주세요.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                if (type == 3) {
                    type = 0;
                } else {
                    type++;
                }
                switch (type) {
                    case 0:
                        txtTitle.setText("비상의 돌");
                        txtGrade.setText("희귀");
                        txtGrade.setTextColor(Color.parseColor("#2093A8"));
                        imgStone.setImageResource(R.drawable.stone1);
                        max = 6;
                        break;
                    case 1:
                        txtTitle.setText("뛰어난 비상의 돌");
                        txtGrade.setText("영웅");
                        txtGrade.setTextColor(Color.parseColor("#9B53D2"));
                        imgStone.setImageResource(R.drawable.stone2);
                        max = 8;
                        break;
                    case 2:
                        txtTitle.setText("강력한 비상의 돌");
                        txtGrade.setText("전설");
                        txtGrade.setTextColor(Color.parseColor("#C2873B"));
                        imgStone.setImageResource(R.drawable.stone3);
                        max = 9;
                        break;
                    case 3:
                        txtTitle.setText("고고한 비상의 돌");
                        txtGrade.setText("유물");
                        txtGrade.setTextColor(Color.parseColor("#BF5700"));
                        imgStone.setImageResource(R.drawable.stone4);
                        max = 10;
                        break;
                }
                ai.setMax(max);
                ai.resetTarget();
                for (int i = 0; i < AI_LENGTH; i++) {
                    txtAInumber[i].setText(Integer.toString(ai.getTarget(i)));
                    if (i == AI_LENGTH-1) {
                        btnAIup[i].setEnabled(true);
                        btnAIdown[i].setEnabled(false);
                    } else {
                        btnAIup[i].setEnabled(false);
                        btnAIdown[i].setEnabled(true);
                    }
                }
                for (int i = 0; i < imgFirst.length; i++) {
                    if (i >= max) {
                        imgFirst[i].setVisibility(View.INVISIBLE);
                        imgSecond[i].setVisibility(View.INVISIBLE);
                        imgThird[i].setVisibility(View.INVISIBLE);
                    } else {
                        imgFirst[i].setVisibility(View.VISIBLE);
                        imgSecond[i].setVisibility(View.VISIBLE);
                        imgThird[i].setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        imgBurf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStop()) {
                    customToast.createToast("이미 스톤이 세공중입니다. 스톤 세공을 취소 또는 완료 후 다시 시도해주세요.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                ListView listView = view.findViewById(R.id.listView);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                ArrayList<Stamp> stamps = new ArrayList<>();
                for (int i = 0; i < 87; i++) {
                    String[] arr = stampDBAdapter.readData(i);
                    Stamp stamp = new Stamp(arr[0], arr[1]);
                    if (!stamp.getName().equals(txtBurf1.getText().toString())) stamps.add(stamp);
                }

                StampAdapter stampAdapter = new StampAdapter(stamps, getActivity(), dn);
                listView.setAdapter(stampAdapter);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dn.setContent("");
                        stampDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                stampDialog = builder.create();

                stampAdapter.setAlertDialog(stampDialog);
                stampDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!dn.getContent().equals("")) {
                            String[] arr = stampDBAdapter.readData(dn.getContent());
                            imgBurf1.setImageResource(getActivity().getResources().getIdentifier(arr[1], "drawable", getActivity().getPackageName()));
                            txtBurf1.setText(arr[0]);
                        }
                    }
                });

                stampDialog.setCancelable(false);
                stampDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                stampDialog.show();
            }
        });

        imgBurf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStop()) {
                    customToast.createToast("이미 스톤이 세공중입니다. 스톤 세공을 취소 또는 완료 후 다시 시도해주세요.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                ListView listView = view.findViewById(R.id.listView);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                ArrayList<Stamp> stamps = new ArrayList<>();
                for (int i = 0; i < 87; i++) {
                    String[] arr = stampDBAdapter.readData(i);
                    Stamp stamp = new Stamp(arr[0], arr[1]);
                    if (!stamp.getName().equals(txtBurf2.getText().toString())) stamps.add(stamp);
                }

                StampAdapter stampAdapter = new StampAdapter(stamps, getActivity(), dn);
                listView.setAdapter(stampAdapter);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dn.setContent("");
                        stampDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                stampDialog = builder.create();

                stampAdapter.setAlertDialog(stampDialog);
                stampDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!dn.getContent().equals("")) {
                            String[] arr = stampDBAdapter.readData(dn.getContent());
                            imgBurf2.setImageResource(getActivity().getResources().getIdentifier(arr[1], "drawable", getActivity().getPackageName()));
                            txtBurf2.setText(arr[0]);
                        }
                    }
                });

                stampDialog.setCancelable(false);
                stampDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                stampDialog.show();
            }
        });

        imgDeburf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStop()) {
                    customToast.createToast("이미 스톤이 세공중입니다. 스톤 세공을 취소 또는 완료 후 다시 시도해주세요.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }

                View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                ListView listView = view.findViewById(R.id.listView);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                ArrayList<Stamp> stamps = new ArrayList<>();
                for (int i = 87; i < 91; i++) {
                    String[] arr = stampDBAdapter.readData(i);
                    Stamp stamp = new Stamp(arr[0], arr[1]);
                    if (!stamp.getName().equals(txtDeburf.getText().toString())) stamps.add(stamp);
                }

                StampAdapter stampAdapter = new StampAdapter(stamps, getActivity(), dn);
                listView.setAdapter(stampAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), stamps.get(position).getName()+"/"+stamps.get(position).getImage(), Toast.LENGTH_LONG).show();
                        customToast.createToast(stamps.get(position).getName()+"/"+stamps.get(position).getImage(), Toast.LENGTH_LONG);
                        customToast.show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dn.setContent("");
                        stampDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                stampDialog = builder.create();

                stampAdapter.setAlertDialog(stampDialog);
                stampDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!dn.getContent().equals("")) {
                            String[] arr = stampDBAdapter.readData(dn.getContent());
                            imgDeburf.setImageResource(getActivity().getResources().getIdentifier(arr[1], "drawable", getActivity().getPackageName()));
                            txtDeburf.setText(arr[0]);
                        }
                    }
                });

                stampDialog.setCancelable(false);
                stampDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                stampDialog.show();
            }
        });

        btnBurf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int now_percent = percent;
                int ransu = (int)(Math.random()*123456)%100;
                if (ransu <= percent) {
                    imgFirst[burf1].setImageResource(R.drawable.success);
                    burf1_cnt++;
                    txtCount1.setText(Integer.toString(burf1_cnt));
                    if (percent > 25) percent -= 10;
                    result = "success";
                    ai.upNow(0);
                } else {
                    imgFirst[burf1].setImageResource(R.drawable.fail);
                    if (percent < 75) percent += 10;
                    result = "fail";
                }
                burf1++;
                ai.upCnt(0);
                txtPercent.setText(Integer.toString(percent));
                if (burf1 == max) btnBurf1.setEnabled(false);
                if (allMax()) {
                    btnConfirm.setEnabled(true);
                }
                if (!history.equals("")) history += "|";
                history += "1/\""+txtBurf1.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                asyncAI();
            }
        });

        btnBurf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int now_percent = percent;
                int ransu = (int)(Math.random()*123456)%100;
                if (ransu <= percent) {
                    imgSecond[burf2].setImageResource(R.drawable.success);
                    burf2_cnt++;
                    txtCount2.setText(Integer.toString(burf2_cnt));
                    if (percent > 25) percent -= 10;
                    result = "success";
                    ai.upNow(1);
                } else {
                    imgSecond[burf2].setImageResource(R.drawable.fail);
                    if (percent < 75) percent += 10;
                    result = "fail";
                }
                burf2++;
                ai.upCnt(1);
                txtPercent.setText(Integer.toString(percent));
                if (burf2 == max) btnBurf2.setEnabled(false);
                if (allMax()) {
                    btnConfirm.setEnabled(true);
                }
                if (!history.equals("")) history += "|";
                history += "2/\""+txtBurf2.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                asyncAI();
            }
        });

        btnDeburf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int now_percent = percent;
                int ransu = (int)(Math.random()*123456)%100;
                if (ransu <= percent) {
                    imgThird[deburf].setImageResource(R.drawable.deburf_success);
                    deburf_cnt++;
                    txtCount3.setText(Integer.toString(deburf_cnt));
                    if (percent > 25) percent -= 10;
                    result = "success";
                    ai.upNow(2);
                } else {
                    imgThird[deburf].setImageResource(R.drawable.deburf_fail);
                    if (percent < 75) percent += 10;
                    result = "fail";
                }
                deburf++;
                ai.upCnt(2);
                txtPercent.setText(Integer.toString(percent));
                if (deburf == max) btnDeburf.setEnabled(false);
                if (allMax()) {
                    btnConfirm.setEnabled(true);
                }
                if (!history.equals("")) history += "|";
                history += "3/\""+txtDeburf.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
                asyncAI();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] stamps = new String[3];
                int[] stamp_cnts = new int[3];
                stamps[0] = txtBurf1.getText().toString();
                stamps[1] = txtBurf2.getText().toString();
                stamps[2] = txtDeburf.getText().toString();
                stamp_cnts[0] = Integer.parseInt(txtCount1.getText().toString());
                stamp_cnts[1] = Integer.parseInt(txtCount2.getText().toString());
                stamp_cnts[2] = Integer.parseInt(txtCount3.getText().toString());

                stoneDBAdapter.open();
                int rowID = stoneDBAdapter.getLastRowID();
                stoneDBAdapter.insertData(new Stone(txtGrade.getText().toString(), stamps, stamp_cnts, history, rowID));
                stoneDBAdapter.close();

                stones.add(0, new Stone(txtGrade.getText().toString(), stamps, stamp_cnts, history, rowID));
                stoneAdapter.notifyDataSetChanged();

                customToast.createToast("어빌리티 스톤을 완성하였습니다.", Toast.LENGTH_SHORT);
                customToast.show();

                reset();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        return root;
    }

    private void asyncAI() {
        if (!swtAI.isChecked()) return;
        ai.setPercent(percent);
        if (ai.logic() == 9) {
            txtFail.setVisibility(View.VISIBLE);
            for (int i = 0; i < txtPick.length; i++) {
                txtPick[i].setVisibility(View.GONE);
            }
            return;
        } else if (ai.logic() == 8) {
            customToast.createToast("원하는 스톤을 세공하는데 성공하였습니다!", Toast.LENGTH_SHORT);
            customToast.show();
            for (int i = 0; i < txtPick.length; i++) {
                txtPick[i].setVisibility(View.GONE);
            }
            return;
        } else if (ai.logic() == 10) {
            customToast.createToast("예상치 못한 상황 발생!!", Toast.LENGTH_SHORT);
            customToast.show();
            return;
        }
        for (int i = 0; i < txtPick.length; i++) {
            if (ai.logic() == i) {
                txtPick[i].setVisibility(View.VISIBLE);
            } else {
                txtPick[i].setVisibility(View.GONE);
            }
        }
    }

    private void reset() {
        btnConfirm.setEnabled(false);

        btnBurf1.setEnabled(true);
        btnBurf2.setEnabled(true);
        btnDeburf.setEnabled(true);
        for (int i = 0; i < AI_LENGTH; i++) {
            btnSuccess[i].setEnabled(true);
            btnFail[i].setEnabled(true);
        }

        txtPercent.setText("75");
        txtCount1.setText("0");
        txtCount2.setText("0");
        txtCount3.setText("0");

        burf1 = 0;
        burf2 = 0;
        deburf = 0;
        percent = 75;
        burf1_cnt = 0;
        burf2_cnt = 0;
        deburf_cnt = 0;
        history = "";

        for (int i = 0; i < 10; i++) {
            imgFirst[i].setImageResource(R.drawable.none);
            imgSecond[i].setImageResource(R.drawable.none);
            imgThird[i].setImageResource(R.drawable.deburf_none);
        }

        ai.reset();
        asyncAI();
        txtFail.setVisibility(View.GONE);
    }

    private boolean allMax() {
        if (burf1 == max && burf2 == max && deburf == max) return true;
        else return false;
    }

    public void loadStones() {
        stones.clear();
        stoneDBAdapter.open();
        Cursor cursor = stoneDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] stamps = new String[3];
            int[] stamp_cnts = new int[3];
            int rowID = cursor.getInt(0);
            stamps[0] = cursor.getString(2);
            stamps[1] = cursor.getString(3);
            stamps[2] = cursor.getString(4);
            stamp_cnts[0] = cursor.getInt(5);
            stamp_cnts[1] = cursor.getInt(6);
            stamp_cnts[2] = cursor.getInt(7);
            String history = cursor.getString(8);
            Stone stone = new Stone(cursor.getString(1), stamps, stamp_cnts, history, rowID);
            stones.add(stone);
            cursor.moveToNext();
        }
        Collections.reverse(stones);
        stoneDBAdapter.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadStones();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStones();
    }
}
