package com.lostark.lostarkapplication.ui.skill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RuneAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Rune> runes;

    public RuneAdapter(Context context, ArrayList<Rune> runes) {
        this.context = context;
        this.runes = runes;
    }

    @Override
    public int getCount() {
        return runes.size();
    }

    @Override
    public Object getItem(int i) {
        return runes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) view = View.inflate(context, R.layout.rune_list, null);

        ImageView imgRuneBackground = view.findViewById(R.id.imgRuneBackground);
        ImageView imgRune = view.findViewById(R.id.imgRune);
        TextView txtRune = view.findViewById(R.id.txtRune);
        TextView txtCount = view.findViewById(R.id.txtCount);
        TextView txtContent = view.findViewById(R.id.txtContent);

        new DownloadFilesTask(imgRune).execute(runes.get(i).getUrl());
        switch (runes.get(i).getGrade()) {
            case 1:
                txtRune.setTextColor(Color.parseColor("#8dbe46"));
                imgRuneBackground.setImageResource(R.drawable.rune1);
                txtRune.setText("고급 ");
                break;
            case 2:
                txtRune.setTextColor(Color.parseColor("#3b6fa7"));
                imgRuneBackground.setImageResource(R.drawable.rune2);
                txtRune.setText("희귀 ");
                break;
            case 3:
                txtRune.setTextColor(Color.parseColor("#9056c3"));
                imgRuneBackground.setImageResource(R.drawable.rune3);
                txtRune.setText("영웅 ");
                break;
            case 4:
                txtRune.setTextColor(Color.parseColor("#c1751d"));
                imgRuneBackground.setImageResource(R.drawable.rune4);
                txtRune.setText("전설 ");
                break;
            default:
                txtRune.setTextColor(Color.parseColor("#FFFFFF"));
                imgRuneBackground.setImageResource(R.drawable.rune0);
                txtRune.setText("");
        }

        if (runes.get(i).getGrade() > 0 && runes.get(i).getGrade() < 5) txtRune.setText(txtRune.getText().toString()+runes.get(i).getName()+" 룬");
        txtContent.setText(runes.get(i).getContent());
        txtCount.setText(Integer.toString(runes.get(i).getCount()));

        return view;
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        private ImageView imgView;

        public DownloadFilesTask(ImageView imgView) {
            this.imgView = imgView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            imgView.setImageBitmap(result);
        }
    }
}
