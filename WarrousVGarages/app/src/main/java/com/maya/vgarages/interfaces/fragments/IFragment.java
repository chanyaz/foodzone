package com.maya.vgarages.interfaces.fragments;

import android.app.Activity;

/**
 * Created by Gokul Kalagara on 1/25/2018.
 */

public interface IFragment
{
    public void changeTitle(String title);
    public void showSnackBar(String snackBarText, int type);
    public Activity activity();
}
