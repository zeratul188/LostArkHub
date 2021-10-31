package com.example.lostarkapplication.ui.slideshow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lostarkapplication.R;
import com.example.lostarkapplication.database.Earring1DBAdapter;
import com.example.lostarkapplication.database.Earring2DBAdapter;
import com.example.lostarkapplication.database.EquipmentDBAdapter;
import com.example.lostarkapplication.database.EquipmentStoneDBAdapter;
import com.example.lostarkapplication.database.NeckDBAdapter;
import com.example.lostarkapplication.database.Ring1DBAdapter;
import com.example.lostarkapplication.database.Ring2DBAdapter;
import com.example.lostarkapplication.database.StatDBAdapter;
import com.example.lostarkapplication.ui.slideshow.objects.Equipment;

import java.util.ArrayList;

public class PresetAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Equipment> equipments;

    private NeckDBAdapter neckDBAdapter;
    private Earring1DBAdapter earring1DBAdapter;
    private Earring2DBAdapter earring2DBAdapter;
    private Ring1DBAdapter ring1DBAdapter;
    private Ring2DBAdapter ring2DBAdapter;
    private EquipmentStoneDBAdapter equipmentStoneDBAdapter;
    private EquipmentDBAdapter equipmentDBAdapter;
    private StatDBAdapter statDBAdapter;

    public PresetAdapter(Context context, ArrayList<Equipment> equipments) {
        this.context = context;
        this.equipments = equipments;
        neckDBAdapter = new NeckDBAdapter(context);
        earring1DBAdapter = new Earring1DBAdapter(context);
        earring2DBAdapter = new Earring2DBAdapter(context);
        ring1DBAdapter = new Ring1DBAdapter(context);
        ring2DBAdapter = new Ring2DBAdapter(context);
        equipmentStoneDBAdapter = new EquipmentStoneDBAdapter(context);
        equipmentDBAdapter = new EquipmentDBAdapter(context);
        statDBAdapter = new StatDBAdapter(context);
    }

    @Override
    public int getCount() {
        return equipments.size();
    }

    @Override
    public Object getItem(int position) {
        return equipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.equipment_preset_list, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        ImageView imgDelete = convertView.findViewById(R.id.imgDelete);

        txtName.setText(equipments.get(position).getName());
        txtDate.setText(equipments.get(position).getDate());

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neckDBAdapter.open();
                neckDBAdapter.deleteData(equipments.get(position).getIndex());
                neckDBAdapter.close();
                earring1DBAdapter.open();
                earring1DBAdapter.deleteData(equipments.get(position).getIndex());
                earring1DBAdapter.close();
                earring2DBAdapter.open();
                earring2DBAdapter.deleteData(equipments.get(position).getIndex());
                earring2DBAdapter.close();
                ring1DBAdapter.open();
                ring1DBAdapter.deleteData(equipments.get(position).getIndex());
                ring1DBAdapter.close();
                ring2DBAdapter.open();
                ring2DBAdapter.deleteData(equipments.get(position).getIndex());
                ring2DBAdapter.close();
                equipmentStoneDBAdapter.open();
                equipmentStoneDBAdapter.deleteData(equipments.get(position).getIndex());
                equipmentStoneDBAdapter.close();
                equipmentDBAdapter.open();
                equipmentDBAdapter.deleteData(equipments.get(position).getIndex());
                equipmentDBAdapter.close();
                statDBAdapter.open();
                statDBAdapter.deleteData(equipments.get(position).getIndex());
                statDBAdapter.close();

                equipments.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
