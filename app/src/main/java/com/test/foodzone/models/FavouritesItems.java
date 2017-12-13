package com.test.foodzone.models;

/**
 * Created by Gokul Kalagara on 12/13/2017.
 */

public class FavouritesItems
{
    public String id, name, description, restaurant, image, offer;
    public boolean available, close, veg;

    public FavouritesItems(String id, String name, String description, String restaurant, String image, String offer, boolean available, boolean close, boolean veg) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.restaurant = restaurant;
        this.image = image;
        this.offer = offer;
        this.available = available;
        this.close = close;
        this.veg = veg;
    }
}
