package com.lostark.lostarkapplication.ui.guild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.MainActivity;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.SettingActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuildFragment extends Fragment {
    private GuildViewModel guildViewModel;

    private ListView listView;
    private FloatingActionButton fabAdd;
    private Spinner sprFilter;
    private TextView txtLimit;

    private ArrayList<Guild> guilds;
    private GuildAdapter guildAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;
    private AlertDialog alertDialog, yesDialog;
    private SharedPreferences pref;
    private CustomToast toast;

    private int max = 0, count = 0;
    private boolean isStart = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        guildViewModel = ViewModelProviders.of(this).get(GuildViewModel.class);
        View root = inflater.inflate(R.layout.fragment_guild, container, false);

        listView = root.findViewById(R.id.listView);
        fabAdd = root.findViewById(R.id.fabAdd);
        sprFilter = root.findViewById(R.id.sprFilter);
        txtLimit = root.findViewById(R.id.txtLimit);

        toast = new CustomToast(getActivity());

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >= max) {
                    toast.createToast("홍보글들이 최대치까지 도달하여 더이상 홍보글을 작성하실 수 없습니다. (최대 홍보글 갯수 : "+max+")", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Intent intent = new Intent(getActivity(), AddGuildActivity.class);
                intent.putExtra("max", max);
                startActivity(intent);
            }
        });

        guilds = new ArrayList<>();
        guildAdapter = new GuildAdapter(guilds, getActivity());
        listView.setAdapter(guildAdapter);
        pref = getActivity().getSharedPreferences("setting_file", getActivity().MODE_PRIVATE);

        List<String> servers = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.servers)));
        servers.add(0, "전체");
        ArrayAdapter<String> serverAdapter = new ArrayAdapter<>(getActivity(), R.layout.server_item, servers);
        sprFilter.setAdapter(serverAdapter);
        sprFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isStart) {
                    isStart = false;
                    return;
                }
                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = getLayoutInflater().inflate(R.layout.guild_dialog, null);

                TextView txtName = dialogView.findViewById(R.id.txtName);
                TextView txtLevel = dialogView.findViewById(R.id.txtLevel);
                TextView txtServer = dialogView.findViewById(R.id.txtServer);
                TextView txtBoss = dialogView.findViewById(R.id.txtBoss);
                TextView txtMinLevel = dialogView.findViewById(R.id.txtMinLevel);
                TextView txtIF = dialogView.findViewById(R.id.txtIF);
                TextView txtSolution = dialogView.findViewById(R.id.txtSolution);
                TextView txtContent = dialogView.findViewById(R.id.txtContent);
                TextView txtDate = dialogView.findViewById(R.id.txtDate);
                Button btnEdit = dialogView.findViewById(R.id.btnEdit);
                Button btnDelete = dialogView.findViewById(R.id.btnDelete);
                Button btnLink = dialogView.findViewById(R.id.btnLink);
                Button btnClose = dialogView.findViewById(R.id.btnClose);
                LinearLayout layoutManager = dialogView.findViewById(R.id.layoutManager);

                if (pref.getString("app_id", "null").equals(guilds.get(position).getId())) {
                    layoutManager.setVisibility(View.VISIBLE);
                } else {
                    layoutManager.setVisibility(View.GONE);
                }
                txtName.setText(guilds.get(position).getName());
                txtLevel.setText(Integer.toString(guilds.get(position).getLevel()));
                txtServer.setText(guilds.get(position).getServer());
                txtBoss.setText(guilds.get(position).getBoss());
                if (guilds.get(position).getMin() == 0) {
                    txtMinLevel.setText("레벨 제한 없음");
                } else {
                    txtMinLevel.setText(guilds.get(position).getMin()+" 레벨 이상 가입 가능");
                }
                if (guilds.get(position).getCondition().equals("")) {
                    txtIF.setText("없음");
                } else {
                    txtIF.setText(guilds.get(position).getCondition());
                }
                txtSolution.setText(guilds.get(position).getSolution());
                if (guilds.get(position).getContent().equals("")) {
                    txtContent.setText("없음");
                } else {
                    txtContent.setText(guilds.get(position).getContent());
                }
                txtDate.setText(guilds.get(position).getDate()+"에 작성");
                if (guilds.get(position).getStatue() == 1) {
                    btnLink.setVisibility(View.VISIBLE);
                } else {
                    btnLink.setVisibility(View.GONE);
                }

                btnLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View dialogView2 = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                        Button btnCancel = dialogView2.findViewById(R.id.btnCancel);
                        Button btnOK = dialogView2.findViewById(R.id.btnOK);
                        TextView txtContent = dialogView2.findViewById(R.id.txtContent);

                        txtContent.setText("\"접속\"을 누르면 \""+guilds.get(position).getLink()+"\"(으)로 이동합니다. 정말로 접속하시겠습니까?");
                        btnOK.setText("접속");
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    alertDialog.dismiss();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(guilds.get(position).getLink())));
                                } catch (Exception e) {
                                    toast.createToast("잘못된 링크입니다.\n("+guilds.get(position).getLink()+")", Toast.LENGTH_SHORT);
                                    toast.show();
                                    e.printStackTrace();
                                    System.out.println("===============================================================\n"+e.getMessage());
                                }
                                yesDialog.dismiss();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                yesDialog.dismiss();
                            }
                        });

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        builder2.setView(dialogView2);

                        yesDialog = builder2.create();
                        yesDialog.setCancelable(true);
                        yesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        yesDialog.show();
                    }
                });
                
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View dialogView2 = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                        Button btnCancel = dialogView2.findViewById(R.id.btnCancel);
                        Button btnOK = dialogView2.findViewById(R.id.btnOK);
                        TextView txtContent = dialogView2.findViewById(R.id.txtContent);

                        txtContent.setText(guilds.get(position).getName()+"의 홍보글을 삭제하시겠습니까?");
                        btnOK.setText("삭제");
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reference.child("guild"+guilds.get(position).getNumber()).removeValue();
                                guilds.remove(position);
                                guildAdapter.notifyDataSetChanged();
                                yesDialog.dismiss();
                                alertDialog.dismiss();
                                toast.createToast("홍보글을 삭제하였습니다.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                yesDialog.dismiss();
                            }
                        });

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        builder2.setView(dialogView2);

                        yesDialog = builder2.create();
                        yesDialog.setCancelable(false);
                        yesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        yesDialog.show();
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(getActivity(), AddGuildActivity.class);
                        intent.putExtra("max", max);
                        intent.putExtra("isEdit", true);
                        intent.putExtra("guild", guilds.get(position));
                        startActivity(intent);
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("guilds");

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        guilds.clear();
        max = 0;
        count = 0;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.getKey().equals("max")) {
                            max = Integer.parseInt(data.getValue().toString());
                        } else {
                            String boss = data.child("boss").getValue().toString();
                            String condition = data.child("condition").getValue().toString();
                            String content = data.child("content").getValue().toString();
                            String date = data.child("date").getValue().toString();
                            String id = data.child("id").getValue().toString();
                            int index = Integer.parseInt(data.child("index").getValue().toString());
                            int level = Integer.parseInt(data.child("level").getValue().toString());
                            String link = data.child("link").getValue().toString();
                            int min = Integer.parseInt(data.child("min").getValue().toString());
                            String name = data.child("name").getValue().toString();
                            int number = Integer.parseInt(data.child("number").getValue().toString());
                            String solution = data.child("solution").getValue().toString();
                            int statue = Integer.parseInt(data.child("statue").getValue().toString());
                            String server = data.child("server").getValue().toString();
                            if (pref.getString("app_id", "null").equals(id) && index != 1) {
                                index = 9;
                            }
                            if (sprFilter.getSelectedItem().toString().equals("전체") || sprFilter.getSelectedItem().toString().equals(server)) {
                                guilds.add(new Guild(number, id, name, boss, condition, solution, content, date, link, level, min, index, statue, server));
                            }
                            count++;
                        }
                    }
                    Collections.sort(guilds);
                    guildAdapter.notifyDataSetChanged();
                    txtLimit.setText(count+"/"+max);
                } catch (Exception e) {
                    e.printStackTrace();
                    txtLimit.setText("?/?");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
