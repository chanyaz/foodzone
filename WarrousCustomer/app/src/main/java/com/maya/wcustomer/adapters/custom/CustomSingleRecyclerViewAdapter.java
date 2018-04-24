package com.maya.wcustomer.adapters.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.activities.HelperActivity;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.models.Action;
import com.maya.wcustomer.models.TimeBasedTrips;
import com.maya.wcustomer.models.Trip;
import com.maya.wcustomer.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 3/22/2018.
 */

public class CustomSingleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    int size = 0;
    int type = 0;
    List<TimeBasedTrips> timeBasedTripsList;
    List<Trip> tripList;
    List<Action> actionList;
    Context context;

    public CustomSingleRecyclerViewAdapter(int type,List<TimeBasedTrips> timeBasedTripsList, Context context)
    {
        this.type = type;
        this.size = timeBasedTripsList.size();
        this.timeBasedTripsList = timeBasedTripsList;
        this.context = context;
    }

    public CustomSingleRecyclerViewAdapter(List<Trip> tripList,Context context)
    {
        this.type = 1;
        this.size = tripList.size();
        this.tripList = tripList;
        this.context = context;
    }

    public CustomSingleRecyclerViewAdapter(Context context,List<Action> actionList,int type)
    {
        this.type = type;
        this.context = context;
        this.actionList = actionList;
        this.size = actionList.size();
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder = null;
        switch (type)
        {
            case 0:
                viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.violation_by_date_item,parent,false));
                break;
            case 1:
                viewHolder = new TripViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item,parent,false));
                break;
            case 2:
                viewHolder = new ParkingActionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item,parent,false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position)
    {
        switch (type)
        {
            case 0: // recent trips day (today, yesterday)

                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.tvDate.setText(timeBasedTripsList.get(position).date.toUpperCase());
                        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        viewHolder.recyclerView.setAdapter(new CustomSingleRecyclerViewAdapter(timeBasedTripsList.get(position).list,context));
                        viewHolder.recyclerView.setNestedScrollingEnabled(false);
                        viewHolder.recyclerView.setFocusable(false);

                break;

            case 1: // recent trips

                        TripViewHolder tripViewHolder = (TripViewHolder) holder;
                        tripViewHolder.tvDistance.setText(tripList.get(position).distance);
                        tripViewHolder.tvDuration.setText(tripList.get(position).duration);
                        tripViewHolder.tvStart.setText(tripList.get(position).startPlace);
                        tripViewHolder.tvEnd.setText(tripList.get(position).endPlace);
                        tripViewHolder.tvStartTime.setText(tripList.get(position).startTime);
                        tripViewHolder.tvEndTime.setText(tripList.get(position).endTime);
                        tripViewHolder.tvLocation.setText(tripList.get(position).tripName.toUpperCase());
                        tripViewHolder.llHead.setVisibility(tripList.size()-1 == position ? View.GONE : View.VISIBLE);

                        tripViewHolder.itemView.setOnClickListener(v -> goToTripDetails(tripList.get(position)));

                break;

            case 2: // parking adapter

                        ParkingActionViewHolder parkingActionViewHolder = (ParkingActionViewHolder) holder;
                        parkingActionViewHolder.imgAction.setImageResource(actionList.get(position).imgAction);
                        Bitmap myBitmap = ((BitmapDrawable)parkingActionViewHolder.imgAction.getDrawable()).getBitmap();
                        Bitmap newBitmap = Utility.addGradient(myBitmap,Constants.PARKING_COLOR_START_CODES[position%3],Constants.PARKING_COLOR_END_CODES[position%3]);
                        parkingActionViewHolder.imgAction.setImageDrawable(new BitmapDrawable(context.getResources(), newBitmap));

                        Shader shader = new LinearGradient(
                                0,  0,0,
                                parkingActionViewHolder.tvActionName.getTextSize(),
                                Color.parseColor(Constants.PARKING_COLOR_START_CODES[position%3]),
                                Color.parseColor(Constants.PARKING_COLOR_END_CODES[position%3]),
                                Shader.TileMode.REPEAT);

                        parkingActionViewHolder.tvActionName.getPaint().setShader(shader);
                        parkingActionViewHolder.tvActionName.setText(actionList.get(position).actionName);
                        int width = context.getResources().getDisplayMetrics().widthPixels;// - Utility.dpSize(context,30);
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                        //params.setMargins(0,0,0,0);
//                        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context,5),actionList.size()-1==position ? Utility.dpSize(context,15) : 0,Utility.dpSize(context,5));
//                        holder.itemView.setLayoutParams(params);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utility.dpSize(context,100), LinearLayout.LayoutParams.MATCH_PARENT);
                        params.setMargins(Utility.dpSize(context,15),Utility.dpSize(context,5),actionList.size()-1==position ? Utility.dpSize(context,15) : 0,Utility.dpSize(context,5));
                        holder.itemView.setLayoutParams(params);

                break;




        }

    }


    public void goToTripDetails(Trip trip)
    {
        Intent intent = new Intent(context, HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,22);
        context.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public int getItemCount() {
        return size;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class TripViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvStart)
        TextView tvStart;

        @BindView(R.id.tvEnd)
        TextView tvEnd;

        @BindView(R.id.tvStartTime)
        TextView tvStartTime;

        @BindView(R.id.tvEndTime)
        TextView tvEndTime;

        @BindView(R.id.tvLocation)
        TextView tvLocation;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.tvDuration)
        TextView tvDuration;

        @BindView(R.id.llHead)
        LinearLayout llHead;

        public TripViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class ParkingActionViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvActionName)
        TextView tvActionName;
        @BindView(R.id.imgAction)
        ImageView imgAction;
        public ParkingActionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
