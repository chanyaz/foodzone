package com.maya.vgarages.fragments.cart.checkout;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.fragments.cart.CheckoutAdapter;
import com.maya.vgarages.interfaces.adapter.cart.ICheckoutAdapter;
import com.maya.vgarages.interfaces.dialog.IAppointmentDetailsDialog;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckOutFragment extends Fragment implements IFragment , ICheckoutAdapter ,IAppointmentDetailsDialog {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tvGarageName)
    TextView tvGarageName;

    @BindView(R.id.imgGarage)
    ImageView imgGarage;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Garage garage;

    @BindView(R.id.tvProceedToPay)
    TextView tvProceedToPay;

    @BindView(R.id.tvGSTTax)
    TextView tvGSTTax;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @BindView(R.id.tvItemsTotal)
    TextView tvItemsTotal;

    @BindView(R.id.tvTotalPay)
    TextView tvTotalPay;

    @BindView(R.id.tvAddAppointment)
    TextView tvAddAppointment;

    @BindView(R.id.llEditAppointment)
    LinearLayout llEditAppointment;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.tvEdit)
    TextView tvEdit;



    int totalPay = 0;

    ICheckoutAdapter iCheckoutAdapter;
    CheckoutAdapter checkoutAdapter;
    IAppointmentDetailsDialog iAppointmentDetailsDialog;

    boolean isPayment = false;

    Appointment appointment = null;


    DecimalFormat df = new DecimalFormat("0.0");




    public CheckOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckOutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckOutFragment newInstance(String param1, String param2) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static CheckOutFragment newInstance(Garage garage,Appointment appointment)
    {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        args.putSerializable("Garage", garage);
        args.putSerializable("Appointment", appointment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);
        iCheckoutAdapter = this;
        iAppointmentDetailsDialog = this;
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        df.setMaximumFractionDigits(1);
        if(getArguments().getSerializable("Garage")==null)
        {
           return;
        }


        garage = (Garage) getArguments().getSerializable("Garage");
        tvGarageName.setText(Utility.getCamelCase(garage.DealerName));
        tvAddress.setText(garage.Address1+ " " + garage.Address2);
        Picasso.with(activity())
                .load(garage.ImageUrl)
                .into(imgGarage);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));

        recyclerView.setAdapter(new CheckoutAdapter(((HelperActivity) activity()).cartGarageServices,iCheckoutAdapter,activity(),true));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                recyclerView.setAdapter(checkoutAdapter = new CheckoutAdapter(((HelperActivity) activity()).cartGarageServices,iCheckoutAdapter,activity(),false));
            }
        },1000);

        updateUI();
        tvProceedToPay.setOnClickListener(click ->
        {
            if(isPayment)
            ((HelperActivity)activity()).startPayment(Double.parseDouble(df.format(totalPay + (totalPay * 5 /100))));
            else
            {
                showSnackBar("Please add appointment",2);
            }
        });

        tvAddAppointment.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAppointmentDialog(null,iAppointmentDetailsDialog);
        });

        updateProceedToPay();

        frameLayout.setOnClickListener(v -> {});

        llEditAppointment.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAppointmentDialog(appointment,iAppointmentDetailsDialog);
        });

        tvDate.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAppointmentDialog(appointment,iAppointmentDetailsDialog);
        });

        tvEdit.setOnClickListener(v -> {
            ((HelperActivity) activity()).openAppointmentDialog(appointment,iAppointmentDetailsDialog);
        });

        if(getArguments().getSerializable("Appointment")!=null)
        {
            appointment = (Appointment) getArguments().getSerializable("Appointment");
            addAppointment(appointment);
        }


    }

    private void updateProceedToPay()
    {
        tvProceedToPay.setBackgroundColor(ContextCompat.getColor(activity(),isPayment? R.color.green_checkout :R.color.light_new_gray));
    }

    public void updateUI()
    {
        totalPay = 0;
        for(int i =0;i<((HelperActivity) activity()).cartGarageServices.size() ;i++)
        {
            totalPay += ((HelperActivity) activity()).cartGarageServices.get(i).Price;
        }


        tvItemsTotal.setText("Rs. "+df.format(totalPay));
        tvGSTTax.setText("Rs. "+df.format((totalPay * 5 /100)));
        tvTotalPay.setText("Rs. "+df.format(totalPay + (totalPay * 5 /100)));

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void deleteItem(GarageService garageService, int position)
    {

        ((HelperActivity)activity()).cartGarageServices.get(position).isPending = true;
        checkoutAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(((HelperActivity)activity()).cartGarageServices.size() == 0)
                {
                    checkoutAdapter.notifyDataSetChanged();
                    updateUI();
                    ((HelperActivity) activity()).refreshGarageServices();
                    ((HelperActivity) activity()).updateCart(((HelperActivity)activity()).cartGarageServices.size());
                    ((HelperActivity) activity()).saveCart();
                    activity().onBackPressed();
                    return;
                }

                ((HelperActivity)activity()).cartGarageServices.remove(position);
                checkoutAdapter.notifyDataSetChanged();
                updateUI();
                ((HelperActivity) activity()).refreshGarageServices();
                ((HelperActivity) activity()).updateCart(((HelperActivity)activity()).cartGarageServices.size());
                ((HelperActivity) activity()).saveCart();


                if(((HelperActivity)activity()).cartGarageServices.size() == 0)
                {
                    activity().onBackPressed();
                    return;
                }


            }
        },1000);



        //((HelperActivity)activity()).cartGarageServices.remove(position);


    }

    @Override
    public void addAppointment(Appointment appointment) {
        this.appointment = appointment;
        ((HelperActivity) activity()).appointment = appointment;
        llEditAppointment.setVisibility(View.VISIBLE);
        tvAddAppointment.setVisibility(View.GONE);
        tvDate.setText("On "+appointment.date);
        isPayment = true;
        updateProceedToPay();
    }
}
