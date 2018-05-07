package com.maya.vgarages.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.start.SplashFragment;
import com.maya.vgarages.fragments.start.StartFragment;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements IFragment, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    SplashFragment splashFragment;

    public GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationClient;

    public CallbackManager callbackManager;

    public FacebookCallback<LoginResult> callback;

    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClientGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(activity(),MainActivity.class);
//        startActivity(intent);
//        finish();

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, splashFragment = SplashFragment.newInstance(null, null)).commit();

    }


    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Utility.generateRequestCodes().get("REQUEST_LOCATION_PERMISSION"));
    }

    public void openLocationFragment() {
        splashFragment.openGetLocationFragment();
    }


    public void createLocationRequest() {
        mGoogleApiClient = null;
        mGoogleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(3 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));


        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************


        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());


        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                    SplashActivity.this.updateRequestLocation();
                    }
                    catch (ApiException exception)
                    {
                    switch (exception.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try
                            {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        SplashActivity.this,
                                        Constants.LOCATION_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });


    }


    public void goToHome(Location location)
    {
        Logger.d("LOCATION DATA",""+ location.getLatitude() + ", "+location.getLongitude() );
        stopLocationUpdates();
        Intent intent = new Intent(activity(),MainActivity.class);
        intent.putExtra("Location",location);
        startActivity(intent);
        finish();
    }


    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(), coordinatorLayout, snackBarText, type);
    }

    @Override
    public Activity activity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        try {
            if (splashFragment.getChildFragmentManager().getBackStackEntryCount() > 1) {
                splashFragment.getChildFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void signInByGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClientGoogle);
        startActivityForResult(signInIntent, Utility.generateRequestCodes().get("GOOGLE_SIGN_IN"));
    }

    public void setUpFB()
    {
        callbackManager = CallbackManager.Factory.create();

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        Logger.d("Facebook Response",""+json.toString());
                        parseFBJson(json);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,cover,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                showAlert();
            }
            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                showAlert();
            }
            private void showAlert()
            {
                showSnackBar("It's in developer mode",2);
            }
        };
    }

    public void setUpGoogle()
    {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing google api client
        if(mGoogleApiClientGoogle == null) {
            mGoogleApiClientGoogle = new GoogleApiClient.Builder(activity())
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    private void parseGoogleResult(GoogleSignInResult result)
    {
        Logger.d("Google Response Status",""+result.getStatus().toString());
        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            String id = "GOOGLE_" + acct.getId();
            Utility.setString(Utility.getSharedPreferences(), Constants.USER_ID, id);
            Utility.setString(Utility.getSharedPreferences(), Constants.LAST_NAME, acct.getFamilyName());
            Utility.setString(Utility.getSharedPreferences(), Constants.FIRST_NAME, acct.getGivenName());
            Utility.setString(Utility.getSharedPreferences(), Constants.USER_EMAIL, acct.getEmail());
            Utility.setString(Utility.getSharedPreferences(), Constants.USER_NAME, acct.getDisplayName());
            try
            {
                if (!acct.getPhotoUrl().toString().isEmpty())
                {
                    Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, acct.getPhotoUrl().toString());
                }
                else
                {
                    Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, Constants.AVATAR_IMAGE);
                }
            }
            catch (Exception e)
            {
                Utility.setString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL, Constants.AVATAR_IMAGE);
            }

            Utility.setBoolen(Utility.getSharedPreferences(), Constants.LOGIN, true);
            signOut();
            attachNextFragment();
        }

    }


    public void attachNextFragment()
    {
        splashFragment.setUpChildFragment(splashFragment.generateFragmentKey());
    }


    public void parseFBJson(JSONObject json)
    {
        try
        {
            if (json != null)
            {
                String id = "FACEBOOK_"+json.getString("id");

                Utility.setString(Utility.getSharedPreferences(),Constants.USER_ID,id);

                Utility.setString(Utility.getSharedPreferences(),Constants.LAST_NAME,json.getString("last_name"));

                Utility.setString(Utility.getSharedPreferences(),Constants.FIRST_NAME,json.getString("first_name"));

                if(json.has("email"))
                    Utility.setString(Utility.getSharedPreferences(),Constants.USER_EMAIL,json.getString("email"));
                else
                    Utility.setString(Utility.getSharedPreferences(),Constants.USER_EMAIL,"abc@warrous.com");

                Utility.setString(Utility.getSharedPreferences(),Constants.USER_NAME,json.getString("name"));

                String profilePicUrl = "";
                if (json.has("picture"))
                {
                    profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                }
                else
                {
                    profilePicUrl = Constants.AVATAR_IMAGE;
                }
                Utility.setString(Utility.getSharedPreferences(),Constants.USER_PHOTO_URL,profilePicUrl);

                Utility.setBoolen(Utility.getSharedPreferences(),Constants.LOGIN,true);

                signOutFacebook();
                attachNextFragment();
            }
            else
            {
                showSnackBar(Constants.SOMETHING_WENT_WRONG,2);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClientGoogle).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status)
                    {

                    }
                });
    }

    private void signOutFacebook()
    {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utility.generateRequestCodes().get("REQUEST_LOCATION_PERMISSION")
                &&
                !(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) == false) {
                    Toast.makeText(this, "Open permissions and give all the permissions in order to access the app", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    return;
                }
                else
                {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Utility.generateRequestCodes().get("REQUEST_LOCATION_PERMISSION"));
                        return;
                    }
                }
            }
        }


        switch (requestCode) {
            case 18103: //REQUEST_LOCATION_PERMISSION
                openLocationFragment();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        if(callbackManager!=null)
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case Constants.LOCATION_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        updateRequestLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        splashFragment.updateUIinLocationFragment();
                        break;
                    default:

                        break;
                }
                break;
            case 18101: //Utility.generateRequestCodes().get("GOOGLE_SIGN_IN")
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                parseGoogleResult(result);
                break;
        }
    }


    public void updateRequestLocation()
    {
        Logger.d("LOCATION DATA INSIDE");
        mFusedLocationClient = null;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();
        splashFragment.updateUIinLocationFragment();
    }

    private void startLocationUpdates()
    {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest,
                mLocationCallback,
                null);
    }

    private void stopLocationUpdates()
    {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    LocationCallback mLocationCallback = new LocationCallback()
    {
    @Override
    public void onLocationResult(LocationResult locationResult)
    {
        Logger.d("INSIDE THE DATA");
        if (locationResult == null)
        {
            return;
        }
        for (Location location : locationResult.getLocations())
        {
            if(location!=null)
            {
                goToHome(location);
                return;
            }
            // Update UI with location data
            // ...
        }
    };
    };
}
