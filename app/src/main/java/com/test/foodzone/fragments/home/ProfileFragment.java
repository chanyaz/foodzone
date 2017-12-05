package com.test.foodzone.fragments.home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.test.foodzone.R;
import com.test.foodzone.constants.Constants;
import com.test.foodzone.interfaces.activities.IActivity;
import com.test.foodzone.interfaces.activities.IHomeScreenActivity;
import com.test.foodzone.utils.Logger;
import com.test.foodzone.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    IActivity iActivity;
    IHomeScreenActivity iHomeScreenActivity;

    @BindView(R.id.gmail)
    Button gmail;

    @BindView(R.id.fblogin)
    LoginButton fblogin;

    @BindView(R.id.llName)
    LinearLayout llName;

    @BindView(R.id.llPhone)
    LinearLayout llPhone;

    @BindView(R.id.llEmail)
    LinearLayout llEmail;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.tvEmail)
    TextView tvEmail;


    SharedPreferences PREFS;


    private List<String> mPermissions = Arrays.asList("public_profile", "email");
    CallbackManager callbackManager;

    private GoogleSignInOptions gso;
    private int RC_SIGN_IN = 10011;
    private GoogleApiClient mGoogleApiClient;

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
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        iActivity = (IActivity)getActivity();
        iHomeScreenActivity = (IHomeScreenActivity)getActivity();
        callbackManager = CallbackManager.Factory.create();
        PREFS = iActivity.getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        ButterKnife.bind(this,view);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        fblogin.setBackgroundResource(R.drawable.ic_facebook);
        fblogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        fblogin.setText("");
        fblogin.setCompoundDrawablePadding(0);
        fblogin.setPadding(
                Utility.dpSize(iActivity.getActivity(), 15),
                Utility.dpSize(iActivity.getActivity(), 15),
                Utility.dpSize(iActivity.getActivity(), 16),
                Utility.dpSize(iActivity.getActivity(), 16)
        );



        fblogin.setReadPermissions(mPermissions);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {


                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        Logger.d("Facebook Response",""+json.toString());
                        try
                        {
                            if (json != null)
                            {

                                Utility.set(PREFS,Constants.LAST_NAME,json.getString("last_name"));

                                Utility.set(PREFS,Constants.FIRST_NAME,json.getString("first_name"));

                                if(json.has("email"))
                                    Utility.set(PREFS,Constants.USER_EMAIL,json.getString("email"));
                                else
                                    Utility.set(PREFS,Constants.USER_EMAIL,"");


                                updateUI();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                Utility.showToast(iActivity.getActivity(), "Not Granted Permission", false);
            }
        };

        fblogin.registerCallback(callbackManager, callback);




        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return view;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //iActivity.showSnackBar("Checking",2);
        //If signin
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void signIn()
    {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //Starting intent for result
        this.startActivityForResult(signInIntent, RC_SIGN_IN);
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

    // Google Details

    private void handleSignInResult(GoogleSignInResult result)
    {
        //If the login succeed result.getStatus().getStatusCode();
        Logger.d("Google Response Status",""+result.getStatus().toString());
        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();

            Utility.set(PREFS, Constants.LAST_NAME,acct.getFamilyName());
            Utility.set(PREFS,Constants.FIRST_NAME,acct.getGivenName());
            Utility.set(PREFS,Constants.USER_EMAIL,acct.getEmail());




            signOut();
            updateUI();


        }
        else
        {
            //If login fails
           // iActivity.showSnackBar("Login Failed",2);
            Utility.showToast(iActivity.getActivity(), "Login Failed ", false);

        }
    }




    public void updateUI()
    {
        tvUserName.setText(Utility.getCamelCase(PREFS.getString(Constants.FIRST_NAME,"")+ " "+PREFS.getString(Constants.LAST_NAME,"") ));

        tvEmail.setText(PREFS.getString(Constants.USER_EMAIL,"").toLowerCase());
        iHomeScreenActivity.hiddenBottomSheet();
        iActivity.showSnackBar("Profile is Updated",2);




    }

}
