package com.maya.wadmin.adapters.fragments.testdrive;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.testdrive.ITestDriveVehiclesAdapter;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by Gokul Kalagara on 2/1/2018.
 */

public class TestDriveVehiclesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    List<Vehicle> list;
    ITestDriveVehiclesAdapter iTestDriveVehiclesAdapter;
    int type = 0;
    String vehicleType;
    int salespersonPosition = -1;

    public TestDriveVehiclesAdapter(Context context, List<Vehicle> list, ITestDriveVehiclesAdapter iTestDriveVehiclesAdapter)
    {
        this.context = context;
        this.list = list;
        this.iTestDriveVehiclesAdapter = iTestDriveVehiclesAdapter;
    }

    public TestDriveVehiclesAdapter(int type,String vehicleType,Context context,List<Vehicle> list, ITestDriveVehiclesAdapter iTestDriveVehiclesAdapter)
    {
        this.type = type;
        this.context = context;
        this.vehicleType = vehicleType;
        this.list = list;
        this.iTestDriveVehiclesAdapter = iTestDriveVehiclesAdapter;
    }

    public TestDriveVehiclesAdapter(int type,String vehicleType,Context context,List<Vehicle> list, ITestDriveVehiclesAdapter iTestDriveVehiclesAdapter,int salespersonPosition)
    {
        this.type = type;
        this.context = context;
        this.vehicleType = vehicleType;
        this.list = list;
        this.iTestDriveVehiclesAdapter = iTestDriveVehiclesAdapter;
        this.salespersonPosition = salespersonPosition;
    }

    @Override
    public int getItemViewType(int position)
    {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;
        switch (type)
        {
            case 0:
                view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.test_drive_vehicle,parent,false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdi_vehicle_item,parent,false);
                PDIViewHolder pdiViewHolder = new PDIViewHolder(view);
                return pdiViewHolder;
            case 2://pdi vehicles
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdi_vehicle_item,parent,false);
                PDIViewHolder lotViewHolder = new PDIViewHolder(view);
                return lotViewHolder;
            case 3://pdi salesperson vehicles
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicles_of_salesperson_item,parent,false);
                SalesPersonVehiclesHolder salesPersonVehiclesHolder = new SalesPersonVehiclesHolder(view);
                return salesPersonVehiclesHolder;
            case 4:// lot vehicle
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_vehicle_item,parent,false);
                LotViewHolder lotViewHolder1 = new LotViewHolder(view);
                return lotViewHolder1;
            case 5: // salesperson lot vehicles
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicles_of_salesperson_item,parent,false);
                SalesPersonVehiclesHolder lotViewHolder2 = new SalesPersonVehiclesHolder(view);
                return lotViewHolder2;
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderRecyclerView, final int position)
    {
            if(type==0)
            {
                    ViewHolder holder = (ViewHolder) holderRecyclerView;
                    holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                    holder.tvSalesPerson.setText(list.get(position).AssignedTo);
                    holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                    holder.tvOther.setText(list.get(position).Model + " " + list.get(position).Year);
                    holder.tvMake.setText(list.get(position).Make);
                    holder.tvCustomerName.setText(list.get(position).CustomerName);
                    if (list.get(position).PhoneNumber == null) {
                        holder.tvCustomerPhone.setText("" + (111111111 + new Random().nextInt(888888888)));
                    } else {
                        holder.tvCustomerPhone.setText(list.get(position).PhoneNumber);
                    }

                    Picasso.with(context)
                            .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                            .into(holder.imgSalesPerson);


                    if (position == list.size() - 1) {
                        holder.bottomView.setVisibility(View.GONE);
                    } else {
                        holder.bottomView.setVisibility(View.VISIBLE);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iTestDriveVehiclesAdapter.onTestDriveVehicleClick(list.get(position), position);
                        }
                    });

                    holder.imgOption.setOnClickListener(click ->{
                        iTestDriveVehiclesAdapter.openOptions(list.get(position),position);
                    });


                    if (list.get(position).viewFullDetails) {
                        holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
                        holder.llhead1.setVisibility(View.VISIBLE);
                        holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
                    } else {
                        holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
                        holder.llhead1.setVisibility(View.GONE);
                        holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
                    }
            }
            else if(type == 1)
            {

                PDIViewHolder holder = (PDIViewHolder) holderRecyclerView;
                if(vehicleType.equals(Constants.MARK_FOR_PDI))
                {
                    holder.llSalesPerson.setVisibility(View.GONE);
                    holder.imgSalesPerson.setVisibility(View.GONE);
                    holder.tvMarkedDate.setVisibility(View.VISIBLE);
                    holder.llMarkForPDI.setVisibility(View.VISIBLE);
                    holder.tvAssignDate.setVisibility(View.GONE);

                }
                else if(vehicleType.equals(Constants.PDI_INCOMPLETE))
                {
                    holder.llSalesPerson.setVisibility(View.VISIBLE);
                    holder.imgSalesPerson.setVisibility(View.VISIBLE);
                    holder.tvMarkedDate.setVisibility(View.GONE);
                    holder.llMarkForPDI.setVisibility(View.GONE);
                    holder.tvAssignDate.setVisibility(View.VISIBLE);
                    final int value = list.get(position).pdiValue;
                    if(value<30)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FF8C8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<50)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FFAF8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<70)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#D6C28D"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<90)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                    }
                    else
                    {

                    }
                    holder.content.setProgress(value);

                }
                else if(vehicleType.equals(Constants.PDI_COMPLETE))
                {
                    holder.llSalesPerson.setVisibility(View.VISIBLE);
                    holder.imgSalesPerson.setVisibility(View.VISIBLE);
                    holder.tvMarkedDate.setVisibility(View.GONE);
                    holder.llMarkForPDI.setVisibility(View.GONE);
                    holder.tvAssignDate.setVisibility(View.VISIBLE);
                    holder.content.setProgress(100);
                    holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                    holder.tvAssignDate.setText("Completed: Yesteray, 04:32 PM");
                }
                else
                {

                }



                holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                holder.tvOther.setText(list.get(position).Model + " " + list.get(position).Year);
                holder.tvMake.setText(list.get(position).Make);

                if(list.get(position).AssignedTo!=null && list.get(position).AssignedTo.length()>0)
                {
                    holder.tvSalesPerson.setText(list.get(position).AssignedTo);
                }
                else
                {
                    holder.tvSalesPerson.setText("Jason Steve");
                }

                Picasso.with(context)
                        .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                        .into(holder.imgSalesPerson);


                if (position == list.size() - 1)
                {
                    holder.bottomView.setVisibility(View.GONE);
                }
                else
                {
                    holder.bottomView.setVisibility(View.VISIBLE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iTestDriveVehiclesAdapter.onTestDriveVehicleClick(list.get(position), position);
                    }
                });



                holder.tvInfo.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.onVehicleInfo(list.get(position),position);
                });

                holder.tvAssignForPDI.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.goToAsignPdi();
                });

                holder.imgSettings.setOnClickListener(v->
                {
                    iTestDriveVehiclesAdapter.openPDIForm(list.get(position),position);
                });


                if (list.get(position).viewFullDetails) {
                    holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
                    holder.llhead1.setVisibility(View.VISIBLE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
                    holder.llhead1.setVisibility(View.GONE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
                }




                // enf
            }
            else if(type == 2)
            {
                PDIViewHolder holder = (PDIViewHolder) holderRecyclerView;
                if(vehicleType.equals(Constants.MARK_FOR_PDI) || vehicleType.equals(Constants.DELIVERY_RECEIVED))
                {
                    holder.llSalesPerson.setVisibility(View.GONE);
                    holder.imgSalesPerson.setVisibility(View.GONE);
                    holder.tvMarkedDate.setVisibility(View.VISIBLE);
                    holder.llMarkForPDI.setVisibility(View.VISIBLE);
                    holder.tvAssignDate.setVisibility(View.GONE);
                    if(vehicleType.equals(Constants.DELIVERY_RECEIVED))
                    holder.tvMarkedDate.setText("Received: Yesterday 5:00 AM");

                }
                else
                {
                    holder.llSalesPerson.setVisibility(View.VISIBLE);
                    holder.imgSalesPerson.setVisibility(View.VISIBLE);
                    holder.tvMarkedDate.setVisibility(View.GONE);
                    holder.llMarkForPDI.setVisibility(View.GONE);
                    holder.tvAssignDate.setVisibility(View.VISIBLE);
                    final int value = list.get(position).pdiValue;
                    if(value<30)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FF8C8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<50)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FFAF8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<70)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#D6C28D"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<90)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                    }
                    else
                    {

                    }
                    holder.content.setProgress(value);
                }




                holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                holder.tvOther.setText(list.get(position).Model + " " + list.get(position).Year);
                holder.tvMake.setText(list.get(position).Make);

                if(list.get(position).AssignedTo!=null && list.get(position).AssignedTo.length()>0)
                {
                    holder.tvSalesPerson.setText(list.get(position).AssignedTo);
                }
                else
                {
                    holder.tvSalesPerson.setText("Jason Steve");
                }

                Picasso.with(context)
                        .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                        .into(holder.imgSalesPerson);


                if (position == list.size() - 1)
                {
                    holder.bottomView.setVisibility(View.GONE);
                }
                else
                {
                    holder.bottomView.setVisibility(View.VISIBLE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        iTestDriveVehiclesAdapter.onTestDriveVehicleClick(list.get(position), position);
                    }
                });



                holder.tvInfo.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.onVehicleInfo(list.get(position),position);
                });

                holder.tvAssignForPDI.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.goToAsignPdi();
                });

                holder.imgSettings.setOnClickListener(v->
                {
                    iTestDriveVehiclesAdapter.openPDIForm(list.get(position),position);
                });


                if (list.get(position).viewFullDetails) {
                    holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
                    holder.llhead1.setVisibility(View.VISIBLE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
                    holder.llhead1.setVisibility(View.GONE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
                }


                //end
            }
            else if(type==3)
            {

                SalesPersonVehiclesHolder holder = (SalesPersonVehiclesHolder) holderRecyclerView;

                if(vehicleType.equals(Constants.PDI_INCOMPLETE))
                {
                    final int value = list.get(position).pdiValue;
                    if(value<30)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FF8C8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<50)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FFAF8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<70)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#D6C28D"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<90)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                    }
                    else
                    {

                    }
                    holder.content.setProgress(value);
                }

                else
                {

                }



                holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                holder.tvOther.setText(list.get(position).Model + " " + list.get(position).Year);
                holder.tvMake.setText(list.get(position).Make);





                if (position == list.size() - 1)
                {
                    holder.bottomView.setVisibility(View.GONE);
                }
                else
                {
                    holder.bottomView.setVisibility(View.VISIBLE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        iTestDriveVehiclesAdapter.onVehicleInfo(list.get(position), position,salespersonPosition);
                    }
                });





                holder.imgSettings.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.openPDIForm(list.get(position),position);
                });


                if (list.get(position).viewFullDetails) {
                    holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
                    holder.llhead1.setVisibility(View.VISIBLE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
                    holder.llhead1.setVisibility(View.GONE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
                }



                //end
            }
            else if(type == 4)
            {
                LotViewHolder holder = (LotViewHolder) holderRecyclerView;
                if(vehicleType.equals(Constants.DELIVERY_RECEIVED))
                {
                    holder.llSalesPerson.setVisibility(View.GONE);
                    holder.imgSalesPerson.setVisibility(View.GONE);
                    holder.tvMarkedDate.setVisibility(View.VISIBLE);
                    holder.llMarkForPDI.setVisibility(View.VISIBLE);
                    holder.tvAssignDate.setVisibility(View.GONE);
                    if(vehicleType.equals(Constants.DELIVERY_RECEIVED))
                        holder.tvMarkedDate.setText("Received: Yesterday 5:00 AM");

                }
                else
                {
                    holder.llSalesPerson.setVisibility(View.VISIBLE);
                    holder.imgSalesPerson.setVisibility(View.VISIBLE);
                    holder.tvMarkedDate.setVisibility(View.GONE);
                    holder.llMarkForPDI.setVisibility(View.GONE);
                    holder.tvAssignDate.setVisibility(View.VISIBLE);
                    final int value = list.get(position).pdiValue;
                    if(value<30)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FF8C8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<50)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FFAF8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<70)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#D6C28D"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<90)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                    }
                    else
                    {

                    }

                    if(vehicleType.equals(Constants.PREPARING_FOR_LOT))
                    {
                        holder.content.setProgress(value);
                    }
                    else
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                        holder.content.setProgress(100);
                    }
                }




                holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                holder.tvOther.setText(list.get(position).Model + " " + list.get(position).Year);
                holder.tvMake.setText(list.get(position).Make);

                if(list.get(position).AssignedTo!=null && list.get(position).AssignedTo.length()>0)
                {
                    holder.tvSalesPerson.setText(list.get(position).AssignedTo);
                }
                else
                {
                    holder.tvSalesPerson.setText("Jason Steve");
                }

                Picasso.with(context)
                        .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                        .into(holder.imgSalesPerson);


                if (position == list.size() - 1)
                {
                    holder.bottomView.setVisibility(View.GONE);
                }
                else
                {
                    holder.bottomView.setVisibility(View.VISIBLE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        iTestDriveVehiclesAdapter.onTestDriveVehicleClick(list.get(position), position);
                    }
                });



                holder.tvInfo.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.onVehicleInfo(list.get(position),position);
                });

                holder.tvAssignForPDI.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.goToAssignPreparation();
                });

                holder.imgSettings.setOnClickListener(v->
                {
                    iTestDriveVehiclesAdapter.openLOTForm(list.get(position),position);
                });


                if (list.get(position).viewFullDetails) {
                    holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
                    holder.llhead1.setVisibility(View.VISIBLE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
                    holder.llhead1.setVisibility(View.GONE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
                }


                //end
            }
            else if(type==5)
            {
                SalesPersonVehiclesHolder holder = (SalesPersonVehiclesHolder) holderRecyclerView;

                if(vehicleType.equals(Constants.PREPARING_FOR_LOT))
                {
                    final int value = list.get(position).pdiValue;
                    if(value<30)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FF8C8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<50)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#FFAF8C"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<70)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#D6C28D"), PorterDuff.Mode.SRC_IN);
                    }
                    else if(value<90)
                    {
                        holder.content.getProgressDrawable().setColorFilter(Color.parseColor("#B6D68D"), PorterDuff.Mode.SRC_IN);
                    }
                    else
                    {

                    }
                    holder.content.setProgress(value);
                }

                else
                {

                }



                holder.tvVin.setText(list.get(position).Vin.toUpperCase());
                holder.imgVehicle.setImageResource(R.drawable.sample_image1);
                holder.tvOther.setText(list.get(position).Model + " " + list.get(position).Year);
                holder.tvMake.setText(list.get(position).Make);





                if (position == list.size() - 1)
                {
                    holder.bottomView.setVisibility(View.GONE);
                }
                else
                {
                    holder.bottomView.setVisibility(View.VISIBLE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        iTestDriveVehiclesAdapter.onVehicleInfo(list.get(position), position,salespersonPosition);
                    }
                });



                holder.imgSettings.setOnClickListener(v ->
                {
                    iTestDriveVehiclesAdapter.openLOTForm(list.get(position),position,salespersonPosition);
                });


                if (list.get(position).viewFullDetails) {
                    holder.llhead.setBackgroundResource(R.drawable.test_drive_item_selected);
                    holder.llhead1.setVisibility(View.VISIBLE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    holder.llhead.setBackgroundResource(R.drawable.corner_radius_white_8);
                    holder.llhead1.setVisibility(View.GONE);
                    holder.imgClick.setImageResource(R.drawable.ic_arrow_down);
                }
            }
            else
            {

            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvVin, tvMake, tvSalesPerson,tvOther, tvCustomerName, tvCustomerPhone;
        ImageView imgVehicle, imgOption,imgClick, imgSalesPerson;
        View bottomView;
        LinearLayout llhead,llhead1;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvVin= itemView.findViewById(R.id.tvVin);
            tvMake= itemView.findViewById(R.id.tvMake);
            tvSalesPerson = itemView.findViewById(R.id.tvSalesPerson);
            tvOther = itemView.findViewById(R.id.tvOther);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgSalesPerson = itemView.findViewById(R.id.imgSalesPerson);
            imgClick = itemView.findViewById(R.id.imgClick);
            imgOption = itemView.findViewById(R.id.imgOption);
            bottomView = itemView.findViewById(R.id.bottomView);
            llhead = itemView.findViewById(R.id.llhead);
            llhead1 = itemView.findViewById(R.id.llhead1);
        }
    }

    public class PDIViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvVin, tvMake, tvSalesPerson,tvOther, tvMarkedDate, tvInfo, tvAssignForPDI, tvAssignDate;
        ImageView imgVehicle, imgClick, imgSalesPerson, imgSettings;
        View bottomView;
        ProgressBar content;
        LinearLayout llhead,llhead1, llSalesPerson, llMarkForPDI;

        public PDIViewHolder(View itemView)
        {
            super(itemView);
            tvVin= itemView.findViewById(R.id.tvVin);
            tvMake= itemView.findViewById(R.id.tvMake);
            tvSalesPerson = itemView.findViewById(R.id.tvSalesPerson);
            tvOther = itemView.findViewById(R.id.tvOther);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgSalesPerson = itemView.findViewById(R.id.imgSalesPerson);
            imgClick = itemView.findViewById(R.id.imgClick);
            bottomView = itemView.findViewById(R.id.bottomView);
            llhead = itemView.findViewById(R.id.llhead);
            llhead1 = itemView.findViewById(R.id.llhead1);

            llSalesPerson = itemView.findViewById(R.id.llSalesPerson);
            llMarkForPDI = itemView.findViewById(R.id.llMarkForPDI);
            content = itemView.findViewById(R.id.content);
            imgSettings = itemView.findViewById(R.id.imgSettings);
            tvMarkedDate = itemView.findViewById(R.id.tvMarkedDate);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            tvAssignForPDI = itemView.findViewById(R.id.tvAssignForPDI);
            tvAssignDate = itemView.findViewById(R.id.tvAssignDate);



        }
    }

    public class LotViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvVin, tvMake, tvSalesPerson,tvOther, tvMarkedDate, tvInfo, tvAssignForPDI, tvAssignDate;
        ImageView imgVehicle, imgClick, imgSalesPerson, imgSettings;
        View bottomView;
        ProgressBar content;
        LinearLayout llhead,llhead1, llSalesPerson, llMarkForPDI;

        public LotViewHolder(View itemView)
        {
            super(itemView);
            tvVin= itemView.findViewById(R.id.tvVin);
            tvMake= itemView.findViewById(R.id.tvMake);
            tvSalesPerson = itemView.findViewById(R.id.tvSalesPerson);
            tvOther = itemView.findViewById(R.id.tvOther);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgSalesPerson = itemView.findViewById(R.id.imgSalesPerson);
            imgClick = itemView.findViewById(R.id.imgClick);
            bottomView = itemView.findViewById(R.id.bottomView);
            llhead = itemView.findViewById(R.id.llhead);
            llhead1 = itemView.findViewById(R.id.llhead1);

            llSalesPerson = itemView.findViewById(R.id.llSalesPerson);
            llMarkForPDI = itemView.findViewById(R.id.llMarkForPDI);
            content = itemView.findViewById(R.id.content);
            imgSettings = itemView.findViewById(R.id.imgSettings);
            tvMarkedDate = itemView.findViewById(R.id.tvMarkedDate);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            tvAssignForPDI = itemView.findViewById(R.id.tvAssignForPDI);
            tvAssignDate = itemView.findViewById(R.id.tvAssignDate);



        }
    }

    public class SalesPersonVehiclesHolder extends RecyclerView.ViewHolder
    {
        TextView tvVin, tvMake, tvOther, tvAssignDate;
        ImageView imgVehicle, imgClick , imgSettings;
        View bottomView;
        ProgressBar content;
        LinearLayout llhead,llhead1;

        public SalesPersonVehiclesHolder(View itemView)
        {
            super(itemView);
            tvVin= itemView.findViewById(R.id.tvVin);
            tvMake= itemView.findViewById(R.id.tvMake);
            tvOther = itemView.findViewById(R.id.tvOther);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgClick = itemView.findViewById(R.id.imgClick);
            bottomView = itemView.findViewById(R.id.bottomView);
            llhead = itemView.findViewById(R.id.llhead);
            llhead1 = itemView.findViewById(R.id.llhead1);
            content = itemView.findViewById(R.id.content);
            imgSettings = itemView.findViewById(R.id.imgSettings);
            tvAssignDate = itemView.findViewById(R.id.tvAssignDate);

        }
    }
}
