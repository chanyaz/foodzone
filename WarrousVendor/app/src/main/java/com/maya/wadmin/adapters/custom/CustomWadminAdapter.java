package com.maya.wadmin.adapters.custom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.delivery.view.VehicleArrivalFragment;
import com.maya.wadmin.fragments.rules_and_alerts.view.AlertsFragment;
import com.maya.wadmin.models.DeliveryTruck;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class CustomWadminAdapter extends FragmentPagerAdapter
{
    int type;
    ArrayList<List<DeliveryTruck>> deliveryTruck;
    List<String> stringList;
    Gson gson = new Gson();
    Type typeGson = new TypeToken<List<DeliveryTruck>>() {}.getType();
    public Fragment mCurrentFragment = null;
    FragmentManager fragmentManager;
    HashMap<Integer,String> tags = new HashMap<>();

    public CustomWadminAdapter(FragmentManager fm) {
        super(fm);
    }

    public CustomWadminAdapter(FragmentManager fm, int type, ArrayList<List<DeliveryTruck>> deliveryTruck, List<String> stringList) {
        super(fm);
        this.fragmentManager = fm;
        this.type = type;
        this.stringList = stringList;
        this.deliveryTruck = deliveryTruck;

    }

    public CustomWadminAdapter(FragmentManager fm,int type, List<String> stringList)
    {
        super(fm);
        this.type = type;
        this.stringList = stringList;
    }


    @Override
    public Fragment getItem(int position)
    {
        try {

            Fragment fragment = new VehicleArrivalFragment();
            Bundle bundle = new Bundle();
            if (type == 0)
            {
                bundle.putInt("type", type);
                bundle.putString("list", gson.toJson(deliveryTruck.get(position), typeGson));
                bundle.putString("value", stringList.get(position));
                fragment.setArguments(bundle);
            }
            else if (type == 1)
            {
                fragment = new AlertsFragment();
                bundle.putInt("CategoryId", Constants.ALERT_TYPE_IDS[position]);
                fragment.setArguments(bundle);

            } else if (type == 2) {


            }
            return fragment;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int getCount()
    {
        return stringList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return stringList.get(position);
    }






}
