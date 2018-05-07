package com.maya.vgarages.interfaces.adapter.cart;

import com.maya.vgarages.models.GarageService;

/**
 * Created by Gokul Kalagara on 5/3/2018.
 */
public interface ICheckoutAdapter
{
    public void deleteItem(GarageService garageService, int position);
}
