package com.spartahack.spartahack17.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Constants;
import com.spartahack.spartahack17.R;

/**
 * Created by ryancasler on 12/28/16
 * Spartahack-Android
 */
public class MessageService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int NOTIFICATION_ID = 123321;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification());
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * @param message the message to be shown in the notification
     */
    private void sendNotification(RemoteMessage.Notification message) {

        // if push is turned off don't show the notification
        SharedPreferences prefs = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        if (!prefs.getBoolean(Constants.PREF_PUSH, true)){
            return;
        }

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap largeLogo = BitmapFactory.decodeResource(getResources(), R.drawable.launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.push)
                .setLargeIcon(largeLogo)
                .setContentInfo("SpartaHack")
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
