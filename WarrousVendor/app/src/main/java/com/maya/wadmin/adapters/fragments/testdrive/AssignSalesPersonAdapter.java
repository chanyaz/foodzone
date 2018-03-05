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
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.testdrive.IAssignSalesPersonAdapter;
import com.maya.wadmin.models.SalesPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gokul Kalagara on 1/30/2018.
 */

public class AssignSalesPersonAdapter extends RecyclerView.Adapter<AssignSalesPersonAdapter.ViewHolder>
{

    Context context;
    List<SalesPerson> list;
    IAssignSalesPersonAdapter iAssignSalesPersonAdapter;
    public AssignSalesPersonAdapter(Context context, List<SalesPerson> list,  IAssignSalesPersonAdapter iAssignSalesPersonAdapter)
    {
        this.context = context;
        this.list = list;
        this.iAssignSalesPersonAdapter = iAssignSalesPersonAdapter;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_sales_person_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position)
    {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                iAssignSalesPersonAdapter.onItemClick(list.get(position),position);
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

        holder.tvSalesPerson.setText(list.get(position).Name);

        if(list.get(position).Type!=null)
        holder.tvRole.setText(list.get(position).Type);
        else
        {
            holder.tvRole.setText(Constants.SALES_PERSON);
        }

        Picasso.with(context)
                .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                .into(holder.imgSalesPerson);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSalesPerson, tvRole;
        ImageView imgSalesPerson, imgClick;
        RelativeLayout llhead;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvSalesPerson= itemView.findViewById(R.id.tvSalesPerson);
            tvRole = itemView.findViewById(R.id.tvRole);
            imgSalesPerson = itemView.findViewById(R.id.imgSalesPerson);
            imgClick = itemView.findViewById(R.id.imgClick);
            llhead = itemView.findViewById(R.id.llhead);
        }
    }
}
