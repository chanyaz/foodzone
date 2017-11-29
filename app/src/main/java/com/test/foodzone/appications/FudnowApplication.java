package com.test.foodzone.appications;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.test.foodzone.apis.volley.LruBitmapCache;

/**
 * Created by home on 11/29/2017.
 */

public class FudnowApplication extends MultiDexApplication
{
    public static final String TAG = FudnowApplication.class.getSimpleName();
    private static FudnowApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

        mInstance = this;
    }

    public static synchronized FudnowApplication getInstance() {
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
