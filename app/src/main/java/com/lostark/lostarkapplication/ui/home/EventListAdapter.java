package com.lostark.lostarkapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lostark.lostarkapplication.WebActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private ArrayList<Event> events;
    private Context context;
    private CustomToast customToast;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    public EventListAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
        storage = FirebaseStorage.getInstance("gs://lostarkhub-cbe60.appspot.com");
        storageRef = storage.getReference();
        customToast = new CustomToast(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = events.get(position).getStartdate()+" ~ "+events.get(position).getEnddate();
        holder.txtDate.setText(date);
        holder.setClickAction(position);
        Calendar now = Calendar.getInstance();
        Calendar end_cal = Calendar.getInstance();

        end_cal.set(Calendar.YEAR, events.get(position).getYear());
        end_cal.set(Calendar.MONTH, events.get(position).getMonth());
        end_cal.set(Calendar.DAY_OF_MONTH, events.get(position).getDay());
        end_cal.set(Calendar.HOUR_OF_DAY, 6);
        end_cal.set(Calendar.MINUTE, 0);
        end_cal.set(Calendar.SECOND, 0);

        if (now.getTime().getTime() > end_cal.getTime().getTime()) {
            holder.txtEnd.setVisibility(View.VISIBLE);
        } else {
            holder.txtEnd.setVisibility(View.GONE);
        }

        if (events.get(position).isFail()) holder.imgEvent.setImageResource(R.drawable.noemptyboss);
        else if (events.get(position).getBitmap() == null) holder.imgEvent.setImageResource(R.drawable.emptyboss);
        else holder.imgEvent.setImageBitmap(events.get(position).getBitmap());
        /*holder.imgEvent.setImageResource(R.drawable.emptyboss);
        storageRef.child("Events/event"+events.get(position).getNumber()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri).into(holder.imgEvent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("Error NullPointerException!!==================================");
                    holder.imgEvent.setImageResource(R.drawable.noemptyboss);
                } catch (Exception e) {
                    e.printStackTrace();
                    holder.imgEvent.setImageResource(R.drawable.noemptyboss);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imgEvent.setImageResource(R.drawable.noemptyboss);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate, txtEnd;
        public ImageView imgEvent;
        public int position = 9999;

        public ViewHolder(View itemVIew) {
            super(itemVIew);

            txtDate = itemVIew.findViewById(R.id.txtDate);
            imgEvent = itemVIew.findViewById(R.id.imgEvent);
            txtEnd = itemVIew.findViewById(R.id.txtEnd);

            //GradientDrawable round_drawable = (GradientDrawable) context.getDrawable(R.drawable.roundimage);
            //imgEvent.setClipToOutline(true);

            itemVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        /*context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(events.get(position).getUrl())));
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", events.get(position).getUrl());*/
                        context.startActivity(new Intent(context, WebActivity.class).putExtra("url", events.get(position).getUrl()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        customToast.createToast("페이지를 여는데 오류가 발생하였습니다.", Toast.LENGTH_LONG);
                        customToast.show();
                    }
                }
            });
        }

        public void setClickAction(int position) {
            this.position = position;
        }
    }
}
