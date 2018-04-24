package com.maya.wcustomer.fragments.my_way.trips;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.maya.wcustomer.R;
import com.maya.wcustomer.interfaces.fragments.IFragment;
import com.maya.wcustomer.models.TripPoint;
import com.maya.wcustomer.utilities.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailsFragment extends Fragment implements IFragment, OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GoogleMap googleMap;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.tvRPM)
    TextView tvRPM;

    @BindView(R.id.tvSpeed)
    TextView tvSpeed;

    @BindView(R.id.tvDistance)
    TextView tvDistance;

    List<TripPoint> tripPoints;
    List<LatLng> latLngList;

    Marker carMarker, startMarker, endMarker;


    public TripDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripDetailsFragment newInstance(String param1, String param2) {
        TripDetailsFragment fragment = new TripDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        ButterKnife.bind(this,view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(latLngList!=null && latLngList.size()>0)
                {
                    updateCarMarker();
                    updateSpeedandRPM();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setEnabled(false);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        try
        {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            activity(), R.raw.style_json));

        }
        catch (Resources.NotFoundException e)
        {

        }
        zoomToPostion(new LatLng(6.686543, 78.110464),3);

        fetchTripPoints();

    }

    private void fetchTripPoints()
    {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                tripPoints = Utility.generateSampleTripPoints();
                latLngList = new ArrayList<>();
                if(tripPoints!=null && tripPoints.size()>0)
                {
                    for (TripPoint tripPoint : tripPoints)
                    {
                        latLngList.add(new LatLng(tripPoint.lat, tripPoint.lng));
                    }
                    drawOnMap();
                }

                progressBar.setVisibility(View.GONE);
            }
        },500);
    }

    private void drawOnMap()
    {
        generateStartEndMarker();
        generateCarMarker();
        seekBar.setMax(latLngList.size()-1);
        seekBar.setProgress(0);
        seekBar.setEnabled(true);
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(latLngList);
        lineOptions.width(12);
        lineOptions.color(Color.parseColor("#816CD4"));
        googleMap.addPolyline(lineOptions);
        zoomToPostion(latLngList.get(0),17);
        updateSpeedandRPM();

    }

    private void generateStartEndMarker()
    {
        MarkerOptions markerOptions = new MarkerOptions();
        float logicalDensity = getResources().getDisplayMetrics().density;
        int thicknessPoints = (int)Math.ceil(20 * logicalDensity + .5f);
        BitmapDrawable b = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.green_circle1)  ;
        Bitmap finalIcon = Bitmap.createScaledBitmap(b.getBitmap(), thicknessPoints, thicknessPoints, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(finalIcon));
        markerOptions.anchor(.5f, .5f);
        markerOptions.position(latLngList.get(0));
        startMarker = googleMap.addMarker(markerOptions);

        markerOptions.position(latLngList.get(latLngList.size()-1));
        b = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.red_circle1)  ;
        finalIcon = Bitmap.createScaledBitmap(b.getBitmap(), thicknessPoints, thicknessPoints, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(finalIcon));
        endMarker = googleMap.addMarker(markerOptions);
    }

    public void updateCarMarker()
    {
        carMarker.setPosition(latLngList.get(seekBar.getProgress()));
        if(seekBar.getProgress()<latLngList.size()-1)
        {
            carMarker.setRotation((float) bearingBetweenLocations(latLngList.get(seekBar.getProgress()), latLngList.get(seekBar.getProgress() + 1)));
        }
        else
        {
            carMarker.setRotation((float) bearingBetweenLocations(latLngList.get(seekBar.getProgress()-1), latLngList.get(seekBar.getProgress())));
        }
        zoomToPostion(latLngList.get(seekBar.getProgress()),17);

    }

    private void updateSpeedandRPM()
    {
        tvRPM.setText("RPM : " + tripPoints.get(seekBar.getProgress()).rpm);
        tvSpeed.setText("Speed : " + tripPoints.get(seekBar.getProgress()).speed+ " mph");
        DecimalFormat df = new DecimalFormat("0.00");
        tvDistance.setText(df.format(Utility.generateDistance(latLngList, seekBar.getProgress())/1.6)+ " miles");
    }



    private void generateCarMarker()
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(getBitmapDescriptor(R.drawable.ic_car_suv,60,100));
        markerOptions.anchor(.5f, .5f);
        markerOptions.position(latLngList.get(0));
        markerOptions.rotation((float) bearingBetweenLocations(latLngList.get(0),latLngList.get(1)));
        carMarker = googleMap.addMarker(markerOptions);
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }


    public void zoomToPostion(final LatLng location, float value)
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

    private BitmapDescriptor getBitmapDescriptor(int id,int right,int bottom)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(activity(),id);
        vectorDrawable.setBounds(0, 0, right, bottom);
        Bitmap bm = Bitmap.createBitmap(right, bottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
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
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

}
