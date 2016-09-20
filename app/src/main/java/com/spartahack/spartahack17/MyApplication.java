package com.spartahack.spartahack17;

import android.app.Activity;
import android.app.Application;

import com.spartahack.spartahack17.Activity.MainActivity;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ryan on 11/3/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        boolean pushOn = getSharedPreferences(getPackageName(), Activity.MODE_PRIVATE).getBoolean(MainActivity.PUSH_PREF, true);
//        if (pushOn)
//            ParsePush.subscribeInBackground("");
//        else {
//            ParsePush.unsubscribeInBackground("");
//        }
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        JodaTimeAndroid.init(this);

    }
}
