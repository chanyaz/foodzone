package com.maya.wadmin.adapters.fragments.zones;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.zones.IZonesViewAdapter;
import com.maya.wadmin.models.Zone;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/12/2018.
 */

public class ZonesViewAdapter extends RecyclerView.Adapter<ZonesViewAdapter.ViewHolder>
{

    List<Zone> list;
    Context context;
    IZonesViewAdapter iZonesViewAdapter;

    public ZonesViewAdapter(List<Zone> list, Context context, IZonesViewAdapter iZonesViewAdapter)
    {
        this.list = list;
        this.context = context;
        this.iZonesViewAdapter = iZonesViewAdapter;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvTitle.setText(list.get(position).GeofenceName);
        holder.tvLocation.setText(list.get(position).City);
        holder.tvKeywords.setText(list.get(position).Keywords);

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
            //iZonesViewAdapter.editZone(list.get(position),position);
        });

        holder.llhead.setOnClickListener(v -> {
            iZonesViewAdapter.openPopUp(v,list.get(position),position);
        });




    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvLocation) TextView tvLocation;
        @BindView(R.id.tvKeywords) TextView tvKeywords;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.bottomView) View bottomView;
        @BindView(R.id.llhead) LinearLayout llhead;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
