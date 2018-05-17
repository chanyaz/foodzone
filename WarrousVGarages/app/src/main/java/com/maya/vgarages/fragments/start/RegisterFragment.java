package com.maya.vgarages.fragments.start;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.application.VGaragesApplication;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tvRegister)
    TextView tvRegister;

    @BindView(R.id.etFirstName)
    EditText etFirstName;

    @BindView(R.id.etLastName)
    EditText etLastName;

    @BindView(R.id.etEmailAddress)
    EditText etEmailAddress;

    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this,view);


        initialize();
        return view;
    }

    private void initialize()
    {
        tvRegister.setOnClickListener(v ->
        {
            if(Utility.isNetworkAvailable(activity()))
            {
                doRegister();
            }
            else
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        });

        imgBack.setOnClickListener(v -> {activity().onBackPressed();});
    }

    private void doRegister()
    {
        String firstName = ("" + etFirstName.getText()).trim();
        String lastName = ("" + etLastName.getText()).trim();
        String email = ("" + etEmailAddress.getText()).trim();
        String phone = "" + etPhoneNumber.getText();
        String password = "" + etPassword.getText();
        String confirmPassword = "" + etConfirmPassword.getText();
        if(firstName.length()>0 && lastName.length()>0 && email.length()>0 && phone.length()==10 && password.length()>0 && confirmPassword.length()>0)
        {
            if(password.equals(confirmPassword))
            {
                if(Utility.isValidEmail(email))
                {
                    if(Utility.isValidMobile(phone))
                    {

                    }
                    else
                    {
                        showSnackBar("Invalid phone number",2);
                        return;
                    }
                }
                else
                {
                    showSnackBar("Invalid email address",2);
                    return;
                }
            }
            else
            {
                showSnackBar("Miss match password and confirm password",2);
                return;
            }
        }
        else
        {
            showSnackBar("Please fill fields",2);
            return;
        }

        callRegisterApi(firstName,lastName,email,phone,password);
    }

    private void callRegisterApi(String firstName, String lastName, String email, String phone, String password)
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_REGISTER;
        JSONObject input = new JSONObject();
        try
        {
            input.put("FirstName",firstName);
            input.put("LastName",lastName);
            input.put("Email", email);
            input.put("PhoneNumber", phone);
            input.put("Password", password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        final String requestBody = input.toString();
        Logger.d("[URL]", URL);
        Logger.d("[INPUT]", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Logger.d("[response]", response);

                        Utility.closeProgressDialog(progressDialog);
                        try
                        {
                            int value = Integer.parseInt(response);
                            if(value!=0)
                            {
                                showSnackBar("Registered Successfully",1);
                                etFirstName.setText("");
                                etLastName.setText("");
                                etEmailAddress.setText("");
                                etPhoneNumber.setText("");
                                etConfirmPassword.setText("");
                                etPassword.setText("");
                            }
                            else
                            {
                                showSnackBar("Already registered",0);
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
                ,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Logger.d("[response]", "Unable to contact server");
                        showSnackBar(Constants.CONNECTION_ERROR,2);
                        Utility.closeProgressDialog(progressDialog);

                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try
                {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee)
                {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }



            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        VGaragesApplication.getInstance().getRequestQueue().add(stringRequest);
    }

    @Override
    public void changeTitle(String title)
    {

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
