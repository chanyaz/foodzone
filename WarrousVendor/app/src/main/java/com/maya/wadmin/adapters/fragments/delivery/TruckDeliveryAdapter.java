package com.maya.wadmin.adapters.fragments.delivery;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.delivery.ITruckDeliveryAdapter;
import com.maya.wadmin.models.DeliveryTruck;
import com.maya.wadmin.utilities.Utility;

import java.util.List;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class TruckDeliveryAdapter extends RecyclerView.Adapter<TruckDeliveryAdapter.ViewHolder>
{
    List<DeliveryTruck> list;
    Context context;
    ITruckDeliveryAdapter iTruckDeliveryAdapter;

    public TruckDeliveryAdapter(List<DeliveryTruck> list, Context context, ITruckDeliveryAdapter iTruckDeliveryAdapter) {
        this.list = list;
        this.context = context;
        this.iTruckDeliveryAdapter = iTruckDeliveryAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_arrival_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvTruckName.setText(list.get(position).TruckName);
        holder.tvTruckDriver.setText("Driver: " + list.get(position).DriverName);

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(new VehicleArrivalAdapter(context,list.get(position).LstVehicles));

        if(list.get(position).EstimatedDeliveryDay.equals(Constants.TODAY))
        {
            try
            {
                String value = list.get(position).EstimatedDelivery.split(" ", 2)[1];
                String content[] = value.split(" ");
                String contentTime[] = content[0].split(":");
                holder.tvTruckDelivery.setText(contentTime[0] + ":" + contentTime[1] + " " + content[1]);
            }
            catch (Exception e)
            {

            }
            Resources r = context.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    200,
                    r.getDisplayMetrics()
            );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(px, 0, 0, 0);
            holder.imgTruck.setLayoutParams(params);

        }
        else if(list.get(position).EstimatedDeliveryDay.equals(Constants.TOMORROW))
        {
            try
            {
                String value = list.get(position).EstimatedDelivery.split(" ", 2)[1];
                String content[] = value.split(" ");
                String contentTime[] = content[0].split(":");
                holder.tvTruckDelivery.setText(contentTime[0] + ":" + contentTime[1] + " " + content[1]);
            }
            catch (Exception e)
            {

            }
            Resources r = context.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    150,
                    r.getDisplayMetrics()
            );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(px, 0, 0, 0);
            holder.imgTruck.setLayoutParams(params);
        }
        else if(list.get(position).EstimatedDeliveryDay.equals(Constants.NEXT_WEEK))
        {
            holder.tvTruckDelivery.setText(Utility.makeJSDateReadable1(list.get(position).EstimatedDelivery));
            Resources r = context.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    100,
                    r.getDisplayMetrics()
            );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(px, 0, 0, 0);
            holder.imgTruck.setLayoutParams(params);
        }
        else
        {

        }


        holder.mapView.setOnClickListener( click -> {
            iTruckDeliveryAdapter.onMapClick(list.get(position),position);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTruckName, tvTruckDelivery, tvTruckDriver;

        RecyclerView recyclerView;

        ImageView mapView, imgTruck;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvTruckName= itemView.findViewById(R.id.tvTruckName);
            tvTruckDelivery = itemView.findViewById(R.id.tvTruckDelivery);
            tvTruckDriver = itemView.findViewById(R.id.tvTruckDriver);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            mapView = itemView.findViewById(R.id.mapView);
            imgTruck = itemView.findViewById(R.id.imgTruck);

        }
    }
}
