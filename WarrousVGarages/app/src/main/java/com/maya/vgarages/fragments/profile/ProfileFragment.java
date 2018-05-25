package com.maya.vgarages.fragments.profile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.vehicle.UserVehiclesFragment;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.imgUser)
    ImageView imgUser;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.llProfile)
    LinearLayout llProfile;

    @BindView(R.id.tvVehicles)
    TextView tvVehicles;

    @BindView(R.id.tvHelp)
    TextView tvHelp;

    @BindView(R.id.tvAppointments)
    TextView tvAppointments;

    @BindView(R.id.tvLogout)
    TextView tvLogout;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    UserVehiclesFragment userVehiclesFragment;

    boolean isProfile = true;
    boolean isVehicle = true;
    int flag = 0;




    public ProfileFragment() {
        // Required empty public constructor
    }



    public static ProfileFragment newInstance(boolean isProfile,boolean isVehicle,int flag) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean("isProfile", isProfile);
        args.putBoolean("isVehicle", isVehicle);
        args.putInt("flag", flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,view);


        initialize();
        return view;
    }

    private void initialize()
    {

        isProfile = getArguments().getBoolean("isProfile",true);

        isVehicle = getArguments().getBoolean("isVehicle",true);

        flag = getArguments().getInt("flag");


        llProfile.setVisibility(isProfile?View.VISIBLE:View.GONE);
        tvUserName.setText(Utility.getCamelCase(Utility.getString(Utility.getSharedPreferences(), Constants.USER_NAME)));
        Picasso.with(activity())
                .load(Utility.getString(Utility.getSharedPreferences(),Constants.USER_PHOTO_URL))
                .into(imgUser);

        if(Utility.getSharedPreferences().contains(Constants.USER_ADDRESS))
        {
            tvAddress.setText(Utility.getCamelCase(Utility.getString(Utility.getSharedPreferences(),Constants.USER_ADDRESS)));
        }
        else
        {
            tvAddress.setText("");
        }
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,userVehiclesFragment = UserVehiclesFragment.newInstance(isProfile,flag)).commit();


        tvAppointments.setOnClickListener(v -> {goToAppointments();});
        tvVehicles.setOnClickListener(v -> {goToVehicles();});
        tvHelp.setOnClickListener(v -> {goToHelp();});
        tvLogout.setOnClickListener(v -> {((HelperActivity) activity()).openLogoutDialog();});

        fab.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAddVehicle();
        });

        if( isVehicle == false)
        {
            fab.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void goToVehicles()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2228);
        startActivity(intent);
    }

    private void goToHelp()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2229);
        startActivity(intent);
    }

    private void goToAppointments()
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2224);
        startActivity(intent);
    }


    public void refreshVehicles()
    {
        userVehiclesFragment.fetchUserVehicles();
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
}
