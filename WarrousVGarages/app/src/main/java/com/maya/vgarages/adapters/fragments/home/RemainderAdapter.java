package com.maya.vgarages.adapters.fragments.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.home.IRemainderAdapter;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/10/2018.
 */

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder>
{

    List<Garage> list;
    Context context;
    int width= 0;
    IRemainderAdapter iRemainderAdapter;
    public RemainderAdapter(List<Garage> list, Context context, IRemainderAdapter iRemainderAdapter) {
        this.list = list;
        this.context = context;
        this.iRemainderAdapter = iRemainderAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.remainder_garage_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvGarageName.setText(list.get(position).Name);
        holder.tvDistance.setText(list.get(position).Distance + " meters from you");
        holder.tvOpen.setText(list.get(position).isOpen?"Open Now":"Closed Now");
        holder.tvOpen.setTextColor(ContextCompat.getColor(context,list.get(position).isOpen?R.color.colorPrimary:R.color.light_orange));
        holder.tvPriceRange1.setTextColor(ContextCompat.getColor(context,list.get(position).Review == 4?R.color.colorPrimary : R.color.light_new_gray));
        holder.tvPriceRange2.setTextColor(ContextCompat.getColor(context,list.get(position).Review >= 3 ?R.color.colorPrimary : R.color.light_new_gray));
        holder.tvPriceRange3.setTextColor(ContextCompat.getColor(context,list.get(position).Review >= 2?R.color.colorPrimary : R.color.light_new_gray));
        holder.tvPriceRange4.setTextColor(ContextCompat.getColor(context,list.get(position).Review >= 1?R.color.colorPrimary : R.color.light_new_gray));

        Picasso.with(context)
                .load(list.get(position).Image)
                //.placeholder(Constants.IMAGE_PLACE_HOLDER)
                .into(holder.imgGarage);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                Utility.dpSize(context,15),
                Utility.dpSize(context, 15),
                Utility.dpSize(context,15),
                Utility.dpSize(context,list.size()-1 == position ? 30 : 15)
        );
        holder.itemView.setLayoutParams(params);

        holder.itemView.setOnClickListener(view -> {iRemainderAdapter.itemClick(list.get(position),position);});



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgGarage)
        ImageView imgGarage;

        @BindView(R.id.tvGarageName)
        TextView tvGarageName;

        @BindView(R.id.rlLocation)
        RelativeLayout rlLocation;

        @BindView(R.id.tvDistance)
        TextView tvDistance;


        @BindView(R.id.tvShopType)
        TextView tvShopType;

        @BindView(R.id.tvOpen)
        TextView tvOpen;

        @BindView(R.id.tvPriceRange1)
        TextView tvPriceRange1;

        @BindView(R.id.tvPriceRange2)
        TextView tvPriceRange2;

        @BindView(R.id.tvPriceRange3)
        TextView tvPriceRange3;

        @BindView(R.id.tvPriceRange4)
        TextView tvPriceRange4;

        @BindView(R.id.llHolder)
        LinearLayout llHolder;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
