package com.maya.wcustomer.adapters.fragments.alert;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.models.AlertRule;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/16/2018.
 */

public class AlertRuleAdapter extends RecyclerView.Adapter<AlertRuleAdapter.ViewHolder> {


    List<AlertRule> list;
    Context context;

    public AlertRuleAdapter(List<AlertRule> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.switchCompat.setChecked(list.get(position).isActive);
        holder.tv_smtwtfs.setText("S M T W T F S");
        holder.tvAlertName.setText(list.get(position).alertName.toUpperCase());
        holder.tvAlertValue.setText(list.get(position).alertValue);
        holder.tvTime.setText(list.get(position).startTime+ " - "+list.get(position).endTime);
        holder.tvAlertName.setTextColor(Color.parseColor(Constants.COLOR_CODES[list.get(position).type-1]));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tvAlertName)
        TextView tvAlertName;

        @BindView(R.id.tvAlertValue)
        TextView tvAlertValue;

        @BindView(R.id.switchCompat)
        SwitchCompat switchCompat;

        @BindView(R.id.tv_smtwtfs)
        TextView tv_smtwtfs;

        @BindView(R.id.tvTime)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
