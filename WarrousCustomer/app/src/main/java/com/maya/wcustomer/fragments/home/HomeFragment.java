package com.maya.wcustomer.fragments.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.maya.wcustomer.R;
import com.maya.wcustomer.activities.HelperActivity;
import com.maya.wcustomer.adapters.fragments.home.HomeOptionsAdapter;
import com.maya.wcustomer.adapters.fragments.home.RecentViolationsAdapter;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.interfaces.adapters.home.IHomeOptionsAdapter;
import com.maya.wcustomer.interfaces.fragments.IFragment;
import com.maya.wcustomer.models.HomeOption;
import com.maya.wcustomer.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IFragment, IHomeOptionsAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.recyclerViewViolations)
    RecyclerView recyclerViewViolations;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.rlHead)
    RelativeLayout rlHead;

    @BindView(R.id.imgOverview)
    ImageView imgOverview;

    @BindView(R.id.llHead)
    LinearLayout llHead;

    IHomeOptionsAdapter iHomeOptionsAdapter;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        setUp();

        return view;
    }

    private void setUp()
    {
        iHomeOptionsAdapter = this;

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewViolations.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewViolations.setAdapter(new RecentViolationsAdapter(Utility.generateRecentViolations(),activity()));


        ViewTreeObserver vto = rlHead.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rlHead.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    rlHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int height = rlHead.getMeasuredHeight();
                recyclerView.setAdapter(new HomeOptionsAdapter(Utility.generateHomeOptions(),activity(),height,iHomeOptionsAdapter));
            }
        });

        imgOverview.setOnClickListener(v -> goToVehicleDetails());
        llHead.setOnClickListener(v -> goToVehicleDetails());

    }

    private void goToVehicleDetails()
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,1);
        startActivity(intent);
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
    public void onItemClick(HomeOption homeOption, int position)
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        switch (position) // + 2
        {
            case 0:
                intent.putExtra(Constants.FRAGMENT_KEY,position+2);
                break;

            case 1:
                intent.putExtra(Constants.FRAGMENT_KEY,position+2);
                break;

            case 2:
                intent.putExtra(Constants.FRAGMENT_KEY,position+2);
                break;
        }

        startActivity(intent);
    }
}
