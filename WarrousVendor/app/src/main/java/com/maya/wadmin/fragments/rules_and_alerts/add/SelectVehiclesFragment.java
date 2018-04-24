package com.maya.wadmin.fragments.rules_and_alerts.add;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.adapters.custom.TopBarAdapter;
import com.maya.wadmin.adapters.fragments.pdi.AssignPDIVehiclesAdapter;
import com.maya.wadmin.adapters.fragments.rules_and_alerts.AlertVehicleStatusAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.IAssignPDIVehiclesAdapter;
import com.maya.wadmin.interfaces.adapters.rules_and_alerts.IAlertVehicleStatusAdapter;
import com.maya.wadmin.interfaces.custom.ITopBarAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.TopBarPanel;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.VehicleStatus;
import com.maya.wadmin.utilities.CommonApiCalls;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectVehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectVehiclesFragment extends Fragment implements IFragment, IAssignPDIVehiclesAdapter, IAlertVehicleStatusAdapter, ITopBarAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    Vehicle vehicle;
    List<Vehicle> list;
    List<VehicleStatus> vehicleStatusList;
    public List<Vehicle> selectedList = new ArrayList<>();
    AssignPDIVehiclesAdapter assignPDIVehiclesAdapter;
    AlertVehicleStatusAdapter alertVehicleStatusAdapter;
    IAssignPDIVehiclesAdapter iAssignPDIVehiclesAdapter;
    IAlertVehicleStatusAdapter iAlertVehicleStatusAdapter;
    int previous = 0,otherPrevious = -1;
    int mainPosition = 0;
    List<TopBarPanel> listTopBarPanel = Utility.getTopBarPanelElements(5);

    @BindView(R.id.recyclerViewTopBar) RecyclerView recyclerViewTopBar;

    ITopBarAdapter iITopBarAdapter;

    @BindView(R.id.llTopBarPanel) LinearLayout llTopBarPanel;
    @BindView(R.id.llMainHead) LinearLayout llMainHead;
    @BindView(R.id.tvTopBarItem) TextView tvTopBarItem;

    TopBarAdapter topBarAdapter;
    List<String> vehicleIds;

    public SelectVehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectVehiclesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectVehiclesFragment newInstance(String param1, String param2) {
        SelectVehiclesFragment fragment = new SelectVehiclesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SelectVehiclesFragment newInstance(String vehiclesIds) {
        SelectVehiclesFragment fragment = new SelectVehiclesFragment();
        Bundle args = new Bundle();
        args.putString("vehiclesIds", vehiclesIds);
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
        View view = inflater.inflate(R.layout.fragment_select_vehicles, container, false);
        ButterKnife.bind(this,view);
        iAssignPDIVehiclesAdapter = this;
        iAlertVehicleStatusAdapter = this;
        iITopBarAdapter = this;
        progressBar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchAssignVehiclesForAlertAction();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tvTopBarItem.setText(listTopBarPanel.get(0).title);
        recyclerViewTopBar.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewTopBar.setAdapter(topBarAdapter = new TopBarAdapter(listTopBarPanel,activity(),iITopBarAdapter));

        tvTopBarItem.setOnClickListener(click ->
        {
            if(progressBar.getVisibility()==View.GONE)
            llTopBarPanel.setVisibility(View.VISIBLE);
            else
            {
                showSnackBar("Please wait",2);
            }
        });

        llTopBarPanel.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.GONE);
        });

        if(Utility.isNetworkAvailable(activity()))
        {
            fetchAssignVehiclesForAlertAction();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

        if(getArguments().getString("vehiclesIds")!=null)
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            vehicleIds = gson.fromJson(getArguments().getString("vehiclesIds"), type);
        }

        return view;
    }

    @Override
    public void changeTitle(String title)
    {

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

    public void fetchAssignVehiclesForAlertAction()
    {
        recyclerView.setVisibility(View.GONE);
        if(mainPosition==0)
        {
            progressBar.setVisibility(View.VISIBLE);
            String URL = Constants.URL_ALERT_PICK_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(), Constants.DEALER_ID);
            VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.d("[response]", response);

                    progressBar.setVisibility(View.GONE);

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Vehicle>>() {
                    }.getType();
                    list = gson.fromJson(response, type);
                    if (list != null)
                    {
                        if(vehicleIds!=null && vehicleIds.size()>0)
                        {
                            for (int i =0;i<list.size();i++)
                            {
                                for(int j=0;j<vehicleIds.size();j++)
                                {
                                    if(list.get(i).VehicleId.equals(vehicleIds.get(j)))
                                    {
                                        list.get(i).assignPDI = true;
                                        break;
                                    }
                                }
                            }
                        }
                        recyclerView.setBackgroundResource(R.drawable.corner_radius_white_8);
                        assignPDIVehiclesAdapter = new AssignPDIVehiclesAdapter(activity(), list, iAssignPDIVehiclesAdapter);
                        recyclerView.setAdapter(assignPDIVehiclesAdapter);
                        recyclerView.setVisibility(View.VISIBLE);

                        if(vehicleIds!=null && vehicleIds.size()>0)
                        {
                            updateList();
                        }

                    }

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError)
                {

                    Logger.d("[response]", Constants.CONNECTION_ERROR);
                    if(volleyError.networkResponse.statusCode == 401)
                    {
                        CommonApiCalls.refreshAuthTokenCall();
                        fetchAssignVehiclesForAlertAction();
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        showSnackBar(Constants.CONNECTION_ERROR, 2);
                    }
                }
            };
            volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);
        }
        else
        {
            recyclerView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.app_transparent));
            alertVehicleStatusAdapter = new AlertVehicleStatusAdapter(vehicleStatusList = Utility.generateVehiclesStatus(),activity(),iAlertVehicleStatusAdapter);
            recyclerView.setAdapter(alertVehicleStatusAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(Vehicle vehicle, int position)
    {
        if(list.get(position).assignPDI)
            list.get(position).assignPDI = false;
        else
            list.get(position).assignPDI = true;

        assignPDIVehiclesAdapter.notifyDataSetChanged();
        updateList();
    }

    @Override
    public void onItemLongClick(Vehicle vehicle, int position)
    {
        if(list.get(position).assignPDI)
            list.get(position).assignPDI = false;
        else
            list.get(position).assignPDI = true;

        assignPDIVehiclesAdapter.notifyDataSetChanged();
        updateList();
    }

    public void updateList()
    {
        selectedList = new ArrayList<>();
        for (Vehicle vehicle : list)
        {
            if(vehicle.assignPDI)
                selectedList.add(vehicle);
        }

    }

    @Override
    public void itemSelected(VehicleStatus vehicleStatus, int position)
    {
        if(vehicleStatusList.get(position).isItemSelected)
            vehicleStatusList.get(position).isItemSelected = false;
        else
            vehicleStatusList.get(position).isItemSelected = true;

        alertVehicleStatusAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemClick(TopBarPanel topBarPanel, int position) {
        setUpAction(position);
        llTopBarPanel.setVisibility(View.GONE);
        if(previous == -1)
        {
            listTopBarPanel.get(position).isSelected = true;
        }
        else
        {
            listTopBarPanel.get(previous).isSelected = false;
            listTopBarPanel.get(position).isSelected = true;
        }
        previous = position;
        tvTopBarItem.setText(topBarPanel.title);
        topBarAdapter.notifyDataSetChanged();
    }

    private void setUpAction(int position)
    {
        if(previous == position)
        {
            return;
        }
        else
        {
            switch (position)
            {
                case 0:
                    mainPosition = 0;
                    fetchAssignVehiclesForAlertAction();
                    break;
                case 1:
                    mainPosition = 1;
                    fetchAssignVehiclesForAlertAction();
                    break;

            }
        }
    }
}
