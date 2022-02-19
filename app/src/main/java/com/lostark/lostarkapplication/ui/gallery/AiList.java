package com.lostark.lostarkapplication.ui.gallery;

public class AiList {
    int[] target = new int[3];
    int[] now = new int[3];
    int[] cnt = new int[3];
    int max = 6, percent = 75;

    public AiList() {
        for (int i = 0; i < target.length; i++) {
            if (i == target.length-1) {
                target[i] = 0;
            } else {
                target[i] = max;
            }
            now[i] = 0;
            cnt[i] = 0;
        }
    }

    //증가 옵션 or 감소 옵션(position)이 최대일 경우 : true
    public boolean isMax(int position) {
        return cnt[position] == max;
    }

    //실패 갯수가 낮을 때 언제까지 45퍼를 인정하는지
    //45%부터 증가 옵션해야할 경우 : true
    public boolean isUpBurf() {
        int fail = max - cnt[2];
        int success = (max - cnt[0]) + (max - cnt[1]);
        if (fail < max/2 && 2*fail < success) {
            return true;
        } else {
            return false;
        }
    }

    //확률이 높을 때 성공 횟수가 적은 놈에게 양보 (65 or 75)
    public int yarn() {
        int[] result = {cnt[0]-now[0], cnt[1]-now[1]};
        if (result[0] > result[1]) {
            return 0;
        } else if (result[0] == result[1]) {
            int[] remain = {max - cnt[0], max - cnt[1]};
            if (remain[0] <= remain[1]) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
    /*
    public int yarn() {
        double[] result = {0.0, 0.0};
        for (int i = 0; i < result.length; i++) {
            if (cnt[i] == 0) result[i] = 1.0;
            else result[i] = (double)now[i] / (double)cnt[i];
        }
        if (result[0] < result[1]) return 0;
        else if (result[0] == result[1]) {
            if (cnt[0] <= cnt[1]) return 0;
            else return 1;
        }
        else return 1;
    }
     */
}
