package com.maya.vgarages.fragments.vehicle.add;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.fragments.vehicle.AddVehicleAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.vehicle.IAddVehicleAdapter;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.vehicle.VehicleDetails;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddVehicleFragment extends Fragment implements IFragment, IAddVehicleAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerViewMake)
    RecyclerView recyclerViewMake;

    @BindView(R.id.recyclerViewModel)
    RecyclerView recyclerViewModel;

    @BindView(R.id.recyclerViewYear)
    RecyclerView recyclerViewYear;

    VehicleDetails vehicleDetails;
    IAddVehicleAdapter iAddVehicleAdapter;
    AddVehicleAdapter addVehicleAdapterYear, addVehicleAdapterMake, addVehicleAdapterModel;

    public VehicleDetails.Make make;
    public VehicleDetails.Model model;
    public VehicleDetails.Year year;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvMake)
    TextView tvMake;

    @BindView(R.id.tvModel)
    TextView tvModel;

    @BindView(R.id.tvYear)
    TextView tvYear;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.etVehicleName)
    EditText etVehicleName;

    @BindView(R.id.imgCreate)
    ImageView imgCreate;

    @BindView(R.id.llApply)
    LinearLayout llApply;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;











    public AddVehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddVehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddVehicleFragment newInstance(String param1, String param2) {
        AddVehicleFragment fragment = new AddVehicleFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        iAddVehicleAdapter = this;
        ButterKnife.bind(this,view);


        initialize();
        return view;
    }

    private void initialize()
    {
        progressBar.setVisibility(View.GONE);
        vehicleDetails = new VehicleDetails();
        hideThings(1);
        recyclerViewMake.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewModel.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewYear.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        if(Utility.isNetworkAvailable(activity()))
        {
            fetchMakes();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }

        imgCreate.setOnClickListener(v -> {
            Utility.hideKeyboard(activity());
            if((""+etVehicleName.getText()).trim().length()>0)
            {
                if(Utility.isNetworkAvailable(activity()))
                {
                    insertUserVehicle();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
                }
            }
            else
            {
                showSnackBar("You miss vehicle name",2);
            }
        });
    }

    private void insertUserVehicle()
    {
        progressBar.setVisibility(View.VISIBLE);
        imgCreate.setVisibility(View.GONE);
        String URL = Constants.URL_SAVE_VEHICLE + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&makeModelYearId="+year.MakeModelYearId+"&vehicleName="+etVehicleName.getText().toString().trim();
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                if(response.equals("true"))
                {
                    ((HelperActivity)activity()).updateUserVehicles();
                }
                else
                {
                    showSnackBar(Constants.ERROR,2);
                }
                progressBar.setVisibility(View.GONE);
                imgCreate.setVisibility(View.VISIBLE);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
                imgCreate.setVisibility(View.VISIBLE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public void hideThings(int value)
    {
        llApply.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
        switch (value)
        {
            case 1:
                tvTitle.setText("SELECT MAKE");
                recyclerViewMake.setVisibility(View.VISIBLE);
                tvMake.setVisibility(View.VISIBLE);
                recyclerViewModel.setVisibility(View.GONE);
                tvModel.setVisibility(View.GONE);
                recyclerViewYear.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
                break;
            case 2:
                tvTitle.setText("SELECT MODEL");
                recyclerViewMake.setVisibility(View.GONE);
                tvMake.setVisibility(View.GONE);
                recyclerViewModel.setVisibility(View.VISIBLE);
                tvModel.setVisibility(View.VISIBLE);
                recyclerViewYear.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
                break;
            case 3:
                tvTitle.setText("SELECT YEAR");
                recyclerViewMake.setVisibility(View.GONE);
                tvMake.setVisibility(View.GONE);
                recyclerViewModel.setVisibility(View.GONE);
                tvModel.setVisibility(View.GONE);
                recyclerViewYear.setVisibility(View.VISIBLE);
                tvYear.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvTitle.setText("ENTER VEHICLE NAME");
                recyclerViewMake.setVisibility(View.GONE);
                tvMake.setVisibility(View.GONE);
                recyclerViewModel.setVisibility(View.GONE);
                tvModel.setVisibility(View.GONE);
                recyclerViewYear.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
                llApply.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void fetchMakes()
    {
        recyclerViewMake.setAdapter(addVehicleAdapterMake = new AddVehicleAdapter(vehicleDetails,activity(),1,iAddVehicleAdapter,true));
        String URL = Constants.URL_GET_MAKES;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<VehicleDetails.Make>>() {
                }.getType();
                vehicleDetails.makeList = gson.fromJson(response, type);
                recyclerViewMake.setAdapter(addVehicleAdapterMake = new AddVehicleAdapter(vehicleDetails,activity(),1,iAddVehicleAdapter,false));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    private void fetchModels() {
        recyclerViewModel.setAdapter(addVehicleAdapterModel = new AddVehicleAdapter(vehicleDetails,activity(),2,iAddVehicleAdapter,true));
        String URL = Constants.URL_GET_MAKE_MODELS + "?make="+make.Make;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<VehicleDetails.Model>>() {
                }.getType();
                vehicleDetails.modelList = gson.fromJson(response, type);
                recyclerViewModel.setAdapter(addVehicleAdapterModel = new AddVehicleAdapter(vehicleDetails,activity(),2,iAddVehicleAdapter,false));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    private void fetchYears()
    {
        recyclerViewYear.setAdapter(addVehicleAdapterYear = new AddVehicleAdapter(vehicleDetails,activity(),3,iAddVehicleAdapter,true));
        String URL = Constants.URL_GET_MODEL_YEARS + "?model="+model.Model;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<VehicleDetails.Year>>() {
                }.getType();
                vehicleDetails.yearList = gson.fromJson(response, type);
                recyclerViewYear.setAdapter(addVehicleAdapterYear = new AddVehicleAdapter(vehicleDetails,activity(),3,iAddVehicleAdapter,false));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
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
    public void onItemClick(int type, int position)
    {
        switch (type)
        {
            case 1:
                for(int i =0; i<vehicleDetails.makeList.size(); i++)
                vehicleDetails.makeList.get(i).isSelected = false;

                vehicleDetails.makeList.get(position).isSelected = true;
                addVehicleAdapterMake.notifyDataSetChanged();
                make = vehicleDetails.makeList.get(position);

                recyclerViewYear.setAdapter(null);
                hideThings(2);
                fetchModels();
                break;
            case 2:
                for(int i =0; i<vehicleDetails.modelList.size(); i++)
                vehicleDetails.modelList.get(i).isSelected = false;

                vehicleDetails.modelList.get(position).isSelected = true;
                addVehicleAdapterModel.notifyDataSetChanged();
                model = vehicleDetails.modelList.get(position);
                hideThings(3);
                fetchYears();

                break;
            case 3:
                for(int i =0; i<vehicleDetails.yearList.size(); i++)
                vehicleDetails.yearList.get(i).isSelected = false;

                vehicleDetails.yearList.get(position).isSelected = true;
                addVehicleAdapterYear.notifyDataSetChanged();
                year = vehicleDetails.yearList.get(position);

                hideThings(4);


                break;
        }

    }
}
