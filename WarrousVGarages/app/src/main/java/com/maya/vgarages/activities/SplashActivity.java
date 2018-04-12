package com.maya.vgarages.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maya.vgarages.R;
import com.maya.vgarages.fragments.start.SplashFragment;
import com.maya.vgarages.fragments.start.StartFragment;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements IFragment {


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(activity(),MainActivity.class);
        startActivity(intent);
        finish();

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, SplashFragment.newInstance(null,null)).commit();

    }

    @Override
    public void changeTitle(String title)
    {

    }

    @Override
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        try
        {
            SplashFragment splashFragment = (SplashFragment) getSupportFragmentManager().getFragments().get(0);
            if(splashFragment.getChildFragmentManager().getBackStackEntryCount()>1)
            {
                splashFragment.getChildFragmentManager().popBackStack();
            }
            else
            {
                super.onBackPressed();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
