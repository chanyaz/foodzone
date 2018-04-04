package com.maya.wadmin.interfaces.dialog;

import com.maya.wadmin.models.AlertRule;

/**
 * Created by Gokul Kalagara on 3/28/2018.
 */

public interface IActionConfirmationDialogAlert
{
    public void deleteAlertAction(AlertRule alertRule);
    public void cloneAlertAction(AlertRule alertRule);
}
