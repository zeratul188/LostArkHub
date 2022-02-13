package com.lostark.lostarkapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.WebActivity;
import com.lostark.lostarkapplication.ui.commander.ChecklistActivity;
import com.lostark.lostarkapplication.ui.home.objects.Event;
import com.lostark.lostarkapplication.ui.home.objects.Homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HomeworkListAdapter extends RecyclerView.Adapter<HomeworkListAdapter.ViewHolder> {
    private ArrayList<Homework> homeworks;
    private Context context;
    private CustomToast customToast;
    private List<String> jobs;

    public HomeworkListAdapter(ArrayList<Homework> homeworks, Context context) {
        this.homeworks = homeworks;
        this.context = context;
        customToast = new CustomToast(context);
        jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homework_home_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPosition(position);
        int index = jobs.indexOf(homeworks.get(position).getJob());
        holder.imgJob.setImageResource(context.getResources().getIdentifier("jbi"+(index+1), "drawable", context.getPackageName()));
        holder.txtName.setText(homeworks.get(position).getName());
        holder.txtJob.setText(homeworks.get(position).getJob());
        holder.txtServer.setText(homeworks.get(position).getServer());
        holder.txtLevel.setText(Integer.toString(homeworks.get(position).getLevel()));
        holder.txtDungeon.setText(homeworks.get(position).getDungeon()+"/2");
        if (homeworks.get(position).getDungeon() >= 2) {
            holder.txtDungeon.setTextColor(Color.parseColor("#92C52D"));
        } else {
            holder.txtDungeon.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.txtBoss.setText(homeworks.get(position).getBoss()+"/2");
        if (homeworks.get(position).getBoss() >= 2) {
            holder.txtBoss.setTextColor(Color.parseColor("#92C52D"));
        } else {
            holder.txtBoss.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.txtQuest.setText(homeworks.get(position).getQuest()+"/3");
        if (homeworks.get(position).getQuest() >= 3) {
            holder.txtQuest.setTextColor(Color.parseColor("#92C52D"));
        } else {
            holder.txtQuest.setTextColor(Color.parseColor("#FFFFFF"));
        }
        int percent = (int) ((double) homeworks.get(position).getNow() / (double) homeworks.get(position).getMax()*100);
        if (percent == 100) {
            holder.txtProgress.setText("숙제 완료");
            holder.txtProgress.setTextColor(Color.parseColor("#92C52D"));
            holder.txtProgressInfo.setVisibility(View.GONE);
        } else {
            holder.txtProgress.setText(percent+"%");
            holder.txtProgress.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txtProgressInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgJob;
        public TextView txtName, txtJob, txtServer, txtLevel, txtDungeon, txtBoss, txtQuest, txtProgress, txtProgressInfo;
        public int position = 9999;

        public ViewHolder(View itemVIew) {
            super(itemVIew);

            imgJob = itemVIew.findViewById(R.id.imgJob);
            txtName = itemVIew.findViewById(R.id.txtName);
            txtJob = itemVIew.findViewById(R.id.txtJob);
            txtServer = itemVIew.findViewById(R.id.txtServer);
            txtLevel = itemVIew.findViewById(R.id.txtLevel);
            txtDungeon = itemVIew.findViewById(R.id.txtDungeon);
            txtBoss = itemVIew.findViewById(R.id.txtBoss);
            txtQuest = itemVIew.findViewById(R.id.txtQuest);
            txtProgress = itemVIew.findViewById(R.id.txtProgress);
            txtProgressInfo = itemVIew.findViewById(R.id.txtProgressInfo);

            itemVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChecklistActivity.class);
                    intent.putExtra("chracter_name", homeworks.get(position).getName());
                    intent.putExtra("chracter_level", homeworks.get(position).getLevel());
                    context.startActivity(intent);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
