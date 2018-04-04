package com.maya.wadmin.adapters.fragments.pdi;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.IAssignTechnicianPDIAdapter;
import com.maya.wadmin.models.SalesPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/5/2018.
 */

public class AssignTechnicianPDIAdapter extends RecyclerView.Adapter<AssignTechnicianPDIAdapter.ViewHolder>
{

    List<SalesPerson> list;
    Context context;
    IAssignTechnicianPDIAdapter iAssignTechnicianPDIAdapter;

    public AssignTechnicianPDIAdapter(List<SalesPerson> list, Context context, IAssignTechnicianPDIAdapter iAssignTechnicianPDIAdapter) {
        this.list = list;
        this.context = context;
        this.iAssignTechnicianPDIAdapter = iAssignTechnicianPDIAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_sales_person_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                iAssignTechnicianPDIAdapter.onTechnicianClick(list.get(position),position);
            }
        });

        if(list.get(position).assignPDI)
        {
            holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8_with_red);
            holder.imgClick.setImageResource(R.drawable.ic_check);
        }
        else
        {
            holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
            holder.imgClick.setImageResource(R.color.white);
        }

        holder.tvSalesPerson.setText(list.get(position).Name);

        if(list.get(position).VehicleCount>0)
        {
            if(list.get(position).VehicleCount==1)
            holder.tvRole.setText(list.get(position).VehicleCount + " Vehicle");
            else
            {
                holder.tvRole.setText(list.get(position).VehicleCount + " Vehicles");
            }
            holder.tvRole.setTextColor(Color.parseColor("#5C5C5C"));
        }
        else
        {
            holder.tvRole.setText("Not assigned");
            holder.tvRole.setTextColor(Color.parseColor("#69AD5E"));
        }

        Picasso.with(context)
                .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                .into(holder.imgSalesPerson);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
        @BindView(R.id.tvRole) TextView tvRole;
        @BindView(R.id.imgSalesPerson) ImageView imgSalesPerson;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.llhead) RelativeLayout llhead;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}