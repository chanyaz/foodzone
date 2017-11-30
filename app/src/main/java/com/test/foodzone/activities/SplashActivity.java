package com.test.foodzone.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;
import com.test.foodzone.R;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.kenBurnsView)
    KenBurnsView kenBurnsView;
    int images[]= {R.drawable.d1,R.drawable.d2,R.drawable.d3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, accelerateDecelerateInterpolator);
        kenBurnsView.setTransitionGenerator(generator);
        kenBurnsView.setImageResource(images[(new Random().nextInt(3))]);
        kenBurnsView.setTransitionListener(new KenBurnsView.TransitionListener()
        {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }
        });

    }
}
