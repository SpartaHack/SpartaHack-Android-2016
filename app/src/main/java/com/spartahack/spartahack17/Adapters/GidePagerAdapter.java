package com.spartahack.spartahack17.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.spartahack.spartahack17.Fragment.MapFragment;
import com.spartahack.spartahack17.Fragment.ScheduleFragment;

import java.util.ArrayList;

/**
 * Created by ryancasler on 1/24/16
 * SpartaHack2016-Android
 */
public class GidePagerAdapter extends FragmentPagerAdapter {

    /**
     * list of fragments in the adapter
     */
    private final ArrayList<Fragment> fragments;

    /**
     * List of titles for the adapter tabs
     */
    private final String[] tabs = {"Schedule", "Map"};

    public GidePagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new ScheduleFragment());
        fragments.add(new MapFragment());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}

