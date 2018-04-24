package com.maya.wadmin.adapters.fragments.message;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.models.ChatPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/21/2018.
 */

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.ViewHolder>
{

    List<ChatPerson> list;
    Context context;

    public ChatBoxAdapter(List<ChatPerson> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_box_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvChatPerson.setText(list.get(position).name);
        Picasso.with(context).load(list.get(position).image).placeholder(R.drawable.unselect_circle).into(holder.imgPerson);
        holder.tvMessage.setText(list.get(position).lastMessage);
        holder.tvMessage.setTextColor(Color.parseColor(list.get(position).type == 0 ?"#27BE60":"#505050"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgPerson) ImageView imgPerson;
        @BindView(R.id.tvChatPerson) TextView tvChatPerson;
        @BindView(R.id.tvMessage) TextView tvMessage;
        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
