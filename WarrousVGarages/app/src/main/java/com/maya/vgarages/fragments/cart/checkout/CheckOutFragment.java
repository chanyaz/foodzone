package com.maya.vgarages.fragments.cart.checkout;


import android.app.Activity;
import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.fragments.cart.CheckoutAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.application.VGaragesApplication;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.cart.ICheckoutAdapter;
import com.maya.vgarages.interfaces.dialog.IAppointmentDetailsDialog;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.models.Cart;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.models.Vehicle;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        recyclerView.setAdapter(new CheckoutAdapter(garage,((HelperActivity) activity()).cartGarageServices,iCheckoutAdapter,activity(),true));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(((HelperActivity) activity()).cartGarageServices==null) return;
                recyclerView.setAdapter(checkoutAdapter = new CheckoutAdapter(garage,((HelperActivity) activity()).cartGarageServices,iCheckoutAdapter,activity(),false));
            }
        },500);

        updateUI();
        tvProceedToPay.setOnClickListener(click ->
        {
            if(isPayment)
            {
                if(totalPay + (totalPay * 5 / 100) <= 0)
                {
                    showSnackBar("Sorry, amount not be 0",2);
                    return;
                }
                ((HelperActivity) activity()).startPayment(Double.parseDouble(df.format(totalPay + (totalPay * 5 / 100))));
            }
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
        try
        {
            if(((HelperActivity) activity()).cartGarageServices==null) return;
            for (int i = 0; i < ((HelperActivity) activity()).cartGarageServices.size(); i++) {
                totalPay += ((HelperActivity) activity()).cartGarageServices.get(i).Price;
            }
            tvItemsTotal.setText("Rs. " + df.format(totalPay));
            tvGSTTax.setText("Rs. " + df.format((totalPay * 5 / 100)));
            tvTotalPay.setText("Rs. " + df.format(totalPay + (totalPay * 5 / 100)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
        if(((HelperActivity) activity()).cartGarageServices==null)
        return;

        ((HelperActivity)activity()).cartGarageServices.get(position).isPending = true;
        checkoutAdapter.notifyDataSetChanged();



        //((HelperActivity)activity()).cartGarageServices.remove(position);
        deleteOpcodeFromCart(garageService,position);


    }

    public void deleteOpcodeFromCart(GarageService garageService,int position)
    {
        if(garageService==null)
            return;

        String URL = Constants.URL_DELETE_CART_OPCODE +
                "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&dealerId=" + garageService.DealerId +
                "&opCodeId=" +  garageService.OpCodeId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                if(((HelperActivity)activity()).cartGarageServices.size() == 0)
                {
                    checkoutAdapter.notifyDataSetChanged();
                    updateUI();
                    ((HelperActivity) activity()).refreshGarageServices();
                    ((HelperActivity) activity()).updateCart(((HelperActivity)activity()).cartGarageServices.size());
                    activity().onBackPressed();
                    return;
                }

                ((HelperActivity)activity()).cartGarageServices.remove(position);
                checkoutAdapter.notifyDataSetChanged();
                updateUI();
                ((HelperActivity) activity()).refreshGarageServices();
                ((HelperActivity) activity()).updateCart(((HelperActivity)activity()).cartGarageServices.size());


                if(((HelperActivity)activity()).cartGarageServices.size() == 0)
                {
                    activity().onBackPressed();
                    return;
                }


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    @Override
    public void addAppointment(Appointment appointment) {
        if(Utility.isNetworkAvailable(activity())) {
            this.appointment = appointment;
            ((HelperActivity) activity()).appointment = appointment;
            createAppointment();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }

    }


    public void createAppointment()
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Vehicle>(){}.getType();
        Vehicle vehicle = gson.fromJson(Utility.getString(Utility.getSharedPreferences(),Constants.DEFAULT_CAR_DATA),type);
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_CREATE_APPOINTMENT;
        JSONObject input = new JSONObject();
        try
        {
            input.put("ServiceAppointmentId",0);
            input.put("VehicleId",vehicle.VehicleId);
            String dateValues[] = appointment.date.trim().split("-");
            input.put("ApptDate", dateValues[2]+ "-" + dateValues[1] + "-" + dateValues[0]);
            input.put("ApptTime",appointment.time );
            input.put("OdometerStatus", 0);
            input.put("OpCount",((HelperActivity) activity()).cartGarageServices.size() );
            input.put("ServiceAdvisorId",0);
            input.put("IsWait", false);
            input.put("TransportationOptionId", 0);
            input.put("CustomerId", Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID));
            input.put("PhoneNumber", Utility.getString(Utility.getSharedPreferences(),Constants.USER_PHONE_NUMBER));
            input.put("YearId", vehicle.MakeModelYearId);
            input.put("dealerId", garage.DealerId);
            input.put("opCodes", ((HelperActivity) activity()).generateCartOpCodes());
            input.put("AppointmentTypeId", appointment.pickUpType? 1: 2);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        final String requestBody = input.toString();
        Logger.d("[URL]", URL);
        Logger.d("[INPUT]", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Logger.d("[response]", response);
                        llEditAppointment.setVisibility(View.VISIBLE);
                        tvAddAppointment.setVisibility(View.GONE);
                        tvDate.setText("On "+appointment.date);
                        isPayment = true;
                        if(!appointment.pickUpType)
                        saveAddress(Utility.getString(Utility.getSharedPreferences(),Constants.USER_COMPLETE_ADDRESS).equalsIgnoreCase(appointment.address));
                        updateProceedToPay();
                        Utility.closeProgressDialog(progressDialog);
                        ((HelperActivity) activity()).showSuccessAppointmentStatus();
                    }
                }
                ,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Logger.d("[response]", "Unable to contact server");
                        showSnackBar(Constants.CONNECTION_ERROR,2);
                        Utility.closeProgressDialog(progressDialog);

                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try
                {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee)
                {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }



            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        VGaragesApplication.getInstance().getRequestQueue().add(stringRequest);
    }

    public void saveAddress(boolean isCurrent)
    {
        String URL = Constants.URL_SAVE_USER_ADDRESS ;
        JSONObject input = new JSONObject();
        try
        {
            input.put("Address1", isCurrent ? Utility.getString(Utility.getSharedPreferences(),Constants.USER_ADDRESS1) : appointment.address);
            input.put("Address2", isCurrent ? Utility.getString(Utility.getSharedPreferences(),Constants.USER_ADDRESS2) : "");
            input.put("City", isCurrent ? Utility.getString(Utility.getSharedPreferences(),Constants.USER_LOCALITY_ADDRESS) : "");
            input.put("PinCode", isCurrent ? Utility.getString(Utility.getSharedPreferences(),Constants.USER_PIN_CODE) : "");
            input.put("State", isCurrent ? Utility.getString(Utility.getSharedPreferences(),Constants.USER_ADMIN_AREA) : "");
            input.put("UserId", Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        final String requestBody = input.toString();
        Logger.d("[URL]", URL);
        Logger.d("[INPUT]", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Logger.d("[response]", response);
                    }
                }
                ,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Logger.d("[response]", "Unable to contact server");
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try
                {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee)
                {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }



            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        VGaragesApplication.getInstance().getRequestQueue().add(stringRequest);
    }
}
