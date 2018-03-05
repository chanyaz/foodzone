package com.maya.wadmin.interfaces.adapters.pdi;

import com.maya.wadmin.models.CheckList;
import com.maya.wadmin.models.Inspection;

/**
 * Created by Gokul Kalagara on 2/12/2018.
 */

public interface IPDIInspectionAdapter
{
    public void ItemClick(Inspection inspection, int position);
    public void changeValue(CheckList checkList, int inspectionPosition, int position, int value);
}
