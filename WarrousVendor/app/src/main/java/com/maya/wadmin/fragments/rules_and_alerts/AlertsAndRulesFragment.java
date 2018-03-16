package com.maya.wadmin.fragments.rules_and_alerts;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.custom.CustomWadminAdapter;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.rules_and_alerts.view.AlertsFragment;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.interfaces.fragments.rules_and_alerts.IAlertsAndRulesFragment;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertsAndRulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertsAndRulesFragment extends Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    IAlertsAndRulesFragment iAlertsAndRulesFragment;
    TabLayout tabLayout;
    public ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;
    List<String> list = new ArrayList<>();
    TextView tvAddAlert;
    CustomWadminAdapter customWadminAdapter;



    public AlertsAndRulesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertsAndRulesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertsAndRulesFragment newInstance(String param1, String param2) {
        AlertsAndRulesFragment fragment = new AlertsAndRulesFragment();
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
        View view = inflater.inflate(R.layout.fragment_alerts_and_rules, container, false);
        iAlertsAndRulesFragment = (HelperActivity)activity();
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        tabLayout = view.findViewById(R.id.tab_layout);
        tvAddAlert = view.findViewById(R.id.tvAddAlert);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        list.add("Speed");
        list.add("Geofence");
        list.add("Mileage");
        list.add("Theft");
        list.add("Delivery");
        list.add("DTC");
        list.add("Billing");
        list.add("Vehicle");
        list.add("Customer");

        viewPager.setAdapter(customWadminAdapter = new CustomWadminAdapter(getFragmentManager(),1,list));
        viewPager.setOffscreenPageLimit(list.size());
        changeListener();
        Utility.updateTablayout(tabLayout,activity());
        updateTab(0);

        tvAddAlert.setOnClickListener(click ->
        {
            if(Utility.isNetworkAvailable(activity()))
            {
                Intent intent = new Intent(activity(),HelperActivity.class);
                intent.putExtra(Constants.FRAGMENT_KEY, 611);
                startActivityForResult(intent,Utility.generateRequestCodes().get("ADD_ALERT"));
            }
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

    }

    public void updateTab(int position)
    {
        try {

            for (int i = 0; i < tabLayout.getTabCount(); i++) {

                TextView tv = (TextView) tabLayout.getTabAt(i).getCustomView();
                if (tv == null)
                    return;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Utility.generateRequestCodes().get("ADD_ALERT"))
        {
            if(data!=null)
            {
                try
                {
                    int value = 0;
                    viewPager.setCurrentItem( value = data.getIntExtra("position", 0), true);
                    List<Fragment> list = getFragmentManager().getFragments();
                    Logger.d("FRAGMENT SIZE " + list.size());
                    try
                    {
                        ((AlertsFragment) (list.get(value+1))).fetchAlertRules();

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


    public void updateAlertsInViewPager(String content)
    {
            try
            {
                List<Fragment> list = getFragmentManager().getFragments();
                Logger.d("FRAGMENT SIZE " + list.size());
                if(list.size()>1)
                {
                    try
                    {
                        for (int i = 1;i<list.size()-1;i++)
                        {
                            ((AlertsFragment) (list.get(i))).updateAlertsBySearch(content.trim());
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

    }


}
