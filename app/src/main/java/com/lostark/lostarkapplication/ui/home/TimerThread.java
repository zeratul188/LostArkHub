package com.lostark.lostarkapplication.ui.home;

import android.os.Handler;
import android.widget.TextView;

public class TimerThread extends Thread{
    private long time = 0;
    private TextView txtView;
    private Handler handler;

    public TimerThread(TextView txtView, Handler handler) {
        this.txtView = txtView;
        this.handler = handler;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void run() {
        while (time > 0) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            time -= 1;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long value = time;
                    String result = "";
                    long hour = value / (60*60);
                    if (hour != 0) {
                        result += hour+"시간 ";
                    }
                    value %= (long)(60*60);
                    long minute = value / 60;
                    if (minute != 0) {
                        result += minute+"분 ";
                    }
                    value %= (long)60;
                    if (value != 0) {
                        result += value+"초";
                    }
                    txtView.setText(result);
                }
            });
        }

        if (time <= 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txtView.setText("출현 중");
                }
            });
        }
    }
}
