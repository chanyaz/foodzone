package com.maya.wadmin.adapters.fragments.testdrive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.testdrive.IAssignCustomerAdapter;
import com.maya.wadmin.models.Customer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 1/30/2018.
 */

public class AssignCustomerAdapter extends RecyclerView.Adapter<AssignCustomerAdapter.ViewHolder>
{
    List<Customer> list;
    Context context;
    IAssignCustomerAdapter iAssignCustomerAdapter;

    public AssignCustomerAdapter(Context context,List<Customer> list, IAssignCustomerAdapter iAssignCustomerAdapter)
    {
        this.list = list;
        this.context = context;
        this.iAssignCustomerAdapter = iAssignCustomerAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_customer_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                iAssignCustomerAdapter.onClickCustomer(list.get(position),position);
            }
        });

        if(list.get(position).assignTestDrive)
        {
            holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8_with_red);
            holder.imgClick.setImageResource(R.drawable.ic_check);
        }
        else
        {
            holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
            holder.imgClick.setImageResource(R.color.white);
        }

        holder.tvCustomerName.setText(list.get(position).FullName);
        if(list.get(position).PhoneNumber.trim().length()==10)
        holder.tvCustomerPhone.setText(
                "(" +   list.get(position).PhoneNumber.substring(0,3) + ") " +
                        list.get(position).PhoneNumber.substring(3,6) + "-" +
                        list.get(position).PhoneNumber.substring(6,10)
        );
        else
        {
            holder.tvCustomerPhone.setText("(415) 555-2671");
        }
        holder.tvCustomerEmail.setText(list.get(position).EmailAddress);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvCustomerName) TextView tvCustomerName;
        @BindView(R.id.tvCustomerEmail) TextView tvCustomerEmail;
        @BindView(R.id.tvCustomerPhone) TextView tvCustomerPhone;
        @BindView(R.id.tvCustomerTime) TextView tvCustomerTime;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.llhead) LinearLayout llhead;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
