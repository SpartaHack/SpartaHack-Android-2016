package com.spartahack.spartahack17.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.spartahack.spartahack17.Fragment.CompanyFragment;
import com.spartahack.spartahack17.Fragment.PrizeFragment;

import java.util.ArrayList;

/**
 * Pager adapter for the awards fragment.
 */
public class AwardsPagerAdapter extends FragmentPagerAdapter {

    /**
     * list of fragments in the adapter
     */
    private final ArrayList<Fragment> fragments;

    /**
     * List of titles for the adapter tabs
     */
    private final String[] tabs = {"Prizes", "Sponsors"};

    public AwardsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new PrizeFragment());
        fragments.add(new CompanyFragment());
    }

    @Override public int getCount() {
        return fragments.size();
    }

    @Override public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
