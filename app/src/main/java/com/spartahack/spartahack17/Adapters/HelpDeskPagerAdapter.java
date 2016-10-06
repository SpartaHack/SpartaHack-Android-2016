package com.spartahack.spartahack17.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.spartahack.spartahack17.Fragment.MyTicketsFragment;
import com.spartahack.spartahack17.Fragment.MentorFragment;

import java.util.ArrayList;

/**
 * Created by ryancasler on 1/22/16
 * SpartaHack2016-Android
 */
public class HelpDeskPagerAdapter extends FragmentPagerAdapter {

    /**
     * list of fragments in the adapter
     */
    private final ArrayList<Fragment> fragments;

    /**
     * List of titles for the adapter tabs
     */
    private final String[] tabs = {"My Tickets", "Mentor Tickets"};

    public HelpDeskPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new MyTicketsFragment());
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

