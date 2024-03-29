package com.lostark.lostarkapplication.ui.commander;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.MainActivity;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChracterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Chracter> chracters;
    private ChracterListDBAdapter chracterListDBAdapter;
    private ChracterDBAdapter chracterDBAdapter;
    private AlertDialog alertDialog;
    private Activity activity;
    private List<String> jobs, servers;
    private CommanderFragment fragment;
    private SharedPreferences pref;
    private CustomToast customToast;

    private DisplayMetrics displayMetrics;
    private float density;

    public ChracterAdapter(Context context, ArrayList<Chracter> chracters, Activity activity, CommanderFragment fragment) {
        this.context = context;
        this.chracters = chracters;
        this.activity = activity;
        this.fragment = fragment;
        chracterListDBAdapter = new ChracterListDBAdapter(context);
        pref = context.getSharedPreferences("setting_file", MODE_PRIVATE);
        customToast = new CustomToast(context);
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //dpi = displayMetrics.densityDpi;
        density = displayMetrics.density;
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
        LinearLayout layoutMain = convertView.findViewById(R.id.layoutMain);
        ImageView imgJob = convertView.findViewById(R.id.imgJob);
        TextView txtServer = convertView.findViewById(R.id.txtServer);
        ImageButton imgbtnFavorite = convertView.findViewById(R.id.imgbtnFavorite);
        TextView txtProgress = convertView.findViewById(R.id.txtProgress);
        TextView txtProgressInfo = convertView.findViewById(R.id.txtProgressInfo);
        ProgressBar progressHomework = convertView.findViewById(R.id.progressHomework);
        LinearLayout layoutTitle = convertView.findViewById(R.id.layoutTitle);
        LinearLayout layoutProgress = convertView.findViewById(R.id.layoutProgress);
        FrameLayout layoutImage = convertView.findViewById(R.id.layoutImage);
        jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));

        txtName.setText(chracters.get(position).getName());
        txtJob.setText(chracters.get(position).getJob());
        txtServer.setText(chracters.get(position).getServer());
        txtLevel.setText(Integer.toString(chracters.get(position).getLevel()));
        if (chracters.get(position).isAlarm()) imgbtnNotication.setImageResource(R.drawable.ic_notifications_black_24dp);
        else imgbtnNotication.setImageResource(R.drawable.ic_notifications_off_black_24dp);
        int index = jobs.indexOf(chracters.get(position).getJob());
        imgJob.setImageResource(context.getResources().getIdentifier("jbi"+(index+1), "drawable", context.getPackageName()));

        chracterListDBAdapter.open();
        chracterDBAdapter = new ChracterDBAdapter(context, "CHRACTER"+chracterListDBAdapter.getRowID(chracters.get(position).getName()));
        chracterListDBAdapter.close();
        chracterDBAdapter.open();
        Cursor cursor = chracterDBAdapter.fetchAllData();
        cursor.moveToFirst();
        int progress_max = 0;
        int progress = 0;
        if (pref.getBoolean("progress_homework", false)) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(2).equals("일일")) {
                    progress += cursor.getInt(3);
                    progress_max += cursor.getInt(4);
                }
                cursor.moveToNext();
            }
        } else {
            if (chracters.get(position).isAlarm()) {
                while (!cursor.isAfterLast()) {
                    if (cursor.getString(2).equals("일일") && Boolean.parseBoolean(cursor.getString(5))) {
                        progress += cursor.getInt(3);
                        progress_max += cursor.getInt(4);
                    }
                    cursor.moveToNext();
                }
            }
        }
        chracterDBAdapter.close();

        if (progress_max != 0) {
            if (progress != 0) progressHomework.setVisibility(View.VISIBLE);
            else progressHomework.setVisibility(View.GONE);
            txtProgressInfo.setVisibility(View.VISIBLE);
            progressHomework.setMax(progress_max);
            progressHomework.setProgress(progress);
            int result = (int)((double)progress / (double)progress_max * 100.0);
            if (result == 100) {
                txtProgress.setTextColor(Color.parseColor("#92C52D"));
                txtProgress.setText("숙제완료");
                txtProgressInfo.setVisibility(View.GONE);
            } else {
                txtProgress.setTextColor(Color.parseColor("#FFFFFF"));
                txtProgress.setText(result+"%");
                txtProgressInfo.setVisibility(View.VISIBLE);
            }
        } else {
            progressHomework.setVisibility(View.GONE);
            txtProgressInfo.setVisibility(View.GONE);
            txtProgress.setText("");
        }


        if (chracters.get(position).getFavorite() == 1) {
            imgbtnFavorite.setImageResource(R.drawable.ic_baseline_star_24);
            txtName.setTextColor(Color.parseColor("#FFCA45"));
        } else {
            imgbtnFavorite.setImageResource(R.drawable.ic_baseline_star_border_24);
            txtName.setTextColor(Color.parseColor("#FFFFFF"));
        }

        imgbtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chracterListDBAdapter.open();
                if (chracters.get(position).getFavorite() == 1) {
                    chracterListDBAdapter.checkFavorite(chracters.get(position).getName(), false);
                    chracters.get(position).setFavorite(0);
                    imgbtnFavorite.setImageResource(R.drawable.ic_baseline_star_border_24);
                    txtName.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    if (chracterListDBAdapter.emptyFavorite()) {
                        chracterListDBAdapter.checkFavorite(chracters.get(position).getName(), true);
                        chracters.get(position).setFavorite(1);
                        imgbtnFavorite.setImageResource(R.drawable.ic_baseline_star_24);
                        txtName.setTextColor(Color.parseColor("#FFCA45"));
                    } else {
                        //Toast.makeText(context, "이미 대표 캐릭터로 지정된 캐릭터가 있습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("이미 대표 캐릭터로 지정된 캐릭터가 있습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                    }
                }
                chracterListDBAdapter.close();
                ((MainActivity)activity).uploadFavoriteChracter();
                fragment.reSort();
            }
        });

        imgJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChecklistActivity.class);
                intent.putExtra("chracter_name", chracters.get(position).getName());
                intent.putExtra("chracter_level", chracters.get(position).getLevel());
                context.startActivity(intent);
            }
        });

        layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChecklistActivity.class);
                intent.putExtra("chracter_name", chracters.get(position).getName());
                intent.putExtra("chracter_level", chracters.get(position).getLevel());
                context.startActivity(intent);
            }
        });

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
                                chracterDBAdapter = new ChracterDBAdapter(context, "CHRACTER"+chracterListDBAdapter.getRowID(name));
                                chracterDBAdapter.open();
                                chracterDBAdapter.dropTable();
                                String db_path = "/data/data/"+context.getPackageName();
                                String db_name = "LOSTARKHUB_CHRACTER"+chracterListDBAdapter.getRowID(name);
                                String db_full_path = db_path+"/databases/"+db_name;
                                File file = new File(db_full_path);
                                if (!file.delete()) {
                                    //Toast.makeText(context, db_full_path+"파일 삭제 실패!", Toast.LENGTH_SHORT).show();
                                    customToast.createToast(db_full_path+"파일 삭제 실패!", Toast.LENGTH_SHORT);
                                    customToast.show();
                                }
                                db_name= "LOSTARKHUB_CHRACTER"+chracterListDBAdapter.getRowID(name)+"-journal";
                                db_full_path = db_path+"/databases/"+db_name;
                                file = new File(db_full_path);
                                if (!file.delete()) {
                                    //Toast.makeText(context, db_full_path+"파일 삭제 실패!", Toast.LENGTH_SHORT).show();
                                    customToast.createToast(db_full_path+"파일 삭제 실패!", Toast.LENGTH_SHORT);
                                    customToast.show();
                                }
                                chracterDBAdapter.close();
                                chracterListDBAdapter.deleteData(rowID);
                                break;
                            }
                            cursor.moveToNext();
                        }
                        chracterListDBAdapter.close();
                        //Toast.makeText(context, chracters.get(position).getName()+"의 정보를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast(chracters.get(position).getName()+"의 정보를 삭제하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        chracters.remove(position);
                        notifyDataSetChanged();
                        ((MainActivity)activity).uploadFavoriteChracter();
                        fragment.refreshProgress();
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

                chracterListDBAdapter.open();
                chracterDBAdapter = new ChracterDBAdapter(context, "CHRACTER"+chracterListDBAdapter.getRowID(chracters.get(position).getName()));
                chracterListDBAdapter.close();
                chracterDBAdapter.open();
                Cursor cursor = chracterDBAdapter.fetchAllData();
                cursor.moveToFirst();
                int progress_max = 0;
                int progress = 0;
                if (pref.getBoolean("progress_homework", false)) {
                    while (!cursor.isAfterLast()) {
                        if (cursor.getString(2).equals("일일")) {
                            progress += cursor.getInt(3);
                            progress_max += cursor.getInt(4);
                        }
                        cursor.moveToNext();
                    }
                } else {
                    if (chracters.get(position).isAlarm()) {
                        while (!cursor.isAfterLast()) {
                            if (cursor.getString(2).equals("일일") && Boolean.parseBoolean(cursor.getString(5))) {
                                progress += cursor.getInt(3);
                                progress_max += cursor.getInt(4);
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                chracterDBAdapter.close();

                if (progress_max != 0) {
                    if (progress != 0) progressHomework.setVisibility(View.VISIBLE);
                    else progressHomework.setVisibility(View.GONE);
                    txtProgressInfo.setVisibility(View.VISIBLE);
                    progressHomework.setMax(progress_max);
                    progressHomework.setProgress(progress);
                    int result = (int)((double)progress / (double)progress_max * 100.0);
                    txtProgress.setText(result+"%");
                    if (result == 100) txtProgress.setTextColor(Color.parseColor("#FE6E0E"));
                    else txtProgress.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    progressHomework.setVisibility(View.GONE);
                    txtProgressInfo.setVisibility(View.GONE);
                    txtProgress.setText("");
                }

                fragment.refreshProgress();
            }
        });

        imgbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.chracter_edit_dialog, null);

                EditText edtName = view.findViewById(R.id.edtName);
                EditText edtLevel = view.findViewById(R.id.edtLevel);
                Spinner sprJob = view.findViewById(R.id.sprJob);
                Spinner sprServer = view.findViewById(R.id.sprServer);
                Button btnEdit = view.findViewById(R.id.btnEdit);
                ImageButton btnNameCopy = view.findViewById(R.id.btnNameCopy);
                ImageButton btnLevelCopy = view.findViewById(R.id.btnLevelCopy);
                TextView txtWarning = view.findViewById(R.id.txtWarning);
                LinearLayout layoutOther = view.findViewById(R.id.layoutOther);

                SharedPreferences pref = activity.getSharedPreferences("setting_file", MODE_PRIVATE);
                if (pref.getBoolean("auto_level", true)) {
                    txtWarning.setVisibility(View.VISIBLE);
                    layoutOther.setVisibility(View.GONE);
                    btnNameCopy.setVisibility(View.GONE);
                }

                jobs = Arrays.asList(context.getResources().getStringArray(R.array.job));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.job_item, jobs);
                sprJob.setAdapter(adapter);

                servers = Arrays.asList(context.getResources().getStringArray(R.array.servers));
                ArrayAdapter<String> serverAdapter = new ArrayAdapter<>(context, R.layout.job_item, servers);
                sprServer.setAdapter(serverAdapter);

                int job_index = jobs.indexOf(chracters.get(position).getJob());
                sprJob.setSelection(job_index);

                int server_index = servers.indexOf(chracters.get(position).getServer());
                sprServer.setSelection(server_index);

                edtName.setHint(chracters.get(position).getName());
                if (!pref.getBoolean("auto_level", true)) edtLevel.setHint(Integer.toString(chracters.get(position).getLevel()));

                btnNameCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtName.setText(chracters.get(position).getName());
                    }
                });

                btnLevelCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtLevel.setText(Integer.toString(chracters.get(position).getLevel()));
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = chracters.get(position).getName();
                        if (edtName.getText().toString().equals("")) {
                            customToast.createToast("이름 값이 비어있습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        } else if (edtLevel.getText().toString().equals("") && !pref.getBoolean("auto_level", true)) {
                            //Toast.makeText(context, "레벨 값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("레벨 값이 비어있습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        } else if (!edtName.getText().toString().equals(name) && isSameName(edtName.getText().toString())) {
                            //Toast.makeText(context, "이미 동일한 캐릭터가 존재합니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("이미 동일한 캐릭터가 존재합니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        }
                        int level;
                        if (pref.getBoolean("auto_level", true)) level = 0;
                        else level = Integer.parseInt(edtLevel.getText().toString());
                        chracterListDBAdapter.open();
                        chracterListDBAdapter.changeInfo(chracters.get(position).getName(), edtName.getText().toString(), sprJob.getSelectedItem().toString(), level, sprServer.getSelectedItem().toString());
                        chracterListDBAdapter.close();
                        chracters.get(position).setName(edtName.getText().toString());
                        chracters.get(position).setJob(sprJob.getSelectedItem().toString());
                        chracters.get(position).setLevel(level);
                        //Toast.makeText(context, chracters.get(position).getName()+"의 정보를 수정하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast(chracters.get(position).getName()+"의 정보를 수정하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        notifyDataSetChanged();
                        ((MainActivity)activity).uploadFavoriteChracter();
                        fragment.reSort();
                        fragment.uploadLevelData();
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

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(100 * density + 0.5));
        layoutTitle.setLayoutParams(titleParams);
        int width = (int)(100 * density + 0.5);
        int progress_height = (int)(4 * density + 0.5);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutProgress.getLayoutParams();
        params.width = width;
        params.height = progress_height;
        params.leftMargin = (-1)*((width/2)-(progress_height/2));
        params.rightMargin = (-1)*((width/2)-(progress_height/2));
        params.topMargin = width/2-(progress_height/2);
        layoutProgress.setLayoutParams(params);

        layoutImage.getLayoutParams().width = width;
        layoutImage.getLayoutParams().height = width;

        return convertView;
    }

    private boolean isSameName(String str) {
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            if (str.equals(name)) {
                chracterListDBAdapter.close();
                return true;
            }
            cursor.moveToNext();
        }
        chracterListDBAdapter.close();
        return false;
    }
}
