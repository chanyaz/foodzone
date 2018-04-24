package com.maya.wadmin.interfaces.custom;

import com.maya.wadmin.models.Options;

/**
 * Created by Gokul Kalagara on 3/12/2018.
 */

public interface IOptionsAdapter
{
    public void onOptionClick(Options options,int type,int position,String content);
}
