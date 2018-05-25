package com.maya.vgarages.interfaces.dialog;

import com.maya.vgarages.models.Appointment;

/**
 * Created by Gokul Kalagara on 5/24/2018.
 */
public interface IRescheduleAppointmentDialog
{
    public void rescheduleAppointment(Appointment appointment,String date, String time);
}
