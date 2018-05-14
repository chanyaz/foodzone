package com.maya.vgarages.adapters.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.interfaces.adapter.other.IServiceAdapter;
import com.maya.vgarages.models.Service;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/9/2018.
 */

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Service> serviceList;
    public Context context;
    int width = 0;
    IServiceAdapter iServiceAdapter;
    boolean isLoading = true;

    public ServiceAdapter(List<Service> serviceList, Context context, IServiceAdapter iServiceAdapter, boolean isLoading) {
        if (context == null) return;
        this.isLoading = isLoading;
        this.serviceList = serviceList;
        this.context = context;
        this.iServiceAdapter = iServiceAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context, 60);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isLoading) {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_service_item, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!isLoading) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.imgService.setImageResource(serviceList.get(position).Image > 0 ? serviceList.get(position).Image : R.drawable.ic_settings);
            viewHolder.tvServiceName.setText(serviceList.get(position).GarageType);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(Utility.dpSize(context, 15), Utility.dpSize(context, 5), Utility.dpSize(context, serviceList.size() - 1 == position ? 15 : 0), Utility.dpSize(context, 5));
            viewHolder.itemView.setLayoutParams(params);

            viewHolder.llSelected.setBackgroundResource(serviceList.get(position).IsSelected ? R.drawable.corner_radius_fb_3 : R.drawable.corner_radius_white_3);
            viewHolder.itemView.setOnClickListener(view -> {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iServiceAdapter.onItemClick(serviceList.get(position), position);
                    }
                }, 200);
            });

//            viewHolder.imgService.setOnClickListener(view -> {
//                iServiceAdapter.onItemClick(serviceList.get(position), position);
//            });

            if (serviceList.get(position).TypeImageUrl != null) {
                Picasso.with(context)
                        .load(serviceList.get(position).TypeImageUrl)
                        .into(viewHolder.imgService);
            }
        } else {
            SkeletonViewHolder skeletonViewHolder = (SkeletonViewHolder) holder;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(Utility.dpSize(context, 15), Utility.dpSize(context, 5), Utility.dpSize(context, serviceList.size() - 1 == position ? 15 : 0), Utility.dpSize(context, 5));
            skeletonViewHolder.itemView.setLayoutParams(params);

        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? 10 : serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgService)
        ImageView imgService;

        @BindView(R.id.tvServiceName)
        TextView tvServiceName;

        @BindView(R.id.llSelected)
        LinearLayout llSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
