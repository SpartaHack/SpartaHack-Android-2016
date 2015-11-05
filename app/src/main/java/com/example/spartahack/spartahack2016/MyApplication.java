package com.example.spartahack.spartahack2016;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by ryan on 11/3/15.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, Keys.PARSE_1, Keys.PARSE_2);
    }
}
