package com.maya.vgarages.fragments.garage.overview;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.CustomViewPagerAdapter;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.garage.reviews.GarageReviewsFragment;
import com.maya.vgarages.fragments.garage.services.GarageServicesFragment;
import com.maya.vgarages.interfaces.dialog.IToPickVehicle;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.io.PipedOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GarageOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GarageOverviewFragment extends Fragment implements IFragment, IToPickVehicle {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public IToPickVehicle iToPickVehicle;


    public GarageOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GarageOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GarageOverviewFragment newInstance(String param1, String param2) {
        GarageOverviewFragment fragment = new GarageOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static GarageOverviewFragment newInstance(Garage garage) {
        GarageOverviewFragment fragment = new GarageOverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_garage_overview, container, false);
        iToPickVehicle = this;
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(Utility.generateViewPageTitles(1).size());
        viewPager.setAdapter(new CustomViewPagerAdapter(getFragmentManager(),1, Utility.generateViewPageTitles(1),
                (Garage) getArguments().getSerializable("Garage")));
        Utility.updateTabLayout(tabLayout,activity());
        updateTab(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==1)
                {
                    if(!Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
                    ((HelperActivity)activity()).pickVehicleDialog(iToPickVehicle);
                }
                changeMenuOptions(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                //changeMenuOptions(viewPager.getCurrentItem());
                TextView tv = (TextView) tab.getCustomView();
                if (tv == null)
                    return;
                tv.setTextColor(ContextCompat.getColor(activity(), R.color.colorPrimary));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                TextView tv = (TextView) tab.getCustomView();
                if (tv == null)
                    return;
                tv.setTextColor(ContextCompat.getColor(activity(), R.color.light_new_gray ));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    public void updateCar()
    {
        try
        {
            List<Fragment> list = getFragmentManager().getFragments();
            Logger.d("FRAGMENT SIZE " + list.size());
            try
            {
                ((GarageServicesFragment) (list.get(2))).updateBottomBar();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateTab(int position)
    {
        try
        {
            changeMenuOptions(0);
            for (int i = 0; i < tabLayout.getTabCount(); i++)
            {
                TextView tv = (TextView) tabLayout.getTabAt(i).getCustomView();
                if (tv == null)
                    return;
                tv.setTextColor(ContextCompat.getColor(activity(), position == i ? R.color.colorPrimary : R.color.light_new_gray));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void changeMenuOptions(int value)
    {
        switch (value)
        {
            case 0:
                ((HelperActivity)activity()).hideMenuOption(R.id.menu_cart);
                ((HelperActivity)activity()).hideMenuOption(R.id.menu_add_review);
                ((HelperActivity)activity()).visibleMenuOption(R.id.menu_fav);
                break;

            case 1:
                ((HelperActivity)activity()).visibleMenuOption(R.id.menu_cart);
                ((HelperActivity)activity()).hideMenuOption(R.id.menu_add_review);
                ((HelperActivity)activity()).hideMenuOption(R.id.menu_fav);
                break;

            case 2:
                ((HelperActivity)activity()).hideMenuOption(R.id.menu_cart);
                ((HelperActivity)activity()).visibleMenuOption(R.id.menu_add_review);
                ((HelperActivity)activity()).hideMenuOption(R.id.menu_fav);
                break;
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

    public void updateGarageServices()
    {
        try
        {
            List<Fragment> list = getFragmentManager().getFragments();
            Logger.d("FRAGMENT SIZE " + list.size());
            try
            {
                ((GarageServicesFragment) (list.get(2))).updateGarageService();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void goToVehicles()
    {
        ((HelperActivity) activity()).goToUserVehicles(Utility.generateRequestCodes().get("ADD_VEHICLE"));
    }

    public void updateRatting()
    {
        try
        {
            List<Fragment> list = getFragmentManager().getFragments();
            Logger.d("FRAGMENT SIZE " + list.size());
            try
            {
                ((GarageReviewsFragment) (list.get(3))).fetchReviews();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
