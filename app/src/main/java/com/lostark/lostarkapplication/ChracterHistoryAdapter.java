package com.lostark.lostarkapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lostark.lostarkapplication.objects.ChracterHistory;
import com.lostark.lostarkapplication.objects.History;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChracterHistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChracterHistory> chracterHistories;

    public ChracterHistoryAdapter(Context context, ArrayList<ChracterHistory> chracterHistories) {
        this.context = context;
        this.chracterHistories = chracterHistories;
    }

    @Override
    public int getCount() {
        return chracterHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return chracterHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.history_chracter_list, null);

        CircleImageView imgJob = convertView.findViewById(R.id.imgJob);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtServer = convertView.findViewById(R.id.txtServer);
        TextView txtLevel = convertView.findViewById(R.id.txtLevel);
        TextView txtJob = convertView.findViewById(R.id.txtJob);
        TextView txtDungeon = convertView.findViewById(R.id.txtDungeon);
        TextView txtBoss = convertView.findViewById(R.id.txtBoss);
        TextView txtQuest = convertView.findViewById(R.id.txtQuest);

        List<String> jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));
        imgJob.setImageResource(context.getResources().getIdentifier("jbi"+(jobs.indexOf(chracterHistories.get(position).getJob())+1), "drawable", context.getPackageName()));
        txtName.setText(chracterHistories.get(position).getName());
        txtServer.setText(chracterHistories.get(position).getServer());
        txtLevel.setText(Integer.toString(chracterHistories.get(position).getLevel()));
        txtJob.setText(chracterHistories.get(position).getJob());
        txtDungeon.setText(Integer.toString(chracterHistories.get(position).getDungeon()));
        txtBoss.setText(Integer.toString(chracterHistories.get(position).getBoss()));
        txtQuest.setText(Integer.toString(chracterHistories.get(position).getQuest()));

        return convertView;
    }
}
