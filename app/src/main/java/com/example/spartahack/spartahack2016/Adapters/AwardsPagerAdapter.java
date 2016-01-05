package com.example.spartahack.spartahack2016.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.spartahack.spartahack2016.Fragment.CompanyFragment;
import com.example.spartahack.spartahack2016.Fragment.JudgesFragment;

import java.util.ArrayList;

/**
 * Pager adapter for the awards fragment.
 */
public class AwardsPagerAdapter extends FragmentPagerAdapter {

    /**
     * list of fragments in the adapter
     */
    private ArrayList<Fragment> fragments;

    /**
     * List of titles for the adapter tabs
     */
    private String[] tabs = {"Judges", "Criteria"};

    public AwardsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new JudgesFragment());
        fragments.add(new CompanyFragment());
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
