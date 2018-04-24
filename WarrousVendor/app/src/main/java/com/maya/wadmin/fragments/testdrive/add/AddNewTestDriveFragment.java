package com.maya.wadmin.fragments.testdrive.add;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.interfaces.fragments.testdrive.IAddNewTestDriveFragment;
import com.maya.wadmin.models.Customer;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewTestDriveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewTestDriveFragment extends Fragment implements IFragment , IAddNewTestDriveFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.frameLayout) FrameLayout frameLayout;

    @BindView(R.id.tvNext) TextView tvNext;
    @BindView(R.id.tvOne) TextView tvOne;
    @BindView(R.id.tvTwo) TextView tvTwo;
    @BindView(R.id.tvThree) TextView tvThree;
    @BindView(R.id.tvFour) TextView tvFour;

    Vehicle vehicle;
    SalesPerson salesPerson;
    Customer customer;

    public int currentFragment = 1;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    public AddNewTestDriveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewTestDriveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewTestDriveFragment newInstance(String param1, String param2) {
        AddNewTestDriveFragment fragment = new AddNewTestDriveFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_new_test_drive, container, false);
        ButterKnife.bind(this,view);



        tvNext.setOnClickListener(v -> {
            addFragment();
        });

        addFragment();


        return view;
    }


    public void createTestDrive()
    {

        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_SAVE_TEST_DRIVE + "?DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID) +
                "&vehicleId=" + vehicle.VehicleId +"&salesPersonId=" + salesPerson.SalesPersonId + "&customerId=" + customer.CustomerId;
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

    public void addFragment()
    {
        switch (currentFragment)
        {
            case 1:
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,AssignVehicleFragment.newInstance(null,null)).commit();
                break;
            case 2:
                if(vehicle==null)
                {
                    ((AssignVehicleFragment)getChildFragmentManager().getFragments().get(0)).showSnackBar("Please select vehicle",2);
                    return;
                }
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,AssignSalesPersonFragment.newInstance(vehicle)).commit();
                //((HelperActivity) activity()).clearSearchText();
                break;
            case 3:
                if(salesPerson==null)
                {
                    ((AssignSalesPersonFragment)getChildFragmentManager().getFragments().get(0)).showSnackBar("Please select salesperson",2);
                    return;
                }
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,AssignCustomerFragment.newInstance(vehicle,salesPerson)).commit();
                //((HelperActivity) activity()).clearSearchText();
                break;
            case 4:
                if(customer==null)
                {
                    ((AssignCustomerFragment)getChildFragmentManager().getFragments().get(0)).showSnackBar("Please select customer",2);
                    return;
                }
                tvNext.setText("FINISH");
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,AssignRouteFragment.newInstance(vehicle,salesPerson,customer)).commit();
                //((HelperActivity) activity()).clearSearchText();
                break;
            case 5:
                createTestDrive();
                break;
        }
        changeViewStyle();
        if(currentFragment<5)
        {
            currentFragment++;
        }



    }

    public void changeViewStyle()
    {
        switch (currentFragment)
        {
            case 1:
                tvOne.setBackgroundResource(R.drawable.circle_red);
                tvTwo.setBackgroundResource(R.drawable.unselect_circle);
                tvThree.setBackgroundResource(R.drawable.unselect_circle);
                tvFour.setBackgroundResource(R.drawable.unselect_circle);
                break;
            case 2:
                tvOne.setBackgroundResource(R.drawable.unselect_circle);
                tvTwo.setBackgroundResource(R.drawable.circle_red);
                tvThree.setBackgroundResource(R.drawable.unselect_circle);
                tvFour.setBackgroundResource(R.drawable.unselect_circle);
                break;
            case 3:
                tvOne.setBackgroundResource(R.drawable.unselect_circle);
                tvTwo.setBackgroundResource(R.drawable.unselect_circle);
                tvThree.setBackgroundResource(R.drawable.circle_red);
                tvFour.setBackgroundResource(R.drawable.unselect_circle);
                break;
            case 4:
                tvOne.setBackgroundResource(R.drawable.unselect_circle);
                tvTwo.setBackgroundResource(R.drawable.unselect_circle);
                tvThree.setBackgroundResource(R.drawable.unselect_circle);
                tvFour.setBackgroundResource(R.drawable.circle_red);
                break;
        }
    }

    @Override
    public void addVehicle(Vehicle vehicle)
    {
        this.vehicle = vehicle;
    }

    @Override
    public void addSalesPerson(SalesPerson salesPerson)
    {
        this.salesPerson = salesPerson;
    }

    @Override
    public void addCustomer(Customer customer) {
        this.customer = customer;
    }


    public void updateSearchInAll(String content)
    {
        Logger.d(content);
        switch(currentFragment)
        {
            case 2:
                ((AssignVehicleFragment) getChildFragmentManager().getFragments().get(0)).updateSearchByVehicles(content);
                break;
            case 3:
                ((AssignSalesPersonFragment) getChildFragmentManager().getFragments().get(0)).updateSalespersonBySearch(content);
                break;
            case 4:
                ((AssignCustomerFragment) getChildFragmentManager().getFragments().get(0)).updateCustomersBySearch(content);
                break;
        }
    }
}
