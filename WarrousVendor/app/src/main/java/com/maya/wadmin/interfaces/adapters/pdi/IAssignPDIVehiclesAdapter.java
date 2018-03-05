package com.maya.wadmin.interfaces.adapters.pdi;

import com.maya.wadmin.models.Vehicle;

/**
 * Created by Gokul Kalagara on 2/2/2018.
 */

public interface IAssignPDIVehiclesAdapter
{
    public void onItemClick(Vehicle vehicle,int position);
    public void onItemLongClick(Vehicle vehicle,int position);
}
