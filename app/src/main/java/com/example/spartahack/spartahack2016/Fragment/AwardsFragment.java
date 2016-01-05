package com.example.spartahack.spartahack2016.Fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.AwardsPagerAdapter;
import com.example.spartahack.spartahack2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AwardsFragment extends BaseFragment {

    @Bind(R.id.view_pager) ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_awards, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO Fix this not showing content on resuming fragment

        viewPager.setAdapter(new AwardsPagerAdapter(getFragmentManager()));
        setUpTabBar(viewPager);
    }
}
