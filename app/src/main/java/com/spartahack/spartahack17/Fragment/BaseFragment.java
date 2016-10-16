package com.spartahack.spartahack17.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * Created by ryan on 10/22/15
 * SpartaHack2016-Android
 */
public abstract class BaseFragment extends Fragment{

    protected boolean registerEventBus = false;

    protected void setUpTabBar(ViewPager viewPager){
        EventBus.getDefault().post(viewPager);
    }
    private Unbinder unbinder;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(getLayout(), container, false);

        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override public void onResume() {
        // register for event bus
        if (registerEventBus) EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override public void onPause() {
        // unregister event bus
        if (registerEventBus) EventBus.getDefault().unregister(this);
        super.onPause();
    }

    protected void hideKeyboard(View view){
        // hide keyboard!!! fuck android
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * @return the res layout id for the fragment
     */
    @SuppressWarnings("SameReturnValue")
    @LayoutRes abstract int getLayout();
}
