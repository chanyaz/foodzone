package com.maya.wcustomer.adapters.fragments.nearby;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.models.NearBy;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/19/2018.
 */

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder>
{

    List<NearBy> list;
    Context context;

    public NearByAdapter(List<NearBy> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.near_by_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.imgNearBy.setImageResource(list.get(position).nearByImage);


        Bitmap myBitmap = ((BitmapDrawable)holder.imgNearBy.getDrawable()).getBitmap();
        Bitmap newBitmap = Utility.addGradient(myBitmap, Constants.COLOR_START_CODES[position%5],Constants.COLOR_END_CODES[position%5]);
        holder.imgNearBy.setImageDrawable(new BitmapDrawable(context.getResources(), newBitmap));

        Shader shader = new LinearGradient(
                0,  0,0,
                holder.tvNearByName.getTextSize(),
                Color.parseColor(Constants.COLOR_START_CODES[position%5]),
                Color.parseColor(Constants.COLOR_END_CODES[position%5]),
                Shader.TileMode.REPEAT);

        holder.tvNearByName.getPaint().setShader(shader);
        holder.tvNearByName.setText(list.get(position).name);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utility.dpSize(context,90), ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context,5),list.size()-1==position ? Utility.dpSize(context,15) : 0,Utility.dpSize(context,5));
        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvNearByName)
        TextView tvNearByName;
        @BindView(R.id.imgNearBy)
        ImageView imgNearBy;
        @BindView(R.id.card)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
