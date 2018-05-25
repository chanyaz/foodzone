package com.maya.vgarages.dialogs.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.dialog.LocationGarageServiceAdapter;
import com.maya.vgarages.interfaces.adapter.dialog.ILocationGarageServiceAdapter;
import com.maya.vgarages.interfaces.dialog.IVGarageServicesDialog;
import com.maya.vgarages.models.Service;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/23/2018.
 */
public class VGarageServicesDialog extends Dialog implements ILocationGarageServiceAdapter
{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.llHeader)
    LinearLayout llHeader;

    Context context;
    List<Service> list;
    ILocationGarageServiceAdapter iLocationGarageServiceAdapter;
    IVGarageServicesDialog ivGarageServicesDialog;

    public VGarageServicesDialog(Context context, List<Service> list,  IVGarageServicesDialog ivGarageServicesDialog) {
        super(context);
        this.context = context;
        this.list = list;
        this.ivGarageServicesDialog = ivGarageServicesDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vgarage_services_dialog);
        ButterKnife.bind(this);
        iLocationGarageServiceAdapter = this;

        initialize();
    }

    private void initialize()
    {
        llHeader.setOnClickListener(v -> {this.dismiss();});
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new LocationGarageServiceAdapter(list, context,iLocationGarageServiceAdapter));
    }

    @Override
    public void onItemClick(Service service, int position)
    {
        this.dismiss();
        ivGarageServicesDialog.garageTypeChanged(service,position);
    }
}
