package com.maya.wadmin.fragments.testdrive.add;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.CustomCap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.testdrive.AssignCustomerAdapter;
import com.maya.wadmin.adapters.fragments.testdrive.AssignSalesPersonAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.testdrive.IAssignCustomerAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.interfaces.fragments.testdrive.IAddNewTestDriveFragment;
import com.maya.wadmin.models.Customer;
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
 * Use the {@link AssignCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignCustomerFragment extends Fragment implements IFragment, IAssignCustomerAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    CoordinatorLayout coordinatorLayout;
    NestedScrollView nestedScrollView;
    EditText etFirstName, etLastName, etEmail, etPhoneNumber;
    TextView tvAddCustomer;
    TextView tvSalesPerson, tvRole, tvVehicleCount;
    ImageView imgSalesPerson,imgVehicle;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    Vehicle vehicle;
    SalesPerson salesPerson;
    Customer customer;
    List<Customer> list, finalList;
    AssignCustomerAdapter assignCustomerAdapter;
    IAssignCustomerAdapter iAssignCustomerAdapter;
    int previous = -1;
    IAddNewTestDriveFragment iAddNewTestDriveFragment;
    boolean isAdded = false;
    ProgressBar progressBar;


    public AssignCustomerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignCustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignCustomerFragment newInstance(String param1, String param2) {
        AssignCustomerFragment fragment = new AssignCustomerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AssignCustomerFragment newInstance(Vehicle vehicle, SalesPerson salesPerson)
    {
        AssignCustomerFragment fragment = new AssignCustomerFragment();
        Bundle args = new Bundle();
        args.putSerializable("vehicle", vehicle);
        args.putSerializable("salesPerson", salesPerson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
            salesPerson = (SalesPerson)getArguments().getSerializable("salesPerson");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_customer, container, false);
        iAssignCustomerAdapter = this;
        ((HelperActivity) activity()).clearSearchText();

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        iAddNewTestDriveFragment = (AddNewTestDriveFragment)getParentFragment();
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvSalesPerson = view.findViewById(R.id.tvSalesPerson);
        tvRole = view.findViewById(R.id.tvRole);
        tvVehicleCount = view.findViewById(R.id.tvVehicleCount);
        tvAddCustomer = view.findViewById(R.id.tvAddCustomer);
        etEmail = view.findViewById(R.id.etEmail);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        imgVehicle = view.findViewById(R.id.imgVehicle);
        imgSalesPerson = view.findViewById(R.id.imgSalesPerson);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));




        tvAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.hideKeyboard(activity());
                if(Utility.isNetworkAvailable(activity()))
                {
                    checkCustomer();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
            }
        });



        if(vehicle!=null && salesPerson!=null)
        {
//            Picasso.with(activity())
//                    .load(Constants.SAMPLE_IMAGE)
//                    .placeholder(R.drawable.corner_radius_hash_pool_6)
//                    .error(R.drawable.corner_radius_hash_pool_6)
//                    .into(imgVehicle);

            imgVehicle.setImageResource(R.drawable.sample_image1);

            Picasso.with(activity())
                    .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                    .into(imgSalesPerson);

            tvSalesPerson.setText(salesPerson.Name);
            if(salesPerson.Type!=null)
            tvRole.setText(salesPerson.Name);
            else
            {
                tvRole.setText(Constants.SALES_PERSON);
            }

            tvVehicleCount.setText("1 Vehicle Selected");


        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                ((HelperActivity)activity()).clearSearchText();
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchAssignCustomersForTestDrive();
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
            fetchAssignCustomersForTestDrive();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }



        return view;
    }



    public void fetchAssignCustomersForTestDrive()
    {
        previous = -1;
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_FETCH_TEST_DRIVE_CUSTOMERS + "?DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Customer>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    finalList = list;
                    if(isAdded==true)
                    {
                        list.get(0).assignTestDrive = true;
                        isAdded = false;
                        assignCustomerAdapter = new AssignCustomerAdapter(activity(), list, iAssignCustomerAdapter);
                        recyclerView.setAdapter(assignCustomerAdapter);
                        onClickCustomer(list.get(0),0);
                    }
                    else
                    {
                        assignCustomerAdapter = new AssignCustomerAdapter(activity(), list, iAssignCustomerAdapter);
                        recyclerView.setAdapter(assignCustomerAdapter);
                    }
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

    public void checkCustomer()
    {
        String firstName = ""+etFirstName.getText().toString().trim();
        String lastName = ""+etLastName.getText().toString().trim();
        String email = ""+etEmail.getText().toString().trim();
        String phone = ""+etPhoneNumber.getText().toString().trim();

        if(firstName.length()>0 && lastName.length()>0 && email.length()>0 && phone.length()==10)
        {
            addCustomer();
        }
        else
        {
            showSnackBar("Please fill the fields",2);
        }
    }

    public void addCustomer(){
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_SAVE_CUSTOMER + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID)
                + "&firstName=" + etFirstName.getText().toString().trim() + "&lastName=" + etLastName.getText().toString().trim() + "&phoneNumber=" + etPhoneNumber.getText().toString().trim() + "&email=" + etEmail.getText().toString().trim();
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                try
                {
                    int customerId = Integer.parseInt(response);
                    if(customerId>0)
                    {
                        showSnackBar("Successfully added customer",1);
                        isAdded = true;
                        fetchAssignCustomersForTestDrive();
                    }
                    else
                    {
                        showSnackBar("Failed to added customer",0);
                    }
                }
                catch (Exception e)
                {
                    showSnackBar("Failed to added customer",0);
                }

                etFirstName.setText("");
                etLastName.setText("");
                etEmail.setText("");
                etPhoneNumber.setText("");

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


    @Override
    public void onClickCustomer(Customer customer, int position)
    {
        this.customer = customer;
        for (int i = 0; i < list.size() ;i++)
        {
            list.get(i).assignTestDrive = (i==position) ? true:false;
        }
        previous = position;
        assignCustomerAdapter.notifyDataSetChanged();
        iAddNewTestDriveFragment.addCustomer(customer);
    }



    public void updateCustomersBySearch(String content)
    {
        if (finalList != null && finalList.size() > 0)
        {
            if (content.length() > 0)
            {
                List<Customer> subList = new ArrayList<>();
                for (Customer customer : finalList) {
                    if ((customer.FullName + " " + customer.EmailAddress + " " + customer.PhoneNumber)
                            .toLowerCase().contains(content.toLowerCase()))
                    {
                        subList.add(customer);
                    }
                }

                if (subList.size() > 0) {
                    list = subList;
                    previous = -1;
                    assignCustomerAdapter = new AssignCustomerAdapter(activity(), list, iAssignCustomerAdapter);
                    recyclerView.setAdapter(assignCustomerAdapter);
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
                assignCustomerAdapter = new AssignCustomerAdapter(activity(), list, iAssignCustomerAdapter);
                recyclerView.setAdapter(assignCustomerAdapter);
                onClickCustomer(null,-1);
            }
        }
    }
}
