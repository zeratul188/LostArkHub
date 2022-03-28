package com.lostark.lostarkapplication.ui.commander;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class ChecklistIconAdapter extends RecyclerView.Adapter<ChecklistIconAdapter.ViewHolder> {
    private ArrayList<Icon> icons;
    private Context context;

    private final int MAX_DAY = 10;
    private final int MAX_WEEK = 12;

    private int index = 0;

    public int getIndex() {
        return index;
    }

    public ChecklistIconAdapter(ArrayList<Icon> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_view, parent, false);
        return new ChecklistIconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (icons.get(position).getIndex() > 0 && icons.get(position).getIndex() <= MAX_DAY) {
            holder.imgIcon.setImageResource(context.getResources().getIdentifier("hwid"+icons.get(position).getIndex(), "drawable", context.getPackageName()));
        } else if (icons.get(position).getIndex() > MAX_DAY && icons.get(position).getIndex() <= MAX_WEEK+MAX_DAY) {
            holder.imgIcon.setImageResource(context.getResources().getIdentifier("hwiw"+(icons.get(position).getIndex()-MAX_DAY), "drawable", context.getPackageName()));
        } else {
            holder.imgIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
        }

        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < icons.size(); i++) {
                    if (i == position) {
                        icons.get(i).setSelect(true);
                        index = icons.get(i).getIndex();
                    } else {
                        icons.get(i).setSelect(false);
                    }
                }
                notifyDataSetChanged();
            }
        });

        if (icons.get(position).isSelect()) {
            holder.layoutBackground.setBackgroundColor(Color.parseColor("#5A8857"));
        } else {
            holder.layoutBackground.setBackgroundColor(Color.parseColor("#2a2c35"));
        }
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIcon;
        public ConstraintLayout layoutBackground;

        public ViewHolder(View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgIcon);
            layoutBackground = itemView.findViewById(R.id.layoutBackground);
        }
    }
}
