package com.maya.wadmin.fragments.zones.view;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.activities.MainActivity;
import com.maya.wadmin.adapters.fragments.delivery.VehicleArrivalAdapter;
import com.maya.wadmin.adapters.fragments.zones.ZonesViewAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.dialogs.actions.ActionConfirmationDialog;
import com.maya.wadmin.interfaces.adapters.zones.IZonesViewAdapter;
import com.maya.wadmin.interfaces.dialog.IActionConfirmationDialogZone;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.Zone;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZonesViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZonesViewFragment extends Fragment implements IFragment ,IZonesViewAdapter, IActionConfirmationDialogZone {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    IActionConfirmationDialogZone iActionConfirmationDialogZone;

    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    List<Zone> list,finalList;
    ZonesViewAdapter zonesViewAdapter;
    IZonesViewAdapter iZonesViewAdapter;

    public ZonesViewFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZonesViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZonesViewFragment newInstance(String param1, String param2) {
        ZonesViewFragment fragment = new ZonesViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_zones_view, container, false);
        ButterKnife.bind(this,view);

        iZonesViewAdapter = this;
        iActionConfirmationDialogZone = this;




        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                ((HelperActivity) activity()).clearSearchText();
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchAllZones();
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
            fetchAllZones();
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

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    public void fetchAllZones()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GEOFENCES_LIST +  Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID) + Utility.addPortalTag();
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Zone>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    if(mParam2!=null)
                    {
                        List<Zone> subList = new ArrayList<>();
                        for(int i =0;i<(list.size()>5?5:list.size());i++)
                        {
                            subList.add(list.get(i));
                        }
                        list = subList;
                    }
                    finalList = list;
                    zonesViewAdapter = new ZonesViewAdapter(list,activity(),iZonesViewAdapter);
                    recyclerView.setAdapter(zonesViewAdapter);
                }


                progressBar.setVisibility(View.GONE);


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
    public void editZone(Zone zone, int position)
    {
        if(Utility.isNetworkAvailable(activity()))
        {
            if(progressBar.getVisibility()==View.GONE)
            {
                getDetailsZone(zone);
            }
            else
            {
                showSnackBar("Please wait",2);
            }
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }
    }

    @Override
    public void openPopUp(View view, Zone zone,final int position)
    {
        ((HelperActivity) activity()).openOptionsForZone(11,zone);



//        PopupMenu popup = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
//        {
//            popup = new PopupMenu(activity(),view, Gravity.END);
//        }
//        else
//        {
//            popup = new PopupMenu(activity(),view);
//        }
//        // Inflate the menu from xml
//        popup.inflate(R.menu.zone_options);
//        // Setup menu item selection
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item)
//            {
//                switch (item.getItemId())
//                {
//                    case R.id.nav_edit:
//                        editZone(zone,position);
//                        return true;
//                    case R.id.nav_delete:
//                        deleteZone(zone,position);
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//        popup.show();

    }

    @Override
    public void deleteZone(Zone zone, int position)
    {
        if(Utility.isNetworkAvailable(activity()))
        {
            confirmationDialog("Delete this Zone",0,zone);
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

    }

    public void confirmationDialog(String content,int type,Zone zone)
    {
        ActionConfirmationDialog dialog = new ActionConfirmationDialog(1,type,activity(),content,iActionConfirmationDialogZone,zone);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteZoneByGUID(Zone zone)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_DELETE_GEOFENCE + zone.GeofenceGuid;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                fetchAllZones();
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


    public void getDetailsZone(Zone zone)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_EDIT_GEOFENCE + zone.GeofenceGuid;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Zone>>() {
                }.getType();
                List<Zone> zoneDetails = gson.fromJson(response, type);
                if(zoneDetails!=null && zoneDetails.size()>0)
                {

                    zoneDetails.get(0).GeofenceGuid = zone.GeofenceGuid;
                    Intent intent = new Intent(activity(),HelperActivity.class);
                    intent.putExtra(Constants.FRAGMENT_KEY, 333);
                    intent.putExtra("Zone",zoneDetails.get(0));
                    startActivityForResult(intent,Utility.generateRequestCodes().get("EDIT_ZONE"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Utility.generateRequestCodes().get("EDIT_ZONE"))
        {
            if(data!=null)
            {
                fetchAllZones();
            }
        }
    }


    public void updateSearchZones(String content)
    {
        Logger.d(content);

        if(finalList!=null && finalList.size()>0)
        {

            if (content.length() > 0)
            {
                List<Zone> subList = new ArrayList<>();
                for(Zone zone : finalList) {
                    if ((zone.GeofenceName + " " + zone.Keywords + " " + zone.City)
                            .toLowerCase().contains(content.toLowerCase())) {
                        subList.add(zone);
                    }
                }

                if (subList.size() > 0)
                {
                    list = subList;
                    zonesViewAdapter = new ZonesViewAdapter(list,activity(),iZonesViewAdapter);
                    recyclerView.setAdapter(zonesViewAdapter);
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
                zonesViewAdapter = new ZonesViewAdapter(list,activity(),iZonesViewAdapter);
                recyclerView.setAdapter(zonesViewAdapter);
            }
        }


    }

    @Override
    public void deleteZoneAction(Zone zone) {
        deleteZoneByGUID(zone);
    }
}
