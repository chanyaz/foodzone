package com.maya.vgarages.fragments.start;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.activities.SplashActivity;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetLocationFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.tvEnable)
    TextView tvEnable;

    @BindView(R.id.imgLocation)
    ImageView imgLocation;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;


    public GetLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetLocationFragment newInstance(String param1, String param2) {
        GetLocationFragment fragment = new GetLocationFragment();
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
        View view = inflater.inflate(R.layout.fragment_get_location, container, false);
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        updateUI();
        tvEnable.setOnClickListener(v ->
        {
            ((SplashActivity)activity()).createLocationRequest();
        });

        ((SplashActivity)activity()).createLocationRequest();

        if(!Utility.isGPSEnable(activity()))
        {
            tvTitle.setText("Please Allow Location");
            imgLocation.setImageResource(R.drawable.ic_my_location);
            tvEnable.setVisibility(View.GONE);
        }
    }

    public void updateUI()
    {
        if(!Utility.isGPSEnable(activity()))
        {
            progressBar.setVisibility(View.GONE);
            tvEnable.setVisibility(View.VISIBLE);
            imgLocation.setImageResource(R.drawable.ic_location_off);
            tvTitle.setText("GPS turned off");
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            tvEnable.setVisibility(View.GONE);
            imgLocation.setImageResource(R.drawable.ic_location);
            tvTitle.setText("Getting Location");
        }
    }


    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity activity()
    {
        return getActivity();
    }
}
