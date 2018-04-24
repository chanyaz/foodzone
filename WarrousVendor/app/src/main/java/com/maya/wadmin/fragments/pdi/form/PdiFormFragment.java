package com.maya.wadmin.fragments.pdi.form;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.pdi.AssignTechnicianPDIAdapter;
import com.maya.wadmin.adapters.fragments.pdi.PDIInspectionAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.pdi.IPDIInspectionAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.CheckList;
import com.maya.wadmin.models.Inspection;
import com.maya.wadmin.models.PDIPreparation;
import com.maya.wadmin.models.SalesPerson;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PdiFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PdiFormFragment extends Fragment implements IFragment , IPDIInspectionAdapter {
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

    List<Inspection> list;
    IPDIInspectionAdapter ipdiInspectionAdapter;
    PDIInspectionAdapter pdiInspectionAdapter;
    int previous = -1;

    PDIPreparation pdiPreparation;

    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
    @BindView(R.id.tvVin) TextView tvVin;
    @BindView(R.id.tvOther) TextView tvOther;
    @BindView(R.id.tvCancel) TextView tvCancel;
    @BindView(R.id.tvSave) TextView tvSave;
    @BindView(R.id.imgSalesPerson) ImageView imgSalesPerson;

    Vehicle vehicle;
    int result = 0;


    public PdiFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PdiFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PdiFormFragment newInstance(String param1, String param2, String value) {
        PdiFormFragment fragment = new PdiFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static PdiFormFragment newInstance(Vehicle vehicle, String value)
    {
        PdiFormFragment fragment = new PdiFormFragment();
        Bundle args = new Bundle();
        args.putSerializable("vehicle", vehicle);
        args.putString("value",value);
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
        View view = inflater.inflate(R.layout.fragment_pdi_form, container, false);
        ButterKnife.bind(this,view);
        ipdiInspectionAdapter =this;

        if(getArguments()!=null)
        {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
            if(getArguments().getString("value")!=null)
            if(getArguments().getString("value").equals(Constants.PDI_COMPLETE))
            {
              tvSave.setVisibility(View.GONE);
            }
        }
        swipeRefreshLayout.setEnabled(false);


        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerView.setFocusable(false);
        progressBar.setVisibility(View.GONE);

        if(vehicle==null)
        {
            vehicle = Utility.generateSampleVehicle();
        }

        Picasso.with(activity())
                .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                .into(imgSalesPerson);
        tvSalesPerson.setText(vehicle.AssignedTo);
        tvVin.setText(vehicle.Vin);
        tvOther.setText(vehicle.Make+ "       "+vehicle.Model+ " " + vehicle.Year);




        if(Utility.isNetworkAvailable(activity()))
        {
            fetchPDIPreparationData();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

        tvCancel.setOnClickListener(click -> {activity().onBackPressed();});
        tvSave.setOnClickListener(click -> {
            if(Utility.isNetworkAvailable(activity()))
            {
                savePDIData();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        });



        return view;
    }

    private void savePDIData()
    {
        if(pdiPreparation == null)
        {
            showSnackBar(Constants.SOMETHING_WENT_WRONG,2);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String value = "";
        result = 0;
         for(Inspection inspection : list)
        {
            for (CheckList checkList: inspection.checkLists)
            {
                value += checkList.value + ",";
                result += checkList.value;
            }
        }
        result+=4;
        value += "1,1,1,1";
        String URL = Constants.URL_PDI_UPDATE_VALUES + pdiPreparation.PdiPreparationId + "&inspectionValues=" + value +  "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                if(result==100)
                {
                    Intent returnData = new Intent(activity(),HelperActivity.class);
                    returnData.putExtra("data","1");
                    activity().setResult(Activity.RESULT_OK,returnData);
                    activity().finish();
                }
                else
                {
                    showSnackBar("Successfully saved",1);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }


    public void fetchPDIPreparationData()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_PDI_PREPARATION + vehicle.VehicleGuid+  "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<PDIPreparation>() {
                }.getType();
                pdiPreparation = gson.fromJson(response, type);
                if(pdiPreparation!=null)
                {
                    if(tvSave.getVisibility()==View.GONE)
                    {
                        pdiPreparation.InspectionValues = Constants.ALL_TRUE_INSPECTION;
                    }
                    list = Utility.getListInspectionWithChecklist(pdiPreparation);
                    pdiInspectionAdapter = new PDIInspectionAdapter(list,activity(),ipdiInspectionAdapter);
                    if(tvSave.getVisibility()==View.GONE)
                    {
                        pdiInspectionAdapter.isClickable = false;
                    }
                    recyclerView.setAdapter(pdiInspectionAdapter);
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
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

    @Override
    public void ItemClick(Inspection inspection, int position)
    {
        if(previous == -1)
        {
            list.get(position).isOpenFlag = true;
        }
        else if(previous == position)
        {
            if(list.get(position).isOpenFlag == false)
                list.get(position).isOpenFlag = true;
            else
            {
                list.get(position).isOpenFlag = false;
            }
        }
        else
        {
            list.get(previous).isOpenFlag = false;
            list.get(position).isOpenFlag = true;
        }
        previous = position;
        pdiInspectionAdapter.notifyDataSetChanged();



    }

    @Override
    public void changeValue(CheckList checkList, int inspectionPosition, int position, int value) {
        list.get(inspectionPosition).checkLists.get(position).value = value;
        pdiInspectionAdapter.notifyDataSetChanged();
    }
}
