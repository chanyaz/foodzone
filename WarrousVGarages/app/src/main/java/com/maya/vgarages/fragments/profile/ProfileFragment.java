package com.maya.vgarages.fragments.profile;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String mParam1;
    private String mParam2;


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

    UserVehiclesFragment userVehiclesFragment;

    boolean isProfile = true;




    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(boolean isProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean("isProfile", isProfile);
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,view);


        initialize();
        return view;
    }

    private void initialize()
    {
        if(!getArguments().getBoolean("isProfile",true))
        {
            isProfile = false;
        }
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
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,userVehiclesFragment = UserVehiclesFragment.newInstance(isProfile)).commit();

        fab.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAddVehicle();
        });
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
