package com.maya.vgarages.models;

/**
 * Created by Gokul Kalagara on 4/9/2018.
 */

public class Service
{
    public int Id;
    public String Name;
    public int Image;
    public boolean IsSelected = false;

    public Service(String name, int image,boolean isSelected) {
        Name = name;
        Image = image;
        this.IsSelected = isSelected;
    }
}
