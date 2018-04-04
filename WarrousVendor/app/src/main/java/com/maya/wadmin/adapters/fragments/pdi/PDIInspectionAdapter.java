package com.maya.wadmin.adapters.fragments.pdi;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.pdi.IPDIChecklistAdapter;
import com.maya.wadmin.interfaces.adapters.pdi.IPDIInspectionAdapter;
import com.maya.wadmin.models.CheckList;
import com.maya.wadmin.models.Inspection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/12/2018.
 */

public class PDIInspectionAdapter extends RecyclerView.Adapter<PDIInspectionAdapter.ViewHolder> implements IPDIChecklistAdapter
{

    List<Inspection> list;
    Context context;
    IPDIInspectionAdapter ipdiInspectionAdapter;
    PDIChecklistAdapter pdiChecklistAdapter;
    IPDIChecklistAdapter ipdiChecklistAdapter;
    public boolean isClickable = true;

    public PDIInspectionAdapter(List<Inspection> list, Context context,IPDIInspectionAdapter ipdiInspectionAdapter) {
        this.list = list;
        this.context = context;
        this.ipdiInspectionAdapter = ipdiInspectionAdapter;
        ipdiChecklistAdapter = this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inspection_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.tvTitle.setText(list.get(position).name);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setFocusable(false);
        holder.recyclerView.setFocusableInTouchMode(false);

        int triggerInspected = 0;
        for(int i =0;i<list.get(position).checkLists.size();i++)
        {
            if(list.get(position).checkLists.get(i).value==-1)
            {
                triggerInspected++;
            }
        }
        holder.tvSelect.setText(""+(list.get(position).checkLists.size()-triggerInspected));
        holder.tvUnSelect.setText("" + triggerInspected);

        pdiChecklistAdapter = new PDIChecklistAdapter(list.get(position).checkLists,context,position,ipdiChecklistAdapter);
        pdiChecklistAdapter.isClickable = isClickable;

        holder.recyclerView.setAdapter(pdiChecklistAdapter);

        holder.llClick.setOnClickListener(click -> {ipdiInspectionAdapter.ItemClick(list.get(position),position);});

        holder.tvTitle.setOnClickListener(click -> {ipdiInspectionAdapter.ItemClick(list.get(position),position);});


        if(list.get(position).isOpenFlag)
        {
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
            holder.tvTitle.setTextColor(Color.parseColor("#C4172C"));
            holder.llhead.setVisibility(View.GONE);
            holder.llhead1.setVisibility(View.GONE);
        }
        else
        {
            holder.recyclerView.setVisibility(View.GONE);
            holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
            holder.tvTitle.setTextColor(Color.parseColor("#5C5C5C"));
            holder.llhead.setVisibility(View.VISIBLE);
            holder.llhead1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void changeValue(CheckList checkList, int inspectionPosition, int position, int value)
    {
        ipdiInspectionAdapter.changeValue(checkList,inspectionPosition,position,value);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSelect) TextView tvSelect;
        @BindView(R.id.tvUnSelect) TextView tvUnSelect;
        @BindView(R.id.recyclerView) RecyclerView recyclerView;
        @BindView(R.id.llClick) LinearLayout llClick;
        @BindView(R.id.llhead) LinearLayout llhead;
        @BindView(R.id.llhead1) LinearLayout llhead1;
        @BindView(R.id.imgClick) ImageView imgClick;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
