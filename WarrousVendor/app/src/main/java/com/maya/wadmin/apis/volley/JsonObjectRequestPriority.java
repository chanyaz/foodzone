package com.maya.wadmin.apis.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.utilities.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gokul Kalagara on 16-Jun-16.
 *
 */
public class JsonObjectRequestPriority extends JsonObjectRequest
{
    private Priority mPriority = Priority.LOW;
    public JsonObjectRequestPriority(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public JsonObjectRequestPriority(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }



    public void setPriority(Priority priority){
        mPriority = priority;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String,String> params = new HashMap<String, String>();
        if(Utility.getSharedPreferences().contains(Constants.TOKEN_TYPE))
        {
            params.put("Authorization", Utility.getString(Utility.getSharedPreferences(),Constants.TOKEN_TYPE)+ " " + Utility.getString(Utility.getSharedPreferences(),Constants.ACCESS_TOKEN));
        }
        return params;
    }
}