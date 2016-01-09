package com.example.spartahack.spartahack2016;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParsePushBroadcastReceiver;

import io.realm.RealmObject;

/**
 * Created by ryancasler on 12/22/15.
 * Custom push notification receiver for parse push notifications
 */
public class PushNotificationReceiver extends ParsePushBroadcastReceiver {

    /**
     * Tag for logs
     */
    private static String TAG = "Push Receiver";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.d(TAG, "Recieved");

        // get the json string form the push
        String jsonString = intent.getStringExtra(KEY_PUSH_DATA);

        if (!TextUtils.isEmpty(jsonString)) {

            // gson deserializer
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).create();

            // convert the string into a PushNotification object
            final PushInfo push = gson.fromJson(jsonString, PushInfo.class);

            // intent opens to main activity
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

            // vibrate pattern 500 ms, pause 50ms, vibrate 500ms
            long[] pattern = {0, 500, 50, 500};

            Bitmap largeLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_black);

            // build the notification
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentTitle("SpartaHack")
                    .setContentText(push.alert)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(largeLogo)
                    .setVibrate(pattern);

            // show notificaiton in notificaiton bar
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());

        }
    }

    private class PushInfo {
        public String alert;
    }
}
