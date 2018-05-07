package com.maya.vgarages.models.vehicle;

import java.util.List;

/**
 * Created by Gokul Kalagara on 5/2/2018.
 */
public class VehicleDetails
{
    public List<Year> yearList = null;
    public List<Make> makeList = null;
    public List<Model> modelList = null;

    public class Make
    {
        public String Make;
        public boolean isSelected = false;
    }

    public class Model
    {
        public String Model;
        public boolean isSelected = false;
    }

    public class Year
    {
        public int Year;
        public int MakeModelYearId;
        public boolean isSelected = false;
    }

}
