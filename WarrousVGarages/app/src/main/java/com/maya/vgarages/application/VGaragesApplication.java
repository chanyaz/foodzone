package com.maya.vgarages.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.maya.vgarages.apis.volley.LruBitmapCache;
import com.maya.vgarages.constants.Constants;

/**
 * Created by Gokul Kalagara on 4/5/2018.
 */

public class VGaragesApplication extends Application
{
    public static final String TAG = VGaragesApplication.class.getSimpleName();
    private static VGaragesApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public SharedPreferences sharedPreferences;


    @Override
    public void onCreate()
    {
        super.onCreate();
        //FacebookSdk.sdkInitialize(getApplicationContext());
        sharedPreferences = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        mInstance = this;
    }

    public static synchronized VGaragesApplication getInstance() {
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

