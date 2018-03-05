package com.maya.wadmin.fragments.testdrive.add;


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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.testdrive.AssignSalesPersonAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.AssignVehicleAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.testdrive.IAssignSalesPersonAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.interfaces.fragments.testdrive.IAddNewTestDriveFragment;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignSalesPersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignSalesPersonFragment extends Fragment implements IFragment, IAssignSalesPersonAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Vehicle vehicle;
    TextView tvVin, tvOther, tvEdit;
    ImageView imgVehicle;
    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    SalesPerson salesPerson;
    AssignSalesPersonAdapter assignSalesPersonAdapter;
    List<SalesPerson> list, finalList;
    IAssignSalesPersonAdapter iAssignSalesPersonAdapter;
    int previous = -1;
    IAddNewTestDriveFragment iAddNewTestDriveFragment;
    ProgressBar progressBar;


    public AssignSalesPersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignSalesPersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignSalesPersonFragment newInstance(String param1, String param2) {
        AssignSalesPersonFragment fragment = new AssignSalesPersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AssignSalesPersonFragment newInstance(Vehicle vehicle)
    {
        AssignSalesPersonFragment fragment = new AssignSalesPersonFragment();
        Bundle args = new Bundle();
        args.putSerializable("vehicle", vehicle);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            vehicle =  (Vehicle) getArguments().getSerializable("vehicle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_sales_person, container, false);
        iAssignSalesPersonAdapter = this;
        ((HelperActivity) activity()).clearSearchText();


        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        iAddNewTestDriveFragment = (AddNewTestDriveFragment)getParentFragment();
        tvVin= view.findViewById(R.id.tvVin);
        tvOther = view.findViewById(R.id.tvOther);
        imgVehicle = view.findViewById(R.id.imgVehicle);
        tvEdit = view.findViewById(R.id.tvEdit);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);



        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        if(vehicle!=null)
        {
            tvVin.setText(vehicle.Vin.toUpperCase());
            tvOther.setText(vehicle.Make+ " " +vehicle.Model + " " + vehicle.Year);
//            Picasso.with(activity())
//                    .load(Constants.SAMPLE_IMAGE)
//                    .placeholder(R.drawable.corner_radius_hash_pool_6)
//                    .error(R.drawable.corner_radius_hash_pool_6)
//                    .into(imgVehicle);
            imgVehicle.setImageResource(R.drawable.sample_image1);
        }

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                ((HelperActivity)activity()).clearSearchText();
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchAssignSalesPersonsForTestDrive();
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
            fetchAssignSalesPersonsForTestDrive();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

        return view;
    }


    public void fetchAssignSalesPersonsForTestDrive()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_SALES_PERSONS + "?DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<SalesPerson>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    finalList = list;
                    assignSalesPersonAdapter = new AssignSalesPersonAdapter(activity(),list,iAssignSalesPersonAdapter);
                    recyclerView.setAdapter(assignSalesPersonAdapter);
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
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onItemClick(SalesPerson salesPerson, int position)
    {
        this.salesPerson = salesPerson;
        for (int i = 0;i<list.size();i++)
        {
            list.get(i).assignTestDrive = ( i == position)? true : false;
        }
        previous = position;
        assignSalesPersonAdapter.notifyDataSetChanged();
        iAddNewTestDriveFragment.addSalesPerson(salesPerson);
    }



    public void updateSalespersonBySearch(String content)
    {
        if (finalList != null && finalList.size() > 0)
        {
            if (content.length() > 0)
            {
                List<SalesPerson> subList = new ArrayList<>();
                for (SalesPerson salesPerson : finalList) {
                    if (salesPerson.Name
                            .toLowerCase().contains(content.toLowerCase())) {
                        subList.add(salesPerson);
                    }
                }

                if (subList.size() > 0) {
                    list = subList;
                    previous = -1;
                    assignSalesPersonAdapter = new AssignSalesPersonAdapter(activity(),list,iAssignSalesPersonAdapter);
                    recyclerView.setAdapter(assignSalesPersonAdapter);
                }
                else
                {
                    recyclerView.setAdapter(null);
                }
            } else {
                //Utility.hideKeyboard(activity());
                list = finalList;
                previous = -1;
                assignSalesPersonAdapter = new AssignSalesPersonAdapter(activity(),list,iAssignSalesPersonAdapter);
                recyclerView.setAdapter(assignSalesPersonAdapter);
                onItemClick(null,-1);
            }
        }
    }
}
