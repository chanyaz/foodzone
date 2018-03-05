package com.maya.wadmin.fragments.rules_and_alerts.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.maya.wadmin.adapters.fragments.pdi.AssignPDIVehiclesAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.dialogs.rules_and_alerts.AddActionDialog;
import com.maya.wadmin.fragments.rules_and_alerts.add.AlertControlFragment;
import com.maya.wadmin.fragments.rules_and_alerts.add.SelectVehiclesFragment;
import com.maya.wadmin.interfaces.dialog.IAddActionDialog;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AlertActionChannel;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Operation;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.Zone;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAlertFragment extends Fragment implements IFragment,IAddActionDialog {
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
    TextView tvNext,tvAddAlert;
    ImageView imgNextArrow;
    ProgressBar progressBar;
    List<Operation> operations;
    int alertPosition = 0;
    List<Zone> zoneList = null;
    public int currentAlertId = 0;
    IAddActionDialog iAddActionDialog;
    List<Vehicle> selectedList;
    String alertName, alertDesc;
    AlertRule alertRule;
    public AlertActionChannel alertActionChannel;


    public AddAlertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAlertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAlertFragment newInstance(String param1, String param2) {
        AddAlertFragment fragment = new AddAlertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddAlertFragment newInstance(AlertRule alertRule)
    {
        AddAlertFragment fragment = new AddAlertFragment();
        Bundle args = new Bundle();
        args.putSerializable("AlertRule", alertRule);
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
        View view = inflater.inflate(R.layout.fragment_add_alert, container, false);
        iAddActionDialog = this;

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        llSelectAll = view.findViewById(R.id.llSelectAll);
        llNext = view.findViewById(R.id.llNext);
        tvNext = view.findViewById(R.id.tvNext);
        tvAddAlert = view.findViewById(R.id.tvAddAlert);
        imgNextArrow = view.findViewById(R.id.imgNextArrow);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);



        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(progressBar.getVisibility()==View.GONE)
                {
                    if(checkActions())
                    addFragment();
                }
                else
                {
                    showSnackBar("Please wait",2);
                }
            }
        });


        llSelectAll.setOnClickListener( click ->
        {
            if(progressBar.getVisibility()==View.GONE)
            openAction();
            else
            {
                showSnackBar("Please wait",2);
            }
        });





        if(getArguments().getSerializable("AlertRule")==null)
        {
            if (Utility.isNetworkAvailable(activity()))
                generateCurrentAlertId();
            else {
                activity().finish();
            }
        }
        else
        {
            alertRule = (AlertRule) getArguments().getSerializable("AlertRule");
            currentAlertId = alertRule.AlertId;
        }

        addFragment();

        return view;


    }

    private boolean checkActions()
    {
        AlertControlFragment alertControlFragment = (AlertControlFragment)getChildFragmentManager().getFragments().get(0);

        if(alertControlFragment.alertActionChannelList!=null && alertControlFragment.alertActionChannelList.size()>0)
        {
            if( (""+alertControlFragment.etAlertName.getText()).trim().length() > 0 && (""+alertControlFragment.etAlertDesc.getText()).trim().length()>0)
            {
                alertName = alertControlFragment.etAlertName.getText().toString().trim();
                alertDesc = alertControlFragment.etAlertDesc.getText().toString().trim();
                return true;
            }
            else
            {
                alertControlFragment.showSnackBar("Please fill the fields",2);
            }
        }
        else
        {
            alertControlFragment.showSnackBar("Please add actions",2);
        }

        return false;
    }

    private void generateCurrentAlertId()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_GENERATE_ALERT_ID +  Utility.getString(Utility.getSharedPreferences(), Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Utility.closeProgressDialog(progressDialog);

                try
                {
                    currentAlertId = Integer.parseInt(response);
                }
                catch (Exception e)
                {
                   activity().finish();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                Utility.closeProgressDialog(progressDialog);
                showSnackBar(Constants.CONNECTION_ERROR, 2);
                activity().finish();
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);

    }

    private void openAction()
    {
        if(getChildFragmentManager().getBackStackEntryCount()>1)
        {
            getChildFragmentManager().popBackStack();
            setUpAllInitial();
        }
        else
        {
            if(Utility.isNetworkAvailable(activity()))
            {
                fetchOperations(Constants.ALERT_TYPE_IDS[alertPosition = ((AlertControlFragment)getChildFragmentManager().getFragments().get(0)).previous]);
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        }
    }


    public void fetchOperations(final int categoryId)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_CATEGORY_OPERATION + categoryId + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(), Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Operation>>() {
                }.getType();
                operations = gson.fromJson(response, type);
                if (operations != null && operations.size()>0)
                {
                    if(categoryId==3 || categoryId==4)
                    {
                        fetchZones(categoryId);
                    }
                    else
                    openDialog(categoryId);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR, 2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);

    }

    private void fetchZones(int categoryId)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ALERT_GEOFENCE + Utility.getString(Utility.getSharedPreferences(), Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Zone>>() {
                }.getType();
                zoneList = gson.fromJson(response, type);
                if (zoneList != null && zoneList.size()>0)
                {
                    openDialog(categoryId);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR, 2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);

    }

    private void openDialog(int categoryId)
    {
        AddActionDialog addActionDialog;
        if(alertActionChannel == null)
        addActionDialog = new AddActionDialog(activity(),iAddActionDialog,alertPosition,operations,categoryId ==3 || categoryId == 4 ?zoneList :null,currentAlertId);
        else
        {
            addActionDialog = new AddActionDialog(activity(),alertActionChannel,iAddActionDialog,getPositionOfCategory(categoryId),operations,categoryId ==1 || categoryId ==3 ?zoneList :null,currentAlertId);
            alertActionChannel = null;
        }

        addActionDialog.setCancelable(true);
        addActionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addActionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addActionDialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;

        int displayHeight = displayMetrics.heightPixels;


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();


        layoutParams.copyFrom(addActionDialog.getWindow().getAttributes());


        int dialogWindowWidth = (int) (displayWidth * 0.99f);

        int dialogWindowHeight = (int) (displayHeight * 0.95f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        addActionDialog.getWindow().setAttributes(layoutParams);

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


    public void addFragment()
    {
        switch (currentFragment)
        {
            case 1:
                if(alertRule==null)
                {
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, AlertControlFragment.newInstance(null, null)).addToBackStack("Start").commit();
                }
                else
                {
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, AlertControlFragment.newInstance(alertRule)).addToBackStack("Start").commit();
                }
                break;
            case 2:
                tvAddAlert.setText("< Back to Actions");
                tvNext.setText("Finish");
                imgNextArrow.setVisibility(View.GONE);
                if(alertRule==null)
                {
                    getChildFragmentManager().beginTransaction().add(R.id.frameLayout, SelectVehiclesFragment.newInstance(null, null)).addToBackStack("Vehicles").commit();
                }
                else
                {
                    fetchAlertVehicles(alertRule);
                }
              break;
            case 3:
                if(verifySelectedVehicles())
                {
                    if(Utility.isNetworkAvailable(activity()))
                    {
                        addAlert();
                    }
                }
                else
                {
                    return;
                }

                break;

        }
        if(currentFragment<3)
        {
            currentFragment++;
        }



    }

    public void setUpAllInitial()
    {
        tvAddAlert.setText("+ Add Action");
        tvNext.setText("Select Vehicles");
        imgNextArrow.setVisibility(View.VISIBLE);
        currentFragment=2;
    }

    @Override
    public void changeAction()
    {
        if(Utility.isNetworkAvailable(activity()))
        {
            ((AlertControlFragment)getChildFragmentManager().getFragments().get(0)).fetchAlertActions(currentAlertId);
        }
    }


    public boolean verifySelectedVehicles()
    {
        SelectVehiclesFragment selectVehiclesFragment = ((SelectVehiclesFragment)getChildFragmentManager().getFragments().get(1));
        selectedList = selectVehiclesFragment.selectedList;
        if(selectedList!=null && selectedList.size()>0)
        {
            return true;
        }
        else
        {
            selectVehiclesFragment.showSnackBar("Please select vehicles",2);
            return false;
        }

    }

    public void addAlert()
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

        String URL =Constants.URL_UPDATE_ALERT + currentAlertId +
                "&categoryId="+ Constants.ALERT_TYPE_IDS[alertPosition] + "&name="+alertName +
                "&description=" + alertDesc +
                "&vehicleIds="+value+"&channelsXML= HTTP/1.1";
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
                returnData.putExtra("position",alertPosition);
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

    public void fetchAlertVehicles(AlertRule alertRule)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ALERT_VEHICLES_DETAILS + alertRule.AlertId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                List<String> vehiclesIds = gson.fromJson(response, type);
                if(vehiclesIds!=null&&vehiclesIds.size()>0)
                {
                    getChildFragmentManager().beginTransaction().add(R.id.frameLayout, SelectVehiclesFragment.newInstance(response)).addToBackStack("Vehicles").commit();
                }
                else
                {
                    getChildFragmentManager().beginTransaction().add(R.id.frameLayout, SelectVehiclesFragment.newInstance(null, null)).addToBackStack("Vehicles").commit();
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

    public int getPositionOfCategory(int category)
    {
        for (int i=0;i<Constants.ALERT_TYPE_IDS.length;i++)
        {
            if(Constants.ALERT_TYPE_IDS[i]==category)
            {
                return i;
            }
        }

        return 0;
    }
}
