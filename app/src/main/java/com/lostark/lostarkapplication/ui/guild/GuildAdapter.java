package com.lostark.lostarkapplication.ui.guild;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
        LinearLayout layoutBackground = convertView.findViewById(R.id.layoutBackground);

        txtName.setText(guilds.get(position).getName());
        txtLevel.setText(Integer.toString(guilds.get(position).getLevel()));
        txtBoss.setText(guilds.get(position).getBoss());
        txtMinLevel.setText(Integer.toString(guilds.get(position).getMin()));
        if (guilds.get(position).getMin() == 0) {
            txtMinLevel.setText("레벨 제한 없음");
        } else {
            txtMinLevel.setText(guilds.get(position).getMin()+" 레벨 이상 가입 가능");
        }
        if (guilds.get(position).getContent().equals("")) {
            txtContent.setText("소개 없음");
        } else {
            String content = "";
            if (guilds.get(position).getContent().length() > 100) {
                content = guilds.get(position).getContent().substring(0, 100);
                content = content.replaceAll("(\r\n|\r|\n|\n\r)", " ");
                content += "...";
            } else {
                content = guilds.get(position).getContent();
                content = content.replaceAll("(\r\n|\r|\n|\n\r)", " ");
            }
            txtContent.setText(content);
        }
        txtServer.setText(guilds.get(position).getServer());
        if (guilds.get(position).getIndex() == 9) {
            layoutBackground.setBackgroundColor(Color.parseColor("#805A8857"));
        } else {
            layoutBackground.setBackgroundColor(Color.parseColor("#1d2026"));
        }

        return convertView;
    }
}
