package com.maya.vgarages.adapters.fragments.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.adapters.custom.SkeletonViewHolder;
import com.maya.vgarages.interfaces.adapter.cart.ICheckoutAdapter;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

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
    Garage garage;
    int editFlag = 1;

    public CheckoutAdapter(Garage garage, List<GarageService> list, ICheckoutAdapter iCheckoutAdapter, Context context, boolean isLoading) {

        if(context==null)
            return;

        this.garage = garage;
        this.list = list;
        this.context = context;
        this.isLoading = isLoading;
        this.iCheckoutAdapter = iCheckoutAdapter;
    }

    public CheckoutAdapter(Garage garage, List<GarageService> list, Context context, boolean isLoading,int editFlag) {

        if(context==null)
            return;

        this.garage = garage;
        this.list = list;
        this.context = context;
        this.isLoading = isLoading;
        this.editFlag = editFlag;
    }




    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(isLoading)
        {
            if(viewType == 0)
            {
                return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_cart_header,parent,false));
            }
            else
            return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_cart_item,parent,false));
        }
        else
        {
            if(viewType == 0)
            {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_header_item, parent, false));
            }
            else
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position)
    {

        if(!isLoading)
        {
            if(position>0)
            {
                ViewHolder holder = (ViewHolder) viewHolder;
                holder.tvServiceName.setText(Utility.getCamelCase(list.get(position-1).OpCodeName!=null ? list.get(position-1).OpCodeName : list.get(position-1).Description));
                holder.tvPrice.setText("Rs. " + list.get(position-1).Price);
                holder.progressBar.setVisibility(list.get(position-1).isPending ? View.VISIBLE : View.GONE);
                holder.imgClose.setVisibility(list.get(position-1).isPending ? View.GONE : View.VISIBLE);

                if(editFlag==0)
                {
                    holder.imgClose.setVisibility(View.GONE);
                }


                holder.imgClose.setOnClickListener(v ->
                        {
                            if(iCheckoutAdapter!=null)
                            iCheckoutAdapter.deleteItem(list.get(position - 1), position - 1);
                        }
                        );

                if(position == list.size())
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0, Utility.dpSize(context,100));
                    holder.itemView.setLayoutParams(params);
                }
            }
            else
            {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                headerViewHolder.tvGarageName.setText(Utility.getCamelCase(garage.DealerName));
                headerViewHolder.tvAddress.setText(garage.Address1+ " " + garage.Address2);
                Picasso.with(context)
                        .load(garage.ImageUrl)
                        .into(headerViewHolder.imgGarage);
            }
        }
        else
        {

        }

    }

    @Override
    public int getItemCount() {
        return isLoading ? 10 : list.size()+1;
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvGarageName)
        TextView tvGarageName;

        @BindView(R.id.imgGarage)
        ImageView imgGarage;

        @BindView(R.id.tvAddress)
        TextView tvAddress;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
