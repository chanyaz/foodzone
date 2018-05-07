package com.maya.vgarages.dialogs.garage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hsalf.smilerating.SmileRating;
import com.maya.vgarages.R;
import com.maya.vgarages.activities.HelperActivity;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.dialog.IAddReviewDialog;
import com.maya.vgarages.interfaces.dialog.IAddVehicleDialog;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/30/2018.
 */
public class AddReviewDialog extends Dialog
{
    Context context;

    @BindView(R.id.smileRating)
    SmileRating smileRating;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.etComment)
    EditText etComment;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    Garage garage;
    IAddReviewDialog iAddReviewDialog;

    int value = -1;

    public AddReviewDialog(Context context, Garage garage, IAddReviewDialog iAddReviewDialog) {
        super(context);
        this.context = context;
        this.iAddReviewDialog = iAddReviewDialog;
        this.garage = garage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review_dialog);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize()
    {
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected)
            {
                value = smiley;
            }
        });
        fab.setOnClickListener(v ->
        {
            if(value==-1)
            {
                showSnackBar("Please pick one",2);
                return;
            }
            if((""+etComment.getText()).trim().length()==0)
            {
                showSnackBar("Please add comment",2);
                return;
            }

            updateUserRatting();
        });
    }

    private void updateUserRatting()
    {
        fab.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        int ratting = (value +1) * 2;
        String URL = Constants.URL_ADD_REVIEW + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&dealerId=" + garage.DealerId + "&review="+ etComment.getText().toString().trim() +"&rating=" + ratting  +"&isEdit=0";
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Logger.d("[response]", response);

                progressBar.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                AddReviewDialog.this.dismiss();
                iAddReviewDialog.updateRatting();

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                progressBar.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(context,coordinatorLayout,snackBarText,type);
    }
}
