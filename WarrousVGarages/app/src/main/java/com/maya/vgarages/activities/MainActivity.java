package com.maya.vgarages.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.home.HomeFragment;
import com.maya.vgarages.fragments.home.LocationFragment;
import com.maya.vgarages.fragments.home.NotificationsFragment;
import com.maya.vgarages.fragments.home.RemainderFragment;
import com.maya.vgarages.fragments.home.SearchFragment;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IFragment {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    int previous = R.id.navigation_home;

    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialize();
        setUpFragment();
    }

    private void setUpFragment()
    {
        changeTitle("Garages List");
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, HomeFragment.newInstance(null,null)).commit();
    }

    private void initialize()
    {
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);

    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,drawer,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }



    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            item.setChecked(true);
            navigation.getMenu().findItem(R.id.navigation_home).setIcon(ContextCompat.getDrawable(activity(),item.getItemId()==R.id.navigation_home?R.drawable.home_fill:R.drawable.home));
            navigation.getMenu().findItem(R.id.navigation_search).setIcon(ContextCompat.getDrawable(activity(),item.getItemId()==R.id.navigation_search?R.drawable.search_fill:R.drawable.search));
            navigation.getMenu().findItem(R.id.navigation_notifications).setIcon(ContextCompat.getDrawable(activity(),item.getItemId()==R.id.navigation_notifications?R.drawable.notifications_fill:R.drawable.notifications));
            navigation.getMenu().findItem(R.id.navigation_remainder).setIcon(ContextCompat.getDrawable(activity(),item.getItemId()==R.id.navigation_remainder?R.drawable.pin_fill:R.drawable.pin));
            Fragment fragment = null;
            if(previous == item.getItemId())
            {
                return false;
            }
            previous = item.getItemId();

            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    toolbar.setVisibility(View.VISIBLE);
                    changeTitle("Garages List");
                    fragment = HomeFragment.newInstance(null,null);
                    break;
                case R.id.navigation_search:
                    changeTitle("Search");
                    toolbar.setVisibility(View.GONE);
                    fragment = SearchFragment.newInstance(null,null);
                    break;

                case R.id.navigation_location:
                    toolbar.setVisibility(View.VISIBLE);
                    changeTitle("Geo Search");
                    fragment = LocationFragment.newInstance(null,null);
                    break;

                case R.id.navigation_notifications:
                    toolbar.setVisibility(View.VISIBLE);
                    fragment = NotificationsFragment.newInstance(null,null);
                    changeTitle("Notifications");
                    break;
                case R.id.navigation_remainder:
                    toolbar.setVisibility(View.VISIBLE);
                    changeTitle("Bookmarks");
                    fragment = RemainderFragment.newInstance(null,null);
                    break;
            }
            if(fragment!=null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_profile:
                goToProfile();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void goToProfile()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,1111);
        startActivity(intent);
    }

    @Override
    public void changeTitle(String title)
    {
        tvTitle.setText(title);
    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return this;
    }
}
