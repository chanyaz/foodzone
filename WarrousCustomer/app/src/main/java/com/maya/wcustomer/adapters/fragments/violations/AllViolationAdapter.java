package com.maya.wcustomer.adapters.fragments.violations;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.models.TimeBasedViolations;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/16/2018.
 */

public class AllViolationAdapter extends RecyclerView.Adapter<AllViolationAdapter.ViewHolder>
{

    List<TimeBasedViolations> list;
    Context context;

    public AllViolationAdapter(List<TimeBasedViolations> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.violation_by_date_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.tvDate.setText(list.get(position).date.toUpperCase());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(new ViolationsByDataAdapter(list.get(position).list,context));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
