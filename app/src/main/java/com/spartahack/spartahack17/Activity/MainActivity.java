package com.spartahack.spartahack17.Activity;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.spartahack.spartahack17.Constants;
import com.spartahack.spartahack17.Fragment.AnnouncementFragment;
import com.spartahack.spartahack17.Fragment.AwardsFragment;
import com.spartahack.spartahack17.Fragment.GuideFragment;
import com.spartahack.spartahack17.Fragment.HelpDeskFragment;
import com.spartahack.spartahack17.Fragment.ProfileFragment;
import com.spartahack.spartahack17.Fragment.SettingsFragment;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Utility;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.navigate_before_button) ImageButton navigateBeforeButton;
    @BindView(R.id.settings_icon) ImageButton settingsIcon;

    private static final String TAG = "MainActivity";

    public static final String ACTION = "action";
    public static final String OBJECT_ID = "objectid";
    public static final String NOT_ID = "notid";

    private String title;
    private State currentState;

    private enum State {
        ANNOUNCEMENTS,
        GUIDE,
        AWARDS,
        HELP,
        PROFILE,
        SETTINGS
    }

    public static PendingIntent getPendingIntent(Context context, String ticketId, String action, int pushID) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ACTION, action);
        intent.putExtra(OBJECT_ID, ticketId);
        intent.putExtra(NOT_ID, pushID);

        return PendingIntent.getActivity(context, pushID, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    public static Intent toHelpDesk(Context c) {
        return new Intent(c, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(ACTION, ACTION);
    }

    @OnClick(R.id.navigate_before_button)
    public void onNavigateBeforeButtonClicked() {
        currentState = State.PROFILE;
        changeState();
    }

    @OnClick(R.id.settings_icon)
    public void onSettingsIconClicked() {
        currentState = State.SETTINGS;
        changeState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerEventBus = true;

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Log.d(TAG, "onCreate: " + FirebaseInstanceId.getInstance().getToken());

        // add padding for transparent statusbar if > kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolbar != null) toolbar.setPadding(0, Utility.getStatusBarHeight(this), 0, 0);
        }

        currentState = State.ANNOUNCEMENTS;
        changeState();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_notifications:
                            currentState = State.ANNOUNCEMENTS;
                            break;
                        case R.id.action_schedule:
                            currentState = State.GUIDE;
                            break;
                        case R.id.action_awards:
                            currentState = State.AWARDS;
                            break;
                        case R.id.action_help:
                            currentState = State.HELP;
                            break;
                        case R.id.action_profile:
                            currentState = State.PROFILE;
                            break;
                    }
                    changeState();

                    return true;
                });
    }

    private void changeState() {
        Menu bottomNavigationViewMenu = bottomNavigationView.getMenu();

        switch (currentState) {
            case ANNOUNCEMENTS:
                hideToolbarItems();
                title = getResources().getString(R.string.notifications);
                addFragment(new AnnouncementFragment());
                bottomNavigationViewMenu.findItem(R.id.action_notifications).setChecked(true);
                break;
            case GUIDE:
                hideToolbarItems();
                title = getResources().getString(R.string.guide);
                addFragment(new GuideFragment());
                bottomNavigationViewMenu.findItem(R.id.action_schedule).setChecked(true);
                break;
            case AWARDS:
                hideToolbarItems();
                title = getResources().getString(R.string.awards);
                addFragment(new AwardsFragment());
                bottomNavigationViewMenu.findItem(R.id.action_awards).setChecked(true);
                break;
            case HELP:
                hideToolbarItems();
                title = getResources().getString(R.string.help);
                addFragment(new HelpDeskFragment());
                bottomNavigationViewMenu.findItem(R.id.action_help).setChecked(true);
                break;
            case PROFILE:
                navigateBeforeButton.setVisibility(View.GONE);
                settingsIcon.setVisibility(View.VISIBLE);
                title = getResources().getString(R.string.profile);
                addFragment(new ProfileFragment());
                bottomNavigationViewMenu.findItem(R.id.action_profile).setChecked(true);
                break;
            case SETTINGS:
                navigateBeforeButton.setVisibility(View.VISIBLE);
                settingsIcon.setVisibility(View.GONE);
                title = getResources().getString(R.string.settings);
                addFragment(new SettingsFragment());
                bottomNavigationViewMenu.findItem(R.id.action_profile).setChecked(true);
                break;
        }
    }

    /**
     * Replaces the current fragment in the frame layout
     *
     * @param fragment to replace the current fragment
     */
    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
        toolbarTitle.setText(title);
        tabLayout.setVisibility(View.GONE);
    }

    /**
     * Eventbus reciever for fragments passing a view pager for the tab bar
     *
     * @param pager to set the tab bar up with
     */
    public void onEvent(ViewPager pager) {
        if (pager != null) {
            tabLayout.setupWithViewPager(pager);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, fragment, fragment.toString())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        switch (currentState) {
            case ANNOUNCEMENTS:
                super.onBackPressed();
                break;
            case SETTINGS:
                currentState = State.PROFILE;
                changeState();
                break;
            default:
                currentState = State.ANNOUNCEMENTS;
                changeState();
                break;
        }
    }

    private void hideToolbarItems() {
        navigateBeforeButton.setVisibility(View.GONE);
        settingsIcon.setVisibility(View.GONE);
    }

    public void onEvent(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void onEvent(Boolean isSubscribing) {
        getSharedPreferences(getApplication().getPackageName(), Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(Constants.PREF_PUSH, isSubscribing)
                .apply();

        Snackbar.make(findViewById(R.id.placeSnackBar), isSubscribing ? "Subscribed successfully" : "Unsubscribed successfully", Snackbar.LENGTH_SHORT).show();
    }
}
