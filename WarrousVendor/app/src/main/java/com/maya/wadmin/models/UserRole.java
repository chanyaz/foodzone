package com.maya.wadmin.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Gokul Kalagara on 1/25/2018.
 */

public class UserRole
{
    public String name;
    public int image;
    public List<VehicleCount> vehicleCounts;

    public UserRole(String name, int image)
    {
        this.name = name;
        this.image = image;
    }

    public UserRole(String name, int image,List<VehicleCount> vehicleCounts) {
        this.name = name;
        this.image = image;
        this.vehicleCounts = vehicleCounts;
    }
}
