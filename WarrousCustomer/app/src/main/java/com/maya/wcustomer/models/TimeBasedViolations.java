package com.maya.wcustomer.models;

import java.util.List;

/**
 * Created by Gokul Kalagara on 3/16/2018.
 */

public class TimeBasedViolations
{
    public String date;
    public List<Violation> list;

    public TimeBasedViolations(String date, List<Violation> list) {
        this.date = date;
        this.list = list;
    }
}
