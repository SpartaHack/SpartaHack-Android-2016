package com.spartahack.spartahack17.Activity;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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

    private String title = "Notifications";
    private static final String TAG = "MainActivity";

    public static final String ACTION = "action";
    public static final String OBJECT_ID = "objectid";
    public static final String NOT_ID = "notid";

    /**
     * Reference to the currently selected menu item in the nav drawer
     */

    public static PendingIntent getPendingIntent(Context context, String ticketId, String action, int pushID){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ACTION, action);
        intent.putExtra(OBJECT_ID, ticketId);
        intent.putExtra(NOT_ID, pushID);

        return PendingIntent.getActivity(context, pushID, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    public static Intent toHelpDesk(Context c){
        return new Intent(c, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(ACTION, ACTION);
    }

    @OnClick(R.id.navigate_before_button) public void onNavigateBeforeButtonClicked() {
        navigateBeforeButton.setVisibility(View.GONE);
        settingsIcon.setVisibility(View.VISIBLE);
        title = getResources().getString(R.string.profile);
        addFragment(new ProfileFragment());
    }

    @OnClick(R.id.settings_icon) public void onSettingsIconClicked() {
        navigateBeforeButton.setVisibility(View.VISIBLE);
        settingsIcon.setVisibility(View.GONE);
        title = getResources().getString(R.string.settings);
        addFragment(new SettingsFragment());
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

        addFragment(new AnnouncementFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_notifications:
                            hideToolbarItems();
                            title = getResources().getString(R.string.notifications);
                            addFragment(new AnnouncementFragment());
                            break;
                        case R.id.action_schedule:
                            hideToolbarItems();
                            title = getResources().getString(R.string.guide);
                            addFragment(new GuideFragment());
                            break;
                        case R.id.action_awards:
                            hideToolbarItems();
                            title = getResources().getString(R.string.awards);
                            addFragment(new AwardsFragment());
                            break;
                        case R.id.action_help:
                            hideToolbarItems();
                            title = getResources().getString(R.string.help);
                            addFragment(new HelpDeskFragment());
                            break;
                        case R.id.action_profile:
                            navigateBeforeButton.setVisibility(View.GONE);
                            settingsIcon.setVisibility(View.VISIBLE);
                            title = getResources().getString(R.string.profile);
                            addFragment(new ProfileFragment());
                            break;
                    }

                    return true;
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTitle.setText(title);
    }

    /**
     * Replaces the current fragment in the frame layout
     *
     * @param fragment to replace the current fragment
     */
    public void addFragment(android.app.Fragment fragment) {
        // use fragment transaction and add the fragment to the container
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    private void hideToolbarItems() {
        navigateBeforeButton.setVisibility(View.GONE);
        settingsIcon.setVisibility(View.GONE);
    }

    public void onEvent(String url){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void onEvent(Boolean isSubscribing) {
        getSharedPreferences(getApplication().getPackageName(), Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(Constants.PREF_PUSH, isSubscribing)
                .apply();

        Snackbar.make(tabLayout, isSubscribing ? "Subscribed successfully" : "Unsubscribed successfully", Snackbar.LENGTH_SHORT).show();
    }
}
