package com.maya.wadmin.interfaces.fragments.testdrive;

import com.maya.wadmin.models.Vehicle;

/**
 * Created by Gokul Kalagara on 2/1/2018.
 */

public interface ITestDriveVehiclesAdapter
{
    public void onTestDriveVehicleClick(Vehicle vehicle,int position);
    public void onVehicleInfo(Vehicle vehicle,int position);
    public void onVehicleInfo(Vehicle vehicle,int position,int salespersonPosition);
    public void openLOTForm(Vehicle vehicle,int position,int salespersonPosition);
    public void openPDIForm(Vehicle vehicle,int position,int salespersonPosition);
    public void goToAsignPdi();
    public void goToAssignPreparation();
    public void openPDIForm(Vehicle vehicle,int position);
    public void openLOTForm(Vehicle vehicle,int position);
}
