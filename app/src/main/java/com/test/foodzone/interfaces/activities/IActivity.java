package com.test.foodzone.interfaces.activities;

import android.app.Activity;

/**
 * Created by Gokul Kalagara on 11/30/2017.
 */

public interface IActivity
{
    public void changeTitle(String title);
    public void showSnackBar(String snackBarText,int type);
    public Activity getActivity();
}
