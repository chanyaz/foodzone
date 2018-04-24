package com.maya.wadmin.adapters.fragments.pdi;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.IAssignPDIVehiclesAdapter;
import com.maya.wadmin.interfaces.adapters.testdrive.IAssignVehicleAdapter;
import com.maya.wadmin.models.Vehicle;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/2/2018.
 */

public class AssignPDIVehiclesAdapter extends RecyclerView.Adapter<AssignPDIVehiclesAdapter.ViewHolder>
{
    List<Vehicle> list,unChangedList;
    Context context;
    IAssignPDIVehiclesAdapter iAssignPDIVehiclesAdapter;

    public AssignPDIVehiclesAdapter(Context context, List<Vehicle> list, IAssignPDIVehiclesAdapter iAssignPDIVehiclesAdapter)
    {
        this.context = context;
        this.list = list;
        unChangedList =list;
        this.iAssignPDIVehiclesAdapter = iAssignPDIVehiclesAdapter;
    }

    @Override
    public AssignPDIVehiclesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AssignPDIVehiclesAdapter.ViewHolder viewHolder = new AssignPDIVehiclesAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_vehicle_item,parent,false));
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(AssignPDIVehiclesAdapter.ViewHolder holder, final int position)
    {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAssignPDIVehiclesAdapter.onItemClick(list.get(position),position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                iAssignPDIVehiclesAdapter.onItemClick(list.get(position),position);
                return false;
            }
        });

        if(list.get(position).assignPDI)
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
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.bottomView) View bottomView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
