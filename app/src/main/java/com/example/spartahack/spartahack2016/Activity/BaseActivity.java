package com.example.spartahack.spartahack2016.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by ryan on 10/22/15.
 */
public class BaseActivity extends AppCompatActivity{

    protected boolean registerEventBus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set transparent status bar if its kitkat or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onResume() {
        // register for event bus
        if (registerEventBus) EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // unregister event bus
        if (registerEventBus) EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * Start an activity
     * @param activity class of the activity to start
     */
    protected void start(Class<? extends BaseActivity> activity) {
        startActivity(new Intent(this, activity));
    }

    /**
     * Hide the soft keyboard
     * @param view to get window token
     */
    protected void hideKeyboard(View view){
        // hide keyboard!!! fuck android
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
