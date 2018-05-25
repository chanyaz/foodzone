package com.maya.vgarages.interfaces.adapter.appointments;

import android.view.View;

import com.maya.vgarages.models.Appointment;

/**
 * Created by Gokul Kalagara on 5/16/2018.
 */
public interface IAppointmentsAdapter
{
    public void onItemClick(Appointment appointment,int position);
    public void openOptions(Appointment appointment,int position,View view);
}
