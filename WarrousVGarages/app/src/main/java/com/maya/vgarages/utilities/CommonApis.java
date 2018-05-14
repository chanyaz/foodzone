package com.maya.vgarages.utilities;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.maya.vgarages.application.VGaragesApplication;
import com.maya.vgarages.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gokul Kalagara on 5/10/2018.
 */
public class CommonApis
{
    public static void sendFcmToken(Activity activity, String fcmToken)
    {
        String URL = Constants.URL_INSERT_USER_PNS;
        //input
        JSONObject input = new JSONObject();
        try
        {
            input.put("PNSId", Utility.getString(Utility.getSharedPreferences(), Constants.USER_FCM_TOKEN));
            input.put("SecureId", Constants.SERIAL_PROJECT_ID);
            input.put("UserId", Utility.getString(Utility.getSharedPreferences(), Constants.USER_ID));
            input.put("IMEI", Utility.getPhoneUniqueId(activity));
            input.put("FirstName", Utility.getString(Utility.getSharedPreferences(), Constants.FIRST_NAME));
            input.put("LastName", Utility.getString(Utility.getSharedPreferences(), Constants.LAST_NAME));
            input.put("EmailAddress", Utility.getString(Utility.getSharedPreferences(), Constants.USER_EMAIL));
            input.put("PhoneNumber", "");
            input.put("RoleId", "0");
            input.put("Type", "A");
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
                    public void onResponse(String response) {
                        Utility.setString(Utility.getSharedPreferences(), Constants.CURRENT_USER_FCM_TOKEN, fcmToken);
                        Logger.d("[response]", response);
                    }
                }
                ,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Logger.d("[response]", "Unable to contact server");

                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
//                if(Utility.getSharedPreferences().contains(Constants.TOKEN_TYPE))
//                {
//                    params.put("Authorization", Utility.getString(Utility.getSharedPreferences(),Constants.TOKEN_TYPE)+ " " + Utility.getString(Utility.getSharedPreferences(),Constants.ACCESS_TOKEN));
//                }
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
}
