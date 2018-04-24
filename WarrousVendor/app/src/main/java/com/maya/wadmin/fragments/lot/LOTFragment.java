package com.maya.wadmin.fragments.lot;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.custom.TopBarAdapter;
import com.maya.wadmin.adapters.fragments.vehicle.types.VehiclesTypeAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.lot.violations.ViolationVehiclesFragment;
import com.maya.wadmin.fragments.other.DummyFragment;
import com.maya.wadmin.fragments.pdi.view.PDIViewFragment;
import com.maya.wadmin.fragments.vehicle.types.VehiclesTypeFragment;
import com.maya.wadmin.interfaces.custom.ITopBarAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.TopBarPanel;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LOTFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LOTFragment extends Fragment implements IFragment , ITopBarAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    List<Vehicle> list;
    List<Vehicle> inDeliveryReceived;
    List<Vehicle> inPreparingForLot;
    List<Vehicle> inMarkForPDI;
    List<Vehicle> inPDIIncomplete;
    List<Vehicle> inInventory;
    List<Vehicle> inTestDrive;
    List<TopBarPanel> listTopBarPanel = Utility.getTopBarPanelElements(3);
    int previous = 0;

    @BindView(R.id.mainTabLayout)
    LinearLayout mainTabLayout;

    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.recyclerViewTopBar)
    RecyclerView recyclerViewTopBar;

    ITopBarAdapter iITopBarAdapter;

    @BindView(R.id.llTopBarPanel)
    LinearLayout llTopBarPanel;

    @BindView(R.id.llMainHead)
    LinearLayout llMainHead;

    @BindView(R.id.tvTopBarItem)
    TextView tvTopBarItem;

    TopBarAdapter topBarAdapter;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    ArrayList<String> stringList = new ArrayList<>();
    int vehiclesType[] = {7,8,9,10,1,2};

    @BindView(R.id.tvAssign)
    TextView tvAssign;

    VehiclesTypeAdapter vehiclesTypeAdapter;


    public LOTFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LOTFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LOTFragment newInstance(String param1, String param2) {
        LOTFragment fragment = new LOTFragment();
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
        View view= inflater.inflate(R.layout.fragment_lot, container, false);
        ButterKnife.bind(this,view);
        iITopBarAdapter = this;


        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tvAssign = view.findViewById(R.id.tvAssign);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utility.isNetworkAvailable(activity()))
                {
                    //fetchVehiclesOfLot();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        stringList.add(Constants.DELIVERY_RECEIVED);
        stringList.add(Constants.PREPARING_FOR_LOT);
        stringList.add(Constants.MARK_FOR_PDI);
        stringList.add(Constants.PDI_INCOMPLETE);
        stringList.add(Constants.INVENTORY);
        stringList.add(Constants.TEST_DRIVE);

        tvTopBarItem.setText(listTopBarPanel.get(0).title);
        recyclerViewTopBar.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewTopBar.setAdapter(topBarAdapter = new TopBarAdapter(listTopBarPanel,activity(),iITopBarAdapter));
        swipeRefreshLayout.setEnabled(false);
        frameLayout.setVisibility(View.GONE);

        tvTopBarItem.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.VISIBLE);
        });

        llTopBarPanel.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.GONE);
        });

        tvAssign.setOnClickListener(click -> {gotoAddPreparing();});


//        if(Utility.isNetworkAvailable(activity()))
//        {
//            fetchVehiclesOfLot();
//        }
//        else
//        {
//            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
//        }


        viewPager.setAdapter(vehiclesTypeAdapter = new VehiclesTypeAdapter(getFragmentManager(),7891012,stringList,vehiclesType));
        viewPager.setOffscreenPageLimit(stringList.size());
        Utility.updateTablayout(tabLayout,activity());
        changeListener();
        updateTab(0);


        return view;
    }

    public void gotoAddPreparing()
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,11);
        startActivityForResult(intent,Utility.generateRequestCodes().get("ASSIGN_PREPARE"));
    }

    public void gotoAddPreparing(Vehicle vehicle)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,11);
        intent.putExtra("vehicle",vehicle);
        startActivityForResult(intent,Utility.generateRequestCodes().get("ASSIGN_PREPARE"));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utility.generateRequestCodes().get("ASSIGN_PREPARE"))
        {
            if(data!=null)
            {
                try
                {

                    Logger.d("DATA IS UPDATED" + data.getStringExtra("data"));
                    frameLayout.setVisibility(View.GONE);
                    mainTabLayout.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(1, true);

                    List<Fragment> list = getFragmentManager().getFragments();
                    Logger.d(" Size "+list.size());
                    for (int i = 1;i<list.size();i++)
                    {
                        try
                        {
                            VehiclesTypeFragment vehiclesTypeFragment = (VehiclesTypeFragment) list.get(i);
                            vehiclesTypeFragment.fetchVehiclesForFleet();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    showSnackBar("Assigned for prepare",1);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }
            else
            {

            }
        }
        if(requestCode == Utility.generateRequestCodes().get("LOT_FORM"))
        {
            if(data!=null)
            {
                try
                {

                    Logger.d("DATA IS UPDATED" + data.getStringExtra("data"));
                    frameLayout.setVisibility(View.GONE);
                    mainTabLayout.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(2, true);

                    List<Fragment> list = getFragmentManager().getFragments();
                    Logger.d(" Size "+list.size());
                    for (int i = 1;i<list.size();i++)
                    {
                        try
                        {
                            VehiclesTypeFragment vehiclesTypeFragment = (VehiclesTypeFragment) list.get(i);
                            vehiclesTypeFragment.fetchVehiclesForFleet();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    showSnackBar("Updating vehicles",1);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }
            else
            {

            }
        }
    }

    public void changeListener()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                updateTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


        llMainHead.setOnClickListener(click -> {
            appBar.setExpanded(true,true);
        });

        llMainHead.setOnTouchListener((v, event) -> {
            appBar.setExpanded(true,true);
            return false;
        });

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
                Logger.d("Percentage" , ""+ percentage);
                float value = (int) (percentage * 10);
                int width = activity().getResources().getDisplayMetrics().widthPixels - Utility.dpSize(activity(),50);

                if(value!= 0)
                {
                    RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(

                            Math.round(
                                    width /(value) > Utility.dpSize(activity(),100)
                                            ?  Math.round(width /(value)) : Utility.dpSize(activity(),100))

                            ,Math.round(
                            Utility.dpSize(activity(),50) /(value) > Utility.dpSize(activity(),5)
                                    ? Utility.dpSize(activity(),50) /(value) : Utility.dpSize(activity(),5)));

                    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    llMainHead.setLayoutParams(params);
                    tvTopBarItem.setVisibility(View.GONE);

                }
                else
                {
                    RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams( width , Utility.dpSize(activity(),50));
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    llMainHead.setLayoutParams(params);
                    tvTopBarItem.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void updateTab(int position)
    {
        appBar.setExpanded(true,true);
        tvAssign.setVisibility(position==0?View.VISIBLE:View.GONE);
        try {

            for (int i = 0; i < tabLayout.getTabCount(); i++) {

                TextView tv = (TextView) tabLayout.getTabAt(i).getCustomView();
                if (position == i) {
                    tv.setTextColor(Color.parseColor("#58595B"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                } else {
                    tv.setTextColor(Color.parseColor("#8058595B"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            activity().finish();
        }
    }

    public void fetchVehiclesOfLot()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_LOT_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null)
                {
                    Logger.d("list contains",""+list.size());
                    //Logger.d("list contains",""+list.get(0).get(0).Vin);
                    inMarkForPDI = new ArrayList<>();
                    inPDIIncomplete = new ArrayList<>();
                    inDeliveryReceived = new ArrayList<>();
                    inPreparingForLot = new ArrayList<>();
                    inInventory = new ArrayList<>();
                    inTestDrive = new ArrayList<>();

                    for(int i =0;i<list.size();i++)
                    {
                        Logger.d(list.get(i).Type);
                        if(list.get(i).Type==null)
                        {

                        }
                        else if(list.get(i).Type.equalsIgnoreCase(Constants.DELIVERY_RECEIVED))
                        {
                            inDeliveryReceived.add(list.get(i));
                            //break;
                        }
                        else if(list.get(i).Type.equalsIgnoreCase(Constants.PREPARING_FOR_LOT))
                        {
                            inPreparingForLot.add(list.get(i));
                            //break;
                        }
                        else if(list.get(i).Type.equalsIgnoreCase(Constants.MARK_FOR_PDI))
                        {
                            inMarkForPDI.add(list.get(i));
                            //break;
                        }
                        else if(list.get(i).Type.equalsIgnoreCase(Constants.PDI_INCOMPLETE))
                        {
                            inPDIIncomplete.add(list.get(i));
                            //break;
                        }
                        else if(list.get(i).Type.equalsIgnoreCase(Constants.INVENTORY))
                        {
                            inInventory.add(list.get(i));
                            //break;
                        }
                        else if(list.get(i).Type.equalsIgnoreCase(Constants.TEST_DRIVE))
                        {
                            inTestDrive.add(list.get(i));
                            //break;
                        }

                        else
                        {

                        }

                    }

                    viewPager.setAdapter(new VehiclesTypeAdapter(getFragmentManager(),inDeliveryReceived,inPreparingForLot,
                            inMarkForPDI,inPDIIncomplete,inInventory,inTestDrive
                            ,2));
                    Utility.updateTablayout(tabLayout,activity());
                    updateTab(0);
                }


                Utility.closeProgressDialog(progressDialog);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                Utility.closeProgressDialog(progressDialog);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }


    @Override
    public void changeTitle(String title) {

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
    public void itemClick(TopBarPanel topBarPanel, int position) {
        setUpAction(position);
        llTopBarPanel.setVisibility(View.GONE);
        if(previous == -1)
        {
            listTopBarPanel.get(position).isSelected = true;
        }
        else
        {
            listTopBarPanel.get(previous).isSelected = false;
            listTopBarPanel.get(position).isSelected = true;
        }
        previous = position;
        tvTopBarItem.setText(topBarPanel.title);
        topBarAdapter.notifyDataSetChanged();
    }

    private void setUpAction(int position)
    {
        if(previous == position)
        {
            return;
        }
        else
        {
            ((HelperActivity)activity()).clearSearchText();
            switch (position)
            {
                case 0:
                    tvAssign.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    mainTabLayout.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvAssign.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, PDIViewFragment.newInstance("lot",null)).commit();
                    mainTabLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    tvAssign.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, ViolationVehiclesFragment.newInstance(null,null)).commit();
                    mainTabLayout.setVisibility(View.GONE);
                    break;
                case 3:
                    tvAssign.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new VehiclesTypeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",4);
                    bundle.putString("list",null);
                    bundle.putString("value",Constants.PDI_COMPLETE);
                    fragment.setArguments(bundle);
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    mainTabLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }


    public void updateVehiclesInAll(String content)
    {
        Logger.d(content);
        switch (previous)
        {
            case 0:

                List<Fragment> list = getFragmentManager().getFragments();
                Logger.d(" Size "+list.size());
                if (list.size()>1)
                    for (int i = 1;i<tabLayout.getTabCount()+1;i++)
                    {
                        try
                        {
                            VehiclesTypeFragment vehiclesTypeFragment = (VehiclesTypeFragment) list.get(i);
                            vehiclesTypeFragment.updateVehiclesBySearch(content);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                break;
            case 1:
                ((PDIViewFragment) getChildFragmentManager().getFragments().get(0)).updateSalespersonBySearch(content);
                break;
            case 2:
                ((ViolationVehiclesFragment) getChildFragmentManager().getFragments().get(0)).updateVehiclesBySearch(content);
                break;
            case 3:
                ((VehiclesTypeFragment) getChildFragmentManager().getFragments().get(0)).updateVehiclesBySearch(content);
                break;
        }


    }
}