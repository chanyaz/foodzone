package com.test.foodzone.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;
import com.test.foodzone.R;
import com.test.foodzone.constants.Constants;
import com.test.foodzone.interfaces.activities.IActivity;
import com.test.foodzone.utils.Utility;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements IActivity{

    @BindView(R.id.kenBurnsView)
    KenBurnsView kenBurnsView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        sharedPreferences = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(Constants.U_UID))
        {
            Intent intent=new Intent(SplashActivity.this,StartActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, accelerateDecelerateInterpolator);
        kenBurnsView.setTransitionGenerator(generator);
        kenBurnsView.setImageResource(Constants.imagesSplash[(new Random().nextInt(3))]);
        kenBurnsView.setTransitionListener(new KenBurnsView.TransitionListener()
        {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }
        });

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent=new Intent(SplashActivity.this,HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void showSnackBar(String snackBarText, int type) {

    }

    @Override
    public Activity getActivity() {
        return SplashActivity.this;
    }
}
