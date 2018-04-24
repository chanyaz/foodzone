package com.maya.wadmin.interfaces.adapters.vehicle.find;

import com.maya.wadmin.models.Vehicle;

/**
 * Created by Gokul Kalagara on 2/2/2018.
 */

public interface IFindVehiclesAdapter
{
    public void OnFindVehicleClick(Vehicle vehicle,int position);
    public void GoToVehicleOverview(Vehicle vehicle,int position);
}
