package com.maya.vgarages.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gokul Kalagara on 5/4/2018.
 */
public class Appointment implements Serializable
{
    public String date;
    public String time;
    public boolean pickUpType = false;
    public String address;

    public int ServiceAppointmentId;
    public int VehicleId;
    public String Make;
    public String Model;
    public String Year;
    public String ApptDate;
    public String ApptTime;

    public int DealerId;
    public String ImageUrl;
    public String DealerName;
    public String CustomerName;
    public String AppointmentStatusType;

    public List<GarageService> ICustomerOpcodes;

    public int OpCount;
    public int ServiceAdvisorId;
    public int AppointmentTypeId;
    public int AppointmentStatusTypeId;
    public String VehicleName;
    public int MakeModelYearId;
    public double TotalPrice;

}
