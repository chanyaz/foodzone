package com.maya.wcustomer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.fragments.start.LoginFragment;
import com.maya.wcustomer.interfaces.activities.IActivity;
import com.maya.wcustomer.utilities.Utility;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements IActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utility.getSharedPreferences().contains(Constants.LOGIN))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },500);
        }
        else
            {

            setContentView(R.layout.activity_splash);
            ButterKnife.bind(this);
            setUpFragment();
        }
    }

    private void setUpFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, LoginFragment.newInstance(null,null)).commit();
    }

    public void goToHome()
    {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity activity() {
        return this;
    }
}
