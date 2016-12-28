package com.spartahack.spartahack17;

import android.content.Context;
import android.content.SharedPreferences;

import com.spartahack.spartahack17.Model.Session;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ryan on 11/5/15
 * SpartaHack2016-Android
 */
public class Cache {

    /**
     * Singleton instance of the cache
     */
    public static final Cache INSTANCE = new Cache();

    // all variables for shared prefs
    private static final String P_USER_ID = "user id";
    private static final String P_USER_AUTH_TOKEN = "auth token";
    private static final String P_USER_EMAIL = "email";
    private static final String P_USER_ROLE = "role";
    private static final String MY_PREFS_NAME = "com.spartahack.spartahack17";

    /**
     * Constructor is private for singleton pattern
     */
    private Cache() {}

    private Session session;

    public Session getSession() {
        return session;
    }

    /**
     * Set the session object in the cache here and in shared prefs
     * @param session the Session object to save in the cache and shared prefs
     * @param context a Context reference to load shared prefs from
     */
    public void setSession(Session session, Context context) {
        this.session = session;
        writeToSharedPrefs(context);
    }

    /**
     * Clear the session from the cache and remove from shared preferences
     * @param context a Context reference to load shared prefs from
     */
    public void clear(Context context){
        session = null;
        context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
    }

    /**
     * Check if a session exists
     * @return if the session exists or not
     */
    public boolean hasSession() {
        return session != null;
    }

    /**
     * Write all the session values from the cache to shared preferences
     * @param context a Context reference to load shared prefs from
     * @return if the write was successful or not
     */
    public boolean writeToSharedPrefs(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(P_USER_ID, session.getId());
        editor.putString(P_USER_EMAIL, session.getEmail());
        editor.putString(P_USER_AUTH_TOKEN, session.getAuth_token());
        editor.putInt(P_USER_ROLE, session.getRole());
        return editor.commit();
    }

    /**
     * Read all the session values from the shared preferences to the cache
     * @param context a Context reference to load shared prefs from
     * @return if the read was successful or not
     */
    public boolean readFromSharedPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int id = prefs.getInt(P_USER_ID, -100);
        String email = prefs.getString(P_USER_EMAIL, null);
        String auth = prefs.getString(P_USER_AUTH_TOKEN, null);
        int role = prefs.getInt(P_USER_ROLE, -100);

        if (id != -100 && email != null && auth != null && role!= -100){
            session = new Session(id, email, auth, role);
            return true;
        }

        return false;
    }
}
