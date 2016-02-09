package com.example.spartahack.spartahack2016.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.spartahack.spartahack2016.R;
import com.joanzapata.pdfview.PDFView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {

    public static final String MAP_FILE = "map-old.pdf";

    @Bind(R.id.pdfview) PDFView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, v);


        pdfView.fromAsset(MAP_FILE)
                .swipeVertical(true)
                .load();


        return v;
    }

}
