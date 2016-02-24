package com.example.spartahack.spartahack16;

import android.app.Activity;
import android.app.Application;

import com.example.spartahack.spartahack16.Activity.MainActivity;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ryan on 11/3/15.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Keys.PARSE_APP_ID, Keys.PARSE_API_KEY);
        ParseInstallation.getCurrentInstallation().saveEventually();
        boolean pushOn = getSharedPreferences(getPackageName(), Activity.MODE_PRIVATE).getBoolean(MainActivity.PUSH_PREF, true);
        if (pushOn)
            ParsePush.subscribeInBackground("");
        else {
            ParsePush.unsubscribeInBackground("");
        }


        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        JodaTimeAndroid.init(this);

    }
}
