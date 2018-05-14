package com.maya.vgarages.dialogs.checkout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.dialog.IAppointmentDetailsDialog;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.utilities.Utility;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/3/2018.
 */
public class AppointmentDetailsDialog extends Dialog
{

    @BindView(R.id.llAddress)
    LinearLayout llAddress;

    @BindView(R.id.llCurrent)
    LinearLayout llCurrent;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.llDate)
    LinearLayout llDate;

    @BindView(R.id.llTime)
    LinearLayout llTime;

    @BindView(R.id.rbGarage)
    RadioButton rbGarge;

    @BindView(R.id.rbSelf)
    RadioButton rbSelf;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    Context context;
    final Calendar calendar = Calendar.getInstance();
    String pickDate = null, pickTime = null;
    Appointment appointment;
    IAppointmentDetailsDialog iAppointmentDetailsDialog;





    public AppointmentDetailsDialog(Context context, Appointment appointment, IAppointmentDetailsDialog iAppointmentDetailsDialog) {
        super(context);
        this.context = context;
        this.appointment = appointment;
        this.iAppointmentDetailsDialog = iAppointmentDetailsDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_details_dialog);
        ButterKnife.bind(this);

        initialize();

    }

    private void initialize()
    {
        rbGarge.setTypeface(Utility.getTypeface(1,context));
        rbSelf.setTypeface(Utility.getTypeface(1,context));

        rbGarge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    rbSelf.setChecked(false);
                    llAddress.setVisibility(View.VISIBLE);
                }


            }
        });

        rbSelf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    rbGarge.setChecked(false);
                    llAddress.setVisibility(View.GONE);
                }


            }
        });

        fab.setOnClickListener(v ->
        {
           // this.dismiss();
            checkAppointment();
        });


        llCurrent.setOnClickListener(v -> {
            etAddress.setText(Utility.getString(Utility.getSharedPreferences(),Constants.USER_COMPLETE_ADDRESS));
        });

        llDate.setOnClickListener(v ->
        {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = String.valueOf(dayOfMonth)+ "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(year);
                    tvDate.setText(date);
                    pickDate = date;
                }
            }, yy, mm, dd);
            datePicker.show();
        });

        llTime.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();
            int hr = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                    tvTime.setText(time);
                    pickTime = time;
                }
            },hr,min,true);
            timePickerDialog.show();


        });

        updateUI();
    }

    private void checkAppointment()
    {
        if(pickDate == null) {
            showSnackBar("Please pick date", 2);
            return;
        }
        if(pickTime == null)
        {
            showSnackBar("Please pick time", 2);
            return;
        }
        if(rbGarge.isChecked() == true)
        {
           if(("" + etAddress.getText()).trim().length()==0)
           {

               showSnackBar("Please add address", 2);
               return;
           }
        }

        appointment = new Appointment();
        appointment.date = pickDate;
        appointment.time = pickTime;
        appointment.pickUpType = rbGarge.isChecked() ? false : true;
        appointment.address = ("" + etAddress.getText()).trim();
        iAppointmentDetailsDialog.addAppointment(appointment);
        this.dismiss();

    }

    private void updateUI()
    {
        if (appointment == null) return;

        pickDate = appointment.date;
        pickTime = appointment.time;
        tvDate.setText(appointment.date);
        tvTime.setText(appointment.time);
        if(appointment.pickUpType)
        {
            rbSelf.setChecked(true);
            rbGarge.setChecked(false);
            llAddress.setVisibility(View.GONE);
        }
        else
        {
            rbSelf.setChecked(false);
            rbGarge.setChecked(true);
            llAddress.setVisibility(View.VISIBLE);
            etAddress.setText(appointment.address);
        }
    }

    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(context,coordinatorLayout,snackBarText,type);
    }
}
