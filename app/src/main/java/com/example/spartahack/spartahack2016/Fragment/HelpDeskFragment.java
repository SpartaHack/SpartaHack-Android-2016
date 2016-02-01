package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.HelpDeskPagerAdapter;
import com.example.spartahack.spartahack2016.PushNotificationReceiver;
import com.example.spartahack.spartahack2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDeskFragment extends BaseFragment {

    @Bind(R.id.view_pager) ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_desk, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewPager.setAdapter(new HelpDeskPagerAdapter(getChildFragmentManager()));
        setUpTabBar(viewPager);


        Bundle args = this.getArguments();
        if (args != null && args.containsKey(PushNotificationReceiver.ACTION)) {
            String aciton = args.getString(PushNotificationReceiver.ACTION);
            String tix = args.getString(PushNotificationReceiver.OBJECT_ID);
            if (TextUtils.isEmpty(aciton)) {
            } else {
                EventBus.getDefault().post(new ModTix(tix, aciton));
            }
        }


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
