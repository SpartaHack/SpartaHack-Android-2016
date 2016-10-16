package com.spartahack.spartahack17.Fragment;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.spartahack.spartahack17.Adapters.GidePagerAdapter;
import com.spartahack.spartahack17.R;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseFragment {

    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override public void onResume() {
        super.onResume();

        viewPager.setAdapter(new GidePagerAdapter(getChildFragmentManager()));
        setUpTabBar(viewPager);
    }

    @Override int getLayout() {
        return R.layout.fragment_guide;
    }
}
