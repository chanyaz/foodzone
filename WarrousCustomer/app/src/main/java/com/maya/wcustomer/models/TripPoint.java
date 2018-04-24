package com.maya.wcustomer.models;

import java.sql.Timestamp;

/**
 * Created by Gokul Kalagara on 3/22/2018.
 */

public class TripPoint
{
    public String tripId;
    public double lat;
    public double lng;
    public double bearing;
    public double speed;
    public double rpm;
    public boolean isSaved;
    public Timestamp timestamp;
}
