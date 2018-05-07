package com.maya.vgarages.adapters.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/26/2018.
 */
public class EmptyDataAdapter extends RecyclerView.Adapter<EmptyDataAdapter.EmptyViewHolder>
{
    int type = 1;
    Context context;
    String content = null;
    int imageId = 0;
    public EmptyDataAdapter(Context context,int type)
    {
        this.context = context;
        this.type = type;
    }

    public EmptyDataAdapter(Context context,int type,String content,int imageId)
    {
        this.context = context;
        this.type = type;
        this.content = content;
        this.imageId = imageId;
    }
    @Override
    public EmptyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_data_item,parent,false));
    }

    @Override
    public void onBindViewHolder(EmptyViewHolder holder, int position)
    {
        holder.llEmpty.setVisibility(type == 0 ? View.GONE :View.VISIBLE);
        if(content!=null)
        {
            holder.tvError.setText(content);
            holder.imgError.setImageResource(imageId);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.llEmpty)
        LinearLayout llEmpty;

        @BindView(R.id.tvError)
        TextView tvError;

        @BindView(R.id.imgError)
        ImageView imgError;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
