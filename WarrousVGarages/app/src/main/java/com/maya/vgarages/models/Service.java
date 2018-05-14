package com.maya.vgarages.models;

/**
 * Created by Gokul Kalagara on 4/9/2018.
 */

public class Service
{
    public int Id;
    public int Image = 0;
    public String GarageType;
    public boolean IsSelected = false;

    public int GarageTypeId;
    public String GarageTypeGuid;
    public String TypeDescription;
    public String TypeImageUrl;

    public Service(String name, int image,boolean isSelected) {
        GarageType = name;
        Image = image;
        this.IsSelected = isSelected;
    }
}
