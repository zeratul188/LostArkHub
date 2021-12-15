package com.lostark.lostarkapplication.ui.commander;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.BossDBAdapter;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;
import com.lostark.lostarkapplication.database.DungeonDBAdapter;
import com.lostark.lostarkapplication.ui.chracter.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CommanderFragment extends Fragment {

    private CommanderViewModel homeViewModel;

    private ListView listView;
    private Button fabAdd, fabRefresh;
    private TextView txtAllProgress, txtAllProgressInfo;
    private ProgressBar progressAll;

    private LoadingDialog loadingDialog;

    private ArrayList<Chracter> characters;
    private ChracterAdapter chracterAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;
    private AlertDialog alertDialog;
    private Bundle bundle;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CustomToast customToast;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            chracterListDBAdapter.open();
            for (int i = 0; i < bundle.getInt("size"); i++) {
                if (bundle.getBoolean("gone"+i)) continue;
                String level_str = bundle.getString("equip_level"+i);
                System.out.println(level_str+" : "+i+"=================================================");
                level_str = level_str.substring(3);
                level_str = level_str.replace(",", "");
                int level = 0;
                if (level_str.indexOf(".") != -1) {
                    double dv = Double.parseDouble(level_str);
                    level = (int)dv;
                } else level = Integer.parseInt(level_str);

                Cursor cursor = chracterListDBAdapter.fetchData(bundle.getString("name"+i));
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    String name = cursor.getString(1);
                    chracterListDBAdapter.changeLevel(name, level);
                    if (bundle.getString("add_"+name) != null) {
                        boolean isAlarm = !pref.getBoolean("homework_alarm", false);
                        ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
                        chracterDBAdapter.open();
                        if (level >= 325 && level < 1415) {
                            int now;
                            if (level >= 915 && level < 1325) now = 3;
                            else now = 2;
                            chracterDBAdapter.insertData(new Checklist("어비스 던전", "주간", "", 0, now, isAlarm));
                        }
                        if (level >= 915) {
                            chracterDBAdapter.insertData(new Checklist("도전 어비스 던전", "주간", "", 0, 2, isAlarm));
                        }
                        if (level >= 415) {
                            chracterDBAdapter.insertData(new Checklist("도전 가디언 토벌", "주간", "", 0, 3, isAlarm));
                        }
                        if (level >= 1370 && level < 1475) {
                            chracterDBAdapter.insertData(new Checklist("어비스 레이드 - 아르고스", "주간", "", 0, 3, isAlarm));
                        }
                        if (level >= 1415) {
                            chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 발탄", "주간", "", 0, 2, isAlarm));
                        }
                        if (level >= 1430) {
                            chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 비아키스", "주간", "", 0, 3, isAlarm));
                        }
                        if (level >= 1475) {
                            chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 쿠크세이튼", "주간", "", 0, 3, isAlarm));
                        }
                        if (level >= 1490) {
                            chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 아브렐슈드", "주간", "", 0, 6, isAlarm));
                        }
                        if (level >= 1385) {
                            chracterDBAdapter.insertData(new Checklist("한밤중의 서커스 리허설", "주간", "", 0, 3, isAlarm));
                        }
                        if (level >= 1430) {
                            chracterDBAdapter.insertData(new Checklist("몽환의 아스탤지어 데자뷰", "주간", "", 0, 4, isAlarm));
                        }
                        DungeonDBAdapter dungeonDBAdapter = new DungeonDBAdapter(getActivity());
                        if (level >= 1560) {
                            String[] args = dungeonDBAdapter.readData(dungeonDBAdapter.getSize()-1);
                            String content = args[0]+" : "+args[1];
                            chracterDBAdapter.insertData(new Checklist("카오스 던전", "일일", content, 0, 2, isAlarm));
                            chracterDBAdapter.insertData(new Checklist("카던 휴식", "휴식게이지", "", 0, 10, isAlarm));
                        } else {
                            for (int j = 0; j < dungeonDBAdapter.getSize(); j++) {
                                String[] args = dungeonDBAdapter.readData(j);
                                int min_level = Integer.parseInt(args[2]);
                                if (level < min_level) {
                                    if (j == 0) break;
                                    String[] results = dungeonDBAdapter.readData(j-1);
                                    String content = results[0]+" : "+results[1];
                                    chracterDBAdapter.insertData(new Checklist("카오스 던전", "일일", content, 0, 2, isAlarm));
                                    chracterDBAdapter.insertData(new Checklist("카던 휴식", "휴식게이지", "", 0, 10, isAlarm));
                                    break;
                                }
                            }
                        }
                        BossDBAdapter bossDBAdapter = new BossDBAdapter(getActivity());
                        if (level >= 1540) {
                            String[] args = bossDBAdapter.readData(bossDBAdapter.getSize()-1);
                            String content = args[1]+" : "+args[0];
                            chracterDBAdapter.insertData(new Checklist("가디언 토벌", "일일", content, 0, 2, isAlarm));
                            chracterDBAdapter.insertData(new Checklist("가디언 휴식", "휴식게이지", "", 0, 10, isAlarm));
                        } else {
                            for (int j = 0; j < bossDBAdapter.getSize(); j++) {
                                String[] args = bossDBAdapter.readData(j);
                                int min_level = Integer.parseInt(args[2]);
                                if (level < min_level) {
                                    if (j == 0) break;
                                    String[] results = bossDBAdapter.readData(j-1);
                                    String content = results[1]+" : "+results[0];
                                    chracterDBAdapter.insertData(new Checklist("가디언 토벌", "일일", content, 0, 2, isAlarm));
                                    chracterDBAdapter.insertData(new Checklist("가디언 휴식", "휴식게이지", "", 0, 10, isAlarm));
                                    break;
                                }
                            }
                        }
                        chracterDBAdapter.close();
                        bundle.remove("add_"+name);
                    }
                }
            }
            chracterListDBAdapter.close();

            loadingDialog.dismiss();
            onResume();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(CommanderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_commander, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        bundle = new Bundle();
        pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();

        customToast = new CustomToast(getActivity());

        listView = root.findViewById(R.id.listView);
        fabAdd = root.findViewById(R.id.fabAdd);
        fabRefresh = root.findViewById(R.id.fabRefresh);
        txtAllProgress = root.findViewById(R.id.txtAllProgress);
        progressAll = root.findViewById(R.id.progressAll);
        txtAllProgressInfo = root.findViewById(R.id.txtAllProgressInfo);

        chracterListDBAdapter = new ChracterListDBAdapter(getActivity());
        characters = new ArrayList<>();
        chracterAdapter = new ChracterAdapter(getActivity(), characters, getActivity(), this);
        listView.setAdapter(chracterAdapter);

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadLevelData();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.chracter_add_dialog, null);

                EditText edtName = view.findViewById(R.id.edtName);
                EditText edtLevel = view.findViewById(R.id.edtLevel);
                Spinner sprJob = view.findViewById(R.id.sprJob);
                Spinner sprServer = view.findViewById(R.id.sprServer);
                Button btnAdd = view.findViewById(R.id.btnAdd);
                TextView txtWarning = view.findViewById(R.id.txtWarning);

                if (pref.getBoolean("auto_level", true)) {
                    edtLevel.setHint("자동 설정");
                    edtLevel.setHintTextColor(Color.parseColor("#FF9999"));
                    edtLevel.setEnabled(false);
                    txtWarning.setVisibility(View.VISIBLE);
                }

                List<String> jobs = Arrays.asList(getActivity().getResources().getStringArray(R.array.job));
                ArrayAdapter<String> jobAdapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, jobs);
                sprJob.setAdapter(jobAdapter);

                List<String> servers = Arrays.asList(getActivity().getResources().getStringArray(R.array.servers));
                ArrayAdapter<String> serverAdapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, servers);
                sprServer.setAdapter(serverAdapter);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtName.getText().toString().equals("")) {
                            //Toast.makeText(getActivity(), "이름 값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("이름 값이 비어있습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        } else if (edtLevel.getText().toString().equals("") && !pref.getBoolean("auto_level", true)) {
                            //Toast.makeText(getActivity(), "레벨 값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("레벨 값이 비어있습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        } else if (isSameName(edtName.getText().toString())) {
                            //Toast.makeText(getActivity(), "이미 동일한 캐릭터가 존재합니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("이미 동일한 캐릭터가 존재합니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            return;
                        }
                        chracterListDBAdapter.open();
                        String name = edtName.getText().toString();
                        String job = sprJob.getSelectedItem().toString();
                        String server = sprServer.getSelectedItem().toString();
                        int level;
                        if (!pref.getBoolean("auto_level", true)) level = Integer.parseInt(edtLevel.getText().toString());
                        else level = 0;
                        chracterListDBAdapter.insertData(new Chracter(name, job, server, level, 0, true));
                        characters.add(new Chracter(name, job, server, level, 0, true));
                        chracterListDBAdapter.close();
                        Collections.sort(characters);
                        chracterAdapter.notifyDataSetChanged();

                        bundle.putString("add_"+name, name);

                        if (pref.getBoolean("auto_create_homework", true)) {
                            boolean isAlarm = !pref.getBoolean("homework_alarm", false);
                            chracterListDBAdapter.open();
                            ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
                            chracterListDBAdapter.close();
                            chracterDBAdapter.open();
                            chracterDBAdapter.insertData(new Checklist("에포나 주간 의뢰", "주간", "", 0, 3, isAlarm));
                            chracterDBAdapter.insertData(new Checklist("에포나 일일 의뢰", "일일", "", 0, 3, isAlarm));
                            chracterDBAdapter.insertData(new Checklist("에포나 휴식", "휴식게이지", "", 0, 10, isAlarm));
                            if (level >= 325 && level < 1415) {
                                int now;
                                if (level >= 915 && level < 1325) now = 3;
                                else now = 2;
                                chracterDBAdapter.insertData(new Checklist("어비스 던전", "주간", "", 0, now, isAlarm));
                            }
                            if (level >= 915) {
                                chracterDBAdapter.insertData(new Checklist("도전 어비스 던전", "주간", "", 0, 2, isAlarm));
                            }
                            if (level >= 415) {
                                chracterDBAdapter.insertData(new Checklist("도전 가디언 토벌", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1370 && level < 1475) {
                                chracterDBAdapter.insertData(new Checklist("어비스 레이드 - 아르고스", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1415) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 발탄", "주간", "", 0, 2, isAlarm));
                            }
                            if (level >= 1430) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 비아키스", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1475) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 쿠크세이튼", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1490) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 아브렐슈드", "주간", "", 0, 6, isAlarm));
                            }
                            if (level >= 1385) {
                                chracterDBAdapter.insertData(new Checklist("한밤중의 서커스 리허설", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1430) {
                                chracterDBAdapter.insertData(new Checklist("몽환의 아스탤지어 데자뷰", "주간", "", 0, 4, isAlarm));
                            }
                            DungeonDBAdapter dungeonDBAdapter = new DungeonDBAdapter(getActivity());
                            if (level >= 1560) {
                                String[] args = dungeonDBAdapter.readData(dungeonDBAdapter.getSize()-1);
                                String content = args[0]+" : "+args[1];
                                chracterDBAdapter.insertData(new Checklist("카오스 던전", "일일", content, 0, 2, isAlarm));
                                chracterDBAdapter.insertData(new Checklist("카던 휴식", "휴식게이지", "", 0, 10, isAlarm));
                            } else {
                                for (int i = 0; i < dungeonDBAdapter.getSize(); i++) {
                                    String[] args = dungeonDBAdapter.readData(i);
                                    int min_level = Integer.parseInt(args[2]);
                                    if (level < min_level) {
                                        if (i == 0) break;
                                        String[] results = dungeonDBAdapter.readData(i-1);
                                        String content = results[0]+" : "+results[1];
                                        chracterDBAdapter.insertData(new Checklist("카오스 던전", "일일", content, 0, 2, isAlarm));
                                        chracterDBAdapter.insertData(new Checklist("카던 휴식", "휴식게이지", "", 0, 10, isAlarm));
                                        break;
                                    }
                                }
                            }
                            BossDBAdapter bossDBAdapter = new BossDBAdapter(getActivity());
                            if (level >= 1540) {
                                String[] args = bossDBAdapter.readData(bossDBAdapter.getSize()-1);
                                String content = args[1]+" : "+args[0];
                                chracterDBAdapter.insertData(new Checklist("가디언 토벌", "일일", content, 0, 2, isAlarm));
                                chracterDBAdapter.insertData(new Checklist("가디언 휴식", "휴식게이지", "", 0, 10, isAlarm));
                            } else {
                                for (int i = 0; i < bossDBAdapter.getSize(); i++) {
                                    String[] args = bossDBAdapter.readData(i);
                                    int min_level = Integer.parseInt(args[2]);
                                    if (level < min_level) {
                                        if (i == 0) break;
                                        String[] results = bossDBAdapter.readData(i-1);
                                        String content = results[1]+" : "+results[0];
                                        chracterDBAdapter.insertData(new Checklist("가디언 토벌", "일일", content, 0, 2, isAlarm));
                                        chracterDBAdapter.insertData(new Checklist("가디언 휴식", "휴식게이지", "", 0, 10, isAlarm));
                                        break;
                                    }
                                }
                            }

                            chracterDBAdapter.close();
                        }

                        //Toast.makeText(getActivity(), name+"을 추가하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast(name+"을 추가하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        alertDialog.dismiss();
                        uploadLevelData();
                        refreshProgress();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        return root;
    }

    public void reSort() {
        Collections.sort(characters);
        chracterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getBoolean("auto_level", true)) fabRefresh.setVisibility(View.VISIBLE);
        else fabRefresh.setVisibility(View.GONE);
        characters.clear();
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            String job = cursor.getString(2);
            int level = cursor.getInt(3);
            String server = cursor.getString(5);
            int favorite = cursor.getInt(6);
            boolean isAlarm = Boolean.parseBoolean(cursor.getString(4));
            characters.add(new Chracter(name, job, server, level, favorite, isAlarm));
            cursor.moveToNext();
        }
        Collections.sort(characters);
        chracterListDBAdapter.close();
        chracterAdapter.notifyDataSetChanged();
        refreshProgress();
    }

    public void refreshProgress() {
        int now = 0;
        int max = 0;
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(cursor.getString(1)));
            chracterDBAdapter.open();
            Cursor cursor2 = chracterDBAdapter.fetchAllData();
            cursor2.moveToFirst();
            while (!cursor2.isAfterLast()) {
                if (!cursor2.getString(2).equals("일일")) {
                    cursor2.moveToNext();
                    continue;
                }
                if (pref.getBoolean("progress_homework", false)) {
                    now += cursor2.getInt(3);
                    max += cursor2.getInt(4);
                } else {
                    if (Boolean.parseBoolean(cursor.getString(4)) && Boolean.parseBoolean(cursor2.getString(5))) {
                        now += cursor2.getInt(3);
                        max += cursor2.getInt(4);
                    }
                }
                cursor2.moveToNext();
            }
            chracterDBAdapter.close();
            cursor.moveToNext();
        }
        chracterListDBAdapter.close();

        if (max == 0) {
            txtAllProgressInfo.setVisibility(View.GONE);
            progressAll.setVisibility(View.GONE);
            txtAllProgress.setText("숙제 없음");
            txtAllProgress.setTextColor(Color.parseColor("#FF8888"));
        } else {
            txtAllProgressInfo.setVisibility(View.VISIBLE);
            progressAll.setVisibility(View.VISIBLE);
            progressAll.setMax(max);
            progressAll.setProgress(now);
            if (now == max) {
                txtAllProgress.setTextColor(Color.parseColor("#92C52D"));
                txtAllProgress.setText("숙제완료");
                txtAllProgressInfo.setVisibility(View.GONE);
            } else {
                txtAllProgress.setTextColor(Color.parseColor("#FFFFFF"));
                txtAllProgress.setText((int)((double)now / (double)max * 100.0)+"%");
                txtAllProgressInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    public void uploadLevelData() {
        if (!pref.getBoolean("auto_level", true)) {
            return;
        }

        loadingDialog.show();

        new Thread() {
            @Override
            public void run() {
                chracterListDBAdapter.open();
                Cursor cursor = chracterListDBAdapter.fetchAllData();
                cursor.moveToFirst();
                int position = 0;
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(1);
                    try {
                        bundleCroring(name, ".level-info2__expedition span", "equip_level", 2, position);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bundle.putString("name"+position, name);
                    position++;
                    cursor.moveToNext();
                }
                bundle.putInt("size", cursor.getCount());
                chracterListDBAdapter.close();

                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void bundleCroring(String chracter, String id, String name, int index, int position) throws IOException {
        Document doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+chracter).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
        Elements temele = doc.select(id+":nth-child("+index+")");	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        boolean isEmpty = temele.isEmpty(); //빼온 값 null체크
        Log.d("Tag", id+" : isNull? : " + isEmpty); //로그캣 출력
        if(isEmpty == false) { //null값이 아니면 크롤링 실행
            bundle.putBoolean("gone"+position, false);
            bundle.putString(name+position, temele.get(0).text()); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        } else {
            bundle.putBoolean("gone"+position, true);
        }
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
