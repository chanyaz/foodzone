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
import com.maya.vgarages.interfaces.adapter.home.IGaragesAdapter;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/10/2018.
 */

public class GaragesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<Garage> list;
    Context context;
    int width = 0;
    int type = 0;
    IGaragesAdapter iGaragesAdapter;
    public GaragesAdapter(List<Garage> list, Context context, IGaragesAdapter iGaragesAdapter) {
        this.list = list;
        this.context = context;
        this.iGaragesAdapter = iGaragesAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    public GaragesAdapter(List<Garage> list,Context context,int type,IGaragesAdapter iGaragesAdapter)
    {
        this.type = type;
        this.list = list;
        this.context = context;
        this.iGaragesAdapter = iGaragesAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,type==1 ? 130 : 30);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(type==0) // garage
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_item,parent,false));
        else if(type ==1) // garage by location
        {
            return new LocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_garage_item,parent,false));
        }
        else if(type == 2)
        {
            return new RecommendedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_garage_item,parent,false));
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(type==0) // garage list
        {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.tvGarageName.setText(list.get(position).Name);
                viewHolder.tvLocation.setText(list.get(position).Location);
                viewHolder.tvDistance.setText(list.get(position).Distance + " meters from you");
                viewHolder.tvOpen.setText(list.get(position).isOpen?"Open Now":"Closed Now");
                viewHolder.tvOpen.setTextColor(ContextCompat.getColor(context,list.get(position).isOpen?R.color.colorPrimary:R.color.light_orange));
                viewHolder.tvValue.setText(list.get(position).Value);
                viewHolder.tvPriceRange1.setTextColor(ContextCompat.getColor(context,list.get(position).PriceRange == 4?R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange2.setTextColor(ContextCompat.getColor(context,list.get(position).PriceRange >= 3 ?R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange3.setTextColor(ContextCompat.getColor(context,list.get(position).PriceRange >= 2?R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange4.setTextColor(ContextCompat.getColor(context,list.get(position).PriceRange >= 1?R.color.colorPrimary : R.color.light_new_gray));

                    Picasso.with(context)
                            .load(list.get(position).Image)
                            //.placeholder(Constants.IMAGE_PLACE_HOLDER)
                            .into(viewHolder.imgGarage);


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(
                            Utility.dpSize(context,15),
                            Utility.dpSize(context, 15),
                            Utility.dpSize(context,15),
                            Utility.dpSize(context,list.size()-1 == position ? 30 : 15)
                    );
                viewHolder.itemView.setLayoutParams(params);
        }
        else if(type == 1)
        {

            LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
            locationViewHolder.tvGarageName.setText(list.get(position).Name);
            locationViewHolder.tvDistance.setText(list.get(position).Distance + " meters from you");
            locationViewHolder.tvOpen.setText(list.get(position).isOpen?"Open Now":"Closed Now");
            locationViewHolder.tvOpen.setTextColor(ContextCompat.getColor(context,list.get(position).isOpen?R.color.colorPrimary:R.color.light_orange));
            locationViewHolder.tvValue.setText(list.get(position).Value);
            Picasso.with(context)
                    .load(list.get(position).Image)
                    //.placeholder(Constants.IMAGE_PLACE_HOLDER)
                    .into(locationViewHolder.imgGarage);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(
                    Utility.dpSize(context,position == 0 ? 30 : 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context,list.size()-1 == position ? 30 : 0),
                    Utility.dpSize(context,20)
            );
            locationViewHolder.itemView.setLayoutParams(params);

        }

        else if(type == 2)
        {
            RecommendedViewHolder recommendedViewHolder = (RecommendedViewHolder) holder;
            recommendedViewHolder.tvGarageName.setText(list.get(position).Name);
            recommendedViewHolder.tvValue.setText(list.get(position).Value);
            recommendedViewHolder.tvDistance.setText(list.get(position).Distance + " meters from you");
            recommendedViewHolder.tvOpen.setText(list.get(position).isOpen?"Open Now":"Closed Now");
            recommendedViewHolder.tvOpen.setTextColor(ContextCompat.getColor(context,list.get(position).isOpen?R.color.colorPrimary:R.color.light_orange));
            recommendedViewHolder.tvPriceRange1.setTextColor(ContextCompat.getColor(context,list.get(position).Review == 4?R.color.colorPrimary : R.color.light_new_gray));
            recommendedViewHolder.tvPriceRange2.setTextColor(ContextCompat.getColor(context,list.get(position).Review >= 3 ?R.color.colorPrimary : R.color.light_new_gray));
            recommendedViewHolder.tvPriceRange3.setTextColor(ContextCompat.getColor(context,list.get(position).Review >= 2?R.color.colorPrimary : R.color.light_new_gray));
            recommendedViewHolder.tvPriceRange4.setTextColor(ContextCompat.getColor(context,list.get(position).Review >= 1?R.color.colorPrimary : R.color.light_new_gray));

            Picasso.with(context)
                    .load(list.get(position).Image)
                    //.placeholder(Constants.IMAGE_PLACE_HOLDER)
                    .into(recommendedViewHolder.imgGarage);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(
                    Utility.dpSize(context,15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context,15),
                    Utility.dpSize(context,list.size()-1 == position ? 30 : 15)
            );
            recommendedViewHolder.itemView.setLayoutParams(params);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                iGaragesAdapter.itemClick(list.get(position),position);
            }
        });

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

        @BindView(R.id.tvValue)
        TextView tvValue;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.tvLocation)
        TextView tvLocation;

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


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgGarage)
        ImageView imgGarage;

        @BindView(R.id.tvGarageName)
        TextView tvGarageName;

        @BindView(R.id.tvValue)
        TextView tvValue;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.tvOpen)
        TextView tvOpen;



        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgGarage)
        ImageView imgGarage;

        @BindView(R.id.tvGarageName)
        TextView tvGarageName;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.tvValue)
        TextView tvValue;

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



        public RecommendedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
