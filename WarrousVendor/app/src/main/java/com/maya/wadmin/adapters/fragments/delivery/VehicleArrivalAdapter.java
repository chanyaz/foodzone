package com.maya.wadmin.adapters.fragments.delivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.delivery.IVehicleArrivalAdapter;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class VehicleArrivalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    List<Vehicle> list;
    int type = 0;
    IVehicleArrivalAdapter iVehicleArrivalAdapter = null;

    public VehicleArrivalAdapter(Context context, List<Vehicle> list)
    {
        this.context = context;
        this.list = list;
    }

    public VehicleArrivalAdapter(int type,Context context, List<Vehicle> list,IVehicleArrivalAdapter iVehicleArrivalAdapter)
    {
        this.type = type;
        this.context = context;
        this.list = list;
        this.iVehicleArrivalAdapter = iVehicleArrivalAdapter;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(type==0)
        {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_vehicles, parent, false));
            return viewHolder;
        }
        else if(type==1)
        {
            NormalViewHolder normalViewHolder = new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_delivery_vehicle_item, parent, false));
            return normalViewHolder;
        }
        else
        {
            OtherViewHolder otherViewHolder = new OtherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.violated_vehicle_item, parent, false));
            return otherViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(type==0)
        {
                    ViewHolder holder = (ViewHolder) viewHolder;
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    if (list.get(position).IsConnected) {
                        holder.imgClick.setImageResource(R.drawable.ic_connected_true);
                        holder.imgClick.setColorFilter(Color.parseColor("#FFA552"), PorterDuff.Mode.SRC_IN);
                    } else {
                        holder.imgClick.setImageResource(R.drawable.ic_connected_true);
                        holder.imgClick.setColorFilter(Color.parseColor("#BBBCBD"), PorterDuff.Mode.SRC_IN);
                    }


                    holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                    holder.tvOther.setText(list.get(position).Make + "    " + list.get(position).Model + " " + list.get(position).Year);

                    holder.imgVehicle.setImageResource(R.drawable.sample_image1);


                    if (position == list.size() - 1) {
                        holder.bottomView.setVisibility(View.GONE);
                    } else {
                        holder.bottomView.setVisibility(View.VISIBLE);
                    }


                    holder.itemView.setOnClickListener(click ->
                    {
                        Intent intent = new Intent(context,HelperActivity.class);
                        intent.putExtra(Constants.FRAGMENT_KEY,111);
                        intent.putExtra("vehicle",list.get(position));
                        context.startActivity(intent);
                    });
        }
        else if(type==1)
        {
                    NormalViewHolder holder = (NormalViewHolder) viewHolder;
                    holder.itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    if (list.get(position).IsConnected) {
                        holder.imgClick.setImageResource(R.drawable.ic_connected_true);
                        holder.imgClick.setColorFilter(Color.parseColor("#FFA552"), PorterDuff.Mode.SRC_IN);
                    } else {
                        holder.imgClick.setImageResource(R.drawable.ic_connected_true);
                        holder.imgClick.setColorFilter(Color.parseColor("#BBBCBD"), PorterDuff.Mode.SRC_IN);
                    }


                    holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                    holder.tvOther.setText(list.get(position).Make + "    " + list.get(position).Model + " " + list.get(position).Year);


                    holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                    if(Utility.makeJSDateReadable1(list.get(position).EstimatedDelivery)!=null)
                    holder.tvArrival.setText("Arrival: "+ Utility.makeJSDateReadable1(list.get(position).EstimatedDelivery));
                    else
                    {
                        holder.tvArrival.setVisibility(View.GONE);
                    }


                    if (position == list.size() - 1) {
                        holder.bottomView.setVisibility(View.GONE);
                    } else {
                        holder.bottomView.setVisibility(View.VISIBLE);
                    }

                    holder.itemView.setOnClickListener(click -> {
                        if(iVehicleArrivalAdapter!=null)
                        {
                            iVehicleArrivalAdapter.itemClick(list.get(position),position);
                        }
                    });
        }
        else if(type==2)
        {
                OtherViewHolder holder = (OtherViewHolder) viewHolder;
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {

                    }
                });




                holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                holder.tvOther.setText(list.get(position).Make + "    " + list.get(position).Model + " " + list.get(position).Year);

                holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                holder.tvCount.setText("Violations : " + list.get(position).ViolateCount + ((list.get(position).ViolateCount>1) ? " times" : " time"));

                if (position == list.size() - 1)
                {
                    holder.bottomView.setVisibility(View.GONE);
                }
                else
                {
                    holder.bottomView.setVisibility(View.VISIBLE);
                }


            holder.itemView.setOnClickListener(click -> {
                if(iVehicleArrivalAdapter!=null)
                {
                    iVehicleArrivalAdapter.itemClick(list.get(position),position);
                }
            });
        }
        else
        {

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

    public class NormalViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvArrival) TextView tvArrival;
        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.bottomView) View bottomView;

        public NormalViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvCount) TextView tvCount;
        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.bottomView) View bottomView;

        public OtherViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
