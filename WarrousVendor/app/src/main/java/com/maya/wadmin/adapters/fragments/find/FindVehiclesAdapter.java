package com.maya.wadmin.adapters.fragments.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.vehicle.find.IFindVehiclesAdapter;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/2/2018.
 */

public class FindVehiclesAdapter extends RecyclerView.Adapter<FindVehiclesAdapter.ViewHolder>
{
    List<Vehicle> list;
    Context context;
    IFindVehiclesAdapter iFindVehiclesAdapter;

    public FindVehiclesAdapter(List<Vehicle> list, Context context, IFindVehiclesAdapter iFindVehiclesAdapter) {
        this.list = list;
        this.context = context;
        this.iFindVehiclesAdapter = iFindVehiclesAdapter;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_vehicle_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {

        holder.tvVin.setText(list.get(position).Vin.toUpperCase());
        holder.tvModel.setText(list.get(position).Model);
        holder.imgVehicle.setImageResource(R.drawable.sample_image1);
        holder.tvOther.setText(list.get(position).Make + " " + list.get(position).Year);


        if(list.get(position).Recived==null)
        {

        }
        else
        {
            Logger.d(Utility.makeDatetoAgo(list.get(position).Recived));
            holder.tvReceived.setText("Received: "+ Utility.makeJSDateReadable(list.get(position).Recived));
        }



        if(list.get(position).Type==null)
        {

        }
        else
        {
            holder.tvStatus.setText("Status: "+list.get(position).Type);
        }



        if(position==list.size()-1)
        {
            holder.bottomView.setVisibility(View.GONE);
        }
        else
        {
            holder.bottomView.setVisibility(View.VISIBLE);
        }

        holder.llhead3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFindVehiclesAdapter.OnFindVehicleClick(list.get(position), position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFindVehiclesAdapter.GoToVehicleOverview(list.get(position),position);
            }
        });


        if(list.get(position).viewFullDetails)
        {
            holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
            holder.llhead1.setVisibility(View.VISIBLE);
            holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
        }
        else
        {
            holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
            holder.llhead1.setVisibility(View.GONE);
            holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvModel) TextView tvModel;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvReceived) TextView tvReceived;
        @BindView(R.id.tvStatus) TextView tvStatus;
        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.bottomView) View bottomView;
        @BindView(R.id.llhead) LinearLayout llhead;
        @BindView(R.id.llhead1) LinearLayout llhead1;
        @BindView(R.id.llhead3) LinearLayout llhead3;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
