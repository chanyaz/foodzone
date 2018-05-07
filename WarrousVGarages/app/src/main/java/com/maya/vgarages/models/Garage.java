package com.maya.vgarages.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 4/10/2018.
 */

public class Garage implements Serializable
{
//    public String Name;
//    public String Image;
//    public String Location;

    public String ShopType;
    //public int PriceRange = 0;
    //public String Value;
   // public int Review = 0;

    public int DealerId = 0;
    public String DealerGuid;
    public String DealerName;
    public String DealerDesc;
    public String Address1;
    public String Address2;
    public String CountryCode;
    public String StateCode;
    public String City;
    public String Zipcode;
    public String Phone1;
    public String Phone2;
    public String EmailAddress;

    public double Latitude;
    public double Longitude;
    public double CustomerRating;
    public double DealerRating;
    public double Distance;

    public String ImageUrl;
    public String LogoUrl;
    public String StartHr;
    public String EndHr;
    public String Types;

    public boolean IsClosed = false;


}
