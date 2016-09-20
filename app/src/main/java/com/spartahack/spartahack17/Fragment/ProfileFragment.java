package com.spartahack.spartahack17.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Model.Session;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ProfileFragment extends BaseFragment implements Switch.OnCheckedChangeListener {

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
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.email_layout) TextInputLayout emailLayout;
    @Bind(R.id.password_layout) TextInputLayout passwordLayout;
    @Bind(R.id.login_page_title) TextView loginViewTitle;
    @Bind(R.id.push_switch) Switch aSwitch;
    @Bind(R.id.push_switch2) Switch aSwitch2;


    boolean fromHelp = false;

    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_login, container, false);

        ButterKnife.bind(this, view);

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
        return view;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        if (isChecked){
            Toast.makeText(getActivity(), "Subscribed successfully", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(true);
        } else {
            Toast.makeText(getActivity(), "Unsubscribed successfully", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(false);
        }
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
        String email = userNameTextView.getText().toString().trim().toLowerCase(Locale.US);
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

        // show loading
        toggleViews(true);

        hideKeyboard(passwordTextView);

        GSONMock.Login login = new GSONMock.Login();
        login.email =  email;
        login.password =  passwordTextView.getText().toString().trim();

        SpartaHackAPIService.INSTANCE.getRestAdapter()
                .login(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(session -> {

                    if (session != null) {
                        this.session = session;
                        Snackbar.make(progressBar, "Successfully logged in!", Snackbar.LENGTH_LONG).show();

                        // go back to the help screen
                        if (fromHelp) getActivity().onBackPressed();
                        // show the content
                        else toggleViews(false);

                    } else {
                        Snackbar.make(progressBar, "Invalid credentials", Snackbar.LENGTH_LONG).show();
                        toggleViews(false);
                    }

                }, throwable -> {
                    Snackbar.make(progressBar, "Invalid credentials", Snackbar.LENGTH_LONG).show();
                    Log.e("Login", throwable.toString());
                    toggleViews(false);
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
        SpartaHackAPIService.INSTANCE.getRestAdapter()
                .logout(session.getAuth_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(session1 -> {
                    Snackbar.make(signedOut, "Successfully Logged Out", Snackbar.LENGTH_LONG).show();
                    toggleViews(false);
                }, throwable -> {});

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
                bitmap = encodeAsBitmap(String.valueOf(session.getId()), BarcodeFormat.CODE_128, 600, 300);
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

    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     *
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        if (contents == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}
