package com.maya.vgarages.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.maya.vgarages.R;
import com.maya.vgarages.application.VGaragesApplication;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.models.Notification;
import com.maya.vgarages.models.Option;
import com.maya.vgarages.models.Review;
import com.maya.vgarages.models.Service;

import org.json.JSONObject;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
        String fcmToken = "";
        if(getSharedPreferences().contains(Constants.USER_FCM_TOKEN))
        {
            fcmToken = getString(getSharedPreferences(), Constants.USER_FCM_TOKEN);
        }

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear().commit();

        if(fcmToken.length()>0)
            setString(getSharedPreferences(),Constants.USER_FCM_TOKEN,fcmToken);
    }


    public static SharedPreferences getSharedPreferences()
    {
        return VGaragesApplication.getInstance().sharedPreferences;
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

    public static String getCamelCase(String name)
    {
        if(name==null)
            return null;

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

    public static Typeface getTypeface(int value,Context context)
    {
        String path = "fonts/WorkSansSemiBold.ttf";
        switch (value)
        {
            case 3:
                path = "fonts/WorkSansBold.ttf";
                break;
            case 2:
                path = "fonts/WorkSansSemiBold.ttf";
                break;
            case 1:
                path = "fonts/WorkSansMedium.ttf";
                break;
            case 0:
                path = "fonts/WorkSansRegular.ttf";
                break;
        }
        return Typeface.createFromAsset(context.getAssets(), path);
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

    public static String makeDateToAgo(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("T");

            try {
                String[] time = arr[1].replace(".", "Z").split("Z");
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss");
                Date parsedTimeStamp = dateFormat.parse(arr[0] + " " + time[0]);

                Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
                return getTimeAgo(timestamp.getTime());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return arr[0];
            }
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

    public static void updateTabLayout(TabLayout tabLayout, Activity activity)
    {
        try {

            for (int i = 0; i < tabLayout.getTabCount(); i++)
            {
                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tablayout_textview, null);
                tv.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/WorkSansSemiBold.ttf"));
                tv.setScaleY(-1);
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
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.black));
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

    public static HashMap<String,Integer> generateRequestCodes()
    {
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("GOOGLE_SIGN_IN",18101);
        hashMap.put("FACEBOOK_SIGN_IN",18102);
        hashMap.put("REQUEST_LOCATION_PERMISSION",18103);
        hashMap.put("ADD_VEHICLE",18104);
        hashMap.put("CHANGE_VEHICLE",18105);
        return hashMap;
    }


    public static List<Service> generateServices()
    {
        List<Service> list = new ArrayList<>();
        list.add(new Service("Oil Change",R.drawable.oil_change,true));
        list.add(new Service("Air Conditioning",R.drawable.air_conditioning,false));
        list.add(new Service("Body Shop",R.drawable.body_shop,false));
        return list;
    }


    public static List<Garage> generateGaragesList()
    {
        List<Garage> list = new ArrayList<>();

        Garage g1 = new Garage();
        g1.DealerName = "Asd Garage";
        g1.ImageUrl = "https://media.gettyimages.com/photos/car-mechanic-using-tools-picture-id924923446";
        g1.Distance = 50;
        g1.CustomerRating = 5.3;
        g1.Address1 = "Madhapur, Hyderabad";
        g1.DealerRating = 3;
        g1.IsClosed = false;

        Garage g2 = new Garage();
        g2.DealerName = "DVV Garage";
        g2.ImageUrl = "https://media.gettyimages.com/photos/cars-on-lifts-in-auto-repair-shop-picture-id188075212";
        g2.Distance = 200;
        g2.CustomerRating = 9.3;
        g2.Address1 = "Madhapur, Hyderabad";
        g2.DealerRating = 2;
        g2.IsClosed = false;


        Garage g3 = new Garage();
        g3.DealerName = "Maya Garage";
        g3.ImageUrl = "https://media.gettyimages.com/photos/car-on-lift-in-auto-repair-shop-picture-id188075213";
        g3.Distance = 500;
        g3.CustomerRating = 7.5;
        g3.Address1 = "Hi-Tech, Hyderabad";
        g3.DealerRating = 4;
        g3.IsClosed = true;


        Garage g4 = new Garage();
        g4.DealerName = "Murali Garage";
        g4.ImageUrl = "https://media.gettyimages.com/photos/tools-on-cart-in-auto-repair-shop-picture-id188075211";
        g4.Distance = 900;
        g4.CustomerRating = 8.3;
        g4.Address1 = "Kondapur, Hyderabad";
        g4.DealerRating = 2;
        g4.IsClosed = false;


        Garage g5 = new Garage();
        g5.DealerName = "Akbar Garage";
        g5.ImageUrl = "https://media.gettyimages.com/photos/car-repair-shop-in-japan-picture-id669872522";
        g5.Distance = 1500;
        g5.CustomerRating = 5.3;
        g5.Address1 = "Gachibowli, Hyderabad";
        g5.DealerRating = 3;
        g5.IsClosed = true;


        Garage g6 = new Garage();
        g6.DealerName = "Sai Teja Garage";
        g6.ImageUrl = "https://media.gettyimages.com/photos/auto-repair-shop-car-garage-picture-id176712668";
        g6.Distance = 1550;
        g6.CustomerRating = 9.2;
        g6.Address1 = "Gachibowli, Hyderabad";
        g6.DealerRating = 0;
        g6.IsClosed = false;

        list.add(g1);
        list.add(g2);
        list.add(g3);
        list.add(g4);
        list.add(g5);
        list.add(g6);


        return list;

    }

    public static List<Garage> generateRemainderGaragesList()
    {
        List<Garage> list = new ArrayList<>();

        Garage g1 = new Garage();
        g1.DealerName = "Asd Garage";
        g1.ImageUrl = "https://media.gettyimages.com/photos/car-mechanic-using-tools-picture-id924923446";
        g1.Distance = 50;
        g1.CustomerRating = 5.3;
        g1.Address1 = "Madhapur, Hyderabad";
        g1.DealerRating = 3;
        g1.IsClosed = true;

        Garage g2 = new Garage();
        g2.DealerName = "DVV Garage";
        g2.ImageUrl = "https://media.gettyimages.com/photos/cars-on-lifts-in-auto-repair-shop-picture-id188075212";
        g2.Distance = 200;
        g2.CustomerRating = 9.3;
        g2.Address1 = "Madhapur, Hyderabad";
        g2.DealerRating = 2;
        g2.IsClosed = false;


        Garage g3 = new Garage();
        g3.DealerName = "Maya Garage";
        g3.ImageUrl = "https://media.gettyimages.com/photos/car-on-lift-in-auto-repair-shop-picture-id188075213";
        g3.Distance = 500;
        g3.CustomerRating = 7.5;
        g3.Address1 = "Hi-Tech, Hyderabad";
        g3.DealerRating = 4;
        g3.IsClosed = true;


        Garage g4 = new Garage();
        g4.DealerName = "Murali Garage";
        g4.ImageUrl = "https://media.gettyimages.com/photos/tools-on-cart-in-auto-repair-shop-picture-id188075211";
        g4.Distance = 900;
        g4.CustomerRating = 8.3;
        g4.Address1 = "Kondapur, Hyderabad";
        g4.DealerRating = 2;
        g4.IsClosed = false;


        Garage g5 = new Garage();
        g5.DealerName = "Akbar Garage";
        g5.ImageUrl = "https://media.gettyimages.com/photos/car-repair-shop-in-japan-picture-id669872522";
        g5.Distance = 1500;
        g5.CustomerRating = 5.3;
        g5.Address1 = "Gachibowli, Hyderabad";
        g5.DealerRating = 3;
        g5.IsClosed = true;


        Garage g6 = new Garage();
        g6.DealerName = "Sai Teja Garage";
        g6.ImageUrl = "https://media.gettyimages.com/photos/auto-repair-shop-car-garage-picture-id176712668";
        g6.Distance = 1550;
        g6.CustomerRating = 9.2;
        g6.Address1 = "Gachibowli, Hyderabad";
        g6.DealerRating = 0;
        g6.IsClosed = true;

        list.add(g1);
        list.add(g2);
        list.add(g3);
        list.add(g4);
        list.add(g5);
        list.add(g6);


        return list;

    }

    public static List<Garage> generateRecommendedGaragesList()
    {
        List<Garage> list = new ArrayList<>();

        Garage g1 = new Garage();
        g1.DealerName = "Asd Garage";
        g1.ImageUrl = "https://media.gettyimages.com/photos/car-mechanic-using-tools-picture-id924923446";
        g1.Distance = 50;
        g1.CustomerRating = 5.3;
        g1.Address1 = "Madhapur, Hyderabad";
        g1.DealerRating = 3;
        g1.IsClosed = true;

        Garage g2 = new Garage();
        g2.DealerName = "DVV Garage";
        g2.ImageUrl = "https://media.gettyimages.com/photos/cars-on-lifts-in-auto-repair-shop-picture-id188075212";
        g2.Distance = 200;
        g2.CustomerRating = 9.3;
        g2.Address1 = "Madhapur, Hyderabad";
        g2.DealerRating = 2;
        g2.IsClosed = false;


        Garage g3 = new Garage();
        g3.DealerName = "Maya Garage";
        g3.ImageUrl = "https://media.gettyimages.com/photos/car-on-lift-in-auto-repair-shop-picture-id188075213";
        g3.Distance = 500;
        g3.CustomerRating = 7.5;
        g3.Address1 = "Hi-Tech, Hyderabad";
        g3.DealerRating = 4;
        g3.IsClosed = true;


        Garage g4 = new Garage();
        g4.DealerName = "Murali Garage";
        g4.ImageUrl = "https://media.gettyimages.com/photos/tools-on-cart-in-auto-repair-shop-picture-id188075211";
        g4.Distance = 900;
        g4.CustomerRating = 8.3;
        g4.Address1 = "Kondapur, Hyderabad";
        g4.DealerRating = 2;
        g4.IsClosed = false;


        Garage g5 = new Garage();
        g5.DealerName = "Akbar Garage";
        g5.ImageUrl = "https://media.gettyimages.com/photos/car-repair-shop-in-japan-picture-id669872522";
        g5.Distance = 1500;
        g5.CustomerRating = 5.3;
        g5.Address1 = "Gachibowli, Hyderabad";
        g5.DealerRating = 3;
        g5.IsClosed = true;


        Garage g6 = new Garage();
        g6.DealerName = "Sai Teja Garage";
        g6.ImageUrl = "https://media.gettyimages.com/photos/auto-repair-shop-car-garage-picture-id176712668";
        g6.Distance = 1550;
        g6.CustomerRating = 9.2;
        g6.Address1 = "Gachibowli, Hyderabad";
        g6.DealerRating = 0;
        g6.IsClosed = true;

        list.add(g1);
        list.add(g2);
        list.add(g3);
        list.add(g4);
        list.add(g5);
        list.add(g6);


        return list;

    }

    public static List<Notification> generateNotificationsList()
    {
        List<Notification> list = new ArrayList<>();
        Notification n1 = new Notification();
        n1.Title = "Discount 10%";
        n1.Content = "In Asd Garage for car wash";
        n1.TimeAgo = "just now";
        n1.Image = "https://media.gettyimages.com/photos/red-and-blue-car-racing-picture-id168959214";

        Notification n2 = new Notification();
        n2.Title = "Service Appointment";
        n2.Content = "In Qx Garage, pick on 11/04/2018";
        n2.TimeAgo = "5 hours ago";
        n2.Image = "https://media.gettyimages.com/photos/car-on-lift-in-auto-repair-shop-picture-id188075213";


        Notification n4 = new Notification();
        n4.Title = "Happy Birthday";
        n4.Content = "Wish you many more happy returns of the day";
        n4.TimeAgo = "3 days ago";
        n4.Image = "http://assets.stickpng.com/thumbs/580b585b2edbce24c47b28d3.png";

        Notification n3 = new Notification();
        n3.Title = "Service Appointment";
        n3.Content = "In Jagan Garage, pick on 10/04/2018";
        n3.TimeAgo = "7 days ago";
        n3.Image = "https://media.gettyimages.com/photos/red-and-blue-car-racing-picture-id168959214";

        Notification n5 = new Notification();
        n5.Title = "Discount 15%";
        n5.Content = "In Qx Garage for Noisy brakes";
        n5.TimeAgo = "16 days ago";
        n5.Image = "http://maxxerp.com/wp-content/uploads/2015/04/Offer-Schemes.png";

        Notification n6 = new Notification();
        n6.Title = "Discount 20%";
        n6.Content = "In Maya Garage for car wash";
        n6.TimeAgo = "20 days ago";
        n6.Image = "https://media.gettyimages.com/photos/car-service-procedure-picture-id522394158";

        list.add(n1);
        list.add(n2);
        list.add(n4);
        list.add(n3);
        list.add(n5);
        list.add(n6);

        return list;
    }


    public static List<String> generateViewPageTitles(int type)
    {
        List<String> stringList = new ArrayList<>();
        switch (type)
        {
            case 1: // garage overview
                stringList = Arrays.asList("Profile","Services","Reviews");
                break;
            case 2:
                break;

        }
        return stringList;
    }

    public static  List<String> generateImageUrls()
    {
        List<String> stringList = Arrays.asList(
                "https://media.gettyimages.com/photos/car-mechanic-using-tools-picture-id924923446",
                "https://media.gettyimages.com/photos/cars-on-lifts-in-auto-repair-shop-picture-id188075212",
                "https://media.gettyimages.com/photos/car-on-lift-in-auto-repair-shop-picture-id188075213",
                "https://media.gettyimages.com/photos/tools-on-cart-in-auto-repair-shop-picture-id188075211",
                "https://media.gettyimages.com/photos/car-repair-shop-in-japan-picture-id669872522",
                "https://i.pinimg.com/originals/3e/3e/14/3e3e14d932098c3c3140c419daaa61c6.jpg",
                "https://media.gettyimages.com/photos/auto-repair-shop-car-garage-picture-id176712668",
                "https://media.gettyimages.com/photos/car-service-procedure-picture-id522394158",
                "https://media.gettyimages.com/photos/red-and-blue-car-racing-picture-id168959214"
                );
        return stringList;
    }


    public static List<Option> generateOptions()
    {
        List<Option> list = new ArrayList<>();
        list.add(new Option("10AM - 7PM",R.drawable.clock));
        list.add(new Option("Direct",R.drawable.locate));
        list.add(new Option("Call Now",R.drawable.phone));
        list.add(new Option("Bookmark",R.drawable.pin));
        return list;
    }

    public static List<Review> generateReviews()
    {
        List<Review> list = new ArrayList<>();

        Review r0 = new Review();
        r0.FullName = "Kate Margarita";
        r0.Review = "You've done it again! Thank you for helping a damsel in distress, you repaired one tyre and replaced another even though I didn't have an appointment, thank you.";
        r0.ImageUrl = "https://media.gettyimages.com/photos/young-woman-shopping-online-picture-id613241502";
        r0.Rating = 8;
        r0.CreatedDt = "23 hours ago";

        Review r1 = new Review();
        r1.FullName = "Enay Bill";
        r1.Review = "Even though it was a really busy period for them they were able to fit in my repair when my car broke down. The team were very friendly and helpful and I'll definitely use Squire Furneaux Guildford again";
        r1.ImageUrl = "https://d2e70e9yced57e.cloudfront.net/wallethub/posts/29912/eric-klinenberg.jpg";
        r1.Rating = 9.3;
        r1.CreatedDt = "1 day ago";


        Review r2 = new Review();
        r2.FullName = "Henry David";
        r2.Review = "Had my MOT carried out here and have booked in for additional repairs to be carried out. Would definately recommend to friends and family";
        r2.ImageUrl = "https://www.cs.ox.ac.uk/files/8640//profile.jpg";
        r2.Rating = 7.8;
        r2.CreatedDt = "3 days ago";

        Review r3= new Review();
        r3.FullName = "Christin Curie";
        r3.Review = "Due to a misunderstanding the 2 new tyres were fitted on the rear by mistake. However this was quickly rectified with the incorrectly removed tyres refitted and new tyres moved to the front. Have used Chemix for exhausts and tyres for years, always good value";
        r3.ImageUrl = "https://www.radioproject.org/wp-Review/uploads/2011/02/0711.jpg";
        r3.Rating = 9.5;
        r3.CreatedDt = "18 days ago";

        Review r4 = new Review();
        r4.FullName = "John Rayie";
        r4.Review = "First Class as usual";
        r4.ImageUrl = "https://edb-cdn-prod-tqgiyve.stackpathdns.com/players/icons/9e71815e-cabb-11e7-be93-0e6c723feec8.png";
        r4.Rating = 10;
        r4.CreatedDt = "1 month ago";

        Review r5 = new Review();
        r5.FullName = "Gates Johnson";
        r5.Review = "Avoid at any cost!!!";
        r5.ImageUrl = "http://www.unitytubes.com/wp-Review/uploads/2017/07/DAVIDO-4-150x150.png";
        r5.Rating = 4.5;
        r5.CreatedDt = "2 months ago";

        Review r6 = new Review();
        r6.FullName = "Catherine Ton";
        r6.Review = "Brilliant customer service, knowledgeable and friendly staff.";
        r6.ImageUrl = "https://media.gettyimages.com/photos/photo-of-a-woman-using-smart-phone-picture-id647677346";
        r6.Rating = 8;
        r6.CreatedDt = "2 months ago";

        list.add(r0);
        list.add(r1);
        list.add(r2);
        list.add(r3);
        list.add(r4);
        list.add(r5);
        list.add(r6);



        return list;
    }


    public static List<GarageService> generateGarageServices()
    {
        List<GarageService> list = new ArrayList<>();

        GarageService gs1 = new GarageService();
        gs1.OpCodeName = "Oil Change";
        gs1.Price = 1500;
        gs1.OpCodeContent = "Regularly changing your car's engine oil and filter are one of the most important things you can do to keep your car running well.";
        gs1.ImageUrl = "https://media.gettyimages.com/photos/engine-oil-picture-id184832880";
        gs1.Tag = true;
        gs1.TagContent = "Exclusive";
        gs1.TagType = 0;



        GarageService gs2 = new GarageService();
        gs2.OpCodeName = "Replace AC Air Filter";
        gs2.Price = 3000;
        gs2.OpCodeContent = "To change or clean the air filter in the central air system, first ensure that the system is off. Locate the filter inside of the furnace (or air handler for electric systems).";
        gs2.ImageUrl = "https://media.gettyimages.com/photos/automotive-air-filter-replacement-picture-id184348820";
        gs2.Tag = false;


        GarageService gs3 = new GarageService();
        gs3.OpCodeName = "Car wash \nInterior / Exterior ";
        gs3.Price = 600;
        gs3.OpCodeContent = "With a damp cloth we dust the dash, steering wheel, and center console";
        gs3.ImageUrl = "https://media.gettyimages.com/photos/convertible-going-through-car-wash-picture-id154945530";
        gs3.Tag = true;
        gs3.TagContent = "Promotive";
        gs3.TagType = 1;




        GarageService gs4 = new GarageService();
        gs4.OpCodeName = "Engine Servicing";
        gs4.Price = 1500;
        gs4.OpCodeContent = "A complete service history usually adds to the resale value of a vehicle";
        gs4.ImageUrl = "https://media.gettyimages.com/photos/car-engine-repair-picture-id641975348";
        gs4.Tag = false;




        GarageService gs5 = new GarageService();
        gs5.OpCodeName = "Replace Side Mirrors";
        gs5.OpCodeContent = "Best quality with warranty";
        gs5.Price = 1100;
        gs5.ImageUrl = "https://media.gettyimages.com/photos/side-view-of-a-luxus-car-picture-id157583605";
        gs5.Tag = false;




        GarageService gs6 = new GarageService();
        gs6.OpCodeName = "Repairing Tires";
        gs6.Price = 700;
        gs6.OpCodeContent = "Flat tire repair, tire patches and more";
        gs6.ImageUrl = "https://media.gettyimages.com/photos/tire-repairer-checking-the-tire-integrity-picture-id621978130";
        gs6.Tag = true;
        gs6.TagContent = "10% Discount";
        gs6.TagType = 3;




        GarageService gs7 = new GarageService();
        gs7.OpCodeName = "Door Service";
        gs7.OpCodeContent = "Maintaining your car doors and door parts is essential to their effortless function";
        gs7.Price = 500;
        gs7.ImageUrl = "https://media.gettyimages.com/photos/auto-mechanic-picture-id598293869";
        gs7.Tag = false;



        GarageService gs8 = new GarageService();
        gs8.OpCodeName = "Car Painting";
        gs8.OpCodeContent = "All types of colors available";
        gs8.Price = 5000;
        gs8.ImageUrl = "https://media.gettyimages.com/photos/painting-table-on-white-color-picture-id510976011";
        gs8.Tag = true;
        gs8.TagContent = "25% Discount";
        gs8.TagType = 2;


        list.add(gs1);
        list.add(gs2);
        list.add(gs3);
        list.add(gs4);
        list.add(gs5);
        list.add(gs6);
        list.add(gs7);
        list.add(gs8);



        return list;
    }

    public static void openPhoneDialPad(Context context,String phone)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }

    public static void openGoogleNavigate(Context context, LatLng latLng)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," + latLng.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }


    public static boolean isGPSEnable(Activity activity)
    {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String getPhoneUniqueId(Context context)
    {
        return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }





}
