package com.maya.wadmin.utilities;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gokul Kalagara on 4/3/2018.
 */

public class CommonApiCalls
{

    public static void refreshAuthTokenCall()
    {
        String URL = Constants.URL_LOGIN;
        Map<String,String> input = new HashMap<String,String>();
        try
        {
            input.put("username",Utility.getString(Utility.getSharedPreferences(),Constants.USER_NAME));
            input.put("password",Utility.getString(Utility.getSharedPreferences(),Constants.USER_PASSWORD));
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
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has(Constants.ACCESS_TOKEN))
                    {
                        Utility.setString(Utility.getSharedPreferences(),Constants.ACCESS_TOKEN,jsonObject.getString(Constants.ACCESS_TOKEN));
                        Utility.setString(Utility.getSharedPreferences(),Constants.EXPIRES_IN,jsonObject.getString(Constants.EXPIRES_IN));
                        Utility.setString(Utility.getSharedPreferences(),Constants.TOKEN_TYPE,jsonObject.getString(Constants.TOKEN_TYPE));
                    }
                    else
                    {

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
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL,Constants.POST_REQUEST);
    }
}
