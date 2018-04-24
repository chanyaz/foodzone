package com.maya.wadmin.interfaces.adapters.pdi;

import com.maya.wadmin.models.CheckList;

/**
 * Created by Gokul Kalagara on 2/13/2018.
 */

public interface IPDIChecklistAdapter
{
    public void changeValue(CheckList checkList,int inspectionPosition,int position,int value);
}
