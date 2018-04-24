package com.maya.vgarages.dialogs.options;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.activities.SplashActivity;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 4/24/2018.
 */
public class LogoutDialog extends Dialog
{
    @BindView(R.id.tvYes)
    TextView tvYes;

    @BindView(R.id.tvNo)
    TextView tvNo;


    Activity activity;
    public LogoutDialog(Activity activity)
    {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_dialog);
        ButterKnife.bind(this);

        tvNo.setOnClickListener(click -> dismiss());

        tvYes.setOnClickListener(click ->
        {
            dismiss();
            Utility.deleteSharedPreferences();
            Intent intent = new Intent(activity, SplashActivity.class);
            activity.startActivity(intent);
            activity.finish();
        });

    }
}
