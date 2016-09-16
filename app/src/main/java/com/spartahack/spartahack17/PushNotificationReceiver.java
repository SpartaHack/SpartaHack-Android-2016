package com.spartahack.spartahack17;

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

import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Activity.ViewTicketActivity;
import com.spartahack.spartahack17.Retrofit.GSONMock;
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
    @SuppressWarnings("FieldCanBeLocal")
    private static String TAG = "Push Receiver";


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.d(TAG, "Recieved");
        boolean notify = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE).getBoolean(MainActivity.PUSH_PREF, true);

        if (!notify) {
            Log.d(TAG, "onPushReceive: dont notify");
            return;
        }

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
            final GSONMock.PushInfo push = gson.fromJson(jsonString, GSONMock.PushInfo.class);

            int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

            // create pending intent to open the notificaiton fragment
            PendingIntent pIntent;

            if (push.alert.contains("ticket will expire in 10 minutes") || push.alert.contains("ticket has expired") || push.alert.contains("you can mentor in")){
                pIntent = PendingIntent.getActivity(context, uniqueInt, MainActivity.toHelpDesk(context), PendingIntent.FLAG_ONE_SHOT);
            } else {
                pIntent = PendingIntent.getActivity(context, uniqueInt, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
            }

            // intent opens to main activity

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

            // if there are actions add them to the notificaiton
            if ( push.action != null && !push.action.isEmpty()){
                builder.addAction(R.drawable.ic_add, push.action.get(0), ViewTicketActivity.getPendingIntent(context,uniqueInt, push.ticketId, ViewTicketActivity.EXTEND));
                builder.addAction(R.drawable.ic_delete, push.action.get(1), ViewTicketActivity.getPendingIntent(context, uniqueInt, push.ticketId, ViewTicketActivity.CLOSE));
            }

            // show notificaiton in notificaiton bar
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(uniqueInt, builder.build());
        }
    }
}
