package com.maya.vgarages.adapters.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.adapter.dialog.ILocationGarageServiceAdapter;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.models.Service;
import com.maya.vgarages.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/23/2018.
 */
public class LocationGarageServiceAdapter extends RecyclerView.Adapter<LocationGarageServiceAdapter.ViewHolder>
{
    List<Service> list;
    Context context;
    ILocationGarageServiceAdapter iLocationGarageServiceAdapter;

    public LocationGarageServiceAdapter(List<Service> list, Context context, ILocationGarageServiceAdapter iLocationGarageServiceAdapter) {
        this.list = list;
        this.context = context;
        this.iLocationGarageServiceAdapter = iLocationGarageServiceAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vgl_service_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(Utility.getCamelCase(list.get(position).GarageType));
        holder.imgStatus.setVisibility(list.get(position).IsSelected? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            iLocationGarageServiceAdapter.onItemClick(list.get(position),position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.imgStatus)
        ImageView imgStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
