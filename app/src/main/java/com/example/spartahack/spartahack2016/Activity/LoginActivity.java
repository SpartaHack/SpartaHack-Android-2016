package com.example.spartahack.spartahack2016.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.spartahack.spartahack2016.Cache;
import com.example.spartahack.spartahack2016.Keys;
import com.example.spartahack.spartahack2016.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.password) EditText passwordTextView;
    @Bind(R.id.user_name) EditText userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    /**
     * Called when the login button is pressed
     */
    @OnClick(R.id.login_button)
    public void onLogin(){
        ParseUser.logInInBackground(userNameTextView.getText().toString(), passwordTextView.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

                    // set temp cache with users info
                    if (user.get("role") != null) Cache.INSTANCE.setRole((String) user.get("role"));
                    if (user.get("qrCode") != null)
                        Cache.INSTANCE.setQrURL(user.getParseFile("qrCode").getUrl());
                    if (user.get("username") != null)
                        Cache.INSTANCE.setUserName((String) user.get("username"));

                    // go back to main activity
                    onBackPressed();

                } else {
                    Log.e("Login", e.toString());
                }
            }
        });
    }

    /**
     * Called when a user clicks on forgot password. This will open the users browser of choice
     */
    @OnClick(R.id.forgot_passowrd)
    public void onForgotPassword(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Keys.RESET_URL)));
    }

}
