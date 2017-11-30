package com.test.foodzone.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.foodzone.R;
import com.test.foodzone.interfaces.activities.IActivity;
import com.test.foodzone.utils.Utility;

public class MainActivity extends AppCompatActivity implements IActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void changeTitle(String title)
    {

    }

    @Override
    public void showSnackBar(String snackBarText, int type)
    {

    }

    @Override
    public Activity getActivity() {
        return MainActivity.this;
    }
}
