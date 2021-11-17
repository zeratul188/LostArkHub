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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ClassDBAdapter;
import com.lostark.lostarkapplication.database.SkillDBAdapter;
import com.lostark.lostarkapplication.database.SkillPresetDBAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PresetAdapter extends BaseAdapter {
    private ArrayList<SkillPresetList> presets;
    private Context context;
    private Activity activity;

    private ClassDBAdapter classDBAdapter;
    private AlertDialog alertDialog;
    private SkillPresetDBAdapter skillPresetDBAdapter;

    public PresetAdapter(ArrayList<SkillPresetList> presets, Context context, Activity activity) {
        this.presets = presets;
        this.context = context;
        this.activity = activity;
        classDBAdapter = new ClassDBAdapter(activity);
        skillPresetDBAdapter = new SkillPresetDBAdapter(context);
    }

    @Override
    public int getCount() {
        return presets.size();
    }

    @Override
    public Object getItem(int position) {
        return presets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.skill_preset_list, null);

        ImageView imgJob = convertView.findViewById(R.id.imgJob);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtJob = convertView.findViewById(R.id.txtJob);
        TextView txtPoint = convertView.findViewById(R.id.txtPoint);
        ImageView imgDelete = convertView.findViewById(R.id.imgDelete);

        for (int i = 0; i < classDBAdapter.getSize(); i++) {
            String[] args = classDBAdapter.readData(i);
            if (args[0].equals(presets.get(position).getJob())) {
                new DownloadFilesTask(imgJob).execute(args[1]);
            }
        }
        txtName.setText(presets.get(position).getName());
        txtJob.setText(presets.get(position).getJob());
        txtPoint.setText(Integer.toString(presets.get(position).getMax()));

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnOK = view.findViewById(R.id.btnOK);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                txtContent.setText("프리셋을 삭제하시겠습니까?");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SkillDBAdapter skillDBAdapter = new SkillDBAdapter(context, Integer.toString(presets.get(position).getId()));
                        skillPresetDBAdapter.open();
                        skillPresetDBAdapter.deleteData(presets.get(position).getId());
                        skillPresetDBAdapter.close();
                        skillDBAdapter.open();
                        skillDBAdapter.dropTable();
                        skillDBAdapter.close();
                        presets.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "프리셋을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
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

        return convertView;
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
