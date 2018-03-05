package com.maya.wadmin.adapters.fragments.vehicle.types;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.adapters.fragments.delivery.VehicleArrivalAdapter;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.vehicle.types.VehiclesTypeFragment;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul Kalagara on 2/1/2018.
 */

public class VehiclesTypeAdapter extends FragmentStatePagerAdapter
{
    int type;
    Gson gson = new Gson();
    List<Vehicle> inLot,inTestDrive,inReturn;
    Type typeGson = new TypeToken<List<Vehicle>>() {}.getType();
    ArrayList<List<Vehicle>> arrayList = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    int[] vehiclesType;


    public VehiclesTypeAdapter(FragmentManager fm, int type, ArrayList<String> stringArrayList,int[] vehicleType)
    {
        super(fm);
        this.type = type;
        this.stringArrayList = stringArrayList;
        this.vehiclesType = vehicleType;

    }

    public VehiclesTypeAdapter(FragmentManager fm, int type, List<Vehicle> inLot, List<Vehicle> inTestDrive, List<Vehicle> inReturn)
    {
        super(fm);
        this.type = type;
        this.inLot = inLot;
        this.inTestDrive = inTestDrive;
        this.inReturn = inReturn;
    }

    List<Vehicle> inMarkForPDI,inPDIInCompleted,inPDICompleted;
    public VehiclesTypeAdapter(FragmentManager fm, List<Vehicle> inMarkForPDI, List<Vehicle> inPDIInCompleted, List<Vehicle> inPDICompleted,  int type)
    {
        super(fm);
        this.type = type;
        this.inMarkForPDI = inMarkForPDI;
        this.inPDIInCompleted = inPDIInCompleted;
        this.inPDICompleted = inPDICompleted;
    }

    List<Vehicle> inDeliveryReceived, inPreparingForLot, inInventory;

    public VehiclesTypeAdapter(FragmentManager fm, List<Vehicle> inDeliveryReceived, List<Vehicle> inPreparingForLot,
                               List<Vehicle> inMarkForPDI, List<Vehicle> inPDIInCompleted, List<Vehicle> inInventory,
                               List<Vehicle> inTestDrive, int type)
    {
        super(fm);
        this.type = type;
        arrayList.add(inDeliveryReceived);
        arrayList.add(inPreparingForLot);
        arrayList.add(inMarkForPDI);
        arrayList.add(inPDIInCompleted);
        arrayList.add(inInventory);
        arrayList.add(inTestDrive);

        stringArrayList.add(Constants.DELIVERY_RECEIVED);
        stringArrayList.add(Constants.PREPARING_FOR_LOT);
        stringArrayList.add(Constants.MARK_FOR_PDI);
        stringArrayList.add(Constants.PDI_INCOMPLETE);
        stringArrayList.add(Constants.INVENTORY);
        stringArrayList.add(Constants.TEST_DRIVE);

    }
    @Override
    public Fragment getItem(int position)
    {
        try {

            Fragment fragment = new VehiclesTypeFragment();
            Bundle bundle = new Bundle();
            if (type == 0) {
                switch (position) {
                    case 0:
                        bundle.putInt("type", type);
                        bundle.putString("list", gson.toJson(inLot, typeGson));
                        fragment.setArguments(bundle);
                        break;
                    case 1:
                        bundle.putInt("type", type);
                        bundle.putString("list", gson.toJson(inTestDrive, typeGson));
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        bundle.putInt("type", type);
                        bundle.putString("list", gson.toJson(inReturn, typeGson));
                        fragment.setArguments(bundle);
                        break;
                }
            } else if (type == 1) {
                switch (position) {
                    case 0:
                        bundle.putInt("type", type);
                        bundle.putString("list", gson.toJson(inMarkForPDI, typeGson));
                        bundle.putString("value", Constants.MARK_FOR_PDI);
                        fragment.setArguments(bundle);
                        break;
                    case 1:
                        bundle.putInt("type", type);
                        bundle.putString("list", gson.toJson(inPDIInCompleted, typeGson));
                        bundle.putString("value", Constants.PDI_INCOMPLETE);
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        bundle.putInt("type", type);
                        bundle.putString("list", gson.toJson(inPDICompleted, typeGson));
                        bundle.putString("value", Constants.PDI_COMPLETE);
                        fragment.setArguments(bundle);
                        break;
                }
            } else if (type == 2) {
                bundle.putInt("type", type);
                bundle.putString("list", gson.toJson(arrayList.get(position), typeGson));
                bundle.putString("value", stringArrayList.get(position));
                fragment.setArguments(bundle);

            } else if (type == 91011) {
                bundle.putInt("type", type);
                bundle.putString("list", null);
                bundle.putString("value", stringArrayList.get(position));
                bundle.putInt("content", vehiclesType[position]);
                fragment.setArguments(bundle);
            } else if (type == 789) {
                bundle.putInt("type", type);
                bundle.putString("list", null);
                bundle.putString("value", stringArrayList.get(position));
                bundle.putInt("content", vehiclesType[position]);
                fragment.setArguments(bundle);
            } else if (type == 7891012) {
                bundle.putInt("type", type);
                bundle.putString("list", null);
                bundle.putString("value", stringArrayList.get(position));
                bundle.putInt("content", vehiclesType[position]);
                fragment.setArguments(bundle);
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
        int count = 0;
        switch (type)
        {
            case 0:
                count = 3;
                break;
            case 1:
                count = 3;
                break;
            case 2:
                count = stringArrayList.size();
                break;

            case 91011:
                count = stringArrayList.size();
                break;
            case 789:
                count = stringArrayList.size();
                break;
            case 7891012:
                count = stringArrayList.size();
                break;
        }
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String frag=null;

        if(type==0)
        {
            switch (position)
            {
                case 0:
                    frag = "On Lot";
                    break;
                case 1:
                    frag = "Test Drive";
                    break;
                case 2:
                    frag = "Return";
                    break;
            }
        }
        else if(type==1)
        {
            switch (position)
                {
                    case 0:
                        frag = Constants.MARK_FOR_PDI;
                        break;
                    case 1:
                        frag = Constants.PDI_INCOMPLETE;
                        break;
                    case 2:
                        frag = Constants.PDI_COMPLETE;
                        break;
                }
        }
        else if(type==2)
        {
            frag = stringArrayList.get(position);
        }
        else if(type == 91011)
        {
            frag = stringArrayList.get(position);
        }
        else if(type == 789)
        {
            frag = stringArrayList.get(position);
        }
        else if(type == 7891012)
        {
            frag = stringArrayList.get(position);
        }
        else
        {

        }
        return frag;
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
