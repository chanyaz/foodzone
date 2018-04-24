package com.maya.wadmin.adapters.fragments.rules_and_alerts;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.rules_and_alerts.IActionChannelAdapter;
import com.maya.wadmin.models.AlertActionChannel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/20/2018.
 */

public class ActionChannelAdapter extends RecyclerView.Adapter<ActionChannelAdapter.ViewHolder>
{

    List<AlertActionChannel> list;
    Context context;
    IActionChannelAdapter iActionChannelAdapter;

    public ActionChannelAdapter(List<AlertActionChannel> list, Context context,IActionChannelAdapter iActionChannelAdapter) {
        this.list = list;
        this.context = context;
        this.iActionChannelAdapter = iActionChannelAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        holder.tvSubTitle.setText(list.get(position).Message);
        holder.tvTitle.setText(list.get(position).Category + " " + list.get(position).Operator+ " "+list.get(position).Value);
        holder.imgCall.setImageResource(list.get(position).IsCall ? R.drawable.alert_call : R.drawable.alert_unselect_call);
        holder.imgMail.setImageResource(list.get(position).IsEmail ? R.drawable.alert_mail : R.drawable.alert_unselect_mail);
        holder.imgMessage.setImageResource(list.get(position).IsSMS ? R.drawable.alert_message : R.drawable.alert_unselect_message);
        holder.imgNoti.setImageResource(list.get(position).IsPush ? R.drawable.alert_noti : R.drawable.alert_unselect_noti);

        holder.cardView.setOnClickListener(click -> iActionChannelAdapter.editChannel(list.get(position),position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSubTitle) TextView tvSubTitle;
        @BindView(R.id.tvVehiclesContent) TextView tvVehiclesContent;
        @BindView(R.id.imgMessage) ImageView imgMessage;
        @BindView(R.id.imgCall) ImageView imgCall;
        @BindView(R.id.imgMail) ImageView imgMail;
        @BindView(R.id.imgNoti) ImageView imgNoti;
        @BindView(R.id.cardView) CardView cardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}