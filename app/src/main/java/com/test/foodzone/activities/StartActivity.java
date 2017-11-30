package com.test.foodzone.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.test.foodzone.R;
import com.test.foodzone.fragments.start.StartFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity
{

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, StartFragment.newInstance(null,null)).commit();
    }
}
