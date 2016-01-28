package com.example.spartahack.spartahack2016.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.spartahack.spartahack2016.R;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment {

    /**
     * URL to reset a password
     */
    public static final String RESET_URL = "http://spartahack.com/forgot";

    public static final String I_EXTRA_FROM = "From help";

    @Bind(R.id.password) EditText passwordTextView;
    @Bind(R.id.user_name) EditText userNameTextView;
    @Bind(R.id.signedIn) View signedIn;
    @Bind(R.id.signedOut) View signedOut;
    @Bind(R.id.qr) ImageView qr;
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.progressBar) ProgressBar bar;
    @Bind(R.id.email_layout) TextInputLayout emailLayout;
    @Bind(R.id.password_layout) TextInputLayout passwordLayout;

    boolean fromHelp = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_login, container, false);

        ButterKnife.bind(this, view);

        Bundle args = this.getArguments();
        if (args != null && args.containsKey(I_EXTRA_FROM))
            fromHelp = true;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleViews(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard(passwordTextView);
    }

    /**
     * Called when the login button is pressed
     */
    @OnClick(R.id.login_button)
    public void onLogin() {

        // flag for if there are any errors
        boolean error = false;

        // validate email;
        String email = userNameTextView.getText().toString().trim().toLowerCase();
        if (!validateEmail(email)) {
            emailLayout.setError("Invalid Email");
            error = true;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        // validate password
        String password = passwordTextView.getText().toString().trim();
        if (!validatePassword(password)) {
            passwordLayout.setError("Password not long enough");
            error = true;
        } else {
            passwordLayout.setErrorEnabled(false);
        }

        // Don't submit call if errors
        if (error) return;

        hideKeyboard(passwordTextView);

        // change views shown
        toggleViews(true);

        ParseUser.logInInBackground(email, passwordTextView.getText().toString().trim(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Snackbar.make(bar, "Successfully logged in!", Snackbar.LENGTH_LONG).show();
                    updateParseInstillation(false);
                    if (fromHelp) {
                        getActivity().onBackPressed();
                    } else {
                        toggleViews(false);
                    }

                } else {
                    Snackbar.make(bar, "Invalid credentials", Snackbar.LENGTH_LONG).show();
                    Log.e("Login", e.toString());
                    e.printStackTrace();
                    toggleViews(false);
                }
            }

        });
    }

    /**
     * Called when a user clicks on forgot password. This will open the users browser of choice
     */
    @OnClick(R.id.forgot_passowrd)
    public void onForgotPassword() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(RESET_URL)));
    }

    /**
     * Called when logout is clicked. This will log out and show a snackbar
     * when the logout is done
     */
    @OnClick(R.id.logout)
    public void onLogout() {
        toggleViews(true);
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                updateParseInstillation(true);
                toggleViews(false);
                Snackbar.make(signedOut, "Successfully Logged Out", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Toggles which views are shown in the view. Either the loading circle if
     * something is loading, the logged in view or the logged out view
     *
     * @param load if the loading circle should show or not
     */
    private void toggleViews(boolean load) {
        ParseUser user = ParseUser.getCurrentUser();
        if (load) {
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
        } else if (user != null) {
            bar.setVisibility(View.GONE);
            signedIn.setVisibility(View.VISIBLE);
            signedOut.setVisibility(View.GONE);
            if (!(user.getParseFile("qrCode") == null || TextUtils.isEmpty(user.getParseFile("qrCode").getUrl())))
                Glide.with(this).load(user.getParseFile("qrCode").getUrl()).into(qr);
            displayName.setText(String.format(getActivity().getResources().getString(R.string.logged_in_as), user.get("username")));

        } else {
            bar.setVisibility(View.GONE);
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Makes sure email address is valid
     *
     * @param email a string which is the email address
     * @return if it is valid or not
     */
    private boolean validateEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * @param password string which is the password entered
     * @return if the password is long enough
     */
    private boolean validatePassword(String password) {
        return password.length() >= 4;
    }

    private void updateParseInstillation(boolean logout){
        ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();

        if (logout)
            currentInstall.remove("user");
        else
            currentInstall.put("user", ParseUser.getCurrentUser());

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
