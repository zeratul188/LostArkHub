package com.lostark.lostarkapplication.ui.chracter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.stamp.ClearEditText;

public class ChracterFragment extends Fragment {
    private ChracterViewModel chracterViewModel;

    private ClearEditText edtSearch;
    private ImageButton imgbtnSearch;
    private WebView webView;

    private WebSettings webSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chracterViewModel = ViewModelProviders.of(this).get(ChracterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chracter, container, false);

        edtSearch = root.findViewById(R.id.edtSearch);
        imgbtnSearch = root.findViewById(R.id.imgbtnSearch);
        webView = root.findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl("https://lostark.game.onstove.com/Profile/Character");

        imgbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    webView.loadUrl("https://lostark.game.onstove.com/Profile/Character/"+edtSearch.getText().toString());
                    webView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "캐릭터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    webView.setVisibility(View.GONE);
                }
            }
        });
        return root;
    }
}
