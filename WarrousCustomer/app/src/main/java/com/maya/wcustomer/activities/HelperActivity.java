package com.maya.wcustomer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.fragments.combine.AlertViolationsHomeFragment;
import com.maya.wcustomer.fragments.vehicle.details.VehicleDetailsFragment;
import com.maya.wcustomer.fragments.violations.ViolationsMainFragment;
import com.maya.wcustomer.interfaces.activities.IActivity;
import com.maya.wcustomer.utilities.Logger;
import com.maya.wcustomer.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperActivity extends AppCompatActivity implements IActivity{

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ic_back)
    ImageView ic_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);

        toolbar.setTitle("");

        ic_back.setOnClickListener(v -> onBackPressed());


        setUpFragment();
    }


    public void setUpFragment()
    {
        Intent intent = getIntent();
        int fragmentKey = intent.getIntExtra(Constants.FRAGMENT_KEY,0);
        Logger.d(Constants.FRAGMENT_KEY,""+fragmentKey);
        if(fragmentKey>0) {
            Fragment fragment = null;

            switch (fragmentKey)
            {
                case 1:
                    changeTitle("Details");
                    fragment = VehicleDetailsFragment.newInstance(null,null);
                    break;

                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    fragment = AlertViolationsHomeFragment.newInstance(null,null);
                    break;
            }

            if(fragment!=null)
            {
                //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack("START").commitAllowingStateLoss();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        }
    }

    @Override
    public void changeTitle(String title) {

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
        super.onBackPressed();
    }
}
