package com.maya.vgarages.dialogs.garage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.dialog.IToPickVehicle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/4/2018.
 */
public class ToPickVehicle extends Dialog
{

    @BindView(R.id.tvGotoVehicles)
    TextView tvGotoVehicles;

    IToPickVehicle iToPickVehicle;

    Context context;
    public ToPickVehicle(Context context,  IToPickVehicle iToPickVehicle) {
        super(context);
        this.context = context;
        this.iToPickVehicle = iToPickVehicle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_pick_vehicle);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize()
    {
        tvGotoVehicles.setOnClickListener(v ->
        {
            iToPickVehicle.goToVehicles();
            this.dismiss();
        });
    }
}
