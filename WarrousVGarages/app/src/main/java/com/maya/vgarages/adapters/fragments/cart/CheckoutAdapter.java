package com.maya.vgarages.adapters.fragments.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.interfaces.adapter.cart.ICheckoutAdapter;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/1/2018.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    List<GarageService> list;
    Context context;
    boolean isLoading = true;
    ICheckoutAdapter iCheckoutAdapter;

    public CheckoutAdapter(List<GarageService> list, ICheckoutAdapter iCheckoutAdapter, Context context, boolean isLoading) {

        if(context==null) return;

        this.list = list;
        this.context = context;
        this.isLoading = isLoading;
        this.iCheckoutAdapter = iCheckoutAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_cart_item,parent,false));
        }
        else
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position)
    {

        if(!isLoading)
        {
            ViewHolder holder = (ViewHolder)viewHolder;
            holder.tvServiceName.setText(Utility.getCamelCase(list.get(position).OpCodeName));
            holder.tvPrice.setText("Rs. " + list.get(position).Price);

            holder.progressBar.setVisibility(list.get(position).isPending ? View.VISIBLE : View.GONE);
            holder.imgClose.setVisibility(list.get(position).isPending ? View.GONE : View.VISIBLE);

            holder.imgClose.setOnClickListener(v -> iCheckoutAdapter.deleteItem(list.get(position),position));
        }
        else
        {

        }

    }

    @Override
    public int getItemCount() {
        return isLoading ? 10 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvServiceName)
        TextView tvServiceName;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        @BindView(R.id.imgClose)
        ImageView imgClose;

        @BindView(R.id.view)
        View view;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
