package com.lostark.lostarkapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private LinearLayout layoutNotication;

    private String url = null;
    private WebSettings webSettings;
    private CustomToast customToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        webView = findViewById(R.id.webView);
        layoutNotication = findViewById(R.id.layoutNotication);

        customToast = new CustomToast(getApplicationContext());

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if (url != null) {
            webView.setVisibility(View.VISIBLE);
            layoutNotication.setVisibility(View.GONE);
            try {
                webView.setWebViewClient(new WebViewClient());
                webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportMultipleWindows(false);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(false);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                webSettings.setDomStorageEnabled(true);

                webView.loadUrl(url);
            } catch (Exception e) {
                customToast.createToast("??????????????? ????????? ?????????????????????.", Toast.LENGTH_LONG);
                customToast.show();
                e.printStackTrace();
            }
        } else {
            webView.setVisibility(View.GONE);
            layoutNotication.setVisibility(View.VISIBLE);
            customToast.createToast("????????? ??????????????? ????????? ???????????????.", Toast.LENGTH_SHORT);
            customToast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}