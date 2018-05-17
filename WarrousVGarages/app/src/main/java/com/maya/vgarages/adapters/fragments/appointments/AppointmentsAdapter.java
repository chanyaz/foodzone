package com.maya.vgarages.adapters.fragments.appointments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.appointments.IAppointmentsAdapter;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/16/2018.
 */
public class AppointmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    List<Appointment> appointmentList;
    IAppointmentsAdapter iAppointmentsAdapter;
    Context context;
    boolean isLoading = true;
    int width = 0;

    public AppointmentsAdapter(List<Appointment> appointmentList, IAppointmentsAdapter iAppointmentsAdapter, Context context, boolean isLoading) {

        if(context==null)
            return;

        this.appointmentList = appointmentList;
        this.iAppointmentsAdapter = iAppointmentsAdapter;
        this.context = context;
        this.isLoading = isLoading;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_appointment_item,parent,false));
        }
        else
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(!isLoading)
        {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvId.setText("" + appointmentList.get(position).ServiceAppointmentId);
            holder.tvDate.setText(Utility.makeJSDateReadableOther(appointmentList.get(position).ApptDate));
            holder.tvTime.setText(appointmentList.get(position).ApptTime);
            holder.tvEstimatedCost.setText("Rs. " + appointmentList.get(position).TotalPrice);
            holder.tvStatus.setText(appointmentList.get(position).AppointmentStatusType.toUpperCase());

            DrawableCompat.setTint(holder.tvStatus.getBackground(),ContextCompat.getColor(context,appointmentList.get(position).AppointmentStatusType.equalsIgnoreCase(Constants.REJECTED) ? R.color.badge_color1 : R.color.light_primary));



            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, appointmentList.size() - 1 == position ? 30 : 15)
            );

            holder.itemView.setLayoutParams(params);

            holder.itemView.setOnClickListener(v -> {
                if(iAppointmentsAdapter!=null) iAppointmentsAdapter.onItemClick(appointmentList.get(position),position);
            });

        }
        else
        {


        }

    }

    @Override
    public int getItemCount()
    {
        return isLoading?10:appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tvId)
        TextView tvId;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.imgOptions)
        ImageView imgOptions;

        @BindView(R.id.tvEstimatedCost)
        TextView tvEstimatedCost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
