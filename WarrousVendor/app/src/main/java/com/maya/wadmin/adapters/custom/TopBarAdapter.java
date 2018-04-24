package com.maya.wadmin.adapters.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.custom.ITopBarAdapter;
import com.maya.wadmin.models.TopBarPanel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/7/2018.
 */

public class TopBarAdapter extends RecyclerView.Adapter<TopBarAdapter.ViewHolder>
{
    List<TopBarPanel> list;
    Context context;
    ITopBarAdapter iTopBarAdapter;
    int hide = 0;

    public TopBarAdapter(List<TopBarPanel> list, Context context, ITopBarAdapter iTopBarAdapter) {
        this.list = list;
        this.context = context;
        this.iTopBarAdapter = iTopBarAdapter;
    }
    public TopBarAdapter(List<TopBarPanel> list, Context context, ITopBarAdapter iTopBarAdapter,int hide) {
        this.list = list;
        this.context = context;
        this.iTopBarAdapter = iTopBarAdapter;
        this.hide = hide;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_bar_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {

        holder.tvTitle.setText(list.get(position).title);

        holder.tvSubTitle.setText(list.get(position).subTitle);
        holder.tvSubTitle.setVisibility(hide==0?View.VISIBLE:View.GONE);
        if(list.get(position).isSelected)
        {
            holder.imgClick.setImageResource(R.drawable.ic_check);
        }
        else
        {
            holder.imgClick.setImageResource(R.drawable.corner_radius_white_8);
        }

        if(position==list.size()-1)
        {
            holder.bottomView.setVisibility(View.GONE);
        }
        else
        {
            holder.bottomView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(click ->
        {
            iTopBarAdapter.itemClick(list.get(position),position);
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSubTitle) TextView tvSubTitle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.bottomView) View bottomView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
