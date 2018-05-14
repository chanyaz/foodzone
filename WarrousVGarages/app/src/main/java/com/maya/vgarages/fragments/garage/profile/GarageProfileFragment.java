package com.maya.vgarages.fragments.garage.profile;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.garage.profile.GarageImagesAdapter;
import com.maya.vgarages.adapters.fragments.garage.reviews.ReviewAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.garage.services.GarageServicesFragment;
import com.maya.vgarages.fragments.profile.ProfileFragment;
import com.maya.vgarages.interfaces.dialog.IAppointmentDetailsDialog;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageMedia;
import com.maya.vgarages.models.Review;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GarageProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GarageProfileFragment extends Fragment implements IFragment, IAppointmentDetailsDialog{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tvGarageContent)
    TextView tvGarageContent;

    @BindView(R.id.tvGarageName)
    TextView tvGarageName;

    @BindView(R.id.tvValue)
    TextView tvValue;

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

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.llNavigate)
    LinearLayout llNavigate;

    @BindView(R.id.llCall)
    LinearLayout llCall;

    @BindView(R.id.llBookmark)
    LinearLayout llBookmark;

    @BindView(R.id.imgBookmark)
    ImageView imgBookmark;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tvBook)
    TextView tvBook;

    Garage garage;
    IAppointmentDetailsDialog iAppointmentDetailsDialog;
    boolean isBookmark = true;

    LatLng myLocation = new LatLng(17.439091, 78.399097);
    public GarageProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GarageProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GarageProfileFragment newInstance(String param1, String param2) {
        GarageProfileFragment fragment = new GarageProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static GarageProfileFragment newInstance(Garage garage)
    {
        GarageProfileFragment fragment = new GarageProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("Garage", garage);
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
        View view = inflater.inflate(R.layout.fragment_garage_profile, container, false);
        iAppointmentDetailsDialog = this;
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new GarageImagesAdapter( Utility.generateImageUrls(),activity(),true));

        tvGarageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        llCall.setOnClickListener(v -> {Utility.openPhoneDialPad(activity(), garage!=null ? garage.Phone1!=null ? garage.Phone1 : Constants.SAMPLE_CUSTOMER_PHONE_NUMBER : Constants.SAMPLE_CUSTOMER_PHONE_NUMBER);});

        llNavigate.setOnClickListener(v -> {Utility.openGoogleNavigate(activity() ,  garage!=null ? garage.Latitude>0 ? new LatLng(garage.Latitude,garage.Longitude) : myLocation  : myLocation);});

        llBookmark.setOnClickListener(v ->
        {
            isBookmark = isBookmark? false: true;
            applyBookmark(isBookmark);

        });

        updateUI();

        if(garage!=null)
        {
            if (Utility.isNetworkAvailable(activity()))
            {
                fetchImagesOfGarage();
                getIsBookmark();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET, 2);
            }
        }

        tvBook.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAppointmentDialog(null,iAppointmentDetailsDialog);
        });
    }


    private void updateUI()
    {
        if(getArguments()==null || getArguments().getSerializable("Garage")==null)
        {
            return;
        }
        garage = (Garage) getArguments().getSerializable("Garage");
        if(garage.Types!=null)
        tvShopType.setText(garage.Types);
        if(garage.DealerDesc!=null)
        tvGarageContent.setText(garage.DealerDesc);
        tvGarageName.setText(Utility.getCamelCase(garage.DealerName));
        tvLocation.setText(garage.Address1 + " " + garage.Address2);
        tvOpen.setText(!garage.IsClosed ? "Open Now" : "Closed Now");
        tvOpen.setTextColor(ContextCompat.getColor(activity(), !garage.IsClosed ? R.color.colorPrimary : R.color.light_orange));
        tvValue.setText(""+garage.CustomerRating);

        tvPriceRange.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating == 5 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange1.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >=4 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange2.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 3 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange3.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 2 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange4.setTextColor(ContextCompat.getColor(
                activity(), garage.DealerRating >= 1 ? R.color.colorPrimary : R.color.light_new_gray));

        try
        {
            tvTime.setText(garage.StartHr.substring(0, 5) + "-" + garage.EndHr.substring(0, 5));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void fetchImagesOfGarage()
    {
        recyclerView.setAdapter(new GarageImagesAdapter( Utility.generateImageUrls(),activity(),true));
        String URL = Constants.URL_GET_GARAGE_IMAGES + "?dealerId="+garage.DealerId+"&pageCount=1";
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<GarageMedia>>() {
                }.getType();
                List<GarageMedia> list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    List<String> garageImages = new ArrayList<>();

                    for(GarageMedia garageMedia : list)
                        garageImages.add(garageMedia.MediaUrl);

                    recyclerView.setAdapter(new GarageImagesAdapter( Utility.generateImageUrls(),activity(),false));
                }
                else
                {
                    recyclerView.setAdapter(new GarageImagesAdapter( Arrays.asList(garage.ImageUrl),activity(),false));
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


    public void applyBookmark(boolean isBookmark)
    {
        imgBookmark.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String URL = Constants.URL_ADD_REMOVE_BOOKMARK + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&dealerId=" + garage.DealerId + "&isBookmark=" +  isBookmark;// (isBookmark ?  "1" : "0");
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                imgBookmark.setVisibility(View.VISIBLE);
                imgBookmark.setImageResource(isBookmark? R.drawable.pin_fill : R.drawable.pin);
                imgBookmark.setColorFilter(ContextCompat.getColor(activity(),isBookmark ? R.color.colorPrimary : R.color.icon_color));

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
                imgBookmark.setVisibility(View.VISIBLE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }


    public void getIsBookmark()
    {
        imgBookmark.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String URL = Constants.URL_GET_IS_BOOKMARK + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&dealerId=" + garage.DealerId ;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                isBookmark = Boolean.parseBoolean(response.trim());

                progressBar.setVisibility(View.GONE);
                imgBookmark.setVisibility(View.VISIBLE);
                imgBookmark.setImageResource(isBookmark? R.drawable.pin_fill : R.drawable.pin);
                imgBookmark.setColorFilter(ContextCompat.getColor(activity(),isBookmark ? R.color.colorPrimary : R.color.icon_color));

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
                imgBookmark.setVisibility(View.VISIBLE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
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
    public void addAppointment(Appointment appointment) {

    }
}
