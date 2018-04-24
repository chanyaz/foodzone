package com.maya.wcustomer.adapters.fragments.myway.trips;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.models.Trip;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/19/2018.
 */

public class RecentTripsAdapter extends RecyclerView.Adapter<RecentTripsAdapter.ViewHolder>
{

    List<Trip> list;
    Context context;
    int width = 0;
    public RecentTripsAdapter(List<Trip> list, Context context) {
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,100);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_trip_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvTime.setText(list.get(position).startTime + " - " +list.get(position).endTime.toLowerCase().trim());
        holder.tvDay.setText(list.get(position).day.toLowerCase());
        holder.tvTripName.setText(list.get(position).tripName);
        holder.tvStart.setText(list.get(position).startPlace);
        holder.tvEnd.setText(list.get(position).endPlace);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context,5),list.size()-1==position ? Utility.dpSize(context,15) : 0,Utility.dpSize(context,5));
        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tvTripName)
        TextView tvTripName;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.tvDay)
        TextView tvDay;

        @BindView(R.id.tvStart)
        TextView tvStart;

        @BindView(R.id.tvEnd)
        TextView tvEnd;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
