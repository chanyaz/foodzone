package com.maya.wcustomer.adapters.fragments.home;

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

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.models.Violation;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/14/2018.
 */

public class RecentViolationsAdapter extends RecyclerView.Adapter<RecentViolationsAdapter.ViewHolder>
{

    List<Violation> list;
    Context context;
    int width = 0;


    public RecentViolationsAdapter(List<Violation> list, Context context) {
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,100);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.violation_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvAlertName.setText(list.get(position).AlertType);
        holder.tvAlertMessage.setText(list.get(position).AlertMessage);
        holder.tvAlertDetails.setText(list.get(position).AlertNow);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context, 5 ),Utility.dpSize(context, list.size()-1 == position ? 15 : 0),Utility.dpSize(context,5));
        holder.itemView.setLayoutParams(params);
        holder.imgAlert.setColorFilter(Color.parseColor(Constants.COLOR_CODES[position%4]), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvAlertName)
        TextView tvAlertName;
        @BindView(R.id.tvAlertMessage)
        TextView tvAlertMessage;
        @BindView(R.id.tvAlertDetails)
        TextView tvAlertDetails;
        @BindView(R.id.imgAlert)
        ImageView imgAlert;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
