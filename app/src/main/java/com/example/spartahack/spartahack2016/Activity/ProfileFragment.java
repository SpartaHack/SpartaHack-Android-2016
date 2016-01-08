package com.example.spartahack.spartahack2016.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spartahack.spartahack2016.Fragment.BaseFragment;
import com.example.spartahack.spartahack2016.R;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment {

    public static final String RESET_URL = "http://spartahack.com/forgot";

    @Bind(R.id.password) EditText passwordTextView;
    @Bind(R.id.user_name) EditText userNameTextView;
    @Bind(R.id.signedIn) View signedIn;
    @Bind(R.id.signedOut) View signedOut;
    @Bind(R.id.qr) ImageView qr;
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.progressBar) ProgressBar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_login, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleViews(false);
    }

    /**
     * Called when the login button is pressed
     */
    @OnClick(R.id.login_button)
    public void onLogin(){
        toggleViews(true);
        ParseUser.logInInBackground(userNameTextView.getText().toString().trim().toLowerCase(), passwordTextView.getText().toString().trim(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    toggleViews(false);

                } else {
                    // invalid email or password
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND){
                        Toast.makeText(getActivity(), "Wrong username or email", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Login", e.toString());
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * Called when a user clicks on forgot password. This will open the users browser of choice
     */
    @OnClick(R.id.forgot_passowrd)
    public void onForgotPassword(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(RESET_URL)));
    }

    @OnClick(R.id.logout)
    public void onLogout(){
        toggleViews(true);
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                toggleViews(false);
                Snackbar.make(signedOut, "Successfully Logged Out", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleViews(boolean load){
        if (load){
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
        }
        else if (ParseUser.getCurrentUser() != null){
            bar.setVisibility(View.GONE);
            signedIn.setVisibility(View.VISIBLE);
            signedOut.setVisibility(View.GONE);
            Glide.with(this).load(ParseUser.getCurrentUser().getParseFile("qrCode").getUrl()).into(qr);
            displayName.setText(String.format(getActivity().getResources().getString(R.string.logged_in_as), ParseUser.getCurrentUser().get("username")));

        } else {
            bar.setVisibility(View.GONE);
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.VISIBLE);
        }
    }

}
