package com.maya.wadmin.fragments.home.options;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.utilities.Utility;

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

    @BindView(R.id.tvFirstName) TextView tvFirstName;
    @BindView(R.id.tvLastName) TextView tvLastName;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserRoll) TextView tvUserRoll;


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
        setUp(view);
        return view;
    }

    public void setUp(View view)
    {
        ButterKnife.bind(this,view);
        tvFirstName.setText(Utility.getCamelCase(Utility.getString(Utility.getSharedPreferences(), Constants.FIRST_NAME)));
        tvLastName.setText(Utility.getCamelCase(Utility.getString(Utility.getSharedPreferences(), Constants.LAST_NAME)));
        tvUserName.setText(Utility.getString(Utility.getSharedPreferences(), Constants.USER_NAME).toLowerCase());
        tvUserRoll.setText(Utility.getString(Utility.getSharedPreferences(), Constants.USER_ROLL_NAME));

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
    public Activity activity()
    {
        return getActivity();
    }
}
