package com.maya.vgarages.dialogs.appointments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.dialog.IRescheduleAppointmentDialog;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.utilities.Utility;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/24/2018.
 */
public class RescheduleAppointmentDialog extends Dialog
{

    @BindView(R.id.tvYes)
    TextView tvYes;

    @BindView(R.id.tvNo)
    TextView tvNo;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.tvTime)
    TextView tvTime;



    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    String date = null, time = null;

    Context context;
    Appointment appointment;
    IRescheduleAppointmentDialog iRescheduleAppointmentDialog;

    public RescheduleAppointmentDialog(@NonNull Context context, Appointment appointment, IRescheduleAppointmentDialog iRescheduleAppointmentDialog) {
        super(context);
        this.context = context;
        this.iRescheduleAppointmentDialog = iRescheduleAppointmentDialog;
        this.appointment = appointment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reschedule_appointment_dialog);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize()
    {
        tvDate.setOnClickListener(v ->
        {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date = String.valueOf(dayOfMonth)+ "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(year);
                    tvDate.setText(date);
                }
            }, yy, mm, dd);
            datePicker.show();
        });

        tvTime.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();
            int hr = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                    tvTime.setText(time);
                }
            },hr,min,true);
            timePickerDialog.show();


        });

        tvNo.setOnClickListener(v -> {this.dismiss();});
        tvYes.setOnClickListener(v -> {
            //this.dismiss();
            if(date == null || time == null)
            {
                showSnackBar(date==null ? "Please pick date" : time == null ? "Please pick time" : "",2);
                return;
            }
            this.dismiss();
            iRescheduleAppointmentDialog.rescheduleAppointment(appointment,date,time);
        });
    }

    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(context,coordinatorLayout,snackBarText,type);
    }
}
