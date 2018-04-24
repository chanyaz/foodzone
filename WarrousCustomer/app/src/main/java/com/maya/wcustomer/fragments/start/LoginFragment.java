package com.maya.wcustomer.fragments.start;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.maya.wcustomer.R;
import com.maya.wcustomer.activities.SplashActivity;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.interfaces.fragments.IFragment;
import com.maya.wcustomer.utilities.Logger;
import com.maya.wcustomer.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements IFragment ,GoogleApiClient.OnConnectionFailedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.llGoogleSignIn)
    LinearLayout llGoogleSignIn;

    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        setUpGoogle();
        llGoogleSignIn.setOnClickListener(v -> {signInByGoogle();});
    }

    private void signInByGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Utility.generateRequestCodes().get("GOOGLE_SIGN_IN"));
    }


    private void setUpGoogle()
    {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing google api client
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void parseGoogleResult(GoogleSignInResult result)
    {
        Logger.d("Google Response Status",""+result.getStatus().toString());
        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            String id = "GOOGLE_" + acct.getId();
            Utility.setString(Utility.getSharedPreferences(), Constants.USER_ID, id);
            Utility.setString(Utility.getSharedPreferences(), Constants.LAST_NAME, acct.getFamilyName());
            Utility.setString(Utility.getSharedPreferences(), Constants.FIRST_NAME, acct.getGivenName());
            Utility.setString(Utility.getSharedPreferences(), Constants.USER_EMAIL, acct.getEmail());
            Utility.setString(Utility.getSharedPreferences(), Constants.USER_NAME, acct.getDisplayName());
            try
            {
                if (!acct.getPhotoUrl().toString().isEmpty())
                {
                    Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, acct.getPhotoUrl().toString());
                }
                else
                {
                    Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, Constants.AVATAR_IMAGE);
                }
            }
            catch (Exception e)
            {
                Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL,  Constants.AVATAR_IMAGE);
            }

            Utility.setBoolen(Utility.getSharedPreferences(), Constants.LOGIN, true);
            signOut();
            ((SplashActivity) activity()).goToHome();
        }
    }

    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status)
                    {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Logger.d("INSIDE THE THIS");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utility.generateRequestCodes().get("GOOGLE_SIGN_IN"))
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            parseGoogleResult(result);
        }
    }
}
