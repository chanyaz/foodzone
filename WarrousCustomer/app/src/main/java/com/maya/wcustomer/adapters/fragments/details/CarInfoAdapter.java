package com.maya.wcustomer.adapters.fragments.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.models.CarInfo;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/15/2018.
 */

public class CarInfoAdapter extends RecyclerView.Adapter<CarInfoAdapter.ViewHolder> {

    List<CarInfo> list;
    Context context;
    int height;


    public CarInfoAdapter(List<CarInfo> list, Context context, int height) {
        this.list = list;
        this.context = context;
        this.height = height;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.car_info_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.imgInfo.setImageResource(list.get(position).infoImage);
        holder.tvInfoName.setText(list.get(position).infoName);
        holder.tvInfoValue.setText(list.get(position).value);
        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height> Utility.dpSize(context,200)? height/2 :  Utility.dpSize(context,200)/2));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = position%2 == 0 ? Gravity.LEFT : Gravity.RIGHT;
        holder.llHead.setLayoutParams(params);
        holder.tvInfoValue.setLayoutParams(params);


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvInfoValue)
        TextView tvInfoValue;

        @BindView(R.id.tvInfoName)
        TextView tvInfoName;

        @BindView(R.id.imgInfo)
        ImageView imgInfo;

        @BindView(R.id.llHead)
        LinearLayout llHead;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
