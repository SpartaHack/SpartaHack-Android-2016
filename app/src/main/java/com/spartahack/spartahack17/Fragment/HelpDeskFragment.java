package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class HelpDeskFragment extends BaseFragment {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.no_user) LinearLayout noUser;

    @Override public void onResume() {
        super.onResume();
    }

    @Override int getLayout() {
        return R.layout.fragment_help_desk;
    }

    @OnClick(R.id.login) void onLogin() {
        MainActivity activity = ((MainActivity) getActivity());
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProfileFragment.I_EXTRA_FROM, ProfileFragment.I_EXTRA_FROM);
        fragment.setArguments(bundle);
        activity.switchContent(R.id.container, fragment);
    }
}
