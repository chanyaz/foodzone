package com.maya.vgarages.fragments.home;


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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.home.RemainderAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.home.IRemainderAdapter;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemainderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemainderFragment extends Fragment implements IFragment, IRemainderAdapter{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    IRemainderAdapter iRemainderAdapter;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    LatLng myLocation = new LatLng(17.439091, 78.399097);

    List<Garage> list;


    public RemainderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemainderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemainderFragment newInstance(String param1, String param2) {
        RemainderFragment fragment = new RemainderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static RemainderFragment newInstance(LatLng latLng) {
        RemainderFragment fragment = new RemainderFragment();
        Bundle args = new Bundle();
        args.putParcelable("Location", latLng);
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
        View view = inflater.inflate(R.layout.fragment_remainder, container, false);
        iRemainderAdapter = this;
        ButterKnife.bind(this,view);

        initialize();
        return view;
    }

    private void initialize()
    {
        if(getArguments().getParcelable("Location")!=null)
        {
            myLocation = getArguments().getParcelable("Location");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if(Utility.isNetworkAvailable(activity()))
            {
                fetchBookmarkGarages();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            }
        });



        if(Utility.isNetworkAvailable(activity()))
        fetchBookmarkGarages();
        else
        showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);

    }

    private void fetchBookmarkGarages()
    {
        recyclerView.setAdapter(new RemainderAdapter(Utility.generateRemainderGaragesList(),activity(),iRemainderAdapter,true));
        String URL = Constants.URL_GET_USER_BOOKMARK_GARAGES + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID) +
                "&pageCount=1&latitude="+myLocation.latitude+"&longitude="+myLocation.longitude;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Garage>>() {
                }.getType();
                list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    recyclerView.setAdapter(new RemainderAdapter(list,activity(),iRemainderAdapter,false));
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

    @Override
    public void itemClick(Garage garage, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2222);
        intent.putExtra("Garage",garage);
        startActivity(intent);
    }
}
