package com.maya.vgarages.fragments.vehicle;


import android.app.Activity;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.vehicle.VehicleAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.vehicle.IVehicleAdapter;
import com.maya.vgarages.interfaces.dialog.IAddVehicleDialog;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Vehicle;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserVehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserVehiclesFragment extends Fragment implements IFragment, IVehicleAdapter,IAddVehicleDialog {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    boolean isSelected = false;
    int flag = 0;

    List<Vehicle> list;

    IVehicleAdapter iVehicleAdapter;
    IAddVehicleDialog iAddVehicleDialog;
    VehicleAdapter vehicleAdapter;

    public UserVehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserVehiclesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserVehiclesFragment newInstance(String param1, String param2) {
        UserVehiclesFragment fragment = new UserVehiclesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserVehiclesFragment newInstance(boolean isSelected,int flag) {
        UserVehiclesFragment fragment = new UserVehiclesFragment();
        Bundle args = new Bundle();
        args.putBoolean("isSelected", isSelected);
        args.putInt("flag", flag);
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
        View view = inflater.inflate(R.layout.fragment_user_vehicles, container, false);
        iVehicleAdapter = this;
        iAddVehicleDialog = this;
        ButterKnife.bind(this,view);


        initialize();
        return view;
    }

    private void initialize()
    {

        isSelected = getArguments().getBoolean("isSelected",true);
        flag = getArguments().getInt("flag");

        tvTitle.setText("PICK VEHICLE");
        tvTitle.setVisibility(flag == 1 ? View.VISIBLE: View.GONE);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if(Utility.isNetworkAvailable(activity()))
            {
                fetchUserVehicles();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        if(Utility.isNetworkAvailable(activity()))
        {
            fetchUserVehicles();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }
    }

    public void fetchUserVehicles()
    {
        recyclerView.setAdapter(new VehicleAdapter(null,iVehicleAdapter,activity(),true,true));
        String URL = Constants.URL_USER_VEHICLES + "?userId="+ Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID);
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
                    recyclerView.setAdapter(vehicleAdapter = new VehicleAdapter(list,iVehicleAdapter,activity(),flag == 1? false : true ,false));
                    updateVehicles();
                }
                else
                {
                    recyclerView.setAdapter(new EmptyDataAdapter(activity(),1));
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                recyclerView.setAdapter(new EmptyDataAdapter(activity(),0));
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
    public void deleteItem(Vehicle vehicle, int position)
    {
        if(Utility.isNetworkAvailable(activity()))
        {
            deleteUserVehicle(vehicle, position);
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }
    }

    @Override
    public void selectItem(Vehicle vehicle, int position)
    {
        ((HelperActivity) activity()).confirmVehicleDialog(vehicle,iAddVehicleDialog);
    }

    private void deleteUserVehicle(Vehicle vehicle,int position)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_DELETE_VEHICLE + "?userId=" +
                Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID) + "&vehicleId="+  vehicle.VehicleId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                list.remove(position);
                vehicleAdapter.notifyDataSetChanged();
                if(Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
                {
                    Gson gson = new Gson();
                    Type type = new TypeToken<Vehicle>() {
                    }.getType();
                    Vehicle defaultVehicle = gson.fromJson(Utility.getString(Utility.getSharedPreferences(), Constants.DEFAULT_CAR_DATA), type);
                    if (vehicle != null) {
                        if (vehicle.VehicleId == defaultVehicle.VehicleId) {
                            Utility.del(Utility.getSharedPreferences(), Constants.DEFAULT_CAR_DATA);
                        }
                    }
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

    public void updateVehicles()
    {
        if(Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
        {
            Gson gson = new Gson();
            Type type = new TypeToken<Vehicle>(){}.getType();
            Vehicle vehicle = gson.fromJson(Utility.getString(Utility.getSharedPreferences(),Constants.DEFAULT_CAR_DATA),type);
            if(vehicle!=null)
            {
                if(vehicle.VehicleId!=0)
                {
                    for(int i=0;i<list.size();i++)
                    {
                        if(list.get(i).VehicleId == vehicle.VehicleId)
                        {
                            list.get(i).isDefault = true;
                        }
                        else
                        {
                            list.get(i).isDefault = false;
                        }
                    }

                    vehicleAdapter.notifyDataSetChanged();
                }
            }

        }
    }

    @Override
    public void makeDefaultVehicle(Vehicle vehicle)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Vehicle>(){}.getType();
        Utility.setString(Utility.getSharedPreferences(),Constants.DEFAULT_CAR_DATA,gson.toJson(vehicle,type));
        updateVehicles();
        if(!isSelected) return;

        Intent returnData = new Intent(activity(),HelperActivity.class);
        returnData.putExtra("data","1");
        activity().setResult(Activity.RESULT_OK,returnData);
        activity().finish();
    }
}
