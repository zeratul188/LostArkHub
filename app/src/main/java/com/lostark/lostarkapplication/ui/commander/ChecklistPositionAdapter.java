package com.lostark.lostarkapplication.ui.commander;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.home.EventListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChecklistPositionAdapter extends RecyclerView.Adapter<ChecklistPositionAdapter.ViewHolder>
    implements ItemTouchHelperListener{
    private ArrayList<Checklist> checklists;
    private Context context;
    private boolean isDay = true;

    public ChecklistPositionAdapter(ArrayList<Checklist> checklists, Context context, boolean isDay) {
        this.checklists = checklists;
        this.context = context;
        this.isDay = isDay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checklist_position_list, parent, false);
        return new ChecklistPositionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> homeworks;
        if (isDay) {
            homeworks = Arrays.asList(context.getResources().getStringArray(R.array.day_homework));
            switch (homeworks.indexOf(checklists.get(position).getName())) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 9:
                    holder.imgIcon.setImageResource(context.getResources().getIdentifier("hwid"+(homeworks.indexOf(checklists.get(position).getName())+1), "drawable", context.getPackageName()));
                    break;
                default:
                    holder.imgIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
            }
        } else {
            homeworks = Arrays.asList(context.getResources().getStringArray(R.array.week_homework));
            switch (homeworks.indexOf(checklists.get(position).getName())) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
                    holder.imgIcon.setImageResource(context.getResources().getIdentifier("hwiw"+(homeworks.indexOf(checklists.get(position).getName())+1), "drawable", context.getPackageName()));
                    break;
                default:
                    holder.imgIcon.setImageResource(R.drawable.ic_assignment_black_24dp);
            }
        }
        holder.txtName.setText(checklists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return checklists.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(checklists, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIcon;
        public TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
