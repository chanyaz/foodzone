package com.maya.wadmin.fragments.delivery.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.delivery.TruckDeliveryAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.TestDriveVehiclesAdapter;
import com.maya.wadmin.interfaces.adapters.delivery.ITruckDeliveryAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.interfaces.fragments.delivery.IVehicleArrivalFragment;
import com.maya.wadmin.models.DeliveryTruck;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehicleArrivalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleArrivalFragment extends Fragment implements IFragment, ITruckDeliveryAdapter
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<DeliveryTruck> list, finalList;
    int adapterType = 0;
    TruckDeliveryAdapter truckDeliveryAdapter;
    ITruckDeliveryAdapter iTruckDeliveryAdapter;
    IVehicleArrivalFragment iVehicleArrivalFragment;


    public VehicleArrivalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehicleArrivalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleArrivalFragment newInstance(String param1, String param2) {
        VehicleArrivalFragment fragment = new VehicleArrivalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_arrival, container, false);
        iTruckDeliveryAdapter = this;
        iVehicleArrivalFragment = (HelperActivity) activity();
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout.setEnabled(false);
        recyclerView.setNestedScrollingEnabled(true);

        if(getArguments().getString("list")!=null)
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<DeliveryTruck>>() {
            }.getType();
            list =  gson.fromJson(getArguments().getString("list"), type);
            finalList = list;
            adapterType = getArguments().getInt("type",0);
        }
        else
        {
            return view;
        }

        if(list!=null && list.size()>0 && adapterType == 0)
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
            truckDeliveryAdapter = new TruckDeliveryAdapter(list,activity(),iTruckDeliveryAdapter);
            recyclerView.setAdapter(truckDeliveryAdapter);
        }


        return view;
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onMapClick(DeliveryTruck truck, int position)
    {
        iVehicleArrivalFragment.addMapFragment(truck,position);
    }



    public void updateSearchItem(String content)
    {
        Logger.d(content);
        if(finalList!=null && finalList.size()>0)
        {
            if (content.length() > 0)
            {
                List<DeliveryTruck> subList = new ArrayList<>();
                for(DeliveryTruck truck : finalList)
                {
                    if ((truck.TruckName + " " + truck.Origin + " " + truck.Destination +
                        truck.DriverName)
                            .toLowerCase().contains(content.toLowerCase()))
                    {
                        subList.add(truck);
                    }
                }

                if (subList.size() > 0)
                {
                    list = subList;
                    truckDeliveryAdapter = new TruckDeliveryAdapter(list,activity(),iTruckDeliveryAdapter);
                    recyclerView.setAdapter(truckDeliveryAdapter);
                }
                else
                {
                    recyclerView.setAdapter(null);
                }
            }
            else
            {
                //Utility.hideKeyboard(activity());
                list = finalList;
                truckDeliveryAdapter = new TruckDeliveryAdapter(list,activity(),iTruckDeliveryAdapter);
                recyclerView.setAdapter(truckDeliveryAdapter);
            }
        }

    }
}
