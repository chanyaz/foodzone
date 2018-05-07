package com.maya.vgarages.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 5/4/2018.
 */
public class Appointment implements Serializable
{
    public String date;

    public String time;

    public boolean pickUpType = false;

    public String address;
}
