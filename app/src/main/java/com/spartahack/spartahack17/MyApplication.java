package com.spartahack.spartahack17;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by ryancasler on 11/3/15
 * SpartaHack2016-Android
 */
public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        // init the user from shared prefs
        Cache.INSTANCE.readFromSharedPrefs(this);
    }
}
