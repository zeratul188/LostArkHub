package com.lostark.lostarkapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostark.lostarkapplication.objects.History;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<History> histories;

    public HistoryAdapter(Context context, ArrayList<History> histories) {
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
        if (convertView == null) convertView = View.inflate(context, R.layout.history_list, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        TextView txtContent = convertView.findViewById(R.id.txtContent);
        ImageView imgIcon = convertView.findViewById(R.id.imgIcon);

        txtName.setText(histories.get(position).getName());
        txtDate.setText(histories.get(position).getDate());
        txtContent.setText(histories.get(position).getContent());

        List<String> days = Arrays.asList(context.getResources().getStringArray(R.array.day_homework));
        List<String> weeks = Arrays.asList(context.getResources().getStringArray(R.array.week_homework));

        for (String str : days) {
            if (histories.get(position).getContent().indexOf(str) != -1) {
                switch (days.indexOf(str)) {
                    case 0: case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 9:
                        imgIcon.setImageResource(context.getResources().getIdentifier("hwid"+(days.indexOf(str)+1), "drawable", context.getPackageName()));
                        imgIcon.setVisibility(View.VISIBLE);
                        break;
                    default:
                        imgIcon.setVisibility(View.GONE);
                }
            }
        }

        for (String str : weeks) {
            if (histories.get(position).getContent().indexOf(str) != -1) {
                switch (weeks.indexOf(str)) {
                    case 0: case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 9:
                        imgIcon.setImageResource(context.getResources().getIdentifier("hwiw"+(weeks.indexOf(str)+1), "drawable", context.getPackageName()));
                        imgIcon.setVisibility(View.VISIBLE);
                        break;
                    default:
                        imgIcon.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }
}
