package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.GidePagerAdapter;
import com.example.spartahack.spartahack16.R;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseFragment {

    @Bind(R.id.view_pager) ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_guide, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewPager.setAdapter(new GidePagerAdapter(getChildFragmentManager()));
        setUpTabBar(viewPager);

    }

}
