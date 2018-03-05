package com.maya.wadmin.fragments.pdi.add;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.testdrive.add.AssignVehicleFragment;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPDIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPDIFragment extends Fragment implements IFragment {
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
    ProgressBar progressBar;
    SalesPerson salesPerson;


    public AddPDIFragment() {
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
    public static AddPDIFragment newInstance(String param1, String param2) {
        AddPDIFragment fragment = new AddPDIFragment();
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
        View view = inflater.inflate(R.layout.fragment_assign_pdi, container, false);
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
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,AssignVehiclesPDIFragment.newInstance(null,null)).commit();
                break;
            case 2:
                if(addAndVerify())
                {
                    ((HelperActivity) activity()).clearSearchText();
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, AssignTechnicianPDIFragment.newInstance(selectedList.size())).commit();
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
                AssignTechnicianPDIFragment assignTechnicianPDIFragment = ((AssignTechnicianPDIFragment)getChildFragmentManager().getFragments().get(0));

                if(assignTechnicianPDIFragment.salesPerson!=null)
                {
                    this.salesPerson = assignTechnicianPDIFragment.salesPerson;
                    doAssignPDIVehicles();
                }
                else
                {
                    assignTechnicianPDIFragment.showSnackBar("Please Select technician",2);
                }

                break;

        }
        if(currentFragment<3)
        {
            currentFragment++;
        }



    }

    public void doActionSelectAll()
    {
        AssignVehiclesPDIFragment assignVehiclesPDIFragment = ((AssignVehiclesPDIFragment)getChildFragmentManager().getFragments().get(0));
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
        AssignVehiclesPDIFragment assignVehiclesPDIFragment = ((AssignVehiclesPDIFragment)getChildFragmentManager().getFragments().get(0));
        selectedList = assignVehiclesPDIFragment.selectedList;
        if(selectedList!=null && selectedList.size()>0)
        {

            return true;
        }
        else
        {
            assignVehiclesPDIFragment.showSnackBar("Please select vehicles",2);
            return false;
        }

    }

    public void doAssignPDIVehicles()
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

        String URL = Constants.URL_ASSIGN_VEHICLES_FOR_PDI + "?DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID) +
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

    public void updateSearchInAll(String content)
    {
        Logger.d(content);
        switch (currentFragment)
        {
            case 2:
                ((AssignVehiclesPDIFragment) getChildFragmentManager().getFragments().get(0)).updateVehiclesBySearch(content);
                break;
            case 3:
                ((AssignTechnicianPDIFragment) getChildFragmentManager().getFragments().get(0)).updateTechniciansBySearch(content);
                break;
        }

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity activity() {
        return getActivity();
    }
}
