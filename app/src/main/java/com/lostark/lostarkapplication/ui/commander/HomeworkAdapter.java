package com.lostark.lostarkapplication.ui.commander;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;

import java.util.ArrayList;

public class HomeworkAdapter extends BaseAdapter {
    private ArrayList<Checklist> checklists;
    private Context context;
    private ChracterDBAdapter chracterDBAdapter;
    private Activity activity;
    private boolean isDay = true;
    
    private RestPackage dungeonPackage, bossPackage, questPackage;

    private AlertDialog alertDialog, diag_alertDialog;

    public HomeworkAdapter(ArrayList<Checklist> checklists, Context context, ChracterDBAdapter chracterDBAdapter, Activity activity, boolean isDay) {
        this.checklists = checklists;
        this.context = context;
        this.chracterDBAdapter = chracterDBAdapter;
        this.activity = activity;
        this.isDay = isDay;
        dungeonPackage = new RestPackage();
        bossPackage = new RestPackage();
        questPackage = new RestPackage();
    }
    
    public void setQuestPackage(LinearLayout layout, TextView txtView, ProgressBar progressBar) {
        questPackage.setLayout(layout);
        questPackage.setProgressBar(progressBar);
        questPackage.setTxtView(txtView);
    }
    
    public void setDungeonPackage(LinearLayout layout, TextView txtView, ProgressBar progressBar) {
        dungeonPackage.setLayout(layout);
        dungeonPackage.setProgressBar(progressBar);
        dungeonPackage.setTxtView(txtView);
    }

    public void setBossPackage(LinearLayout layout, TextView txtView, ProgressBar progressBar) {
        bossPackage.setLayout(layout);
        bossPackage.setProgressBar(progressBar);
        bossPackage.setTxtView(txtView);
    }

    @Override
    public int getCount() {
        return checklists.size();
    }

    @Override
    public Object getItem(int position) {
        return checklists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.day_homework_list, null);

        ImageButton imgbtnAlarm = convertView.findViewById(R.id.imgbtnAlarm);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtNow = convertView.findViewById(R.id.txtNow);
        TextView txtMax = convertView.findViewById(R.id.txtMax);
        ImageButton imgbtnUp = convertView.findViewById(R.id.imgbtnUp);
        LinearLayout layoutMain = convertView.findViewById(R.id.layoutMain);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        txtName.setText(checklists.get(position).getName());
        txtNow.setText(Integer.toString(checklists.get(position).getNow()));
        txtMax.setText(Integer.toString(checklists.get(position).getMax()));
        if (checklists.get(position).isAlarm()) imgbtnAlarm.setImageResource(R.drawable.ic_notifications_black_24dp);
        else imgbtnAlarm.setImageResource(R.drawable.ic_notifications_off_black_24dp);
        if (checklists.get(position).getNow() >= checklists.get(position).getMax()) imgbtnUp.setEnabled(false);
        else imgbtnUp.setEnabled(true);
        progressBar.setMax(checklists.get(position).getMax());
        progressBar.setProgress(checklists.get(position).getNow());

        if (checklists.get(position).getType().equals("주간")) imgbtnAlarm.setVisibility(View.INVISIBLE);
        
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialog_view = activity.getLayoutInflater().inflate(R.layout.edit_homework_dialog, null);

                EditText edtHomework = dialog_view.findViewById(R.id.edtHomework);
                EditText edtCount = dialog_view.findViewById(R.id.edtCount);
                Button btnDelete = dialog_view.findViewById(R.id.btnDelete);
                Button btnEdit = dialog_view.findViewById(R.id.btnEdit);
                Button btnReset = dialog_view.findViewById(R.id.btnReset);
                ImageButton btnNameCopy = dialog_view.findViewById(R.id.btnNameCopy);
                ImageButton btnCountCopy = dialog_view.findViewById(R.id.btnCountCopy);

                edtHomework.setHint(checklists.get(position).getName());
                edtCount.setHint(checklists.get(position).getMax()+" (0~99)");

                btnNameCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtHomework.setText(checklists.get(position).getName());
                    }
                });

                btnCountCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtCount.setText(Integer.toString(checklists.get(position).getMax()));
                    }
                });

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chracterDBAdapter.open();
                        chracterDBAdapter.changeNow(checklists.get(position).getName(), 0);
                        chracterDBAdapter.close();
                        checklists.get(position).setNow(0);
                        imgbtnUp.setEnabled(true);
                        notifyDataSetChanged();
                        Toast.makeText(context, "진행 상황을 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View diag_view = activity.getLayoutInflater().inflate(R.layout.yesnodialog, null);

                        TextView txtContent = diag_view.findViewById(R.id.txtContent);
                        Button btnCancel = diag_view.findViewById(R.id.btnCancel);
                        Button btnOK = diag_view.findViewById(R.id.btnOK);

                        txtContent.setText(checklists.get(position).getName()+"의 숙제를 삭제하시겠습니까?");
                        btnOK.setText("삭제");

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                diag_alertDialog.dismiss();
                            }
                        });

                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, checklists.get(position).getName()+" 숙제를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                chracterDBAdapter.open();
                                chracterDBAdapter.deleteData(checklists.get(position).getName());
                                chracterDBAdapter.close();
                                String name = checklists.get(position).getName();
                                checklists.remove(position);
                                for (int i = 0; i < checklists.size(); i++) {
                                    if (name.equals("카오스 던전") && checklists.get(i).getName().equals("카던 휴식")) {
                                        checklists.remove(i);
                                        dungeonPackage.setText("0");
                                        dungeonPackage.setProgress(0);
                                        dungeonPackage.setVisible(View.GONE);
                                        chracterDBAdapter.open();
                                        chracterDBAdapter.deleteData("카던 휴식");
                                        chracterDBAdapter.close();
                                    } else if (name.equals("가디언 토벌") && checklists.get(i).getName().equals("가디언 휴식")) {
                                        checklists.remove(i);
                                        bossPackage.setText("0");
                                        bossPackage.setProgress(0);
                                        bossPackage.setVisible(View.GONE);
                                        chracterDBAdapter.open();
                                        chracterDBAdapter.deleteData("가디언 휴식");
                                        chracterDBAdapter.close();
                                    } else if (name.equals("에포나 일일 의뢰") && checklists.get(i).getName().equals("에포나 휴식")) {
                                        checklists.remove(i);
                                        questPackage.setText("0");
                                        questPackage.setProgress(0);
                                        questPackage.setVisible(View.GONE);
                                        chracterDBAdapter.open();
                                        chracterDBAdapter.deleteData("에포나 휴식");
                                        chracterDBAdapter.close();
                                    }
                                }
                                diag_alertDialog.dismiss();
                                notifyDataSetChanged();
                                alertDialog.dismiss();
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(diag_view);

                        diag_alertDialog = builder.create();
                        diag_alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        diag_alertDialog.show();
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtHomework.getText().toString().equals("") || edtCount.getText().toString().equals("")) {
                            Toast.makeText(context, "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String name = edtHomework.getText().toString();
                        int max = Integer.parseInt(edtCount.getText().toString());
                        Checklist checklist;
                        if (isDay) checklist = new Checklist(name, "일일", 0, max, true);
                        else checklist = new Checklist(name, "주간", 0, max, true);
                        chracterDBAdapter.open();
                        chracterDBAdapter.changeData(checklists.get(position).getName(), checklist);
                        chracterDBAdapter.close();
                        checklists.set(position, checklist);
                        notifyDataSetChanged();
                        Toast.makeText(context, "값이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialog_view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imgbtnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chracterDBAdapter.open();
                if (checklists.get(position).isAlarm()) {
                    checklists.get(position).setAlarm(false);
                    chracterDBAdapter.changeAlarm(checklists.get(position).getName(), false);
                } else {
                    checklists.get(position).setAlarm(true);
                    chracterDBAdapter.changeAlarm(checklists.get(position).getName(), true);
                }
                chracterDBAdapter.close();
                notifyDataSetChanged();
            }
        });

        imgbtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklists.get(position).setNow(checklists.get(position).getNow()+1);
                if (checklists.get(position).getNow() >= checklists.get(position).getMax()) imgbtnUp.setEnabled(false);
                else imgbtnUp.setEnabled(true);
                chracterDBAdapter.open();
                chracterDBAdapter.changeNow(checklists.get(position).getName(), checklists.get(position).getNow());
                if (checklists.get(position).getName().equals("카오스 던전")) {
                    for (int i = 0; i < checklists.size(); i++) {
                        if (checklists.get(i).getName().equals("카던 휴식")) {
                            if (checklists.get(i).getNow() >= 2) checklists.get(i).setNow(checklists.get(i).getNow() - 2);
                            chracterDBAdapter.changeNow(checklists.get(i).getName(), checklists.get(i).getNow());
                            dungeonPackage.setProgress(checklists.get(i).getNow());
                            dungeonPackage.setText(Integer.toString(checklists.get(i).getNow()*10));
                        }
                    }
                } else if (checklists.get(position).getName().equals("가디언 토벌")) {
                    for (int i = 0; i < checklists.size(); i++) {
                        if (checklists.get(i).getName().equals("가디언 휴식")) {
                            if (checklists.get(i).getNow() >= 2) checklists.get(i).setNow(checklists.get(i).getNow() - 2);
                            chracterDBAdapter.changeNow(checklists.get(i).getName(), checklists.get(i).getNow());
                            bossPackage.setProgress(checklists.get(i).getNow());
                            bossPackage.setText(Integer.toString(checklists.get(i).getNow()*10));
                        }
                    }
                } else if (checklists.get(position).getName().equals("에포나 일일 의뢰")) {
                    for (int i = 0; i < checklists.size(); i++) {
                        if (checklists.get(i).getName().equals("에포나 휴식")) {
                            if (checklists.get(i).getNow() >= 2) checklists.get(i).setNow(checklists.get(i).getNow() - 2);
                            chracterDBAdapter.changeNow(checklists.get(i).getName(), checklists.get(i).getNow());
                            questPackage.setProgress(checklists.get(i).getNow());
                            questPackage.setText(Integer.toString(checklists.get(i).getNow()*10));
                        }
                    }
                }
                chracterDBAdapter.close();
                notifyDataSetChanged();
            }
        });

        if (checklists.get(position).getType().equals("휴식게이지")) layoutMain.setVisibility(View.GONE);
        else layoutMain.setVisibility(View.VISIBLE);

        return convertView;
    }
}
