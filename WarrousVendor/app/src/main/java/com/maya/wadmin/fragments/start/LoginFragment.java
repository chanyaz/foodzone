package com.maya.wadmin.fragments.start;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.MainActivity;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    CoordinatorLayout coordinatorLayout;
    EditText etUserName,etPassword;
    RelativeLayout rlCheckUserName, rlCheckPassword, rlPassword, rlUserName;
    TextView tvPortalName;

    String firstName = "Default";
    String lastName = "User";
    int type = 0;
    String userName= null, userRoll = null, dealerId = null;


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
        initialize(view);

        return view;
    }

    private void initialize(View view) {

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        rlCheckUserName = view.findViewById(R.id.rlCheckUserName);
        rlCheckPassword = view.findViewById(R.id.rlCheckPassword);
        rlUserName = view.findViewById(R.id.rlUserName);
        rlPassword = view.findViewById(R.id.rlPassword);
        tvPortalName = view.findViewById(R.id.tvPortalName);

        tvPortalName.setText(Constants.portalsTypeIDS[ Utility.getPortalType()]);


        etUserName.setText("superadmin@warrous.com");
        etPassword.setText("Warrous@123");

        rlPassword.setVisibility(View.GONE);

        rlCheckUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(Utility.isNetworkAvailable(activity()))
                    verifyUserName();
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
                }
            }
        });

        rlCheckPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(Utility.isNetworkAvailable(activity()))
                    verifyUser();
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
                }
            }
        });
    }

    private void verifyUser()
    {
        Utility.hideKeyboard(activity());
        String username = ""+etUserName.getText();
        String password = ""+etPassword.getText();

        if(username.trim().length()>0&&password.trim().length()>0)
        {
            doApiVerify();
        }
        else
        {
            showSnackBar("Please fill the fields",0);
        }

    }

    private void doApiVerify()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_LOGIN;
        Map<String,String> input = new HashMap<String,String>();
        try
        {
            input.put("username",etUserName.getText().toString().trim());
            input.put("password",etPassword.getText().toString().trim());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]",response);

                try
                {
                    type = 0;
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
                            dealerId  =  jwt.getClaim("dealerid").asString();

                            String[] roles = jwt.getClaim("role").asArray(String.class);
                            try
                            {
                                if(roles != null && roles.length>0)
                                {

                                }
                                else
                                {
                                    roles = new String[1];
                                    roles[0] = jwt.getClaim("role").asString();
                                }
                            }
                            catch (Exception e)
                            {

                            }

                            generateRole(roles);

                            if(type>0)
                            {
//                                Utility.showSnackBar(getActivity(), coordinatorLayout, userRoll, 0);
//                                Utility.closeProgressDialog(progressDialog);
//                                return;
                                Utility.setString(Utility.getSharedPreferences(),Constants.FIRST_NAME,firstName);
                                Utility.setString(Utility.getSharedPreferences(),Constants.LAST_NAME,lastName);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_ROLL_NAME,userRoll);
                                Utility.setInt(Utility.getSharedPreferences(),Constants.USER_TYPE,type);
                                Utility.setBoolen(Utility.getSharedPreferences(),Constants.LOGIN,true);
                                Utility.setString(Utility.getSharedPreferences(),Constants.USER_NAME,userName);
                                Utility.setString(Utility.getSharedPreferences(),Constants.DEALER_ID,dealerId);
                            }
                            else
                            {
                                Utility.showSnackBar(getActivity(), coordinatorLayout, Constants.INVALID_OPERATION, 0);
                            }


                            Logger.d("payload",firstName + " " + lastName + " "+ userName + " "+ dealerId + " " +type);
                        }
                        catch (DecodeException exception)
                        {
                            //Invalid token

                        }

                    }
                    else
                    {
                        //etUserName.setText("");
                        etPassword.setText("");
                        showSnackBar("Invalid password",0);
                    }
                }
                catch (Exception e)
                {
                    //etUserName.setText("");
                    etPassword.setText("");
                    showSnackBar("Invalid password",0);
                }


                Utility.closeProgressDialog(progressDialog);
                if(type>0 && Utility.getBoolen(Utility.getSharedPreferences(),Constants.LOGIN))
                {
                    Intent intent = new Intent(activity(), MainActivity.class);
                    startActivity(intent);
                    activity().finish();
                }

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


    private void verifyUserName()
    {
        Utility.hideKeyboard(activity());
        String username = ""+etUserName.getText();
         if(username.trim().length()>0)
        {
            doUserNameVerify();
        }
        else
        {
            showSnackBar("Please fill the fields",0);
        }
    }

    private void doUserNameVerify()
    {

        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_CHECK_USERNAME + etUserName.getText().toString().trim();
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {
                Utility.closeProgressDialog(progressDialog);
                Logger.d("[response]",response);
                try
                {
                    int value = Integer.parseInt(response);
                    if(value==1)
                    {
                        rlUserName.setVisibility(View.GONE);
                        rlPassword.setVisibility(View.VISIBLE);
                    }
                    else
                    {

                        etPassword.setText("");
                        showSnackBar("Invalid username",0);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


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
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);


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


    public void generateRole(String[] list)
    {
        String contactList = "";
        for(int i=0;i<list.length;i++)
        {
            contactList = contactList + list[i] + " ";
        }
        Logger.d("list",contactList);

        if (contactList.contains(Constants.SUPER_ADMIN))
        {
            type = 1439;
            userRoll = Constants.SUPER_ADMIN;
            return;
        }
        else if(contactList.contains(Constants.SALES_PERSON))
        {
            type = 6;
            userRoll = Constants.SALES_PERSON;
            return;
        }
        else if(contactList.contains(Constants.VEHICLE_SALES_MANAGER))
        {
            type = 7;
            userRoll = Constants.VEHICLE_SALES_MANAGER;
            return;
        }
        else if(contactList.contains(Constants.VEHICLE_SERVICE_TECHNICIAN))
        {
            type = 5;
            userRoll = Constants.VEHICLE_SERVICE_TECHNICIAN;
            return;
        }
        else if(contactList.contains(Constants.GENERAL_MANAGER))
        {
            type = 4;
            userRoll = Constants.GENERAL_MANAGER;
            return;
        }

        else
        {

        }

    }





}
