package com.maya.wadmin.interfaces.adapters.pdi;

import com.maya.wadmin.models.SalesPerson;

/**
 * Created by Gokul Kalagara on 2/5/2018.
 */

public interface IAssignTechnicianPDIAdapter
{
    public void onTechnicianClick(SalesPerson salesPerson,int position);
}
