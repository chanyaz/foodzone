package com.maya.wcustomer.adapters.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.interfaces.adapters.home.IHomeOptionsAdapter;
import com.maya.wcustomer.models.HomeOption;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/14/2018.
 */

public class HomeOptionsAdapter extends RecyclerView.Adapter<HomeOptionsAdapter.ViewHolder>
{

    List<HomeOption> list;
    Context context;
    int height = 0;
    IHomeOptionsAdapter iHomeOptionsAdapter;

    public HomeOptionsAdapter(List<HomeOption> list, Context context,int height,IHomeOptionsAdapter iHomeOptionsAdapter) {
        this.list = list;
        this.context = context;
        this.iHomeOptionsAdapter = iHomeOptionsAdapter;
        this.height = height;
        this.height = this.height >= Utility.dpSize(context,210) ? height :  Utility.dpSize(context,210);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_option,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.imgOption.setImageResource(list.get(position).homeImage);
        holder.tvOptionTitle.setText(list.get(position).homeTitle);
        holder.tvOptionDetails.setText(list.get(position).homeDetails);

        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height/list.size()));

        holder.itemView.setOnClickListener(v -> iHomeOptionsAdapter.onItemClick(list.get(position),position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.imgOption)
        ImageView imgOption;

        @BindView(R.id.tvOptionTitle)
        TextView tvOptionTitle;

        @BindView(R.id.tvOptionDetails)
        TextView tvOptionDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
