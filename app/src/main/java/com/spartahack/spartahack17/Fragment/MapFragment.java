package com.spartahack.spartahack17.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;

import com.spartahack.spartahack17.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {

    @BindView(R.id.web_view) WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView.getSettings().setJavaScriptEnabled(true);
        String MAP_URL = "https://api.spartahack.com/map";
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + MAP_URL);
    }

    @Override int getLayout() {
        return R.layout.fragment_map;
    }
}