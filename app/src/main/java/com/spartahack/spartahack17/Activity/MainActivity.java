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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.spartahack.spartahack17.Constants;
import com.spartahack.spartahack17.Fragment.AnnouncementFragment;
import com.spartahack.spartahack17.Fragment.AwardsFragment;
import com.spartahack.spartahack17.Fragment.CheckInFragment;
import com.spartahack.spartahack17.Fragment.GuideFragment;
import com.spartahack.spartahack17.Fragment.ProfileFragment;
import com.spartahack.spartahack17.Model.Ticket;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.ParseAPIService;
import com.spartahack.spartahack17.Utility;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    private String title = "Notifications";
    private static final String TAG = "MainActivity";

    public static final String ACTION = "action";
    public static final String OBJECT_ID = "objectid";
    public static final String NOT_ID = "notid";

    private FirebaseAnalytics firebaseAnalytics;

    /**
     * Reference to the currently selected menu item in the nav drawer
     */
    private MenuItem currentItem;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerEventBus = true;

        setSupportActionBar(toolbar);

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);

        Log.d(TAG, "onCreate: " + FirebaseInstanceId.getInstance().getToken());

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // add padding for transparent statusbar if > kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolbar != null) toolbar.setPadding(0, Utility.getStatusBarHeight(this), 0, 0);
        }

        // set navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener(this);

        // inflate the nav drawer items programmatically because it is dynamic based on roles
        navigationView.inflateMenu(R.menu.drawer_nav_items);

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.accent));

        if (getIntent() == null || getIntent().getExtras() == null) {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        } else{
            onNavigationItemSelected(navigationView.getMenu().getItem(2));
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_notifications:
                                title = getResources().getString(R.string.notifications);
                                addFragment(new AnnouncementFragment());
                                break;
                            case R.id.action_schedule:
                                title = getResources().getString(R.string.guide);
                                addFragment(new GuideFragment());
                                break;
                            case R.id.action_awards:
                                title = getResources().getString(R.string.awards);
                                addFragment(new AwardsFragment());
                                break;
                            case R.id.action_help:
//                                title = getResources().getString(R.string.help);
//                                addFragment(new CheckInFragment());
                                break;
                            case R.id.action_profile:
                                title = getResources().getString(R.string.profile);
                                addFragment(new ProfileFragment());
                                break;
                        }

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(title);
        drawerLayout.closeDrawers();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (currentItem == null || currentItem != item) {
            item.setChecked(true);

            switch (item.getItemId()) {
                case R.id.awards:
                    title = getResources().getString(R.string.awards);
                    addFragment(new AwardsFragment());
                    break;

                case R.id.scan:
                    title = getResources().getString(R.string.scan);
                    addFragment(new CheckInFragment());
                    break;

                case R.id.help:
//                    title = getResources().getString(R.string.help);
//                    addFragment(new CheckInFragment());
                    break;

                case R.id.notifications:
                    title = getResources().getString(R.string.notifications);
                    addFragment(new AnnouncementFragment());
                    break;

                case R.id.schedule:
                    title = getResources().getString(R.string.guide);
                    addFragment(new GuideFragment());
                    break;

                case R.id.profile:
                    title = getResources().getString(R.string.profile);
                    addFragment(new ProfileFragment());
                    break;
            }

            currentItem = item;
        }

        drawerLayout.closeDrawers();

        return true;
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
        if (toolbar != null) toolbar.setTitle(title);
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

    public void onEvent(String url){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void onEvent(StartViewTicketActivity a){
        startActivity(ViewTicketActivity.getIntent(this, a.ticket, -1).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public static class StartViewTicketActivity {
        public StartViewTicketActivity(Ticket t) {this.ticket = t;}
        public final Ticket ticket;
    }

    public static class StartMentorViewTicketActivity {
        public StartMentorViewTicketActivity(Ticket t) {this.ticket = t;}
        public final Ticket ticket;
    }

    public void onEvent(StartMentorViewTicketActivity a){
        startActivity(MentorViewTicketActivity.getIntent(this, a.ticket).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void onEvent(Boolean isSubscribing) {
        getSharedPreferences(getApplication().getPackageName(), Activity.MODE_PRIVATE)
                .edit()
                .putBoolean(Constants.PREF_PUSH, isSubscribing)
                .apply();

        Snackbar.make(tabLayout, isSubscribing ? "Subscribed successfully" : "Unsubscribed successfully", Snackbar.LENGTH_SHORT).show();
    }

        public void refreshTicket(GSONMock.UpdateTicketStatusRequest request, final String confirmMessage, String id ) {
        ParseAPIService.INSTANCE.getRestAdapter()
                .updateTicketStatus(id, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.UpdateObj>() {
                    @Override public void onCompleted() { }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override public void onNext(GSONMock.UpdateObj updateObj) {
                        Toast.makeText(MainActivity.this, confirmMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
