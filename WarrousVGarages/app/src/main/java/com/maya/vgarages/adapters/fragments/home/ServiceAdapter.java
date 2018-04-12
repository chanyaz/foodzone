package com.maya.vgarages.adapters.fragments.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.adapter.other.IServiceAdapter;
import com.maya.vgarages.models.Service;
import com.maya.vgarages.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/9/2018.
 */

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public List<Service> serviceList;
    public Context context;
    int width = 0;
    IServiceAdapter iServiceAdapter;

    public ServiceAdapter(List<Service> serviceList, Context context, IServiceAdapter iServiceAdapter) {
        this.serviceList = serviceList;
        this.context = context;
        this.iServiceAdapter = iServiceAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,60);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.imgService.setImageResource(serviceList.get(position).Image);
        viewHolder.tvServiceName.setText(serviceList.get(position).Name);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/3, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context, 5 ),Utility.dpSize(context, serviceList.size()-1 == position ? 15 : 0),Utility.dpSize(context,5));
        viewHolder.itemView.setLayoutParams(params);

        viewHolder.llSelected.setBackgroundResource(serviceList.get(position).IsSelected? R.drawable.corner_radius_fb_3 : R.drawable.corner_radius_white_3);
        viewHolder.itemView.setOnClickListener(view -> {iServiceAdapter.onItemClick(serviceList.get(position),position);});
    }

    @Override
    public int getItemCount()
    {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgService)
        ImageView imgService;

        @BindView(R.id.tvServiceName)
        TextView tvServiceName;

        @BindView(R.id.llSelected)
        LinearLayout llSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
