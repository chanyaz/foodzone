package com.maya.vgarages.fragments.appointments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.appointments.AppointmentsAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.dialogs.appointments.DeleteConfirmDialog;
import com.maya.vgarages.dialogs.appointments.RescheduleAppointmentDialog;
import com.maya.vgarages.interfaces.adapter.appointments.IAppointmentsAdapter;
import com.maya.vgarages.interfaces.dialog.IDeleteConfirmDialog;
import com.maya.vgarages.interfaces.dialog.IRescheduleAppointmentDialog;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsFragment extends Fragment implements IFragment, IAppointmentsAdapter, IDeleteConfirmDialog, IRescheduleAppointmentDialog {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    IAppointmentsAdapter iAppointmentsAdapter;
    IDeleteConfirmDialog iDeleteConfirmDialog;
    IRescheduleAppointmentDialog iRescheduleAppointmentDialog;
    List<Appointment> list;



    public AppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsFragment newInstance(String param1, String param2) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        ButterKnife.bind(this,view);
        iAppointmentsAdapter = this;
        iDeleteConfirmDialog = this;
        iRescheduleAppointmentDialog = this;

        initialize();
        return view;
    }

    private void initialize()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if(Utility.isNetworkAvailable(getActivity()))
            {
                fetchUserAppointments();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            }
        });

        if(Utility.isNetworkAvailable(getActivity()))
        {
            fetchUserAppointments();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }

    }

    private void fetchUserAppointments()
    {
        recyclerView.setAdapter(new AppointmentsAdapter(null,iAppointmentsAdapter,activity(),true));
        String URL = Constants.URL_USER_APPOINTMENTS + "?userId=" + Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Appointment>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    recyclerView.setAdapter(new AppointmentsAdapter(list,iAppointmentsAdapter,activity(),false));
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
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void onItemClick(Appointment appointment, int position)
    {
        ((HelperActivity) activity()).openAppointmentDetails(appointment);
    }

    @Override
    public void openOptions(Appointment appointment, int position, View view) {

        PopupMenu popup = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {
            popup = new PopupMenu(activity(),view, Gravity.END);
        }
        else
        {
            popup = new PopupMenu(activity(),view);
        }
        // Inflate the menu from xml
        popup.inflate(R.menu.app_options);
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_cancel:
                        if(appointment.AppointmentStatusType.equalsIgnoreCase(Constants.REJECTED))
                        {
                            showSnackBar("Already cancelled",2);
                            return true;
                        }
                        openCancelDialog(appointment);
                        return true;
                    case R.id.nav_reschedule:
                        if(appointment.AppointmentStatusType.equalsIgnoreCase(Constants.REJECTED))
                        {
                            showSnackBar("Cannot reschedule",2);
                            return true;
                        }
                        openRescheduleDialog(appointment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void openRescheduleDialog(Appointment appointment)
    {
        DisplayMetrics metrics = activity().getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.85);
        RescheduleAppointmentDialog dialog = new RescheduleAppointmentDialog(activity(),appointment,iRescheduleAppointmentDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void openCancelDialog(Appointment appointment)
    {
        DisplayMetrics metrics = activity().getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.85);
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(activity(),appointment,iDeleteConfirmDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void deleteAppointment(Appointment appointment)
    {
        if(!Utility.isNetworkAvailable(activity()))
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_CANCEL_APPOINTMENT + "?serviceAppointmentId=" + appointment.ServiceAppointmentId +
                "&appointmentType=Rejected" + "&rejectReason=User Cancelled";
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                showSnackBar("Cancelled appointment",1);
                fetchUserAppointments();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                progressBar.setVisibility(View.GONE);
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }


    @Override
    public void rescheduleAppointment(Appointment appointment, String date, String time)
    {
        if(!Utility.isNetworkAvailable(activity()))
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String dateValues[] = date.trim().split("-");
        String URL = Constants.URL_RESCHEDULE_APPOINTMENT + "?serviceAppointmentId=" + appointment.ServiceAppointmentId +
                "&apptDate=" +  dateValues[2]+ "-" + dateValues[1] + "-" + dateValues[0] + "&apptTime=" + time;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                showSnackBar("Cancelled appointment",1);
                fetchUserAppointments();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                progressBar.setVisibility(View.GONE);
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }
}
