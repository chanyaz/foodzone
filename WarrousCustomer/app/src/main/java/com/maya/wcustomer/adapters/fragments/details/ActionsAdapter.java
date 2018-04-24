package com.maya.wcustomer.adapters.fragments.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.models.Action;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/15/2018.
 */

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder>
{

    List<Action> list;
    Context context;


    public ActionsAdapter(List<Action> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.imgAction.setImageResource(list.get(position).imgAction);


        Bitmap myBitmap = ((BitmapDrawable)holder.imgAction.getDrawable()).getBitmap();
        Bitmap newBitmap = Utility.addGradient(myBitmap,Constants.COLOR_START_CODES[position%3],Constants.COLOR_END_CODES[position%3]);
        holder.imgAction.setImageDrawable(new BitmapDrawable(context.getResources(), newBitmap));

        Shader shader = new LinearGradient(
                0,  0,0,
                holder.tvActionName.getTextSize(),
                Color.parseColor(Constants.COLOR_START_CODES[position%3]),
                Color.parseColor(Constants.COLOR_END_CODES[position%3]),
                Shader.TileMode.REPEAT);

        holder.tvActionName.getPaint().setShader(shader);
        holder.tvActionName.setText(list.get(position).actionName);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utility.dpSize(context,100), LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context,5),list.size()-1==position ? Utility.dpSize(context,15) : 0,Utility.dpSize(context,5));
        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvActionName)
        TextView tvActionName;
        @BindView(R.id.imgAction)
        ImageView imgAction;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
