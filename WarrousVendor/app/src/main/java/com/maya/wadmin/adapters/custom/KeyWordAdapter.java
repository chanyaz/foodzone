package com.maya.wadmin.adapters.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.custom.IKeyWordAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 2/21/2018.
 */

public class KeyWordAdapter extends RecyclerView.Adapter<KeyWordAdapter.ViewHolder>
{

    List<String> list;
    Context context;
    IKeyWordAdapter iKeyWordAdapter;

    public KeyWordAdapter(List<String> list, Context context,IKeyWordAdapter iKeyWordAdapter) {
        this.list = list;
        this.context = context;
        this.iKeyWordAdapter = iKeyWordAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.key_words_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvTitle.setText(list.get(position));
        holder.imgCancel.setOnClickListener(click -> {iKeyWordAdapter.removeKeyword(position,list.get(position));});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.imgCancel) ImageView imgCancel;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
