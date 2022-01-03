package com.lostark.lostarkapplication.ui.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class StoneAdapter extends BaseAdapter {
    private ArrayList<Stone> stones;
    private Context context;

    public StoneAdapter(ArrayList<Stone> stones, Context context) {
        this.stones = stones;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stones.size();
    }

    @Override
    public Object getItem(int position) {
        return stones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.stonehistorylayout, null);

        LinearLayout layoutMain = convertView.findViewById(R.id.layoutMain);
        View viewLine = convertView.findViewById(R.id.viewLine);
        ImageView imgStone = convertView.findViewById(R.id.imgStone);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtGrade = convertView.findViewById(R.id.txtGrade);
        TextView[] txtBurf = new TextView[3];
        TextView[] txtBurfCnt = new TextView[3];
        for (int i = 0; i < txtBurf.length; i++) {
            txtBurf[i] = convertView.findViewById(context.getResources().getIdentifier("txtBurf"+(i+1), "id", context.getPackageName()));
            txtBurfCnt[i] = convertView.findViewById(context.getResources().getIdentifier("txtBurfCnt"+(i+1), "id", context.getPackageName()));
        }

        txtGrade.setText(stones.get(position).getGrade());
        switch (stones.get(position).getGrade()) {
            case "희귀":
                layoutMain.setBackground(context.getDrawable(R.drawable.stonehistorystyle1));
                viewLine.setBackgroundColor(context.getColor(R.color.rare));
                imgStone.setImageResource(R.drawable.stone1);
                txtName.setText("비상의 돌");
                txtGrade.setTextColor(context.getColor(R.color.rare));
                break;
            case "영웅":
                layoutMain.setBackground(context.getDrawable(R.drawable.stonehistorystyle2));
                viewLine.setBackgroundColor(context.getColor(R.color.epic));
                imgStone.setImageResource(R.drawable.stone2);
                txtName.setText("뛰어난 비상의 돌");
                txtGrade.setTextColor(context.getColor(R.color.epic));
                break;
            case "전설":
                layoutMain.setBackground(context.getDrawable(R.drawable.stonehistorystyle3));
                viewLine.setBackgroundColor(context.getColor(R.color.legend));
                imgStone.setImageResource(R.drawable.stone3);
                txtName.setText("강력한 비상의 돌");
                txtGrade.setTextColor(context.getColor(R.color.legend));
                break;
            case "유물":
                layoutMain.setBackground(context.getDrawable(R.drawable.stonehistorystyle4));
                viewLine.setBackgroundColor(context.getColor(R.color.exotic));
                imgStone.setImageResource(R.drawable.stone4);
                txtName.setText("고고한 비상의 돌");
                txtGrade.setTextColor(context.getColor(R.color.exotic));
                break;
        }

        String[] burf = stones.get(position).getStamp();
        int[] count = stones.get(position).getCnt();
        for (int i = 0; i < burf.length; i++) {
            txtBurf[i].setText(burf[i]);
            txtBurfCnt[i].setText(Integer.toString(count[i]));
        }

        return convertView;
    }
}
