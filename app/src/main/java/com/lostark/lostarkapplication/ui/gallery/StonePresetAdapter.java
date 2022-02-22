package com.lostark.lostarkapplication.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.StampDBAdapter;
import com.lostark.lostarkapplication.database.StonePresetDBAdapter;

import java.util.ArrayList;

public class StonePresetAdapter extends BaseAdapter {
    private StampDBAdapter stampDBAdapter;
    private Context context;
    private ArrayList<Preset> presets;
    private StonePresetDBAdapter presetDBAdapter;
    private Activity activity;
    private AlertDialog alertDialog;
    private CustomToast customToast;

    public StonePresetAdapter(Context context, ArrayList<Preset> presets, Activity activity) {
        this.context = context;
        this.presets = presets;
        this.activity = activity;
        presetDBAdapter = new StonePresetDBAdapter(context);
        stampDBAdapter = new StampDBAdapter(activity);
        customToast = new CustomToast(context);
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
        if (convertView == null) convertView = View.inflate(context, R.layout.stone_preset_list, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtGrade = convertView.findViewById(R.id.txtGrade);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);
        ImageView[] imgStamp = new ImageView[3];
        TextView[] txtStamp = new TextView[3];
        for (int i = 0; i < imgStamp.length; i++) {
            imgStamp[i] = convertView.findViewById(context.getResources().getIdentifier("imgStamp"+(i+1), "id", context.getPackageName()));
            txtStamp[i] = convertView.findViewById(context.getResources().getIdentifier("txtStamp"+(i+1), "id", context.getPackageName()));

            String imgSrc = stampDBAdapter.readData(presets.get(position).getStamps()[i])[1];
            imgStamp[i].setImageResource(context.getResources().getIdentifier(imgSrc, "drawable", context.getPackageName()));
            txtStamp[i].setText(presets.get(position).getStamps()[i]);
        }

        txtName.setText(presets.get(position).getName());
        txtGrade.setText(presets.get(position).getGrade());

        switch (presets.get(position).getGrade()) {
            case "희귀":
                txtGrade.setTextColor(Color.parseColor("#2093A8"));
                break;
            case "영웅":
                txtGrade.setTextColor(Color.parseColor("#9B53D2"));
                break;
            case "전설":
                txtGrade.setTextColor(Color.parseColor("#C2873B"));
                break;
            case "유물":
                txtGrade.setTextColor(Color.parseColor("#BF5700"));
                break;
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("스톤을 삭제하시겠습니까?");
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
                        presetDBAdapter.open();
                        presetDBAdapter.deleteData(presets.get(position).getId());
                        presets.remove(position);
                        presetDBAdapter.close();
                        customToast.createToast("스톤이 삭제되었습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

        return convertView;
    }
}
