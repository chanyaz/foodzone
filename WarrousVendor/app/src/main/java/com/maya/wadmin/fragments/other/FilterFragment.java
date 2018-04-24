package com.maya.wadmin.fragments.other;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.other.FilterAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.adapters.other.IFilterAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.GroupFilter;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements IFragment,IFilterAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.recyclerViewYear) RecyclerView recyclerViewYear;
    @BindView(R.id.recyclerViewMake) RecyclerView recyclerViewMake;
    @BindView(R.id.recyclerViewModel) RecyclerView recyclerViewModel;
    @BindView(R.id.recyclerViewVehicleStatus) RecyclerView recyclerViewVehicleStatus;

    @BindView(R.id.tvFrom) TextView tvFrom;
    @BindView(R.id.tvTo) TextView tvTo;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    IFilterAdapter iFilterAdapter;
    FilterAdapter filterAdapterYear, filterAdapterMake, filterAdapterModel, filterAdapterVehicleStatus;
    GroupFilter groupFilter;


    String toDate = "", fromDate = "";

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FilterFragment newInstance(String groupFilter) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("GroupFilter", groupFilter);
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this,view);
        iFilterAdapter = this;


        setUp();


        if(Utility.isNetworkAvailable(activity()))
        {
            if(getArguments().getString("GroupFilter")==null)
            fetchGroupFilter();
            else
            {
                Gson gson = new Gson();
                Type type = new TypeToken<GroupFilter>(){}.getType();
                groupFilter = gson.fromJson(getArguments().getString("GroupFilter"),type);
                checkAll();
            }
        }
        else
        {
            activity().finish();
            Utility.showToast(activity(), Constants.PLEASE_CHECK_INTERNET,false);
        }



        return view;
    }

    private void fetchGroupFilter()
    {
        groupFilter = new GroupFilter();
        fetchGroupFilterYear();
        fetchGroupFilterMake();
        fetchGroupFilterModel();
        fetchGroupFilterVehicleStatus();
    }

    private void fetchGroupFilterVehicleStatus()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_VEHICLES_TYPE;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<GroupFilter.VehicleStatus>>() {
                }.getType();
                groupFilter.vehicleStatusList = gson.fromJson(response, type);
                checkAll();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    private void fetchGroupFilterModel()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_MODEL;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<GroupFilter.Model>>() {
                }.getType();
                groupFilter.modelList = gson.fromJson(response, type);
                checkAll();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    private void fetchGroupFilterMake()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_MAKE;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<GroupFilter.Make>>() {
                }.getType();
                groupFilter.makeList = gson.fromJson(response, type);
                checkAll();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    private void fetchGroupFilterYear()
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_YEAR;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<GroupFilter.Year>>() {
                }.getType();
                groupFilter.yearList = gson.fromJson(response, type);
                checkAll();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }

    public void checkAll()
    {
        if(groupFilter.modelList!=null && groupFilter.yearList!=null && groupFilter.vehicleStatusList!=null && groupFilter.makeList!=null)
        {

            recyclerViewYear.setAdapter(filterAdapterYear = new FilterAdapter(groupFilter,activity(),1,iFilterAdapter));
            recyclerViewMake.setAdapter(filterAdapterMake = new FilterAdapter(groupFilter,activity(),2,iFilterAdapter));
            recyclerViewModel.setAdapter(filterAdapterModel = new FilterAdapter(groupFilter,activity(),3,iFilterAdapter));
            recyclerViewVehicleStatus.setAdapter(filterAdapterVehicleStatus = new FilterAdapter(groupFilter,activity(),4,iFilterAdapter));
            progressBar.setVisibility(View.GONE);

            tvFrom.setEnabled(true);
            tvTo.setEnabled(true);
        }
        else
        {
            return;
        }

    }

    public void clearAll()
    {
        if(filterAdapterMake!=null && filterAdapterYear!=null && filterAdapterModel!=null && filterAdapterVehicleStatus!=null)
        {

            for(int i =0;i<groupFilter.yearList.size();i++)
            {
                groupFilter.yearList.get(i).isSelected = false;
            }
            for(int i =0;i<groupFilter.makeList.size();i++)
            {
                groupFilter.makeList.get(i).isSelected = false;
            }
            for(int i =0;i<groupFilter.modelList.size();i++)
            {
                groupFilter.modelList.get(i).isSelected = false;
            }
            for(int i =0;i<groupFilter.vehicleStatusList.size();i++)
            {
                groupFilter.vehicleStatusList.get(i).isSelected = false;
            }
            filterAdapterYear.notifyDataSetChanged();
            filterAdapterMake.notifyDataSetChanged();
            filterAdapterModel.notifyDataSetChanged();
            filterAdapterVehicleStatus.notifyDataSetChanged();
            showSnackBar("Reset all filters",2);
        }
        else
        {
            showSnackBar("Action not applied right now",2);
        }
    }

    private void setUp()
    {
        progressBar.setVisibility(View.GONE);

//        GridLayoutManager layoutManager = new GridLayoutManager(activity(), 4);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position)
//            {
//                if (position < 3)
//                return 1;
//                else
//                return 2;
//            }
//        });

        recyclerViewYear.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        //recyclerViewYear.setLayoutManager(layoutManager);
        recyclerViewMake.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewModel.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewVehicleStatus.setLayoutManager(new LinearLayoutManager(activity(),LinearLayoutManager.HORIZONTAL,false));


        tvFrom.setOnClickListener(click -> {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(activity(), new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String date = String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth) + "-"+ String.valueOf(year);
                    tvFrom.setText(date);
                    fromDate = date;
                }
            }, yy, mm, dd);
            datePicker.show();
        });

        tvTo.setOnClickListener(click -> {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(activity(), new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String date = String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth) + "-"+ String.valueOf(year);
                    tvTo.setText(date);
                    toDate = date;
                }
            }, yy, mm, dd);
            datePicker.show();
        });

        tvTo.setEnabled(false);
        tvFrom.setEnabled(false);
    }


    public void applyFilters()
    {
        if(filterAdapterMake!=null && filterAdapterYear!=null && filterAdapterModel!=null && filterAdapterVehicleStatus!=null)
        {

            String YearContent = "", MakeContent = "", ModelContent = "",VehicleStatusContent = "";

            for(int i =0;i<groupFilter.yearList.size();i++)
            {
                if(groupFilter.yearList.get(i).isSelected)
                {

                        YearContent += groupFilter.yearList.get(i).Years.trim().replaceAll("\n","")  + ",";

                }
            }
            YearContent = YearContent.length()>0? YearContent.substring(0,YearContent.length()-1) : YearContent;
            for(int i =0;i<groupFilter.makeList.size();i++)
            {
                if(groupFilter.makeList.get(i).isSelected)
                {

                        MakeContent += "'" + groupFilter.makeList.get(i).Make.trim().replaceAll("\n","")  + "',";

                }
            }
            MakeContent = MakeContent.length()>0? MakeContent.substring(0,MakeContent.length()-1) : MakeContent;
            for(int i =0;i<groupFilter.modelList.size();i++)
            {
                if(groupFilter.modelList.get(i).isSelected)
                {

                        ModelContent += "'" + groupFilter.modelList.get(i).Models.trim().replaceAll("\n","")  + "',";
                }
            }
            ModelContent = ModelContent.length()>0? ModelContent.substring(0,ModelContent.length()-1) : ModelContent;
            for(int i =0;i<groupFilter.vehicleStatusList.size();i++)
            {
                if(groupFilter.vehicleStatusList.get(i).isSelected)
                {

                        VehicleStatusContent += "'" + groupFilter.vehicleStatusList.get(i).Type.trim().replaceAll("\n","")  + "',";
                }
            }
            VehicleStatusContent = VehicleStatusContent.length()>0? VehicleStatusContent.substring(0,VehicleStatusContent.length()-1) : VehicleStatusContent;
            String content = "MakeId=" + MakeContent +"&ModelId=" + ModelContent +"&YearId="+ YearContent
                    +"&status="+ VehicleStatusContent +"&fromDate=" + fromDate + "&toDate=" + toDate;

            Intent returnData = new Intent(activity(),HelperActivity.class);
            returnData.putExtra("data","1");
            returnData.putExtra("content",content);
            Gson gson = new Gson();
            Type type = new TypeToken<GroupFilter>(){}.getType();
            returnData.putExtra("GroupFilter",gson.toJson(groupFilter,type));
            activity().setResult(Activity.RESULT_OK,returnData);
            activity().finish();
            return;

        }
        else
        {
            showSnackBar("Action not applied right now",2);
        }
    }

    @Override
    public void changeTitle(String title)
    {

    }

    @Override
    public void showSnackBar(String snackBarText, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity()
    {
        return getActivity();
    }

    @Override
    public void onItemClick(int type, int position)
    {
        switch (type)
        {
            case 1:
                groupFilter.yearList.get(position).isSelected = (groupFilter.yearList.get(position).isSelected == false)? true : false;
                break;
            case 2:
                groupFilter.makeList.get(position).isSelected = (groupFilter.makeList.get(position).isSelected == false)? true : false;
                break;
            case 3:
                groupFilter.modelList.get(position).isSelected = (groupFilter.modelList.get(position).isSelected == false)? true : false;
                break;
            case 4:
                groupFilter.vehicleStatusList.get(position).isSelected = (groupFilter.vehicleStatusList.get(position).isSelected == false)? true : false;
                break;
        }

        filterAdapterYear.notifyDataSetChanged();
        filterAdapterMake.notifyDataSetChanged();
        filterAdapterModel.notifyDataSetChanged();
        filterAdapterVehicleStatus.notifyDataSetChanged();

    }
}
