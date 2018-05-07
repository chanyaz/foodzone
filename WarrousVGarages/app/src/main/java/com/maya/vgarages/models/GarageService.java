package com.maya.vgarages.models;

/**
 * Created by Gokul Kalagara on 4/12/2018.
 */

public class GarageService
{
    public double Price;
    public Boolean Tag = false;
    public int TagType;
    public String TagContent;

    public int DealerId;
    public int OpCodeId;
    public String OpCodeName;
    public String OpCodeContent;
    public String Code;
    public String ImageUrl;

    public boolean isAdded = false;
    public boolean isPending = false;
}
