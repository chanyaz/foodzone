package com.maya.wadmin.fragments.scan;


import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.HelperActivity;
import com.maya.wadmin.adapters.fragments.testdrive.TestDriveVehiclesAdapter;
import com.maya.wadmin.apis.volley.VolleyHelperLayer;
import com.maya.wadmin.constants.Constants;
import com.maya.wadmin.interfaces.fragments.IFragment;
import com.maya.wadmin.models.Vehicle;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanVinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanVinFragment extends Fragment implements IFragment,ZXingScannerView.ResultHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.zXingScannerView)
    ZXingScannerView zXingScannerView;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    boolean isProcessing = true;


    public ScanVinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanVinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanVinFragment newInstance(String param1, String param2) {
        ScanVinFragment fragment = new ScanVinFragment();
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
        View view = inflater.inflate(R.layout.fragment_scan_vin, container, false);
        ButterKnife.bind(this,view);

        progressBar.setVisibility(View.GONE);

        setUp();
        return view;
    }

    private void setUp()
    {

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity activity()
    {
        return getActivity();
    }



    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleResult(Result result)
    {
        if(Utility.isNetworkAvailable(activity()))
        {
            progressBar.setVisibility(View.VISIBLE);
            if (isProcessing)
            {
                isProcessing = false;
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(700);
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_qrcode_detected);
                mediaPlayer.start();
                Logger.d(result.getText());
                fetchVehicleDetailsByVin(result.getText().trim());
            }
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }
    }

    public void fetchVehicleDetailsByVin(String data)
    {
        progressBar.setVisibility(View.VISIBLE);
        String URL = Constants.URL_GET_VEHICLE_DETAILS_BY_VIN + data.trim() + "&DealerId=" + Utility.getString(Utility.getSharedPreferences(),Constants.DEALER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Vehicle>>() {
                }.getType();
                List<Vehicle> list = gson.fromJson(response, type);
                if(list!=null && list.size()>0)
                {
                    ((HelperActivity) activity()).goToVehicleOverView(list.get(0));
                }
                else
                {
                    showSnackBar("No vehicle found by barcode",2);
                }

                isProcessing = true;
                onResume();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                progressBar.setVisibility(View.GONE);
                isProcessing = true;
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);

    }
}
