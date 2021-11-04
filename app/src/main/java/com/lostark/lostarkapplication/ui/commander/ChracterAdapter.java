package com.lostark.lostarkapplication.ui.commander;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChracterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Chracter> chracters;
    private ChracterListDBAdapter chracterListDBAdapter;
    private AlertDialog alertDialog;
    private Activity activity;
    private List<String> jobs;

    public ChracterAdapter(Context context, ArrayList<Chracter> chracters, Activity activity) {
        this.context = context;
        this.chracters = chracters;
        this.activity = activity;
        chracterListDBAdapter = new ChracterListDBAdapter(context);
    }

    @Override
    public int getCount() {
        return chracters.size();
    }

    @Override
    public Object getItem(int position) {
        return chracters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.chracter_list, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtJob = convertView.findViewById(R.id.txtJob);
        TextView txtLevel = convertView.findViewById(R.id.txtLevel);
        ImageButton imgbtnEdit = convertView.findViewById(R.id.imgbtnEdit);
        ImageButton imgbtnNotication = convertView.findViewById(R.id.imgbtnNotication);
        ImageButton imgbtnDelete = convertView.findViewById(R.id.imgbtnDelete);
        ImageView imgBackground = convertView.findViewById(R.id.imgBackground);

        jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));

        txtName.setText(chracters.get(position).getName());
        txtJob.setText(chracters.get(position).getJob());
        txtLevel.setText(Integer.toString(chracters.get(position).getLevel()));
        if (chracters.get(position).isAlarm()) imgbtnNotication.setImageResource(R.drawable.ic_notifications_black_24dp);
        else imgbtnNotication.setImageResource(R.drawable.ic_notifications_off_black_24dp);
        int index = jobs.indexOf(chracters.get(position).getJob());
        imgBackground.setImageResource(context.getResources().getIdentifier("jb"+(index+1), "drawable", context.getPackageName()));

        imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText(chracters.get(position).getName()+"을 삭제하시겠습니까?\n저장되어 있는 데이터는 모두 삭제됩니다.");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chracterListDBAdapter.open();
                        Cursor cursor = chracterListDBAdapter.fetchAllData();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            int rowID = cursor.getInt(0);
                            String name = cursor.getString(1);
                            if (name.equals(chracters.get(position).getName())) {
                                chracterListDBAdapter.deleteData(rowID);
                                break;
                            }
                            cursor.moveToNext();
                        }
                        chracterListDBAdapter.close();
                        Toast.makeText(context, chracters.get(position).getName()+"의 정보를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        chracters.remove(position);
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imgbtnNotication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chracters.get(position).isAlarm()) {
                    chracters.get(position).setAlarm(false);
                    imgbtnNotication.setImageResource(R.drawable.ic_notifications_off_black_24dp);
                } else {
                    chracters.get(position).setAlarm(true);
                    imgbtnNotication.setImageResource(R.drawable.ic_notifications_black_24dp);
                }
                chracterListDBAdapter.open();
                chracterListDBAdapter.changeAlarm(chracters.get(position).getName(), chracters.get(position).isAlarm());
                chracterListDBAdapter.close();
            }
        });

        imgbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.chracter_edit_dialog, null);

                EditText edtName = view.findViewById(R.id.edtName);
                EditText edtLevel = view.findViewById(R.id.edtLevel);
                Spinner sprJob = view.findViewById(R.id.sprJob);
                Button btnEdit = view.findViewById(R.id.btnEdit);

                jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.job_item, jobs);
                sprJob.setAdapter(adapter);

                int index = jobs.indexOf(chracters.get(position).getJob());
                sprJob.setSelection(index);

                edtName.setHint(chracters.get(position).getName());
                edtLevel.setHint(Integer.toString(chracters.get(position).getLevel()));

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtName.getText().toString().equals("") || edtLevel.getText().toString().equals("")) {
                            Toast.makeText(context, "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        chracterListDBAdapter.open();
                        chracterListDBAdapter.changeInfo(chracters.get(position).getName(), edtName.getText().toString(), sprJob.getSelectedItem().toString(), Integer.parseInt(edtLevel.getText().toString()));
                        chracterListDBAdapter.close();
                        chracters.get(position).setName(edtName.getText().toString());
                        chracters.get(position).setJob(sprJob.getSelectedItem().toString());
                        chracters.get(position).setLevel(Integer.parseInt(edtLevel.getText().toString()));
                        Toast.makeText(context, chracters.get(position).getName()+"의 정보를 수정하였습니다.", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        return convertView;
    }
}
