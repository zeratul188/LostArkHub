package com.lostark.lostarkapplication.ui.chracter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.stamp.ClearEditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChracterFragment extends Fragment {
    private ChracterViewModel chracterViewModel;

    private String url = null;
    private Bundle bundle = null;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (bundle.getBoolean("gone")) {
                layoutResult.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                return;
            }
            Bundle bundle = msg.getData();
            txtNickname.setText(bundle.getString("nickname"));
            txtServer.setText(bundle.getString("server"));
            txtGroupLevel.setText(bundle.getString("group_level"));
            txtAttackLevel.setText(bundle.getString("attack_level"));
            txtEquipLevel.setText(bundle.getString("equip_level"));
            txtMaxLevel.setText(bundle.getString("max_level"));
            txtBeforeName.setText(bundle.getString("before_name"));
            txtClan.setText(bundle.getString("clan"));
            txtPVP.setText(bundle.getString("pvp"));
            txtGround.setText(bundle.getString("ground1")+" "+bundle.getString("ground2"));
            txtJob.setText(bundle.getString("job"));

            ArrayList<String> jobs = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.job)));
            if (jobs.indexOf(bundle.getString("job")) != -1) {
                imgJob.setImageResource(getActivity().getResources().getIdentifier("jbi"+(jobs.indexOf(bundle.getString("job"))+1), "drawable", getActivity().getPackageName()));
            }

            txtAttack.setText(bundle.getString("attack"));
            txtMaxHealth.setText(bundle.getString("max_health"));

            for (int i = 0; i < txtStat.length; i++) {
                txtStat[i].setText(bundle.getString("stat"+(i+1)));
            }

            for (int i = 0; i < stamp_index; i++) {
                stamps.add(bundle.getString("stamp"+(i+1)));
            }
            adapter.notifyDataSetChanged();

            layoutResult.setVisibility(View.VISIBLE);
            loadingDialog.dismiss();
        }
    };

    private LoadingDialog loadingDialog;

    private ClearEditText edtSearch;
    private ImageButton imgbtnSearch;
    private LinearLayout layoutResult;
    private ScrollView scrollView;

    //캐릭터 정보
    private CircleImageView imgJob;
    private TextView txtNickname, txtServer, txtGroupLevel, txtAttackLevel, txtEquipLevel, txtMaxLevel, txtBeforeName, txtClan, txtPVP, txtGround, txtJob;

    //기본 특성
    private TextView txtAttack, txtMaxHealth;

    //전투 특성
    private TextView[] txtStat = new TextView[6];

    //각인 효과
    private ListView listStamp;
    private int stamp_index = 0;
    ArrayList<String> stamps;
    ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chracterViewModel = ViewModelProviders.of(this).get(ChracterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chracter, container, false);

        edtSearch = root.findViewById(R.id.edtSearch);
        imgbtnSearch = root.findViewById(R.id.imgbtnSearch);
        scrollView = root.findViewById(R.id.scrollView);

        imgJob = root.findViewById(R.id.imgJob);
        txtNickname = root.findViewById(R.id.txtNickname);
        txtServer = root.findViewById(R.id.txtServer);
        txtGroupLevel = root.findViewById(R.id.txtGroupLevel);
        txtAttackLevel = root.findViewById(R.id.txtAttackLevel);
        txtEquipLevel = root.findViewById(R.id.txtEquipLevel);
        txtMaxLevel = root.findViewById(R.id.txtMaxLevel);
        txtBeforeName = root.findViewById(R.id.txtBeforeName);
        txtClan = root.findViewById(R.id.txtClan);
        txtPVP = root.findViewById(R.id.txtPVP);
        txtGround = root.findViewById(R.id.txtGround);
        layoutResult = root.findViewById(R.id.layoutResult);
        txtJob = root.findViewById(R.id.txtJob);

        txtAttack = root.findViewById(R.id.txtAttack);
        txtMaxHealth = root.findViewById(R.id.txtMaxHealth);

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);

        for (int i = 0; i < txtStat.length; i++) txtStat[i] = root.findViewById(getActivity().getResources().getIdentifier("txtStat"+(i+1), "id", getActivity().getPackageName()));

        listStamp = root.findViewById(R.id.listStamp);
        stamps = new ArrayList<>();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, stamps){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                return view;
            }
        };
        listStamp.setAdapter(adapter);
        listStamp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        bundle = new Bundle();

        imgbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingDialog.show();

                url = "https://lostark.game.onstove.com/Profile/Character/"+edtSearch.getText().toString();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            bundleCroring(".profile-character-info__name", "nickname"); //닉네임
                            bundleCroring(".profile-character-info__server", "server"); //서버
                            bundleCroring(".level-info__expedition span", "group_level", 2); //원정대 레벨
                            bundleCroring(".level-info__item span", "attack_level", 2); //전투 레벨
                            bundleCroring_alt(".profile-character-info__img", "job"); //직업 (alt 속성)
                            bundleCroring(".level-info2__expedition span", "equip_level", 2); //장착 아이템 레벨
                            bundleCroring(".level-info2__item span", "max_level", 2); //달성 아이템 레벨
                            bundleCroring(".game-info__title span", "before_name", 2); //칭호
                            bundleCroring(".game-info__guild span", "clan", 2); //길드
                            bundleCroring(".level-info__pvp span", "pvp", 2); //PVP
                            bundleCroring(".game-info__wisdom span", "ground1", 2); //영지 레벨
                            bundleCroring(".game-info__wisdom span", "ground2", 3); //영지 이름

                            //기본 특성 game-tooltip game-tooltip-item
                            //bundleCroring(".profile-ability-basic ul li:nth-child(1) span", "attack", 2); //공격력
                            bundleCroring("#lostark-wrapper > div.game-tooltip.game-tooltip-item > div.NameTagBox > p > font", "attack", 2); //공격력
                            bundleCroring(".profile-ability-basic ul li:nth-child(2) span", "max_health", 2); //최대 생명력

                            //전투 특성
                            for (int i = 0; i < txtStat.length; i++) bundleCroring(".profile-ability-battle ul li:nth-child("+(i+1)+") span", "stat"+(i+1), 2); //치명, 특화, 제압, 신속, 인내, 숙련

                            //각인 효과
                            stamp_index = 0;
                            int index = 0;
                            int ul_index = 0;
                            while (true) {
                                if (!bundleCroringStamp(ul_index, stamp_index, index)) {
                                    if (index == 0) break;
                                    ul_index++;
                                    index = 0;
                                } else {
                                    index++;
                                    stamp_index++;
                                }
                            }

                            Message msg = new Message();
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        return root;
    }

    private void bundleCroring_alt(String id, String name) throws IOException {
        Document doc = Jsoup.connect(url).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
        Elements temele = doc.select(id);	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        boolean isEmpty = temele.isEmpty(); //빼온 값 null체크
        Log.d("Tag", id+" : isNull? : " + isEmpty); //로그캣 출력
        if(isEmpty == false) { //null값이 아니면 크롤링 실행
            bundle.putBoolean("gone", false);
            bundle.putString(name, temele.get(0).attr("alt")); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        } else {
            if (!id.equals(".profile-character-info__name")) return;
            bundle.putBoolean("gone", true);
        }
    }

    private void bundleCroring(String id, String name) throws IOException {
        Document doc = Jsoup.connect(url).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
        Elements temele = doc.select(id);	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        boolean isEmpty = temele.isEmpty(); //빼온 값 null체크
        Log.d("Tag", id+" : isNull? : " + isEmpty); //로그캣 출력
        if(isEmpty == false) { //null값이 아니면 크롤링 실행
            bundle.putBoolean("gone", false);
            bundle.putString(name, temele.get(0).text()); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        } else {
            if (!id.equals(".profile-character-info__name")) return;
            bundle.putBoolean("gone", true);
        }
    }

    private boolean bundleCroringStamp(int ul_index, int stamp_index, int index) throws IOException {
        Document doc = Jsoup.connect(url).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
        Elements temele = doc.select(".swiper-wrapper ul:nth-child("+(ul_index+1)+") li:nth-child("+(index+1)+") span");	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        boolean isEmpty = temele.isEmpty(); //빼온 값 null체크
        Log.d("Tag", "isNull? : " + isEmpty); //로그캣 출력
        if(isEmpty == false) { //null값이 아니면 크롤링 실행
            bundle.putString("stamp"+(stamp_index+1), temele.get(0).text()); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
            return true;
        } else {
            return false;
        }
    }

    private void bundleCroring(String id, String name, int index) throws IOException {
        Document doc = Jsoup.connect(url).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
        Elements temele = doc.select(id+":nth-child("+index+")");	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        boolean isEmpty = temele.isEmpty(); //빼온 값 null체크
        Log.d("Tag", id+" : isNull? : " + isEmpty); //로그캣 출력
        if(isEmpty == false) { //null값이 아니면 크롤링 실행
            bundle.putBoolean("gone", false);
            bundle.putString(name, temele.get(0).text()); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        } else {
            if (!id.equals(".profile-character-info__name")) return;
            bundle.putBoolean("gone", true);
        }
    }
}
