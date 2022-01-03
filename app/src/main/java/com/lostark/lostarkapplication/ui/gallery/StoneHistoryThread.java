package com.lostark.lostarkapplication.ui.gallery;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class StoneHistoryThread implements Runnable {
    private Handler handler;
    private ImageView[] imgFirst, imgSecond, imgThird;
    private Button btnStart;
    private double second = 0;
    private ArrayList<StoneHistory> histories;

    private int one = 0, two = 0, three = 0, index = 0;

    public StoneHistoryThread(Handler handler, ImageView[] imgFirst, ImageView[] imgSecond, ImageView[] imgThird, ArrayList<StoneHistory> histories, Button btnStart) {
        this.handler = handler;
        this.imgFirst = imgFirst;
        this.imgSecond = imgSecond;
        this.imgThird = imgThird;
        this.histories = histories;
        this.btnStart = btnStart;
    }

    public void setSecond(double second) {
        this.second = second;
    }

    public void run() {
        index = 0;
        one = 0;
        two = 0;
        three = 0;
        handler.post(new Runnable() {
            @Override
            public void run() {
                int invisible_start = 0;
                if (histories.size() == 18) invisible_start = 6;
                else if (histories.size() == 24) invisible_start = 8;
                else if (histories.size() == 27) invisible_start = 9;
                else invisible_start = 10;
                for (int i = 0; i < imgFirst.length; i++) {
                    imgFirst[i].setImageResource(R.drawable.none);
                    imgSecond[i].setImageResource(R.drawable.none);
                    imgThird[i].setImageResource(R.drawable.deburf_none);
                    if (imgFirst[i].getVisibility() == View.INVISIBLE) imgFirst[i].setVisibility(View.VISIBLE);
                    if (imgSecond[i].getVisibility() == View.INVISIBLE) imgSecond[i].setVisibility(View.VISIBLE);
                    if (imgThird[i].getVisibility() == View.INVISIBLE) imgThird[i].setVisibility(View.VISIBLE);
                    if (invisible_start <= i) {
                        imgFirst[i].setVisibility(View.INVISIBLE);
                        imgSecond[i].setVisibility(View.INVISIBLE);
                        imgThird[i].setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        try {
            while (!Thread.currentThread().isInterrupted() && index < histories.size()) {
                switch (histories.get(index).getNum()) {
                    case 1:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (histories.get(index).isSuccess()) imgFirst[one].setImageResource(R.drawable.success);
                                else imgFirst[one].setImageResource(R.drawable.fail);
                                one++;
                            }
                        });
                        break;
                    case 2:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (histories.get(index).isSuccess()) imgSecond[two].setImageResource(R.drawable.success);
                                else imgSecond[two].setImageResource(R.drawable.fail);
                                two++;
                            }
                        });
                        break;
                    case 3:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (histories.get(index).isSuccess()) imgThird[three].setImageResource(R.drawable.deburf_success);
                                else imgThird[three].setImageResource(R.drawable.deburf_fail);
                                three++;
                            }
                        });
                        break;
                }

                Thread.sleep((long) (second*1000));
                index++;
            }
        } catch (Exception e) {
            //TODO: handle finally clause
        } finally {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnStart.setBackgroundResource(R.drawable.restbuttonstyle);
                    btnStart.setText("시작");
                }
            });
        }
    }
}
