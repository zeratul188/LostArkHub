package com.lostark.lostarkapplication.ui.stamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.stamp.objects.Filter;
import com.lostark.lostarkapplication.ui.stamp.objects.StampChecklist;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private ArrayList<Filter> filters;
    private Context context;
    private StampChecklist checklist;
    private StampFragment fragment;

    public FilterAdapter(ArrayList<Filter> filters, Context context, StampChecklist checklist, StampFragment fragment) {
        this.filters = filters;
        this.context = context;
        this.checklist = checklist;
        this.fragment = fragment;
    }

    public ArrayList<Filter> getFilters() {
        return filters;
    }

    public void setChecklist(StampChecklist checklist) {
        this.checklist = checklist;
    }

    public StampChecklist getChecklist() {
        return checklist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(filters.get(position).getFilter());
        holder.setClickAction(position);

        holder.imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (filters.get(position).getFilter()) {
                    case "공통":
                        checklist.setIncrease(false);
                        break;
                    case "감소":
                        checklist.setDecrease(false);
                        break;
                    default:
                        checklist.setClass(filters.get(position).getIndex(), false);
                }
                filters.remove(position);
                fragment.refreshData(checklist);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public ImageButton imgbtnDelete;
        public int position = 9999;

        public ViewHolder(View itemVIew) {
            super(itemVIew);

            txtName = itemVIew.findViewById(R.id.txtName);
            imgbtnDelete = itemVIew.findViewById(R.id.imgbtnDelete);
        }

        public void setClickAction(int position) {
            this.position = position;
        }
    }
}
