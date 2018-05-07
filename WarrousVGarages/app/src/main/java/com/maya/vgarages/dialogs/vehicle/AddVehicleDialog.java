package com.maya.vgarages.dialogs.vehicle;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.dialog.IAddVehicleDialog;
import com.maya.vgarages.models.Vehicle;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/1/2018.
 */
public class AddVehicleDialog extends Dialog
{
    @BindView(R.id.tvDetails)
    TextView tvDetails;

    @BindView(R.id.tvYes)
    TextView tvYes;

    @BindView(R.id.tvNo)
    TextView tvNo;

    @BindView(R.id.cbMakeDefault)
    CheckBox cbMakeDefault;

    Context context;
    Vehicle vehicle;
    IAddVehicleDialog iAddVehicleDialog;

    public AddVehicleDialog(Context context, Vehicle vehicle,IAddVehicleDialog iAddVehicleDialog) {
        super(context);
        this.vehicle = vehicle;
        this.context = context;
        this.iAddVehicleDialog = iAddVehicleDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle_dialog);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize()
    {
        cbMakeDefault.setTypeface(Utility.getTypeface(0,context));
        tvDetails.setText("Do you want to continue with this "+vehicle.vehicleName+ "?");

        tvNo.setOnClickListener(v -> {
            this.dismiss();
        });

        tvYes.setOnClickListener(v ->
        {
            iAddVehicleDialog.makeDefaultVehicle(vehicle);
            this.dismiss();
        });


    }
}
