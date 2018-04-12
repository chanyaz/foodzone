package com.maya.vgarages.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 4/10/2018.
 */

public class Garage implements Serializable
{
    public String Name;
    public String Image;
    public String Location;
    public String Distance;
    public String ShopType;
    public int PriceRange = 0;
    public String Value;
    public int Review = 0;

    public boolean isOpen = false;

}
