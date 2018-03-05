package com.maya.wadmin.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.applications.WAdminApplication;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.start.LoginFragment;
import com.maya.wadmin.interfaces.activities.IActivity;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

public class SplashActivity extends AppCompatActivity implements IActivity{

    CoordinatorLayout coordinatorLayout;
    TextView tvTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Utility.hideKeyboard(activity());
        if(Utility.getBoolen(Utility.getSharedPreferences(),Constants.LOGIN))
        {
            Intent intent = new Intent(activity(),MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        tvTitle = findViewById(R.id.tvTitle);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setUpFragment();

    }


    public void setUpFragment()
    {
        tvTitle.setText("Sign In");
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, LoginFragment.newInstance(null,null)).commit();
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void changeTitle(String title)
    {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity activity()
    {
        return this;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
