package com.maya.wadmin.fragments.vehicle.location;


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
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehicleLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleLocationFragment extends Fragment implements IFragment ,OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GoogleMap googleMap;
    MapView mapView;
    Vehicle vehicle = null;
    TextView tvModel, tvVin, tvMake, tvYear;
    CoordinatorLayout coordinatorLayout;

    public VehicleLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehicleLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleLocationFragment newInstance(String param1, String param2) {
        VehicleLocationFragment fragment = new VehicleLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static VehicleLocationFragment newInstance(Vehicle vehicle) {
        VehicleLocationFragment fragment = new VehicleLocationFragment();
        Bundle args = new Bundle();
        args.putSerializable("Vehicle", vehicle);
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
        View view = inflater.inflate(R.layout.fragment_vehicle_location, container, false);
        vehicleIdsSetUp(view);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        if(getArguments()!=null)
        {
            if(getArguments().getSerializable("Vehicle")!=null)
            {
                vehicle = (Vehicle) getArguments().getSerializable("Vehicle");
            }
            else
            {
                showSnackBar("No data found",2);
            }
        }



        return view;
    }

    private void vehicleIdsSetUp(View view)
    {
        tvModel = view.findViewById(R.id.tvModel);
        tvVin = view.findViewById(R.id.tvVin);
        tvMake = view.findViewById(R.id.tvMake);
        tvYear = view.findViewById(R.id.tvYear);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
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

        if(vehicle!=null)
        {
            tvMake.setText(vehicle.Make);
            tvModel.setText(vehicle.Model);
            tvVin.setText(vehicle.Vin);
            tvYear.setText(vehicle.Year);

            MarkerOptions truck = new MarkerOptions();
            truck.title("Current");
            truck.icon(getBitmapDescriptor(R.drawable.ic_direction,200,200));
            truck.anchor(.5f, .5f);
            truck.position(new LatLng(Double.parseDouble(vehicle.Latitude),Double.parseDouble(vehicle.Longitude)));
            googleMap.addMarker(truck);
            zoomToPostion(new LatLng(Double.parseDouble(vehicle.Latitude),Double.parseDouble(vehicle.Longitude)),(float) 12.5);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
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


    private BitmapDescriptor getBitmapDescriptor(int id, int right, int bottom)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(activity(),id);
        vectorDrawable.setBounds(0, 0, right, bottom);
        Bitmap bm = Bitmap.createBitmap(right, bottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

}
