package com.maya.vgarages.fragments.garage.profile;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.maya.vgarages.R;
import com.maya.vgarages.adapters.fragments.garage.profile.GarageImagesAdapter;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Utility;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GarageProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GarageProfileFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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

    @BindView(R.id.imgBookmark)
    ImageView imgBookmark;

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
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new GarageImagesAdapter( Utility.generateImageUrls(),activity(),true));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new GarageImagesAdapter( Utility.generateImageUrls(),activity(),false));
            }
        },3000);
        tvGarageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        llCall.setOnClickListener(v -> {Utility.openPhoneDialPad(activity(), Constants.SAMPLE_CUSTOMER_PHONE_NUMBER);});
        llNavigate.setOnClickListener(v -> {Utility.openGoogleNavigate(activity(),myLocation);});
        int value = new Random().nextInt(2);
        imgBookmark.setColorFilter(ContextCompat.getColor(activity(),value%2==0 ? R.color.colorPrimary : R.color.icon_color));

        updateUI();
    }

    private void updateUI()
    {
        if(getArguments()==null || getArguments().getSerializable("Garage")==null)
        {
            return;
        }
        Garage garage = (Garage) getArguments().getSerializable("Garage");
        tvGarageName.setText(garage.Name);
        tvLocation.setText(garage.Location);
        tvOpen.setText(garage.isOpen ? "Open Now" : "Closed Now");
        tvOpen.setTextColor(ContextCompat.getColor(activity(), garage.isOpen ? R.color.colorPrimary : R.color.light_orange));
        tvValue.setText(garage.Value);
        tvPriceRange1.setTextColor(ContextCompat.getColor(
                activity(), garage.PriceRange == 4 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange2.setTextColor(ContextCompat.getColor(
                activity(), garage.PriceRange >= 3 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange3.setTextColor(ContextCompat.getColor(
                activity(), garage.PriceRange >= 2 ? R.color.colorPrimary : R.color.light_new_gray));
        tvPriceRange4.setTextColor(ContextCompat.getColor(
                activity(), garage.PriceRange >= 1 ? R.color.colorPrimary : R.color.light_new_gray));
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
}
