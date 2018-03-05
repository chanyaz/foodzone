package com.maya.wadmin.fragments.rules_and_alerts.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.rules_and_alerts.AlertsAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.AssignVehicleAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.rules_and_alerts.IAlertsAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertsFragment extends Fragment implements IFragment, IAlertsAdapter {
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
    List<AlertRule> finalList,list;
    AlertsAdapter alertsAdapter;
    IAlertsAdapter iAlertsAdapter;
    int CategoryId = 0;

    public AlertsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertsFragment newInstance(String param1, String param2) {
        AlertsFragment fragment = new AlertsFragment();
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
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        iAlertsAdapter = this;

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerView.setNestedScrollingEnabled(true);

        if(getArguments()!=null)
        {
            CategoryId = getArguments().getInt("CategoryId");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchAlertRules();
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
            fetchAlertRules();
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
    public Activity activity()
    {
        return getActivity();
    }


    public void fetchAlertRules()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ALERTS_LIST_BASED_ON_CATEGORY + CategoryId  + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<AlertRule>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    alertsAdapter = new AlertsAdapter(list,activity(),iAlertsAdapter);
                    recyclerView.setAdapter(alertsAdapter);
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


    public void fetchAlertDetails(AlertRule alertRule)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ALERT_DETAILS + alertRule.AlertId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<AlertRule>() {
                }.getType();
                AlertRule alertRuleDetails = gson.fromJson(response, type);
                if(alertRuleDetails!=null)
                {
                    Intent intent = new Intent(activity(),HelperActivity.class);
                    intent.putExtra(Constants.FRAGMENT_KEY, 611);
                    intent.putExtra("AlertRule",alertRuleDetails);
                    startActivityForResult(intent,Utility.generateRequestCodes().get("ADD_ALERT"));
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



    public void updateAlertsBySearch(String content)
    {
        Logger.d(content);
        if(finalList!=null && finalList.size()>0)
        {

            if (content.length() > 0)
            {
                List<AlertRule> subList = new ArrayList<>();
                for(AlertRule alertRule : finalList) {
                    if ((alertRule.AlertDescription + " " + alertRule.AlertName + " " + alertRule.AlertDescription)
                            .toLowerCase().contains(content.toLowerCase())) {
                        subList.add(alertRule);
                    }
                }

                    if (subList.size() > 0)
                    {
                        list = subList;
                        alertsAdapter = new AlertsAdapter(list,activity(),iAlertsAdapter);
                        recyclerView.setAdapter(alertsAdapter);
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
                alertsAdapter = new AlertsAdapter(list,activity(),iAlertsAdapter);
                recyclerView.setAdapter(alertsAdapter);
            }
        }
    }


    @Override
    public void cloneAlert(AlertRule alertRule, int position) {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_CLONE_ALERT + alertRule.AlertId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                fetchAlertRules();
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
    public void edit(AlertRule alertRule, int position)
    {
        if(Utility.isNetworkAvailable(activity()))
        {
           fetchAlertDetails(alertRule);
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

    }

    @Override
    public void openPopUpOptions(View view, AlertRule alertRule, int position)
    {
        PopupMenu popup = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {
            popup = new PopupMenu(activity(),view, Gravity.END);
        }
        else
        {
            popup = new PopupMenu(activity(),view);
        }
        // Inflate the menu from xml
        popup.inflate(R.menu.alert_options);
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_clone:
                        if(Utility.isNetworkAvailable(activity()))
                        cloneAlert(alertRule,position);
                        else
                        showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                        return true;
                    case R.id.nav_edit:
                        edit(alertRule,position);
                        return true;
                    case R.id.nav_delete:
                        if(Utility.isNetworkAvailable(activity()))
                        deleteAlert(alertRule,position);
                        else
                        showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                        return true;
                    case R.id.nav_violations:
                        if(Utility.isNetworkAvailable(activity()))
                        openViolationVehicles(alertRule,position);
                        else
                        showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();

    }

    @Override
    public void deleteAlert(AlertRule alertRule, int position)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_DELETE_ALERT + alertRule.AlertId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                fetchAlertRules();
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
    public void openViolationVehicles(AlertRule alertRule, int position)
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,612);
        intent.putExtra("AlertRule",alertRule);
        startActivity(intent);
    }
}
