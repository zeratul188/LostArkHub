package com.lostark.lostarkapplication.ui.commander;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChracterListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChracterSelectList> lists;

    public ChracterListAdapter(Context context, ArrayList<ChracterSelectList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.chracter_select_list, null);

        CircleImageView imgJob = convertView.findViewById(R.id.imgJob);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtServer = convertView.findViewById(R.id.txtServer);
        TextView txtLevel = convertView.findViewById(R.id.txtLevel);
        TextView txtJob = convertView.findViewById(R.id.txtJob);

        txtName.setText(lists.get(position).getName());
        txtJob.setText(lists.get(position).getJob());
        txtServer.setText(lists.get(position).getServer());
        txtLevel.setText(Integer.toString(lists.get(position).getLevel()));
        List<String> jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));
        imgJob.setImageResource(context.getResources().getIdentifier("jbi"+(jobs.indexOf(lists.get(position).getJob())+1), "drawable", context.getPackageName()));

        return convertView;
    }
}
