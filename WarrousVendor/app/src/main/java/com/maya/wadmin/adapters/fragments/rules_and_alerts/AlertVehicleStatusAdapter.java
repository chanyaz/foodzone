package com.maya.wadmin.adapters.fragments.rules_and_alerts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.rules_and_alerts.IAlertVehicleStatusAdapter;
import com.maya.wadmin.models.VehicleStatus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/19/2018.
 */

public class AlertVehicleStatusAdapter extends RecyclerView.Adapter<AlertVehicleStatusAdapter.ViewHolder>
{

    List<VehicleStatus> list;
    Context context;
    IAlertVehicleStatusAdapter iAlertVehicleStatusAdapter;

    public AlertVehicleStatusAdapter(List<VehicleStatus> list, Context context, IAlertVehicleStatusAdapter iAlertVehicleStatusAdapter) {
        this.list = list;
        this.context = context;
        this.iAlertVehicleStatusAdapter = iAlertVehicleStatusAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_status_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.llhead.setBackgroundResource(!list.get(position).isItemSelected? R.drawable.corner_radius_white_8 : R.drawable.corner_radius_white_8_with_red);
        holder.tvVehicleStatus.setText(list.get(position).StatusName);
        holder.itemView.setOnClickListener( click -> iAlertVehicleStatusAdapter.itemSelected(list.get(position),position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVehicleStatus) TextView tvVehicleStatus;
        @BindView(R.id.llhead) RelativeLayout llhead;
        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
