package com.maya.vgarages.service;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;


/**
 * Created by Maya on 11/30/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "FCM_TOKEN";

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(Constants.USER_FCM_TOKEN, "Refreshed token: " + refreshedToken);
        if (refreshedToken!=null&&refreshedToken.length()>10)
        {
            try
            {
                Utility.setString(Utility.getSharedPreferences(), Constants.USER_FCM_TOKEN, refreshedToken);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Logger.d(TAG,e.getMessage());
            }
        }
    }


}
