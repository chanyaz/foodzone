package com.maya.vgarages.adapters.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.models.Notification;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/10/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{

    List<Notification> list;
    Context context;
    int width = 0;

    public NotificationAdapter(List<Notification> list, Context context)
    {
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvTitle.setText(list.get(position).Title);
        holder.tvContent.setText(list.get(position).Content);
        holder.tvTimeAgo.setText(list.get(position).TimeAgo);

        Picasso.with(context)
                .load(list.get(position).Image)
                .into(holder.imgNotification);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, Utility.dpSize(context,100));
        params.setMargins(
                Utility.dpSize(context,15),
                Utility.dpSize(context, 15),
                Utility.dpSize(context,15),
                Utility.dpSize(context,list.size()-1 == position ? 30 : 15)
        );
        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgNotification)
        ImageView imgNotification;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvContent)
        TextView tvContent;

        @BindView(R.id.tvTimeAgo)
        TextView tvTimeAgo;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}