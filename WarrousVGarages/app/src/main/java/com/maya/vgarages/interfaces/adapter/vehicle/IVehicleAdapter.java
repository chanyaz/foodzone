package com.maya.vgarages.interfaces.adapter.vehicle;

import com.maya.vgarages.models.Vehicle;

/**
 * Created by Gokul Kalagara on 5/2/2018.
 */
public interface IVehicleAdapter
{
    public void deleteItem(Vehicle vehicle,int position);
    public void selectItem(Vehicle vehicle,int position);
}
