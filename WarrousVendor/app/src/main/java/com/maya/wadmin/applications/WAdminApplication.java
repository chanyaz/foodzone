package com.maya.wadmin.applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maya.wadmin.apis.volley.LruBitmapCache;
import com.maya.wadmin.constants.Constants;

/**
 * Created by Gokul Kalagara on 1/26/2018.
 */

public class WAdminApplication extends Application
{
    public static final String TAG = WAdminApplication.class.getSimpleName();
    private static WAdminApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public SharedPreferences sharedPreferences;


    @Override
    public void onCreate()
    {
        super.onCreate();
        sharedPreferences = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        mInstance = this;
    }

    public static synchronized WAdminApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        try {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }
        }
        catch (Exception e)
        {

        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        StringRequest request;
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
