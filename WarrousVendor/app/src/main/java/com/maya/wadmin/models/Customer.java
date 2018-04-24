package com.maya.wadmin.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 1/30/2018.
 */

public class Customer implements Serializable
{
    public int CustomerId;
    public String FirstName;
    public String LastName ;
    public String FullName ;
    public String PrimaryEmail ;
    public String PhoneNumber ;
    public String CustomerGuid ;
    public String CountryCode ;
    public String StateCode ;
    public String EmailAddress ;
    public boolean assignTestDrive = false;
}
