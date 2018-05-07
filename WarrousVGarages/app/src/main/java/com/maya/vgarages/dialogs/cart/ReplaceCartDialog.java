package com.maya.vgarages.dialogs.cart;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.interfaces.dialog.IReplaceCartDialog;
import com.maya.vgarages.models.Garage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/4/2018.
 */
public class ReplaceCartDialog extends Dialog
{
    Context context;
    Garage currentGarage, cartGarage;
    IReplaceCartDialog iReplaceCartDialog;

    @BindView(R.id.tvDetails)
    TextView tvDetails;

    @BindView(R.id.tvYes)
    TextView tvYes;

    @BindView(R.id.tvNo)
    TextView tvNo;

    public ReplaceCartDialog(Context context, IReplaceCartDialog iReplaceCartDialog, Garage currentGarage, Garage cartGarage)
    {
        super(context);
        this.context = context;
        this.currentGarage = currentGarage;
        this.cartGarage = cartGarage;
        this.iReplaceCartDialog = iReplaceCartDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_cart_dialog);
        ButterKnife.bind(this);


        initialize();
    }

    private void initialize()
    {
        tvDetails.setText("Your cart contains services from "+cartGarage.DealerName+". Do you want to discard the selection and add services from "+currentGarage.DealerName+"?");
        tvNo.setOnClickListener(v -> {
            this.dismiss();
        });

        tvYes.setOnClickListener(v -> {
            iReplaceCartDialog.replaceCart();
            this.dismiss();
        });
    }
}
