package com.maya.vgarages.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gokul Kalagara on 4/24/2018.
 */
public class FetchAddressIntentService extends IntentService
{
    private static final String TAG = "FetchAddressIS";

    private ResultReceiver mReceiver;

    public FetchAddressIntentService()
    {
        // Use the TAG to name the worker thread.
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        // Check if receiver was properly registered.
        if (mReceiver == null)
        {
            Logger.d(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        if (location == null)
        {
            errorMessage = "NO DATA";
            Logger.e(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try
        {

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, we get just a single address.
                    1);

        }
        catch (IOException ioException)
        {
            // Catch network or other I/O problems.
            errorMessage = "NOT AVAILABLE";
            Logger.e(TAG, errorMessage, ioException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "ERROR";
            Logger.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        if (addresses == null || addresses.size()  == 0)
        {
            if (errorMessage.isEmpty())
            {
                errorMessage = "NO ADDRESS FOUND";
                Logger.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        }
        else
        {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)

                        for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                        {
                            addressFragments.add(address.getAddressLine(i));
                        }

            addressFragments.add(address.getLocality());
            addressFragments.add(address.getAdminArea());
            addressFragments.add(address.getCountryName());

            Utility.setString(Utility.getSharedPreferences(),Constants.USER_LOCALITY_ADDRESS,address.getLocality());
            Utility.setString(Utility.getSharedPreferences(),Constants.USER_COMPLETE_ADDRESS,TextUtils.join(System.getProperty("line.separator"), addressFragments));

            Logger.d(TAG, "ADDRESS FOUND");
            //deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
            deliverResultToReceiver(Constants.SUCCESS_RESULT, address.getLocality()+", "+address.getAdminArea());
        }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, String message)
    {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}