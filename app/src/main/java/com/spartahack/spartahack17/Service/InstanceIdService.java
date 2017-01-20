package com.spartahack.spartahack17.Service;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.spartahack.spartahack17.Constants;
import com.spartahack.spartahack17.Model.AddInstillationRequest;
import com.spartahack.spartahack17.Model.AddInstillationResponse;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ryancasler on 12/28/16
 * Spartahack-Android
 */
public class InstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private static final String TAG = "InstanceIdService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        SpartaHackAPIService.INSTANCE.getRestAdapter()
                .addInstillation(new AddInstillationRequest(token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddInstillationResponse>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {}

                    @Override public void onNext(AddInstillationResponse addInstillationResponse) {
                        SharedPreferences preferences = getSharedPreferences(getApplication().getPackageName(), Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constants.PREF_INSTALL_TOKEN, addInstillationResponse.token);
                        editor.putInt(Constants.PREF_INSTALL_ID, addInstillationResponse.id);
                        editor.apply();
                    }
                });
    }
}
