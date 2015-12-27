package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.spartahack.spartahack2016.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpFragment extends BaseFragment {

    @Bind(R.id.categorySpinner) Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        ButterKnife.bind(this, view);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("iOS");
        spinnerArray.add("Android");
        spinnerArray.add("Backend");
        spinnerArray.add("Frontend");
        spinnerArray.add("Windows");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        return view;
    }

}
