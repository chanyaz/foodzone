package com.maya.vgarages.fragments.start;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GetLocationFragment getLocationFragment;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    public int value  = 0;


    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
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
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this,view);
        //getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,StartFragment.newInstance(null,null)).addToBackStack(null).commit();
        setUpChildFragment(value = generateFragmentKey());
        return view;
    }


    public void setUpChildFragment(int value)
    {
        Fragment fragment = null;
        switch (value)
        {
            case 101:
                fragment = RequestPermissionFragment.newInstance(null,null);
                break;
            case 102:
                getLocationFragment = GetLocationFragment.newInstance(null,null);
                fragment = getLocationFragment;
                break;
            case 103:
                //fragment = StartFragment.newInstance(null,null);
                fragment = LoginFragment.newInstance(null,null);
                break;
        }
        if(fragment!=null)
        {
            if(value == 101 || value == 102)
            {
                if(getChildFragmentManager().getBackStackEntryCount()>1)
                {
                    getChildFragmentManager().popBackStack();
                }
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
            else
            {
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack(null).commit();
            }
        }

    }

    public int generateFragmentKey()
    {

        if(Utility.getSharedPreferences().contains(Constants.LOGIN))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(activity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return 101;
                }
                return 102;
            } else {
                return 102;
            }
        }
        else
        {
            return 103;
        }
    }

    public void openGetLocationFragment()
    {
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,getLocationFragment = GetLocationFragment.newInstance(null,null)).commit();
    }

    public void updateUIinLocationFragment()
    {
        getLocationFragment.updateUI();
    }

    public void goToSignIn()
    {
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout,LoginFragment.newInstance(null,null)).addToBackStack(null).commit();
    }

    public void goToSignUp()
    {
        getChildFragmentManager().popBackStack();
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
