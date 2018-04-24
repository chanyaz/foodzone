package com.maya.wadmin.adapters.fragments.rules_and_alerts;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.rules_and_alerts.IAlertsAdapter;
import com.maya.wadmin.models.AlertRule;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/8/2018.
 */

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ViewHolder>
{

    List<AlertRule> list;
    Context context;
    IAlertsAdapter iAlertsAdapter;
    public AlertsAdapter(List<AlertRule> list, Context context, IAlertsAdapter iAlertsAdapter) {
        this.list = list;
        this.context = context;
        this.iAlertsAdapter = iAlertsAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvTitle.setText(list.get(position).AlertName);
        holder.tvSubTitle.setText(list.get(position).CategoryName);
        holder.tvVehiclesContent.setText(list.get(position).VehicleCount +" vehicles");
        holder.cardView.setCardBackgroundColor(position%2==0 ? Color.parseColor("#C9F0D9") : Color.parseColor("#FFEAC2"));
        holder.llhead.setOnClickListener(click -> {iAlertsAdapter.openPopUpOptions(click,list.get(position),position);});

        holder.imgMessage.setImageResource(list.get(position).isSMS ? R.drawable.alert_message : R.drawable.alert_unselect_message);
        holder.imgCall.setImageResource(list.get(position).isCall ? R.drawable.alert_call : R.drawable.alert_unselect_call);
        holder.imgMail.setImageResource(list.get(position).isEmail ? R.drawable.alert_mail : R.drawable.alert_unselect_mail);
        holder.imgNoti.setImageResource(list.get(position).isPush ? R.drawable.alert_noti : R.drawable.alert_unselect_noti);
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
        @BindView(R.id.llhead) LinearLayout llhead;
        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
