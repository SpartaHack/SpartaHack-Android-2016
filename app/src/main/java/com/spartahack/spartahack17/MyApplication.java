package com.spartahack.spartahack17;

import android.app.Application;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/*
 * Created by ryancasler on 11/3/15
 * SpartaHack2016-Android
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override public void onCreate() {
        super.onCreate();

        instance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/lato/Lato-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        JodaTimeAndroid.init(this);

        // init the user from shared prefs
        Cache.INSTANCE.readFromSharedPrefs(this);

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }


}
