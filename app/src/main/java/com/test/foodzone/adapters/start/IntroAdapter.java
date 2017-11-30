package com.test.foodzone.adapters.start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.test.foodzone.fragments.start.IntroFragment;

/**
 * Created by Gokul Kalagara on 11/30/2017.
 */

public class IntroAdapter extends FragmentPagerAdapter
{
    public IntroAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return IntroFragment.newInstance(null,"0");

            case 1:
                return IntroFragment.newInstance(null,"1");

            case 2:
                return IntroFragment.newInstance(null,"2");

        }
        return null;

    }

    @Override
    public int getCount() {
        return 3;
    }
}