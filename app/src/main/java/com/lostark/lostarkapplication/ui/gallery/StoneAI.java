package com.lostark.lostarkapplication.ui.gallery;

public class StoneAI extends AiList{
    public StoneAI() {
        super();
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getTarget(int position) {
        return target[position];
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void upNow(int position) {
        now[position] = now[position]+1;
    }

    public void upCnt(int position) {
        cnt[position] = cnt[position]+1;
    }

    //Set max before calling.
    public void resetTarget() {
        for (int i = 0; i < target.length; i++) {
            if (i == target.length-1) {
                target[i] = 0;
            } else {
                target[i] = max;
            }
        }
    }

    public void reset() {
        for (int i = 0; i < now.length; i++) {
            now[i] = 0;
            cnt[i] = 0;
        }
        percent = 75;
    }

    public void targetUp(int position) {
        if (target[position] == max) return;
        target[position] = target[position]+1;
    }

    public void targetDown(int position) {
        if (target[position] == 0) return;
        target[position] = target[position]-1;
    }

    public int logic() {
        int result = 10;
        /*
        0 : 증가 능력 1
        1 : 증가 능력 2
        2 : 감소 능력
         */

        for (int i = 0; i < target.length; i++) {
            if (i == target.length-1) {
                if (target[i] < now[i]) {
                    return 9;
                }
            } else {
                int remain = max - cnt[i];
                if (now[i]+remain < target[i]) {
                    return 9;
                }
            }
        }

        if (isMax(0) && isMax(1) && isMax(2)) {
            return 8;
        }

        if (((isMax(2) || percent >= 55) || (isUpBurf() && percent >= 45)) && (!isMax(0) || !isMax(1))) {
            if (isMax(0)) {
                result = 1;
            } else if (isMax(1)) {
                result = 0;
            } else {
                if (percent >= 65) {
                    result = yarn();
                } else {
                    if (yarn() == 0) {
                        result = 1;
                    } else {
                        result = 0;
                    }
                }
            }
        } else {
            result = 2;
        }

        return result;
    }
}
