package com.maya.vgarages.fragments.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.activities.MainActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.home.GaragesAdapter;
import com.maya.vgarages.adapters.fragments.home.ServiceAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.home.IGaragesAdapter;
import com.maya.vgarages.interfaces.adapter.other.IServiceAdapter;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.Service;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IFragment, IServiceAdapter, IGaragesAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    IGaragesAdapter iGaragesAdapter;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerViewServices)
    RecyclerView recyclerViewServices;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    List<Service> list;
    List<Garage> garageList;
    IServiceAdapter iServiceAdapter;
    ServiceAdapter serviceAdapter;

    int selectedPosition = 0;

    boolean isDoneService = false ,isDoneGarage = false, isOnlyGarage = false;


    LatLng myLocation = new LatLng(17.439091, 78.399097);

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(LatLng latLng) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("Location", latLng);
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        iGaragesAdapter = this;
        iServiceAdapter = this;

        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        if(getArguments().getParcelable("Location")!=null)
        {
            myLocation = getArguments().getParcelable("Location");
        }

        recyclerViewServices.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));

        if(Utility.isNetworkAvailable(activity()))
        {
            fetchGarageServices();
            fetchGarages("All");
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);

            if(Utility.isNetworkAvailable(activity()))
            {
                selectedPosition = 0;
                isOnlyGarage = false;
                isDoneGarage = false;
                isDoneService = false;
                fetchGarageServices();
                fetchGarages("All");
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
//                if (scrollY - oldScrollY >= 10) //scroll down
//                {
//                    ((MainActivity) activity()).hideToolbar();
//                }
//                else if (scrollY - oldScrollY <= 10) //scroll up
//                {
//                    ((MainActivity) activity()).showToolbar();
//                }
//                else
//                {
//
//                }
            }
        });
    }

    private void fetchGarages(String value)
    {
        recyclerView.setAdapter(new GaragesAdapter(Utility.generateGaragesList(),activity(),iGaragesAdapter,true));
        String URL = Constants.URL_GET_GARAGES_LIST_BY_TYPE + "?latitude="+myLocation.latitude+"&longitude="+myLocation.longitude+"&GarageType="+value.trim().replace(" ","%20")+"&pagecount=1" ;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Garage>>() {
                }.getType();
                garageList = gson.fromJson(response, type);
                isDoneGarage = true;
                reflectItems();


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                recyclerView.setAdapter(new EmptyDataAdapter(activity(),0));
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);


    }



    private void fetchGarageServices()
    {
        recyclerViewServices.setAdapter(serviceAdapter = new ServiceAdapter(Utility.generateServices(),activity(),iServiceAdapter,true));
        String URL = Constants.URL_GARAGE_SERVICE_TYPES + "?pagecount=1" ;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Service>>() {
                }.getType();
                list = new ArrayList<>();
                list.add(new Service("All",R.drawable.all,true));
                list.addAll(gson.fromJson(response, type));

                Utility.setString(Utility.getSharedPreferences(),Constants.VGARAGE_SERVICES,gson.toJson(list,type));
                isDoneService = true;
                reflectItems();


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                recyclerViewServices.setAdapter(new EmptyDataAdapter(activity(),0));
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }


    public void reflectItems()
    {
        if(isDoneService == true && isDoneGarage == true)
        {
            if (garageList != null && garageList.size() > 0)
            {
                recyclerView.setAdapter(new GaragesAdapter(garageList, activity(), iGaragesAdapter, false));
            }
            else
            {
                recyclerView.setAdapter(new EmptyDataAdapter(activity(), 1));
            }

            if (list != null && list.size() > 0)
            {
                if(isOnlyGarage == false)
                {
                    isOnlyGarage = true;
                    recyclerViewServices.setAdapter(serviceAdapter = new ServiceAdapter(list, activity(), iServiceAdapter, false));
                }
            }
            else
            {
                recyclerViewServices.setAdapter(serviceAdapter = new ServiceAdapter(list = Utility.generateServices(), activity(), iServiceAdapter, false));
            }
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

    @Override
    public void onItemClick(Service service, int position)
    {
        for(int i= 0;i<list.size();i++)
        {
            list.get(i).IsSelected = false;
        }
        list.get(position).IsSelected = true;
        serviceAdapter.notifyDataSetChanged();
        if(position == selectedPosition)
        {
            return;
        }
        else
        {
            selectedPosition = position;
            fetchGarages(service.GarageType);
        }
    }

    @Override
    public void itemClick(Garage garage, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2222);
        intent.putExtra("Garage",garage);
        startActivity(intent);
    }


}
