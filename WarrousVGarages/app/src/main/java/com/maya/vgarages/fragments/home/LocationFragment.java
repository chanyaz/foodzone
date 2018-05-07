package com.maya.vgarages.fragments.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.home.GaragesAdapter;
import com.maya.vgarages.adapters.fragments.home.ServiceAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.home.IGaragesAdapter;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements IFragment, OnMapReadyCallback, IGaragesAdapter,GoogleMap.OnMarkerClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int previous = -1;

    @BindView(R.id.llGarage)
    LinearLayout llGarage;

    IGaragesAdapter iGaragesAdapter;
    Polyline curvedPolyline;

    Marker markerYou;
    List<Marker> markerList;
    List<Garage> garageList;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.mapView)
    MapView mapView;

    GoogleMap googleMap;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tvGarageName)
    TextView tvGarageName;

    @BindView(R.id.tvValue)
    TextView tvValue;

    @BindView(R.id.tvDistance)
    TextView tvDistance;

    @BindView(R.id.tvLocation)
    TextView tvLocation;

    @BindView(R.id.tvShopType)
    TextView tvShopType;

    @BindView(R.id.tvOpen)
    TextView tvOpen;

    @BindView(R.id.tvPriceRange)
    TextView tvPriceRange;

    @BindView(R.id.tvPriceRange1)
    TextView tvPriceRange1;

    @BindView(R.id.tvPriceRange2)
    TextView tvPriceRange2;

    @BindView(R.id.tvPriceRange3)
    TextView tvPriceRange3;

    @BindView(R.id.tvPriceRange4)
    TextView tvPriceRange4;

    @BindView(R.id.rlLocation)
    RelativeLayout rlLocation;

    List<Garage> listRecommended;

    LatLng myLocation = new LatLng(17.439091, 78.399097);

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static LocationFragment newInstance(LatLng latLng) {
        LocationFragment fragment = new LocationFragment();
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
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        iGaragesAdapter = this;
        ButterKnife.bind(this,view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initialize();
        return view;
    }

    private void initialize()
    {
        if(getArguments().getParcelable("Location")!=null)
        {
            myLocation = getArguments().getParcelable("Location");
        }

        llGarage.setVisibility(View.GONE);
        llGarage.setOnClickListener(v -> {
            Intent intent = new Intent(activity(), HelperActivity.class);
            intent.putExtra(Constants.FRAGMENT_KEY,2222);
            intent.putExtra("Garage",garageList.get(previous));
            startActivity(intent);
        });
        markerList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        if(Utility.isNetworkAvailable(activity()))
        {
            generateRecommendedGarages();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        rlLocation.setOnClickListener(v ->
        {
            zoomToPosition(myLocation,13);
        });
        googleMap.setOnMarkerClickListener(this);

        try
        {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            activity(), R.raw.style_json));

        }
        catch (Resources.NotFoundException e)
        {

        }


        MarkerOptions marker2 = new MarkerOptions();
        marker2.title("you");
        marker2.icon(getBitmapDescriptor(R.drawable.user_marker,75,75));
        marker2.anchor(.5f, .5f);
        marker2.position(myLocation);
        //marker2.rotation((float) bearingBetweenLocations(latLng,new LatLng(17.439042, 78.399146)));
        markerYou = googleMap.addMarker(marker2);
        markerYou.setTag(143914);
        zoomToPosition(myLocation,11);

//        garageList = Utility.generateGaragesList();
//
//        for(int i=0;i<garageList.size();i++)
//        {
//            double offset =  (i+1) / 30d;
//            LatLng latLng = new LatLng(myLocation.latitude + offset, myLocation.longitude + offset);
//            generateGarageMarkerView(Utility.generateGaragesList().get(i),false,latLng,i);
//        }

        if(Utility.isNetworkAvailable(activity()))
        fetchGarages();
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }


    }


    private void fetchGarages()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_GARAGES_LIST_BY_TYPE + "?latitude="+myLocation.latitude+"&longitude="+myLocation.longitude+"&GarageType=ALL&pagecount=1" ;
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
                if(garageList!=null && garageList.size()>0)
                {
                    for(int i=0;i<garageList.size();i++)
                    {
                        LatLng latLng = new LatLng( garageList.get(i).Latitude, garageList.get(i).Longitude);
                        generateGarageMarkerView(garageList.get(i),false,latLng,i);
                    }
                }

                progressBar.setVisibility(View.GONE);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);


    }



    public void zoomToPosition(final LatLng location, float value)
    {

        activity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                CameraPosition.Builder builder = CameraPosition.builder();
                builder.target(location);
                builder.zoom(value);
                CameraPosition cameraPosition = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
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

    private BitmapDescriptor getBitmapDescriptor(int id, int right, int bottom)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(activity(),id);
        vectorDrawable.setBounds(0, 0, right, bottom);
        Bitmap bm = Bitmap.createBitmap(right, bottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }


    public void generateGarageMarkerView(Garage garage,boolean focus,LatLng latLng,int tag)
    {
        try {
            View view = ((LayoutInflater) activity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.garage_marker_layout, null);
            ImageView imgBack = view.findViewById(R.id.imgBack);
            ImageView imgGarage = view.findViewById(R.id.imgGarage);
            imgBack.setColorFilter(ContextCompat.getColor(activity(), focus ? R.color.colorPrimary : R.color.light_gray));

            Picasso.with(activity())
                    .load(garage.ImageUrl)
                    .into(imgGarage, new Callback() {
                        @Override
                        public void onSuccess() {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            activity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
                            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
                            view.buildDrawingCache();
                            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                            Canvas canvas = new Canvas(bitmap);
                            view.draw(canvas);

                            MarkerOptions garageMarker = new MarkerOptions();

                            garageMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                            garageMarker.anchor(.5f, .5f);
                            garageMarker.position(latLng);
                            Marker marker = googleMap.addMarker(garageMarker);
                            marker.setTag(tag);
                            //marker2.rotation((float) bearingBetweenLocations(latLng,new LatLng(17.439042, 78.399146)));
                            markerList.add(marker);
                        }

                        @Override
                        public void onError() {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            activity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
                            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
                            view.buildDrawingCache();
                            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                            Canvas canvas = new Canvas(bitmap);
                            view.draw(canvas);

                            MarkerOptions garageMarker = new MarkerOptions();

                            garageMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                            garageMarker.anchor(.5f, .5f);
                            garageMarker.position(latLng);
                            Marker marker = googleMap.addMarker(garageMarker);
                            marker.setTag(tag);
                            //marker2.rotation((float) bearingBetweenLocations(latLng,new LatLng(17.439042, 78.399146)));
                            markerList.add(marker);
                        }
                    });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void updateViewOfMarker(Garage garage,boolean focus,Marker marker)
    {
        View view = ((LayoutInflater) activity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.garage_marker_layout, null);
        ImageView imgBack = view.findViewById(R.id.imgBack);
        ImageView imgGarage = view.findViewById(R.id.imgGarage);
        imgBack.setColorFilter(ContextCompat.getColor(activity(),focus ? R.color.colorPrimary : R.color.light_gray));

        Picasso.with(activity())
                .load(garage.ImageUrl)
                .into(imgGarage, new Callback() {
                    @Override
                    public void onSuccess()
                    {


                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        activity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
                        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
                        view.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);

                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2)
    {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;
        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
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

    @Override
    public void itemClick(Garage garage, int position) {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2222);
        intent.putExtra("Garage",garage);
        startActivity(intent);
    }


    private void showCurvedPolyline(LatLng p1, LatLng p2, double k) {
        //Calculate distance and heading between two points
        if(curvedPolyline!=null)
        {
            curvedPolyline.remove();
        }
        double d = SphericalUtil.computeDistanceBetween(p1,p2);
        double h = SphericalUtil.computeHeading(p1, p2);

        //Midpoint position
        LatLng p = SphericalUtil.computeOffset(p1, d*0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1-k*k)*d*0.5/(2*k);
        double r = (1+k*k)*d*0.5/(2*k);

        LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);

        //Polyline options
        PolylineOptions options = new PolylineOptions();
        options.width(3);
        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(c, p1);
        double h2 = SphericalUtil.computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numpoints = 100;
        double step = (h2 -h1) / numpoints;

        for (int i=0; i < numpoints; i++) {
            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
            options.add(pi);
        }

        //Draw polyline
        curvedPolyline = googleMap.addPolyline(options.width(10).color(ContextCompat.getColor(activity(),R.color.colorPrimary)).geodesic(false).pattern(pattern));
        zoomPolyline(p1,p2);
    }



    @Override
    public boolean onMarkerClick(Marker marker)
    {
        if(marker.getTag()!=null)
        {

            try
            {

                Integer value = (Integer) marker.getTag();
                if(value==143914)
                {
                    return false;
                }

                if(previous != -1)
                {
                    updateViewOfMarker(garageList.get(previous),false,markerList.get(previous));
                }
                updateViewOfMarker(garageList.get(value),true,marker);
                previous = value;

                showCurvedPolyline(markerYou.getPosition(),marker.getPosition(),0.2);
                updateUI(garageList.get(value));
                return true;
            }
            catch (Exception e)
            {

            }


            return true;
        }

        return false;
    }



    private void zoomPolyline(LatLng l1, LatLng l2)
    {
        LatLngBounds.Builder boundsPoints = new LatLngBounds.Builder();
            boundsPoints.include(l1);
            boundsPoints.include(l2);

        LatLngBounds bounds = boundsPoints.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels - Utility.dpSize(activity(),300);
        Logger.d("Height " + height);
        int padding = (int) (width * 0.15);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
        googleMap.moveCamera(cu);
        googleMap.animateCamera(cu);
    }


    public void updateUI(Garage garage)
    {
        recyclerView.setVisibility(View.GONE);
        llGarage.setVisibility(View.VISIBLE);
        tvGarageName.setText(Utility.getCamelCase(garage.DealerName));
        tvLocation.setText(garage.Address1);
        tvShopType.setText(garage.Types);
        tvDistance.setText(garage.Distance + " km from you");
        tvOpen.setText(!garage.IsClosed ? "Open Now" : "Closed Now");
        tvOpen.setTextColor(ContextCompat.getColor(activity(), !garage.IsClosed ? R.color.colorPrimary : R.color.light_orange));
        tvValue.setText(""+garage.CustomerRating);

        tvPriceRange1.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating == 5 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange1.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 4 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange2.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 3 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange3.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 2 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange4.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 1 ? R.color.colorPrimary : R.color.light_new_gray));

    }


    private void generateRecommendedGarages()
    {
        recyclerView.setAdapter(new GaragesAdapter(Utility.generateGaragesList(),activity(),1,iGaragesAdapter,true));
        String URL = Constants.URL_RECOMMENDED_GARAGES + "?GarageType=all" +
                "&pageCount=1&latitude="+myLocation.latitude+"&longitude="+myLocation.longitude;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Garage>>() {
                }.getType();
                listRecommended = gson.fromJson(response, type);
                if(listRecommended!=null && listRecommended.size()>0)
                {
                    recyclerView.setAdapter(new GaragesAdapter(listRecommended,activity(),1,iGaragesAdapter,false));
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



}
