package com.maya.vgarages.adapters.custom;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maya.vgarages.fragments.garage.profile.GarageProfileFragment;
import com.maya.vgarages.fragments.garage.reviews.GarageReviewsFragment;
import com.maya.vgarages.fragments.garage.services.GarageServicesFragment;

import java.util.List;

/**
 * Created by Gokul Kalagara on 4/11/2018.
 */

public class CustomViewPagerAdapter extends FragmentPagerAdapter
{

    private int type = 0;
    private List<String> stringList;

    public CustomViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CustomViewPagerAdapter(FragmentManager fm, int type, List<String> stringList)
    {
        super(fm);
        this.type = type;
        this.stringList = stringList;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if(type==1)
        {
            switch (position)
            {
                case 0:
                    fragment = GarageProfileFragment.newInstance(null,null);
                    break;
                case 1:
                    fragment = GarageServicesFragment.newInstance(null,null);
                    break;
                case 2:
                    fragment = GarageReviewsFragment.newInstance(null,null);
                    break;
            }
        }


        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return stringList.get(position);
    }

    @Override
    public int getCount()
    {
        return stringList.size();
    }
}
