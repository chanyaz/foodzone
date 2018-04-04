package com.maya.wadmin.models;

/**
 * Created by Gokul Kalagara on 3/21/2018.
 */

public class Notification
{
    public String AlertType;

    public String AlertMessage;

    public String AlertNow;

    public int type = 1;

    public String dateTime;

    public Notification(String alertType, String alertMessage, String alertNow, int type)
    {
        AlertType = alertType;
        AlertMessage = alertMessage;
        AlertNow = alertNow;
        this.type = type;
    }
}
