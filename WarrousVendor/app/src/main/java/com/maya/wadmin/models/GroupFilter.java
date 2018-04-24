package com.maya.wadmin.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gokul Kalagara on 2/27/2018.
 */

public class GroupFilter implements Serializable
{

    public List<Year> yearList = null;
    public List<Make> makeList = null;
    public List<Model> modelList = null;
    public List<VehicleStatus> vehicleStatusList = null;


    public class Year
    {
        public String Years;
        public boolean isSelected = false;
    }
    public class Make
    {
        public String Make;
        public boolean isSelected = false;
    }
    public class Model
    {
        public String Models;
        public boolean isSelected = false;
    }
    public class VehicleStatus
    {
        public String Type;
        public int VehicleTypeId;
        public boolean isSelected = false;
    }


}
