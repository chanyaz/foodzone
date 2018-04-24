package com.maya.wadmin.models;

import java.util.List;

/**
 * Created by Gokul Kalagara on 3/21/2018.
 */

public class OrderNotification
{
    public String date;
    public List<Notification> list;

    public OrderNotification(String date, List<Notification> list)
    {
        this.date = date;
        this.list = list;
    }
}
