package com.maya.wcustomer.fragments.parking;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.maya.wcustomer.R;
import com.maya.wcustomer.adapters.custom.CustomSingleRecyclerViewAdapter;
import com.maya.wcustomer.interfaces.fragments.IFragment;
import com.maya.wcustomer.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParkingHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParkingHomeFragment extends Fragment implements IFragment , OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    GoogleMap googleMap;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    public ParkingHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParkingHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParkingHomeFragment newInstance(String param1, String param2) {
        ParkingHomeFragment fragment = new ParkingHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_parking_home, container, false);
        ButterKnife.bind(this,view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        setUp();
        return view;
    }

    private void setUp()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new CustomSingleRecyclerViewAdapter(activity(),Utility.generateActionsForParking(),2));
        progressBar.setVisibility(View.GONE);
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
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        try
        {

            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity(), R.raw.style_json));

        }
        catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
        }

        LatLng latLng = new LatLng(17.439091, 78.399097);
        MarkerOptions marker2 = new MarkerOptions();
        marker2.title("Car");
        marker2.icon(getBitmapDescriptor(R.drawable.circle_pointer,200,200));
        marker2.anchor(.5f, .5f);
        marker2.rotation((float) bearingBetweenLocations(latLng,new LatLng(17.439042, 78.399146)));
        marker2.position(latLng);
        googleMap.addMarker(marker2);
        marker2.icon(getBitmapDescriptor(R.drawable.ic_car_suv,60,100));
        marker2.position(latLng);
        googleMap.addMarker(marker2);
        zoomToPostion(latLng,16);
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
