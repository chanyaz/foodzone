package com.maya.wadmin.adapters.fragments.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.other.IFilterAdapter;
import com.maya.wadmin.models.GroupFilter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/27/2018.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder>
{
    GroupFilter groupFilter;
    Context context;
    int type;
    IFilterAdapter iFilterAdapter;

    public FilterAdapter(GroupFilter groupFilter, Context context, int type, IFilterAdapter iFilterAdapter)
    {
        this.groupFilter = groupFilter;
        this.context = context;
        this.type = type;
        this.iFilterAdapter = iFilterAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        String title = "";
        boolean isSelected = true;
        switch (type)
        {
            case 1:
                title = groupFilter.yearList.get(position).Years;
                isSelected = groupFilter.yearList.get(position).isSelected;
                break;
            case 2:
                title = groupFilter.makeList.get(position).Make;
                isSelected = groupFilter.makeList.get(position).isSelected;
                break;
            case 3:
                title = groupFilter.modelList.get(position).Models;
                isSelected = groupFilter.modelList.get(position).isSelected;
                break;
            case 4:
                title = groupFilter.vehicleStatusList.get(position).Type;
                isSelected = groupFilter.vehicleStatusList.get(position).isSelected;
                break;
        }

        holder.tvTitle.setText(title.trim().replaceAll("\n ", ""));
        holder.llhead.setBackgroundResource(isSelected?R.drawable.corner_radius_primary_30:R.drawable.corner_radius_unselect_30);
        holder.itemView.setOnClickListener(click -> {iFilterAdapter.onItemClick(type,position);});
    }

    @Override
    public int getItemCount()
    {
        int count = 0;
        switch (type)
        {
            case 1:
                count = groupFilter.yearList.size();
                break;
            case 2:
                count = groupFilter.makeList.size();
                break;
            case 3:
                count = groupFilter.modelList.size();
                break;
            case 4:
                count = groupFilter.vehicleStatusList.size();
                break;
        }
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.llhead) LinearLayout llhead;
        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
