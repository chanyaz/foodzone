package com.maya.wadmin.adapters.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.custom.IOptionsAdapter;
import com.maya.wadmin.models.Options;
import com.maya.wadmin.utilities.Utility;

import java.util.List;

/**
 * Created by Gokul Kalagara on 3/12/2018.
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder>
{

    List<Options> list;
    Context context;
    IOptionsAdapter iOptionsAdapter;
    int type;
    String content;
    int width;
    public OptionsAdapter(int type,List<Options> list, Context context,IOptionsAdapter iOptionsAdapter,String content)
    {
        this.list = list;
        this.context = context;
        this.iOptionsAdapter = iOptionsAdapter;
        this.type = type;
        this.content = content;
        width = context.getResources().getDisplayMetrics().widthPixels - Utility.dpSize(context,10);

        if(list.size()>0)
        width = list.size()<5? width/list.size() : Utility.dpSize(context,90);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.imgOption.setImageResource(list.get(position).optionImage);
        holder.tvOption.setText(list.get(position).optionName);
        holder.itemView.setOnClickListener(v -> {iOptionsAdapter.onOptionClick(list.get(position),type,position,content);});
        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvOption;
        ImageView imgOption;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvOption= itemView.findViewById(R.id.tvOption);
            imgOption = itemView.findViewById(R.id.imgOption);
        }
    }
}
