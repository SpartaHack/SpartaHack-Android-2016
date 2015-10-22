package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.R;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


}
