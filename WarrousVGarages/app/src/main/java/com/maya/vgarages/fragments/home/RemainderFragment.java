package com.maya.vgarages.fragments.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.fragments.home.RemainderAdapter;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.home.IRemainderAdapter;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemainderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemainderFragment extends Fragment implements IFragment, IRemainderAdapter{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    IRemainderAdapter iRemainderAdapter;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public RemainderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemainderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemainderFragment newInstance(String param1, String param2) {
        RemainderFragment fragment = new RemainderFragment();
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
        View view = inflater.inflate(R.layout.fragment_remainder, container, false);
        iRemainderAdapter = this;
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerView.setAdapter(new RemainderAdapter(Utility.generateRemainderGaragesList(),activity(),iRemainderAdapter));
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
    public void itemClick(Garage garage, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2222);
        intent.putExtra("Garage",garage);
        startActivity(intent);
    }
}
