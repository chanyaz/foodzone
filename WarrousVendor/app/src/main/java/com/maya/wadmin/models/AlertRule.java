package com.maya.wadmin.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 2/8/2018.
 */

public class AlertRule implements Serializable
{
    public int AlertId;
    public String AlertGuid;
    public String AlertName;
    public String CategoryId;
    public String CategoryName;
    public int VehicleCount;
    public boolean isSMS;
    public boolean isEmail;
    public boolean isPush;
    public boolean isCall;
    public String AlertDescription;

}
