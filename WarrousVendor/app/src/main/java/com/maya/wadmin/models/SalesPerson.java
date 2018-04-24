package com.maya.wadmin.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gokul Kalagara on 1/30/2018.
 */

public class SalesPerson implements Serializable
{

    public int SalesPersonId;
    public String Name;
    public String Type;
    public int RouteId;
    public int VehiclesAssigned;
    public int Selected;

    // combining both of them
    public int VehicleId;
    public String VehicleGuid;
    public int CustomerId;
    public int DealerId;
    public int MakeId;
    public int MakemodelId;
    public int ModelyearId;
    public String AssignedTo;
    public int VehicleTypeId;
    public int VehicleCount;
    public List<Vehicle> LstVehicle;
    public String Status;
    public String CustomerName;
    public String Role;



    public boolean assignTestDrive;
    public boolean assignPDI;
    public boolean itemClicked = false;


}
