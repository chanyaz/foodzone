package com.maya.wadmin.adapters.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.interfaces.adapters.home.IUserRoleAdapter;
import com.maya.wadmin.models.UserRole;

import java.util.List;
import java.util.Map;


/**
 * Created by Gokul Kalagara on 1/25/2018.
 */

public class UserRoleAdapter extends RecyclerView.Adapter<UserRoleAdapter.ViewHolder>
{
    List<UserRole> list;
    Context context;
    IUserRoleAdapter iUserRoleAdapter;
    public UserRoleAdapter(Context context, List<UserRole> list, IUserRoleAdapter iUserRoleAdapter)
    {
        this.context = context;
        this.list = list;
        this.iUserRoleAdapter = iUserRoleAdapter;
    }

    public UserRoleAdapter(Activity activity, List<UserRole> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_role,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iUserRoleAdapter!=null)
                iUserRoleAdapter.onUserRoleClick(list.get(position),position);
            }
        });

        holder.imgUserRole.setImageResource(list.get(position).image);
        holder.tvUserRoleName.setText(list.get(position).name);
        if(list.get(position).vehicleCounts!=null && list.get(position).vehicleCounts.size()>0)
        {
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            holder.recyclerView.setAdapter(new VehicleCountAdapter(list.get(position).vehicleCounts,context));
        }


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUserRoleName;
        ImageView imgUserRole;
        RecyclerView recyclerView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tvUserRoleName= itemView.findViewById(R.id.tvUserRoleName);
            imgUserRole = itemView.findViewById(R.id.imgUserRole);
            recyclerView = itemView.findViewById(R.id.recyclerView);

        }
    }




}
