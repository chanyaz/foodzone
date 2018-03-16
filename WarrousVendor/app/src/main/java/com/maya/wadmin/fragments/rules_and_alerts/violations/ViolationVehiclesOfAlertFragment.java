package com.maya.wadmin.fragments.rules_and_alerts.violations;


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
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.delivery.VehicleArrivalAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViolationVehiclesOfAlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationVehiclesOfAlertFragment extends Fragment implements IFragment {
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
    ProgressBar progressBar;
    VehicleArrivalAdapter vehicleArrivalAdapter;
    List<Vehicle> list;
    AlertRule alertRule;

    public ViolationVehiclesOfAlertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViolationVehiclesOfAlertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViolationVehiclesOfAlertFragment newInstance(String param1, String param2) {
        ViolationVehiclesOfAlertFragment fragment = new ViolationVehiclesOfAlertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ViolationVehiclesOfAlertFragment newInstance(AlertRule alertRule)
    {
        ViolationVehiclesOfAlertFragment fragment = new ViolationVehiclesOfAlertFragment();
        Bundle args = new Bundle();
        args.putSerializable("AlertRule", alertRule);
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
        View view = inflater.inflate(R.layout.fragment_violation_vehicles_of_alert, container, false);
        if(getArguments()!=null)
        {
            if((AlertRule) getArguments().getSerializable("AlertRule")!=null)
            {
                alertRule = (AlertRule) getArguments().getSerializable("AlertRule");
                initialize(view);
            }
            else
            {
                return view;
            }
        }
        else
        {
            return view;
        }
        return view;
    }

    private void initialize(View view)
    {
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if(Utility.isNetworkAvailable(activity()))
                {
                    ((HelperActivity) activity()).clearSearchText();
                    fetchViolationVehicles();
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
            fetchViolationVehicles();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }
    }

    private void fetchViolationVehicles()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_VIOLATION_VEHICLES_OF_ALERT + alertRule.AlertId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    vehicleArrivalAdapter = new VehicleArrivalAdapter(2,activity(),list,null);
                    recyclerView.setAdapter(vehicleArrivalAdapter);
                }


                progressBar.setVisibility(View.GONE);


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
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }
}
