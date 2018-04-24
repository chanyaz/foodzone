package com.maya.wcustomer.activities;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.maya.wcustomer.R;
import com.maya.wcustomer.constants.Constants;
import com.maya.wcustomer.fragments.home.HomeFragment;
import com.maya.wcustomer.interfaces.activities.IActivity;
import com.maya.wcustomer.utilities.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IActivity{


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgUser)
    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        initialize();

    }

    private void initialize()
    {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, HomeFragment.newInstance(null,null)).commit();

        Picasso.with(activity())
                .load(Utility.getString(Utility.getSharedPreferences(), Constants.USER_PHOTO_URL))

                .into(imgUser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

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
    public Activity activity() {
        return this;
    }
}
