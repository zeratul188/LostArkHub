package com.lostark.lostarkapplication.ui.skill;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.JobTripodDBAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SkillAdapter extends BaseAdapter {
    private ArrayList<Skill> skills;
    private Context context;
    private DataNetwork dataNetwork;
    private Activity activity;

    private Bitmap[] bitmaps;
    private AlertDialog alertDialog;
    private int job_index = 9999;

    private int[] checklist = new int[3];

    public SkillAdapter(ArrayList<Skill> skills, Context context, DataNetwork dataNetwork, Activity activity) {
        this.skills = skills;
        this.context = context;
        this.dataNetwork = dataNetwork;
        this.activity = activity;
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

        if (need_point == 9999) txtNeedSkillPoint.setText("MAX");
        else txtNeedSkillPoint.setText(Integer.toString(need_point));

        int stack = 0;
        for (int tripod : skills.get(position).getTripods()) {
            if (tripod != 4) {
                stack++;
            }
        }
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

                new DownloadFilesTask(imgSkill).execute(skills.get(position).getUrl());
                txtName.setText(skills.get(position).getName());
                txtTime.setText(skills.get(position).getTime()+"초");
                txtStrike.setText(skills.get(position).getStrike());
                txtAttackType.setText(skills.get(position).getAttack_type());
                txtDestroyLevel.setText(skills.get(position).getDestroy_level());
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
            txtSkillName.setTextColor(Color.parseColor("#FE6E0E"));
        } else {
            layoutSetting.setVisibility(View.VISIBLE);
            layoutNeedSkillPoint.setVisibility(View.VISIBLE);
            imgTripod.setVisibility(View.VISIBLE);
            txtSkillName.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if (skills.get(position).getLevel() == skills.get(position).getMax_level()) imgbtnIncrease.setEnabled(false);
        else imgbtnIncrease.setEnabled(true);
        if (skills.get(position).getLevel() == 1) imgbtnDecrease.setEnabled(false);
        else imgbtnDecrease.setEnabled(true);

        imgTripod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBetween(skills.get(position).getLevel(), 1, 3)) {
                    Toast.makeText(context, "스킬 레벨이 낮아 트라이포드를 설정하실 수 없습니다. (최소 레벨 : 4)", Toast.LENGTH_SHORT).show();
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
                ImageView[][] imgTripod = new ImageView[3][3];
                TextView[][] txtTripod = new TextView[3][3];
                TextView[][] txtTripodContent = new TextView[3][3];;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (i == 2 && j == 2) continue;
                        layoutTripod[i][j] = view.findViewById(context.getResources().getIdentifier("layoutTripod"+(i+1)+(j+1), "id", context.getPackageName()));
                        imgTripod[i][j] = view.findViewById(context.getResources().getIdentifier("imgTripod"+(i+1)+(j+1), "id", context.getPackageName()));
                        txtTripod[i][j] = view.findViewById(context.getResources().getIdentifier("txtTripod"+(i+1)+(j+1), "id", context.getPackageName()));
                        txtTripodContent[i][j] = view.findViewById(context.getResources().getIdentifier("txtTripodContent"+(i+1)+(j+1), "id", context.getPackageName()));

                        if (skills.get(position).getTripods()[i] != 4) {
                            if (j == skills.get(position).getTripods()[i]) {
                                layoutTripod[i][j].setBackgroundColor(Color.parseColor("#6A833D"));
                                txtTripodContent[i][j].setBackgroundResource(R.drawable.tripod_checked_content_background);
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
                                        layoutTripod[first_index][i].setBackgroundColor(Color.parseColor("#6A833D"));
                                        txtTripodContent[first_index][i].setBackgroundResource(R.drawable.tripod_checked_content_background);
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
                                    new DownloadFilesTask(imgTripod[0][first]).execute(args[4]);
                                    txtTripod[0][first].setText(args[2]);
                                    txtTripodContent[0][first].setText(args[3]);
                                    first++;
                                }
                                break;
                            case 2:
                                if (second < 3) {
                                    new DownloadFilesTask(imgTripod[1][second]).execute(args[4]);
                                    txtTripod[1][second].setText(args[2]);
                                    txtTripodContent[1][second].setText(args[3]);
                                    second++;
                                }
                                break;
                            case 3:
                                if (third < 2) {
                                    new DownloadFilesTask(imgTripod[2][third]).execute(args[4]);
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
                        notifyDataSetChanged();
                        Toast.makeText(context, "트라이포드를 저장하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
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
                    Toast.makeText(context, "스킬 포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
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

}
