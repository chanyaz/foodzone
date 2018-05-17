package com.maya.vgarages.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul Kalagara on 5/4/2018.
 */
public class Cart
{
    public Garage garage;
    public List<GarageService> listServices;

    public Cart()
    {
        listServices = new ArrayList<>();
    }

    public void addGarage(Garage garage)
    {
        this.garage = garage;
    }

    public void addGarageServices(List<GarageService> listServices)
    {
        this.listServices = listServices;
    }

    
}
