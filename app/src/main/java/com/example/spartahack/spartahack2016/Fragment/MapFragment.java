package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.spartahack.spartahack2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {


    @Bind(R.id.web_view) WebView webView;

    private final String MAP_URL = "http://www.cse.msu.edu/~glxing/422/slides/Introduction.pdf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, v);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + MAP_URL);

        return v;
    }

}
