package com.maya.wcustomer.models;

/**
 * Created by Gokul Kalagara on 3/14/2018.
 */

public class Violation
{
    public String AlertType;

    public String AlertMessage;

    public String AlertNow;

    public int type = 1;

    public String dateTime;

    public Violation(String alertType, String alertMessage, String alertNow)
    {
        AlertType = alertType;
        AlertMessage = alertMessage;
        AlertNow = alertNow;
    }

    public Violation(String alertType, String alertMessage, String alertNow, int type) {
        AlertType = alertType;
        AlertMessage = alertMessage;
        AlertNow = alertNow;
        this.type = type;
    }
}
