package com.maya.wadmin.fragments.pdi.add;


import android.app.Activity;
import android.app.ProgressDialog;
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
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.pdi.AssignTechnicianPDIAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.AssignSalesPersonAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.IAssignTechnicianPDIAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignTechnicianPDIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignTechnicianPDIFragment extends Fragment implements IFragment, IAssignTechnicianPDIAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tvVehicleCount)
    TextView tvVehicleCount;

    @BindView(R.id.tvEdit)
    TextView tvEdit;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    List<SalesPerson> list, finalList;
    SalesPerson salesPerson;
    AssignTechnicianPDIAdapter assignTechnicianPDIAdapter;
    IAssignTechnicianPDIAdapter iAssignTechnicianPDIAdapter;
    int previous = -1;



    public AssignTechnicianPDIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignTechnicianPDIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignTechnicianPDIFragment newInstance(String param1, String param2) {
        AssignTechnicianPDIFragment fragment = new AssignTechnicianPDIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static AssignTechnicianPDIFragment newInstance(int vehicleCount) {
        AssignTechnicianPDIFragment fragment = new AssignTechnicianPDIFragment();
        Bundle args = new Bundle();
        args.putInt("vehicleCount", vehicleCount);
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
        View view = inflater.inflate(R.layout.fragment_assign_technician_pdi, container, false);
        ButterKnife.bind(this,view);

        iAssignTechnicianPDIAdapter = this;

        progressBar.setVisibility(View.GONE);



        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        if(getArguments().getInt("vehicleCount")!=0)
        {
            if(getArguments().getInt("vehicleCount")==1)
            {
                tvVehicleCount.setText("1 Vehicle \nselected");
            }
            else
            {
                tvVehicleCount.setText(getArguments().getInt("vehicleCount")+" Vehicles \nselected");
            }

        }

        swipeRefreshLayout.setOnRefreshListener(() ->
        {
            swipeRefreshLayout.setRefreshing(false);
            previous = -1;
            ((HelperActivity) activity()).clearSearchText();
            if(Utility.isNetworkAvailable(activity()))
            {
                fetchAssignSalesTechniciansForPDI();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        });


        if(Utility.isNetworkAvailable(activity()))
        {
            fetchAssignSalesTechniciansForPDI();
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



    public void fetchAssignSalesTechniciansForPDI()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_SALES_TECHNICIANS + "?DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
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
                    assignTechnicianPDIAdapter = new AssignTechnicianPDIAdapter(list,activity(),iAssignTechnicianPDIAdapter);
                    recyclerView.setAdapter(assignTechnicianPDIAdapter);
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
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onTechnicianClick(SalesPerson salesPerson, int position)
    {
        this.salesPerson = salesPerson;
        for(int i =0;i<list.size();i++)
        {
            if(i==position)
                list.get(i).assignPDI = true;
            else
            {
                list.get(i).assignPDI = false;
            }
        }
        previous = position;
        assignTechnicianPDIAdapter.notifyDataSetChanged();
    }



    public void updateTechniciansBySearch(String content)
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
                    assignTechnicianPDIAdapter = new AssignTechnicianPDIAdapter(list,activity(),iAssignTechnicianPDIAdapter);
                    recyclerView.setAdapter(assignTechnicianPDIAdapter);
                }
                else
                {
                    recyclerView.setAdapter(null);
                }
            } else {
                //Utility.hideKeyboard(activity());
                list = finalList;
                previous = -1;
                assignTechnicianPDIAdapter = new AssignTechnicianPDIAdapter(list,activity(),iAssignTechnicianPDIAdapter);
                recyclerView.setAdapter(assignTechnicianPDIAdapter);
                onTechnicianClick(null,-1);
            }
        }
    }
}
