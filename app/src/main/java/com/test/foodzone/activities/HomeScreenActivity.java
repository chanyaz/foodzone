package com.test.foodzone.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.test.foodzone.R;
import com.test.foodzone.fragments.home.profile.ProfileFragment;
import com.test.foodzone.interfaces.activities.IActivity;
import com.test.foodzone.interfaces.activities.IHomeScreenActivity;
import com.test.foodzone.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity implements IActivity, IHomeScreenActivity {

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    @BindView(R.id.fragment_bottom_sheet)
    FrameLayout bottomSheetLayout;

    @BindView(R.id.imgClose)
    ImageView imgClose;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    ProfileFragment profileFragment;


    BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        toolbar.setTitle("Fudnow");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#909090"));
        bottomSheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        profileFragment = ProfileFragment.newInstance(null,null);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutOther, profileFragment).commit();




        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.menu_more:
                drawer.openDrawer(Gravity.RIGHT);
                return true;
            case R.id.menu_profile:
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                }
                else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
                else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(this,coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity getActivity()
    {
        return HomeScreenActivity.this;
    }

    @Override
    public void onBackPressed() {

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        }
        else
        {
            finish();
        }
    }

    @Override
    public void expandBottomSheet()
    {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
        else
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void hiddenBottomSheet()
    {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        }
        else
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(profileFragment!=null)
         profileFragment.onActivityResult(requestCode, resultCode, data);
    }
}
