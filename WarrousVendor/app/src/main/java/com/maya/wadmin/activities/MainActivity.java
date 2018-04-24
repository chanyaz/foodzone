package com.maya.wadmin.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.maya.wadmin.Manifest;
import com.maya.wadmin.R;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.dialogs.other.LogoutDialog;
import com.maya.wadmin.fragments.home.HomeFragment;
import com.maya.wadmin.interfaces.activities.IActivity;
import com.maya.wadmin.utilities.CommonApiCalls;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity implements IActivity
{

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.coordinatorLayout)
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
        ButterKnife.bind(this);

        initialize();

        if(Utility.getSharedPreferences().contains(Constants.USER_FCM_TOKEN))
        {
            if (Utility.getSharedPreferences().contains(Constants.CURRENT_USER_FCM_TOKEN)) {
                if (Utility.getString(Utility.getSharedPreferences(), Constants.USER_FCM_TOKEN).equalsIgnoreCase(Utility.getString(Utility.getSharedPreferences(), Constants.CURRENT_USER_FCM_TOKEN))) {
                    return;
                }
                if (Utility.isNetworkAvailable(activity()))
                    sendFcmToken(Utility.getString(Utility.getSharedPreferences(), Constants.USER_FCM_TOKEN));
            } else {
                if (Utility.isNetworkAvailable(activity()))
                    sendFcmToken(Utility.getString(Utility.getSharedPreferences(), Constants.USER_FCM_TOKEN));
            }
        }


    }

    private void sendFcmToken(final String fcmToken)
    {
        if(fcmToken==null)
        {
            return;
        }
        JSONObject input = new JSONObject();
        try
        {
            input.put("PNSId",Utility.getString(Utility.getSharedPreferences(),Constants.USER_FCM_TOKEN));
            input.put("SecureId",Constants.SECURE_ID);
            input.put("UserId",Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID));
            input.put("IMEI",Utility.getPhoneUniqueId(activity()));
            input.put("FirstName",Utility.getString(Utility.getSharedPreferences(),Constants.FIRST_NAME));
            input.put("LastName",Utility.getString(Utility.getSharedPreferences(),Constants.LAST_NAME));
            input.put("EmailAddress",Utility.getString(Utility.getSharedPreferences(),Constants.USER_NAME));
            input.put("PhoneNumber","");
            input.put("RoleId","0");
            input.put("Type","A");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            boolean success;
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Utility.setString(Utility.getSharedPreferences(),Constants.CURRENT_USER_FCM_TOKEN,fcmToken);
                Logger.d("[response]",jsonObject.toString());
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]","Unable to contact server");
                try
                {
                    if (volleyError.networkResponse.statusCode == 401)
                    {
                        CommonApiCalls.refreshAuthTokenCall();
                        sendFcmToken(fcmToken);
                    }
                    else
                    {

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Utility.setString(Utility.getSharedPreferences(),Constants.CURRENT_USER_FCM_TOKEN,fcmToken);
                }

            }
        };
        volleyHelperLayer.startHandlerVolley(Constants.URL_INSERT_USER_PNS,input,listener,errorListener, Request.Priority.NORMAL);




    }

    private void initialize()
    {

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        // side navigation items
        headerLayout = navigationView.getHeaderView(0);
        tvUserRoleName =  headerLayout.findViewById(R.id.tvUserRoleName);
        tvUserName =  headerLayout.findViewById(R.id.tvUserName);
        tvOtherPlatform = headerLayout.findViewById(R.id.tvOtherPlatform);
        tvCurrentPlatform = headerLayout.findViewById(R.id.tvCurrentPlatform);
        nestedScrollView = headerLayout.findViewById(R.id.nestedScrollView);
        tvSignout = headerLayout.findViewById(R.id.tvSignOut);
        tvUserProfile = headerLayout.findViewById(R.id.tvUserProfile);
        tvFAQ = headerLayout.findViewById(R.id.tvFAQ);



        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(true);

        tvUserName.setText(Utility.getString(Utility.getSharedPreferences(), Constants.FIRST_NAME)+ " " +Utility.getString(Utility.getSharedPreferences(), Constants.LAST_NAME));
        tvUserName.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text",Utility.getString(Utility.getSharedPreferences(),Constants.USER_FCM_TOKEN));
            clipboard.setPrimaryClip(clip);
            showSnackBar("Copied fcm token",2);
            drawer.closeDrawers();

        });
        tvUserRoleName.setText(Utility.getString(Utility.getSharedPreferences(), Constants.USER_ROLL_NAME));


        tvCurrentPlatform.setText(Constants.portalsTypeIDS[Utility.getPortalType()]);
        tvOtherPlatform.setText(Utility.getPortalType()==1?Constants.portalsTypeIDS[0]:Constants.portalsTypeIDS[1]);


        tvOtherPlatform.setOnClickListener(v -> {
            updatePortal();
        });

        addBadgeAt(0, 4);
        addBadgeAt(3, 9);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, HomeFragment.newInstance(null,null)).commit();



        tvSignout.setOnClickListener(v ->
        {
            openLogoutDialog();
        });

        tvUserProfile.setOnClickListener(v -> {
            openUserProfile();
        });

        tvFAQ.setOnClickListener(v -> {
            openFAQ();
        });



    }

    private void openUserProfile()
    {
        drawer.closeDrawers();
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,8);
        startActivity(intent);
    }

    private void openFAQ()
    {
        drawer.closeDrawers();
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,9);
        startActivity(intent);
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


    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            //item.setChecked(true);

            switch (item.getItemId())
            {
                case R.id.navigation_scan:
                    openBarCodeScan();
                    break;
                case R.id.navigation_notifications:
                    openNotifications();
                    break;
                case R.id.navigation_location:
                    openLocations();
                    break;
                case R.id.navigation_messages:
                    openMessages();
                    break;
            }
            return false;
        }
    };

    private void openLocations()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,704);
        startActivity(intent);
    }

    private void openNotifications()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,703);
        startActivity(intent);
    }

    private void openMessages()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,702);
        startActivity(intent);
    }

    private void openBarCodeScan()
    {
        if(checkPermissions(1))
        {
            Intent intent = new Intent(activity(),HelperActivity.class);
            intent.putExtra(Constants.FRAGMENT_KEY,701);
            startActivity(intent);
        }
    }

    private boolean checkPermissions(int option)
    {
        switch (option)
        {

            case 1:
                if (ContextCompat.checkSelfPermission(activity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(activity(), new String[]{android.Manifest.permission.CAMERA}, Utility.generateRequestCodes().get("REQUEST_CAMERA"));
                    return false;
                }
            case 2:
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Utility.generateRequestCodes().get("REQUEST_CAMERA"))
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
              if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
              {
                  if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) == false)
                  {
                      Toast.makeText(this, "Open permissions and give all the permissions in order to access the app", Toast.LENGTH_LONG).show();
                      Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                      Uri uri = Uri.fromParts("package", getPackageName(), null);
                      intent.setData(uri);
                      startActivity(intent);
                  }
                  else
                  {
                      checkPermissions(1);
                  }
              }
            }
            else
            {
                openBarCodeScan();
            }
        }
    }
}
