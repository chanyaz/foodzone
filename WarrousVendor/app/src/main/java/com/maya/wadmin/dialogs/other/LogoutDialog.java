package com.maya.wadmin.dialogs.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.maya.wadmin.R;
import com.maya.wadmin.activities.SplashActivity;
import com.maya.wadmin.utilities.Utility;

/**
 * Created by Gokul Kalagara on 3/5/2018.
 */

public class LogoutDialog extends Dialog
{
    TextView tvYes, tvNo;
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
        tvYes = findViewById(R.id.tvYes);
        tvNo = findViewById(R.id.tvNo);

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
