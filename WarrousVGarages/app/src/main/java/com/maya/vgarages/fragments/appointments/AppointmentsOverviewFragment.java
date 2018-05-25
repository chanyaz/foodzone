package com.maya.vgarages.fragments.appointments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.cart.CheckoutAdapter;
import com.maya.vgarages.adapters.fragments.garage.reviews.ReviewAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.Review;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentsOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsOverviewFragment extends Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    Appointment appointment;
    Garage garage;



    public AppointmentsOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentsOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsOverviewFragment newInstance(String param1, String param2) {
        AppointmentsOverviewFragment fragment = new AppointmentsOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AppointmentsOverviewFragment newInstance(Appointment appointment) {
        AppointmentsOverviewFragment fragment = new AppointmentsOverviewFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        Type type = new TypeToken<Appointment>() {
        }.getType();
        args.putString("Appointment", gson.toJson(appointment,type));
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
        View view = inflater.inflate(R.layout.fragment_appointments_overview, container, false);
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        if(getArguments().getString("Appointment")==null)
        {
            return;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Appointment>() {
        }.getType();
        appointment = gson.fromJson(getArguments().getString("Appointment"), type);
        tvDate.setText(Utility.makeJSDateReadableOther(appointment.ApptDate));
        tvTime.setText(appointment.ApptTime);
        tvStatus.setText(appointment.AppointmentStatusType);
        tvStatus.setTextColor(ContextCompat.getColor(activity(),
                 appointment.AppointmentStatusType.equalsIgnoreCase(Constants.REJECTED)
                ? R.color.badge_color1
                : appointment.AppointmentStatusType.equalsIgnoreCase(Constants.ACCEPTED)
                ? R.color.app_snack_bar_true : R.color.light_primary));

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        if(Utility.isNetworkAvailable(activity()))
        {
            fetchAppointDetails();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }

    }

    private void fetchAppointDetails()
    {
        recyclerView.setAdapter(new CheckoutAdapter(null,null,activity(),true,0));
        String URL = Constants.URL_APPOINTMENT_DETAILS + "?serviceAppointmentId=" + appointment.ServiceAppointmentId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<Appointment>() {
                }.getType();
                appointment = gson.fromJson(response, type);
                if(appointment!=null && appointment.ICustomerOpcodes.size()>0)
                {
                    garage = new Garage();
                    garage.DealerId = appointment.DealerId;
                    garage.DealerName = appointment.DealerName;
                    garage.ImageUrl = appointment.ImageUrl;
                    garage.Address1 =  appointment.Make + " " +appointment.Model + " " + appointment.Year;
                    garage.Address2 = "";
                    recyclerView.setAdapter(new CheckoutAdapter(garage,appointment.ICustomerOpcodes,activity(),false,0));

                    tvDate.setText(Utility.makeJSDateReadableOther(appointment.ApptDate));
                    tvTime.setText(appointment.ApptTime);
                    tvStatus.setText(appointment.AppointmentStatusType);
                    tvStatus.setTextColor(ContextCompat.getColor(activity(),
                            appointment.AppointmentStatusType.equalsIgnoreCase(Constants.REJECTED)
                            ? R.color.badge_color1
                            : appointment.AppointmentStatusType.equalsIgnoreCase(Constants.ACCEPTED)
                            ? R.color.app_snack_bar_true : R.color.light_primary));
                }
                else
                {
                    recyclerView.setAdapter(new EmptyDataAdapter(activity(),1));
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                recyclerView.setAdapter(new EmptyDataAdapter(activity(),0));
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
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
}