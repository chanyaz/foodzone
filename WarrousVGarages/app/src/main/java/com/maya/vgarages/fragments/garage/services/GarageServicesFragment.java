package com.maya.vgarages.fragments.garage.services;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.garage.services.GarageServicesAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.garage.overview.GarageOverviewFragment;
import com.maya.vgarages.interfaces.adapter.garage.IGarageServicesAdapter;
import com.maya.vgarages.interfaces.dialog.IReplaceCartDialog;
import com.maya.vgarages.interfaces.dialog.IToPickVehicle;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.models.Vehicle;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GarageServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GarageServicesFragment extends Fragment implements IFragment, IGarageServicesAdapter,IReplaceCartDialog,IToPickVehicle {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Vehicle vehicle;

    LinearLayoutManager layoutManager;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    @BindView(R.id.tvCarName)
    TextView tvCarName;

    @BindView(R.id.rlChange)
    RelativeLayout rlChange;

    Garage garage;
    List<GarageService> list;
    GarageService garageService;
    int position =0;

    GarageServicesAdapter garageServicesAdapter;
    IGarageServicesAdapter iGarageServicesAdapter;
    IReplaceCartDialog iReplaceCartDialog;
    IToPickVehicle iToPickVehicle;

    Animation slideDown, slideUp;


    public GarageServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GarageServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GarageServicesFragment newInstance(String param1, String param2) {
        GarageServicesFragment fragment = new GarageServicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static GarageServicesFragment newInstance(Garage garage)
    {
        GarageServicesFragment fragment = new GarageServicesFragment();
        Bundle args = new Bundle();
        args.putSerializable("Garage", garage);
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
        View view = inflater.inflate(R.layout.fragment_garage_services, container, false);
        iGarageServicesAdapter = this;
        iReplaceCartDialog = this;
        iToPickVehicle = this;
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        if(getArguments()==null || getArguments().getSerializable("Garage")==null)
        {
            return;
        }
        slideDown = AnimationUtils.loadAnimation(activity(), R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(activity(), R.anim.slide_up);

        garage = (Garage) getArguments().getSerializable("Garage");

        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(activity()));

        swipeRefreshLayout.setOnRefreshListener( () ->
        {
            swipeRefreshLayout.setRefreshing(false);
            if(Utility.isNetworkAvailable(activity()))
            {
                ((HelperActivity) activity()).refreshCart();
                fetchGarageServices();
            }
            else
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        });

        if(Utility.isNetworkAvailable(activity()))
            fetchGarageServices();
        else
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);

        rlChange.setOnClickListener(v ->
        {
            ((HelperActivity) activity()).goToUserVehicles(Utility.generateRequestCodes().get("CHANGE_VEHICLE"));
        });


        updateBottomBar();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findLastVisibleItemPosition();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                Logger.d("DATA " + firstVisibleItemPosition + " " + visibleItemCount + " " +totalItemCount + " " + pastVisibleItems );
                if (pastVisibleItems + visibleItemCount >= totalItemCount)
                {
                    //End of list
                    if(Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
                    {
                        if(llBottom.getVisibility()==View.GONE)
                            return;

                        slideDown = AnimationUtils.loadAnimation(activity(), R.anim.slide_down);
                        slideDown.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                llBottom.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        llBottom.setAnimation(slideDown);
                    }
                }
                else
                {
                    if(firstVisibleItemPosition == 0 || visibleItemCount - pastVisibleItems == 0 )
                    {
                        if(Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
                        {
                            if(llBottom.getVisibility() == View.VISIBLE)
                                return;
                            slideUp = AnimationUtils.loadAnimation(activity(), R.anim.slide_up);
                            slideUp.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    llBottom.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            llBottom.setAnimation(slideUp);
                        }

                    }

                }

            }
        });

    }

    public void updateBottomBar()
    {
        if(Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
        {
            llBottom.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            Type type = new TypeToken<Vehicle>(){}.getType();
            vehicle = gson.fromJson(Utility.getString(Utility.getSharedPreferences(),Constants.DEFAULT_CAR_DATA),type);
            tvCarName.setText(Utility.getCamelCase(vehicle.vehicleName));

        }
        else
        {
            llBottom.setVisibility(View.GONE);
        }
    }

    private void fetchGarageServices()
    {
        recyclerView.setAdapter(new GarageServicesAdapter(Utility.generateGarageServices(),iGarageServicesAdapter,activity(),true));
        String URL = Constants.URL_GET_GARAGES_SERVICES + "?dealerId="+garage.DealerId+"&pageCount=1";
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<GarageService>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    garageServicesAdapter =new GarageServicesAdapter(list,iGarageServicesAdapter,activity(),false);
                    recyclerView.setAdapter(garageServicesAdapter);
                    updateGarageService();
                }
                else
                {
                    recyclerView.setAdapter(new EmptyDataAdapter(activity(),1));
                }

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


    public void updateGarageService()
    {
        if(((HelperActivity) activity()).cartGarage == null)
        {
            return;
        }
        else
        {
            if(((HelperActivity) activity()).cartGarage.DealerId != garage.DealerId)
            {
                return;
            }
        }


        if(((HelperActivity) activity()).cartGarageServices.size()==0)
        {
            for(int j=0; j<list.size() ;j++)
            {
                list.get(j).isAdded = false;
                list.get(j).isPending = false;
            }
            garageServicesAdapter.notifyDataSetChanged();
            return;
        }


        for(int j=0; j<list.size() ;j++)
        {
            for (int i = 0; i < ((HelperActivity) activity()).cartGarageServices.size(); i++)
            {
                if (((HelperActivity) activity()).cartGarageServices.get(i).OpCodeId == list.get(j).OpCodeId)
                {
                    list.get(j).isAdded = true;
                    list.get(j).isPending = false;
                    break;
                }
                else
                {
                    list.get(j).isAdded = false;
                    list.get(j).isPending = false;
                }
            }
        }
        garageServicesAdapter.notifyDataSetChanged();
    }


    @Override
    public void applyAction(GarageService garageService, int position)
    {

        if(!Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
        {
            ((HelperActivity)activity()).pickVehicleDialog(iToPickVehicle);
            return;
        }

        if(((HelperActivity) activity()).cartGarage == null)
        {
            ((HelperActivity) activity()).cartGarage = garage;
        }

        if(((HelperActivity) activity()).cartGarage.DealerId != garage.DealerId)
        {
           //showSnackBar("Garage is different",2);

            if(((HelperActivity) activity()).cartGarageServices.size() == 0)
            {
                ((HelperActivity) activity()).cartGarage = garage;
            }
            else
            {
                this.garageService = garageService;
                this.position = position;
                ((HelperActivity) activity()).requestCartReplace(iReplaceCartDialog, garage);
                return;
            }
        }

        if(garageService.isAdded)
        {
            for (int i = 0; i < ((HelperActivity) activity()).cartGarageServices.size(); i++)
            {
                if (((HelperActivity) activity()).cartGarageServices.get(i).DealerId == garageService.DealerId
                        &&
                        ((HelperActivity) activity()).cartGarageServices.get(i).OpCodeId == garageService.OpCodeId)
                {
                    ((HelperActivity) activity()).cartGarageServices.remove(i);
                    break;
                }
            }

        }
        else
        {
            ((HelperActivity) activity()).cartGarageServices.add(garageService);
        }

        //Logger.d("SIZE  "+((HelperActivity) activity()).cartGarageServices.size() );


        list.get(position).isPending = true;
        garageServicesAdapter.notifyDataSetChanged();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {

                list.get(position).isPending = false;
                list.get(position).isAdded = list.get(position).isAdded ?  false : true;
                garageServicesAdapter.notifyDataSetChanged();
                Logger.d("SIZE "+ ((HelperActivity) activity()).cartGarageServices.size());

                ((HelperActivity) activity()).updateCart(((HelperActivity) activity()).cartGarageServices.size());
                ((HelperActivity) activity()).saveCart();
            }
        },1000);

    }

    @Override
    public void replaceCart()
    {
        ((HelperActivity) activity()).replaceCart(garage);
        applyAction(garageService,position);
    }

    @Override
    public void goToVehicles()
    {
        ((HelperActivity) activity()).goToUserVehicles(Utility.generateRequestCodes().get("ADD_VEHICLE"));
    }
}
