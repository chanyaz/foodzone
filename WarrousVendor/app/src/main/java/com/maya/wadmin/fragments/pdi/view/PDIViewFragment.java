package com.maya.wadmin.fragments.pdi.view;


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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.pdi.AssignPDIVehiclesAdapter;
import com.maya.wadmin.adapters.fragments.pdi.SalesPersonVehiclesAdapter;
import com.maya.wadmin.adapters.fragments.vehicle.types.VehiclesTypeAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.ISalesPersonVehiclesAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.SalesPerson;
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
 * Use the {@link PDIViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PDIViewFragment extends Fragment implements IFragment,ISalesPersonVehiclesAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    int previous = -1;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    List<SalesPerson> list,finalList;
    SalesPersonVehiclesAdapter salesPersonVehiclesAdapter;
    ISalesPersonVehiclesAdapter iSalesPersonVehiclesAdapter;


    int position = 0;


    public PDIViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PDIViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PDIViewFragment newInstance(String param1, String param2) {
        PDIViewFragment fragment = new PDIViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_pdiview, container, false);
        ButterKnife.bind(this,view);

        iSalesPersonVehiclesAdapter = this;

        progressBar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));

        swipeRefreshLayout.setOnRefreshListener(() -> {

            if(Utility.isNetworkAvailable(activity()))
            {
                ((HelperActivity)activity()).clearSearchText();
                fetchVehiclesOfTestDrive();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
            swipeRefreshLayout.setRefreshing(false);
        });




        if(Utility.isNetworkAvailable(activity()))
        {
            fetchVehiclesOfTestDrive();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }




        return view;
    }


    public void fetchVehiclesOfTestDrive()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = "";
        if(mParam1==null)
        {
            URL = Constants.URL_SALESPERSON_WITH_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(), Constants.DEALER_ID);
        }
        else if(mParam1.equals("lot"))
        {
            URL = Constants.URL_SALESPERSON_WITH_LOT_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        }
        else if(mParam1.equals("fleet"))
        {
            URL = Constants.URL_SALESPERSON_WITH_LOT_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        }
        else if(mParam1.equals("testdrive"))
        {
            URL = Constants.URL_GET_TEST_DRIVE_VEHICLES_BY_SALESPERSON + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            return;
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<SalesPerson>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    finalList = list;
                    Logger.d("list contains"," "+list.size() + " "+mParam1);
                    //Logger.d("list contains",""+list.get(0).get(0).Vin);
                    if(mParam1==null)
                    {
                        salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(list,activity(),iSalesPersonVehiclesAdapter);
                    }
                    else if(mParam1.equals("lot"))
                    {
                        salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(7891012,list,activity(),iSalesPersonVehiclesAdapter);
                    }
                    else if(mParam1.equals("fleet"))
                    {
                        salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(789,list,activity(),iSalesPersonVehiclesAdapter);
                    }
                    else
                    {
                        salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(list,activity(),iSalesPersonVehiclesAdapter);
                    }
                    recyclerView.setAdapter(salesPersonVehiclesAdapter);

                }
                progressBar.setVisibility(View.GONE);

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
    public void itemClicked(SalesPerson salesPerson, int position)
    {
        if(previous==-1)
        {
            list.get(position).itemClicked = true;
        }
        else if(previous == position)
        {
            if(list.get(position).itemClicked == false)
                list.get(position).itemClicked = true;
            else
            {
                list.get(position).itemClicked = false;
            }
        }
        else
        {
            list.get(previous).itemClicked = false;
            list.get(position).itemClicked = true;
        }
        previous = position;
        salesPersonVehiclesAdapter.notifyDataSetChanged();
    }


    public void updateSalespersonBySearch(String content)
    {
        Logger.d(content);
        if(finalList!=null && finalList.size()>0)
        {
            if (content.length() > 0)
            {
                List<SalesPerson> subList = new ArrayList<>();
                for(SalesPerson salesPerson : finalList)
                {
                    if (salesPerson.Name.toLowerCase().contains(content.toLowerCase())) {
                        subList.add(salesPerson);
                    }
                }

                if (subList.size() > 0)
                {
                    list = subList;
                    previous = -1;
                    setSearchAdapter();
                    recyclerView.setAdapter(salesPersonVehiclesAdapter);
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
                setSearchAdapter();
                recyclerView.setAdapter(salesPersonVehiclesAdapter);
            }
        }
    }

    public void setSearchAdapter()
    {
        if(mParam1==null)
        {
            salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(list,activity(),iSalesPersonVehiclesAdapter);
        }
        else if(mParam1.equals("lot"))
        {
            salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(7891012,list,activity(),iSalesPersonVehiclesAdapter);
        }
        else if(mParam1.equals("fleet"))
        {
            salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(789,list,activity(),iSalesPersonVehiclesAdapter);
        }
        else
        {
            salesPersonVehiclesAdapter = new SalesPersonVehiclesAdapter(list,activity(),iSalesPersonVehiclesAdapter);
        }
    }


}
