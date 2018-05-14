package com.maya.vgarages.fragments.start;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.SplashActivity;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment  implements IFragment, GoogleApiClient.OnConnectionFailedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.llGoogleSignIn)
    LinearLayout llGoogleSignIn;

    @BindView(R.id.fbSignIn)
    LoginButton fbSignIn;

    @BindView(R.id.tvSignIn)
    TextView tvSignIn;

    @BindView(R.id.tvSignUp)
    TextView tvSignUp;

    @BindView(R.id.tvPrivacyPolicy)
    TextView tvPrivacyPolicy;

    @BindView(R.id.tvTerms)
    TextView tvTerms;

    boolean isGoogle = false;




    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.bind(this,view);



        initialize();

        llGoogleSignIn.setOnClickListener(v -> {
            if(!isGoogle)
            {
                showSnackBar("Google login disabled", 2);
                return;
            }
            ((SplashActivity) activity()).signInByGoogle();
        });

        tvSignIn.setOnClickListener(v -> {goToSignInFragment();});

        return view;
    }

    private void initialize()
    {
        ((SplashActivity) activity()).setUpGoogle();
        ((SplashActivity)activity()).setUpFB();
        fbSignIn.setText("Continue with Facebook");
        fbSignIn.setCompoundDrawablePadding(Utility.dpSize(activity(),10));
        fbSignIn.setTypeface(Typeface.createFromAsset(activity().getAssets(), "fonts/RalewayMedium.ttf"));
        fbSignIn.setReadPermissions(Constants.FB_PERMISSIONS);
        fbSignIn.registerCallback(((SplashActivity)activity()).callbackManager, ((SplashActivity)activity()).callback);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }



    private void goToSignInFragment()
    {
        ((SplashFragment) getParentFragment()).goToSignIn();
    }



}
