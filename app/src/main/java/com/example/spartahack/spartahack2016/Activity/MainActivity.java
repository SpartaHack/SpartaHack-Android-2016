package com.example.spartahack.spartahack2016.Activity;


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
import android.view.MenuItem;
import android.view.View;

import com.example.spartahack.spartahack2016.Fragment.AwardsFragment;
import com.example.spartahack.spartahack2016.Fragment.HelpFragment;
import com.example.spartahack.spartahack2016.Fragment.MentorFragment;
import com.example.spartahack.spartahack2016.Fragment.NotificationFragment;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Utility;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view) NavigationView navigationView;
    @Bind(R.id.tab_layout) TabLayout tabLayout;

    private View headerView;

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

        if (actionBar!=null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // add padding for transparent statusbar if > kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolbar != null) toolbar.setPadding(0, Utility.getStatusBarHeight(this),0,0);
        }

        // inflate header view manually b/c no get headerview yet
        headerView = navigationView.inflateHeaderView(R.layout.nav_drawer_header);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ProfileActivity.class);
            }
        });

        // set navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener(this);

        // inflate the nav drawer items programmatically because it is dynamic based on roles
        navigationView.inflateMenu(R.menu.nav_drawer_items);

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
    public boolean onNavigationItemSelected(MenuItem item) {

        if (currentItem == null || currentItem != item) {
            item.setChecked(true);

            switch (item.getItemId()) {
                case R.id.awards:
                    addFragment(new AwardsFragment());
                    break;
                case R.id.help:
                    addFragment(new HelpFragment());
                    break;
                case R.id.notifications:
                    addFragment(new NotificationFragment());
                    break;
                case R.id.mentor:
                    addFragment(new MentorFragment());
                    break;
            }

            currentItem = item;
        }

        drawerLayout.closeDrawers();

        return true;
    }

    /**
     * Replaces the current fragment in the frame layout
     * @param fragment to replace the current fragment
     */
    private void addFragment(android.app.Fragment fragment) {
        // use fragment transaction and add the fragment to the container
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        fragmentTransaction.commit();
        tabLayout.setVisibility(View.GONE);
    }

    /**
     * Eventbus reciever for fragments passing a view pager for the tab bar
     * @param pager to set the tab bar up with
     */
    public void onEvent(ViewPager pager){
        if (pager != null){
            tabLayout.setupWithViewPager(pager);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }
}
