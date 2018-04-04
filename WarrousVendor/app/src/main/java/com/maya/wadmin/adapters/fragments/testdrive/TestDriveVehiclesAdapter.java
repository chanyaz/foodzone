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

import butterknife.BindView;
import butterknife.ButterKnife;

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
                    holder.llMarkForPDI.setVisibility(View.GONE); // details of vehicle and assign for pdi
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

                holder.imgOption.setOnClickListener(v -> {
                    iTestDriveVehiclesAdapter.openOptions(list.get(position),position);
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
                    holder.llMarkForPDI.setVisibility(View.GONE); // vehicle info and assign for preparing
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

                holder.imgOption.setOnClickListener(v -> {
                    iTestDriveVehiclesAdapter.openOptions(list.get(position),position);
                });


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
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvMake) TextView tvMake;
        @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvCustomerName) TextView tvCustomerName;
        @BindView(R.id.tvCustomerPhone) TextView tvCustomerPhone;


        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.imgSalesPerson) ImageView imgSalesPerson;
        @BindView(R.id.imgOption) ImageView imgOption;

        @BindView(R.id.bottomView) View bottomView;

        @BindView(R.id.llhead) LinearLayout llhead;
        @BindView(R.id.llhead1) LinearLayout llhead1;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class PDIViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvMake) TextView tvMake;
        @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvMarkedDate) TextView tvMarkedDate;
        @BindView(R.id.tvInfo) TextView tvInfo;
        @BindView(R.id.tvAssignForPDI) TextView tvAssignForPDI;
        @BindView(R.id.tvAssignDate) TextView tvAssignDate;

        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.imgSalesPerson) ImageView imgSalesPerson;
        @BindView(R.id.imgSettings) ImageView imgSettings;
        @BindView(R.id.imgOption) ImageView imgOption;

        @BindView(R.id.bottomView) View bottomView;
        @BindView(R.id.content) ProgressBar content;

        @BindView(R.id.llhead) LinearLayout llhead;
        @BindView(R.id.llhead1) LinearLayout llhead1;
        @BindView(R.id.llSalesPerson) LinearLayout llSalesPerson;
        @BindView(R.id.llMarkForPDI) LinearLayout llMarkForPDI;

        public PDIViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class LotViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvMake) TextView tvMake;
        @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvMarkedDate) TextView tvMarkedDate;
        @BindView(R.id.tvInfo) TextView tvInfo;
        @BindView(R.id.tvAssignForPDI) TextView tvAssignForPDI;
        @BindView(R.id.tvAssignDate) TextView tvAssignDate;

        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.imgSalesPerson) ImageView imgSalesPerson;
        @BindView(R.id.imgSettings) ImageView imgSettings;
        @BindView(R.id.imgOption) ImageView imgOption;

        @BindView(R.id.bottomView) View bottomView;
        @BindView(R.id.content) ProgressBar content;

        @BindView(R.id.llhead) LinearLayout llhead;
        @BindView(R.id.llhead1) LinearLayout llhead1;
        @BindView(R.id.llSalesPerson) LinearLayout llSalesPerson;
        @BindView(R.id.llMarkForPDI) LinearLayout llMarkForPDI;

        public LotViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class SalesPersonVehiclesHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvVin) TextView tvVin;
        @BindView(R.id.tvMake) TextView tvMake;
        @BindView(R.id.tvOther) TextView tvOther;
        @BindView(R.id.tvAssignDate) TextView tvAssignDate;
        @BindView(R.id.imgVehicle) ImageView imgVehicle;
        @BindView(R.id.imgClick) ImageView imgClick;
        @BindView(R.id.imgSettings) ImageView imgSettings;
        @BindView(R.id.bottomView) View bottomView;
        @BindView(R.id.content) ProgressBar content;
        @BindView(R.id.llhead) LinearLayout llhead;
        @BindView(R.id.llhead1) LinearLayout llhead1;

        public SalesPersonVehiclesHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
