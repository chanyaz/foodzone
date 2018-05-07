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

public class RemainderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    List<Garage> list;
    Context context;
    int width= 0;
    IRemainderAdapter iRemainderAdapter;
    boolean isLoading = true;

    public RemainderAdapter(List<Garage> list, Context context, IRemainderAdapter iRemainderAdapter,boolean isLoading) {

        if(context==null)return;


        this.isLoading = isLoading;
        this.list = list;
        this.context = context;
        this.iRemainderAdapter = iRemainderAdapter;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_recommended_garage_item, parent, false));
        }
        else
        {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.remainder_garage_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position)
    {
        if(!isLoading)
        {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvGarageName.setText(list.get(position).DealerName);
            holder.tvDistance.setText(list.get(position).Distance + " km from you");
            holder.tvOpen.setText(!list.get(position).IsClosed ? "Open Now" : "Closed Now");
            holder.tvOpen.setTextColor(ContextCompat.getColor(context, !list.get(position).IsClosed ? R.color.colorPrimary : R.color.light_orange));

            holder.tvPriceRange.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating == 5 ? R.color.colorPrimary : R.color.light_new_gray));
            holder.tvPriceRange1.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 4 ? R.color.colorPrimary : R.color.light_new_gray));
            holder.tvPriceRange2.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 3 ? R.color.colorPrimary : R.color.light_new_gray));
            holder.tvPriceRange3.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 2 ? R.color.colorPrimary : R.color.light_new_gray));
            holder.tvPriceRange4.setTextColor(ContextCompat.getColor(context, list.get(position).DealerRating >= 1 ? R.color.colorPrimary : R.color.light_new_gray));

            Picasso.with(context)
                    .load(list.get(position).ImageUrl)
                    .into(holder.imgGarage);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, 15),
                    Utility.dpSize(context, list.size() - 1 == position ? 30 : 15)
            );
            holder.itemView.setLayoutParams(params);

            holder.itemView.setOnClickListener(view -> {
                iRemainderAdapter.itemClick(list.get(position), position);
            });
        }
        else
        {

        }



    }

    @Override
    public int getItemCount() {
        return isLoading ? 10 : list.size();
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

        @BindView(R.id.llHolder)
        LinearLayout llHolder;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
