package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spartahack.spartahack17.Adapters.AwardsPagerAdapter;
import com.spartahack.spartahack17.R;

import butterknife.BindView;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.JOIN_GROUP;

public class AwardsFragment extends BaseFragment {

    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAnalytics.getInstance(getActivity()).logEvent(JOIN_GROUP, null);
    }

    @Override public void onResume() {
        super.onResume();

        viewPager.setAdapter(new AwardsPagerAdapter(getChildFragmentManager()));
        setUpTabBar(viewPager);
    }

    @Override int getLayout() {
        return R.layout.fragment_awards;
    }
}
