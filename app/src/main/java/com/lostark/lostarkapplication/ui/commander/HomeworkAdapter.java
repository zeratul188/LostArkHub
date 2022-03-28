package com.lostark.lostarkapplication.ui.commander;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.BossDBAdapter;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.DungeonDBAdapter;
import com.lostark.lostarkapplication.database.HistoryCountDBAdapter;
import com.lostark.lostarkapplication.database.HistoryDBAdapter;
import com.lostark.lostarkapplication.objects.History;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HomeworkAdapter extends BaseAdapter {
    private ArrayList<Checklist> checklists;
    private Context context;
    private ChracterDBAdapter chracterDBAdapter;
    private HistoryDBAdapter historyDBAdapter;
    private HistoryCountDBAdapter historyCountDBAdapter;
    private Activity activity;
    private CustomToast customToast;
    private SharedPreferences pref;
    private boolean isDay = true;
    private String chracter_name;
    private ChecklistIconAdapter iconAdapter;

    private final int MAX_DAY = 10;
    private final int MAX_WEEK = 12;

    private AlertDialog alertDialog, diag_alertDialog;

    public HomeworkAdapter(ArrayList<Checklist> checklists, Context context, ChracterDBAdapter chracterDBAdapter, Activity activity, boolean isDay, String chracter_name) {
        this.checklists = checklists;
        this.context = context;
        this.chracterDBAdapter = chracterDBAdapter;
        this.activity = activity;
        this.isDay = isDay;
        this.chracter_name = chracter_name;
        customToast = new CustomToast(context);
        historyCountDBAdapter = new HistoryCountDBAdapter(context);
        historyDBAdapter = new HistoryDBAdapter(context);
        pref = context.getSharedPreferences("setting_file", MODE_PRIVATE);
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
        TextView txtContent = convertView.findViewById(R.id.txtContent);
        TextView txtRest = convertView.findViewById(R.id.txtRest);
        ProgressBar progressRest = convertView.findViewById(R.id.progressRest);
        LinearLayout layoutRest = convertView.findViewById(R.id.layoutRest);
        ImageView imgIcon = convertView.findViewById(R.id.imgIcon);
        LinearLayout layoutAdd = convertView.findViewById(R.id.layoutAdd);

        List<String> homeworks;
        if (checklists.get(position).getIcon() == 0) {
            if (isDay) {
                homeworks = Arrays.asList(context.getResources().getStringArray(R.array.day_homework));
                switch (homeworks.indexOf(checklists.get(position).getName())) {
                    case 0: case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 9:
                        imgIcon.setImageResource(context.getResources().getIdentifier("hwid"+(homeworks.indexOf(checklists.get(position).getName())+1), "drawable", context.getPackageName()));
                        break;
                    default:
                        imgIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
                }
            } else {
                homeworks = Arrays.asList(context.getResources().getStringArray(R.array.week_homework));
                switch (homeworks.indexOf(checklists.get(position).getName())) {
                    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
                        imgIcon.setImageResource(context.getResources().getIdentifier("hwiw"+(homeworks.indexOf(checklists.get(position).getName())+1), "drawable", context.getPackageName()));
                        break;
                    default:
                        imgIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
                }
            }
        } else {
            if (checklists.get(position).getIcon() > 0 && checklists.get(position).getIcon() <= MAX_DAY) {
                imgIcon.setImageResource(context.getResources().getIdentifier("hwid"+checklists.get(position).getIcon(), "drawable", context.getPackageName()));
            } else if (checklists.get(position).getIcon() > MAX_DAY && checklists.get(position).getIcon() <= MAX_WEEK+MAX_DAY) {
                imgIcon.setImageResource(context.getResources().getIdentifier("hwiw"+(checklists.get(position).getIcon()-MAX_DAY), "drawable", context.getPackageName()));
            } else {
                imgIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
            }
        }

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialog_view = activity.getLayoutInflater().inflate(R.layout.icon_add_dialog, null);

                RecyclerView listView = dialog_view.findViewById(R.id.listView);
                Button btnEdit = dialog_view.findViewById(R.id.btnEdit);
                Button btnDelete = dialog_view.findViewById(R.id.btnDelete);

                ArrayList<Icon> icons = new ArrayList<>();
                for (int i = 1; i <= MAX_WEEK+MAX_DAY; i++) {
                    if (i == 7) continue;
                    icons.add(new Icon(i));
                }

                System.out.println(icons.size());
                GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
                listView.setLayoutManager(layoutManager);
                iconAdapter = new ChecklistIconAdapter(icons, context);
                listView.setAdapter(iconAdapter);
                iconAdapter.notifyDataSetChanged();

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iconAdapter.getIndex() != 0) {
                            chracterDBAdapter.open();
                            chracterDBAdapter.changeIcon(checklists.get(position).getName(), iconAdapter.getIndex());
                            chracterDBAdapter.close();
                            checklists.get(position).setIcon(iconAdapter.getIndex());
                            notifyDataSetChanged();
                            customToast.createToast("아이콘을 변경하였습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            alertDialog.dismiss();
                        } else {
                            customToast.createToast("변경할 아이콘을 선택하지 않았습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                        }
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chracterDBAdapter.open();
                        chracterDBAdapter.changeIcon(checklists.get(position).getName(), 0);
                        chracterDBAdapter.close();
                        checklists.get(position).setIcon(0);
                        notifyDataSetChanged();
                        customToast.createToast("아이콘이 기본값으로 초기화되었습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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

        txtName.setText(checklists.get(position).getName());
        txtNow.setText(Integer.toString(checklists.get(position).getNow()));
        txtMax.setText(Integer.toString(checklists.get(position).getMax()));
        if (checklists.get(position).isAlarm()) imgbtnAlarm.setImageResource(R.drawable.ic_notifications_black_24dp);
        else imgbtnAlarm.setImageResource(R.drawable.ic_notifications_off_black_24dp);
        if (checklists.get(position).getNow() >= checklists.get(position).getMax()) {
            imgbtnUp.setImageResource(R.drawable.ic_refresh_black_24dp);
            imgbtnUp.setBackgroundResource(R.drawable.homeworkbuttonresetstyle);
        } else {
            imgbtnUp.setImageResource(R.drawable.ic_baseline_check_24);
            imgbtnUp.setBackgroundResource(R.drawable.homeworkbuttonstyle);
        }
        progressBar.setMax(checklists.get(position).getMax());
        progressBar.setProgress(checklists.get(position).getNow());
        if (checklists.get(position).getContent().equals("")) txtContent.setVisibility(View.GONE);
        else {
            txtContent.setVisibility(View.VISIBLE);
            txtContent.setText(checklists.get(position).getContent());
        }

        Cursor cursor;
        int progress;
        switch (checklists.get(position).getName()) {
            case "카오스 던전":
                layoutRest.setVisibility(View.VISIBLE);
                chracterDBAdapter.open();
                cursor = chracterDBAdapter.fetchData("카던 휴식");
                cursor.moveToFirst();
                chracterDBAdapter.close();
                if (cursor.getCount() > 0) {
                    progress = cursor.getInt(3);
                    txtRest.setText(Integer.toString(progress*10));
                    progressRest.setProgress(progress);
                }
                break;
            case "가디언 토벌":
                layoutRest.setVisibility(View.VISIBLE);
                chracterDBAdapter.open();
                cursor = chracterDBAdapter.fetchData("가디언 휴식");
                cursor.moveToFirst();
                chracterDBAdapter.close();
                if (cursor.getCount() > 0) {
                    progress = cursor.getInt(3);
                    txtRest.setText(Integer.toString(progress*10));
                    progressRest.setProgress(progress);
                }
                break;
            case "에포나 일일 의뢰":
                layoutRest.setVisibility(View.VISIBLE);
                chracterDBAdapter.open();
                cursor = chracterDBAdapter.fetchData("에포나 휴식");
                cursor.moveToFirst();
                chracterDBAdapter.close();
                if (cursor.getCount() > 0) {
                    progress = cursor.getInt(3);
                    txtRest.setText(Integer.toString(progress*10));
                    progressRest.setProgress(progress);
                }
                break;
            default:
                layoutRest.setVisibility(View.GONE);
        }



        if (checklists.get(position).getType().equals("주간")) imgbtnAlarm.setVisibility(View.GONE);
        
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
                LinearLayout layoutSelect = dialog_view.findViewById(R.id.layoutSelect);
                ListView listSelect = dialog_view.findViewById(R.id.listSelect);
                LinearLayout layoutRest = dialog_view.findViewById(R.id.layoutRest);
                SeekBar seekRest = dialog_view.findViewById(R.id.seekRest);
                TextView txtRest = dialog_view.findViewById(R.id.txtRest);

                ArrayList<Select> selects = new ArrayList<>();
                if (checklists.get(position).getName().equals("카오스 던전")) {
                    layoutSelect.setVisibility(View.VISIBLE);
                    layoutRest.setVisibility(View.VISIBLE);
                    DungeonDBAdapter dungeonDBAdapter = new DungeonDBAdapter(activity);
                    for (int i = 0; i < dungeonDBAdapter.getSize(); i++) {
                        String[] args = dungeonDBAdapter.readData(i);
                        String content = args[0]+" : "+args[1];
                        selects.add(new Select(content));
                    }
                    chracterDBAdapter.open();
                    Cursor cursor = chracterDBAdapter.fetchData("카던 휴식");
                    cursor.moveToFirst();
                    chracterDBAdapter.close();
                    int progress = cursor.getInt(3);
                    seekRest.setProgress(progress);
                    txtRest.setText(Integer.toString(progress*10));
                } else if (checklists.get(position).getName().equals("가디언 토벌")) {
                    layoutSelect.setVisibility(View.VISIBLE);
                    layoutRest.setVisibility(View.VISIBLE);
                    BossDBAdapter bossDBAdapter = new BossDBAdapter(activity);
                    for (int i = 0; i < bossDBAdapter.getSize(); i++) {
                        String[] args = bossDBAdapter.readData(i);
                        String content = args[1]+" : "+args[0];
                        selects.add(new Select(content));
                    }
                    chracterDBAdapter.open();
                    Cursor cursor = chracterDBAdapter.fetchData("가디언 휴식");
                    cursor.moveToFirst();
                    chracterDBAdapter.close();
                    int progress = cursor.getInt(3);
                    seekRest.setProgress(progress);
                    txtRest.setText(Integer.toString(progress*10));
                } else if (checklists.get(position).getName().equals("에포나 일일 의뢰")) {
                    layoutSelect.setVisibility(View.GONE);
                    layoutRest.setVisibility(View.VISIBLE);
                    chracterDBAdapter.open();
                    Cursor cursor = chracterDBAdapter.fetchData("에포나 휴식");
                    cursor.moveToFirst();
                    chracterDBAdapter.close();
                    int progress = cursor.getInt(3);
                    seekRest.setProgress(progress);
                    txtRest.setText(Integer.toString(progress*10));
                } else {
                    layoutRest.setVisibility(View.GONE);
                    layoutSelect.setVisibility(View.GONE);
                }

                seekRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        txtRest.setText(Integer.toString(progress*10));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                String[] args = checklists.get(position).getContent().split("\n");
                for (String arg : args) {
                    if (selects.indexOf(new Select(arg)) != -1) selects.get(selects.indexOf(new Select(arg))).setChecked(true);
                }
                SelectAdapter selectAdapter = new SelectAdapter(selects, context);
                listSelect.setAdapter(selectAdapter);

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
                        int now = checklists.get(position).getNow();
                        int history = checklists.get(position).getHistory();
                        if (now > history) history = 0;
                        else history -= now;
                        historyCountDBAdapter.open();
                        String[] homeworks = {"카오스 던전", "가디언 토벌", "에포나 일일 의뢰"};
                        int count = 0;
                        if (Arrays.asList(homeworks).indexOf(checklists.get(position).getName()) != -1) {
                            count = historyCountDBAdapter.getQueryCount(homeworks[Arrays.asList(homeworks).indexOf(checklists.get(position).getName())]);
                            if (now > count) count = 0;
                            else count -= now;
                            historyCountDBAdapter.changeValue(homeworks[Arrays.asList(homeworks).indexOf(checklists.get(position).getName())], count);
                        }
                        for (String homework : homeworks) {
                            if (homework.equals(checklists.get(position).getName())) {
                                String[] args = {"카던 휴식", "가디언 휴식", "에포나 휴식"};
                                int pos = Arrays.asList(homeworks).indexOf(checklists.get(position).getName());
                                if (pos != -1) {
                                    chracterDBAdapter.open();
                                    int restCount = chracterDBAdapter.getRestCount(args[pos]);
                                    int now_rest = chracterDBAdapter.getRest(args[pos]);
                                    now_rest += restCount*2;
                                    chracterDBAdapter.changeRestCount(args[pos], 0);
                                    chracterDBAdapter.changeRest(args[pos], now_rest);
                                    chracterDBAdapter.close();
                                    for (Checklist checklist : checklists) {
                                        if (checklist.getName().equals(args[pos])) {
                                            checklist.setNow(now_rest);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        historyCountDBAdapter.close();
                        chracterDBAdapter.open();
                        chracterDBAdapter.changeNow(checklists.get(position).getName(), 0);
                        chracterDBAdapter.changeHistory(checklists.get(position).getName(), history);
                        chracterDBAdapter.close();
                        checklists.get(position).setNow(0);
                        checklists.get(position).setHistory(history);
                        imgbtnUp.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                        imgbtnUp.setBackgroundResource(R.drawable.homeworkbuttonstyle);
                        notifyDataSetChanged();
                        //Toast.makeText(context, "진행 상황을 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("진행 상황을 초기화하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        alertDialog.dismiss();
                        historyDBAdapter.open();
                        String date = getNowTime();
                        String content = "\""+checklists.get(position).getName()+"\"(을)를 초기화";
                        historyDBAdapter.insertData(new History(chracter_name, date, content));
                        historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                        historyDBAdapter.close();
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
                                //Toast.makeText(context, checklists.get(position).getName()+" 숙제를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                customToast.createToast(checklists.get(position).getName()+" 숙제를 삭제하였습니다.", Toast.LENGTH_SHORT);
                                customToast.show();
                                historyDBAdapter.open();
                                String date = getNowTime();
                                String content = "\""+checklists.get(position).getName()+"\" 항목 삭제";
                                historyDBAdapter.insertData(new History(chracter_name, date, content));
                                historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                                historyDBAdapter.close();
                                chracterDBAdapter.open();
                                chracterDBAdapter.deleteData(checklists.get(position).getName());
                                chracterDBAdapter.close();
                                String name = checklists.get(position).getName();
                                checklists.remove(position);
                                for (int i = 0; i < checklists.size(); i++) {
                                    if (name.equals("카오스 던전") && checklists.get(i).getName().equals("카던 휴식")) {
                                        checklists.remove(i);
                                        /*dungeonPackage.setText("0");
                                        dungeonPackage.setProgress(0);
                                        dungeonPackage.setVisible(View.GONE);*/
                                        chracterDBAdapter.open();
                                        chracterDBAdapter.deleteData("카던 휴식");
                                        chracterDBAdapter.close();
                                    } else if (name.equals("가디언 토벌") && checklists.get(i).getName().equals("가디언 휴식")) {
                                        checklists.remove(i);
                                        /*bossPackage.setText("0");
                                        bossPackage.setProgress(0);
                                        bossPackage.setVisible(View.GONE);*/
                                        chracterDBAdapter.open();
                                        chracterDBAdapter.deleteData("가디언 휴식");
                                        chracterDBAdapter.close();
                                    } else if (name.equals("에포나 일일 의뢰") && checklists.get(i).getName().equals("에포나 휴식")) {
                                        checklists.remove(i);
                                        /*questPackage.setText("0");
                                        questPackage.setProgress(0);
                                        questPackage.setVisible(View.GONE);*/
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
                            //Toast.makeText(context, "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("값이 비어있습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        }

                        String undo_name = checklists.get(position).getName();
                        int undo_max = checklists.get(position).getMax();
                        int undo_rest_progress = 0;
                        int after_rest_progress = 0;

                        String name = edtHomework.getText().toString();
                        int max = Integer.parseInt(edtCount.getText().toString());
                        int now = checklists.get(position).getNow();
                        if (now > max) now = max;

                        String content = "";
                        Checklist checklist;
                        switch (checklists.get(position).getName()) {
                            case "카오스 던전":
                                for (Select select : selects) {
                                    if (select.isChecked()) {
                                        if (!content.equals("")) content += "\n";
                                        content += select.getContent();
                                    }
                                }
                                if (isDay) checklist = new Checklist(name, "일일", content, now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                else checklist = new Checklist(name, "주간", content, now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                chracterDBAdapter.open();
                                chracterDBAdapter.changeNow("카던 휴식", seekRest.getProgress());
                                chracterDBAdapter.close();
                                undo_rest_progress = checklists.get(checklists.indexOf(new Checklist("카던 휴식"))).getNow();
                                after_rest_progress = seekRest.getProgress();
                                checklists.get(checklists.indexOf(new Checklist("카던 휴식"))).setNow(seekRest.getProgress());
                                break;
                            case "가디언 토벌":
                                for (Select select : selects) {
                                    if (select.isChecked()) {
                                        if (!content.equals("")) content += "\n";
                                        content += select.getContent();
                                    }
                                }
                                if (isDay) checklist = new Checklist(name, "일일", content, now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                else checklist = new Checklist(name, "주간", content, now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                chracterDBAdapter.open();
                                chracterDBAdapter.changeNow("가디언 휴식", seekRest.getProgress());
                                chracterDBAdapter.close();
                                undo_rest_progress = checklists.get(checklists.indexOf(new Checklist("가디언 휴식"))).getNow();
                                after_rest_progress = seekRest.getProgress();
                                checklists.get(checklists.indexOf(new Checklist("가디언 휴식"))).setNow(seekRest.getProgress());
                                break;
                            case "에포나 일일 의뢰":
                                if (isDay) checklist = new Checklist(name, "일일", "", now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                else checklist = new Checklist(name, "주간", "", now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                chracterDBAdapter.open();
                                chracterDBAdapter.changeNow("에포나 휴식", seekRest.getProgress());
                                chracterDBAdapter.close();
                                undo_rest_progress = checklists.get(checklists.indexOf(new Checklist("에포나 휴식"))).getNow();
                                after_rest_progress = seekRest.getProgress();
                                checklists.get(checklists.indexOf(new Checklist("에포나 휴식"))).setNow(seekRest.getProgress());
                            default:
                                if (isDay) checklist = new Checklist(name, "일일", "", now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                                else checklist = new Checklist(name, "주간", "", now, max, checklists.get(position).isAlarm(), checklists.get(position).getHistory());
                        }


                        chracterDBAdapter.open();
                        chracterDBAdapter.changeData(checklists.get(position).getName(), checklist);
                        chracterDBAdapter.close();
                        checklists.set(position, checklist);
                        notifyDataSetChanged();
                        //Toast.makeText(context, "값이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("값이 수정되었습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        alertDialog.dismiss();

                        historyDBAdapter.open();
                        String date = getNowTime();
                        if (undo_max != max) {
                            String contents = "\""+name+"\"의 최대치 "+undo_max+"에서 "+max+"으로 최대치 변경";
                            historyDBAdapter.insertData(new History(chracter_name, date, contents));
                            historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                        }
                        if (!undo_name.equals(name)) {
                            String contents = "\""+undo_name+"\"(을)를 \""+name+"\"으로 이름 변경";
                            historyDBAdapter.insertData(new History(chracter_name, date, contents));
                            historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                        }
                        if (undo_rest_progress != after_rest_progress) {
                            String contents = "\""+name+"\"의 휴식 게이지를 "+(undo_rest_progress*10)+"에서 "+(after_rest_progress*10)+"으로 변경";
                            historyDBAdapter.insertData(new History(chracter_name, date, contents));
                            historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                        }
                        historyDBAdapter.close();
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
                historyDBAdapter.open();
                String date = getNowTime();
                String content;
                if (checklists.get(position).isAlarm()) content = "\""+checklists.get(position).getName()+"\"의 알림 킴";
                else content = "\""+checklists.get(position).getName()+"\"의 알림 끔";
                historyDBAdapter.insertData(new History(chracter_name, date, content));
                historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                historyDBAdapter.close();
            }
        });

        imgbtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checklists.get(position).getNow() >= checklists.get(position).getMax()) {
                    int now = checklists.get(position).getNow();
                    int history = checklists.get(position).getHistory();
                    if (now > history) history = 0;
                    else history -= now;
                    historyCountDBAdapter.open();
                    String[] homeworks = {"카오스 던전", "가디언 토벌", "에포나 일일 의뢰"};
                    int count = 0;
                    if (Arrays.asList(homeworks).indexOf(checklists.get(position).getName()) != -1) {
                        count = historyCountDBAdapter.getQueryCount(homeworks[Arrays.asList(homeworks).indexOf(checklists.get(position).getName())]);
                        if (now > count) count = 0;
                        else count -= now;
                        historyCountDBAdapter.changeValue(homeworks[Arrays.asList(homeworks).indexOf(checklists.get(position).getName())], count);
                    }
                    for (String homework : homeworks) {
                        if (homework.equals(checklists.get(position).getName())) {
                            String[] args = {"카던 휴식", "가디언 휴식", "에포나 휴식"};
                            int pos = Arrays.asList(homeworks).indexOf(checklists.get(position).getName());
                            if (pos != -1) {
                                chracterDBAdapter.open();
                                int restCount = chracterDBAdapter.getRestCount(args[pos]);
                                int now_rest = chracterDBAdapter.getRest(args[pos]);
                                now_rest += restCount*2;
                                chracterDBAdapter.changeRestCount(args[pos], 0);
                                chracterDBAdapter.changeRest(args[pos], now_rest);
                                chracterDBAdapter.close();
                                for (Checklist checklist : checklists) {
                                    if (checklist.getName().equals(args[pos])) {
                                        checklist.setNow(now_rest);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    historyCountDBAdapter.close();
                    chracterDBAdapter.open();
                    chracterDBAdapter.changeNow(checklists.get(position).getName(), 0);
                    chracterDBAdapter.changeHistory(checklists.get(position).getName(), history);
                    chracterDBAdapter.close();
                    checklists.get(position).setNow(0);
                    checklists.get(position).setHistory(history);
                    imgbtnUp.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                    imgbtnUp.setBackgroundResource(R.drawable.homeworkbuttonstyle);
                    //Toast.makeText(context, "진행 상황을 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                    customToast.createToast("진행 상황을 초기화하였습니다.", Toast.LENGTH_SHORT);
                    customToast.show();
                    historyDBAdapter.open();
                    DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
                    Calendar calendar = Calendar.getInstance();
                    String date = df.format(calendar.getTime());
                    String content = checklists.get(position).getName()+"(을)를 초기화";
                    historyDBAdapter.insertData(new History(chracter_name, date, content));
                    historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                    historyDBAdapter.close();
                } else {
                    checklists.get(position).setNow(checklists.get(position).getNow()+1);
                    checklists.get(position).setHistory(checklists.get(position).getHistory()+1);
                    if (checklists.get(position).getNow() >= checklists.get(position).getMax()) {
                        imgbtnUp.setImageResource(R.drawable.ic_refresh_black_24dp);
                        imgbtnUp.setBackgroundResource(R.drawable.homeworkbuttonresetstyle);
                    }
                    chracterDBAdapter.open();
                    chracterDBAdapter.changeHistory(checklists.get(position).getName(), checklists.get(position).getHistory());
                    chracterDBAdapter.changeNow(checklists.get(position).getName(), checklists.get(position).getNow());
                    if (checklists.get(position).getName().equals("카오스 던전")) {
                        historyCountDBAdapter.open();
                        historyCountDBAdapter.changeValue("카오스 던전", historyCountDBAdapter.getQueryCount("카오스 던전")+1);
                        historyCountDBAdapter.close();
                        for (int i = 0; i < checklists.size(); i++) {
                            if (checklists.get(i).getName().equals("카던 휴식")) {
                                if (checklists.get(i).getNow() >= 2) {
                                    checklists.get(i).setNow(checklists.get(i).getNow() - 2);
                                    int restCount = chracterDBAdapter.getRestCount(checklists.get(i).getName());
                                    chracterDBAdapter.changeRestCount(checklists.get(i).getName(), restCount+1);
                                }
                                chracterDBAdapter.changeNow(checklists.get(i).getName(), checklists.get(i).getNow());
                            }
                        }
                    } else if (checklists.get(position).getName().equals("가디언 토벌")) {
                        historyCountDBAdapter.open();
                        historyCountDBAdapter.changeValue("가디언 토벌", historyCountDBAdapter.getQueryCount("가디언 토벌")+1);
                        historyCountDBAdapter.close();
                        for (int i = 0; i < checklists.size(); i++) {
                            if (checklists.get(i).getName().equals("가디언 휴식")) {
                                if (checklists.get(i).getNow() >= 2) {
                                    checklists.get(i).setNow(checklists.get(i).getNow() - 2);
                                    int restCount = chracterDBAdapter.getRestCount(checklists.get(i).getName());
                                    chracterDBAdapter.changeRestCount(checklists.get(i).getName(), restCount+1);
                                }
                                chracterDBAdapter.changeNow(checklists.get(i).getName(), checklists.get(i).getNow());
                            }
                        }
                    } else if (checklists.get(position).getName().equals("에포나 일일 의뢰")) {
                        historyCountDBAdapter.open();
                        historyCountDBAdapter.changeValue("에포나 일일 의뢰", historyCountDBAdapter.getQueryCount("에포나 일일 의뢰")+1);
                        historyCountDBAdapter.close();
                        for (int i = 0; i < checklists.size(); i++) {
                            if (checklists.get(i).getName().equals("에포나 휴식")) {
                                if (checklists.get(i).getNow() >= 2) {
                                    checklists.get(i).setNow(checklists.get(i).getNow() - 2);
                                    int restCount = chracterDBAdapter.getRestCount(checklists.get(i).getName());
                                    chracterDBAdapter.changeRestCount(checklists.get(i).getName(), restCount+1);
                                }
                                chracterDBAdapter.changeNow(checklists.get(i).getName(), checklists.get(i).getNow());
                            }
                        }
                    }
                    chracterDBAdapter.close();
                    historyDBAdapter.open();
                    String date = getNowTime();
                    String content = "\""+checklists.get(position).getName()+"\" 숙제 체크";
                    historyDBAdapter.insertData(new History(chracter_name, date, content));
                    historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                    if (checklists.get(position).getNow() == checklists.get(position).getMax()) {
                        content = "\""+checklists.get(position).getName()+"\" 숙제 완료";
                        historyDBAdapter.insertData(new History(chracter_name, date, content));
                        historyDBAdapter.limitDelete(pref.getInt("limit_count", 300));
                    }
                    historyDBAdapter.close();
                }
                notifyDataSetChanged();
            }
        });

        if (checklists.get(position).getType().equals("휴식게이지")) layoutMain.setVisibility(View.GONE);
        else layoutMain.setVisibility(View.VISIBLE);

        return convertView;
    }

    private String getNowTime() {
        DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }
}
