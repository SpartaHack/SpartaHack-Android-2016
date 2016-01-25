package com.example.spartahack.spartahack2016.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.spartahack.spartahack2016.Fragment.HelpFragment;
import com.example.spartahack.spartahack2016.Fragment.MentorFragment;

import java.util.ArrayList;

/**
 * Created by ryancasler on 1/22/16.
 */
public class HelpDeskPagerAdapter extends FragmentPagerAdapter {

    /**
     * list of fragments in the adapter
     */
    private ArrayList<Fragment> fragments;

    /**
     * List of titles for the adapter tabs
     */
    private String[] tabs = {"Tickets", "Mentor"};

    public HelpDeskPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new HelpFragment());
        fragments.add(new MentorFragment());
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

