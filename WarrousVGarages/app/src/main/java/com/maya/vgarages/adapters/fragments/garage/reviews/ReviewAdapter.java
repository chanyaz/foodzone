package com.maya.vgarages.adapters.fragments.garage.reviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.models.Review;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/12/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
{

    List<Review> list;
    Context context;
    int width = 0;

    public ReviewAdapter(List<Review> list, Context context)
    {
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,30);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvUserName.setText(Utility.getCamelCase(list.get(position).UserName));
        holder.tvRatting.setText(list.get(position).Ratting+ "/10");
        holder.tvReviewContent.setText(list.get(position).Content);
        holder.tvTimeAgo.setText(list.get(position).TimeAgo);


        Picasso.with(context)
                .load(list.get(position).Image)
                .into(holder.imgUser);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                Utility.dpSize(context, 15),
                Utility.dpSize(context, 15),
                Utility.dpSize(context, 15),
                Utility.dpSize(context, list.size() - 1 == position ? 30 : 15)
        );

        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgUser)
        ImageView imgUser;

        @BindView(R.id.tvUserName)
        TextView tvUserName;

        @BindView(R.id.tvReviewContent)
        TextView tvReviewContent;

        @BindView(R.id.tvRatting)
        TextView tvRatting;

        @BindView(R.id.tvTimeAgo)
        TextView tvTimeAgo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
