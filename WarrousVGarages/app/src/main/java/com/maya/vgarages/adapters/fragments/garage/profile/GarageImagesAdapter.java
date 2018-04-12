package com.maya.vgarages.adapters.fragments.garage.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/11/2018.
 */

public class GarageImagesAdapter extends RecyclerView.Adapter<GarageImagesAdapter.ViewHolder>
{
    List<String> list;
    Context context;
    int width = 0;

    public GarageImagesAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context)
                .load(list.get(position) != null && list.get(position).length() > 0 ? list.get(position) : Constants.SAMPLE_ERROR_IMAGE)
                .into(holder.imgGarage);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(list.size()>1 ? width - Utility.dpSize(context,100) : width - Utility.dpSize(context,30), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                Utility.dpSize(context, 15),
                Utility.dpSize(context, 15),
                Utility.dpSize(context, list.size() - 1 == position ? 15 : 0),
                Utility.dpSize(context, 15)
        );

        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgGarage)
        ImageView imgGarage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
