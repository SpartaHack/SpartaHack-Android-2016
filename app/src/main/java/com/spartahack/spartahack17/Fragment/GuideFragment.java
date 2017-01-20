package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spartahack.spartahack17.Adapters.GidePagerAdapter;
import com.spartahack.spartahack17.R;

import butterknife.BindView;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.LEVEL_UP;

public class GuideFragment extends BaseFragment {

    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAnalytics.getInstance(getActivity()).logEvent(LEVEL_UP, null);
    }

    @Override public void onResume() {
        super.onResume();

        viewPager.setAdapter(new GidePagerAdapter(getChildFragmentManager()));
        setUpTabBar(viewPager);
    }

    @Override int getLayout() {
        return R.layout.fragment_guide;
    }
}
