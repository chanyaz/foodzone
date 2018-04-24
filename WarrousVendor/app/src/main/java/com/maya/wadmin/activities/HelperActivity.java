package com.maya.wadmin.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.adapters.custom.OptionsAdapter;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.delivery.VehicleDeliveryFragment;
import com.maya.wadmin.fragments.fleet.FleetHomeFragment;
import com.maya.wadmin.fragments.home.options.FAQFragment;
import com.maya.wadmin.fragments.home.options.ProfileFragment;
import com.maya.wadmin.fragments.location.LocationOfVehicleFragment;
import com.maya.wadmin.fragments.lot.LOTFragment;
import com.maya.wadmin.fragments.lot.add.AddPreparingLotFragment;
import com.maya.wadmin.fragments.lot.form.LotPreparationFormFragment;
import com.maya.wadmin.fragments.map.CustomMapFragment;
import com.maya.wadmin.fragments.messages.ChatBoxMessagesFragment;
import com.maya.wadmin.fragments.notifications.NotificationsFragment;
import com.maya.wadmin.fragments.other.FilterFragment;
import com.maya.wadmin.fragments.pdi.PDIHomeFragment;
import com.maya.wadmin.fragments.pdi.add.AddPDIFragment;
import com.maya.wadmin.fragments.pdi.form.PdiFormFragment;
import com.maya.wadmin.fragments.rules_and_alerts.AlertsAndRulesFragment;
import com.maya.wadmin.fragments.rules_and_alerts.view.AddAlertFragment;
import com.maya.wadmin.fragments.rules_and_alerts.view.AlertsFragment;
import com.maya.wadmin.fragments.rules_and_alerts.violations.ViolationVehiclesOfAlertFragment;
import com.maya.wadmin.fragments.scan.ScanVinFragment;
import com.maya.wadmin.fragments.testdrive.TestDriveHomeFragment;
import com.maya.wadmin.fragments.testdrive.add.AddNewTestDriveFragment;
import com.maya.wadmin.fragments.vehicle.find.FindFragment;
import com.maya.wadmin.fragments.vehicle.location.VehicleLocationFragment;
import com.maya.wadmin.fragments.vehicle.overview.VehicleOverviewFragment;
import com.maya.wadmin.fragments.zones.ZonesHomeFragment;
import com.maya.wadmin.fragments.zones.add.CreateZoneFragment;
import com.maya.wadmin.fragments.zones.view.ZonesViewFragment;
import com.maya.wadmin.interfaces.activities.IActivity;
import com.maya.wadmin.interfaces.custom.IOptionsAdapter;
import com.maya.wadmin.interfaces.fragments.delivery.IVehicleArrivalFragment;
import com.maya.wadmin.interfaces.fragments.rules_and_alerts.IAlertsAndRulesFragment;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.DeliveryTruck;
import com.maya.wadmin.models.GroupFilter;
import com.maya.wadmin.models.Options;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.Zone;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperActivity extends AppCompatActivity implements IActivity, IVehicleArrivalFragment,IAlertsAndRulesFragment ,IOptionsAdapter
{


    String previousTitle = "";
    public MenuItem searchViewItem, locationItem;
    SearchView searchView;
    BottomSheetBehavior bottomSheetBehavior;
    IOptionsAdapter iOptionsAdapter;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.fragment_bottom_sheet)
    FrameLayout bottomSheetLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.llClose)
    LinearLayout llClose;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        iOptionsAdapter = this;


        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        llClose.setOnClickListener(click -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });


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

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }

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
                case 8:
                    changeTitle("User Profile");
                    fragment = ProfileFragment.newInstance(null,null);
                    break;
                case 9:
                    changeTitle("FAQ'S");
                    fragment = FAQFragment.newInstance(null,null);
                    break;
                case 11:
                    changeTitle("Assign for Preparing");
                    if(getIntent().getSerializableExtra("vehicle")==null)
                        fragment = AddPreparingLotFragment.newInstance(null,null);
                    else
                        fragment = AddPreparingLotFragment.newInstance((Vehicle) getIntent().getSerializableExtra("vehicle"));
                    break;
                case 21:
                    changeTitle("Assign for PDI");
                    if(getIntent().getSerializableExtra("vehicle")==null)
                    fragment = AddPDIFragment.newInstance(null,null);
                    else
                    fragment = AddPDIFragment.newInstance((Vehicle) getIntent().getSerializableExtra("vehicle"));
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

                case 112:
                    changeTitle("Locate Vehicle");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0);
                    }
                    if(getIntent().getSerializableExtra("vehicle")!=null)
                    {
                        fragment = VehicleLocationFragment.newInstance((Vehicle) getIntent().getSerializableExtra("vehicle"));
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
                        fragment = AddAlertFragment.newInstance(getIntent().getIntExtra("CategoryId",0),(AlertRule)getIntent().getSerializableExtra("AlertRule"));
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

                case 701:
                    changeTitle("Scan Vin");
                    fragment = ScanVinFragment.newInstance(null,null);
                    break;

                case 702:
                    changeTitle("Messages");
                    fragment = ChatBoxMessagesFragment.newInstance(null,null);
                    break;

                case 703:
                    changeTitle("Notifications");
                    fragment = NotificationsFragment.newInstance(null,null);
                    break;

                case 704:
                    changeTitle("Location");
                    fragment = LocationOfVehicleFragment.newInstance(null,null);
                    break;
            }


            if(fragment!=null) // add for 5 only
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("START").commitAllowingStateLoss();
            }

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

        // hide search and back press enable search view
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

    public void openOptions(int value,Vehicle vehicle)
    {
        Logger.d("TYPE ",vehicle.Type + " " +vehicle.VehicleId);
        switch (value)
        {
            case 1: // test drive lot
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(1,Utility.generateOptions(1),activity(),iOptionsAdapter,new Gson().toJson(vehicle)));
                break;
            case 2: // test drive / return
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(2,Utility.generateOptions(2),activity(),iOptionsAdapter,new Gson().toJson(vehicle)));
                break;
            case 3:// marked for pdi
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(3,Utility.generateOptions(3),activity(),iOptionsAdapter,new Gson().toJson(vehicle)));
                break;

            case 4:// all vehicle default actions locate and overview
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(4,Utility.generateOptions(4),activity(),iOptionsAdapter,new Gson().toJson(vehicle)));
                break;

            case 5:// delivery received in fleet and lot
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(5,Utility.generateOptions(5),activity(),iOptionsAdapter,new Gson().toJson(vehicle)));
                break;

        }
    }

    public void openOptionsForZone(int value,Zone zone)
    {
        switch (value)
        {
            case 11: // test drive lot
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(11,Utility.generateOptions(11),activity(),iOptionsAdapter,new Gson().toJson(zone)));
                break;
        }
    }

    public void openOptionsForAlerts(int value,AlertRule alertRule)
    {
        switch (value)
        {
            case 12: // test drive lot
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                recyclerView.setAdapter(new OptionsAdapter(12,Utility.generateOptions(12),activity(),iOptionsAdapter,new Gson().toJson(alertRule)));
                break;
        }
    }

    @Override
    public void onOptionClick(Options options, int type, int position, String content) {
        try {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            switch (type) {
                case 1:
                    switch (position) {
                        case 0:

                            break;
                        case 1:
                            if (Utility.isNetworkAvailable(activity()))
                                ((TestDriveHomeFragment) getSupportFragmentManager().getFragments().get(0)).deleteVehicleFromLot(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                                }.getType()));
                            else {
                                showSnackBar(Constants.PLEASE_CHECK_INTERNET, 0);
                            }
                            break;
                        case 2:
                            goToVehicleOverView(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                        case 3:
                            goToLocateVehicle(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                    }
                    break;

                case 2:
                    switch (position) {
                        case 0:
                            goToVehicleOverView(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                        case 1:
                            goToLocateVehicle(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                    }
                    break;
                case 3:
                    switch (position)
                    {
                        case 0:
                            if (Utility.isNetworkAvailable(activity()))
                                ((PDIHomeFragment) getSupportFragmentManager().getFragments().get(0)).gotoAddPDI(new Gson().fromJson(content, new TypeToken<Vehicle>() {}.getType()));
                            else {
                                showSnackBar(Constants.PLEASE_CHECK_INTERNET, 0);
                            }
                            break;
                        case 1:
                            goToVehicleOverView(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                        case 2:
                            goToLocateVehicle(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                    }
                    break;

                case 4:
                    switch (position) {
                        case 0:
                            goToVehicleOverView(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                        case 1:
                            goToLocateVehicle(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                    }
                    break;

                case 5:
                    switch (position)
                    {
                        case 0:
                            if (Utility.isNetworkAvailable(activity()))
                            {
                                int fragmentKey = getIntent().getIntExtra(Constants.FRAGMENT_KEY,0);
                                if(fragmentKey==12)
                                ((FleetHomeFragment) getSupportFragmentManager().getFragments().get(0)).gotoAddPreparing(new Gson().fromJson(content, new TypeToken<Vehicle>() {}.getType()));
                                else if(fragmentKey==1)
                                {
                                ((LOTFragment) getSupportFragmentManager().getFragments().get(0)).gotoAddPreparing(new Gson().fromJson(content, new TypeToken<Vehicle>() {}.getType()));
                                }
                                else
                                {

                                }
                            }
                            else
                            {
                                showSnackBar(Constants.PLEASE_CHECK_INTERNET, 0);
                            }
                            break;
                        case 1:
                            goToVehicleOverView(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                        case 2:
                            goToLocateVehicle(new Gson().fromJson(content, new TypeToken<Vehicle>() {
                            }.getType()));
                            break;
                    }
                    break;
                case 11:
                    ZonesHomeFragment zonesHomeFragment = (ZonesHomeFragment) getSupportFragmentManager().getFragments().get(0);
                    ZonesViewFragment zonesViewFragment = (ZonesViewFragment) zonesHomeFragment.getChildFragmentManager().getFragments().get(0);
                    switch (position) {
                        case 0:
                            zonesViewFragment.editZone(new Gson().fromJson(content, new TypeToken<Zone>() {
                            }.getType()), -1);
                            break;
                        case 1:
                            zonesViewFragment.deleteZone(new Gson().fromJson(content, new TypeToken<Zone>() {
                            }.getType()), -1);
                            break;
                    }
                case 12:
                    AlertsAndRulesFragment alertsAndRulesFragment = (AlertsAndRulesFragment) getSupportFragmentManager().getFragments().get(0);
                    AlertsFragment alertsFragment = (AlertsFragment) alertsAndRulesFragment.getFragmentManager().getFragments().get(alertsAndRulesFragment.viewPager.getCurrentItem() + 1);
                    switch (position) {
                        case 0:
                            alertsFragment.cloneAlert(new Gson().fromJson(content, new TypeToken<AlertRule>() {
                            }.getType()), -1);
                            break;
                        case 1:
                            alertsFragment.edit(new Gson().fromJson(content, new TypeToken<AlertRule>() {
                            }.getType()), -1);
                            break;
                        case 2:
                            alertsFragment.deleteAlert(new Gson().fromJson(content, new TypeToken<AlertRule>() {
                            }.getType()), -1);
                            break;
                        case 3:
                            alertsFragment.openViolationVehicles(new Gson().fromJson(content, new TypeToken<AlertRule>() {
                            }.getType()), -1);
                            break;

                    }
                    break;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void goToVehicleOverView(Vehicle vehicle)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,111);
        intent.putExtra("vehicle",vehicle);
        startActivity(intent);
    }

    public void goToLocateVehicle(Vehicle vehicle)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,112);
        intent.putExtra("vehicle",vehicle);
        startActivity(intent);
    }


}
