package com.maya.vgarages.adapters.fragments.vehicle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.interfaces.adapter.vehicle.IAddVehicleAdapter;
import com.maya.vgarages.models.vehicle.VehicleDetails;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/2/2018.
 */
public class AddVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    VehicleDetails vehicleDetails;
    Context context;
    int type;
    IAddVehicleAdapter iAddVehicleAdapter;
    boolean isLoading = true;

    public AddVehicleAdapter(VehicleDetails vehicleDetails, Context context, int type, IAddVehicleAdapter iAddVehicleAdapter, boolean isLoading) {

        if(context==null) return;

        this.vehicleDetails = vehicleDetails;
        this.context = context;
        this.type = type;
        this.iAddVehicleAdapter = iAddVehicleAdapter;
        this.isLoading = isLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_vehicle_details_item,parent,false));
        }
        else
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_details_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(!isLoading)
        {
            ViewHolder viewHolder = (ViewHolder) holder;
            String title = "";
            boolean isSelected = true;
            switch (type)
            {
                case 1:
                    title = vehicleDetails.makeList.get(position).Make;
                    isSelected = vehicleDetails.makeList.get(position).isSelected;
                    break;
                case 2:
                    title = vehicleDetails.modelList.get(position).Model;
                    isSelected = vehicleDetails.modelList.get(position).isSelected;
                    break;
                case 3:
                    title = "" + vehicleDetails.yearList.get(position).Year;
                    isSelected = vehicleDetails.yearList.get(position).isSelected;
                    break;

            }

            viewHolder.tvTitle.setText(title.trim().replaceAll("\n ", ""));
            viewHolder.llHeader.setBackgroundResource(isSelected?R.drawable.corner_radius_primary_30:R.drawable.corner_radius_unselect_30);
            viewHolder.itemView.setOnClickListener(click -> {iAddVehicleAdapter.onItemClick(type,position);});

        }
        else
        {

        }

    }

    @Override
    public int getItemCount() {
        if(isLoading) return 10;

        int count = 0;
        switch (type)
        {
            case 1:
                count = vehicleDetails.makeList.size();
                break;
            case 2:
                count = vehicleDetails.modelList.size();
                break;
            case 3:
                count = vehicleDetails.yearList.size();
                break;
        }
        return count;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.llHeader)
        LinearLayout llHeader;
        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
