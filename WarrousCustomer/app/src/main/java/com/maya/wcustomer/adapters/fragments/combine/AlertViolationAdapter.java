package com.maya.wcustomer.adapters.fragments.combine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maya.wcustomer.fragments.alerts.AlertsMainFragment;
import com.maya.wcustomer.fragments.violations.ViolationsMainFragment;

/**
 * Created by Gokul Kalagara on 3/16/2018.
 */

public class AlertViolationAdapter extends FragmentPagerAdapter
{

    public AlertViolationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = AlertsMainFragment.newInstance(null,null);
                break;
            case 1:
                fragment = ViolationsMainFragment.newInstance(null,null);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
