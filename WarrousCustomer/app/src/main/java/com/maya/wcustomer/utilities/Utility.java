package com.maya.wcustomer.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wcustomer.R;
import com.maya.wcustomer.application.WCustomerApplication;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.models.Action;
import com.maya.wcustomer.models.AlertRule;
import com.maya.wcustomer.models.CarInfo;
import com.maya.wcustomer.models.HomeOption;
import com.maya.wcustomer.models.NearBy;
import com.maya.wcustomer.models.TimeBasedTrips;
import com.maya.wcustomer.models.TimeBasedViolations;
import com.maya.wcustomer.models.Trip;
import com.maya.wcustomer.models.TripPoint;
import com.maya.wcustomer.models.Violation;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gokul Kalagara on 1/25/2018.
 */

public class Utility
{
    public static boolean isNetworkAvailable(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
        {
            /* show a toast, we cannot use the internet right now */
            //showToast(activity, "network_unavailable_message", Constants.NO_INTERNET_CONNECTION, true);
            /*if(HomeScreenActivity.mHomeScreenActivity != null){
                HomeScreenActivity.mHomeScreenActivity.tvInternetStatus.setText(Constants.NO_INTERNET_CONNECTION);
            }*/
            return false;
            /* aka, do nothing */
        }
        return true;
    }


    public static boolean isValidEmail(CharSequence paramCharSequence)
    {
        if (paramCharSequence == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(paramCharSequence).matches();
    }

    public static boolean isValidLicenseNo(String paramString)
    {
        String str = paramString;
        if (paramString != null) {
            str = paramString.replaceAll(" ", "");
        }
        return str.matches("[a-zA-Z]{2,2}-[0-9]{1,2}-[a-zA-Z]{1,2}-[0-9]{1,4}");
    }

    public static boolean isValidMobile(String paramString)
    {
        return paramString.matches("[0-9]{10}");
    }

    public static void deleteSharedPreferences()
    {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear().commit();
    }


    public static SharedPreferences getSharedPreferences()
    {
        return WCustomerApplication.getInstance().sharedPreferences;
    }

    public static void setString(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBoolen(SharedPreferences sharedPreferences, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setInt(SharedPreferences sharedPreferences,String key, int value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(SharedPreferences sharedPreferences,String key)
    {
        return sharedPreferences.getString(key,null);
    }

    public static boolean getBoolen(SharedPreferences sharedPreferences,String key)
    {
        return sharedPreferences.getBoolean(key,false);
    }

    public static int getInt(SharedPreferences sharedPreferences,String key)
    {
        return sharedPreferences.getInt(key,0);
    }

    public static void del(SharedPreferences sharedPreferences, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getCamelCase(String name) {
        int flag=1439;
        name=name.trim();
        name=name.toLowerCase();

        while(flag==1439)
        {

            if(name.contains("  "))
            {

                name=name.replaceAll("  "," ");

            }
            else
            {
                flag=3914;
            }


        }

        StringBuilder sb = new StringBuilder();
        try{
            String[] words = name.toString().split(" ");
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString());
                }
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
            e.printStackTrace();
            return name;
        }
        return sb.toString();

    }

    public static String getShortName(String name) {
        int flag=1439;
        name=name.trim();
        name=name.toLowerCase();
        while(flag==1439)
        {

            if(name.contains("  "))
            {

                name=name.replaceAll("  "," ");

            }
            else
            {
                flag=3914;
            }


        }

        StringBuilder sb = new StringBuilder();
        try{
            String[] words = name.toString().split(" ");
            if (words[0].length() > 0)
            {
                sb.append(Character.toUpperCase(words[0].charAt(0)));
                for (int i = 1; i < words.length; i++)
                {
                    sb.append(Character.toLowerCase(words[i].charAt(0)));
                }
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
            e.printStackTrace();
            return (""+name.charAt(0)).toUpperCase();
        }

        if(sb.length()>2)
        {
            return (sb.toString()).substring(0,2);
        }
        else
        {
            return sb.toString();
        }
    }

    public static boolean isSuccessful(String s)
    {
        try {
            JSONObject jsonObject = new JSONObject(s);
            return jsonObject.getBoolean("status");
        } catch (Exception e)
        {
            return false;
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static ProgressDialog generateProgressDialog(Context activity)
    {
        try {

            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            progressDialog.setContentView(R.layout.progressdialog);
            return progressDialog;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeProgressDialog(ProgressDialog progressDialog)
    {
        try
        {
            if(progressDialog!=null&&progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void showToast(Context c, String s, boolean duration) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, s, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        tst.show();
    }
    public static void showToast(Context c, String t, String s, boolean duration) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, t, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        tst.setText(s);
        tst.show();
    }

    public static void showToast(Context c, String t, String s, boolean duration, String response) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, t, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("error")) {
                tst.setText(jsonObject.getString("error").toString());
            }
            else if(jsonObject.has("message")){
                tst.setText(jsonObject.getString("message").toString());
            }
            else {
                tst.setText(s);
            }
            tst.show();
        } catch (Exception e) {
            Logger.d("[Exception]", e.toString());
        }
    }

    public static void displayError(JSONObject jsonObject,Context context)
    {
        try {
            Utility.showToast(context,"Error",jsonObject.getString("error"),true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static String makeJSDateReadable(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("-");
            ret = new DateFormatSymbols().getMonths()[Integer.parseInt(arr[1]) - 1] + " " + arr[0] + ret;
            ret = arr[2].split("T")[0] + " " + ret;
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String makeJSDateReadable1(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("/");
            ret = arr[1] + " " + new DateFormatSymbols().getMonths()[Integer.parseInt(arr[0]) - 1]  + ret;
            ret = ret + " " + arr[2].split(" ")[0];
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return s;
        }
    }

    public static String makeDatetoAgo(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("T");
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date parsedTimeStamp = dateFormat.parse(arr[0]);

            Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
            return getTimeAgo(timestamp.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Yesterday";
        }
    }







    @Nullable
    public static Bitmap urlToBitMap(String urlLink){
        try {
            URL url = new URL(urlLink);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String getPhoneNumber(String name, Context context)
    {
        try {
            String ret = null;
            String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "='" + name + "'";
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, selection, null, null);
            if (c.moveToFirst()) {
                ret = c.getString(0);
            }
            c.close();
            if (ret == null)
                ret = "Unsaved";
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }



    public static String removeSpaces(String number){
        String ans="";
        for(int i=0;i<number.length();i++){
            if(number.charAt(i) == ' ' || number.charAt(i) == '-'){
                continue;
            }
            ans = ans +number.charAt(i);
        }
        return ans;
    }

    public static void updateTablayout(TabLayout tabLayout, Activity activity)
    {
        try {

            for (int i = 0; i < tabLayout.getTabCount(); i++) {

                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tablayout_textview, null);
                tv.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/SFMedium.ttf"));
                tabLayout.getTabAt(i).setCustomView(tv);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static String getStringFromCharArray(char[] pin)
    {
        return ""+pin[0]+pin[1]+pin[2]+pin[3];
    }


    public static String getTimeAgo(long time)
    {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < Constants.MINUTE_MILLIS)
        {
            return "just now";
        }
        else if (diff < 2 * Constants.MINUTE_MILLIS)
        {
            return "a minute ago";
        }
        else if (diff < 50 * Constants.MINUTE_MILLIS)
        {
            return diff / Constants.MINUTE_MILLIS + " minutes ago";
        }
        else if (diff < 90 * Constants.MINUTE_MILLIS) {
            return "an hour ago";
        }
        else if (diff < 24 * Constants.HOUR_MILLIS) {
            return diff / Constants.HOUR_MILLIS + " hours ago";
        }
        else if (diff < 48 * Constants.HOUR_MILLIS) {
            return "yesterday";
        }
        else
        {
            return diff / Constants.DAY_MILLIS + " days ago";
        }
    }





    public static HashMap<String,Integer> generateRequestCodes()
    {
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("ASSIGN_PREPARE",14111);
        hashMap.put("ASSIGN_PDI",14112);
        hashMap.put("ASSIGN_TESTDRIVE",14113);
        hashMap.put("LOT_FORM",14114);
        hashMap.put("PDI_FORM",14115);
        hashMap.put("ADD_ALERT",14116);
        hashMap.put("OPEN_PLACES_SEARCH",14116);
        hashMap.put("ADD_ZONE",14117);
        hashMap.put("EDIT_ZONE",14118);
        hashMap.put("APPLY_FILTER",14119);
        hashMap.put("GOOGLE_SIGN_IN",14120);
        return hashMap;
    }

    public static void showSnackBar(Context activity, CoordinatorLayout coordinatorLayout, String text, int type)
    {
        if(coordinatorLayout==null|| text==null || activity==null)
        {
            return;
        }
        Snackbar snackBar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT);
        TextView txtMessage = (TextView) snackBar.getView().findViewById(R.id.snackbar_text);
        txtMessage.setTextColor(ContextCompat.getColor(activity,R.color.white));
        if (type==2)
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity,R.color.black));
        else if(type==1)
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity,R.color.app_snack_bar_true));
        else
        {
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity,R.color.mainColorPrimary));
        }
        txtMessage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/medium.ttf"));

        snackBar.show();
    }


    public static int dpSize(Context context, int sizeInDp)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }


    public static List<HomeOption> generateHomeOptions()
    {
        List<HomeOption> list = new ArrayList<>();
        list.add(new HomeOption(R.drawable.option_myway,"My Way","Trips & Places Nearby"));
        list.add(new HomeOption(R.drawable.parking,"Parking","Self parking & Valet"));
        list.add(new HomeOption(R.drawable.alert,"Alerts","Set Alerts & Violations"));

        return list;
    }

    public static List<Violation> generateRecentViolations()
    {
        List<Violation> list = new ArrayList<>();
        list.add(new Violation("Speed Alert","Vehicle has passed 96mph","Today 7:12 pm in Sans"));
        list.add(new Violation("Mileage Alert","Mileage is equal 78mph","Today 5:30 pm in San-Dieg"));
        list.add(new Violation("Geofence Alert","Entered in Hyderabad","Yesterday 7:00 pm in Hyderabad"));
        list.add(new Violation("Speed Alert","Vehicle has passed 106mph","Yesterday 6:12 pm in Vizag"));

        return list;
    }

    public static List<CarInfo> generateCarInfo()
    {
        List<CarInfo> list = new ArrayList<>();
        list.add(new CarInfo("Fuel","88%",R.drawable.ic_gas_station));
        list.add(new CarInfo("Mileage","14,039",R.drawable.ic_speedometer));
        list.add(new CarInfo("Next Repair","20 Days",R.drawable.ic_service));
        list.add(new CarInfo("Engine","OFF",R.drawable.ic_engine));
        return list;
    }

    public static List<Action> generateActions()
    {
        List<Action> list = new ArrayList<>();
        list.add(new Action("Schedule\nRepair",R.drawable.schedule_repair));
        list.add(new Action("Roadside\nSupport",R.drawable.roadside_support));
        list.add(new Action("Scan for\nError",R.drawable.scan_errors));

        return list;
    }

    public static List<Action> generateActionsForParking()
    {
        List<Action> list = new ArrayList<>();
        list.add(new Action("Start\nParking",R.drawable.other_parking));
        list.add(new Action("Valet\nParking",R.drawable.valet_parking));
        return list;
    }

    public static List<AlertRule> generateAlertRules()
    {
        List<AlertRule> list = new ArrayList<>();
        AlertRule alertRule = new AlertRule();
        alertRule.alertName = "SPEED MAX";
        alertRule.alertValue = "77 mph";
        alertRule.startTime = "9:00 am";
        alertRule.endTime = "7:00 pm";
        alertRule.isActive = true;
        alertRule.type = 1;


        AlertRule alertRule1 = new AlertRule();
        alertRule1.alertName = "ENTER GEOFENCE";
        alertRule1.alertValue = "Hyderabad";
        alertRule1.startTime = "6:00 am";
        alertRule1.endTime = "10:00 am";
        alertRule1.isActive = false;
        alertRule1.type = 3;


        AlertRule alertRule2 = new AlertRule();
        alertRule2.alertName = "LEAVE GEOFENCE";
        alertRule2.alertValue = "Vijayawada";
        alertRule2.startTime = "9:00 am";
        alertRule2.endTime = "7:00 pm";
        alertRule2.isActive = true;
        alertRule2.type = 4;

        AlertRule alertRule3 = new AlertRule();
        alertRule3.alertName = "SPEED MIN";
        alertRule3.alertValue = "77 mph";
        alertRule3.startTime = "9:00 am";
        alertRule3.endTime = "3:00 pm";
        alertRule3.isActive = true;
        alertRule3.type = 2;

        AlertRule alertRule4 = new AlertRule();
        alertRule4.alertName = "Mileage Alert";
        alertRule4.alertValue = "50 mph";
        alertRule4.startTime = "6:00 am";
        alertRule4.endTime = "1:00 pm";
        alertRule4.isActive = false;
        alertRule4.type = 1;

        list.add(alertRule);
        list.add(alertRule1);
        list.add(alertRule2);
        list.add(alertRule3);
        list.add(alertRule4);


        return list;
    }

    public static Bitmap addGradient(Bitmap originalBitmap,String start,String end) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, height, Color.parseColor(start), Color.parseColor(end), Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);
        return updatedBitmap;
    }

    public static List<TimeBasedViolations> generateTimeBasedViolations()
    {
        List<TimeBasedViolations> list = new ArrayList<>();

        List<Violation> violationList = new ArrayList<>();
        violationList.add(new Violation("Speed Alert","Vehicle has passed 96mph","Today 7:12 pm in Sans",1));
        violationList.add(new Violation("Mileage Alert","Mileage is equal 78mph","Today 5:30 pm in San-Dieg",2));

        List<Violation> violationList1 = new ArrayList<>();
        violationList1.add(new Violation("Geofence Alert","Entered in Hyderabad","Yesterday 7:00 pm in Hyderabad",3));
        violationList1.add(new Violation("Speed Alert","Vehicle has passed 106mph","Yesterday 6:12 pm in Vizag",4));


        list.add(new TimeBasedViolations("today",violationList));
        list.add(new TimeBasedViolations("yesterday",violationList1));
        return list;
    }

    public static List<NearBy> generateNearByOptions()
    {
        List<NearBy> list = new ArrayList<>();

        list.add(new NearBy("Gas",R.drawable.gas));
        list.add(new NearBy("Dine",R.drawable.dine));
        list.add(new NearBy("Coffee",R.drawable.hotel));
        list.add(new NearBy("ATM",R.drawable.atm));
        list.add(new NearBy("Parking",R.drawable.other_parking));

        return list;
    }

    public static List<Trip> generateRecentTrips()
    {
        List<Trip> list = new ArrayList<>();

        Trip trip = new Trip();
        trip.tripName = "Trip 1";
        trip.day = "Today";
        trip.startPlace = "Pragathi Nagar";
        trip.endPlace = "Jntu";
        trip.startTime = "3:00 am";
        trip.endTime = "6:00 am";


        Trip trip1 = new Trip();
        trip1.tripName = "Trip Hi-Tech";
        trip1.day = "Today";
        trip1.startPlace = "Hi-Tech";
        trip1.endPlace = "Jntu";
        trip1.startTime = "9:00 am";
        trip1.endTime = "10:00 pm";


        Trip trip2 = new Trip();
        trip2.tripName = "Trip 3";
        trip2.day = "Yesterday";
        trip2.startPlace = "SR Nagar";
        trip2.endPlace = "Nizampet";
        trip2.startTime = "6:00 am";
        trip2.endTime = "10:00 am";


        Trip trip3 = new Trip();
        trip3.tripName = "Trip 4";
        trip3.day = "5 days ago";
        trip3.startPlace = "Pragathi Nagar";
        trip3.endPlace = "Jntu";
        trip3.startTime = "12:40 pm";
        trip3.endTime = "3:00 pm";



        list.add(trip);
        list.add(trip1);
        list.add(trip2);
        list.add(trip3);



        return list;
    }


    public static double generateDistance(List<LatLng> list, int position)
    {
        double distance = 0;
        if(list==null&& list.size()>0)
        {
            return 0;
        }
        if(position == 0)
        {
            return 0;
        }
        for(int i=0;i<=position-1;i++)
        {
            distance += getDistanceFromLatLonInKm(list.get(i).latitude, list.get(i).longitude,list.get(i + 1).latitude, list.get(i+1).longitude);
        }
        return distance;
    }

    public static double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2)
    {
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    public static double deg2rad(double deg)
    {
        return deg * (Math.PI / 180);
    }




    public static List<TimeBasedTrips> generatePastTrips()
    {
        List<TimeBasedTrips> timeBasedTrips = new ArrayList<>();

        List<Trip> list = new ArrayList<>();
        List<Trip> list1 = new ArrayList<>();

        Trip trip = new Trip();
        trip.tripName = "Trip 1";
        trip.day = "Today";
        trip.startPlace = "Pragathi Nagar";
        trip.endPlace = "Jntu";
        trip.startTime = "3:00 am";
        trip.endTime = "6:00 am";
        trip.duration = "3 hrs";
        trip.distance = "15 miles";
        trip.location = "Hyderabad";


        Trip trip1 = new Trip();
        trip1.tripName = "Trip 2";
        trip1.day = "Today";
        trip1.startPlace = "Hi-Tech";
        trip1.endPlace = "Jntu";
        trip1.startTime = "9:00 am";
        trip1.endTime = "10:00 pm";
        trip1.duration = "1 hrs";
        trip1.location = "Hyderabad";
        trip1.distance = "39 miles";


        Trip trip2 = new Trip();
        trip2.tripName = "Trip 3";
        trip2.day = "Yesterday";
        trip2.startPlace = "SR Nagar";
        trip2.endPlace = "Nizampet";
        trip2.startTime = "6:00 am";
        trip2.endTime = "10:00 am";
        trip2.duration = "4 hrs";
        trip2.location = "Hyderabad";
        trip2.distance = "8 miles";


        Trip trip3 = new Trip();
        trip3.tripName = "Trip 4";
        trip3.day = "5 days ago";
        trip3.startPlace = "Pragathi Nagar";
        trip3.endPlace = "Jntu";
        trip3.startTime = "12:40 pm";
        trip3.endTime = "3:00 pm";
        trip3.duration = "140 min";
        trip3.location = "Hyderabad";
        trip3.distance = "18.3 miles";



        list.add(trip);
        list.add(trip1);


        list1.add(trip2);
        list1.add(trip3);

        timeBasedTrips.add(new TimeBasedTrips("Today",list));
        timeBasedTrips.add(new TimeBasedTrips("yesterday",list1));
        timeBasedTrips.add(new TimeBasedTrips("2 day ago",list));


        return timeBasedTrips;
    }

    public static List<TripPoint> generateSampleTripPoints()
    {
        Gson gson = new Gson();
        List<TripPoint> list = gson.fromJson(Constants.SAMPLE_TRIP_POINTS,new TypeToken<List<TripPoint>>(){}.getType());
        return list;
    }


}
