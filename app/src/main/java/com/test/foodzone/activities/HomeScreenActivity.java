package com.test.foodzone.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.test.foodzone.R;
import com.test.foodzone.interfaces.activities.IActivity;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity implements IActivity {

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        toolbar.setTitle("Fudnow");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#909090"));







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity getActivity()
    {
        return HomeScreenActivity.this;
    }
}
