package com.example.spartahack.spartahack2016;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ryan on 11/3/15.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Keys.PARSE_1, Keys.PARSE_2);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

    }
}
