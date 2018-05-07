package com.maya.vgarages.fragments.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.adapters.custom.EmptyDataAdapter;
import com.maya.vgarages.adapters.fragments.garage.services.GarageServicesAdapter;
import com.maya.vgarages.adapters.fragments.home.GaragesAdapter;
import com.maya.vgarages.adapters.fragments.home.RemainderAdapter;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.adapter.home.IGaragesAdapter;
import com.maya.vgarages.interfaces.fragments.IFragment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements IFragment, IGaragesAdapter{
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

    @BindView(R.id.recyclerViewSearch)
    RecyclerView recyclerViewSearch;

    @BindView(R.id.tvSearchRelated)
    TextView tvSearchRelated;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.imgClose)
    ImageView imgClose;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    List<Garage> listSearch;

    List<Garage> listRecommended;


    IGaragesAdapter iGaragesAdapter;

    LatLng myLocation = new LatLng(17.439091, 78.399097);


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchFragment newInstance(LatLng latLng) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        iGaragesAdapter = this;
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

        tvSearchRelated.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        imgClose.setOnClickListener(v -> {
            tvSearchRelated.setVisibility(View.GONE);
            Utility.hideKeyboard(activity());
            etSearch.setText("");
            progressBar.setVisibility(View.GONE);
            recyclerViewSearch.setAdapter(null);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(activity()));
       // recyclerView.setAdapter(new GaragesAdapter(Utility.generateRecommendedGaragesList(),activity(),2,iGaragesAdapter));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(Utility.isNetworkAvailable(activity()))
                {
                    s = s.toString().trim();
                    if (s.length() > 2)
                    {
                        searchGarages("" + s);
                    }
                    else if (s.length() == 0) {
                        recyclerViewSearch.setAdapter(null);
                        progressBar.setVisibility(View.GONE);
                        tvSearchRelated.setVisibility(View.GONE);
                    }
                    else
                    {
                        recyclerViewSearch.setAdapter(null);
                        progressBar.setVisibility(View.GONE);
                        tvSearchRelated.setVisibility(View.GONE);
                    }
                }
                else
                {
                    showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if(Utility.isNetworkAvailable(activity()))
            {
                generateRecommendedGarages();
            }
            else
            {
                showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
            }
        });
        if(Utility.isNetworkAvailable(activity()))
        generateRecommendedGarages();
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }
    }


    private void generateRecommendedGarages()
    {
        recyclerView.setAdapter(new GaragesAdapter(Utility.generateRecommendedGaragesList(),activity(),2,iGaragesAdapter,true));
        String URL = Constants.URL_RECOMMENDED_GARAGES + "?GarageType=all" +
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
                listRecommended = gson.fromJson(response, type);
                if(listRecommended!=null && listRecommended.size()>0)
                {
                    recyclerView.setAdapter(new GaragesAdapter(listRecommended,activity(),2,iGaragesAdapter,false));
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


    private void searchGarages(String content)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_FOR_SEARCH_GARAGES + "?latitude="+myLocation.latitude+"&longitude="+
                myLocation.longitude+"&searchParm="+content+"&pagecount=1";
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);


                tvSearchRelated.setText("Related to \""+ content +"\"");
                tvSearchRelated.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Garage>>() {
                }.getType();
                listSearch = gson.fromJson(response, type);
                if(listSearch!=null && listSearch.size()>0)
                {
                    if((""+etSearch.getText()).trim().length()>0)
                    recyclerViewSearch.setAdapter(new GaragesAdapter(listSearch,activity(),2,iGaragesAdapter,false));
                }
                else
                {
                    recyclerViewSearch.setAdapter(new EmptyDataAdapter(activity(),1,"No Result found. Please check the spelling or try a different search",R.drawable.search));
                }
                progressBar.setVisibility(View.GONE);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                recyclerViewSearch.setAdapter(new EmptyDataAdapter(activity(),0));
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
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
    public void itemClick(Garage garage, int position)
    {
        Intent intent = new Intent(activity(), HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2222);
        intent.putExtra("Garage",garage);
        startActivity(intent);

    }
}
