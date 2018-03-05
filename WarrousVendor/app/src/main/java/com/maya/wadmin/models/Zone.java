package com.maya.wadmin.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 2/12/2018.
 */

public class Zone implements Serializable
{
    public int GeofenceId;
    public double Radius;
    public double Longitude;
    public double Latitude;
    public String GeofenceName;
    public String GeofenceGuid;
    public String Path;
    public String GeofenceType;
    public String MapCenter;
    public String City;
    public String Keywords;

    public boolean itemClick;


}
