package com.maya.wadmin.adapters.fragments.pdi;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.pdi.IPDIChecklistAdapter;
import com.maya.wadmin.models.CheckList;

import java.util.List;

/**
 * Created by Gokul Kalagara on 2/12/2018.
 */

public class PDIChecklistAdapter extends RecyclerView.Adapter<PDIChecklistAdapter.ViewHolder>
{

    List<CheckList> lists;
    Context context;
    int inspectionPosition = -1;
    IPDIChecklistAdapter iPdiChecklistAdapter;

    public PDIChecklistAdapter(List<CheckList> lists, Context context, int inspectionPosition,IPDIChecklistAdapter iPdiChecklistAdapter ) {
        this.lists = lists;
        this.context = context;
        this.inspectionPosition = inspectionPosition;
        this.iPdiChecklistAdapter = iPdiChecklistAdapter;
    }

    public PDIChecklistAdapter(List<CheckList> lists, Context context,IPDIChecklistAdapter iPdiChecklistAdapter )
    {
        this.lists = lists;
        this.context = context;
        this.inspectionPosition = inspectionPosition;
        this.iPdiChecklistAdapter = iPdiChecklistAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {

        holder.tvTitle.setText(lists.get(position).CheckListName);
        holder.imgComment.setVisibility(View.GONE);

        switch (lists.get(position).value)
        {
            case -1:
                holder.tvInspected.setBackgroundResource(R.drawable.unselected_corner_6);
                holder.tvPass.setBackgroundResource(R.drawable.pdi_pass_unselected);
                holder.tvFail.setBackgroundResource(R.drawable.pdi_fail_unselected);

                holder.tvInspected.setTextColor(Color.parseColor("#FFFFFF"));
                holder.tvPass.setTextColor(Color.parseColor("#5C5C5C"));
                holder.tvFail.setTextColor(Color.parseColor("#5C5C5C"));


                break;
            case 0:

                holder.tvInspected.setBackgroundResource(R.drawable.corner_radius_white_6);
                holder.tvPass.setBackgroundResource(R.drawable.pdi_pass_unselected);
                holder.tvFail.setBackgroundResource(R.drawable.corner_radius_primary_6);

                holder.tvInspected.setTextColor(Color.parseColor("#5C5C5C"));
                holder.tvPass.setTextColor(Color.parseColor("#5C5C5C"));
                holder.tvFail.setTextColor(Color.parseColor("#FFFFFF"));

                break;
            case 1:

                holder.tvInspected.setBackgroundResource(R.drawable.corner_radius_white_6);
                holder.tvPass.setBackgroundResource(R.drawable.corner_radius_pass_6);
                holder.tvFail.setBackgroundResource(R.drawable.pdi_fail_unselected);

                holder.tvInspected.setTextColor(Color.parseColor("#5C5C5C"));
                holder.tvPass.setTextColor(Color.parseColor("#FFFFFF"));
                holder.tvFail.setTextColor(Color.parseColor("#5C5C5C"));

                break;
        }

        holder.tvFail.setOnClickListener(click -> {iPdiChecklistAdapter.changeValue(lists.get(position),inspectionPosition,position,0);});
        holder.tvPass.setOnClickListener(click -> {iPdiChecklistAdapter.changeValue(lists.get(position),inspectionPosition,position,1);});
        holder.tvInspected.setOnClickListener(click -> {iPdiChecklistAdapter.changeValue(lists.get(position),inspectionPosition,position,-1);});


        holder.llhead.setBackgroundColor(Color.parseColor(position%2==0?"#F7F8FB":"#FFFFFF"));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle, tvInspected, tvFail, tvPass;
        LinearLayout llhead;
        ImageView imgComment;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            tvInspected = itemView.findViewById(R.id.tvInspected);
            tvFail = itemView.findViewById(R.id.tvFail);
            tvPass = itemView.findViewById(R.id.tvPass);
            imgComment = itemView.findViewById(R.id.imgComment);
            llhead = itemView.findViewById(R.id.llhead);
        }
    }
}
