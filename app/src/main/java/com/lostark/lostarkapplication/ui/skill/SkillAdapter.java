package com.lostark.lostarkapplication.ui.skill;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.JobTripodDBAdapter;
import com.lostark.lostarkapplication.database.RuneDBAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SkillAdapter extends BaseAdapter {
    private ArrayList<Skill> skills;
    private Context context;
    private DataNetwork dataNetwork;
    private Activity activity;
    private CustomToast customToast;

    private RuneDBAdapter runeDBAdapter;

    private Bitmap[] bitmaps;
    private AlertDialog alertDialog;
    private int job_index = 9999;

    private boolean isShow = false, isRetripod = false;

    private int[] checklist = new int[3];

    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            // 원래 하려던 동작 (UI변경 작업 등)
            notifyDataSetChanged();
        }
    };

    public SkillAdapter(ArrayList<Skill> skills, Context context, DataNetwork dataNetwork, Activity activity) {
        this.skills = skills;
        this.context = context;
        this.dataNetwork = dataNetwork;
        this.activity = activity;
        runeDBAdapter = new RuneDBAdapter(activity);
        customToast = new CustomToast(context);
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getJob_index() {
        return job_index;
    }

    public void setJob_index(int job_index) {
        this.job_index = job_index;
    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return skills.size();
    }

    @Override
    public Object getItem(int position) {
        return skills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.skill_list, null);

        ImageView imgSkill = convertView.findViewById(R.id.imgSkill);
        TextView txtSkillName = convertView.findViewById(R.id.txtSkillName);
        TextView txtSkillLevel = convertView.findViewById(R.id.txtSkillLevel);
        TextView txtNeedSkillPoint = convertView.findViewById(R.id.txtNeedSkillPoint);
        ImageView imgTripod = convertView.findViewById(R.id.imgTripod);
        ImageButton imgbtnIncrease = convertView.findViewById(R.id.imgbtnIncrease);
        ImageButton imgbtnDecrease = convertView.findViewById(R.id.imgbtnDecrease);
        LinearLayout layoutNeedSkillPoint = convertView.findViewById(R.id.layoutNeedSkillPoint);
        LinearLayout layoutSetting = convertView.findViewById(R.id.layoutSetting);
        ImageView[] imgTripods = new ImageView[3];
        TextView txtRune = convertView.findViewById(R.id.txtRune);
        ImageView imgRune = convertView.findViewById(R.id.imgRune);
        ImageView imgRuneBackground = convertView.findViewById(R.id.imgRuneBackground);
        for (int i = 0; i < imgTripods.length; i++) imgTripods[i] = convertView.findViewById(context.getResources().getIdentifier("imgTripod"+(i+1), "id", context.getPackageName()));

        if (bitmaps != null) imgSkill.setImageBitmap(bitmaps[position]);
        else imgSkill.setImageResource(R.drawable.close_eye);
        txtSkillName.setText(skills.get(position).getName());
        txtSkillLevel.setText(Integer.toString(skills.get(position).getLevel()));
        int need_point = 1;
        if (getBetween(skills.get(position).getLevel(), 1, 3)) need_point = 1;
        else if (skills.get(position).getLevel() == 3) need_point = 2;
        else if (getBetween(skills.get(position).getLevel(), 4, 6)) need_point = 4;
        else if (getBetween(skills.get(position).getLevel(), 6, 9)) need_point = 8;
        else if (skills.get(position).getLevel() == 9) need_point = 12;
        else if (getBetween(skills.get(position).getLevel(), 10, 12)) need_point = 6;
        else need_point = 9999;

        if (need_point == 9999 || (skills.get(position).getMax_level() == 10 && skills.get(position).getLevel() == 10)) txtNeedSkillPoint.setText("MAX");
        else txtNeedSkillPoint.setText(Integer.toString(need_point));

        int stack = 0;
        for (int tripod : skills.get(position).getTripods()) {
            if (tripod != 4) {
                stack++;
            }
        }

        if (skills.get(position).getRuneBitmap() != null) {
            imgRune.setImageBitmap(skills.get(position).getRuneBitmap());
        } else {
            imgRune.setImageResource(R.drawable.ic_add_black_24dp);
        }

        if (skills.get(position).getTripodBitmaps() != null) {
            for (int i = 0; i < 3; i++) {
                if (skills.get(position).getTripodBitmaps()[i] != null) imgTripods[i].setImageBitmap(skills.get(position).getTripodBitmaps()[i]);
                else imgTripods[i].setImageResource(R.drawable.close_eye);
            }
        }

        if (job_index != 9999) {
            JobTripodDBAdapter jobTripodDBAdapter = new JobTripodDBAdapter(activity, job_index+1);
            int first = 0, second = 0, third = 0;
            for (int i = 0; i < jobTripodDBAdapter.getSize(); i++) {
                String[] args = jobTripodDBAdapter.readData(i);
                if (Integer.parseInt(args[0]) == position+1) {
                    switch (Integer.parseInt(args[1])) {
                        case 1:
                            if (skills.get(position).getTripods()[0] == first && skills.get(position).getTripods()[0] < 4) {
                                //new DownloadFilesTask(imgTripods[0], position, 0).execute(args[4]);
                                if (skills.get(position).getTripodBitmaps()[0] == null) new TripodDownloadFilesTask(position, 0, imgTripods).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[4]);
                            }
                            first++;
                            break;
                        case 2:
                            if (skills.get(position).getTripods()[1] == second && skills.get(position).getTripods()[1] < 4) {
                                //new DownloadFilesTask(imgTripods[1], position, 1).execute(args[4]);
                                if (skills.get(position).getTripodBitmaps()[1] == null) new TripodDownloadFilesTask(position, 1, imgTripods).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[4]);
                            }
                            second++;
                            break;
                        case 3:
                            if (skills.get(position).getTripods()[2] == third && skills.get(position).getTripods()[2] < 4) {
                                //new DownloadFilesTask(imgTripods[2], position, 2).execute(args[4]);
                                if (skills.get(position).getTripodBitmaps()[2] == null) new TripodDownloadFilesTask(position, 2, imgTripods).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[4]);
                            }
                            third++;
                            break;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                if (skills.get(position).getTripods()[i] == 4) {
                    imgTripods[i].setImageResource(R.drawable.close_eye);
                }
            }
        }

        if (skills.get(position).getRune() < runeDBAdapter.getSize()) {
            String[] args = runeDBAdapter.readData(skills.get(position).getRune());
            int grade = Integer.parseInt(args[1]);
            if (skills.get(position).getRuneBitmap() == null) new RuneDownloadFilesTask(position, imgRune).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[3]);
            switch (grade) {
                case 1:
                    txtRune.setTextColor(Color.parseColor("#8dbe46"));
                    imgRuneBackground.setImageResource(R.drawable.rune1);
                    txtRune.setText("고급 ");
                    break;
                case 2:
                    txtRune.setTextColor(Color.parseColor("#3b6fa7"));
                    imgRuneBackground.setImageResource(R.drawable.rune2);
                    txtRune.setText("희귀 ");
                    break;
                case 3:
                    txtRune.setTextColor(Color.parseColor("#9056c3"));
                    imgRuneBackground.setImageResource(R.drawable.rune3);
                    txtRune.setText("영웅 ");
                    break;
                case 4:
                    txtRune.setTextColor(Color.parseColor("#c1751d"));
                    imgRuneBackground.setImageResource(R.drawable.rune4);
                    txtRune.setText("전설 ");
                    break;
            }
            txtRune.setText(txtRune.getText().toString()+args[0]+" 룬");
        } else {
            txtRune.setTextColor(Color.parseColor("#FFFFFF"));
            imgRuneBackground.setImageResource(R.drawable.rune0);
            imgRune.setImageResource(R.drawable.ic_add_black_24dp);
            txtRune.setText("");
        }


        imgRune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt = 0;
                for (Skill skill : skills) if (skill.getRune() != 99) cnt++;
                if (cnt >= 8 && skills.get(position).getRune() == 99) {
                    //Toast.makeText(context, "더 이상 룬을 설정하실 수 없습니다. (최대 8개까지 설정 가능)", Toast.LENGTH_SHORT).show();
                    customToast.createToast("더 이상 룬을 설정하실 수 없습니다. (최대 8개까지 설정 가능)", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }

                View dialog_view = activity.getLayoutInflater().inflate(R.layout.rune_dialog, null);

                ListView listView = dialog_view.findViewById(R.id.listView);
                Button btnDelete = dialog_view.findViewById(R.id.btnDelete);
                RadioGroup rgGrade = dialog_view.findViewById(R.id.rgGrade);
                TextView txtLimit = dialog_view.findViewById(R.id.txtLimit);

                txtLimit.setText("("+cnt+"/8)");

                RadioButton[] rdoRune = new RadioButton[5];
                for (int i = 0; i < rdoRune.length; i++) {
                    rdoRune[i] = dialog_view.findViewById(context.getResources().getIdentifier("rdoRune"+(i+1), "id", context.getPackageName()));
                }

                ArrayList<Rune> runes = new ArrayList<>();
                for (int i = 0; i < runeDBAdapter.getSize(); i++) {
                    String[] args = runeDBAdapter.readData(i);
                    int count = 0;
                    for (Skill skill : skills) {
                        if (skill.getRune() == i) {
                            count++;
                        }
                    }
                    runes.add(new Rune(args[0], args[2], args[3], Integer.parseInt(args[1]), i, count));
                }
                RuneAdapter runeAdapter = new RuneAdapter(context, runes);
                listView.setAdapter(runeAdapter);

                rgGrade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        runes.clear();
                        switch (checkedId) {
                            case R.id.rdoRune1:
                                for (int i = 0; i < runeDBAdapter.getSize(); i++) {
                                    String[] args = runeDBAdapter.readData(i);
                                    int count = 0;
                                    for (Skill skill : skills) {
                                        if (skill.getRune() == i) {
                                            count++;
                                        }
                                    }
                                    runes.add(new Rune(args[0], args[2], args[3], Integer.parseInt(args[1]), i, count));
                                }
                                break;
                            case R.id.rdoRune2:
                                for (int i = 0; i < runeDBAdapter.getSize(); i++) {
                                    String[] args = runeDBAdapter.readData(i);
                                    int count = 0;
                                    for (Skill skill : skills) {
                                        if (skill.getRune() == i) {
                                            count++;
                                        }
                                    }
                                    if (Integer.parseInt(args[1]) == 4) runes.add(new Rune(args[0], args[2], args[3], Integer.parseInt(args[1]), i, count));
                                }
                                break;
                            case R.id.rdoRune3:
                                for (int i = 0; i < runeDBAdapter.getSize(); i++) {
                                    String[] args = runeDBAdapter.readData(i);
                                    int count = 0;
                                    for (Skill skill : skills) {
                                        if (skill.getRune() == i) {
                                            count++;
                                        }
                                    }
                                    if (Integer.parseInt(args[1]) == 3) runes.add(new Rune(args[0], args[2], args[3], Integer.parseInt(args[1]), i, count));
                                }
                                break;
                            case R.id.rdoRune4:
                                for (int i = 0; i < runeDBAdapter.getSize(); i++) {
                                    String[] args = runeDBAdapter.readData(i);
                                    int count = 0;
                                    for (Skill skill : skills) {
                                        if (skill.getRune() == i) {
                                            count++;
                                        }
                                    }
                                    if (Integer.parseInt(args[1]) == 2) runes.add(new Rune(args[0], args[2], args[3], Integer.parseInt(args[1]), i, count));
                                }
                                break;
                            case R.id.rdoRune5:
                                for (int i = 0; i < runeDBAdapter.getSize(); i++) {
                                    String[] args = runeDBAdapter.readData(i);
                                    int count = 0;
                                    for (Skill skill : skills) {
                                        if (skill.getRune() == i) {
                                            count++;
                                        }
                                    }
                                    if (Integer.parseInt(args[1]) == 1) runes.add(new Rune(args[0], args[2], args[3], Integer.parseInt(args[1]), i, count));
                                }
                                break;
                        }
                        runeAdapter.notifyDataSetChanged();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        skills.get(position).setRune(99);
                        //Toast.makeText(context, "룬을 제거하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("룬을 제거하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        skills.get(position).setRune(runes.get(i).getIndex());
                        skills.get(position).setRuneBitmap(null);
                        //Toast.makeText(context, "룬을 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("룬을 설정하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        notifyDataSetChanged();
                        new SleepNotifyThread().start();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(dialog_view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imgSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.skill_information_dialog, null);

                ImageView imgSkill = view.findViewById(R.id.imgSkill);
                TextView txtName = view.findViewById(R.id.txtName);
                TextView txtTime = view.findViewById(R.id.txtTime);
                TextView txtStrike = view.findViewById(R.id.txtStrike);
                TextView txtAttackType = view.findViewById(R.id.txtAttackType);
                TextView txtDestroyLevel = view.findViewById(R.id.txtDestroyLevel);
                TextView txtContent = view.findViewById(R.id.txtContent);
                TableRow trStrike = view.findViewById(R.id.trStrike);
                TableRow trAttackType = view.findViewById(R.id.trAttackType);
                TableRow trDestroyLevel = view.findViewById(R.id.trDestroyLevel);

                new DownloadFilesTask(imgSkill).execute(skills.get(position).getUrl());
                txtName.setText(skills.get(position).getName());
                txtTime.setText(skills.get(position).getTime()+"초");
                if (skills.get(position).getStrike().equals("-")) trStrike.setVisibility(View.GONE);
                else txtStrike.setText(skills.get(position).getStrike());
                if (skills.get(position).getAttack_type().equals("-")) trAttackType.setVisibility(View.GONE);
                else txtAttackType.setText(skills.get(position).getAttack_type());
                if (skills.get(position).getDestroy_level().equals("-")) trDestroyLevel.setVisibility(View.GONE);
                else txtDestroyLevel.setText(skills.get(position).getDestroy_level());
                txtContent.setText(skills.get(position).getContent());

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imgTripod.setImageResource(context.getResources().getIdentifier("skill"+(stack+1), "drawable", context.getPackageName()));

        if (skills.get(position).getMax_level() == 0) {
            layoutSetting.setVisibility(View.GONE);
            layoutNeedSkillPoint.setVisibility(View.GONE);
            imgTripod.setVisibility(View.GONE);
            for (ImageView imgView : imgTripods) imgView.setVisibility(View.GONE);
            txtSkillName.setTextColor(Color.parseColor("#FE6E0E"));
        } else {
            layoutSetting.setVisibility(View.VISIBLE);
            layoutNeedSkillPoint.setVisibility(View.VISIBLE);
            imgTripod.setVisibility(View.VISIBLE);
            //for (ImageView imgView : imgTripods) imgView.setVisibility(View.VISIBLE);
            for (int i = 0; i < 3; i++) {
                if (skills.get(position).getTripods()[i] == 4) {
                    imgTripods[i].setVisibility(View.GONE);
                } else {
                    imgTripods[i].setVisibility(View.VISIBLE);
                }
            }
            txtSkillName.setTextColor(Color.parseColor("#FFFFFF"));
        }



        if (skills.get(position).getLevel() == skills.get(position).getMax_level()) imgbtnIncrease.setEnabled(false);
        else imgbtnIncrease.setEnabled(true);
        if (skills.get(position).getLevel() == 1) imgbtnDecrease.setEnabled(false);
        else imgbtnDecrease.setEnabled(true);

        imgTripod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBetween(skills.get(position).getLevel(), 1, 4)) {
                    //Toast.makeText(context, "스킬 레벨이 낮아 트라이포드를 설정하실 수 없습니다. (최소 레벨 : 4)", Toast.LENGTH_SHORT).show();
                    customToast.createToast("스킬 레벨이 낮아 트라이포드를 설정하실 수 없습니다. (최소 레벨 : 4)", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                View view = activity.getLayoutInflater().inflate(R.layout.tripod_layout, null);

                for (int i = 0; i < skills.get(position).getTripods().length; i++) {
                    if (skills.get(position).getTripods()[i] == 4) checklist[i] = 0;
                    else checklist[i] = skills.get(position).getTripods()[i];
                }

                LinearLayout layoutFirst = view.findViewById(R.id.layoutFirst);
                LinearLayout layoutSecond = view.findViewById(R.id.layoutSecond);
                LinearLayout layoutThird = view.findViewById(R.id.layoutThird);
                Button btnSetting = view.findViewById(R.id.btnSetting);
                LinearLayout[][] layoutTripod = new LinearLayout[3][3];
                ImageView[][] imgTripodss = new ImageView[3][3];
                TextView[][] txtTripod = new TextView[3][3];
                TextView[][] txtTripodContent = new TextView[3][3];;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (i == 2 && j == 2) continue;
                        layoutTripod[i][j] = view.findViewById(context.getResources().getIdentifier("layoutTripod"+(i+1)+(j+1), "id", context.getPackageName()));
                        imgTripodss[i][j] = view.findViewById(context.getResources().getIdentifier("imgTripod"+(i+1)+(j+1), "id", context.getPackageName()));
                        txtTripod[i][j] = view.findViewById(context.getResources().getIdentifier("txtTripod"+(i+1)+(j+1), "id", context.getPackageName()));
                        txtTripodContent[i][j] = view.findViewById(context.getResources().getIdentifier("txtTripodContent"+(i+1)+(j+1), "id", context.getPackageName()));

                        if (skills.get(position).getTripods()[i] != 4) {
                            if (j == skills.get(position).getTripods()[i]) {
                                switch (i) {
                                    case 0:  //2D8C98
                                        layoutTripod[i][j].setBackgroundColor(Color.parseColor("#2D8C98"));
                                        txtTripodContent[i][j].setBackgroundResource(R.drawable.tripod1_checked_content_background);
                                        break;
                                    case 1: //#6A833D
                                        layoutTripod[i][j].setBackgroundColor(Color.parseColor("#6A833D"));
                                        txtTripodContent[i][j].setBackgroundResource(R.drawable.tripod2_checked_content_background);
                                        break;
                                    case 2: //#BF9131
                                        layoutTripod[i][j].setBackgroundColor(Color.parseColor("#BF9131"));
                                        txtTripodContent[i][j].setBackgroundResource(R.drawable.tripod3_checked_content_background);
                                        break;
                                }
                            } else {
                                layoutTripod[i][j].setBackgroundColor(Color.parseColor("#40aaaaaa"));
                                txtTripodContent[i][j].setBackgroundResource(R.drawable.tripod_content_background);
                            }
                        }


                        final int first_index = i;
                        final int second_index = j;
                        layoutTripod[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int length;
                                if (first_index == 2) length = 2;
                                else length = 3;
                                for (int i = 0; i < length; i++) {
                                    if (i == second_index) {
                                        switch (first_index) {
                                            case 0:  //2D8C98
                                                layoutTripod[first_index][i].setBackgroundColor(Color.parseColor("#2D8C98"));
                                                txtTripodContent[first_index][i].setBackgroundResource(R.drawable.tripod1_checked_content_background);
                                                break;
                                            case 1: //#6A833D
                                                layoutTripod[first_index][i].setBackgroundColor(Color.parseColor("#6A833D"));
                                                txtTripodContent[first_index][i].setBackgroundResource(R.drawable.tripod2_checked_content_background);
                                                break;
                                            case 2: //#BF9131
                                                layoutTripod[first_index][i].setBackgroundColor(Color.parseColor("#BF9131"));
                                                txtTripodContent[first_index][i].setBackgroundResource(R.drawable.tripod3_checked_content_background);
                                                break;
                                        }

                                    } else {
                                        layoutTripod[first_index][i].setBackgroundColor(Color.parseColor("#40aaaaaa"));
                                        txtTripodContent[first_index][i].setBackgroundResource(R.drawable.tripod_content_background);
                                    }
                                }

                                checklist[first_index] = second_index;
                            }
                        });
                    }
                }

                JobTripodDBAdapter jobTripodDBAdapter = new JobTripodDBAdapter(activity, job_index+1);

                int first = 0, second = 0, third = 0;
                for (int i = 0; i < jobTripodDBAdapter.getSize(); i++) {
                    String[] args = jobTripodDBAdapter.readData(i);
                    if (Integer.parseInt(args[0]) == position+1) {
                        switch (Integer.parseInt(args[1])) {
                            case 1:
                                if (first < 3) {
                                    new DownloadFilesTask(imgTripodss[0][first]).execute(args[4]);
                                    txtTripod[0][first].setText(args[2]);
                                    txtTripodContent[0][first].setText(args[3]);
                                    first++;
                                }
                                break;
                            case 2:
                                if (second < 3) {
                                    new DownloadFilesTask(imgTripodss[1][second]).execute(args[4]);
                                    txtTripod[1][second].setText(args[2]);
                                    txtTripodContent[1][second].setText(args[3]);
                                    second++;
                                }
                                break;
                            case 3:
                                if (third < 2) {
                                    new DownloadFilesTask(imgTripodss[2][third]).execute(args[4]);
                                    txtTripod[2][third].setText(args[2]);
                                    txtTripodContent[2][third].setText(args[3]);
                                    third++;
                                }
                                break;
                        }
                    }

                }

                if (getBetween(skills.get(position).getLevel(), 4, 7)) {
                    layoutSecond.setVisibility(View.GONE);
                    layoutThird.setVisibility(View.GONE);
                } else if (getBetween(skills.get(position).getLevel(), 7, 10)) {
                    layoutThird.setVisibility(View.GONE);
                }

                btnSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getBetween(skills.get(position).getLevel(), 4, 7)) {
                            checklist[1] = 4;
                            checklist[2] = 4;
                        } else if (getBetween(skills.get(position).getLevel(), 7, 10)) {
                            checklist[2] = 4;
                        }
                        skills.get(position).setTripods(checklist);
                        skills.get(position).setTripodBitmaps(new Bitmap[3]);
                        isRetripod = true;

                        JobTripodDBAdapter jobTripodDBAdapter = new JobTripodDBAdapter(activity, job_index+1);
                        int first = 0, second = 0, third = 0;
                        for (int i = 0; i < jobTripodDBAdapter.getSize(); i++) {
                            String[] args = jobTripodDBAdapter.readData(i);
                            if (Integer.parseInt(args[0]) == position+1) {
                                switch (Integer.parseInt(args[1])) {
                                    case 1:
                                        if (skills.get(position).getTripods()[0] == first && skills.get(position).getTripods()[0] < 4) {
                                            //new DownloadFilesTask(imgTripods[0], position, 0).execute(args[4]);
                                            if (skills.get(position).getTripodBitmaps()[0] == null) new TripodDownloadFilesTask(position, 0, imgTripods).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[4]);
                                        }
                                        first++;
                                        break;
                                    case 2:
                                        if (skills.get(position).getTripods()[1] == second && skills.get(position).getTripods()[1] < 4) {
                                            //new DownloadFilesTask(imgTripods[1], position, 1).execute(args[4]);
                                            if (skills.get(position).getTripodBitmaps()[1] == null) new TripodDownloadFilesTask(position, 1, imgTripods).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[4]);
                                        }
                                        second++;
                                        break;
                                    case 3:
                                        if (skills.get(position).getTripods()[2] == third && skills.get(position).getTripods()[2] < 4) {
                                            //new DownloadFilesTask(imgTripods[2], position, 2).execute(args[4]);
                                            if (skills.get(position).getTripodBitmaps()[2] == null) new TripodDownloadFilesTask(position, 2, imgTripods).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args[4]);
                                        }
                                        third++;
                                        break;
                                }
                            }
                        }
                        for (int i = 0; i < 3; i++) {
                            if (skills.get(position).getTripods()[i] == 4) {
                                imgTripods[i].setImageResource(R.drawable.close_eye);
                            }
                        }

                        int st = 0;
                        for (int tripod : skills.get(position).getTripods()) {
                            if (tripod != 4) {
                                st++;
                            }
                        }
                        imgTripod.setImageResource(context.getResources().getIdentifier("skill"+(st+1), "drawable", context.getPackageName()));

                        for (int i = 0; i < 3; i++) {
                            if (skills.get(position).getTripods()[i] == 4) {
                                imgTripods[i].setVisibility(View.GONE);
                            } else {
                                imgTripods[i].setVisibility(View.VISIBLE);
                            }
                        }

                        //notifyDataSetChanged();
                        //Toast.makeText(context, "트라이포드를 저장하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("트라이포드를 저장하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        alertDialog.dismiss();
                        //new SleepNotifyThread().start();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imgbtnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int next_need_point = Integer.parseInt(txtNeedSkillPoint.getText().toString());
                if (dataNetwork.getSkillpoint() < next_need_point) {
                    //Toast.makeText(context, "스킬 포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                    customToast.createToast("스킬 포인트가 부족합니다.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                dataNetwork.setSkillpoint(dataNetwork.getSkillpoint() - next_need_point);
                skills.get(position).setLevel(skills.get(position).getLevel()+1);
                dataNetwork.getTxtView().setText(dataNetwork.getSkillpoint()+"/"+dataNetwork.getMax());
                if (dataNetwork.getMax() != 0) dataNetwork.getProgressBar().setProgress(dataNetwork.getSkillpoint());
                notifyDataSetChanged();
            }
        });

        imgbtnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skills.get(position).setLevel(skills.get(position).getLevel()-1);
                if (getBetween(skills.get(position).getLevel(), 1, 3)) dataNetwork.setSkillpoint(dataNetwork.getSkillpoint()+1);
                else if (skills.get(position).getLevel() == 3) dataNetwork.setSkillpoint(dataNetwork.getSkillpoint()+2);
                else if (getBetween(skills.get(position).getLevel(), 4, 6)) dataNetwork.setSkillpoint(dataNetwork.getSkillpoint()+4);
                else if (getBetween(skills.get(position).getLevel(), 6, 9)) dataNetwork.setSkillpoint(dataNetwork.getSkillpoint()+8);
                else if (skills.get(position).getLevel() == 9) dataNetwork.setSkillpoint(dataNetwork.getSkillpoint()+12);
                else if (getBetween(skills.get(position).getLevel(), 10, 12)) dataNetwork.setSkillpoint(dataNetwork.getSkillpoint()+6);
                dataNetwork.getTxtView().setText(dataNetwork.getSkillpoint()+"/"+dataNetwork.getMax());
                if (dataNetwork.getMax() != 0) dataNetwork.getProgressBar().setProgress(dataNetwork.getSkillpoint());

                if (skills.get(position).getLevel() < 4) for (int i = 0; i < skills.get(position).getTripods().length; i++) skills.get(position).getTripods()[i] = 4;
                else if (skills.get(position).getLevel() >= 4 && skills.get(position).getLevel() < 7) for (int i = 1; i < skills.get(position).getTripods().length; i++) skills.get(position).getTripods()[i] = 4;
                else if (skills.get(position).getLevel() >= 7 && skills.get(position).getLevel() < 10) for (int i = 2; i < skills.get(position).getTripods().length; i++) skills.get(position).getTripods()[i] = 4;
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private boolean getBetween(int value, int start, int end) {
        if (value >= start && value < end) return true;
        return false;
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        private ImageView imgView;

        public DownloadFilesTask(ImageView imgView) {
            this.imgView = imgView;
        }

        public DownloadFilesTask() {
        }

        public ImageView getImgView() {
            return imgView;
        }

        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            imgView.setImageBitmap(result);
        }
    }

    private class RuneDownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        private int position;
        private ImageView imgView;

        public RuneDownloadFilesTask(int position, ImageView imgView) {
            this.position = position;
            this.imgView = imgView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            try {
                skills.get(position).setRuneBitmap(result);
                imgView.setImageBitmap(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TripodDownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        private ImageView[] imgView;
        private int position, index;

        public TripodDownloadFilesTask(int position, int index, ImageView[] imgView) {
            this.position = position;
            this.index = index;
            this.imgView = imgView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            try {
                Bitmap[] temp = skills.get(position).getTripodBitmaps();
                temp[index] = result;
                skills.get(position).setTripodBitmaps(temp);
                imgView[index].setImageBitmap(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SleepNotifyThread extends Thread {
        @Override
        public void run() {
            try {
                SleepNotifyThread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }
    }

}
