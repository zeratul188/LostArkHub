package com.example.lostarkapplication.ui.gallery;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lostarkapplication.R;
import com.example.lostarkapplication.database.StampDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private ImageView imgStone, imgBurf1, imgBurf2, imgDeburf;
    private TextView txtTitle, txtGrade, txtBurf1, txtBurf2, txtDeburf, txtPercent, txtCount1, txtCount2, txtCount3;
    private Button btnChange, btnBurf1, btnBurf2, btnDeburf, btnConfirm;
    private ImageView[] imgFirst = new ImageView[10];
    private ImageView[] imgSecond = new ImageView[10];
    private ImageView[] imgThird = new ImageView[10];

    private StampDBAdapter stampDBAdapter;
    private AlertDialog stampDialog, alertDialog;
    private DataNetwork dn;

    private int max = 0;
    private int burf1 = 0, burf2 = 0, deburf = 0;
    private int burf1_cnt = 0, burf2_cnt = 0, deburf_cnt = 0;
    private int percent = 75;

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
        for (int i = 0; i < imgFirst.length; i++) {
            imgFirst[i] = root.findViewById(getActivity().getResources().getIdentifier("imgFirst"+(i+1), "id", getActivity().getPackageName()));
            imgSecond[i] = root.findViewById(getActivity().getResources().getIdentifier("imgSecond"+(i+1), "id", getActivity().getPackageName()));
            imgThird[i] = root.findViewById(getActivity().getResources().getIdentifier("imgThird"+(i+1), "id", getActivity().getPackageName()));
        }

        stampDBAdapter = new StampDBAdapter(getActivity());

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.selectstampdialog, null);

                ImageView imgSelectStone = view.findViewById(R.id.imgSelectStone);
                TextView txtName = view.findViewById(R.id.txtName);
                Spinner sprGrade = view.findViewById(R.id.sprGrade);
                ImageView imgSelectBurf1 = view.findViewById(R.id.imgSelectBurf1);
                TextView txtSelectBurf1 = view.findViewById(R.id.txtSelectBurf1);
                Button btnChange1 = view.findViewById(R.id.btnChange1);
                ImageView imgSelectBurf2 = view.findViewById(R.id.imgSelectBurf2);
                TextView txtSelectBurf2 = view.findViewById(R.id.txtSelectBurf2);
                Button btnChange2 = view.findViewById(R.id.btnChange2);
                ImageView imgSelectDeburf = view.findViewById(R.id.imgSelectDeburf);
                TextView txtSelectDeburf = view.findViewById(R.id.txtSelectDeburf);
                Button btnChange3 = view.findViewById(R.id.btnChange3);
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
                                txtGrade.setTextColor(Color.parseColor("#2093A8"));
                                imgStone.setImageResource(R.drawable.stone1);
                                max = 6;
                                break;
                            case "영웅":
                                txtTitle.setText("뛰어난 비상의 돌");
                                txtGrade.setTextColor(Color.parseColor("#9B53D2"));
                                imgStone.setImageResource(R.drawable.stone2);
                                max = 8;
                                break;
                            case "전설":
                                txtTitle.setText("강력한 비상의 돌");
                                txtGrade.setTextColor(Color.parseColor("#C2873B"));
                                imgStone.setImageResource(R.drawable.stone3);
                                max = 9;
                                break;
                            case "유물":
                                txtTitle.setText("고고한 비상의 돌");
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

                        alertDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnChange1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                        ListView listView = view.findViewById(R.id.listView);
                        Button btnCancel = view.findViewById(R.id.btnCancel);

                        ArrayList<Stamp> stamps = new ArrayList<>();
                        for (int i = 0; i < 85; i++) {
                            String[] arr = stampDBAdapter.readData(i);
                            Stamp stamp = new Stamp(arr[0], arr[1]);
                            stamps.add(stamp);
                        }

                        StampAdapter stampAdapter = new StampAdapter(stamps, getActivity(), dn);
                        listView.setAdapter(stampAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), stamps.get(position).getName()+"/"+stamps.get(position).getImage(), Toast.LENGTH_LONG).show();
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

                btnChange2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                        ListView listView = view.findViewById(R.id.listView);
                        Button btnCancel = view.findViewById(R.id.btnCancel);

                        ArrayList<Stamp> stamps = new ArrayList<>();
                        for (int i = 0; i < 85; i++) {
                            String[] arr = stampDBAdapter.readData(i);
                            Stamp stamp = new Stamp(arr[0], arr[1]);
                            stamps.add(stamp);
                        }

                        StampAdapter stampAdapter = new StampAdapter(stamps, getActivity(), dn);
                        listView.setAdapter(stampAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), stamps.get(position).getName()+"/"+stamps.get(position).getImage(), Toast.LENGTH_LONG).show();
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

                btnChange3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.stamplistlayout, null);

                        ListView listView = view.findViewById(R.id.listView);
                        Button btnCancel = view.findViewById(R.id.btnCancel);

                        ArrayList<Stamp> stamps = new ArrayList<>();
                        for (int i = 85; i < 89; i++) {
                            String[] arr = stampDBAdapter.readData(i);
                            Stamp stamp = new Stamp(arr[0], arr[1]);
                            stamps.add(stamp);
                        }

                        StampAdapter stampAdapter = new StampAdapter(stamps, getActivity(), dn);
                        listView.setAdapter(stampAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), stamps.get(position).getName()+"/"+stamps.get(position).getImage(), Toast.LENGTH_LONG).show();
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
                int ransu = (int)(Math.random()*123456)%100;
                if (ransu <= percent) {
                    imgFirst[burf1].setImageResource(R.drawable.success);
                    burf1_cnt++;
                    txtCount1.setText(Integer.toString(burf1_cnt));
                    if (percent > 25) percent -= 10;
                } else {
                    imgFirst[burf1].setImageResource(R.drawable.fail);
                    if (percent < 75) percent += 10;
                }
                burf1++;
                txtPercent.setText(Integer.toString(percent));
                if (burf1 == max) btnBurf1.setEnabled(false);
                if (allMax()) btnConfirm.setEnabled(true);
            }
        });

        btnBurf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ransu = (int)(Math.random()*123456)%100;
                if (ransu <= percent) {
                    imgSecond[burf2].setImageResource(R.drawable.success);
                    burf2_cnt++;
                    txtCount2.setText(Integer.toString(burf2_cnt));
                    if (percent > 25) percent -= 10;
                } else {
                    imgSecond[burf2].setImageResource(R.drawable.fail);
                    if (percent < 75) percent += 10;
                }
                burf2++;
                txtPercent.setText(Integer.toString(percent));
                if (burf2 == max) btnBurf2.setEnabled(false);
                if (allMax()) btnConfirm.setEnabled(true);
            }
        });

        btnDeburf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ransu = (int)(Math.random()*123456)%100;
                if (ransu <= percent) {
                    imgThird[deburf].setImageResource(R.drawable.deburf_success);
                    deburf_cnt++;
                    txtCount3.setText(Integer.toString(deburf_cnt));
                    if (percent > 25) percent -= 10;
                } else {
                    imgThird[deburf].setImageResource(R.drawable.deburf_fail);
                    if (percent < 75) percent += 10;
                }
                deburf++;
                txtPercent.setText(Integer.toString(percent));
                if (deburf == max) btnDeburf.setEnabled(false);
                if (allMax()) btnConfirm.setEnabled(true);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgStone.setImageResource(0);
                txtTitle.setText("스톤을 선택하세요");
                txtGrade.setText("-");
                imgBurf1.setImageResource(0);
                imgBurf2.setImageResource(0);
                imgDeburf.setImageResource(0);
                for (int i = 0; i < 10; i++) {
                    imgFirst[i].setVisibility(View.INVISIBLE);
                    imgFirst[i].setImageResource(R.drawable.none);
                    imgSecond[i].setVisibility(View.INVISIBLE);
                    imgSecond[i].setImageResource(R.drawable.none);
                    imgThird[i].setVisibility(View.INVISIBLE);
                    imgThird[i].setImageResource(R.drawable.deburf_none);
                }
                txtBurf1.setText("-");
                txtBurf2.setText("-");
                txtDeburf.setText("-");
                btnConfirm.setEnabled(false);
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
            }
        });

        return root;
    }

    private boolean allMax() {
        if (burf1 == max && burf2 == max && deburf == max) return true;
        else return false;
    }
}
