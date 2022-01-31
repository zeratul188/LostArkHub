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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.StampDBAdapter;
import com.lostark.lostarkapplication.database.StoneDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private ImageView imgStone, imgBurf1, imgBurf2, imgDeburf;
    private TextView txtTitle, txtGrade, txtBurf1, txtBurf2, txtDeburf, txtPercent, txtCount1, txtCount2, txtCount3;
    private Button btnChange, btnBurf1, btnBurf2, btnDeburf, btnConfirm, btnReset;
    private ListView listView;
    private ImageView[] imgFirst = new ImageView[10];
    private ImageView[] imgSecond = new ImageView[10];
    private ImageView[] imgThird = new ImageView[10];
    private LinearLayout layoutHistory;
    private CheckBox chkKeep;

    private StampDBAdapter stampDBAdapter;
    private StoneDBAdapter stoneDBAdapter;
    private StoneAdapter stoneAdapter;
    private AlertDialog stampDialog, alertDialog;
    private DataNetwork dn;
    private ArrayList<Stone> stones;
    private CustomToast customToast;
    private Thread th = null;

    private SharedPreferences pref;

    private int max = 0;
    private int burf1 = 0, burf2 = 0, deburf = 0;
    private int burf1_cnt = 0, burf2_cnt = 0, deburf_cnt = 0;
    private int percent = 75;
    private String history = "";

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
        btnChange = root.findViewById(R.id.btnChange);
        btnBurf1 = root.findViewById(R.id.btnBurf1);
        btnBurf2 = root.findViewById(R.id.btnBurf2);
        btnDeburf = root.findViewById(R.id.btnDeburf);
        txtCount1 = root.findViewById(R.id.txtCount1);
        txtCount2 = root.findViewById(R.id.txtCount2);
        txtCount3 = root.findViewById(R.id.txtCount3);
        btnConfirm = root.findViewById(R.id.btnConfirm);
        btnReset = root.findViewById(R.id.btnReset);
        listView = root.findViewById(R.id.listView);
        chkKeep = root.findViewById(R.id.chkKeep);
        layoutHistory = root.findViewById(R.id.layoutHistory);
        for (int i = 0; i < imgFirst.length; i++) {
            imgFirst[i] = root.findViewById(getActivity().getResources().getIdentifier("imgFirst"+(i+1), "id", getActivity().getPackageName()));
            imgSecond[i] = root.findViewById(getActivity().getResources().getIdentifier("imgSecond"+(i+1), "id", getActivity().getPackageName()));
            imgThird[i] = root.findViewById(getActivity().getResources().getIdentifier("imgThird"+(i+1), "id", getActivity().getPackageName()));
        }

        stampDBAdapter = new StampDBAdapter(getActivity());
        stoneAdapter = new StoneAdapter(stones, getActivity());
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
                Spinner sprSecond = dialog_view.findViewById(R.id.sprSecond);
                Button btnStart = dialog_view.findViewById(R.id.btnStart);
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
                String[] speeds = {"느리게", "보통", "빠르게"};
                List<String> listSpeeds = Arrays.asList(speeds);
                ArrayAdapter<String> speedAdapter = new ArrayAdapter<String>(getActivity(), R.layout.abilitystonehistoryitem, listSpeeds);
                speedAdapter.setDropDownViewResource(R.layout.abilitystonehistoryitem);
                sprSecond.setAdapter(speedAdapter);
                sprSecond.setSelection(1);

                th = new Thread(thread);

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {double second = 0;
                        switch (sprSecond.getSelectedItem().toString()) {
                            case "느리게":
                                second = 1;
                                break;
                            case "보통":
                                second = 0.6;
                                break;
                            case "빠르게":
                                second = 0.3;
                                break;
                        }
                        thread.setSecond(second);
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

        pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
        stoneDBAdapter = new StoneDBAdapter(getActivity());

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.selectstampdialog, null);

                ImageView imgSelectStone = view.findViewById(R.id.imgSelectStone);
                TextView txtName = view.findViewById(R.id.txtName);
                Spinner sprGrade = view.findViewById(R.id.sprGrade);
                ImageView imgSelectBurf1 = view.findViewById(R.id.imgSelectBurf1);
                TextView txtSelectBurf1 = view.findViewById(R.id.txtSelectBurf1);
                ImageView imgSelectBurf2 = view.findViewById(R.id.imgSelectBurf2);
                TextView txtSelectBurf2 = view.findViewById(R.id.txtSelectBurf2);
                ImageView imgSelectDeburf = view.findViewById(R.id.imgSelectDeburf);
                TextView txtSelectDeburf = view.findViewById(R.id.txtSelectDeburf);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                List<String> grades = Arrays.asList(getActivity().getResources().getStringArray(R.array.ability_stone));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.abilitystoneitem, grades);
                adapter.setDropDownViewResource(R.layout.abilitystoneitem);
                sprGrade.setAdapter(adapter);

                sprGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        imgSelectStone.setImageResource(getActivity().getResources().getIdentifier("stone"+(position+1), "drawable", getActivity().getPackageName()));
                        //String[] names = {"비상의 돌", "뛰어난 비상의 돌", "강력한 비상의 돌", "고고한 비상의 돌"};
                        switch (grades.get(position)) {
                            case "희귀":
                                txtName.setText("비상의 돌");
                                txtName.setTextColor(Color.parseColor("#2093A8"));
                                break;
                            case "영웅":
                                txtName.setText("뛰어난 비상의 돌");
                                txtName.setTextColor(Color.parseColor("#9B53D2"));
                                break;
                            case "전설":
                                txtName.setText("강력한 비상의 돌");
                                txtName.setTextColor(Color.parseColor("#C2873B"));
                                break;
                            case "유물":
                                txtName.setText("고고한 비상의 돌");
                                txtName.setTextColor(Color.parseColor("#BF5700"));
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (sprGrade.getSelectedItem().toString()) {
                            case "희귀":
                                txtTitle.setText("비상의 돌");
                                txtGrade.setText("희귀");
                                txtGrade.setTextColor(Color.parseColor("#2093A8"));
                                imgStone.setImageResource(R.drawable.stone1);
                                max = 6;
                                break;
                            case "영웅":
                                txtTitle.setText("뛰어난 비상의 돌");
                                txtGrade.setText("영웅");
                                txtGrade.setTextColor(Color.parseColor("#9B53D2"));
                                imgStone.setImageResource(R.drawable.stone2);
                                max = 8;
                                break;
                            case "전설":
                                txtTitle.setText("강력한 비상의 돌");
                                txtGrade.setText("전설");
                                txtGrade.setTextColor(Color.parseColor("#C2873B"));
                                imgStone.setImageResource(R.drawable.stone3);
                                max = 9;
                                break;
                            case "유물":
                                txtTitle.setText("고고한 비상의 돌");
                                txtGrade.setText("유물");
                                txtGrade.setTextColor(Color.parseColor("#BF5700"));
                                imgStone.setImageResource(R.drawable.stone4);
                                max = 10;
                                break;
                        }

                        String[] arr1 = stampDBAdapter.readData(txtSelectBurf1.getText().toString());
                        String[] arr2 = stampDBAdapter.readData(txtSelectBurf2.getText().toString());
                        String[] arr3 = stampDBAdapter.readData(txtSelectDeburf.getText().toString());

                        imgBurf1.setImageResource(getActivity().getResources().getIdentifier(arr1[1], "drawable", getActivity().getPackageName()));
                        imgBurf2.setImageResource(getActivity().getResources().getIdentifier(arr2[1], "drawable", getActivity().getPackageName()));
                        imgDeburf.setImageResource(getActivity().getResources().getIdentifier(arr3[1], "drawable", getActivity().getPackageName()));
                        txtBurf1.setText(arr1[0]);
                        txtBurf2.setText(arr2[0]);
                        txtDeburf.setText(arr3[0]);

                        for (int i = 0; i < max; i++) {
                            imgFirst[i].setVisibility(View.VISIBLE);
                            imgSecond[i].setVisibility(View.VISIBLE);
                            imgThird[i].setVisibility(View.VISIBLE);
                        }
                        for (int i = max; i < imgFirst.length; i++) {
                            imgFirst[i].setVisibility(View.INVISIBLE);
                            imgSecond[i].setVisibility(View.INVISIBLE);
                            imgThird[i].setVisibility(View.INVISIBLE);
                        }

                        btnBurf1.setEnabled(true);
                        btnBurf2.setEnabled(true);
                        btnDeburf.setEnabled(true);
                        btnChange.setEnabled(false);
                        btnReset.setEnabled(true);

                        alertDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                txtSelectBurf1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                        ListView listView = view.findViewById(R.id.listView);
                        Button btnCancel = view.findViewById(R.id.btnCancel);

                        ArrayList<Stamp> stamps = new ArrayList<>();
                        for (int i = 0; i < 87; i++) {
                            String[] arr = stampDBAdapter.readData(i);
                            Stamp stamp = new Stamp(arr[0], arr[1]);
                            if (!stamp.getName().equals(txtSelectBurf2.getText().toString())) stamps.add(stamp);
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
                                    imgSelectBurf1.setImageResource(getActivity().getResources().getIdentifier(arr[1], "drawable", getActivity().getPackageName()));
                                    txtSelectBurf1.setText(arr[0]);
                                }
                            }
                        });

                        stampDialog.setCancelable(false);
                        stampDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        stampDialog.show();
                    }
                });

                txtSelectBurf2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                        ListView listView = view.findViewById(R.id.listView);
                        Button btnCancel = view.findViewById(R.id.btnCancel);

                        ArrayList<Stamp> stamps = new ArrayList<>();
                        for (int i = 0; i < 87; i++) {
                            String[] arr = stampDBAdapter.readData(i);
                            Stamp stamp = new Stamp(arr[0], arr[1]);
                            if (!stamp.getName().equals(txtSelectBurf1.getText().toString())) stamps.add(stamp);
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
                                    imgSelectBurf2.setImageResource(getActivity().getResources().getIdentifier(arr[1], "drawable", getActivity().getPackageName()));
                                    txtSelectBurf2.setText(arr[0]);
                                }
                            }
                        });

                        stampDialog.setCancelable(false);
                        stampDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        stampDialog.show();
                    }
                });

                txtSelectDeburf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                        ListView listView = view.findViewById(R.id.listView);
                        Button btnCancel = view.findViewById(R.id.btnCancel);

                        ArrayList<Stamp> stamps = new ArrayList<>();
                        for (int i = 87; i < 91; i++) {
                            String[] arr = stampDBAdapter.readData(i);
                            Stamp stamp = new Stamp(arr[0], arr[1]);
                            stamps.add(stamp);
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
                                    imgSelectDeburf.setImageResource(getActivity().getResources().getIdentifier(arr[1], "drawable", getActivity().getPackageName()));
                                    txtSelectDeburf.setText(arr[0]);
                                }
                            }
                        });

                        stampDialog.setCancelable(false);
                        stampDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        stampDialog.show();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
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
                } else {
                    imgFirst[burf1].setImageResource(R.drawable.fail);
                    if (percent < 75) percent += 10;
                    result = "fail";
                }
                burf1++;
                txtPercent.setText(Integer.toString(percent));
                if (burf1 == max) btnBurf1.setEnabled(false);
                if (allMax()) {
                    btnConfirm.setEnabled(true);
                    btnReset.setEnabled(true);
                }
                if (!history.equals("")) history += "|";
                history += "1/\""+txtBurf1.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
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
                } else {
                    imgSecond[burf2].setImageResource(R.drawable.fail);
                    if (percent < 75) percent += 10;
                    result = "fail";
                }
                burf2++;
                txtPercent.setText(Integer.toString(percent));
                if (burf2 == max) btnBurf2.setEnabled(false);
                if (allMax()) {
                    btnConfirm.setEnabled(true);
                    btnReset.setEnabled(true);
                }
                if (!history.equals("")) history += "|";
                history += "2/\""+txtBurf2.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
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
                } else {
                    imgThird[deburf].setImageResource(R.drawable.deburf_fail);
                    if (percent < 75) percent += 10;
                    result = "fail";
                }
                deburf++;
                txtPercent.setText(Integer.toString(percent));
                if (deburf == max) btnDeburf.setEnabled(false);
                if (allMax()) {
                    btnConfirm.setEnabled(true);
                    btnReset.setEnabled(true);
                }
                if (!history.equals("")) history += "|";
                history += "3/\""+txtDeburf.getText().toString()+"\" 세공 ("+burf1_cnt+", "+burf2_cnt+", "+deburf_cnt+") - "+now_percent+"%/"+result;
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
                Stone stone = new Stone(txtGrade.getText().toString(), stamps, stamp_cnts, history);

                stoneDBAdapter.open();
                stoneDBAdapter.insertData(stone);
                stoneDBAdapter.close();

                stones.add(stone);
                stoneAdapter.notifyDataSetChanged();

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

    private void reset() {
        if (!chkKeep.isChecked()) {
            imgStone.setImageResource(R.drawable.none_stone);
            txtTitle.setText("스톤을 선택하세요");
            txtGrade.setText("");
            imgBurf1.setImageResource(R.drawable.none_stamp);
            imgBurf2.setImageResource(R.drawable.none_stamp);
            imgDeburf.setImageResource(R.drawable.none_stamp);
            txtBurf1.setText("-");
            txtBurf2.setText("-");
            txtDeburf.setText("-");

            btnConfirm.setEnabled(false);
            btnReset.setEnabled(false);
            btnChange.setEnabled(true);

            for (int i = 0; i < 10; i++) {
                imgFirst[i].setVisibility(View.INVISIBLE);
                imgFirst[i].setImageResource(R.drawable.none);
                imgSecond[i].setVisibility(View.INVISIBLE);
                imgSecond[i].setImageResource(R.drawable.none);
                imgThird[i].setVisibility(View.INVISIBLE);
                imgThird[i].setImageResource(R.drawable.deburf_none);
            }
        } else {
            btnBurf1.setEnabled(true);
            btnBurf2.setEnabled(true);
            btnDeburf.setEnabled(true);

            for (int i = 0; i < 10; i++) {
                imgFirst[i].setImageResource(R.drawable.none);
                imgSecond[i].setImageResource(R.drawable.none);
                imgThird[i].setImageResource(R.drawable.deburf_none);
            }
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
            stamps[0] = cursor.getString(2);
            stamps[1] = cursor.getString(3);
            stamps[2] = cursor.getString(4);
            stamp_cnts[0] = cursor.getInt(5);
            stamp_cnts[1] = cursor.getInt(6);
            stamp_cnts[2] = cursor.getInt(7);
            String history = cursor.getString(8);
            Stone stone = new Stone(cursor.getString(1), stamps, stamp_cnts, history);
            stones.add(stone);
            cursor.moveToNext();
        }
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
        if (!pref.getBoolean("stone_history", false)) layoutHistory.setVisibility(View.VISIBLE);
        else layoutHistory.setVisibility(View.GONE);
    }
}
