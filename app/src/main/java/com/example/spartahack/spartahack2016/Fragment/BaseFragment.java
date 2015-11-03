package com.example.spartahack.spartahack2016.Fragment;

import android.app.Fragment;
import android.support.v4.view.ViewPager;

import de.greenrobot.event.EventBus;

/**
 * Created by ryan on 10/22/15.
 */
public class BaseFragment extends Fragment{

    protected void setUpTabBar(ViewPager viewPager){
        EventBus.getDefault().post(viewPager);
    }

}
