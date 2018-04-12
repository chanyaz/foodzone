package com.maya.vgarages.fragments.start;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

    CallbackManager callbackManager;

    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;

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

        setUpFB();

        setUpGoogle();

        llGoogleSignIn.setOnClickListener(v -> {signInByGoogle();});

        tvSignIn.setOnClickListener(v -> {goToSignInFragment();});

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    private void signInByGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        this.startActivityForResult(signInIntent, Utility.generateRequestCodes().get("GOOGLE_SIGN_IN"));
    }

    private void setUpFB()
    {
        callbackManager = CallbackManager.Factory.create();
        fbSignIn.setText("Continue with Facebook");
        fbSignIn.setCompoundDrawablePadding(Utility.dpSize(activity(),10));
        fbSignIn.setTypeface(Typeface.createFromAsset(activity().getAssets(), "fonts/RalewayMedium.ttf"));
        fbSignIn.setReadPermissions(Constants.FB_PERMISSIONS);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        Logger.d("Facebook Response",""+json.toString());
                        parseFBJson(json);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,cover,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                showAlert();
            }
            @Override
            public void onError(FacebookException e) {
                showAlert();
            }
            private void showAlert()
            {
                showSnackBar("Not granted permission",2);
            }
        };
        fbSignIn.registerCallback(callbackManager, callback);
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

    private void parseGoogleResult(GoogleSignInResult result)
    {
        Logger.d("Google Response Status",""+result.getStatus().toString());
        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            String id = "GOOGLE:" + acct.getId();
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
                    Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, "");
                }
            }
            catch (Exception e)
            {
                Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, "");
            }

            Utility.setBoolen(Utility.getSharedPreferences(), Constants.LOGIN, true);
            signOut();
        }
        else
        {
            showSnackBar("Login failed", 2);
        }
    }

    public void parseFBJson(JSONObject json)
    {
        try
        {
            if (json != null)
            {
                String id = "FACEBOOK:"+json.getString("id");

                Utility.setString(Utility.getSharedPreferences(),Constants.USER_ID,id);

                Utility.setString(Utility.getSharedPreferences(),Constants.LAST_NAME,json.getString("last_name"));

                Utility.setString(Utility.getSharedPreferences(),Constants.FIRST_NAME,json.getString("first_name"));

                if(json.has("email"))
                    Utility.setString(Utility.getSharedPreferences(),Constants.USER_EMAIL,json.getString("email"));
                else
                    Utility.setString(Utility.getSharedPreferences(),Constants.USER_EMAIL,"abc@warrous.com");

                Utility.setString(Utility.getSharedPreferences(),Constants.USER_NAME,json.getString("name"));

                String profilePicUrl = "";
                if (json.has("picture"))
                {
                    profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                }
                else
                {
                    profilePicUrl = "";
                }
                Utility.setString(Utility.getSharedPreferences(),Constants.USER_PHOTO_URL,profilePicUrl);

                Utility.setBoolen(Utility.getSharedPreferences(),Constants.LOGIN,true);
            }
            else
            {
                showSnackBar(Constants.SOMETHING_WENT_WRONG,2);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
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

    private void goToSignInFragment()
    {
        ((SplashFragment) getParentFragment()).goToSignIn();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utility.generateRequestCodes().get("GOOGLE_SIGN_IN"))
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            parseGoogleResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
