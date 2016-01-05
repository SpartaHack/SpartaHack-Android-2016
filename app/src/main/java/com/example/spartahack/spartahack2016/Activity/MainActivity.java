package com.example.spartahack.spartahack2016.Activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.spartahack.spartahack2016.Cache;
import com.example.spartahack.spartahack2016.Fragment.AwardsFragment;
import com.example.spartahack.spartahack2016.Fragment.HelpFragment;
import com.example.spartahack.spartahack2016.Fragment.MentorFragment;
import com.example.spartahack.spartahack2016.Fragment.NotificationFragment;
import com.example.spartahack.spartahack2016.Fragment.ScheduleFragment;
import com.example.spartahack.spartahack2016.Fragment.SettingsFragment;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Utility;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view) NavigationView navigationView;
    @Bind(R.id.tab_layout) TabLayout tabLayout;

    private View headerView;
    private String title = "Notifications";

    /**
     * Reference to the currently selected menu item in the nav drawer
     */
    private MenuItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerEventBus = true;

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_green);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // inflate header view manually b/c no get headerview yet
        headerView = navigationView.inflateHeaderView(R.layout.nav_drawer_header);

        // add padding for transparent statusbar if > kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolbar != null) toolbar.setPadding(0, Utility.getStatusBarHeight(this), 0, 0);
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(LoginActivity.class);
            }
        });

        // set navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener(this);

        // inflate the nav drawer items programmatically because it is dynamic based on roles
        navigationView.inflateMenu(R.menu.nav_drawer_items);

        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));

        // opening fragment is notificaions
        addFragment(new NotificationFragment());
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

        // set nav drawer header view
        if (!TextUtils.isEmpty(Cache.INSTANCE.getQrURL())) {
            Glide.with(this)
                    .load(Cache.INSTANCE.getQrURL())
                    .into((ImageView) headerView.findViewById(R.id.header_image));
            // add padding for transparent statusbar if > kitkat
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (headerView != null) headerView.setPadding(0, Utility.getStatusBarHeight(this), 0, 0);
            }
        } else {
            ((ImageView) headerView.findViewById(R.id.header_image)).setImageResource(R.drawable.banner);
            // remove padding for transparent statusbar if > kitkat
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (headerView != null) headerView.setPadding(0, 0, 0, 0);
            }
        }

        toolbar.setTitle(title);
        drawerLayout.closeDrawers();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (currentItem == null || currentItem != item) {
            item.setChecked(true);

            switch (item.getItemId()) {
                case R.id.awards:
                    title = getResources().getString(R.string.awards);
                    addFragment(new AwardsFragment());
                    break;
                case R.id.help:
                    title = getResources().getString(R.string.help);
                    addFragment(new HelpFragment());
                    break;
                case R.id.notifications:
                    title = getResources().getString(R.string.notifications);
                    addFragment(new NotificationFragment());
                    break;
                case R.id.mentor:
                    title = getResources().getString(R.string.mentor);
                    addFragment(new MentorFragment());
                    break;
                case R.id.settings:
                    title = getResources().getString(R.string.settings);
                    addFragment(new SettingsFragment());
                    break;
                case R.id.schedule:
                    title = getResources().getString(R.string.schedule);
                    addFragment(new ScheduleFragment());
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
    private void addFragment(android.app.Fragment fragment) {
        // use fragment transaction and add the fragment to the container
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
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
        ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        ft.commit();
    }
}
