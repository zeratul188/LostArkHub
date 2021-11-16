package com.lostark.lostarkapplication.ui.skill;

import android.widget.ProgressBar;
import android.widget.TextView;

public class DataNetwork {
    private int skillpoint, max;
    private TextView txtView;
    private ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public TextView getTxtView() {
        return txtView;
    }

    public void setTxtView(TextView txtView) {
        this.txtView = txtView;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSkillpoint() {
        return skillpoint;
    }

    public void setSkillpoint(int skillpoint) {
        this.skillpoint = skillpoint;
    }
}
