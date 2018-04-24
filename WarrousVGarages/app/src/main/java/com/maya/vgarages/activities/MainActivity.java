package com.maya.vgarages.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.home.HomeFragment;
import com.maya.vgarages.fragments.home.LocationFragment;
import com.maya.vgarages.fragments.home.NotificationsFragment;
import com.maya.vgarages.fragments.home.RemainderFragment;
import com.maya.vgarages.fragments.home.SearchFragment;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.service.FetchAddressIntentService;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    private AddressResultReceiver mResultReceiver;

    int previous = R.id.navigation_home;

    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    Location location = null;

    View headerLayout;
    TextView tvUserName, tvAddress;
    ImageView imgUser;


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
        mResultReceiver = new AddressResultReceiver(new Handler());
        if(getIntent().getParcelableExtra("Location")!=null)
        {
            location = getIntent().getParcelableExtra("Location");
            startIntentService();
        }
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);

        headerLayout = navigationView.getHeaderView(0);
        tvAddress =  headerLayout.findViewById(R.id.tvAddress);
        tvUserName =  headerLayout.findViewById(R.id.tvUserName);
        imgUser =  headerLayout.findViewById(R.id.imgUser);

        updateFields();
    }

    private void startIntentService()
    {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private void updateFields()
    {
        tvUserName.setText(Utility.getCamelCase(Utility.getString(Utility.getSharedPreferences(),Constants.USER_NAME)));
        Picasso.with(activity())
                .load(Utility.getString(Utility.getSharedPreferences(),Constants.USER_PHOTO_URL))
                .into(imgUser);

        if(Utility.getSharedPreferences().contains(Constants.USER_ADDRESS))
        {
            tvAddress.setText(Utility.getCamelCase(Utility.getString(Utility.getSharedPreferences(),Constants.USER_ADDRESS)));
        }
        else
        {
            tvAddress.setText("");
        }
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
                    if(location!=null)
                    {
                        fragment = LocationFragment.newInstance(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                    else
                    {
                        fragment = LocationFragment.newInstance(null,null);
                    }
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



    private class AddressResultReceiver extends ResultReceiver
    {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData)
        {
            // Display the address string or an error message sent from the intent service.
            if (resultCode == Constants.SUCCESS_RESULT)
            {
                String address = resultData.getString(Constants.RESULT_DATA_KEY);
                Logger.d("ADDRESS",address);
                Utility.setString(Utility.getSharedPreferences(),Constants.USER_ADDRESS,address);
                updateFields();
            }
            else
            {

            }
        }

    }

}
