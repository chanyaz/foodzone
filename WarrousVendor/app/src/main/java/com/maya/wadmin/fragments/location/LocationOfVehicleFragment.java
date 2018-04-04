package com.maya.wadmin.fragments.location;


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationOfVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationOfVehicleFragment extends Fragment implements OnMapReadyCallback, IFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.etVin)
    EditText etVin;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @BindView(R.id.mapView)
    MapView mapView;

    GoogleMap googleMap;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Vehicle vehicle = null;

    @BindView(R.id.tvModel) TextView tvModel;
    @BindView(R.id.tvVin) TextView tvVin;
    @BindView(R.id.tvMake) TextView tvMake;
    @BindView(R.id.tvYear) TextView tvYear;

    @BindView(R.id.llhead)
    LinearLayout llhead;



    public LocationOfVehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationOfVehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationOfVehicleFragment newInstance(String param1, String param2) {
        LocationOfVehicleFragment fragment = new LocationOfVehicleFragment();
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
        View view = inflater.inflate(R.layout.fragment_location_of_vehicle, container, false);
        ButterKnife.bind(this,view);

        llhead.setVisibility(View.GONE);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        progressBar.setVisibility(View.GONE);

        etVin.setText("W854986KKOW04A946");
        imgSearch.setOnClickListener(v ->
        {
            Utility.hideKeyboard(activity());
            if((""+etVin.getText()).trim().length()>0)
            {
                searchVin();
            }
            else
            {
              showSnackBar("Please enter vin",2);
            }
        });



        return view;
    }

    private void searchVin()
    {
        llhead.setVisibility(View.GONE);
        if(googleMap != null)
        {
            googleMap.clear();
        }
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_VEHICLE_DETAILS_BY_VIN + etVin.getText().toString().trim() + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                List<Vehicle> vehicleList = gson.fromJson(response, type);
                if(vehicleList!=null && vehicleList.size()>0)
                {
                    try {

                        vehicle = vehicleList.get(0);
                        tvMake.setText(vehicle.Make);
                        tvModel.setText(vehicle.Model);
                        tvVin.setText(vehicle.Vin);
                        tvYear.setText(vehicle.Year);
                        llhead.setVisibility(View.VISIBLE);
                        MarkerOptions truck = new MarkerOptions();
                        truck.title("Current");
                        truck.icon(getBitmapDescriptor(R.drawable.ic_direction, 200, 200));
                        truck.anchor(.5f, .5f);
                        truck.position(new LatLng(Double.parseDouble(vehicleList.get(0).CurrentLatitude), Double.parseDouble(vehicleList.get(0).CurrentLongitude)));
                        googleMap.addMarker(truck);
                        zoomToPostion(new LatLng(Double.parseDouble(vehicleList.get(0).CurrentLatitude), Double.parseDouble(vehicleList.get(0).CurrentLongitude)), (float) 12.5);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    showSnackBar("No vehicle found by Vin",2);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

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
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy()
    {
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
}
