package com.maya.wadmin.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.applications.WAdminApplication;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.AppOverview;
import com.maya.wadmin.models.CheckList;
import com.maya.wadmin.models.Inspection;
import com.maya.wadmin.models.LotCheckList;
import com.maya.wadmin.models.PDIPreparation;
import com.maya.wadmin.models.TopBarPanel;
import com.maya.wadmin.models.UserRole;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.VehicleCount;
import com.maya.wadmin.models.VehicleStatus;

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
        return WAdminApplication.getInstance().sharedPreferences;
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
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

    public static int getPortalType()
    {
        int value = 0;
        if(Utility.getSharedPreferences().contains(Constants.PORTAL_TYPE))
        {
            value = Utility.getInt(getSharedPreferences(),Constants.PORTAL_TYPE);
        }
        else
        {
            Utility.setInt(getSharedPreferences(),Constants.PORTAL_TYPE,0);
        }
        return value;
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

    public static List<UserRole> getUserRole(int value)
    {
        List<UserRole> list = new ArrayList<UserRole>();
        Gson gson =new Gson();
        Type type = new TypeToken<AppOverview>(){}.getType();
        if(!getSharedPreferences().contains(Constants.APP_OVERVIEW))
        {
            return list;
        }
        AppOverview appOverview = gson.fromJson(getString(getSharedPreferences(),Constants.APP_OVERVIEW),type); //getString(getSharedPreferences(),Constants.APP_OVERVIEW)  Constants.SAMPLE_APP_OVERVIEW
        if(appOverview == null)
        {
            return list;
        }
        else
        {
            if(appOverview.vehicleTypeCount==null || appOverview.vehicleTypeCount.size()==0)
            {
                return list;
            }
        }

        List<VehicleCount> countList;
        switch (value)
        {
            case 0:
                countList = new ArrayList<>();
                countList.add(new VehicleCount(appOverview.VehicleDeliveryCount,"Vehicle's in the delivery"));
                list.add(new UserRole("Vehicle Delivery",R.drawable.ic_vehicle_delivery,countList));
                countList = new ArrayList<>();
                countList.add(new VehicleCount(appOverview.ZoneCount,"Zones in the present"));
                list.add(new UserRole("Manage Zones",R.drawable.ic_manage_zones,countList));
                countList = new ArrayList<>();
                countList.add(new VehicleCount(appOverview.AlertsCount,"Alerts in the panel"));
                list.add(new UserRole("Rules & Alerts",R.drawable.ic_rules_alerts,countList));
                countList = new ArrayList<>();
                countList.add(new VehicleCount(appOverview.FindVehicleCount,"Vehicles in the current"));
                list.add(new UserRole("Find Vehicle",R.drawable.ic_find_vehicle,countList));
                break;
            case 1:
                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(0).VehicleCount,"Vehicle's in delivery received"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(4).VehicleCount,"Vehicle's in Preparing for lot"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(2).VehicleCount,"Vehicle's in marked for pdi"));
                list.add(new UserRole("Fleet Delivery",R.drawable.fleet_mangement,countList));

                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(2).VehicleCount,"Vehicle's in marked for pdi"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(3).VehicleCount,"Vehicle's in pdi incomplete"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(1).VehicleCount,"Vehicle's in pdi completed"));
                list.add(new UserRole("PDI",R.drawable.ic_p_pdi,countList));

                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(5).VehicleCount,"Vehicle's in lot"));
                list.add(new UserRole("Test Drive",R.drawable.ic_test_drive,countList));


                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(0).VehicleCount,"Vehicle's in delivery received"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(4).VehicleCount,"Vehicle's in Preparing for lot"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(2).VehicleCount,"Vehicle's in marked for pdi"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(3).VehicleCount,"Vehicle's in pdi incomplete"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(1).VehicleCount,"Vehicle's in inventory"));
                    countList.add(new VehicleCount(appOverview.vehicleTypeCount.get(5).VehicleCount,"Vehicle's in test drive"));
                list.add(new UserRole("Lot Management",R.drawable.ic_lot_management,countList));


                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.ZoneCount,"Zones in the present"));
                list.add(new UserRole("Manage Zones",R.drawable.ic_manage_zones,countList));

                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.AlertsCount,"Alerts in the panel"));
                list.add(new UserRole("Rules & Alerts",R.drawable.ic_rules_alerts,countList));

                    countList = new ArrayList<>();
                    countList.add(new VehicleCount(appOverview.FindVehicleCount,"Vehicles in the current"));
                list.add(new UserRole("Find Vehicle",R.drawable.ic_find_vehicle,countList));
                //list.add(new UserRole("Create Zone",R.drawable.ic_rules_alerts));
                break;

        }

        return list;
    }


    public static List<TopBarPanel> getTopBarPanelElements(int value)
    {
        List<TopBarPanel> list = new ArrayList<>();
        switch (value)
        {

            case 0:
                // delivery vehicle
                list.add(new TopBarPanel("Arrival Status","Show all delivery trucks",true));
                list.add(new TopBarPanel("All Vehicles","Show all delivery vehicles",false));
                list.add(new TopBarPanel("Archived","Show all archived vehicles",false));
                break;
            case 1:
                // manage zones
                list.add(new TopBarPanel("Custom Zones","Show all custom zones",true));
                list.add(new TopBarPanel("Recents Zones","Show all recents zones",false));
                list.add(new TopBarPanel("Regions Zones","Show all regions zones",false));
                break;
            case 2:
                // pdi vehicles
                list.add(new TopBarPanel("PDI Status","Show all pdi vehicles",true));
                list.add(new TopBarPanel("Assigned","Show all vehicles by salesperson",false));
                list.add(new TopBarPanel("Archived","Show all archived vehicles",false));

                break;
            case 3:
                // lot vehicles
                list.add(new TopBarPanel("Vehicle Status","Show all vehicles status",true));
                list.add(new TopBarPanel("Assigned","Show all vehicles by salesperson",false));
                list.add(new TopBarPanel("Alert Violations","Show all violation vehicles",false));
                list.add(new TopBarPanel("Archived","Show all archived vehicles",false));

                break;

            case 4:
                // test drive vehicles
                list.add(new TopBarPanel("All Test Drives","Show all test drive vehicles",true));
                list.add(new TopBarPanel("Assigned","Show all vehicles by salesperson",false));
                list.add(new TopBarPanel("Archived","Show all archived vehicles",false));

                break;
            case 5:
                //alert vehicles types
                list.add(new TopBarPanel("By Specific Vehicles","It contains all vehicles",true));
                list.add(new TopBarPanel("By Vehicle Status","Show all vehicles by status types",false));
                break;
            case 6:
                //alert vehicles types
                list.add(new TopBarPanel("Speed Alert","It contains all vehicles",true));
                list.add(new TopBarPanel("Geofence Alert","Show all vehicles by status types",false));
                list.add(new TopBarPanel("Mileage Alert","Show all vehicles by status types",false));
                list.add(new TopBarPanel("Theft Alert","Show all vehicles by status types",false));
                list.add(new TopBarPanel("Delivery Alert","Show all vehicles by status types",false));
                list.add(new TopBarPanel("DTC Alert","Show all vehicles by status types",false));
                list.add(new TopBarPanel("Billing Alert","Show all vehicles",false));
                list.add(new TopBarPanel("Vehicle Alert","Show all vehicles by status types",false));
                list.add(new TopBarPanel("Customer Alert","Show all vehicles by status types",false));
                break;
            case 7:
                // fleet vehicles
                list.add(new TopBarPanel("Vehicle Status","Show all vehicles status",true));
                list.add(new TopBarPanel("Assigned","Show all vehicles by salesperson",false));
                list.add(new TopBarPanel("Archived","Show all archived vehicles",false));

                break;
        }

        return list;
    }

    public static List<CheckList> generateChecklistForPreparationForm(LotCheckList lotCheckList)
    {
        List<CheckList> list = new ArrayList<>();
        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Check for Bill of Lading is Completed";
        ch1.value = lotCheckList.IsBillLading;


        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Check for Count of Keys as 2";
        ch2.value = lotCheckList.IsKeysCount;


        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Check for Owner’s Manual Packet in Vehicle";
        ch3.value = lotCheckList.IsOwnersManualPacket;

        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Check for match of VIN on car with Shipping Invoice";
        ch4.value = lotCheckList.IsVinMatch;

        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Check Unit for Shipping Damage";
        ch5.value = lotCheckList.IsShippingDamage;

        CheckList ch6 = new CheckList();
        ch6.CheckListName = "Check Mirrors";
        ch6.value = lotCheckList.IsMirrors;

        CheckList ch7 = new CheckList();
        ch7.CheckListName = "Check Removal of all shipping wrap, stickers, covers, etc";
        ch7.value = lotCheckList.IsRemovalWrap;

        CheckList ch8 = new CheckList();
        ch8.CheckListName = "Update Shipping Invoice with [SKU-bar code number] next to VIN";
        ch8.value = lotCheckList.IsUpdateShippingInvoice;

        CheckList ch9 = new CheckList();
        ch9.CheckListName = "Create Vehicle Packets to hold Vehicle documents like Owner’s manual and Keys";
        ch9.value = lotCheckList.IsVehiclePackets;

        CheckList ch10 = new CheckList();
        ch10.CheckListName = "Print bar code labels for vehicle, keys, and vehicle packets – all three must have same bar code";
        ch10.value = lotCheckList.IsPrintBarCode;

        CheckList ch11 = new CheckList();
        ch11.CheckListName = "Hand Shipping Invoices to Service Department";
        ch11.value = lotCheckList.IsHandShippingInvoices;

        CheckList ch12 = new CheckList();
        ch12.CheckListName = "Install Bar code Stickers to Vehicles next to VIN number";
        ch12.value = lotCheckList.IsInstallBarCodeStickers;


        CheckList ch13 = new CheckList();
        ch13.CheckListName = "Separate keys and affix matching bar code to each";
        ch13.value = lotCheckList.IsSeperateKeys;

        CheckList ch14 = new CheckList();
        ch14.CheckListName = "File packets in storage cabinets in numerical order";
        ch14.value = lotCheckList.IsFilePackets;


        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch6);
        list.add(ch7);
        list.add(ch8);
        list.add(ch9);
        list.add(ch10);
        list.add(ch11);
        list.add(ch12);
        list.add(ch13);
        list.add(ch14);


        return list;
    }


    public static List<Inspection> getListInspectionWithChecklist(PDIPreparation pdiPreparation)
    {
        List<Inspection> list = new ArrayList<>();
        String contentString[] = pdiPreparation.InspectionValues.split(",");
        int content[] = new int[contentString.length];


        if(pdiPreparation.InspectionValues.length()==0)
        {
            return list;
        }
        for(int i =0 ;i < contentString.length ;i++)
        {
            content[i] = Integer.parseInt(contentString[i]);
        }


        Inspection i1 = new Inspection();
        i1.name = "Vehicle Readiness";
        i1.checkLists = generateVehicleReadness(content);

        Inspection i2 = new Inspection();
        i2.name = "Under Hood";
        i2.checkLists = generateUnderhood(content);

        Inspection i3 = new Inspection();
        i3.name = "Under Vehicle";
        i3.checkLists = generateUnderVehicle(content);

        Inspection i4 = new Inspection();
        i4.name = "Road Test";
        i4.checkLists = generateRoadTest(content);

        Inspection i5 = new Inspection();
        i5.name = "Interior";
        i5.checkLists = generateInterior(content);

        Inspection i6 = new Inspection();
        i6.name = "Exterior";
        i6.checkLists = generateExterior(content);

        Inspection i7 = new Inspection();
        i7.name = "Vehicle Storage";
        i7.checkLists = generateVehicleStorage(content);

        Inspection i8 = new Inspection();
        i8.name = "Final Details and Inspection";
        i8.checkLists = generateFinalDetailInspection(content);

        list.add(i1);
        list.add(i2);
        list.add(i3);
        list.add(i4);
        list.add(i5);
        list.add(i6);
        list.add(i7);
        list.add(i8);



        return list;
    }



    public static List<CheckList> generateVehicleReadness(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Keep All Protective Transit Film and Wheel Covers/Films on Vehicle Until Sold or Up To 180 Days";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Place Vehicle into Customer Mode";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Adjust Tire Pressure Including Spare to Door Placard";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Verify vehicle is built as invoiced";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Install all loose shipped items";
        ch5.status = false;
        CheckList ch6 = new CheckList();
        ch6.CheckListName = "Install Front License Plate Bracket (if required)";
        ch6.status = false;
        CheckList ch7 = new CheckList();
        ch7.CheckListName = "Perform all Incompleted Recalls and RRTs";
        ch7.status = false;


        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch6);
        list.add(ch7);

        for (int i=0;i<=6;i++)
        {
            list.get(i).value = values[i];
        }

        //list.Where(c => c.value == 1).ToList().ForEach(cc => cc.value = -1);



        return list;
    }
    //7

    public static List<CheckList> generateUnderhood(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Hold Latch and Safety Catch - Adjust as  Needed";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Battery State of Charge";
        //ch2.fields = 1;
        //ch2.fieldNames = new string[] { "Volts" };
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Loose Attachments, Routing, Clearance, Damage (Brake Lines, Fuel Lines, Exhaust, Wiring Harnesses)";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "All Fuild Levels";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "No Fuild Leaks Present";
        ch5.status = false;

        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);

        int j = 0;
        for (int i = 7; i <= 11; i++)
        {
            list.get(j).value = values[i];
            j++;
        }

        // list.Where(c => c.value == 0).ToList().ForEach(cc => cc.value = -1);
        return list;
    }
    //5


    public static List<CheckList> generateUnderVehicle(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Remove the underbody shield (if equipped)";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "No Fuild Leaks Present";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "All Fuild Levels";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "DO NOT Check 8-Speed Transmission Fluid Level (Do not top off axle fluid)";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Loose Attachments, Routing, Clearance, Damage (Brake Lines, Fuel Lines, Exhaust, Wiring Harnesses)";
        ch5.status = false;
        CheckList ch6 = new CheckList();
        ch6.CheckListName = "Install the underbody shield (if equipped)";
        ch6.status = false;
        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch6);

        int j = 0;
        for (int i = 12; i <= 17; i++)
        {
            list.get(j).value = values[i];
            j++;
        }


        return list;
    }
    //6


    public static List<CheckList> generateRoadTest(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Remote Test - All Keys";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Passive Entry/Keyless Go";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Remote Proximity/Keyless Entry/Passive Entry/Keyless Go";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Remote Keyless Entry - All Keys";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Engine Starts with All keys";
        ch5.status = false;
        CheckList ch6 = new CheckList();
        ch6.CheckListName = "All Warning Lights and Gauges / No DTCs";
        ch6.status = false;
        CheckList ch7 = new CheckList();
        ch7.CheckListName = "Engine Starts Only in Park & Neutral";
        ch7.status = false;
        CheckList ch8 = new CheckList();
        ch8.CheckListName = "Brake Transmission Shift Interlock";
        ch8.status = false;
        CheckList ch9 = new CheckList();
        ch9.CheckListName = "Engine Performance - Cold";
        ch9.status = false;
        CheckList ch10 = new CheckList();
        ch10.CheckListName = "Perform road test (8 - 10 mile, 10-15 km guideline) on a variety of road surfaces. Note Odometer Reading";
        ch10.status = false;
        //ch10.fields = 2;
        //ch10.fieldNames = new string[] { "Before", "After" };

        CheckList ch11 = new CheckList();
        ch11.CheckListName = "Service and Parking Brakes";
        ch11.status = false;
        CheckList ch12 = new CheckList();
        ch12.CheckListName = "Automile Transmission Shifting";
        ch12.status = false;
        CheckList ch13 = new CheckList();
        ch13.CheckListName = "Stop -  Start System";
        ch13.status = false;
        CheckList ch14 = new CheckList();
        ch14.CheckListName = "Steering and Handling";
        ch14.status = false;
        CheckList ch15 = new CheckList();
        ch15.CheckListName = "Noice, Vibration, Squeaks, and / or Rattles";
        ch15.status = false;
        CheckList ch16 = new CheckList();
        ch16.CheckListName = "Heater / Defrost - Infront";
        ch16.status = false;
        CheckList ch17 = new CheckList();
        ch17.CheckListName = "Air Conditioning";
        ch17.status = false;
        CheckList ch18 = new CheckList();
        ch18.CheckListName = "Rear Heater and Air Conditioning";
        ch18.status = false;
        CheckList ch19 = new CheckList();
        ch19.CheckListName = "Blindspot Monitoring System";
        ch19.status = false;
        CheckList ch20 = new CheckList();
        ch20.CheckListName = "Cruise Control";
        ch20.status = false;
        CheckList ch21 = new CheckList();
        ch21.CheckListName = "Set Compass Variance / Calculation";
        ch21.status = false;
//        CheckList ch22 = new CheckList();
//        ch22.CheckListName = "Load Leveling Air Suspension";
//        ch22.status = false;
        CheckList ch23 = new CheckList();
        ch23.CheckListName = "Tire Pressure Monitoring System";
        ch23.status = false;
        CheckList ch24 = new CheckList();
        ch24.CheckListName = "Engine Performance - Warm";
        ch24.status = false;
        CheckList ch25 = new CheckList();
        ch25.CheckListName = "ParkSense";
        ch25.status = false;
        CheckList ch26 = new CheckList();
        ch26.CheckListName = "ParkView";
        ch26.status = false;

        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch6);
        list.add(ch7);
        list.add(ch8);
        list.add(ch9);
        list.add(ch10);
        list.add(ch11);
        list.add(ch12);
        list.add(ch13);
        list.add(ch14);
        list.add(ch15);
        list.add(ch16);
        list.add(ch17);
        list.add(ch18);
        list.add(ch19);
        list.add(ch20);
        list.add(ch21);
        //list.add(ch22);
        list.add(ch23);
        list.add(ch24);
        list.add(ch25);
        list.add(ch26);
//
        int j = 0;
        for (int i = 18; i <= 42; i++)
        {
            list.get(j).value = values[i];
            j++;
        }



        return list;
    }
    //25


    public static List<CheckList> generateInterior(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Trunk  - Rear Hatch";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Visually inspect Interior Parts for Damage and Fit";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "All Interior Lamps and Horn";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Rare View Mirror";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Front Wipers and Washers";
        ch5.status = false;
        CheckList ch51 = new CheckList();
        ch51.CheckListName = "Rear Wipers and Washers";
        ch51.status = false;

        CheckList ch6 = new CheckList();
        ch6.CheckListName = "Defrost  - Rear";
        ch6.status = false;
        CheckList ch7 = new CheckList();
        ch7.CheckListName = "All Interior Door Locks including Child Locks (if applicable)";
        ch7.status = false;
        CheckList ch8 = new CheckList();
        ch8.CheckListName = "Steering Wheel Mounted Controls";
        ch8.status = false;
        CheckList ch9 = new CheckList();
        ch9.CheckListName = "Memory Mirror Seat";
        ch9.status = false;
        CheckList ch10 = new CheckList();
        ch10.CheckListName = "Power Windows and Window Lock Switch (if equipped) and One Touch Up and One Touch Down (if equipped)";
        ch10.status = false;

        CheckList ch11 = new CheckList();
        ch11.CheckListName = "Power Heated Folding Mirrors";
        ch11.status = false;
//        ?CheckList ch12 = new CheckList();
//        ch12.CheckListName = "Exterior Mirrors";
//        ch12.status = false;
        CheckList ch13 = new CheckList();
        ch13.CheckListName = "Seats, Headrest and Seat Belts - All Adjustments Position the second and third row seat backs in the upright position and ensure the Seat Belts are unbuckled";
        ch13.status = false;
        CheckList ch14 = new CheckList();
        ch14.CheckListName = "Power Sunroof";
        ch14.status = false;
        CheckList ch15 = new CheckList();
        ch15.CheckListName = "Set Clocks (if applicable)";
        ch15.status = false;
//        ?CheckList ch16 = new CheckList();
//        ch16.CheckListName = "Audio System";
//        ch16.status = false;
        CheckList ch17 = new CheckList();
        ch17.CheckListName = "Satellite Radio";
        ch17.status = false;
        CheckList ch18 = new CheckList();
        ch18.CheckListName = "Confirm UCONNECT Langauge Set to Market";
        ch18.status = false;
        CheckList ch181 = new CheckList();
        ch181.CheckListName = "Confirm EVIC Set To Market Unit and Language";
        ch181.status = false;

        CheckList ch19 = new CheckList();
        ch19.CheckListName = "GPS Navigation";
        ch19.status = false;
//        ?CheckList ch20 = new CheckList();
//        ch20.CheckListName = "Video System";
//        ch20.status = false;
        CheckList ch21 = new CheckList();
        ch21.CheckListName = "Heated Steering Wheel";
        ch21.status = false;
        CheckList ch22 = new CheckList();
        ch22.CheckListName = "Power Outlets";
        ch22.status = false;
        CheckList ch23 = new CheckList();
        ch23.CheckListName = "USB Outlets";
        ch23.status = false;
        CheckList ch24 = new CheckList();
        ch24.CheckListName = "Integrated Child Set / Belt";
        ch24.status = false;
        CheckList ch25 = new CheckList();
        ch25.CheckListName = "Power Holding Headrests";
        ch25.status = false;
        CheckList ch26 = new CheckList();
        ch26.CheckListName = "Heated Seats";
        ch26.status = false;

        CheckList ch28 = new CheckList();
        ch28.CheckListName = "Ventilated Seats";
        ch28.status = false;
        CheckList ch29 = new CheckList();
        ch29.CheckListName = "Cargo Compartment Covers";
        ch29.status = false;
        CheckList ch30 = new CheckList();
        ch30.CheckListName = "Power Locking Fuel Filler Door";
        ch30.status = false;
        CheckList ch31 = new CheckList();
        ch31.CheckListName = "Power Tilt Steering Column";
        ch31.status = false;
        CheckList ch32 = new CheckList();
        ch32.CheckListName = "Cycle Glovebox Lock";
        ch32.status = false;

        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch51);
        list.add(ch6);
        list.add(ch7);
        list.add(ch8);
        list.add(ch9);
        list.add(ch10);
        list.add(ch11);
        //list.add(ch12);
        list.add(ch13);
        list.add(ch14);
        list.add(ch15);
        //list.add(ch16);
        list.add(ch17);
        list.add(ch18);
        list.add(ch181);
        list.add(ch19);
       //list.add(ch20);
        list.add(ch21);
        list.add(ch22);
        list.add(ch23);
        list.add(ch24);
        list.add(ch25);
        list.add(ch26);
        list.add(ch28);
        list.add(ch29);
        list.add(ch30);
        list.add(ch31);
        list.add(ch32);


        int j = 0;
        for (int i = 43; i <= 71; i++)
        {
            list.get(j).value = values[i];
            j++;
        }


        return list;
    }
    //29

    public static List<CheckList> generateExterior(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Inspect body and paint for damage and fit and finish";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Remove Door Edge Protector for all doors (if equipped)";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Exterior Lamps - HeadLamps, Turn Signals, Hazards, Park/Tail/Lincense Plate, Trunk Lights ,etc.";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Lock and Unlock all Doors with All Mechanical Keys including Liftgate  or Trunk (if equipped)";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Doors, Liftgate and Tailgate - Adj Strikers as Needed";
        ch5.status = false;
        CheckList ch6 = new CheckList();
        ch6.CheckListName = "Power Liftgate - inspect for fit and operation";
        ch6.status = false;
        CheckList ch7 = new CheckList();
        ch7.CheckListName = "Security Alarm Test - All Keys";
        ch7.status = false;
        CheckList ch8 = new CheckList();
        ch8.CheckListName = "Remote Keyless Entry - All Keys";
        ch8.status = false;
        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch6);
        list.add(ch7);
        list.add(ch8);


        int j = 0;
        for (int i = 72; i <= 79; i++)
        {
            list.get(j).value = values[i];
            j++;
        }

        return list;
    }

    //8

    public static List<CheckList> generateVehicleStorage(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Keep All Protective Transit Film and Wheel Covers/Films on Vehicle Until Sold or Up To 180 Days";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Inflate Tire Pressure to Max Side Wall Pressure";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Periodically move Vehicles to prevent Flat Spotting on Tires";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Periodically move Vehicles to prevent corrosion on brake rotors";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Place Vehicle into Ship Mode";
        ch5.status = false;
        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);

        int j = 0;
        for (int i = 80; i <= 84; i++)
        {
            list.get(j).value = values[i];
        }

        return list;
    }
    //5


    public static List<CheckList> generateFinalDetailInspection(int[] values)
    {
        List<CheckList> list = new ArrayList();

        CheckList ch1 = new CheckList();
        ch1.CheckListName = "Inspect Paint & Body for Damage and Fit Touch Up if Necessary";
        ch1.status = false;
        CheckList ch2 = new CheckList();
        ch2.CheckListName = "Perform all Incomplete Recalls and RRTs";
        ch2.status = false;
        CheckList ch3 = new CheckList();
        ch3.CheckListName = "Place vehicle into Customer Mode";
        ch3.status = false;
        CheckList ch4 = new CheckList();
        ch4.CheckListName = "Battery State of Charge";
        ch4.status = false;
        CheckList ch5 = new CheckList();
        ch5.CheckListName = "Adjust Tire pressure Including Spare to Door Placard";
        ch5.status = false;
        CheckList ch6 = new CheckList();
        ch6.CheckListName = "Confirm brake rotors do not have corrosion. Follow procedure in Service Information";
        ch6.status = false;
        CheckList ch7 = new CheckList();
        ch7.CheckListName = "Remove Interior and Exterior Transportation Protective Covers";
        ch7.status = false;
        CheckList ch8 = new CheckList();
        ch8.CheckListName = "Wash and Clean Vehicle Exterior";
        ch8.status = false;
        CheckList ch9 = new CheckList();
        ch9.CheckListName = "Clean tires";
        ch9.status = false;
        CheckList ch10 = new CheckList();
        ch10.CheckListName = "Clean Vehicle Interior";
        ch10.status = false;

        list.add(ch1);
        list.add(ch2);
        list.add(ch3);
        list.add(ch4);
        list.add(ch5);
        list.add(ch6);
        list.add(ch7);
        list.add(ch8);
        list.add(ch9);
        list.add(ch10);
//
        int j = 0;
        for (int i = 85; i <= 94; i++)
        {
            list.get(j).value = values[i];
            j++;
        }

        return list;
    }
    //10


    public static Vehicle generateSampleVehicle()
    {
        Gson gson = new Gson();
        Vehicle vehicle = gson.fromJson(Constants.SAMPLE_VEHICLE_DATA,Vehicle.class);
        return vehicle;
    }

    public static List<VehicleStatus> generateVehiclesStatus()
    {
        List<VehicleStatus> list = new ArrayList<>();

        VehicleStatus v1 = new VehicleStatus();
        v1.StatusName = "Vehicles marked Delivery Recieved";

        VehicleStatus v2 = new VehicleStatus();
        v2.StatusName = "Vehicles curently Preparing for lot";

        VehicleStatus v3 = new VehicleStatus();
        v3.StatusName = "Vehicles marked for PDI";

        VehicleStatus v4 = new VehicleStatus();
        v4.StatusName = "Vehicles marked PDI Incomplete";

        VehicleStatus v5 = new VehicleStatus();
        v5.StatusName = "Vehicles marked PDI Completed";

        VehicleStatus v6 = new VehicleStatus();
        v6.StatusName = "Vehicles in test drive";

        VehicleStatus v7 = new VehicleStatus();
        v7.StatusName = "Vehicles ready for inventory";

        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        list.add(v5);
        list.add(v6);
        list.add(v7);


        return list;
    }
}
