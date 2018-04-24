package com.maya.wadmin.interfaces.fragments.testdrive;

import com.maya.wadmin.models.Customer;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;

/**
 * Created by Gokul Kalagara on 1/29/2018.
 */

public interface IAddNewTestDriveFragment
{
    public void addVehicle(Vehicle vehicle);
    public void addSalesPerson(SalesPerson salesPerson);
    public void addCustomer(Customer customer);
}
