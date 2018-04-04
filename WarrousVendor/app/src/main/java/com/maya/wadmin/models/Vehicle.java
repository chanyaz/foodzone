package com.maya.wadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Gokul Kalagara on 1/29/2018.
 */

public class Vehicle implements Serializable
{
    public String VehicleId ;
    public String VehicleGuid ;
    public String DealerId;
    public String Make;
    public String Model;
    public String Year;
    public String Vin;
    public String Image ;
    public String Type ;
    public int CustomerId ;
    public String Status;
    public String Latitude;
    public String Longitude;
    public String BodyStyle;
    public String ExteriorColor;
    public String InteriorColor;
    public String OdometerStatus;
    public String DoorsQuantity;
    public String TransmissionType;
    public String VehicleType;
    public int TransitTruckId;
    public int TransitTruckTripId;
    public String DeliveryDate;
    public String FirstName;
    public String LastName;
    public String TruckName;
    public String DriverName;
    public String LicensePlateNo;
    public String Origin;
    public String Destination;
    public String CurrentLocation;
    public String VehicleDetails;
    public String DeviceId;
    public boolean ConnectionStatus;
    public boolean EnableStatus;
    public boolean IsActive;
    public String EstimatedDelivery;
    public int VehicleCount;
    public int RouteId;
    public String AssignedTo;
    public int VehicleTypeId;
    public String Role;
    public int TestDriveTypeId;
    public String Recived;
    public String CustomerName;
    public String EngineNumber;
    public String TrimCode;
    public String PhoneNumber;
    public boolean IsConnected;
    public int ViolateCount;
    public String CurrentLatitude;
    public String CurrentLongitude;


    public boolean assignTestDrive;
    public boolean assignPDI;
    public boolean viewFullDetails;
    public int pdiValue = 1 + new Random().nextInt(90);

}
