package com.maya.wadmin.fragments.pdi;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import com.maya.wadmin.fragments.delivery.view.AllDeliveryVehiclesFragment;
import com.maya.wadmin.fragments.pdi.view.PDIViewFragment;
import com.maya.wadmin.fragments.vehicle.types.VehiclesTypeFragment;
import com.maya.wadmin.interfaces.custom.ITopBarAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.DeliveryTruck;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.TopBarPanel;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PDIHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PDIHomeFragment extends Fragment implements IFragment, ITopBarAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CoordinatorLayout coordinatorLayout;
    TextView tvAssignPDI;
    TabLayout tabLayout;
    ViewPager viewPager;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Vehicle> list;
    List<Vehicle> inMarkForPDI;
    List<Vehicle> inPDIIncomplete;
    List<Vehicle> inPDIcomplete;
    ArrayList<String> stringList = new ArrayList<>();
    int vehiclesType[] = {9,10,1};
    FrameLayout frameLayout;
    RecyclerView recyclerViewTopBar;
    ITopBarAdapter iITopBarAdapter;
    LinearLayout llTopBarPanel,llMainHead;
    TextView tvTopBarItem;
    TopBarAdapter topBarAdapter;
    TopBarPanel topBarPanel;
    List<TopBarPanel> listTopBarPanel = Utility.getTopBarPanelElements(2);
    int previous = 0;
    LinearLayout mainTabLayout;
    AppBarLayout appBar;
    float before = -1;
    ViewGroup.LayoutParams layoutParams;



    public PDIHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PDIHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PDIHomeFragment newInstance(String param1, String param2) {
        PDIHomeFragment fragment = new PDIHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_pdihome, container, false);
        iITopBarAdapter = this;

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        tvAssignPDI = view.findViewById(R.id.tvAssignPDI);
        tvAssignPDI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                gotoAddPDI();
            }
        });


        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
        stringList.add(Constants.MARK_FOR_PDI);
        stringList.add(Constants.PDI_INCOMPLETE);
        stringList.add(Constants.PDI_COMPLETE);


        frameLayout = view.findViewById(R.id.frameLayout);
        mainTabLayout = view.findViewById(R.id.mainTabLayout);
        recyclerViewTopBar = view.findViewById(R.id.recyclerViewTopBar);
        llTopBarPanel = view.findViewById(R.id.llTopBarPanel);
        llMainHead = view.findViewById(R.id.llMainHead);
        tvTopBarItem = view.findViewById(R.id.tvTopBarItem);
        tvTopBarItem.setText(listTopBarPanel.get(0).title);
        recyclerViewTopBar.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewTopBar.setAdapter(topBarAdapter = new TopBarAdapter(listTopBarPanel,activity(),iITopBarAdapter));
        swipeRefreshLayout.setEnabled(false);
        frameLayout.setVisibility(View.GONE);
        appBar = view.findViewById(R.id.appBar);

        layoutParams  = llMainHead.getLayoutParams();
        Logger.d("real content main" , +layoutParams.width + " "  +layoutParams.height);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utility.isNetworkAvailable(activity()))
                {
                    //fetchVehiclesOfTestDrive();
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tvTopBarItem.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.VISIBLE);
        });

        llTopBarPanel.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.GONE);
        });

//        if(Utility.isNetworkAvailable(activity()))
//        {
//            fetchVehiclesOfTestDrive();
//        }
//        else
//        {
//            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
//        }

        viewPager.setAdapter(new VehiclesTypeAdapter(getFragmentManager(),91011,stringList,vehiclesType));
        viewPager.setOffscreenPageLimit(stringList.size());
        Utility.updateTablayout(tabLayout,activity());
        changeListener();
        updateTab(0);



        llMainHead.setOnClickListener(click -> {
            appBar.setExpanded(true,true);
        });

        llMainHead.setOnTouchListener((v, event) -> {
            appBar.setExpanded(true,true);
            return false;
        });


        return view;
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
        tvAssignPDI.setVisibility(position==0?View.VISIBLE:View.GONE);
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


    public void gotoAddPDI()
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,21);
        startActivityForResult(intent,Utility.generateRequestCodes().get("ASSIGN_PDI"));
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
    public Activity activity()
    {
        return getActivity();
    }


    public void fetchVehiclesOfTestDrive()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_FETCH_ALL_VEHICLES + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
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
                    inPDIcomplete = new ArrayList<>();

                    for(int i =0;i<list.size();i++)
                    {
                            Logger.d(list.get(i).Type);
                            if(list.get(i).Type.equalsIgnoreCase(Constants.MARK_FOR_PDI))
                            {
                                inMarkForPDI.add(list.get(i));
                               //break;
                            }
                            else if(list.get(i).Type.equalsIgnoreCase(Constants.PDI_INCOMPLETE))
                            {
                                inPDIIncomplete.add(list.get(i));
                                //break;
                            }
                            else if(list.get(i).Type.equalsIgnoreCase(Constants.PDI_COMPLETE))
                            {
                                inPDIcomplete.add(list.get(i));
                                //break;
                            }
                            else
                            {

                            }

                    }

                    viewPager.setAdapter(new VehiclesTypeAdapter(getFragmentManager(),inMarkForPDI,inPDIIncomplete,inPDIcomplete,1));
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
            ((HelperActivity) activity()).clearSearchText();
            switch (position)
            {
                case 0:
                    tvAssignPDI.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    mainTabLayout.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvAssignPDI.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, PDIViewFragment.newInstance(null,null)).commit();
                    mainTabLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    tvAssignPDI.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new VehiclesTypeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",3);
                    bundle.putString("list",null);
                    bundle.putString("value",Constants.PDI_COMPLETE);
                    fragment.setArguments(bundle);
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    mainTabLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utility.generateRequestCodes().get("ASSIGN_PDI"))
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
                            vehiclesTypeFragment.fetchVehiclesBasedonType();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    showSnackBar("Assigned for pdi",1);

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
        if(requestCode == Utility.generateRequestCodes().get("PDI_FORM"))
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
                            vehiclesTypeFragment.fetchVehiclesBasedonType();
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


    public void updateSearchInAll(String content)
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
                ((VehiclesTypeFragment) getChildFragmentManager().getFragments().get(0)).updateVehiclesBySearch(content);
                break;
        }
    }

}
