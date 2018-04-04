package com.maya.wadmin.fragments.map;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.DeliveryTruck;
import com.maya.wadmin.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomMapFragment extends Fragment implements IFragment, OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.mapView)
    MapView mapView;

    GoogleMap googleMap;
    int type;
    DeliveryTruck deliveryTruck;

    @BindView(R.id.llhead)
    LinearLayout llhead;

    @BindView(R.id.tvVehicleCount)
    TextView tvVehicleCount;

    @BindView(R.id.tvTruckDelivery)
    TextView tvTruckDelivery;

    public CustomMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomMapFragment newInstance(String param1, String param2) {
        CustomMapFragment fragment = new CustomMapFragment();
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
        View view = inflater.inflate(R.layout.fragment_custom_map, container, false);
        ButterKnife.bind(this,view);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        llhead.setVisibility(View.GONE);

        return view;
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
    public void onMapReady(GoogleMap googleMap) {
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
        if(getArguments()!=null)
        doAction();
    }

    private void doAction()
    {
        type = getArguments().getInt(Constants.TYPE,0);
        switch (type)
        {
            case 0:
                this.deliveryTruck = (DeliveryTruck) getArguments().getSerializable("deliveryTruck");
                MarkerOptions truck = new MarkerOptions();
                truck.title("Current");
                truck.icon(getBitmapDescriptor(R.drawable.ic_direction,200,200));
                truck.anchor(.5f, .5f);
                truck.position(new LatLng(Double.parseDouble(deliveryTruck.Latitude),Double.parseDouble(deliveryTruck.Longitude)));
                googleMap.addMarker(truck);
                zoomToPostion(new LatLng(Double.parseDouble(deliveryTruck.Latitude),Double.parseDouble(deliveryTruck.Longitude)),(float) 12.5);
                llhead.setVisibility(View.VISIBLE);
                tvTruckDelivery.setText(Utility.makeJSDateReadable1(deliveryTruck.EstimatedDelivery));
                if(deliveryTruck.LstVehicles.size()>1)
                {
                    tvVehicleCount.setText(deliveryTruck.LstVehicles.size() + " Vehicles");
                }
                else
                {
                    tvVehicleCount.setText(deliveryTruck.LstVehicles.size()+ " Vehicle");
                }
                break;

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
}
