package com.maya.wadmin.adapters.fragments.notifications;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.models.Notification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Gokul Kalagara on 3/16/2018.
 */

public class NotificationsByDateAdapter extends RecyclerView.Adapter<NotificationsByDateAdapter.ViewHolder>
{


    List<Notification> list;
    Context context;


    public NotificationsByDateAdapter(List<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_notification_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        holder.tvAlertName.setText(list.get(position).AlertType);
        holder.tvAlertMessage.setText(list.get(position).AlertMessage);
        holder.tvAlertDetails.setText(list.get(position).AlertNow);
        holder.imgAlert.setColorFilter(Color.parseColor(Constants.COLOR_CODES[list.get(position).type-1]), PorterDuff.Mode.SRC_IN);
        holder.llHead.setVisibility(list.size()-1==position?View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvAlertName) TextView tvAlertName;
        @BindView(R.id.tvAlertMessage) TextView tvAlertMessage;
        @BindView(R.id.tvAlertDetails) TextView tvAlertDetails;
        @BindView(R.id.imgAlert) ImageView imgAlert;
        @BindView(R.id.llHead) LinearLayout llHead;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
