package com.lostark.lostarkapplication.ui.guild;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class GuildAdapter extends BaseAdapter {
    private ArrayList<Guild> guilds;
    private Context context;

    public GuildAdapter(ArrayList<Guild> guilds, Context context) {
        this.guilds = guilds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return guilds.size();
    }

    @Override
    public Object getItem(int position) {
        return guilds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.guild_layout, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtLevel = convertView.findViewById(R.id.txtLevel);
        TextView txtBoss = convertView.findViewById(R.id.txtBoss);
        TextView txtMinLevel = convertView.findViewById(R.id.txtMinLevel);
        TextView txtContent = convertView.findViewById(R.id.txtContent);
        TextView txtServer = convertView.findViewById(R.id.txtServer);

        txtName.setText(guilds.get(position).getName());
        txtLevel.setText(Integer.toString(guilds.get(position).getLevel()));
        txtBoss.setText(guilds.get(position).getBoss());
        txtMinLevel.setText(Integer.toString(guilds.get(position).getMin()));
        txtContent.setText(guilds.get(position).getContent());
        txtServer.setText(guilds.get(position).getServer());

        return convertView;
    }
}
