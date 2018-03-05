package com.maya.wadmin.adapters.fragments.testdrive;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.testdrive.IAssignVehicleAdapter;
import com.maya.wadmin.models.Vehicle;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gokul Kalagara on 1/29/2018.
 */

public class AssignVehicleAdapter extends RecyclerView.Adapter<AssignVehicleAdapter.ViewHolder>
{
    List<Vehicle> list,unChangedList;
    Context context;
    IAssignVehicleAdapter iAssignVehicleAdapter;

    public AssignVehicleAdapter(Context context, List<Vehicle> list, IAssignVehicleAdapter iAssignVehicleAdapter)
    {
        this.context = context;
        this.list = list;
        unChangedList =list;
        this.iAssignVehicleAdapter = iAssignVehicleAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_vehicle_item,parent,false));
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAssignVehicleAdapter.onItemClick(list.get(position),position);
            }
        });

        if(list.get(position).assignTestDrive)
        {
            holder.imgClick.setImageResource(R.drawable.ic_check);
            holder.tvOther.setTextColor(ContextCompat.getColor(context,R.color.mainColorPrimary));
            holder.tvVin.setTextColor(ContextCompat.getColor(context,R.color.mainColorPrimary));
        }
        else
        {
            holder.imgClick.setImageResource(R.drawable.corner_radius_white_8);
            holder.tvOther.setTextColor(ContextCompat.getColor(context,R.color.light_new_gray));
            holder.tvVin.setTextColor(Color.parseColor("#5C5C5C"));
        }


        holder.tvVin.setText(list.get(position).Vin.toUpperCase());
        holder.tvOther.setText(list.get(position).Make+ " " +list.get(position).Model + " " + list.get(position).Year);
//        Picasso.with(context)
//                .load(Constants.SAMPLE_IMAGE)
//                .placeholder(R.drawable.corner_radius_hash_pool_6)
//                .error(R.drawable.corner_radius_hash_pool_6)
//                .into(holder.imgVehicle);

        holder.imgVehicle.setImageResource(R.drawable.sample_image1);


        if(position==list.size()-1)
        {
            holder.bottomView.setVisibility(View.GONE);
        }
        else
        {
            holder.bottomView.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvVin, tvOther;
        ImageView imgVehicle, imgClick;
        View bottomView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvVin= itemView.findViewById(R.id.tvVin);
            tvOther = itemView.findViewById(R.id.tvOther);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgClick = itemView.findViewById(R.id.imgClick);
            bottomView = itemView.findViewById(R.id.bottomView);
        }
    }
}
