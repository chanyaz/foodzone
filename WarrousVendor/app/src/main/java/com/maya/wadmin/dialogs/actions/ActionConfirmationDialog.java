package com.maya.wadmin.dialogs.actions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.dialog.IActionConfirmationDialogAlert;
import com.maya.wadmin.interfaces.dialog.IActionConfirmationDialogVehicle;
import com.maya.wadmin.interfaces.dialog.IActionConfirmationDialogZone;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.models.Zone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/28/2018.
 */

public class ActionConfirmationDialog extends Dialog
{
    String content;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvYes)
    TextView tvYes;

    @BindView(R.id.tvNo)
    TextView tvNo;

    IActionConfirmationDialogVehicle iActionConfirmationDialogVehicle;
    IActionConfirmationDialogAlert iActionConfirmationDialogAlert;
    IActionConfirmationDialogZone iActionConfirmationDialogZone;

    Vehicle vehicle;
    AlertRule alertRule;
    Zone zone;

    int type = 0;
    int way = 0;
    public ActionConfirmationDialog(int type,Context context, String content, IActionConfirmationDialogAlert iActionConfirmationDialogAlert, AlertRule alertRule)
    {
        super(context);
        this.content = content;
        this.type = type;
        this.iActionConfirmationDialogAlert = iActionConfirmationDialogAlert;
        this.alertRule = alertRule;
    }

    public ActionConfirmationDialog(int way, int type, Context context, String content, IActionConfirmationDialogZone iActionConfirmationDialogZone, Zone zone)
    {
        super(context);
        this.content = content;
        this.type = type;
        this.iActionConfirmationDialogZone = iActionConfirmationDialogZone;
        this.zone = zone;
        this.way = way;
    }

    public ActionConfirmationDialog(int type, Context context, String content, IActionConfirmationDialogVehicle iActionConfirmationDialogVehicle, Vehicle vehicle)
    {
        super(context);
        this.content = content;
        this.vehicle = vehicle;
        this.iActionConfirmationDialogVehicle = iActionConfirmationDialogVehicle;
        way = 2;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_confirmation_dialog);
        ButterKnife.bind(this);

        tvTitle.setText("Are you sure you want to"+ "\n" + content);

        tvNo.setOnClickListener(v -> {
            this.dismiss();
        });

        tvYes.setOnClickListener(v ->
        {
            this.dismiss();
            switch (way)
            {
                case 0:
                wayToAlerts();
                break;

                case 1:
                wayToZones();
                break;

                case 2:
                waytoVehicles();
                break;
            }
        });


    }

    private void waytoVehicles()
    {
        switch (type)
        {
            case 0:
                iActionConfirmationDialogVehicle.deleteVehicle(vehicle);
                break;
        }
    }


    private void wayToZones()
    {
        switch (type)
        {
            case 0:
                iActionConfirmationDialogZone.deleteZoneAction(zone);
                break;
        }
    }

    private void wayToAlerts()
    {
        switch (type)
        {
            case 0:
                iActionConfirmationDialogAlert.deleteAlertAction(alertRule);
                break;
            case 1:
                iActionConfirmationDialogAlert.cloneAlertAction(alertRule);
                break;
        }
    }
}
