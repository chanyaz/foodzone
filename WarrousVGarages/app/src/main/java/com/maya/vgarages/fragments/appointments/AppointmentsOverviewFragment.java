package com.maya.vgarages.fragments.appointments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentsOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsOverviewFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    public AppointmentsOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentsOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsOverviewFragment newInstance(String param1, String param2) {
        AppointmentsOverviewFragment fragment = new AppointmentsOverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_appointments_overview, container, false);
        ButterKnife.bind(this,view);

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
}
