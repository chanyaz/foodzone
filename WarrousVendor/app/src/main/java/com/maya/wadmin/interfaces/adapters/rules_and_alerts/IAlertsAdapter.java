package com.maya.wadmin.interfaces.adapters.rules_and_alerts;

import android.view.View;

import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Zone;

/**
 * Created by Gokul Kalagara on 2/22/2018.
 */

public interface IAlertsAdapter
{
    public void cloneAlert(AlertRule alertRule,int position);
    public void edit(AlertRule alertRule,int position);
    public void openPopUpOptions(View view, AlertRule alertRule, int position);
    public void deleteAlert(AlertRule alertRule,int position);
    public void openViolationVehicles(AlertRule alertRule,int position);
}
