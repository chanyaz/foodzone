package com.maya.wadmin.fragments.lot.add;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPreparingLotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPreparingLotFragment extends Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    CoordinatorLayout coordinatorLayout;
    public int currentFragment = 1;
    LinearLayout llSelectAll, llNext;
    TextView tvNext;
    ImageView imgNextArrow;
    List<Vehicle> selectedList;
    SalesPerson salesPerson;
    ProgressBar progressBar;


    public AddPreparingLotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPDIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPreparingLotFragment newInstance(String param1, String param2) {
        AddPreparingLotFragment fragment = new AddPreparingLotFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_preparing_lot, container, false);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        llSelectAll = view.findViewById(R.id.llSelectAll);
        llNext = view.findViewById(R.id.llNext);
        tvNext = view.findViewById(R.id.tvNext);
        imgNextArrow = view.findViewById(R.id.imgNextArrow);
        progressBar = view.findViewById(R.id.progressBar);


        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });

        llSelectAll.setOnClickListener( click -> doActionSelectAll());




        addFragment();


        return view;
    }


    public void addFragment()
    {
        switch (currentFragment)
        {
            case 1:
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, AssignVehiclesLotFragment.newInstance(null,null)).commit();
                break;
            case 2:
                if(addAndVerify())
                {
                    ((HelperActivity) activity()).clearSearchText();
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, AssignTechnicianLotFragment.newInstance(selectedList.size())).commit();
                    tvNext.setText("Assign");
                    llSelectAll.setVisibility(View.GONE);
                    imgNextArrow.setVisibility(View.GONE);
                }
                else
                {
                    return;
                }
                break;
            case 3:
                AssignTechnicianLotFragment assignTechnicianFragment = ((AssignTechnicianLotFragment)getChildFragmentManager().getFragments().get(0));

                if(assignTechnicianFragment.salesPerson!=null)
                {
                    salesPerson = assignTechnicianFragment.salesPerson;
                    if(Utility.isNetworkAvailable(activity()))
                    {
                        doAssignPreparingVehicles();
                    }
                    else
                    {
                        showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                    }
                }
                else
                {
                    assignTechnicianFragment.showSnackBar("Please Select technician",2);
                }

                break;

        }
        if(currentFragment<3)
        {
            currentFragment++;
        }



    }

    public void doAssignPreparingVehicles()
    {

        progressBar.setVisibility(View.VISIBLE);
        String value = "";
        for(int i=0;i<selectedList.size();i++)
        {
            if(i==selectedList.size()-1)
            {
                value += selectedList.get(i).VehicleId;
                Logger.d(value);
            }
            else
            value += selectedList.get(i).VehicleId + ",";
        }

        String URL = Constants.URL_PREPARE_VEHICLES_FOR_LOT + "?DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID) +
                "&vehicleIds=" + value +"&salesPersonId=" + salesPerson.SalesPersonId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                Intent returnData = new Intent(activity(),HelperActivity.class);
                returnData.putExtra("data","1");
                activity().setResult(Activity.RESULT_OK,returnData);
                activity().finish();

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

    public void doActionSelectAll()
    {
        AssignVehiclesLotFragment assignVehiclesPDIFragment = ((AssignVehiclesLotFragment)getChildFragmentManager().getFragments().get(0));
        if(assignVehiclesPDIFragment.list!=null && assignVehiclesPDIFragment.list.size()>0)
        {
            for(int i =0;i<assignVehiclesPDIFragment.list.size();i++)
            {
                assignVehiclesPDIFragment.list.get(i).assignPDI = true;
            }
            assignVehiclesPDIFragment.selectedList = assignVehiclesPDIFragment.list;
            assignVehiclesPDIFragment.assignPDIVehiclesAdapter.notifyDataSetChanged();
        }

    }

    public boolean addAndVerify()
    {
        AssignVehiclesLotFragment assignVehiclesLotFragment = ((AssignVehiclesLotFragment)getChildFragmentManager().getFragments().get(0));
        selectedList = assignVehiclesLotFragment.selectedList;
        if(selectedList!=null && selectedList.size()>0)
        {

            return true;
        }
        else
        {
            assignVehiclesLotFragment.showSnackBar("Please select vehicles",2);
            return false;
        }

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


    public void updateSearchInAll(String content)
    {
        Logger.d(content);
        switch (currentFragment)
        {
            case 2:
                ((AssignVehiclesLotFragment) getChildFragmentManager().getFragments().get(0)).updateVehiclesBySearch(content);
                break;
            case 3:
                ((AssignTechnicianLotFragment) getChildFragmentManager().getFragments().get(0)).updateSearchInTechnician(content);
                break;
        }
    }
}
