package com.maya.wcustomer.adapters.fragments.myway;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maya.wcustomer.fragments.my_way.nearby.NearByFragment;
import com.maya.wcustomer.fragments.my_way.trips.RecentTripsFragment;

/**
 * Created by Gokul Kalagara on 3/19/2018.
 */

public class MyWayHomeAdapter extends FragmentPagerAdapter {


    public MyWayHomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = RecentTripsFragment.newInstance(null,null);
                break;
            case 1:
                fragment = NearByFragment.newInstance(null,null);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
