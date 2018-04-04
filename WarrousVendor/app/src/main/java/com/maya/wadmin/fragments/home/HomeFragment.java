package com.maya.wadmin.fragments.home;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.maya.wadmin.adapters.fragments.home.UserRoleAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.home.IUserRoleAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AppOverview;
import com.maya.wadmin.models.UserRole;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IFragment, IUserRoleAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<UserRole> list;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    ProgressDialog progressDialog;
    IUserRoleAdapter iUserRoleAdapter;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public HomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        iUserRoleAdapter = this;

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        progressBar.setVisibility(View.GONE);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(Utility.isNetworkAvailable(activity()))
            {
                fetchAppOverview();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        if(Utility.isNetworkAvailable(activity()))
        {
            fetchAppOverview();
        }

        if(Utility.getSharedPreferences().contains(Constants.APP_OVERVIEW))
        {
            generateUserRoles();
        }


        return view;
    }



    public void generateUserRoles()
    {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                list = Utility.getUserRole(Utility.getPortalType());

                if(list!=null && list.size()>0)
                {
                    recyclerView.setAdapter(new UserRoleAdapter(activity(),list,iUserRoleAdapter));
                }
                progressBar.setVisibility(View.GONE);
            }
        },400);

    }

    @Override
    public void changeTitle(String title)
    {

    }

    @Override
    public void showSnackBar(String snackBarText, int type)
    {
       Utility.showSnackBar(getActivity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity()
    {
        return getActivity();
    }

    @Override
    public void onUserRoleClick(UserRole userRole, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        position = position + 1;
        if(Utility.getPortalType()==0)
        {
            switch (position)
            {
                case 1:
                    intent.putExtra(Constants.FRAGMENT_KEY, 5); // VEHICLE DELIVERY
                    break;
                case 2:
                    intent.putExtra(Constants.FRAGMENT_KEY, 7); // MANGE ZONE
                    break;
                case 3:
                    intent.putExtra(Constants.FRAGMENT_KEY, 6); // RULES & ALERTS
                    break;
                case 4:
                    intent.putExtra(Constants.FRAGMENT_KEY, 4); // FIND VEHICLE
                    break;

            }
        }
        else
        {
            switch (position)
            {
                case 1:
                    intent.putExtra(Constants.FRAGMENT_KEY, 12); // FLEET MANAGEMENT
                    break;
                case 2:
                    intent.putExtra(Constants.FRAGMENT_KEY, 2); // PDI
                    break;
                case 3:
                    intent.putExtra(Constants.FRAGMENT_KEY, 3); // TEST DRIVE
                    break;
                case 4:
                    intent.putExtra(Constants.FRAGMENT_KEY, 1); // LOT MANAGEMENT
                    break;
                case 5:
                    intent.putExtra(Constants.FRAGMENT_KEY, 7); // MANAGE ZONES
                    break;
                case 6:
                    intent.putExtra(Constants.FRAGMENT_KEY, 6); // RULES & ALERTS
                    break;
                case 7:
                    intent.putExtra(Constants.FRAGMENT_KEY, 4); // FIND VEHICLE
                    break;
                case 8:
                    intent.putExtra(Constants.FRAGMENT_KEY, 333); // Create  Zone
                    break;

            }

        }
        startActivity(intent);
    }

    public void fetchAppOverview()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_APP_OVERVIEW + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<AppOverview>() {
                }.getType();

                AppOverview appOverview = gson.fromJson(response,type);
                if(appOverview!=null && appOverview.vehicleTypeCount.size()>1)
                {
                    Utility.setString(Utility.getSharedPreferences(),Constants.APP_OVERVIEW,response);
                    generateUserRoles();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

}
