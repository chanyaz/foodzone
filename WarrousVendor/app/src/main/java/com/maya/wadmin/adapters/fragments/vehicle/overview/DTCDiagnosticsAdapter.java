package com.maya.wadmin.adapters.fragments.vehicle.overview;

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

import com.maya.wadmin.R;
import com.maya.wadmin.models.DTCDiagnostics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 1/31/2018.
 */

public class DTCDiagnosticsAdapter extends RecyclerView.Adapter<DTCDiagnosticsAdapter.ViewHolder>
{

    Context context;
    List<DTCDiagnostics> list;
    public DTCDiagnosticsAdapter(Context context,List<DTCDiagnostics> list)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnostics_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.tvCodeDesc.setText(list.get(position).CodeDesc);

        if(list.get(position).Status)
        {
            holder.imgStatus.setImageResource(R.drawable.ic_bold_correct);
            holder.tvCodeDesc.setTextColor(Color.parseColor("#5C5C5C"));
        }
        else
        {
            holder.imgStatus.setImageResource(R.drawable.ic_bold_cancel);
            holder.tvCodeDesc.setTextColor(Color.parseColor("#C4172C"));
        }

        if(position==list.size()-1)
        {
            holder.llhead.setVisibility(View.GONE);
        }
        else
        {
            holder.llhead.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvCodeDesc) TextView tvCodeDesc;
        @BindView(R.id.imgStatus) ImageView imgStatus;
        @BindView(R.id.imgDtc) ImageView imgDtc;
        @BindView(R.id.llhead) LinearLayout llhead;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
