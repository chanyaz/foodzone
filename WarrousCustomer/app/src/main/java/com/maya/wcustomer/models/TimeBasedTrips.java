package com.maya.wcustomer.models;

import java.util.List;

/**
 * Created by Gokul Kalagara on 3/22/2018.
 */

public class TimeBasedTrips
{
    public String date;
    public List<Trip> list;

    public TimeBasedTrips(String date, List<Trip> list)
    {
        this.date = date;
        this.list = list;
    }
}
