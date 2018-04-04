package com.maya.wadmin.dialogs.rules_and_alerts;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.wadmin.R;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.dialog.IAddActionDialog;
import com.maya.wadmin.models.AlertActionChannel;
import com.maya.wadmin.models.Operation;
import com.maya.wadmin.models.Zone;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/19/2018.
 */

public class AddActionDialog extends Dialog
{


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    IAddActionDialog iAddActionDialog;

    @BindView(R.id.spOperation)
    Spinner spOperation;

    @BindView(R.id.spGeofence)
    Spinner spGeofence;

    Context context;

    String[] operations,zones;

    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @BindView(R.id.tvNext)
    TextView tvNext;


    @BindView(R.id.imgSMS) ImageView imgSMS;
    @BindView(R.id.imgCall) ImageView imgCall;
    @BindView(R.id.imgMail) ImageView imgMail;
    @BindView(R.id.imgNoti) ImageView imgNoti;

    @BindView(R.id.llSMS) LinearLayout llSMS;
    @BindView(R.id.llCall) LinearLayout llCall;
    @BindView(R.id.llMail) LinearLayout llMail;
    @BindView(R.id.llNoti) LinearLayout llNoti;
    @BindView(R.id.llContent) LinearLayout llContent;


    @BindView(R.id.tvSMS) TextView  tvSMS;
    @BindView(R.id.tvCall) TextView tvCall;
    @BindView(R.id.tvMail) TextView tvMail;
    @BindView(R.id.tvNoti) TextView tvNoti;

    Boolean isSMS = false, isCall = false, isMail = false, isNoti = false;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
    final Calendar calendar = Calendar.getInstance();


    @BindView(R.id.tvFrom)
    TextView tvFrom;

    @BindView(R.id.tvTo)
    TextView tvTo;

    List<Operation> operationList;
    List<Zone> zoneList;
    int position;
    int currentAlertId;

    @BindView(R.id.etContent)
    EditText etContent;

    @BindView(R.id.etMessage)
    EditText etMessage;


    String fromDate, toDate;
    AlertActionChannel alertActionChannel = null;



    public AddActionDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AddActionDialog(Context context,IAddActionDialog iAddActionDialog,int position, List<Operation> operationList, List<Zone> zoneList,int currentAlertId)
    {
        super(context);
        this.context = context;
        this.position = position;
        this.operationList = operationList;
        this.zoneList = zoneList;
        this.currentAlertId = currentAlertId;
        this.iAddActionDialog = iAddActionDialog;
    }

    public AddActionDialog(Context context, AlertActionChannel alertActionChannel,IAddActionDialog iAddActionDialog, int position, List<Operation> operationList, List<Zone> zoneList, int currentAlertId)
    {
        super(context);
        this.alertActionChannel = alertActionChannel;
        this.context = context;
        this.position = position;
        this.operationList = operationList;
        this.zoneList = zoneList;
        this.currentAlertId = currentAlertId;
        this.iAddActionDialog = iAddActionDialog;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_action_dialog);
        ButterKnife.bind(this);



        tvFrom.setOnClickListener(click -> {

            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth) + "-"+ String.valueOf(year) + " " +simpleDateFormat.format(calendar.getTime());
                    tvFrom.setText("From: "+date);
                    fromDate = date;
                }
            }, yy, mm, dd);
            datePicker.show();

        });

        tvTo.setOnClickListener(click ->
        {

            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String date = String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth) + "-"+ String.valueOf(year) + " " +simpleDateFormat.format(calendar.getTime());
                    tvTo.setText("To: "+date);
                    toDate = date;
                }
            }, yy, mm, dd);
            datePicker.show();

        });







        tvCancel.setOnClickListener(click -> {
                if(alertActionChannel==null)
                {
                    this.dismiss();
                }
                else
                {
                    //this.dismiss();
                   deleteAction();
                }
        });
        operations = new String[operationList.size()];
        int i = 0;
        for (Operation operation : operationList)
        {
            operations[i] = operation.OperatorName;
            i++;
        }

        ArrayAdapter<String> adapterOperation=new ArrayAdapter<String>(context,R.layout.spinner_text,operations);
        spOperation.setAdapter(adapterOperation);
        adapterOperation.setDropDownViewResource(R.layout.other_spinner_text);

        if(position == 0 || position==7) // geofence view in this
        {
            zones = new String[zoneList.size()];
            i = 0;
            for (Zone zone :  zoneList)
            {
                zones[i] = zone.GeofenceName;
                i++;
            }
            spGeofence.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapterZones=new ArrayAdapter<String>(context,R.layout.spinner_text,zones);
            spGeofence.setAdapter(adapterZones);
            adapterZones.setDropDownViewResource(R.layout.other_spinner_text);
        }
        else
        {
            spGeofence.setVisibility(View.GONE);
        }

        if(position==1 || position==2) // speed and mielage
        {
            llContent.setVisibility(View.VISIBLE);
        }
        else
        {
            llContent.setVisibility(View.GONE);
        }


        llSMS.setOnClickListener(click -> {  isSMS  = isSMS  == true?false:true; update();});
        llCall.setOnClickListener(click -> { isCall = isCall == true?false:true; update();});
        llMail.setOnClickListener(click -> { isMail = isMail == true?false:true; update();});
        llNoti.setOnClickListener(click -> { isNoti = isNoti == true?false:true; update();});

        update();

        tvNext.setOnClickListener(click -> checkAction());

        if(alertActionChannel!=null)
        {
            setUpAction();
        }


    }

    private void deleteAction()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(context);
        String URL = Constants.URL_DELETE_ALERT_CHANNEL + alertActionChannel.AlertChannelId ;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);

                Utility.closeProgressDialog(progressDialog);

                try
                {
                    iAddActionDialog.changeAction();
                    AddActionDialog.this.dismiss();
                }
                catch (Exception e)
                {
                    AddActionDialog.this.dismiss();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                Utility.closeProgressDialog(progressDialog);
                showSnackbar(Constants.CONNECTION_ERROR, 2);
                AddActionDialog.this.dismiss();
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);

    }

    private void checkAction()
    {
        if(position==1 || position==2)
        {
            if((""+etContent.getText()).length()>0)
            {

            }
            else
            {
                if(position==0)
                    showSnackbar("Please enter the Speed", 2);
                else
                {
                    showSnackbar( "Please enter the mileage", 2);
                }
                return;
            }
        }

        if(toDate==null || fromDate ==null)
        {
            showSnackbar("Please pick date range",2);
            return;
        }
        if((""+etMessage.getText()).trim().length()>0)
        {

        }
        else
        {
            showSnackbar("Please fill message",2);
            return;
        }

        if(isMail||isNoti||isCall||isSMS)
        {
            createAction();

        }
        else
        {
            showSnackbar("Please select notify type",2);
            return;
        }
    }


    private void saveAction()
    {

    }

    private void createAction()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(context);
        String value = position==1||position==2? // speed & mileage
                etContent.getText().toString() :
                position==0||position==7? // geofence 7 theft
                        "" + zoneList.get(spGeofence.getSelectedItemPosition()).GeofenceId :
                        "-1";

        String URL = Constants.URL_CREATE_ALERT_CHANNEL + "alertId=" +currentAlertId +
                "&alertChannelId=" +
                (alertActionChannel==null ? "0" : alertActionChannel.AlertChannelId) +
                "&operatorId=" + operationList.get(spOperation.getSelectedItemPosition()).OperatorId +
                "&value=" + value +
                "&message=" + etMessage.getText() +
                "&isSMS="+isSMS+"&isEmail="+ isMail + "&isPush="+ isNoti + "&isCall="+ isCall +
                "&fromdate=" + fromDate +
                "&todate=" + toDate;

        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);

                Utility.closeProgressDialog(progressDialog);

                try
                {
                    iAddActionDialog.changeAction();
                    AddActionDialog.this.dismiss();
                }
                catch (Exception e)
                {
                    AddActionDialog.this.dismiss();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                Utility.closeProgressDialog(progressDialog);
                showSnackbar(Constants.CONNECTION_ERROR, 2);
                AddActionDialog.this.dismiss();
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);

    }

    private void update()
    {
        imgSMS.setImageResource(isSMS?R.drawable.alert_message:R.drawable.alert_unselect_message);
        imgCall.setImageResource(isCall?R.drawable.alert_call:R.drawable.alert_unselect_call);
        imgMail.setImageResource(isMail?R.drawable.alert_mail:R.drawable.alert_unselect_mail);
        imgNoti.setImageResource(isNoti?R.drawable.alert_noti:R.drawable.alert_unselect_noti);

        tvSMS.setTextColor(Color.parseColor(isSMS? "#B8BECF" : "#c8cecf"));
        tvCall.setTextColor(Color.parseColor(isCall? "#B8BECF" : "#c8cecf"));
        tvMail.setTextColor(Color.parseColor(isMail? "#B8BECF" : "#c8cecf"));
        tvNoti.setTextColor(Color.parseColor(isNoti? "#B8BECF" : "#c8cecf"));
    }

    public void showSnackbar(String content,int type)
    {
        Utility.showSnackBar(context, coordinatorLayout,content,type);
    }

    public void setUpAction()
    {
        //notify
        isCall = alertActionChannel.IsCall;
        isSMS = alertActionChannel.IsSMS;
        isNoti = alertActionChannel.IsPush;
        isMail = alertActionChannel.IsEmail;
        update();

        //other values
        etContent.setText(alertActionChannel.Value);
        etMessage.setText(alertActionChannel.Message);
        toDate = alertActionChannel.ToDate;
        fromDate = alertActionChannel.FromDate;
        tvFrom.setText("From: "+fromDate);
        tvTo.setText("To: "+toDate);

        //spinner
        if(position == 0 || position == 7) // geofence & theft
        {
            for (int i =0;i<zones.length;i++)
            {
                if(zones[i].equalsIgnoreCase(alertActionChannel.Value))
                {
                    spGeofence.setSelection(i);
                    break;
                }
            }
        }

        for (int i =0;i<operations.length;i++)
        {
            if(operations[i].equalsIgnoreCase(alertActionChannel.Operator))
            {
                spOperation.setSelection(i);
                break;
            }
        }



        tvNext.setText("Save Action");
        tvCancel.setText("Delete");
        tvNext.setVisibility(View.VISIBLE);



    }
}
