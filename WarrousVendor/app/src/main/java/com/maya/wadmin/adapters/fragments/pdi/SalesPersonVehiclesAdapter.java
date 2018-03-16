package com.maya.wadmin.adapters.fragments.pdi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.testdrive.TestDriveVehiclesAdapter;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.ISalesPersonVehiclesAdapter;
import com.maya.wadmin.interfaces.fragments.testdrive.ITestDriveVehiclesAdapter;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gokul Kalagara on 2/13/2018.
 */

public class SalesPersonVehiclesAdapter extends RecyclerView.Adapter<SalesPersonVehiclesAdapter.ViewHolder> implements ITestDriveVehiclesAdapter
{

    List<SalesPerson> list;
    Context context;
    ITestDriveVehiclesAdapter iTestDriveVehiclesAdapter;
    TestDriveVehiclesAdapter testDriveVehiclesAdapter;
    int previous = -1;
    int type = 0;
    ISalesPersonVehiclesAdapter iSalesPersonVehiclesAdapter;


    public SalesPersonVehiclesAdapter(List<SalesPerson> list, Context context,ISalesPersonVehiclesAdapter iSalesPersonVehiclesAdapter) {
        this.list = list;
        this.context = context;
        iTestDriveVehiclesAdapter = this;
        this.iSalesPersonVehiclesAdapter = iSalesPersonVehiclesAdapter;
    }

    public SalesPersonVehiclesAdapter(int type,List<SalesPerson> list, Context context,ISalesPersonVehiclesAdapter iSalesPersonVehiclesAdapter) {

        this.list = list;
        this.context = context;
        this.type = type;
        iTestDriveVehiclesAdapter = this;
        this.iSalesPersonVehiclesAdapter = iSalesPersonVehiclesAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.salesperson_vehicles_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        holder.tvSalesPerson.setText(list.get(position).Name);
        Picasso.with(context)
                .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                .into(holder.imgSalesPerson);

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setFocusable(false);
        holder.recyclerView.setFocusableInTouchMode(false);
        holder.recyclerView.setVisibility(list.get(position).itemClicked?View.VISIBLE:View.GONE);
        holder.tvSalesPerson.setTextColor(Color.parseColor(list.get(position).itemClicked?"#C4172C":"#58595B"));

        holder.tvSalesPerson.setOnClickListener(click -> {
            iSalesPersonVehiclesAdapter.itemClicked(list.get(position),position);
        });

        if(type == 0)
        {
            holder.recyclerView.setAdapter(testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(3, Constants.PDI_INCOMPLETE, context, list.get(position).LstVehicle, iTestDriveVehiclesAdapter, position));
        }
        else if(type == 7891012 || type == 789)
        {
            holder.recyclerView.setAdapter(testDriveVehiclesAdapter = new TestDriveVehiclesAdapter(5,Constants.PREPARING_FOR_LOT, context,list.get(position).LstVehicle,iTestDriveVehiclesAdapter,position));
        }
        else
        {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onTestDriveVehicleClick(Vehicle vehicle, int position) {

    }

    @Override
    public void onVehicleInfo(Vehicle vehicle, int position) {

    }

    @Override
    public void onVehicleInfo(Vehicle vehicle, int position,int salespersonPosition)
    {
        for (int i = 0; i<list.get(salespersonPosition).LstVehicle.size();i++)
        {
            if(i==position)
            {
                list.get(salespersonPosition).LstVehicle.get(i).viewFullDetails = list.get(salespersonPosition).LstVehicle.get(i).viewFullDetails? false :true;
            }
            else
            list.get(salespersonPosition).LstVehicle.get(i).viewFullDetails = false;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void openLOTForm(Vehicle vehicle, int position, int salespersonPosition)
    {
        vehicle.AssignedTo = list.get(salespersonPosition).Name;
        Intent intent = new Intent(context, HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,223);
        intent.putExtra("vehicle",vehicle);
        intent.putExtra("value",Constants.PREPARING_FOR_LOT);
        context.startActivity(intent);
    }

    @Override
    public void openPDIForm(Vehicle vehicle, int position, int salespersonPosition) {

        vehicle.AssignedTo = list.get(salespersonPosition).Name;
        Intent intent = new Intent(context, HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,222);
        intent.putExtra("vehicle",vehicle);
        intent.putExtra("value",Constants.PDI_INCOMPLETE);
        context.startActivity(intent);
    }

    @Override
    public void goToAsignPdi() {

    }

    @Override
    public void goToAssignPreparation() {

    }

    @Override
    public void openPDIForm(Vehicle vehicle, int position)
    {
        Intent intent = new Intent(context, HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,222);
        intent.putExtra("vehicle",vehicle);
        intent.putExtra("value",Constants.PDI_INCOMPLETE);
        context.startActivity(intent);
    }

    @Override
    public void openLOTForm(Vehicle vehicle, int position)
    {
        Logger.d("OPEN LOT FORM");
        Intent intent = new Intent(context, HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,223);
        intent.putExtra("vehicle",vehicle);
        intent.putExtra("value",Constants.PREPARING_FOR_LOT);
        context.startActivity(intent);
    }

    @Override
    public void openOptions(Vehicle vehicle, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSalesPerson;
        RecyclerView recyclerView;
        ImageView imgSalesPerson;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvSalesPerson= itemView.findViewById(R.id.tvSalesPerson);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            imgSalesPerson = itemView.findViewById(R.id.imgSalesPerson);
        }
    }
}
