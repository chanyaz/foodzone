package com.maya.wcustomer.services;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.utilities.Logger;
import com.maya.wcustomer.utilities.Utility;





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
