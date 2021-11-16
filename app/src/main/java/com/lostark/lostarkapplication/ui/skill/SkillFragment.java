package com.lostark.lostarkapplication.ui.skill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private AlertDialog alertDialog;

    private Bitmap[] bitmaps;

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
            skills.add(new Skill(name, strike, attack_type, destory_level, content, url, 1, max_level, time, tripods));
        }
        skillAdapter = new SkillAdapter(skills, getActivity(), dataNetwork, getActivity());
        skillAdapter.setJob_index(0);
        listView.setAdapter(skillAdapter);

        bitmaps = new Bitmap[skills.size()];
        for (int i = 0; i < skills.size(); i++) {
            if (i == skills.size()-1) new DownloadFilesTask(i, true).execute(skills.get(i).getUrl());
            else new DownloadFilesTask(i, false).execute(skills.get(i).getUrl());
        }

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
                    skills.add(new Skill(name, strike, attack_type, destory_level, content, url, 1, max_level, time, tripods));
                }
                skillAdapter.setBitmaps(null);
                skillAdapter.setJob_index(position);
                skillAdapter.notifyDataSetChanged();

                bitmaps = new Bitmap[skills.size()];
                for (int i = 0; i < skills.size(); i++) {
                    if (i == skills.size()-1) new DownloadFilesTask(i, true).execute(skills.get(i).getUrl());
                    else new DownloadFilesTask(i, false).execute(skills.get(i).getUrl());
                }

                dataNetwork.setSkillpoint(dataNetwork.getMax());
                txtSkillPoint.setText(dataNetwork.getSkillpoint()+"/"+dataNetwork.getMax());
                progressSkill.setProgress(dataNetwork.getSkillpoint());
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

}
