package com.maya.vgarages.adapters.fragments.vehicle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.interfaces.adapter.vehicle.IVehicleAdapter;
import com.maya.vgarages.models.Vehicle;
import com.maya.vgarages.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/2/2018.
 */
public class VehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    List<Vehicle> vehicleList;
    Context context;
    boolean isLoading = true;
    IVehicleAdapter iVehicleAdapter;
    boolean isDelete = true;

    public VehicleAdapter(List<Vehicle> vehicleList, IVehicleAdapter iVehicleAdapter, Context context,boolean isDelete, boolean isLoading) {
        if(context==null) return;

        this.vehicleList = vehicleList;
        this.context = context;
        this.isLoading = isLoading;
        this.iVehicleAdapter = iVehicleAdapter;
        this.isDelete = isDelete;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_vehicle_item,parent,false));
        }
        else
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position)
    {
        if(!isLoading)
        {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.rlDefault.setVisibility(vehicleList.get(position).isDefault?View.VISIBLE:View.GONE);
            viewHolder.tvVehicleName.setText(Utility.getCamelCase(vehicleList.get(position).vehicleName));
            viewHolder.tvVehicleDetails.setText(vehicleList.get(position).Make + " " + vehicleList.get(position).Model + " " + vehicleList.get(position).Year);
            viewHolder.imgClose.setVisibility(isDelete?View.VISIBLE:View.GONE);
            viewHolder.imgClose.setOnClickListener(v -> iVehicleAdapter.deleteItem(vehicleList.get(position),position));
            viewHolder.itemView.setOnClickListener(v -> iVehicleAdapter.selectItem(vehicleList.get(position),position));

        }
        else
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return isLoading ? 10 : vehicleList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.view)
        View view;

        @BindView(R.id.tvVehicleName)
        TextView tvVehicleName;

        @BindView(R.id.tvVehicleDetails)
        TextView tvVehicleDetails;

        @BindView(R.id.tvYear)
        TextView tvYear;

        @BindView(R.id.imgClose)
        ImageView imgClose;

        @BindView(R.id.rlDefault)
        RelativeLayout rlDefault;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
