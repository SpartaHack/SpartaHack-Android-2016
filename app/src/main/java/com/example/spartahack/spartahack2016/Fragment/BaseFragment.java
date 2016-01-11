package com.example.spartahack.spartahack2016.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    protected void hideKeyboard(View view){
        // hide keyboard!!! fuck android
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
