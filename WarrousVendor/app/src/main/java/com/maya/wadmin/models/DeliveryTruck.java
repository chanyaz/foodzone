package com.maya.wadmin.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class DeliveryTruck implements Serializable
{
    public int TransitTruckId;
    public int TransitTruckTripId;
    public String TruckName;
    public int VehicleCount;
    public int RouteId;
    public String Latitude;
    public String Longitude;
    public String Origin;
    public String Destination;
    public String EstimatedDelivery;
    public String DriverName;
    public String EstimatedDeliveryDay;


    public List<Vehicle> LstVehicles;
}
