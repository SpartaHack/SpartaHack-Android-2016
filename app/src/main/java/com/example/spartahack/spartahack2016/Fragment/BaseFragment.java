package com.example.spartahack.spartahack2016.Fragment;

import android.app.Fragment;
import android.support.v4.view.ViewPager;

import de.greenrobot.event.EventBus;

/**
 * Created by ryan on 10/22/15.
 */
public class BaseFragment extends Fragment{

    protected boolean registerEventBus = false;
    protected void setUpTabBar(ViewPager viewPager){
        EventBus.getDefault().post(viewPager);
    }

    @Override
    public void onResume() {
        // register for event bus
        if (registerEventBus) EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        // unregister event bus
        if (registerEventBus) EventBus.getDefault().unregister(this);
        super.onPause();
    }

}
