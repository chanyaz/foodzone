package com.maya.vgarages.activities;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.fragments.garage.overview.GarageOverviewFragment;
import com.maya.vgarages.fragments.profile.ProfileFragment;
import com.maya.vgarages.interfaces.activities.IActivity;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.utilities.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperActivity extends AppCompatActivity implements IActivity {


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public boolean isBookmark = false;

    MenuItem menuAddReview, menuFav, menuCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);

        initialize();
        setUpFragment();
    }



    private void initialize()
    {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
    }

    private void setUpFragment()
    {
        int fragmentKey = getIntent().getIntExtra(Constants.FRAGMENT_KEY,0);
        Fragment fragment = null;
        switch (fragmentKey)
        {
            case 1111: // account
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    toolbar.setElevation(0);
                }
                fragment = ProfileFragment.newInstance(null,null);
                changeTitle("Account");
                break;
            case 2222: // garage overview
                fragment = GarageOverviewFragment.newInstance((Garage) getIntent().getSerializableExtra("Garage"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    toolbar.setElevation(1);
                }
                changeTitle("");
            break;
        }
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_fav:
                isBookmark = isBookmark?false:true;
                menuFav.setIcon(isBookmark ? R.drawable.ic_fill_love :R.drawable.ic_hallow_love);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        int fragmentKey = getIntent().getIntExtra(Constants.FRAGMENT_KEY,0);
        switch (fragmentKey)
        {
            case 1111:
                break;

            case 2222: // garage overview
                getMenuInflater().inflate(R.menu.garage_overview_menu,menu);
                menuFav = menu.findItem(R.id.menu_fav);
                menuAddReview = menu.findItem(R.id.menu_add_review);
                menuCart = menu.findItem(R.id.menu_cart);

                hideMenuOption(R.id.menu_add_review);
                hideMenuOption(R.id.menu_cart);

                break;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void changeTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(activity(),coordinatorLayout,snackBarText,type);
    }

    public void hideMenuOption(int id)
    {
        switch (id)
        {
            case R.id.menu_add_review:
                menuAddReview.setVisible(false);
                break;

            case R.id.menu_cart:
                menuCart.setVisible(false);
                break;

            case R.id.menu_fav:
                menuFav.setVisible(false);
                break;
        }
    }

    public void visibleMenuOption(int id)
    {
        switch (id)
        {
            case R.id.menu_add_review:
                menuAddReview.setVisible(true);
                break;

            case R.id.menu_cart:
                menuCart.setVisible(true);
                break;

            case R.id.menu_fav:
                menuFav.setVisible(true);
                break;
        }
    }

    @Override
    public Activity activity() {
        return this;
    }
}
