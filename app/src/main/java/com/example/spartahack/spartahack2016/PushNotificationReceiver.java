package com.example.spartahack.spartahack2016;

import android.app.Activity;
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
import com.example.spartahack.spartahack2016.Model.PushNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by ryancasler on 12/22/15.
 * Custom push notification receiver for parse push notifications
 */
public class PushNotificationReceiver extends ParsePushBroadcastReceiver {

    private static String TAG = "Push Receiver";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.d(TAG, "Recieved");

        // get the json string form the push
        String jsonString = intent.getStringExtra(KEY_PUSH_DATA);

        if (!TextUtils.isEmpty(jsonString)) {

            // convert the string into a PushNotification object
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            PushNotification push = gson.fromJson(jsonString, PushNotification.class);

            switch (push.type){
                case 0: // play sound and show in bar

                    // intent opens to main activity
                    PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

                    // vibrate pattern 500 ms, pause 50ms, vibrate 500ms
                    long[] pattern = {0, 500,50,500};

                    Bitmap largeLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_black);

                    // build the notification
                    Notification.Builder builder = new Notification.Builder(context)
                            .setSmallIcon(R.drawable.ic_notif)
                            .setContentTitle("SpartaHack")
                            .setContentIntent(pIntent)
                            .setAutoCancel(true)
                            .setLargeIcon(largeLogo)
                            .setStyle(new Notification.BigTextStyle().bigText(push.message))
                            .setVibrate(pattern);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());
                    break;

                case 1: // silent push to just show in notificaitons

                    break;

                case 2: // update of previous push

                    break;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return super.getSmallIconId(context, intent);
    }

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        return super.getLargeIcon(context, intent);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        return super.getNotification(context, intent);
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }
}
