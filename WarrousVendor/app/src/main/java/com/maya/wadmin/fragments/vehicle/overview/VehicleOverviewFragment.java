package com.maya.wadmin.fragments.vehicle.overview;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.testdrive.AssignSalesPersonAdapter;
import com.maya.wadmin.adapters.fragments.vehicle.overview.DTCDiagnosticsAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.DTCDiagnostics;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehicleOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleOverviewFragment extends Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Vehicle vehicle;
    CoordinatorLayout coordinatorLayout;
    ObjectAnimator animLeftRight;
    ObjectAnimator animReverse;
    int animationDuration = 2000;
    int animationTimes = 0;
    public View viewScanLeftRight;
    public View viewScanRightLeft;
    private int timeSet = 0;
    public int openFlag = 0;
    TextView tvScan, tvStatus;
    List<DTCDiagnostics> list;
    RecyclerView recyclerView;
    DTCDiagnosticsAdapter dtcDiagnosticsAdapter;
    RelativeLayout rlFinishScan, llhead3;
    LinearLayout llhead2;
    ScrollView scrollView;
    ProgressBar progressBar;
    TextView  tvModel, tvVin, tvMake, tvYear, tvEngineNumber;
    TextView tvColorExterior, tvInterior, tvBodyStyle, tvOdometerStatus, tvTransmissionType, tvDoorQuantity;


    public VehicleOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehicleOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleOverviewFragment newInstance(String param1, String param2) {
        VehicleOverviewFragment fragment = new VehicleOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static VehicleOverviewFragment newInstance(Vehicle vehicle) {
        VehicleOverviewFragment fragment = new VehicleOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable("vehicle", vehicle);
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
        View view = inflater.inflate(R.layout.fragment_vehicle_overview, container, false);
        if(getArguments().getSerializable("vehicle")!=null)
        {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
        }
        else {
            vehicle = new Vehicle();
            vehicle.VehicleId = "17";
            vehicle.Type = "In Test Drive";
        }
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        progressBar = view.findViewById(R.id.progressBar);
        viewScanLeftRight = view.findViewById(R.id.view_scan_bar_left_right);
        viewScanRightLeft = view.findViewById(R.id.view_scan_bar_right_left);
        tvScan = view.findViewById(R.id.tvScan);
        tvStatus = view.findViewById(R.id.tvStatus);
        recyclerView = view.findViewById(R.id.recyclerView);
        rlFinishScan = view.findViewById(R.id.rlFinishScan);
        llhead2 = view.findViewById(R.id.llhead2);
        llhead3 = view.findViewById(R.id.llhead3);
        scrollView = view.findViewById(R.id.scrollView);
        vehicleIdsSetUp(view);




        progressBar.setVisibility(View.GONE);
        llhead2.setVisibility(View.GONE);
        llhead3.setVisibility(View.GONE);
        rlFinishScan.setVisibility(View.GONE);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);


        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        viewScanLeftRight.setVisibility(View.INVISIBLE);
        viewScanRightLeft.setVisibility(View.INVISIBLE);
        setUpFirst();

        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Utility.isNetworkAvailable(activity()))
                {

                    tvStatus.setText("Scaning...");
                    tvStatus.setBackgroundResource(R.drawable.corner_radius_primary_10);
                    tvScan.setVisibility(View.INVISIBLE);
                    animate();
                    fetchDTCCodeOfVehicles();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
            }
        });

        rlFinishScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                scrollView.fullScroll(View.FOCUS_UP);
                scrollView.smoothScrollTo(0,0);
                rlFinishScan.setVisibility(View.GONE);
                tvScan.setVisibility(View.VISIBLE);
                llhead3.setVisibility(View.GONE);
                llhead2.setVisibility(View.VISIBLE);

            }
        });

        if(Utility.isNetworkAvailable(activity()))
        {
            fetchExtraDetailsOfVehicles();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

        return view;
    }


    private void vehicleIdsSetUp(View view)
    {
        tvModel = view.findViewById(R.id.tvModel);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvVin = view.findViewById(R.id.tvVin);
        tvMake = view.findViewById(R.id.tvMake);
        tvYear = view.findViewById(R.id.tvYear);
        tvColorExterior = view.findViewById(R.id.tvColorExterior);
        tvInterior = view.findViewById(R.id.tvInterior);
        tvBodyStyle = view.findViewById(R.id.tvBodyStyle);
        tvOdometerStatus = view.findViewById(R.id.tvOdometerStatus);
        tvTransmissionType = view.findViewById(R.id.tvTransmissionType);
        tvDoorQuantity = view.findViewById(R.id.tvDoorQuantity);
        tvEngineNumber = view.findViewById(R.id.tvEngineNumber);
    }

    private void setUpFirst()
    {
        animationDuration = 1;
        timeSet = 2500;
        animate();
        animationDuration = 1500;
        timeSet = 0;
        openFlag = 1;

        if(vehicle!=null && vehicle.Vin!=null)
        {
            tvModel.setText(vehicle.Model);
            tvStatus.setText(vehicle.Type);
            tvVin.setText(vehicle.Vin);
            tvMake.setText(vehicle.Make);
            tvYear.setText(vehicle.Year);
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


    public void animate()
    {
        animLeftRight = ObjectAnimator.ofFloat(viewScanLeftRight, "translationX", new float[] { -viewScanLeftRight.getWidth(), viewScanLeftRight.getWidth() });
        animLeftRight.setDuration(animationDuration);
        animLeftRight.addListener(new Animator.AnimatorListener()
        {
            public void onAnimationCancel(Animator paramAnonymousAnimator) {}

            public void onAnimationEnd(Animator paramAnonymousAnimator) {}

            public void onAnimationRepeat(Animator paramAnonymousAnimator) {}

            public void onAnimationStart(Animator paramAnonymousAnimator)
            {
                if (viewScanLeftRight != null)
                {
                    viewScanLeftRight.setBackgroundResource(R.drawable.scan_background_reverse);
                    if(openFlag==1)
                    viewScanLeftRight.setVisibility(View.VISIBLE);
                }
            }
        });
        animLeftRight.start();
        animReverse = ObjectAnimator.ofFloat(viewScanRightLeft, "translationX", new float[] { viewScanRightLeft.getWidth(), -viewScanRightLeft.getWidth() });
        animReverse.setDuration(animationDuration);
        animReverse.setStartDelay(animationDuration - 750);
        animReverse.start();
        animReverse.addListener(new Animator.AnimatorListener()
        {
            public void onAnimationCancel(Animator paramAnonymousAnimator) {}

            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
                if(timeSet >= 2)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setUpAll();
                        }
                    },900);
                }
            }

            public void onAnimationRepeat(Animator paramAnonymousAnimator) {}

            public void onAnimationStart(Animator paramAnonymousAnimator)
            {
                    if(openFlag==1)
                    viewScanRightLeft.setVisibility(View.VISIBLE);
                    viewScanRightLeft.setBackgroundResource(R.drawable.scan_background);
            }
        });
        //timeSet += 1;
        if (timeSet < 2)
        {
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                  animate();
                }
            }, animationDuration * 2 - 1500);
        }


    }

    public void setUpAll()
    {
        timeSet = 0;
        tvScan.setVisibility(View.GONE);
        tvStatus.setText(vehicle.Type);
        tvStatus.setBackgroundResource(R.drawable.other_corner_radius_10);
        llhead2.setVisibility(View.GONE);
        llhead3.setVisibility(View.VISIBLE);
        rlFinishScan.setVisibility(View.VISIBLE);
    }


    public void fetchDTCCodeOfVehicles()
    {
        String URL = Constants.VEHICLE_HEALTH_REPORT + vehicle.VehicleId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<DTCDiagnostics>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    dtcDiagnosticsAdapter = new DTCDiagnosticsAdapter(activity(),list);
                    recyclerView.setAdapter(dtcDiagnosticsAdapter);
                }
                timeSet = 2;

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                timeSet = 2;
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    public void fetchExtraDetailsOfVehicles()
    {
        llhead2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_VEHICLES_DETAILS + vehicle.VehicleId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                List<Vehicle> vehicleList = gson.fromJson(response, type);
                if(vehicleList!=null)
                {
                  updateUI(vehicleList.get(0));
                }
                progressBar.setVisibility(View.GONE);
                llhead2.setVisibility(View.VISIBLE);

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

    public void updateUI(Vehicle vehicle)
    {
        this.vehicle.Latitude = vehicle.Latitude;
        this.vehicle.Longitude = vehicle.Longitude;
        tvBodyStyle.setText(vehicle.BodyStyle);
        tvColorExterior.setText(vehicle.ExteriorColor);
        tvInterior.setText(vehicle.InteriorColor);
        tvOdometerStatus.setText(vehicle.OdometerStatus);
        tvTransmissionType.setText(vehicle.TransmissionType);
        tvDoorQuantity.setText(vehicle.DoorsQuantity);
    }


    public void openLocateVehicle()
    {
        if(vehicle.Latitude!=null)
        ((HelperActivity)activity()).addLocationOfVehicle(vehicle);
        else
        {
            showSnackBar("Please wait",2);
        }
    }

}
