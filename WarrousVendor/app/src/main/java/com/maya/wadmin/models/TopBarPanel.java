package com.maya.wadmin.models;

/**
 * Created by Gokul Kalagara on 2/7/2018.
 */

public class TopBarPanel
{
    public String title;
    public String subTitle;

    public boolean isSelected;

    public TopBarPanel(String title, String subTitle, boolean isSelected) {
        this.title = title;
        this.subTitle = subTitle;
        this.isSelected = isSelected;
    }
}
