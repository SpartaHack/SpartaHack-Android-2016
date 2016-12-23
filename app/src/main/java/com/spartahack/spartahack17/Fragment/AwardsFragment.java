package com.spartahack.spartahack17.Fragment;

import android.support.v4.view.ViewPager;

import com.spartahack.spartahack17.Adapters.AwardsPagerAdapter;
import com.spartahack.spartahack17.R;

import butterknife.BindView;

public class AwardsFragment extends BaseFragment {

    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override public void onResume() {
        super.onResume();

        viewPager.setAdapter(new AwardsPagerAdapter(getChildFragmentManager()));
        setUpTabBar(viewPager);
    }

    @Override int getLayout() {
        return R.layout.fragment_awards;
    }
}
