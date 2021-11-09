package com.lostark.lostarkapplication.ui.commander;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RestPackage {
    private LinearLayout layout;
    private TextView txtView;
    private ProgressBar progressBar;

    public RestPackage(LinearLayout layout, TextView txtView, ProgressBar progressBar) {
        this.layout = layout;
        this.txtView = txtView;
        this.progressBar = progressBar;
    }

    public RestPackage() {
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public TextView getTxtView() {
        return txtView;
    }

    public void setTxtView(TextView txtView) {
        this.txtView = txtView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public void setText(String text) {
        txtView.setText(text);
    }

    public void setVisible(int state) {
        layout.setVisibility(state);
    }
}
