package com.maya.wadmin.fragments.lot.form;


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
import com.maya.wadmin.adapters.fragments.pdi.PDIChecklistAdapter;
import com.maya.wadmin.adapters.fragments.pdi.PDIInspectionAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.pdi.form.PdiFormFragment;
import com.maya.wadmin.interfaces.adapters.pdi.IPDIChecklistAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.CheckList;
import com.maya.wadmin.models.LotCheckList;
import com.maya.wadmin.models.PDIPreparation;
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
 * Use the {@link LotPreparationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LotPreparationFormFragment extends Fragment implements IFragment, IPDIChecklistAdapter {
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

    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvSalesPerson) TextView tvSalesPerson;
    @BindView(R.id.tvVin) TextView tvVin;
    @BindView(R.id.tvOther) TextView tvOther;
    @BindView(R.id.tvCancel) TextView tvCancel;
    @BindView(R.id.tvSave) TextView tvSave;

    @BindView(R.id.imgSalesPerson)
    ImageView imgSalesPerson;

    Vehicle vehicle = null;
    List<CheckList> list;
    PDIChecklistAdapter pdiChecklistAdapter;
    IPDIChecklistAdapter iPdiChecklistAdapter;
    LotCheckList lotCheckList;
    int result = 0;



    public LotPreparationFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LotPreparationFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LotPreparationFormFragment newInstance(String param1, String param2,String value) {
        LotPreparationFormFragment fragment = new LotPreparationFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static LotPreparationFormFragment newInstance(Vehicle vehicle, String value)
    {
        LotPreparationFormFragment fragment = new LotPreparationFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_lot_preparation_form, container, false);
        ButterKnife.bind(this,view);

        iPdiChecklistAdapter = this;



        if(getArguments()!=null)
        {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
            if(getArguments().getString("value")!=null)
            if(getArguments().getString("value").equals(Constants.MARK_FOR_PDI) || getArguments().getString("value").equals(Constants.PDI_INCOMPLETE) || getArguments().getString("value").equals(Constants.TEST_DRIVE) || getArguments().getString("value").equals(Constants.INVENTORY) )
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
            vehicle.VehicleGuid = "2a7c7fbe-cbdd-4334-bd6e-ef09625ab261";
        }

        Picasso.with(activity())
                .load(Constants.SAMPLE_OTHER_SALES_PERSON)
                .into(imgSalesPerson);
        tvSalesPerson.setText(vehicle.AssignedTo);
        tvVin.setText(vehicle.Vin);
        tvOther.setText(vehicle.Make+ "       "+vehicle.Model+ " " + vehicle.Year);

        if(Utility.isNetworkAvailable(activity()))
        {
            fetchLotPreparationData();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }

        tvCancel.setOnClickListener(click -> {
            activity().onBackPressed();
        });
        tvSave.setOnClickListener(click -> {
            if(Utility.isNetworkAvailable(activity()))
            {
                saveLOTData();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            }
        });



        return view;
    }

    private void saveLOTData()
    {
        if(lotCheckList == null)
        {
            showSnackBar(Constants.SOMETHING_WENT_WRONG,2);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_LOT_UPDATE_VALUES + lotCheckList.VehicleId

                +   "&isBillLading=" + lotCheckList.IsBillLading +
                    "&billRemarks=" + lotCheckList.BillRemarks +
                    "&isKeys_count=" + lotCheckList.IsKeysCount +
                    "&isOwnersManualPacket=" + lotCheckList.IsOwnersManualPacket +
                    "&isVinMatch=" + lotCheckList.IsVinMatch +
                    "&isShippingDamage=" + lotCheckList.IsShippingDamage +
                    "&shippingDamageRemarks=" + lotCheckList.ShippingDamageRemarks +
                    "&isMirrors=" + lotCheckList.IsMirrors +
                    "&isRemovalWrap=" + lotCheckList.IsRemovalWrap +
                    "&isUpdateShippingInvoice=" + lotCheckList.IsUpdateShippingInvoice +
                    "&isVehiclePackets=" + lotCheckList.IsVehiclePackets +
                    "&isPrintBarCode=" + lotCheckList.IsPrintBarCode +
                    "&isHandShippingInvoices=" + lotCheckList.IsHandShippingInvoices +
                    "&isInstallBarCodeStickers=" + lotCheckList.IsInstallBarCodeStickers +
                    "&isSeperateKeys=" + lotCheckList.IsSeperateKeys +
                    "&isFilePackets=" + lotCheckList.IsFilePackets


                +  "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);
                result = 0;
                for (int i =0;i<list.size();i++)
                {
                    result += list.get(i).value;
                }
                if(result==14)
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


    public void fetchLotPreparationData()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_LOT_PREPARATION + vehicle.VehicleGuid+  "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<LotCheckList>() {
                }.getType();
                lotCheckList = gson.fromJson(response, type);
                if(lotCheckList!=null)
                {
                    if(tvSave.getVisibility()==View.GONE)
                    {
                      lotCheckList = Utility.setAllPass(lotCheckList);
                    }

                    list = Utility.generateChecklistForPreparationForm(lotCheckList);
                    pdiChecklistAdapter = new PDIChecklistAdapter(list,activity(),iPdiChecklistAdapter);
                    if(tvSave.getVisibility()==View.GONE)
                    {
                        pdiChecklistAdapter.isClickable = false;
                    }
                    recyclerView.setAdapter(pdiChecklistAdapter);


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
    public void changeValue(CheckList checkList, int inspectionPosition, int position, int value)
    {
        list.get(position).value = value;
        switch (position)
        {
            case 0:
                lotCheckList.IsBillLading = value;
                break;
            case 1:
                lotCheckList.IsKeysCount = value;
                break;
            case 2:
                lotCheckList.IsOwnersManualPacket = value;
                break;
            case 3:
                lotCheckList.IsVinMatch = value;
                break;
            case 4:
                lotCheckList.IsShippingDamage = value;
                break;
            case 5:
                lotCheckList.IsMirrors = value;
                break;
            case 6:
                lotCheckList.IsRemovalWrap = value;
                break;
            case 7:
                lotCheckList.IsUpdateShippingInvoice = value;
                break;
            case 8:
                lotCheckList.IsVehiclePackets =  value;
                break;
            case 9:
                lotCheckList.IsPrintBarCode = value;
                break;
            case 10:
                lotCheckList.IsHandShippingInvoices = value;
                break;
            case 11:
                lotCheckList.IsInstallBarCodeStickers = value;
                break;
            case 12:
                lotCheckList.IsSeperateKeys = value;
                break;
            case 13:
                lotCheckList.IsFilePackets = value;
                break;
        }
        pdiChecklistAdapter.notifyDataSetChanged();
    }
}
