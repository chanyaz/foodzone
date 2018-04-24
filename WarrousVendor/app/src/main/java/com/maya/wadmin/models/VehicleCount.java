package com.maya.wadmin.models;

/**
 * Created by Gokul Kalagara on 2/23/2018.
 */

public class VehicleCount
{
    public int VehicleCount;
    public String Type;
    public int TypeId;
    public String Content;

    public VehicleCount(int vehicleCount, String type, int typeId, String content)
    {
        VehicleCount = vehicleCount;
        Type = type;
        TypeId = typeId;
        Content = content;
    }

    public VehicleCount(int vehicleCount, String type, int typeId)
    {
        VehicleCount = vehicleCount;
        Type = type;
        TypeId = typeId;
    }

    public VehicleCount(int vehicleCount, String content)
    {
        VehicleCount = vehicleCount;
        Content = content;
    }

}
