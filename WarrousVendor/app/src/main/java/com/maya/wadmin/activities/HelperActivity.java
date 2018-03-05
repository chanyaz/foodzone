package com.maya.wadmin.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.delivery.VehicleDeliveryFragment;
import com.maya.wadmin.fragments.fleet.FleetHomeFragment;
import com.maya.wadmin.fragments.lot.LOTFragment;
import com.maya.wadmin.fragments.lot.add.AddPreparingLotFragment;
import com.maya.wadmin.fragments.lot.form.LotPreparationFormFragment;
import com.maya.wadmin.fragments.map.CustomMapFragment;
import com.maya.wadmin.fragments.other.FilterFragment;
import com.maya.wadmin.fragments.pdi.PDIHomeFragment;
import com.maya.wadmin.fragments.pdi.add.AddPDIFragment;
import com.maya.wadmin.fragments.pdi.form.PdiFormFragment;
import com.maya.wadmin.fragments.rules_and_alerts.AlertsAndRulesFragment;
import com.maya.wadmin.fragments.rules_and_alerts.view.AddAlertFragment;
import com.maya.wadmin.fragments.rules_and_alerts.violations.ViolationVehiclesOfAlertFragment;
import com.maya.wadmin.fragments.testdrive.TestDriveHomeFragment;
import com.maya.wadmin.fragments.testdrive.add.AddNewTestDriveFragment;
import com.maya.wadmin.fragments.vehicle.find.FindFragment;
import com.maya.wadmin.fragments.vehicle.location.VehicleLocationFragment;
import com.maya.wadmin.fragments.vehicle.overview.VehicleOverviewFragment;
import com.maya.wadmin.fragments.zones.ZonesHomeFragment;
import com.maya.wadmin.fragments.zones.add.CreateZoneFragment;
import com.maya.wadmin.interfaces.activities.IActivity;
import com.maya.wadmin.interfaces.fragments.delivery.IVehicleArrivalFragment;
import com.maya.wadmin.interfaces.fragments.rules_and_alerts.IAlertsAndRulesFragment;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.DeliveryTruck;
import com.maya.wadmin.models.GroupFilter;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.Zone;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class HelperActivity extends AppCompatActivity implements IActivity, IVehicleArrivalFragment,IAlertsAndRulesFragment {

    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    TextView tvTitle;
    String previousTitle = "";

    // search purpose
    Toolbar searchToolbar;
    public MenuItem searchViewItem, locationItem;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        tvTitle = findViewById(R.id.tvTitle);


        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_bar_back);


        setUpFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        int fragmentKey = getIntent().getIntExtra(Constants.FRAGMENT_KEY,0);
        MenuItem filter = null;
        if(fragmentKey>0)
        {
            switch (fragmentKey)
            {
                case 1:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 2:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 3:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 4:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    generateSearchView(menu);
                    break;
                case 5:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 6:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 7:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 11:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 12:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 21:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 31:
                    getMenuInflater().inflate(R.menu.vehicle_menu, menu);
                    filter = menu.findItem(R.id.nav_filter);
                    filter.setVisible(false);
                    generateSearchView(menu);
                    break;
                case 333:
                    getMenuInflater().inflate(R.menu.zone_menu, menu);
                    break;
                    
                case 41:
                    getMenuInflater().inflate(R.menu.filter_menu, menu);
                    break;
                case 111:
                    getMenuInflater().inflate(R.menu.find_vehicle_menu, menu);
                    locationItem = menu.findItem(R.id.nav_location);
                    break;
            }

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int fragmentKey = getIntent().getIntExtra(Constants.FRAGMENT_KEY,0);
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.nav_search:
                switch (fragmentKey)
                {
                    case 333:
                        openPlacesSearch();
                        break;
                    case 4:

                        break;
                }
                break;
            case R.id.nav_save:
                switch (fragmentKey)
                {
                    case 333:
                        generateZone();
                        break;
                    case 41:
                        applyFilters();
                        break;
                }
                break;
            case R.id.nav_filter:
                switch (fragmentKey)
                {
                    case 4:
                        openFilters();
                        break;
                }
                break;
            case R.id.nav_clear_all:
                switch (fragmentKey)
                {
                    case 41:
                        resetAllFilters();
                        break;
                }
                break;
            case R.id.nav_location:
                switch (fragmentKey)
                {
                    case 111:
                        ((VehicleOverviewFragment) getSupportFragmentManager().getFragments().get(0)).openLocateVehicle();
                        break;
                }
        }
        return true;
    }

    public void addLocationOfVehicle(Vehicle vehicle)
    {
        if(locationItem!=null)
        {
            locationItem.setVisible(false);
        }
        previousTitle = tvTitle.getText().toString();
        changeTitle("Locate Vehicle");
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, VehicleLocationFragment.newInstance(vehicle)).addToBackStack("Map").commitAllowingStateLoss();
    }

    private void applyFilters()
    {
        ((FilterFragment) getSupportFragmentManager().getFragments().get(0)).applyFilters();
    }

    private void resetAllFilters()
    {
        ((FilterFragment)getSupportFragmentManager().getFragments().get(0)).clearAll();
    }

    private void openFilters()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,41);
        String content = null;
        if(((FindFragment) getSupportFragmentManager().getFragments().get(0)).groupFilter!=null)
        {
           content = new Gson().toJson(((FindFragment) getSupportFragmentManager().getFragments().get(0)).groupFilter, new TypeToken<GroupFilter>() {}.getType());
        }
        intent.putExtra("GroupFilter",content);
        getSupportFragmentManager().getFragments().get(0).startActivityForResult(intent,Utility.generateRequestCodes().get("APPLY_FILTER"));
    }

    private void generateSearchView(Menu menu)
    {
        searchViewItem = menu.findItem(R.id.nav_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);

        // add transparent background
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);


        // removing search icon
        ImageView searchViewIcon =  (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);
        linearLayoutSearchView.addView(searchViewIcon);
        searchViewIcon.setAdjustViewBounds(true);
        searchViewIcon.setMaxWidth(0);
        searchViewIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        searchViewIcon.setImageDrawable(null);

        // search white icon
        ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear_white);

        // change typeface
        TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"));

        /* Code for changing the textcolor and hint color for the search view */
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.parseColor("#FFFFFF"));
        searchAutoComplete.setTextColor(Color.parseColor("#FFFFFF"));
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                textChangeInSearch(newText);
                return false;
            }
        });
    }


    private void generateZone()
    {
        CreateZoneFragment createZoneFragment = (CreateZoneFragment) getSupportFragmentManager().getFragments().get(0);
        createZoneFragment.addZone();
    }

    public void openPlacesSearch()
    {
        try
        {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            getSupportFragmentManager().getFragments().get(0).startActivityForResult(intent, Utility.generateRequestCodes().get("OPEN_PLACES_SEARCH"));
        }
        catch (GooglePlayServicesRepairableException e)
        {
            // TODO: Handle the error.
        }
        catch (GooglePlayServicesNotAvailableException e)
        {
            // TODO: Handle the error.
        }
    }


    @Override
    public void onBackPressed()
    {
        Logger.d("FRAGMENT COUNT",""+getSupportFragmentManager().getBackStackEntryCount());


        if(getIntent().getIntExtra(Constants.FRAGMENT_KEY,0)==611)
        {
            AddAlertFragment addAlertFragment = (AddAlertFragment) getSupportFragmentManager().getFragments().get(0);
            if(addAlertFragment!=null)
            {
                if(addAlertFragment.getChildFragmentManager().getBackStackEntryCount()>1)
                {
                    addAlertFragment.getChildFragmentManager().popBackStack();
                    addAlertFragment.setUpAllInitial();
                }
                else
                {
                    finish();
                }
            }

        }
        else if(getSupportFragmentManager().getBackStackEntryCount()>1)
        {
            if(previousTitle.length()>0)
            {
                changeTitle(previousTitle);
            }
            getSupportFragmentManager().popBackStack();
            if(locationItem!=null)
            {
                locationItem.setVisible(true);
            }
        }
        else
        finish();
    }

    @Override
    public void changeTitle(String title)
    {
        tvTitle.setText(title);
    }

    @Override
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return this;
    }



    public void setUpFragment()
    {
        Intent intent = getIntent();
        int fragmentKey = intent.getIntExtra(Constants.FRAGMENT_KEY,0);
        Logger.d(Constants.FRAGMENT_KEY,""+fragmentKey);
        if(fragmentKey>0)
        {
            Fragment fragment = null;

            switch (fragmentKey)
            {
                case 1:
                    changeTitle("Lot Management");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    fragment = LOTFragment.newInstance(null,null);
                    break;
                case 2:
                    changeTitle("PDI");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    fragment = PDIHomeFragment.newInstance(null,null);
                    break;
                case 3:
                    changeTitle("Test Drive");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    fragment = TestDriveHomeFragment.newInstance(null,null);
                    break;
                case 4:
                    changeTitle("Find Vehicle");
                    fragment = FindFragment.newInstance(null,null);
                    break;
                case 5:
                    changeTitle("Vehicle Delivery");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    fragment = VehicleDeliveryFragment.newInstance(null,null);
                    break;
                case 6:
                    changeTitle("Rules & Alerts");
                    fragment = AlertsAndRulesFragment.newInstance(null,null);
                    break;
                case 7:
                    changeTitle("Manage Zones");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    fragment = ZonesHomeFragment.newInstance(null,null);
                    break;
                case 11:
                    changeTitle("Assign for Preparing");
                    fragment = AddPreparingLotFragment.newInstance(null,null);
                    break;
                case 21:
                    changeTitle("Assign for PDI");
                    fragment = AddPDIFragment.newInstance(null,null);
                    break;

                case 31:
                    changeTitle("New Test Drive");
                    fragment = AddNewTestDriveFragment.newInstance(null,null);
                    break;
                case 111:
                    changeTitle("Vehicle Overview");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }

                    if(getIntent().getSerializableExtra("vehicle")!=null)
                    {
                        fragment = VehicleOverviewFragment.newInstance((Vehicle) getIntent().getSerializableExtra("vehicle"));
                    }
                    else
                    {
                        fragment = VehicleOverviewFragment.newInstance(null, null);
                    }
                    break;


                case 222:
                    changeTitle("PDI Form");
                    if(getIntent().getSerializableExtra("vehicle")!=null)
                    {
                    fragment = PdiFormFragment.newInstance((Vehicle) getIntent().getSerializableExtra("vehicle"),getIntent().getStringExtra("value"));
                    }
                    else
                    fragment = PdiFormFragment.newInstance(null,null,null);

                    break;

                case 333:
                    changeTitle("Create Zone");
                    if(getIntent().getSerializableExtra("Zone")==null)
                    {
                        fragment = CreateZoneFragment.newInstance(null, null);
                    }
                    else
                    {
                        changeTitle("Edit Zone");
                        fragment = CreateZoneFragment.newInstance((Zone)getIntent().getSerializableExtra("Zone"));
                    }
                    break;

                case 223:
                    changeTitle("Lot Preparation Form");
                    if(getIntent().getSerializableExtra("vehicle")!=null)
                    {
                        fragment = LotPreparationFormFragment.newInstance((Vehicle) getIntent().getSerializableExtra("vehicle"),getIntent().getStringExtra("value"));
                    }
                    else
                    fragment = LotPreparationFormFragment.newInstance(null,null,null);
                    break;

                case 611:
                    changeTitle("Create Alert");
                    if(getIntent().getSerializableExtra("AlertRule")==null)
                    {
                        fragment = AddAlertFragment.newInstance(null, null);
                    }
                    else
                    {
                        changeTitle("Edit Alert");
                        fragment = AddAlertFragment.newInstance((AlertRule)getIntent().getSerializableExtra("AlertRule"));
                    }
                    break;
                case 612:
                    changeTitle("Violation Vehicles");
                    if(getIntent().getSerializableExtra("AlertRule")!=null)
                    {
                        fragment = ViolationVehiclesOfAlertFragment.newInstance((AlertRule)getIntent().getSerializableExtra("AlertRule"));
                    }
                    break;
                case 12:
                    changeTitle("Fleet Management");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    fragment = FleetHomeFragment.newInstance(null,null);
                    break;
                case 41:
                    changeTitle("Filters");
                    if(getIntent().getStringExtra("GroupFilter")!=null)
                    {
                        fragment = FilterFragment.newInstance(getIntent().getStringExtra("GroupFilter"));
                    }
                    else
                    {
                        fragment = FilterFragment.newInstance(null);
                    }
                    break;
            }


            if(fragment!=null)
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack("START").commitAllowingStateLoss();

        }
        else
        {

        }


    }


    @Override
    public void addMapFragment(DeliveryTruck deliveryTruck, int position)
    {
        previousTitle = tvTitle.getText().toString();
        changeTitle("Delivery "+deliveryTruck.TruckName);
        Fragment fragment = new CustomMapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE,0);
        bundle.putSerializable("deliveryTruck",deliveryTruck);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment).addToBackStack("Map").commitAllowingStateLoss();
    }

    @Override
    public void addCreateAlertView()
    {
        previousTitle = tvTitle.getText().toString();
        changeTitle("Create Alert");
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, AddAlertFragment.newInstance(null,null)).addToBackStack("Add Alert").commitAllowingStateLoss();
    }


    private void textChangeInSearch(String content)
    {
        if(content==null)
        {
            return;
        }
        content = content.trim().toLowerCase();
        int fragmentKey = getIntent().getIntExtra(Constants.FRAGMENT_KEY,0);
        try
        {
            switch (fragmentKey)
            {
                case 1:
                    ((LOTFragment) getSupportFragmentManager().getFragments().get(0)).updateVehiclesInAll(content);
                    break;
                case 2:
                    ((PDIHomeFragment) getSupportFragmentManager().getFragments().get(0)).updateSearchInAll(content);
                    break;
                case 3:
                    ((TestDriveHomeFragment) getSupportFragmentManager().getFragments().get(0)).updateSearchInAll(content);
                    break;
                case 4:
                    ((FindFragment) getSupportFragmentManager().getFragments().get(0)).updateListBySearch(content);
                    break;
                case 5:
                    ((VehicleDeliveryFragment) getSupportFragmentManager().getFragments().get(0)).updateSearchInAll(content);
                    break;
                case 6:
                    ((AlertsAndRulesFragment) getSupportFragmentManager().getFragments().get(0)).updateAlertsInViewPager(content);
                    break;
                case 7:
                    ((ZonesHomeFragment) getSupportFragmentManager().getFragments().get(0)).updateZonesInAll(content);
                    break;
                case 12:
                    ((FleetHomeFragment) getSupportFragmentManager().getFragments().get(0)).updateVehiclesInAll(content);
                    break;
                case 21:
                    ((AddPDIFragment) getSupportFragmentManager().getFragments().get(0)).updateSearchInAll(content);
                    break;
                case 31:
                    ((AddNewTestDriveFragment) getSupportFragmentManager().getFragments().get(0)).updateSearchInAll(content);
                    break;
                case 11:
                    ((AddPreparingLotFragment) getSupportFragmentManager().getFragments().get(0)).updateSearchInAll(content);
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void clearSearchText()
    {
        if(searchView!=null)
        {
            searchView.setQuery("",true);
            searchView.clearFocus();
            if(searchViewItem!=null)
            {
                searchViewItem.collapseActionView();
            }
//            else
//            {
//                onBackPressed();
//            }
        }
    }



}
