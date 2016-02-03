package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.HelpDeskPagerAdapter;
import com.example.spartahack.spartahack2016.PushNotificationReceiver;
import com.example.spartahack.spartahack2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDeskFragment extends BaseFragment {

    @Bind(R.id.view_pager) ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_desk, container, false);

        ButterKnife.bind(this, view);

        // args from main activity from push reciever for actionable pushes
        Bundle args = this.getArguments();
        if (args != null && args.containsKey(PushNotificationReceiver.ACTION)) {

            HelpFragment helpFragment = new HelpFragment();
            helpFragment.setArguments(args);
            viewPager.setAdapter(new HelpDeskPagerAdapter(getChildFragmentManager(), helpFragment));

        } else {
            viewPager.setAdapter(new HelpDeskPagerAdapter(getChildFragmentManager()));
        }


        setUpTabBar(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static class ModTix {
        public String oid;
        public String action;

        public ModTix(String oid, String action) {
            this.oid = oid;
            this.action = action;
        }
    }

}
