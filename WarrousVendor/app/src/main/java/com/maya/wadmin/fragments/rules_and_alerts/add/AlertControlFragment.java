package com.maya.wadmin.fragments.rules_and_alerts.add;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.wadmin.R;
import com.maya.wadmin.adapters.custom.TopBarAdapter;
import com.maya.wadmin.adapters.fragments.rules_and_alerts.ActionChannelAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.fragments.pdi.view.PDIViewFragment;
import com.maya.wadmin.fragments.rules_and_alerts.view.AddAlertFragment;
import com.maya.wadmin.fragments.vehicle.types.VehiclesTypeFragment;
import com.maya.wadmin.interfaces.adapters.rules_and_alerts.IActionChannelAdapter;
import com.maya.wadmin.interfaces.custom.ITopBarAdapter;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.AlertActionChannel;
import com.maya.wadmin.models.AlertRule;
import com.maya.wadmin.models.Operation;
import com.maya.wadmin.models.TopBarPanel;
import com.maya.wadmin.utilities.CommonApiCalls;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertControlFragment extends Fragment implements IFragment,ITopBarAdapter,IActionChannelAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recyclerViewTopBar) RecyclerView recyclerViewTopBar;

    ITopBarAdapter iITopBarAdapter;

    @BindView(R.id.llTopBarPanel) LinearLayout llTopBarPanel;
    @BindView(R.id.llMainHead) LinearLayout llMainHead;
    @BindView(R.id.tvTopBarItem) TextView tvTopBarItem;

    TopBarAdapter topBarAdapter;
    TopBarPanel topBarPanel;
    List<TopBarPanel> listTopBarPanel = Utility.getTopBarPanelElements(6);
    public int previous = 0;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    public List<AlertActionChannel> alertActionChannelList;

    @BindView(R.id.etAlertName) public EditText etAlertName;
    @BindView(R.id.etAlertDesc) public EditText etAlertDesc;

    AlertRule alertRule;

    IActionChannelAdapter iActionChannelAdapter;


    public AlertControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertControlFragment newInstance(String param1, String param2) {
        AlertControlFragment fragment = new AlertControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AlertControlFragment newInstance(int CategoryId, AlertRule alertRule) {
        AlertControlFragment fragment = new AlertControlFragment();
        Bundle args = new Bundle();
        args.putSerializable("AlertRule", alertRule);
        args.putInt("CategoryId",CategoryId);
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
        View view = inflater.inflate(R.layout.fragment_alert_control, container, false);
        ButterKnife.bind(this,view);

        iITopBarAdapter = this;
        iActionChannelAdapter = this;

        swipeRefreshLayout.setEnabled(false);
        tvTopBarItem.setText(listTopBarPanel.get(0).title);
        recyclerViewTopBar.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewTopBar.setAdapter(topBarAdapter = new TopBarAdapter(listTopBarPanel,activity(),iITopBarAdapter,1));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));


        tvTopBarItem.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.VISIBLE);
        });

        llTopBarPanel.setOnClickListener(click ->
        {
            llTopBarPanel.setVisibility(View.GONE);
        });

        if(getArguments().getSerializable("AlertRule")!=null)
        {
            alertRule = (AlertRule) getArguments().getSerializable("AlertRule");
            setUpUI();
        }

        return view;
    }

    public void setUpUI()
    {
        if(alertRule!=null)
        {
            etAlertName.setText(alertRule.AlertName);
            etAlertDesc.setText(alertRule.AlertDescription);
            itemClick(listTopBarPanel.get(getPositionOfCategory(getArguments().getInt("CategoryId",0))), getPositionOfCategory(getArguments().getInt("CategoryId",0)) );
            tvTopBarItem.setEnabled(false);
            fetchAlertActions(alertRule.AlertId);
        }

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }


    public void fetchAlertActions(final int alertId)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GENERATE_ACTION_CHANNEL_BASED_ALERT_ID + alertId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<AlertActionChannel>>() {
                }.getType();
                alertActionChannelList = gson.fromJson(response, type);
                if (alertActionChannelList != null)
                {
                   recyclerView.setAdapter(new ActionChannelAdapter(alertActionChannelList,activity(),iActionChannelAdapter));
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]", Constants.CONNECTION_ERROR);
                if(volleyError.networkResponse.statusCode == 401)
                {
                    CommonApiCalls.refreshAuthTokenCall();
                    fetchAlertActions(alertId);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    showSnackBar(Constants.CONNECTION_ERROR, 2);
                }
            }
        };
        volleyHelperLayer.startHandlerVolley(URL, null, listener, errorListener, Request.Priority.NORMAL, Constants.GET_REQUEST);


    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void itemClick(TopBarPanel topBarPanel, int position)
    {
        setUpAction(position);
        llTopBarPanel.setVisibility(View.GONE);
        if(previous == -1)
        {
            listTopBarPanel.get(position).isSelected = true;
        }
        else
        {
            listTopBarPanel.get(previous).isSelected = false;
            listTopBarPanel.get(position).isSelected = true;
        }
        previous = position;
        tvTopBarItem.setText(topBarPanel.title);
        topBarAdapter.notifyDataSetChanged();
    }

    private void setUpAction(int position)
    {
        if(previous == position)
        {
            return;
        }
        else
        {
            switch (position)
            {
                case 0:
                  break;
            }
        }
    }


    @Override
    public void editChannel(AlertActionChannel alertActionChannel, int position)
    {
        try
        {
            int value = 0;
            for (int i = 0; i < Constants.ALERT_TYPES.length; i++) {
                if (Constants.ALERT_TYPES[i].contains(alertActionChannel.Category)) {
                    value = Constants.ALERT_TYPE_IDS[i];
                }
            }
            AddAlertFragment addAlertFragment = (AddAlertFragment) getParentFragment();
            addAlertFragment.alertActionChannel = alertActionChannel;
            addAlertFragment.fetchOperations(value);
        }
        catch (Exception e)
        {

        }
    }


    public int getPositionOfCategory(int category)
    {
        for (int i=0;i<Constants.ALERT_TYPE_IDS.length;i++)
        {
            if(Constants.ALERT_TYPE_IDS[i] == category)
            {
                return i;
            }
        }
        return 0;
    }
}
