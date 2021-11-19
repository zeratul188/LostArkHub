package com.lostark.lostarkapplication.ui.skill;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.JobDBAdapter;
import com.lostark.lostarkapplication.database.JobTripodDBAdapter;
import com.lostark.lostarkapplication.database.SkillDBAdapter;
import com.lostark.lostarkapplication.database.SkillPresetDBAdapter;
import com.lostark.lostarkapplication.ui.stamp.ClearEditText;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillFragment extends Fragment {
    private SkillViewModel skillViewModel;

    private TextView txtPresetName, txtSkillPoint;
    private Button btnPreset, btnReset, btnSave;
    private Spinner sprJob;
    private ListView listView;
    private ProgressBar progressSkill;

    private JobDBAdapter jobDBAdapter;
    private ArrayList<Skill> skills;
    private SkillAdapter skillAdapter;
    private DataNetwork dataNetwork;
    private AlertDialog alertDialog, alertDialog2;

    private SkillPresetDBAdapter skillPresetDBAdapter;
    private SkillDBAdapter skillDBAdapter;

    private Bitmap[] bitmaps;
    private boolean isLoaded = false;

    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            // 원래 하려던 동작 (UI변경 작업 등)
            skillAdapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        skillViewModel = ViewModelProviders.of(this).get(SkillViewModel.class);
        View root = inflater.inflate(R.layout.fragment_skill, container, false);

        txtPresetName = root.findViewById(R.id.txtPresetName);
        txtSkillPoint = root.findViewById(R.id.txtSkillPoint);
        btnPreset = root.findViewById(R.id.btnPreset);
        btnReset = root.findViewById(R.id.btnReset);
        btnSave = root.findViewById(R.id.btnSave);
        sprJob = root.findViewById(R.id.sprJob);
        listView = root.findViewById(R.id.listView);
        progressSkill = root.findViewById(R.id.progressSkill);

        dataNetwork = new DataNetwork();
        dataNetwork.setMax(0);
        dataNetwork.setSkillpoint(0);
        dataNetwork.setTxtView(txtSkillPoint);
        dataNetwork.setProgressBar(progressSkill);

        skillPresetDBAdapter = new SkillPresetDBAdapter(getActivity());

        List<String> jobs = Arrays.asList(getActivity().getResources().getStringArray(R.array.job));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.job_skill_item, jobs);
        sprJob.setAdapter(adapter);

        skills = new ArrayList<>();
        jobDBAdapter = new JobDBAdapter(getActivity(), sprJob.getSelectedItemPosition()+1);
        for (int i = 0; i < jobDBAdapter.getSize(); i++) {
            String[] args = jobDBAdapter.readData(i);
            String name = args[0];
            int max_level = Integer.parseInt(args[1]);
            int time = Integer.parseInt(args[2]);
            String strike = args[3];
            String attack_type = args[4];
            String destory_level = args[5];
            String content = args[6];
            String url = args[7];
            int[] tripods = {4, 4, 4};
            skills.add(new Skill(name, strike, attack_type, destory_level, content, url, 1, max_level, time, tripods, 99));
        }
        skillAdapter = new SkillAdapter(skills, getActivity(), dataNetwork, getActivity());
        skillAdapter.setJob_index(0);
        listView.setAdapter(skillAdapter);

        bitmaps = new Bitmap[skills.size()];
        for (int i = 0; i < skills.size(); i++) {
            if (i == skills.size()-1) new DownloadFilesTask(i, true).execute(skills.get(i).getUrl());
            else new DownloadFilesTask(i, false).execute(skills.get(i).getUrl());
        }

        btnPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.skill_preset_layout, null);

                ListView listView = view.findViewById(R.id.listView);
                ClearEditText edtName = view.findViewById(R.id.edtName);
                TextView txtLimit = view.findViewById(R.id.txtLimit);
                Button btnSave = view.findViewById(R.id.btnSave);

                ArrayList<SkillPresetList> presets = new ArrayList<>();
                skillPresetDBAdapter.open();
                Cursor cursor = skillPresetDBAdapter.fetchAllData();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int rowID = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String job = cursor.getString(2);
                    int skill_point = cursor.getInt(3);
                    int max = cursor.getInt(4);
                    presets.add(new SkillPresetList(name, job, rowID, skill_point, max));
                    cursor.moveToNext();
                }
                txtLimit.setText("("+skillPresetDBAdapter.getCount()+"/30)");
                if (skillPresetDBAdapter.getCount() >= 30) btnSave.setEnabled(false);
                else btnSave.setEnabled(true);
                skillPresetDBAdapter.close();
                PresetAdapter presetAdapter = new PresetAdapter(presets, getActivity(), getActivity());
                listView.setAdapter(presetAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        View dialog_view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                        TextView txtContent = dialog_view.findViewById(R.id.txtContent);
                        Button btnOK = dialog_view.findViewById(R.id.btnOK);
                        Button btnCancel = dialog_view.findViewById(R.id.btnCancel);

                        txtContent.setText("기존 스킬 시뮬레이션에 프리셋 정보를 덮어씌웁니다. 적용하시겠습니까?");
                        btnOK.setText("적용");

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog2.dismiss();
                            }
                        });

                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isLoaded = true;
                                skillDBAdapter = new SkillDBAdapter(getActivity(), Integer.toString(presets.get(position).getId()));

                                sprJob.setSelection(jobs.indexOf(presets.get(position).getJob()));
                                dataNetwork.setSkillpoint(presets.get(position).getSkillpoint());
                                dataNetwork.setMax(presets.get(position).getMax());
                                txtSkillPoint.setText(presets.get(position).getSkillpoint()+"/"+presets.get(position).getMax());
                                progressSkill.setMax(presets.get(position).getMax());
                                progressSkill.setProgress(presets.get(position).getSkillpoint());

                                skillDBAdapter.open();
                                Cursor cursor = skillDBAdapter.fetchAllData();
                                cursor.moveToFirst();
                                while (!cursor.isAfterLast()) {
                                    for (int i = 0; i < skills.size(); i++) {
                                        if (skills.get(i).getName().equals(cursor.getString(1))) {
                                            skills.get(i).setLevel(cursor.getInt(2));
                                            skills.get(i).setRune(cursor.getInt(3));
                                            int[] tripods = {cursor.getInt(4), cursor.getInt(5), cursor.getInt(6)};
                                            skills.get(i).setTripods(tripods);
                                            break;
                                        }
                                    }
                                    cursor.moveToNext();
                                }
                                skillDBAdapter.close();

                                JobTripodDBAdapter jobTripodDBAdapter = new JobTripodDBAdapter(getActivity(), sprJob.getSelectedItemPosition()+1);
                                int first = 0, second = 0, third = 0;
                                for (int i = 0; i < jobTripodDBAdapter.getSize(); i++) {
                                    String[] args = jobTripodDBAdapter.readData(i);
                                    if (Integer.parseInt(args[0]) == position+1) {
                                        switch (Integer.parseInt(args[1])) {
                                            case 1:
                                                if (skills.get(position).getTripods()[0] == first && skills.get(position).getTripods()[0] < 4) {
                                                    //new DownloadFilesTask(imgTripods[0], position, 0).execute(args[4]);
                                                    new TripodDownloadFilesTask(position, 0).execute(args[4]);
                                                }
                                                first++;
                                                break;
                                            case 2:
                                                if (skills.get(position).getTripods()[1] == second && skills.get(position).getTripods()[1] < 4) {
                                                    //new DownloadFilesTask(imgTripods[1], position, 1).execute(args[4]);
                                                    new TripodDownloadFilesTask(position, 1).execute(args[4]);
                                                }
                                                second++;
                                                break;
                                            case 3:
                                                if (skills.get(position).getTripods()[2] == third && skills.get(position).getTripods()[2] < 4) {
                                                    //new DownloadFilesTask(imgTripods[2], position, 2).execute(args[4]);
                                                    new TripodDownloadFilesTask(position, 2).execute(args[4]);
                                                }
                                                third++;
                                                break;
                                        }
                                    }
                                }

                                txtPresetName.setText(presets.get(position).getName());

                                Toast.makeText(getActivity(), "프리셋을 적용하였습니다.", Toast.LENGTH_SHORT).show();
                                skillAdapter.notifyDataSetChanged();
                                new SleepNotifyThread().start();
                                alertDialog2.dismiss();
                                alertDialog.dismiss();
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(dialog_view);

                        alertDialog2 = builder.create();
                        alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog2.show();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtName.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String name = edtName.getText().toString();
                        String job = sprJob.getSelectedItem().toString();
                        int skill_point = dataNetwork.getSkillpoint();
                        int max = dataNetwork.getMax();

                        skillPresetDBAdapter.open();
                        skillPresetDBAdapter.insertData(name, job, skill_point, max);
                        Cursor cursor = skillPresetDBAdapter.fetchAllData();
                        cursor.moveToFirst();
                        int last_rowID = 0;
                        while (!cursor.isAfterLast()) {
                            int rowID = cursor.getInt(0);
                            if (rowID > last_rowID) last_rowID = rowID;
                            cursor.moveToNext();
                        }
                        skillPresetDBAdapter.close();

                        skillDBAdapter = new SkillDBAdapter(getActivity(), Integer.toString(last_rowID));
                        skillDBAdapter.open();
                        for (Skill skill : skills) {
                            String skill_name = skill.getName();
                            int now = skill.getLevel();
                            int rune = skill.getRune();
                            int[] tripods = skill.getTripods();
                            skillDBAdapter.insertData(new SkillPreset(skill_name, now, rune, tripods));
                        }
                        skillDBAdapter.close();

                        presets.add(new SkillPresetList(name, job, last_rowID, skill_point, max));
                        presetAdapter.notifyDataSetChanged();
                        edtName.setText("");
                        Toast.makeText(getActivity(), "프리셋을 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnOK = view.findViewById(R.id.btnOK);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                txtContent.setText("스킬을 초기화하시겠습니까?");
                btnOK.setText("초기화");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skills.clear();
                        jobDBAdapter = new JobDBAdapter(getActivity(), sprJob.getSelectedItemPosition()+1);
                        for (int i = 0; i < jobDBAdapter.getSize(); i++) {
                            String[] args = jobDBAdapter.readData(i);
                            String name = args[0];
                            int max_level = Integer.parseInt(args[1]);
                            int time = Integer.parseInt(args[2]);
                            String strike = args[3];
                            String attack_type = args[4];
                            String destory_level = args[5];
                            String content = args[6];
                            String url = args[7];
                            int[] tripods = {4, 4, 4};
                            skills.add(new Skill(name, strike, attack_type, destory_level, content, url, 1, max_level, time, tripods, 99));
                        }
                        skillAdapter.notifyDataSetChanged();
                        dataNetwork.setSkillpoint(dataNetwork.getMax());
                        txtSkillPoint.setText(dataNetwork.getSkillpoint()+"/"+dataNetwork.getMax());
                        progressSkill.setProgress(dataNetwork.getSkillpoint());
                        txtPresetName.setText("적용된 프리셋이 없음");
                        Toast.makeText(getActivity(), "스킬 포인트를 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        sprJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skills.clear();
                jobDBAdapter = new JobDBAdapter(getActivity(), sprJob.getSelectedItemPosition()+1);
                for (int i = 0; i < jobDBAdapter.getSize(); i++) {
                    String[] args = jobDBAdapter.readData(i);
                    String name = args[0];
                    int max_level = Integer.parseInt(args[1]);
                    int time = Integer.parseInt(args[2]);
                    String strike = args[3];
                    String attack_type = args[4];
                    String destory_level = args[5];
                    String content = args[6];
                    String url = args[7];
                    int[] tripods = {4, 4, 4};
                    skills.add(new Skill(name, strike, attack_type, destory_level, content, url, 1, max_level, time, tripods, 99));
                }
                skillAdapter.setBitmaps(null);
                skillAdapter.setShow(true);
                skillAdapter.setJob_index(position);
                skillAdapter.notifyDataSetChanged();

                bitmaps = new Bitmap[skills.size()];
                for (int i = 0; i < skills.size(); i++) {
                    if (i == skills.size()-1) new DownloadFilesTask(i, true).execute(skills.get(i).getUrl());
                    else new DownloadFilesTask(i, false).execute(skills.get(i).getUrl());
                }

                if (!isLoaded) dataNetwork.setSkillpoint(dataNetwork.getMax());
                txtSkillPoint.setText(dataNetwork.getSkillpoint()+"/"+dataNetwork.getMax());
                progressSkill.setProgress(dataNetwork.getSkillpoint());

                if (isLoaded) {
                    skillDBAdapter.open();
                    Cursor cursor = skillDBAdapter.fetchAllData();
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        for (int i = 0; i < skills.size(); i++) {
                            if (skills.get(i).getName().equals(cursor.getString(1))) {
                                skills.get(i).setLevel(cursor.getInt(2));
                                skills.get(i).setRune(cursor.getInt(3));
                                int[] tripods = {cursor.getInt(4), cursor.getInt(5), cursor.getInt(6)};
                                skills.get(i).setTripods(tripods);
                                break;
                            }
                        }
                        cursor.moveToNext();
                    }
                    skillDBAdapter.close();
                    isLoaded = false;
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.skillpoint_setting_dialog, null);

                EditText edtSkillPoint = view.findViewById(R.id.edtSkillPoint);
                Button btnSetting = view.findViewById(R.id.btnSetting);

                btnSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtSkillPoint.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int now = dataNetwork.getSkillpoint();
                        if (Integer.parseInt(edtSkillPoint.getText().toString()) > dataNetwork.getMax()) now += Integer.parseInt(edtSkillPoint.getText().toString()) - dataNetwork.getMax();
                        else {
                            now -= dataNetwork.getMax() - Integer.parseInt(edtSkillPoint.getText().toString());
                            if (now < 0) {
                                Toast.makeText(getActivity(), "남은 스킬 포인트가 부족하여 스킬 포인트를 설정하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        dataNetwork.setMax(Integer.parseInt(edtSkillPoint.getText().toString()));
                        dataNetwork.setSkillpoint(now);
                        progressSkill.setMax(Integer.parseInt(edtSkillPoint.getText().toString()));
                        progressSkill.setProgress(now);
                        txtSkillPoint.setText(now+"/"+edtSkillPoint.getText().toString());

                        Toast.makeText(getActivity(), "스킬 포인트를 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        return root;
    }


    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        private int position;
        private boolean isLast = false;

        public DownloadFilesTask(int position, boolean isLast) {
            this.position = position;
            this.isLast = isLast;
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
            bitmaps[position] = result;
            if (isLast) {
                skillAdapter.setBitmaps(bitmaps);
                skillAdapter.notifyDataSetChanged();
            }
        }
    }

    private class SleepNotifyThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }
    }

    private class TripodDownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        private int position, index;

        public TripodDownloadFilesTask(int position, int index) {
            this.position = position;
            this.index = index;
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
            Bitmap[] temp = skills.get(position).getTripodBitmaps();
            temp[index] = result;
            skills.get(position).setTripodBitmaps(temp);
        }
    }

}
