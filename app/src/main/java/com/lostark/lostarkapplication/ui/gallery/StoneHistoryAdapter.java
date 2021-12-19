package com.lostark.lostarkapplication.ui.gallery;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class StoneHistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StoneHistory> histories;

    public StoneHistoryAdapter(Context context, ArrayList<StoneHistory> histories) {
        this.context = context;
        this.histories = histories;
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int position) {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.stone_history_list, null);

        TextView txtCount = convertView.findViewById(R.id.txtCount);
        TextView txtContent = convertView.findViewById(R.id.txtContent);
        TextView txtResult = convertView.findViewById(R.id.txtResult);

        txtCount.setText((position+1)+"번째");
        txtContent.setText(histories.get(position).getContent());
        if (histories.get(position).isSuccess()) {
            txtResult.setText("성공");
            txtResult.setTextColor(Color.parseColor("#92C52D"));
        } else {
            txtResult.setText("실패");
            txtResult.setTextColor(Color.parseColor("#FF4444"));
        }

        return convertView;
    }
}
