package com.maya.wcustomer.models;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.util.List;

/**
 * Created by Gokul Kalagara on 3/19/2018.
 */

public class Trip
{
    public String tripId;
    public String tripName;
    public String startPlace;
    public String endPlace;
    public String startTime;
    public String endTime;
    public String day;
    public String location;
    public String duration;
    public String distance;

    public List<TripPoint> tripPoints;
}
