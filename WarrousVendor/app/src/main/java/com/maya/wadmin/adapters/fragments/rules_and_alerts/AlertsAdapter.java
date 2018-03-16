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
        TextView tvTitle, tvSubTitle, tvVehiclesContent;
        ImageView imgMessage, imgCall, imgMail, imgNoti;
        CardView cardView;
        LinearLayout llhead;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            tvVehiclesContent = itemView.findViewById(R.id.tvVehiclesContent);
            imgMessage = itemView.findViewById(R.id.imgMessage);
            imgCall = itemView.findViewById(R.id.imgCall);
            imgMail = itemView.findViewById(R.id.imgMail);
            imgNoti = itemView.findViewById(R.id.imgNoti);
            cardView = itemView.findViewById(R.id.cardView);
            llhead = itemView.findViewById(R.id.llhead);
        }
    }
}
