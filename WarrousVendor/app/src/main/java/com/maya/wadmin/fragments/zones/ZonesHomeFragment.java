package com.maya.wadmin.fragments.zones;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.custom.TopBarAdapter;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.delivery.view.AllDeliveryVehiclesFragment;
import com.maya.wadmin.fragments.other.DummyFragment;
import com.maya.wadmin.fragments.zones.view.ZonesViewFragment;
import com.maya.wadmin.interfaces.custom.ITopBarAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.TopBarPanel;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZonesHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZonesHomeFragment extends Fragment implements IFragment, ITopBarAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    ITopBarAdapter iITopBarAdapter;
    @BindView(R.id.llTopBarPanel) LinearLayout llTopBarPanel;

    @BindView(R.id.llMainHead) LinearLayout llMainHead;
    @BindView(R.id.tvTopBarItem) TextView tvTopBarItem;

    TopBarAdapter topBarAdapter;
    List<TopBarPanel> listTopBarPanel = Utility.getTopBarPanelElements(1);
    int previous = -1;

    @BindView(R.id.appBar) AppBarLayout appBar;
    ViewGroup.LayoutParams layoutParams;
    @BindView(R.id.recyclerViewTopBar) RecyclerView recyclerViewTopBar;
    @BindView(R.id.tvCreateZone) TextView tvCreateZone;



    public ZonesHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZonesHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZonesHomeFragment newInstance(String param1, String param2) {
        ZonesHomeFragment fragment = new ZonesHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_zones_home, container, false);
        ButterKnife.bind(this,view);

        iITopBarAdapter = this;
        tvTopBarItem.setText(listTopBarPanel.get(0).title);
        recyclerViewTopBar.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewTopBar.setAdapter(topBarAdapter = new TopBarAdapter(listTopBarPanel,activity(),iITopBarAdapter));

        tvCreateZone.setOnClickListener(click -> goToCreateZone() );




        layoutParams  = llMainHead.getLayoutParams();
        tvTopBarItem.setText(listTopBarPanel.get(0).title);
        setUpAction(0);
        previous = 0;
        tvTopBarItem.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.VISIBLE);
        });

        llTopBarPanel.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.GONE);
        });
        llTopBarPanel.setVisibility(View.GONE);

        llMainHead.setOnClickListener(click -> {
            appBar.setExpanded(true,true);
        });

        llMainHead.setOnTouchListener((v, event) -> {
            appBar.setExpanded(true,true);
            return false;
        });

        changeListener();

        return view;
    }


    public void changeListener()
    {

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

    private void goToCreateZone()
    {
        Intent intent  = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,333);
        startActivityForResult(intent,Utility.generateRequestCodes().get("ADD_ZONE"));
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
        tvCreateZone.setVisibility(position==0?View.VISIBLE:View.GONE);
        if(previous == position)
        {
            return;
        }
        else
        {
            ((HelperActivity) getActivity()).clearSearchText();
            switch (position)
            {
                case 0:
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, ZonesViewFragment.newInstance(null,null)).commit();
                    break;

                case 1:
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, ZonesViewFragment.newInstance(null,"5")).commit();
                    break;
                case 2:
                    getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, DummyFragment.newInstance(null,null)).commit();
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Utility.generateRequestCodes().get("ADD_ZONE"))
        {
            if(data!=null)
            {
                try
                {
                    try
                    {
                        ZonesViewFragment zonesViewFragment = (ZonesViewFragment) getChildFragmentManager().getFragments().get(0);
                        zonesViewFragment.fetchAllZones();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    showSnackBar("Updating alerts", 1);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public void updateZonesInAll(String content)
    {
        Logger.d(content);
        switch (previous)
        {
            case 0:
                ((ZonesViewFragment) getChildFragmentManager().getFragments().get(0)).updateSearchZones(content);
                break;
            case 1:
                ((ZonesViewFragment) getChildFragmentManager().getFragments().get(0)).updateSearchZones(content);
                break;
            case 2:

                break;
        }


    }
}
