package com.maya.wadmin.fragments.vehicle.types;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.pdi.AssignPDIVehiclesAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.AssignVehicleAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.TestDriveVehiclesAdapter;
import com.maya.wadmin.adapters.fragments.vehicle.types.VehiclesTypeAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.fleet.FleetHomeFragment;
import com.maya.wadmin.fragments.lot.LOTFragment;
import com.maya.wadmin.fragments.pdi.PDIHomeFragment;
import com.maya.wadmin.interfaces.fragments.testdrive.ITestDriveVehiclesAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehiclesTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehiclesTypeFragment extends Fragment implements IFragment, ITestDriveVehiclesAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    List<Vehicle> finalList,list;
    ITestDriveVehiclesAdapter iVehiclesTypeAdapter;
    TestDriveVehiclesAdapter testDriveVehiclesAdapter;

    int previous = -1;
    int adapterType = 0;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public VehiclesTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehiclesTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehiclesTypeFragment newInstance(String param1, String param2) {
        VehiclesTypeFragment fragment = new VehiclesTypeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static VehiclesTypeFragment newInstance(List<Vehicle> list) {
        VehiclesTypeFragment fragment = new VehiclesTypeFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Vehicle>>() {
        }.getType();
        args.putString("list",gson.toJson(list,type));
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_drive_vehilces_type, container, false);
        ButterKnife.bind(this,view);

        iVehiclesTypeAdapter = this;
        if(getArguments().getInt("type",-1)!= -1)
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Vehicle>>() {
            }.getType();
            list =  gson.fromJson(getArguments().getString("list"), type);
            finalList = list;
            adapterType = getArguments().getInt("type",0);
        }
        else
        {
            return view;
        }
        swipeRefreshLayout.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        progressBar.setVisibility(View.GONE);

        if(list!=null && list.size()>0 && adapterType == 0)
        {
            testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(activity(),list,iVehiclesTypeAdapter);
            recyclerView.setAdapter(testDriveVehiclesAdapter);
        }
        else if(adapterType==1 && list!=null && list.size()>0)
        {
            testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(adapterType,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
            recyclerView.setAdapter(testDriveVehiclesAdapter);
        }
        else if(adapterType ==2 && list!=null && list.size()>0)
        {
            testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(adapterType,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
            recyclerView.setAdapter(testDriveVehiclesAdapter);
        }
        else if(adapterType == 91011)
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity)activity()).clearSearchText();
                    fetchVehiclesBasedonType();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            if(Utility.isNetworkAvailable(activity()))
            {
                fetchVehiclesBasedonType();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }

        }
        else if(adapterType == 789)
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity)activity()).clearSearchText();
                    fetchVehiclesForFleet();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            if(Utility.isNetworkAvailable(activity()))
            {
                fetchVehiclesForFleet();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }

        }
        else if(adapterType == 7891012)
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity)activity()).clearSearchText();
                    fetchVehiclesForFleet();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            if(Utility.isNetworkAvailable(activity()))
            {
                fetchVehiclesForFleet();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }

        }
        else if(adapterType == 3)
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity)activity()).clearSearchText();
                    fetchArchievedVehiclesForPDI();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            if(Utility.isNetworkAvailable(activity()))
            {
                fetchArchievedVehiclesForPDI();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        }
        else if(adapterType == 4)
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity)activity()).clearSearchText();
                    fetchArchievedVehiclesForLoT();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            if(Utility.isNetworkAvailable(activity()))
            {
                fetchArchievedVehiclesForLoT();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        }
        else if(adapterType == 5)
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity)activity()).clearSearchText();
                    fetchArchievedVehiclesForTestDrive();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            if(Utility.isNetworkAvailable(activity()))
            {
                fetchArchievedVehiclesForTestDrive();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        }
        return view;
    }

    public void fetchVehiclesForFleet()
    {

        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_PDI_VEHICLES + getArguments().getInt("content", -1) + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    testDriveVehiclesAdapter =  new TestDriveVehiclesAdapter(4,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                    recyclerView.setAdapter(testDriveVehiclesAdapter);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public void fetchVehiclesBasedonType()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_PDI_VEHICLES + getArguments().getInt("content", -1) + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(1,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                    recyclerView.setAdapter(testDriveVehiclesAdapter);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    public void fetchArchievedVehiclesForPDI()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ARCHIEVED_PDI_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(1,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                    recyclerView.setAdapter(testDriveVehiclesAdapter);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    public void fetchArchievedVehiclesForLoT()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ARCHIEVED_LOT_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(4,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                    recyclerView.setAdapter(testDriveVehiclesAdapter);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    public void fetchArchievedVehiclesForTestDrive()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ARCHIEVED_TESTDRIVE_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(1,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                    recyclerView.setAdapter(testDriveVehiclesAdapter);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

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
    public void onTestDriveVehicleClick(Vehicle vehicle, int position)
    {
        if(previous == -1)
        {
            list.get(position).viewFullDetails = true;
        }
        else if(previous == position)
        {
            //return;
            if(list.get(position).viewFullDetails == false)
            list.get(position).viewFullDetails = true;
            else
            {
                list.get(position).viewFullDetails = false;
            }
        }
        else
        {
            list.get(previous).viewFullDetails = false;
            list.get(position).viewFullDetails = true;
        }
        previous = position;
        testDriveVehiclesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVehicleInfo(Vehicle vehicle, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,111);
        intent.putExtra("vehicle",vehicle);
        startActivity(intent);
    }

    @Override
    public void onVehicleInfo(Vehicle vehicle, int position, int salespersonPosition) {

    }

    @Override
    public void openLOTForm(Vehicle vehicle, int position, int salespersonPosition) {

    }

    @Override
    public void openPDIForm(Vehicle vehicle, int position, int salespersonPosition) {

    }

    @Override
    public void goToAsignPdi() {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,21);
        startActivity(intent);
    }

    @Override
    public void goToAssignPreparation()
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,11);
        startActivity(intent);
    }

    @Override
    public void openPDIForm(Vehicle vehicle, int position) {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,222);
        intent.putExtra("vehicle",vehicle);
        intent.putExtra("value",getArguments().getString("value"));
        try {

            PDIHomeFragment fragment = (PDIHomeFragment) getFragmentManager().getFragments().get(0);
            fragment.startActivityForResult(intent, Utility.generateRequestCodes().get("PDI_FORM"));
        }
        catch (Exception e)
        {
            startActivityForResult(intent, Utility.generateRequestCodes().get("PDI_FORM"));
        }
    }

    @Override
    public void openLOTForm(Vehicle vehicle, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,223);
        intent.putExtra("vehicle",vehicle);
        intent.putExtra("value",getArguments().getString("value"));
        if(adapterType==7891012)
        {
            LOTFragment fragment = (LOTFragment) getFragmentManager().getFragments().get(0);
            fragment.startActivityForResult(intent, Utility.generateRequestCodes().get("LOT_FORM"));
        }
        else if(adapterType==789)
        {
            FleetHomeFragment fragment = (FleetHomeFragment) getFragmentManager().getFragments().get(0);
            fragment.startActivityForResult(intent, Utility.generateRequestCodes().get("LOT_FORM"));
        }
        else
        {
            startActivityForResult(intent, Utility.generateRequestCodes().get("LOT_FORM"));
        }
    }

    @Override
    public void openOptions(Vehicle vehicle, int position)
    {
        switch (adapterType)
        {
            case 0:
            ((HelperActivity) activity()).openOptions(vehicle.Status.equalsIgnoreCase(Constants.ON_LOT) ? 1 : 2, vehicle);
            break;
            case 91011:
            ((HelperActivity) activity()).openOptions(getArguments().getString("value").equalsIgnoreCase(Constants.MARK_FOR_PDI) ?  3: 4, vehicle);
            break;
            case 3:
            ((HelperActivity) activity()).openOptions(4, vehicle);
            break;
            case 789:
            ((HelperActivity) activity()).openOptions(getArguments().getString("value").equalsIgnoreCase(Constants.DELIVERY_RECEIVED) ?  5: 4, vehicle);
            break;
            case 7891012:
            ((HelperActivity) activity()).openOptions(getArguments().getString("value").equalsIgnoreCase(Constants.DELIVERY_RECEIVED) ?  5: 4, vehicle);
            break;



            default: // for missing things
            ((HelperActivity) activity()).openOptions(4, vehicle);
            break;
        }
    }


    public void updateVehiclesBySearch(String content)
    {
        Logger.d(content);
        if(finalList!=null && finalList.size()>0)
        {

            if (content.length() > 0)
            {
                List<Vehicle> subList = new ArrayList<>();
                for(Vehicle vehicle : finalList)
                {
                    if ((vehicle.Model + " " + vehicle.Make + " " + vehicle.Year + " " + vehicle.Type + " " +
                            vehicle.Vin + " " + vehicle.Model + " " + vehicle.Year + " " +
                            vehicle.Make + " " + vehicle.Type + " " + vehicle.Vin + " " + vehicle.Make + " " +
                            vehicle.Model + " " + vehicle.Year + " " + vehicle.Make + " " +
                            vehicle.Type + " " + vehicle.Year + " " + vehicle.Vin + vehicle.Model + " " + vehicle.Type)
                            .toLowerCase().contains(content.toLowerCase()))
                    {
                        subList.add(vehicle);
                    }
                }

                if (subList.size() > 0)
                {
                    list = subList;
                    previous = -1;
                    setAdapterInSearch();
                    recyclerView.setAdapter(testDriveVehiclesAdapter);
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
                previous = -1;
                setAdapterInSearch();
                recyclerView.setAdapter(testDriveVehiclesAdapter);
            }
        }

    }

    public void setAdapterInSearch()
    {
        switch (adapterType)
        {
            case 0:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(activity(),list,iVehiclesTypeAdapter);
                break;
            case 1:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(adapterType,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 2:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(adapterType,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 3:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(1,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 4:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(4,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 5:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(1,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 91011:
                testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(1,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 789:
                testDriveVehiclesAdapter =  new TestDriveVehiclesAdapter(4,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
            case 7891012:
                testDriveVehiclesAdapter =  new TestDriveVehiclesAdapter(4,getArguments().getString("value"),activity(),list,iVehiclesTypeAdapter);
                break;
        }
    }
}
