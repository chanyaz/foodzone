package com.maya.wadmin.adapters.fragments.notifications;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.models.OrderNotification;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Gokul Kalagara on 3/16/2018.
 */

public class AllNotificationsAdapter extends RecyclerView.Adapter<AllNotificationsAdapter.ViewHolder>
{

    List<OrderNotification> list;
    Context context;

    public AllNotificationsAdapter(List<OrderNotification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_by_date_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.tvDate.setText(list.get(position).date.toUpperCase());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setNestedScrollingEnabled(false);
        holder.recyclerView.setFocusable(false);
        holder.recyclerView.setAdapter(new NotificationsByDateAdapter(list.get(position).list,context));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.recyclerView) RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
