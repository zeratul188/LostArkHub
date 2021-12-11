package com.lostark.lostarkapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lostark.lostarkapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private ArrayList<Event> events;
    private Context context;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    public EventListAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
        storage = FirebaseStorage.getInstance("gs://lostarkhub-cbe60.appspot.com");
        storageRef = storage.getReference();
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

        storageRef.child("Events/event"+events.get(position).getNumber()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri).into(holder.imgEvent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("Error NullPointerException!!==================================");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imgEvent.setImageResource(R.drawable.noemptyboss);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public ImageView imgEvent;
        public int position = 9999;

        public ViewHolder(View itemVIew) {
            super(itemVIew);

            txtDate = itemVIew.findViewById(R.id.txtDate);
            imgEvent = itemVIew.findViewById(R.id.imgEvent);

            //GradientDrawable round_drawable = (GradientDrawable) context.getDrawable(R.drawable.roundimage);
            //imgEvent.setClipToOutline(true);

            itemVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(events.get(position).getUrl())));
                }
            });
        }

        public void setClickAction(int position) {
            this.position = position;
        }
    }
}
