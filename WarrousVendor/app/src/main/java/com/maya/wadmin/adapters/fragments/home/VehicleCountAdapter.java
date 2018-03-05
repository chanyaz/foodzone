package com.maya.wadmin.adapters.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.models.VehicleCount;
import com.maya.wadmin.utilities.Utility;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Gokul Kalagara on 2/23/2018.
 */

public class VehicleCountAdapter extends RecyclerView.Adapter<VehicleCountAdapter.ViewHolder>
{

    List<VehicleCount> list;
    Context context;
    int width = 0;

    public VehicleCountAdapter(List<VehicleCount> list, Context context) {
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,56);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_count_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.llhead.setBackgroundResource(position%2==0 ? R.drawable.corner_radius_hash_pool_6 : R.drawable.corner_radius_white_6);
        holder.tvCountVehicles.setText(""+list.get(position).VehicleCount);
        holder.tvVehiclesContent.setText(""+list.get(position).Content);
        holder.llhead.setLayoutParams(new LinearLayout.LayoutParams(width/2, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvCountVehicles, tvVehiclesContent;
        LinearLayout llhead;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tvCountVehicles= itemView.findViewById(R.id.tvCountVehicles);
            tvVehiclesContent = itemView.findViewById(R.id.tvVehiclesContent);
            llhead = itemView.findViewById(R.id.llhead);

        }
    }
}
