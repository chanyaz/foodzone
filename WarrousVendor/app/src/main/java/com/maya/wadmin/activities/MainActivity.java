package com.maya.wadmin.activities;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.dialogs.other.LogoutDialog;
import com.maya.wadmin.fragments.home.HomeFragment;
import com.maya.wadmin.interfaces.activities.IActivity;
import com.maya.wadmin.utilities.Utility;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity implements IActivity
{

    BottomNavigationViewEx navigation;
    DrawerLayout drawer;
    android.support.v7.widget.Toolbar toolbar;
    NavigationView navigationView;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    CoordinatorLayout coordinatorLayout;
    NestedScrollView nestedScrollView;
    TextView tvUserName, tvUserRoleName;
    TextView tvCurrentPlatform, tvOtherPlatform;
    TextView tvUserProfile, tvSettings, tvFAQ, tvSignout;
    View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();


    }

    private void initialize()
    {

        navigationView = findViewById(R.id.nav_view);
        navigation = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        headerLayout = navigationView.getHeaderView(0);
        tvUserRoleName = (TextView) headerLayout.findViewById(R.id.tvUserRoleName);
        tvUserName = (TextView) headerLayout.findViewById(R.id.tvUserName);
        tvOtherPlatform = headerLayout.findViewById(R.id.tvOtherPlatform);
        tvCurrentPlatform = headerLayout.findViewById(R.id.tvCurrentPlatform);
        nestedScrollView = headerLayout.findViewById(R.id.nestedScrollView);
        tvSignout = headerLayout.findViewById(R.id.tvSignOut);



        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(true);
        tvUserName.setText(Utility.getString(Utility.getSharedPreferences(), Constants.FIRST_NAME)+ " " +Utility.getString(Utility.getSharedPreferences(), Constants.LAST_NAME));
        tvUserRoleName.setText(Utility.getString(Utility.getSharedPreferences(), Constants.USER_ROLL_NAME));



        tvCurrentPlatform.setText(Constants.portalsTypeIDS[ Utility.getPortalType() ]);
        tvOtherPlatform.setText(Utility.getPortalType()==1?Constants.portalsTypeIDS[0]:Constants.portalsTypeIDS[1]);
        tvOtherPlatform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updatePortal();
            }
        });

        addBadgeAt(0, 6);
        addBadgeAt(3, 6);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, HomeFragment.newInstance(null,null)).commit();

        tvSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openLogoutDialog();
            }
        });
    }

    private void openLogoutDialog()
    {
        drawer.closeDrawers();
        LogoutDialog dialog = new LogoutDialog(activity());
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,drawer,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    private Badge addBadgeAt(int position, int number)
    {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(22, 2, true)
                .bindTarget(navigation.getBottomNavigationItemView(position))
                .setBadgeBackgroundColor(ContextCompat.getColor(activity(),R.color.badge_color))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                        {

                        }
                          //  Toast.makeText(BadgeViewActivity.this, R.string.tips_badge_removed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void changeTitle(String title)
    {
        toolbar.setTitle(title);
    }

    @Override
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(this,coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity()
    {
        return this;
    }


    public void updatePortal()
    {
        tvOtherPlatform.setText(Constants.portalsTypeIDS[Utility.getPortalType()]);
        if(Utility.getPortalType()==1)
        {
            Utility.setInt(Utility.getSharedPreferences(),Constants.PORTAL_TYPE,0);
        }
        else
        {
            Utility.setInt(Utility.getSharedPreferences(),Constants.PORTAL_TYPE,1);
        }
        tvCurrentPlatform.setText(Constants.portalsTypeIDS[Utility.getPortalType()]);
        drawer.closeDrawers();
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().getFragments().get(0);
        homeFragment.generateUserRoles();
        homeFragment.showSnackBar("Switched to "+Constants.portalsTypeIDS[Utility.getPortalType()],2);


    }
}
