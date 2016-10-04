package com.spartahack.spartahack17.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.spartahack.spartahack17.Presenter.BasePresenter;
import com.spartahack.spartahack17.View.BaseView;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 */

public abstract class MVPActivity<V extends BaseView, P extends BasePresenter> extends AppCompatActivity
    implements BaseView{

    /**
     * Presenter for the activity.
     */
    private P presenter;

    /**
     * View for the activity.
     */
    private V view;

    protected P getMVPPresenter() {return  presenter;}
    protected V getMVPView() {return view;}

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set transparent status bar if its kitkat or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override protected void onResume() {
        // register for event bus
        if (registerEventbus()) EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override protected void onPause() {
        // unregister event bus
        if (registerEventbus()) EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @LayoutRes abstract int getLayout();
    abstract boolean registerEventbus();

    @Override public void setContentView(int layoutResID) {
        super.setContentView(getLayout());
        ButterKnife.bind(this);
    }
}
