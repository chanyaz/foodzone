package com.maya.wadmin.fragments.vehicle.find;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
import com.maya.wadmin.adapters.fragments.find.FindVehiclesAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.AssignVehicleAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.vehicle.find.IFindVehiclesAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.GroupFilter;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment implements IFragment, IFindVehiclesAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupFilter groupFilter = null;

    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    IFindVehiclesAdapter iFindVehiclesAdapter;
    FindVehiclesAdapter findVehiclesAdapter;
    List<Vehicle> list, finalList;
    int previous = -1;


    public FindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
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
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        iFindVehiclesAdapter = this;
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchVehicles();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        if(Utility.isNetworkAvailable(activity()))
        {
            fetchVehicles();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
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


    public void fetchVehicles()
    {
        previous = -1;
        if(list!=null && list.size()>0)
        {
            ((HelperActivity) activity()).clearSearchText();
        }
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_FETCH_ALL_VEHICLES + "&PageNumber=5" + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
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
                    findVehiclesAdapter = new FindVehiclesAdapter(list,activity(),iFindVehiclesAdapter);
                    recyclerView.setAdapter(findVehiclesAdapter);
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
    public void OnFindVehicleClick(Vehicle vehicle,int position)
    {
        if(previous == -1)
        {
            list.get(position).viewFullDetails = true;
        }
        else if(previous == position)
        {
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
        findVehiclesAdapter.notifyDataSetChanged();
    }


    public void getFilterList(String content)
    {
        previous = -1;
        if(list!=null && list.size()>0)
        {
            ((HelperActivity) activity()).clearSearchText();
        }
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_FILTER_VEHICLES + "?" + content + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
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
                    findVehiclesAdapter = new FindVehiclesAdapter(list,activity(),iFindVehiclesAdapter);
                    recyclerView.setAdapter(findVehiclesAdapter);
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
    public void GoToVehicleOverview(Vehicle vehicle, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,111);
        intent.putExtra("vehicle",vehicle);
        startActivity(intent);
    }


    public void updateListBySearch(String content)
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
                                .toLowerCase().contains(content.toLowerCase())) {
                            subList.add(vehicle);
                        }
                    }
                    if (subList.size() > 0)
                    {
                        list = subList;
                        previous = -1;
                        findVehiclesAdapter = new FindVehiclesAdapter(list,activity(),iFindVehiclesAdapter);
                        recyclerView.setAdapter(findVehiclesAdapter);
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
                findVehiclesAdapter = new FindVehiclesAdapter(list,activity(),iFindVehiclesAdapter);
                recyclerView.setAdapter(findVehiclesAdapter);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Utility.generateRequestCodes().get("APPLY_FILTER"))
        {
            if(data!=null)
            {
                if(data.getStringExtra("content")!=null)
                {
                    Gson gson = new Gson();
                    Type type = new TypeToken<GroupFilter>(){}.getType();
                    groupFilter = gson.fromJson(data.getStringExtra("GroupFilter"),type);
                    getFilterList(data.getStringExtra("content"));
                    Logger.d("content",data.getStringExtra("content"));
                    showSnackBar("Updating list",1);
                }
            }
        }
    }
}
