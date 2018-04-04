package com.maya.wadmin.fragments.testdrive.add;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.testdrive.AssignSalesPersonAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.Customer;
import com.maya.wadmin.models.Route;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignRouteFragment extends Fragment implements IFragment,OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    GoogleMap map;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.tvCustomerName) TextView tvCustomerName;
    @BindView(R.id.tvCustomerPhone) TextView tvCustomerPhone;

    @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
    @BindView(R.id.tvRole) TextView tvRole;
    @BindView(R.id.tvVehicleCount) TextView tvVehicleCount;
    @BindView(R.id.tvRouteName) TextView tvRouteName;

    @BindView(R.id.imgSalesPerson) ImageView imgSalesPerson;
    @BindView(R.id.imgVehicle) ImageView imgVehicle;

    Vehicle vehicle;
    SalesPerson salesPerson;
    Customer customer;

    @BindView(R.id.progressBar) ProgressBar progressBar;

    Route route;


    public AssignRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignRouteFragment newInstance(String param1, String param2) {
        AssignRouteFragment fragment = new AssignRouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AssignRouteFragment newInstance(Vehicle vehicle, SalesPerson salesPerson, Customer customer)
    {
        AssignRouteFragment fragment = new AssignRouteFragment();
        Bundle args = new Bundle();
        args.putSerializable("vehicle", vehicle);
        args.putSerializable("salesPerson", salesPerson);
        args.putSerializable("customer",customer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
            salesPerson = (SalesPerson)getArguments().getSerializable("salesPerson");
            customer = (Customer)getArguments().getSerializable("customer");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_route, container, false);
        ButterKnife.bind(this,view);

        ((HelperActivity) activity()).clearSearchText();
        ((HelperActivity) activity()).searchViewItem.setVisible(false);

        mapView.onCreate(savedInstanceState);


        if(customer!=null && salesPerson!=null && vehicle!=null)
        {
            imgVehicle.setImageResource(R.drawable.sample_image1);
            Picasso.with(activity())
                    .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                    .into(imgSalesPerson);

            tvSalesPerson.setText(salesPerson.Name);
            if(salesPerson.Type!=null)
                tvRole.setText(salesPerson.Name);
            else
            {
                tvRole.setText(Constants.SALES_PERSON);
            }

            tvVehicleCount.setText("1 Vehicle Selected");
            tvCustomerName.setText(customer.FullName);
            if(customer.PhoneNumber.trim().length()==10)
                tvCustomerPhone.setText(
                        "(" +   customer.PhoneNumber.substring(0,3) + ") " +
                                customer.PhoneNumber.substring(3,6) + "-" +
                                customer.PhoneNumber.substring(6,10)
                );
            else
            {
                tvCustomerPhone.setText("(415) 555-2671");
            }
        }
        progressBar.setVisibility(View.GONE);

        mapView.getMapAsync(this);

        return view;
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try
        {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            activity(), R.raw.style_json));

        }
        catch (Resources.NotFoundException e)
        {

        }

        //zoomToPostion(new LatLng(17.43751, 78.3939));

        if(Utility.isNetworkAvailable(activity()))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchRouteBasedOnVehicleId();
                }
            },300);

        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

    }

    public void zoomToPostion(final LatLng location)
    {

        activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CameraPosition.Builder builder = CameraPosition.builder();
                builder.target(location);
                builder.zoom((float) 15.5);
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



    public void fetchRouteBasedOnVehicleId()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_ROUTES_BASED_ON_VEHICLE_ID + "?vehicleid=" + vehicle.VehicleId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<Route>() {
                }.getType();
                route = gson.fromJson(response, type);
                if(route!=null)
                {
                    try
                    {
//                        String[] llCenterValues = route.Center.split(",");
//                        zoomToPostion(new LatLng(Double.parseDouble(llCenterValues[0]),Double.parseDouble(llCenterValues[1])));
                        tvRouteName.setText(route.Name);
                        addPolyline();

                    }
                    catch (Exception e)
                    {

                    }
                }

                progressBar.setVisibility(View.GONE);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    public void addPolyline()
    {
        if(route!=null)
        {
           route.Path = route.Path.replaceAll("lat","\"latitude\"");
           route.Path = route.Path.replaceAll("lng","\"longitude\"");
           Logger.d(route.Path);

            Gson gson = new Gson();
            Type type = new TypeToken<List<LatLng>>() {
            }.getType();
            List<LatLng> latLngs = gson.fromJson(route.Path, type);

            if(latLngs.size()>0)
            {

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(latLngs);
                polylineOptions.width(10);
                //polylineOptions.color(ContextCompat.getColor(activity(),R.color.mainColorPrimary));
                polylineOptions.color(Color.parseColor("#EB5757"));
                map.addPolyline(polylineOptions);
                MarkerOptions marker2 = new MarkerOptions();
                marker2.title("Current");
                marker2.icon(getBitmapDescriptor(R.drawable.ic_car_suv));
                marker2.anchor(.5f, .5f);
                marker2.position(latLngs.get(0));
                marker2.rotation((float) bearingBetweenLocations(latLngs.get(0),latLngs.get(1)));
                map.addMarker(marker2);
                zoomToPostion(latLngs.get(0));
            }
            else
            {
                Logger.d("NO SIZE");
            }

        }
    }

}
