package com.spartahack.spartahack17.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Model.Session;
import com.spartahack.spartahack17.Presenter.ProfilePresenter;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Utility;
import com.spartahack.spartahack17.View.ProfileView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class ProfileFragment extends MVPFragment<ProfileView, ProfilePresenter>
        implements Switch.OnCheckedChangeListener, ProfileView{

    private static final String TAG = "ProfileFragment";

    /**
     * URL to reset a password
     */
    public static final String RESET_URL = "http://spartahack.com/forgot";

    public static final String I_EXTRA_FROM = "From help";

    @BindView(R.id.password) EditText passwordTextView;
    @BindView(R.id.user_name) EditText userNameTextView;
    @BindView(R.id.signedIn) View signedIn;
    @BindView(R.id.signedOut) View signedOut;
    @BindView(R.id.qr) ImageView qr;
    @BindView(R.id.display_name) TextView displayName;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.email_layout) TextInputLayout emailLayout;
    @BindView(R.id.password_layout) TextInputLayout passwordLayout;
    @BindView(R.id.login_page_title) TextView loginViewTitle;
    @BindView(R.id.push_switch) Switch aSwitch;
    @BindView(R.id.push_switch2) Switch aSwitch2;

    boolean fromHelp = false;
    private Session session;

    @Override int getLayout() {
        return R.layout.activity_login;
    }

    @Override boolean registerEventbus() {
        return false;
    }

    @NonNull @Override public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = this.getArguments();
        if (args != null && args.containsKey(I_EXTRA_FROM)){
            fromHelp = true;
            loginViewTitle.setText(R.string.login_for_help);
        }

        // set switch to correct value
        aSwitch.setChecked(getActivity().getSharedPreferences(getActivity().getApplication().getPackageName(), Activity.MODE_PRIVATE).getBoolean(MainActivity.PUSH_PREF, true));
        aSwitch2.setChecked(getActivity().getSharedPreferences(getActivity().getApplication().getPackageName(), Activity.MODE_PRIVATE).getBoolean(MainActivity.PUSH_PREF, true));

        aSwitch.setOnCheckedChangeListener(this);
        aSwitch2.setOnCheckedChangeListener(this);
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        if (isChecked){
            Toast.makeText(getActivity(), "Subscribed successfully", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(true);
        } else {
            Toast.makeText(getActivity(), "Unsubscribed successfully", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(false);
        }
    }

    @Override public void onResume() {
        super.onResume();
        toggleViews(false);
    }

    @Override public void onPause() {
        super.onPause();
        hideKeyboard(passwordTextView);
    }

    /**
     * Called when the login button is pressed
     */
    @OnClick(R.id.login_button) public void onLogin() {

        // flag for if there are any errors
        boolean error = false;

        // validate email;
        String email = userNameTextView.getText().toString().trim().toLowerCase(Locale.US);
        if (!Utility.isValidEmail(email)) {
            emailLayout.setError("Invalid Email");
            error = true;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        // validate password
        String password = passwordTextView.getText().toString().trim();
        if (!Utility.isPasswordValid(password)) {
            passwordLayout.setError("Password not long enough");
            error = true;
        } else {
            passwordLayout.setErrorEnabled(false);
        }

        // Don't submit call if errors
        if (error) return;

        // show loading
        toggleViews(true);

        hideKeyboard(passwordTextView);

        getMVPPresenter().attemptLogin(email, passwordTextView.getText().toString().trim());
    }

    /**
     * Called when a user clicks on forgot password. This will open the users browser of choice
     */
    @OnClick(R.id.forgot_passowrd) public void onForgotPassword() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(RESET_URL)));
    }

    /**
     * Called when logout is clicked. This will log out and show a snackbar
     * when the logout is done
     */
    @OnClick(R.id.logout) public void onLogout() {
        getMVPPresenter().logOut(session.getAuth_token());
    }

    /**
     * Toggles which views are shown in the view. Either the loading circle if
     * something is loading, the logged in view or the logged out view
     *
     * @param load if the loading circle should show or not
     */
    private void toggleViews(boolean load) {
        if (load) {
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else if (session != null) {
            progressBar.setVisibility(View.GONE);
            signedIn.setVisibility(View.VISIBLE);
            signedOut.setVisibility(View.GONE);

            // barcode image
            Bitmap bitmap;

            try {
              bitmap = Utility.encodeAsBitmap(String.valueOf(session.getId()), BarcodeFormat.QR_CODE, 250, 250);
                qr.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }

            displayName.setText(String.format(getActivity().getResources().getString(R.string.logged_in_as), session.getEmail()));

        } else {
            progressBar.setVisibility(View.GONE);
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.VISIBLE);
        }
    }

    @Override public void logOutSuccess() {
        Snackbar.make(signedOut, "Successfully Logged Out", Snackbar.LENGTH_LONG).show();
        session = null;
        toggleViews(false);
    }

    @Override public void logOutError() {
        Snackbar.make(signedOut, "Error Logging Out", Snackbar.LENGTH_LONG).show();
        toggleViews(false);
    }

    @Override public void loginSuccess(Session data) {
        this.session = data;
        Snackbar.make(progressBar, "Successfully logged in!", Snackbar.LENGTH_LONG).show();

        // go back to the help screen
        if (fromHelp) getActivity().onBackPressed();
            // show the content
        else toggleViews(false);
    }

    @Override public void showLoading() {
        toggleViews(true);
    }

    @Override public void onError(String error) {
        Snackbar.make(progressBar, "Invalid credentials", Snackbar.LENGTH_LONG).show();
        Log.e("Login", error);
        toggleViews(false);
    }

}
