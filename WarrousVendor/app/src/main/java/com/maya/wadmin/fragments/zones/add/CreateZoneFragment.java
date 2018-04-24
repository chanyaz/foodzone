package com.maya.wadmin.fragments.zones.add;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.custom.KeyWordAdapter;
import com.maya.wadmin.adapters.fragments.rules_and_alerts.ActionChannelAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.custom.IKeyWordAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AlertActionChannel;
import com.maya.wadmin.models.Zone;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;
import com.warkiz.widget.IndicatorSeekBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateZoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateZoneFragment extends Fragment implements IFragment, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener ,IKeyWordAdapter
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    PolygonOptions rectOptions;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    GoogleMap map;
    @BindView(R.id.mapView) MapView mapView;
    Place place;
    int min = 100;
    List<LatLng> polygonsList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    boolean isPolygon = false, isHand = true, isCircle = false, isLocation = false;
    boolean geofence = false, polygon = false;

    @BindView(R.id.llLocation) LinearLayout llLocation;
    @BindView(R.id.llPolyline) LinearLayout llPolyline;
    @BindView(R.id.llHand) LinearLayout llHand;
    @BindView(R.id.llClear) LinearLayout llClear;
    @BindView(R.id.llCircle) LinearLayout llCircle;

    @BindView(R.id.imgLocation) ImageView imgLocation;
    @BindView(R.id.imgPolygon) ImageView imgPolygon;
    @BindView(R.id.imgHand) ImageView imgHand;
    @BindView(R.id.imgCircle) ImageView imgCircle;

    @BindView(R.id.seekbar) IndicatorSeekBar seekBar;
    @BindView(R.id.llhead1) LinearLayout llhead1;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.etKeyword) EditText etKeyword;
    @BindView(R.id.imgAdd) ImageView imgAdd;
    @BindView(R.id.etZoneName) EditText etZoneName;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.rlOverView) RelativeLayout rlOverView;

    IKeyWordAdapter iKeyWordAdapter;
    KeyWordAdapter keyWordAdapter;

    Zone zone;



    public CreateZoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateZoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateZoneFragment newInstance(String param1, String param2) {
        CreateZoneFragment fragment = new CreateZoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CreateZoneFragment newInstance(Zone zone)
    {
        CreateZoneFragment fragment = new CreateZoneFragment();
        Bundle args = new Bundle();
        args.putSerializable("Zone", zone);
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
        View view = inflater.inflate(R.layout.fragment_create_zone, container, false);
        ButterKnife.bind(this,view);

        iKeyWordAdapter = this;
        llhead1.setVisibility(View.GONE);
        llhead1.setOnClickListener(click -> {});


        recyclerView.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(keyWordAdapter = new KeyWordAdapter(stringList,activity(),iKeyWordAdapter));
        recyclerView.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);


        seekBar.setVisibility(View.GONE);

        seekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                if(min>progress)
                {
                    seekBar.setProgress(min);
                }
                addCircleGeofences(polygonsList.get(0));
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });



        llClear.setOnClickListener(click ->
                {
                    cleanMapAndOther();
                }
        );
        llHand.setOnClickListener(click ->
        {
            if(isHand)
            {
                return;
            }
            isHand = true;
            isCircle = false;
            isPolygon = false;
            isLocation = false;
            updateUI();
        });
        llLocation.setOnClickListener(click ->{
            if(isLocation)
            {
                return;
            }
            isHand = false;
            isCircle = false;
            isPolygon = false;
            isLocation = true;
            updateUI();
            cleanMapAndOther();
        });
        llPolyline.setOnClickListener(click ->
        {
            if(isPolygon)
            {
                return;
            }
            isHand = false;
            isCircle = false;
            isPolygon = true;
            isLocation = false;
            updateUI();
            cleanMapAndOther();
            geofence = false;
            polygon = true;
        });
        llCircle.setOnClickListener(click -> {
            if(isCircle)
            {
                return;
            }
            isHand = false;
            isCircle = true;
            isPolygon = false;
            isLocation = false;
            updateUI();
            cleanMapAndOther();
            geofence = true;
            polygon = false;
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        updateUI();

        imgAdd.setOnClickListener(click -> addKeyWord());


        return view;
    }

    private void addKeyWord()
    {
        if((""+etKeyword.getText()).trim().length()>0)
        {
            stringList.add(etKeyword.getText().toString().trim());
            etKeyword.setText("");
            keyWordAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            Utility.hideKeyboard(activity());
        }
        else
        {
            Utility.hideKeyboard(activity());
            showSnackBar("Please add key word",2);
        }
    }

    public void cleanMapAndOther()
    {
        seekBar.setVisibility(View.GONE);
        llhead1.setVisibility(View.GONE);
        map.clear();
        polygonsList = new ArrayList<>();
        geofence = false;
        polygon = false;
    }

    public void updateUI()
    {
        imgCircle.setColorFilter(Color.parseColor(isCircle?"#C4172C":"#58595B"), PorterDuff.Mode.SRC_IN);
        imgHand.setColorFilter(Color.parseColor(isHand?"#C4172C":"#58595B"), PorterDuff.Mode.SRC_IN);
        imgLocation.setColorFilter(Color.parseColor(isLocation?"#C4172C":"#58595B"), PorterDuff.Mode.SRC_IN);
        imgPolygon.setColorFilter(Color.parseColor(isPolygon?"#C4172C":"#58595B"), PorterDuff.Mode.SRC_IN);
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
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;
        try
        {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            activity(), R.raw.style_json));

        }
        catch (Resources.NotFoundException e)
        {

        }
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(this);



        zoomToPostion(new LatLng(6.686543, 78.110464),3);

        if(getArguments().getSerializable("Zone")!=null)
        {
            zone = (Zone) getArguments().getSerializable("Zone");
            updateZoneUI();
        }
    }

    public void drawMap()
    {
        polygon = true;
        map.clear();
        rectOptions = new PolygonOptions();
        rectOptions.addAll(polygonsList);
        rectOptions.strokeColor(Color.parseColor("#58595B"));
        rectOptions.strokeWidth(10);
        rectOptions.fillColor(ContextCompat.getColor(activity(),R.color.map_transparent));
        map.addPolygon(rectOptions);
        for (LatLng latLng : polygonsList)
        {
            addpolygonPointMarker(latLng);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void zoomToPostion(final LatLng location, float value)
    {

        activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CameraPosition.Builder builder = CameraPosition.builder();
                builder.target(location);
                builder.zoom(value);
                CameraPosition cameraPosition = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                map.animateCamera(cameraUpdate);
            }
        });

    }

    private BitmapDescriptor getBitmapDescriptor(int id)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(activity(),id);
        vectorDrawable.setBounds(0, 0, 60, 100);
        Bitmap bm = Bitmap.createBitmap(60, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private BitmapDescriptor getBitmapDescriptor(int id,int right,int bottom)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(activity(),id);
        vectorDrawable.setBounds(0, 0, right, bottom);
        Bitmap bm = Bitmap.createBitmap(right, bottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Utility.generateRequestCodes().get("OPEN_PLACES_SEARCH"))
        {
            if (resultCode == Activity.RESULT_OK)
            {
                cleanMapAndOther();
                Place place = PlaceAutocomplete.getPlace(activity(), data);
                this.place = place;
                zoomToPostion(place.getLatLng(),12);
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng)
    {

        if(isPolygon)
        {
            polygonsList.add(latLng);
            drawMap();
        }
        else if(isCircle)
        {
            if(polygonsList.size()==0)
            {
                polygonsList.add(latLng);
                map.clear();
                addpolygonPointMarker(latLng);
                seekBar.setVisibility(View.VISIBLE);
                llhead1.setVisibility(View.VISIBLE);
                addCircleGeofences(latLng);
            }
        }
        else if(isLocation)
        {
            polygonsList.add(latLng);
            addLocationPointMarker(latLng);
        }
        else if(isHand)
        {

        }
        else
        {

        }
    }

    public void addCircleGeofences(LatLng latLng)
    {
        map.clear();
        addpolygonPointMarker(latLng);
        CircleOptions circleOptions = new CircleOptions()
                .radius(seekBar.getProgress())
                .strokeColor(Color.parseColor("#58595B"))
                .strokeWidth(10)
                .fillColor(ContextCompat.getColor(activity(),R.color.map_transparent))
                .center(latLng);
        map.addCircle(circleOptions);
        zoomToPostion(latLng,14);
        geofence = true;
    }


    public void addpolygonPointMarker(LatLng latLng)
    {
        MarkerOptions point = new MarkerOptions();
        point.icon(getBitmapDescriptor(R.drawable.pointer_point,40,40));
        point.anchor(.5f, .5f);
        point.position(latLng);
        map.addMarker(point);

    }

    public void addLocationPointMarker(LatLng latLng)
    {
        MarkerOptions point = new MarkerOptions();
        point.icon(getBitmapDescriptor(R.drawable.location_old,70,70));
        point.anchor(.5f, .5f);
        point.position(latLng);
        map.addMarker(point);
    }


    @Override
    public void onMapLongClick(LatLng latLng)
    {

    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {

        return false;
    }

    @Override
    public void removeKeyword(int position, String value)
    {
        stringList.remove(position);
        keyWordAdapter.notifyDataSetChanged();
        if(stringList.size()==0)
        {
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void addZone()
    {
        if(progressBar.getVisibility()==View.VISIBLE)
        {
            showSnackBar("Please wait",2);
            return;
        }
        Utility.hideKeyboard(activity());
        if(verifyZone())
        {
            if(checkOtherThings())
            {
                if(Utility.isNetworkAvailable(activity()))
                generateZone();
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
                }
            }
        }
    }

    private boolean verifyZone()
    {
        if(polygonsList!=null && polygonsList.size()>0)
        {
            if(polygonsList.size()>1)
            {
                if(polygonsList.size()==2)
                {
                    showSnackBar("Polygon needs at least 3 points",2);
                    return false;
                }
                else
                {

                    return true;
                }

            }
            else
            {
                if(polygon)
                {
                    showSnackBar("Polygon needs at least 3 points",2);
                    return false;
                }
                else if(geofence)
                {
                    return true;
                }
                return false;
            }
        }
        else
        {
            showSnackBar("Map is empty. Please draw polygon / circle",2);
            return false;
        }

    }

    public boolean checkOtherThings()
    {
        if(stringList.size()>0)
        {
            if((""+etZoneName.getText()).trim().length()>0)
            {
                return true;
            }
            else
            {
                showSnackBar("Please enter zone name",2);
            }
        }
        else
        {
            showSnackBar("Please add at least 1 key word",2);
            return false;
        }
        return false;
    }

    public void generateZone()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_CREATE_GEOFENCE +
                "vehicleGuid=0037A48C-A67A-4A45-94D7-05EAA4C3B4BE" +
                "&geofenceName=" + etZoneName.getText().toString() +
                "&geofenceType=" + ( geofence?"circle" : "polygon" ) +
                "&path=" + generatePathString() +
                "&city=" + etZoneName.getText().toString() +
                "&keywords=" + getKeyWordsString() +
                "&radius=" + (geofence?  seekBar.getProgress() : 0)  +
                "&GeofenceGuid=" + (zone==null ? "00000000-0000-0000-0000-000000000000" : zone.GeofenceGuid) +
                "&DealerId=" +  Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID)
                + Utility.addPortalTag();

        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                Intent returnData = new Intent(activity(),HelperActivity.class);
                returnData.putExtra("data","1");
                activity().setResult(Activity.RESULT_OK,returnData);
                activity().finish();
                return;

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR, 2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);

    }

    public String getKeyWordsString()
    {
        String result = "";
        for (int i =0;i<stringList.size();i++)
        {
            if(stringList.size()-1==i)
            {
                result += stringList.get(i);
            }
            else
            {
                result += stringList.get(i) + ",";
            }
        }
        return result;
    }

    public String generatePathString()
    {
        String result = "";
        for (int i =0;i<polygonsList.size();i++)
        {
            if(polygonsList.size()-1==i)
            {
                result += "(" + polygonsList.get(i).latitude + ", " + polygonsList.get(i).longitude + ")";
            }
            else
            {
                result += "(" + polygonsList.get(i).latitude + ", " + polygonsList.get(i).longitude + "),";
            }
        }
        return result;
    }


    public void updateZoneUI()
    {
        stringList = new ArrayList<>();
        etZoneName.setText(zone.GeofenceName);
        String[] keywords = zone.Keywords.split(",");
        if(keywords.length>0)
        {
            for(int i=0;i<keywords.length;i++)
            stringList.add(keywords[i]);

            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(keyWordAdapter = new KeyWordAdapter(stringList,activity(),iKeyWordAdapter));
        }

        switch (zone.GeofenceType)
        {
            case "polygon":
                isPolygon = true;
                isHand = false;
                isCircle = false;
                polygon = true;
                geofence = false;
                updateUI();
                generatePoints(zone);
                break;
            case "circle":
                isPolygon = false;
                isHand = false;
                isCircle = true;
                polygon = false;
                geofence = false;
                updateUI();
                generatePoints(zone);
                break;
        }

    }

    public void generatePoints(Zone zone)
    {
        try
        {
            String[] pointsLatLng = zone.Path.replace("lat:","")
                    .replace("lng:","")
                    .replace("{","")
                    .replace("}","")
                    .replace("[","")
                    .replace("]","")
                    .replace(" ","").trim().split(",");

            for(int i=0;i<pointsLatLng.length;i++)
            {
                addLocationPointMarker(new LatLng(Double.parseDouble(pointsLatLng[i].trim()), Double.parseDouble(pointsLatLng[i + 1].trim())));
                polygonsList.add(new LatLng(Double.parseDouble(pointsLatLng[i].trim()), Double.parseDouble(pointsLatLng[i + 1].trim())));
                i = i + 1;
            }

            if(polygonsList.size()>0)
            {
                if(polygonsList.size()==1)
                {
                    if(zone.Radius<= 1000)
                    {
                        seekBar.setProgress((float) zone.Radius);
                    }
                    else
                    {
                        seekBar.setMax((float) (zone.Radius * 2));
                        seekBar.setProgress((float) zone.Radius);
                    }
                    llhead1.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    addCircleGeofences(polygonsList.get(0));
                    zoomToPostion(polygonsList.get(0),zone.Radius<=1000?13:9);
                }
                else
                {
                    drawMap();
                    zoomPolygons();

                }



            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void zoomPolygons()
    {
        LatLngBounds.Builder boundsPoints = new LatLngBounds.Builder();
        for(LatLng point :  polygonsList)
        boundsPoints.include(point);

        LatLngBounds bounds = boundsPoints.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels - Utility.dpSize(activity(),250);
        Logger.d("Height " + height);
        int padding = (int) (width * 0.12);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
        map.moveCamera(cu);
        map.animateCamera(cu);
        //zoomToPostion(bounds.getCenter(),12);
    }
}
