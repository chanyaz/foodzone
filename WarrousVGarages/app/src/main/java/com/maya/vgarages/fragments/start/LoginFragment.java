package com.maya.vgarages.fragments.start;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.SplashActivity;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.tvSignUp)
    TextView tvSignUp;

    String firstName = "Default";
    String lastName = "User";
    int type = 0;
    String userName= null, userId = null, email = null, phone = null;



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

        etUsername.setText("jacobpaul.kuchipudi@gmail.com");
        etPassword.setText("Anu@123456");
        tvSignUp.setOnClickListener(v -> {goToStartFragment();});
        tvLogin.setOnClickListener(v -> {doLogin();});
    }

    private void doLogin()
    {
        if(Utility.isNetworkAvailable(activity()))
        {
            if((""+etUsername.getText()).trim().length()>0 && (""+etPassword.getText()).trim().length()>0)
            {
                doLoginApiVerify();
            }
            else
            {
                showSnackBar("Please fill the fields",2);
            }
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }
    }

    private void doLoginApiVerify()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_LOGIN;


        Map<String,String> input = new HashMap<String,String>();
        input.put("username",etUsername.getText().toString().trim());
        input.put("password",etPassword.getText().toString());
        //&grant_type=password&scope=api1+offline_access&client_id=ro.client&client_secret=secret&org_name=Kia&user_type_name=Dealer
        input.put("grant_type","password");
        input.put("scope","api1 offline_access");
        input.put("client_id","ro.client");
        input.put("client_secret","secret");
        input.put("org_name","Kia");
        input.put("user_type_name","Dealer");

        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]",response);

                try
                {

                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.has(Constants.ACCESS_TOKEN))
                    {

                        Utility.setString(Utility.getSharedPreferences(),Constants.ACCESS_TOKEN,jsonObject.getString(Constants.ACCESS_TOKEN));
                        Utility.setString(Utility.getSharedPreferences(),Constants.EXPIRES_IN,jsonObject.getString(Constants.EXPIRES_IN));
                        Utility.setString(Utility.getSharedPreferences(),Constants.TOKEN_TYPE,jsonObject.getString(Constants.TOKEN_TYPE));

                        try
                        {
                                JWT jwt = new JWT(jsonObject.getString(Constants.ACCESS_TOKEN));
                                firstName =  jwt.getClaim("FirstName").asString();
                                lastName  =  jwt.getClaim("LastName").asString();
                                userName  =  jwt.getClaim("UserName").asString();
                                userId  =  jwt.getClaim("UserId").asString();
                                email  =  jwt.getClaim("Email").asString();
                                phone  =  jwt.getClaim("PhoneNumber").asString();


                                Utility.setString(Utility.getSharedPreferences(),Constants.FIRST_NAME,firstName);
                                Utility.setString(Utility.getSharedPreferences(),Constants.LAST_NAME,lastName);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_NAME,firstName + " " + lastName);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_EMAIL,email);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_ID,userId);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_PHONE_NUMBER,phone);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_PHOTO_URL,Constants.AVATAR_IMAGE);
                                Utility.setBoolen(Utility.getSharedPreferences(),Constants.LOGIN,true);


                                Utility.closeProgressDialog(progressDialog);
                                ((SplashActivity) activity()).attachNextFragment();
                        }
                        catch (DecodeException exception)
                        {
                            //Invalid token

                        }


                    }
                    else
                    {
                        showSnackBar("Invalid username or password",0);
                    }
                }
                catch (Exception e)
                {
                    //etUserName.setText("");
                    //etPassword.setText("");
                    showSnackBar("Invalid username or password",0);
                }


                Utility.closeProgressDialog(progressDialog);


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                Utility.closeProgressDialog(progressDialog);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL,Constants.POST_REQUEST);

    }

    public void goToNextOtherThings()
    {

    }

    private void goToStartFragment()
    {
        ((SplashFragment) getParentFragment()).goToSignUp();
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
