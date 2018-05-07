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
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
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
    boolean isLoading = true;
    IGaragesAdapter iGaragesAdapter;
    public GaragesAdapter(List<Garage> list, Context context, IGaragesAdapter iGaragesAdapter,boolean isLoading) {

        if(context==null)return;

        this.list = list;
        this.isLoading = isLoading;
        this.context = context;
        this.iGaragesAdapter = iGaragesAdapter;
        this.isLoading = isLoading;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    public GaragesAdapter(List<Garage> list,Context context,int type,IGaragesAdapter iGaragesAdapter,boolean isLoading)
    {
        if(context==null)return;

        this.isLoading = isLoading;
        this.type = type;
        this.list = list;
        this.context = context;
        this.iGaragesAdapter = iGaragesAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,type==1 ? 130 : 30);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            int layout = R.layout.skeleton_garage_item;

            switch (type)
            {
                case 0:
                    layout = R.layout.skeleton_garage_item;
                    break;
                case 1:
                    layout = R.layout.skeleton_location_garage_item;
                    break;
                case 2:
                    layout = R.layout.skeleton_recommended_garage_item;
                    break;

            }

            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
        }
        else
        {
            if (type == 0) // garage
            {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_item, parent, false));
            }
            else if (type == 1) // garage by location
            {
                return new LocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_garage_item, parent, false));
            }
            else if (type == 2) {
                return new RecommendedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_garage_item, parent, false));
            }
            else
            {
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(!isLoading)
        {
            if (type == 0) // garage list
            {
                ViewHolder viewHolder = (ViewHolder) holder;

                Picasso.with(context)
                        .load(list.get(position).ImageUrl)
                        .into(viewHolder.imgGarage);

                viewHolder.tvShopType.setText(list.get(position).Types);
                viewHolder.tvGarageName.setText(Utility.getCamelCase(list.get(position).DealerName));
                viewHolder.tvLocation.setText(list.get(position).Address1);
                viewHolder.tvDistance.setText(list.get(position).Distance + " km from you");
                viewHolder.tvOpen.setText(!list.get(position).IsClosed ? "Open Now" : "Closed Now");
                viewHolder.tvOpen.setTextColor(ContextCompat.getColor(context, !list.get(position).IsClosed ? R.color.colorPrimary : R.color.light_orange));
                viewHolder.tvValue.setText("" + list.get(position).CustomerRating);
                viewHolder.tvPriceRange.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating == 5 ? R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange1.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 4 ? R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange2.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 3 ? R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange3.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 2 ? R.color.colorPrimary : R.color.light_new_gray));
                viewHolder.tvPriceRange4.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 1 ? R.color.colorPrimary : R.color.light_new_gray));

                //if(list.get(position).ImageUrl!=null)

//                else
//                {
//                    viewHolder.imgGarage.setImageResource(R.drawable.corner_radius_white_3);
//                    viewHolder.imgGarage.setColorFilter(ContextCompat.getColor(context,R.color.app_hash_pool));
//                }


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, list.size() - 1 == position ? 30 : 15)
                );
                viewHolder.itemView.setLayoutParams(params);
            } else if (type == 1) {

                LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
                locationViewHolder.tvGarageName.setText(Utility.getCamelCase(list.get(position).DealerName));
                locationViewHolder.tvDistance.setText(list.get(position).Distance + " km from you");
                locationViewHolder.tvOpen.setText(!list.get(position).IsClosed ? "Open Now" : "Closed Now");
                locationViewHolder.tvOpen.setTextColor(ContextCompat.getColor(context, !list.get(position).IsClosed ? R.color.colorPrimary : R.color.light_orange));
                locationViewHolder.tvValue.setText("" + list.get(position).CustomerRating);


                Picasso.with(context)
                            .load(list.get(position).ImageUrl)
                            .into(locationViewHolder.imgGarage);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(
                        Utility.dpSize(context, position == 0 ? 30 : 15),
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, list.size() - 1 == position ? 30 : 0),
                        Utility.dpSize(context, 20)
                );
                locationViewHolder.itemView.setLayoutParams(params);

            } else if (type == 2) {
                RecommendedViewHolder recommendedViewHolder = (RecommendedViewHolder) holder;
                recommendedViewHolder.tvGarageName.setText(Utility.getCamelCase(list.get(position).DealerName));
                recommendedViewHolder.tvValue.setText("" + list.get(position).CustomerRating);
                recommendedViewHolder.tvDistance.setText(list.get(position).Distance + " km from you");
                recommendedViewHolder.tvOpen.setText(!list.get(position).IsClosed ? "Open Now" : "Closed Now");
                recommendedViewHolder.tvOpen.setTextColor(ContextCompat.getColor(context, !list.get(position).IsClosed ? R.color.colorPrimary : R.color.light_orange));
                recommendedViewHolder.tvPriceRange1.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating == 4 ? R.color.colorPrimary : R.color.light_new_gray));
                recommendedViewHolder.tvPriceRange2.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 3 ? R.color.colorPrimary : R.color.light_new_gray));
                recommendedViewHolder.tvPriceRange3.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 2 ? R.color.colorPrimary : R.color.light_new_gray));
                recommendedViewHolder.tvPriceRange4.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 1 ? R.color.colorPrimary : R.color.light_new_gray));


                Picasso.with(context)
                            .load(list.get(position).ImageUrl)
                            .into(recommendedViewHolder.imgGarage);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, list.size() - 1 == position ? 30 : 15)
                );
                recommendedViewHolder.itemView.setLayoutParams(params);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iGaragesAdapter.itemClick(list.get(position), position);
                }
            });
        }
        else
        {
            if(type==1)
            {
                SkeletonViewHolder skeletonViewHolder = (SkeletonViewHolder) holder;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(
                        Utility.dpSize(context, position == 0 ? 30 : 15),
                        Utility.dpSize(context, 15),
                        Utility.dpSize(context, position == 9 ? 30 : 0),
                        Utility.dpSize(context, 20)
                );
                skeletonViewHolder.itemView.setLayoutParams(params);

            }

        }

    }


    @Override
    public int getItemCount() {
        return isLoading ? 10 :list.size();
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

        @BindView(R.id.tvPriceRange)
        TextView tvPriceRange;

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

        @BindView(R.id.tvPriceRange)
        TextView tvPriceRange;

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
