package com.lostark.lostarkapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IslandAwardToast {
    private Toast toast = null;
    private Context context = null;

    public IslandAwardToast(Context context) {
        this.context = context;
    }

    public void createToast(String message, int toast_length, int resource) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_layout, null);

        TextView txtView = view.findViewById(R.id.txtContent);
        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        txtView.setText(message);
        imgLogo.setImageResource(resource);

        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(toast_length);
        toast.setView(view);
    }

    public void show() {
        if (toast != null) {
            toast.show();
        } else {
            System.out.println("Toast == null!!");
        }
    }

    private int toPx(float value) {
        return (int)(value * Resources.getSystem().getDisplayMetrics().density);
    }
}
