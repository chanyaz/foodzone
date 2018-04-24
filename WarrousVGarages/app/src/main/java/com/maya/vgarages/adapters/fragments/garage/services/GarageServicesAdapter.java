package com.maya.vgarages.adapters.fragments.garage.services;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/12/2018.
 */

public class GarageServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    List<GarageService> list;
    Context context;
    int width = 0;
    boolean isLoading = true;

    public GarageServicesAdapter(List<GarageService> list, Context context, boolean isLoading)
    {
        if(context == null) return;
        this.isLoading = isLoading;
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_garage_service_item,parent,false));
        }
        else
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_service_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position)
    {
        if(!isLoading)
        {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvServiceName.setText(Utility.getCamelCase(list.get(position).Name));
            holder.tvServiceContent.setText(list.get(position).Content);
            holder.tvPrice.setText("Rs. " + list.get(position).Price);

            //holder.rlTag.setVisibility(list.get(position).Tag? View.VISIBLE : View.GONE);
            if (list.get(position).Tag) {
                holder.tvTag.setText(Utility.getCamelCase(list.get(position).TagContent));
                holder.imgTag.setColorFilter(Color.parseColor(
                        Constants.TAG_COLOR_CODES[
                                Constants.TAG_COLOR_CODES.length < list.get(position).TagType
                                        ? Constants.TAG_COLOR_CODES.length - 1
                                        : list.get(position).TagType])
                );
            } else {
                holder.imgTag.setVisibility(View.GONE);
                holder.tvTag.setVisibility(View.GONE);
            }

            Picasso.with(context)
                    .load(list.get(position).Image)
                    .into(holder.imgGarageService);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, list.size() - 1 == position ? 30 : 15)
            );

            holder.itemView.setLayoutParams(params);
        }
        else
        {

        }

    }

    @Override
    public int getItemCount() {
        return isLoading? 10 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgGarageService)
        ImageView imgGarageService;

        @BindView(R.id.tvServiceName)
        TextView tvServiceName;

        @BindView(R.id.tvTag)
        TextView tvTag;

        @BindView(R.id.rlTag)
        RelativeLayout rlTag;

        @BindView(R.id.imgTag)
        ImageView imgTag;

        @BindView(R.id.tvServiceContent)
        TextView tvServiceContent;

        @BindView(R.id.tvAdd)
        TextView tvAdd;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
