package com.maya.vgarages.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maya.vgarages.R;
import com.maya.vgarages.fragments.appointments.AppointmentsOverviewFragment;
import com.maya.vgarages.apis.volley.VolleyHelperLayer;
import com.maya.vgarages.constants.Constants;
import com.maya.vgarages.customviews.BadgeDrawable;
import com.maya.vgarages.dialogs.cart.ReplaceCartDialog;
import com.maya.vgarages.dialogs.checkout.AppointmentDetailsDialog;
import com.maya.vgarages.dialogs.garage.AddReviewDialog;
import com.maya.vgarages.dialogs.garage.ToPickVehicle;
import com.maya.vgarages.dialogs.vehicle.AddVehicleDialog;
import com.maya.vgarages.fragments.cart.checkout.CheckOutFragment;
import com.maya.vgarages.fragments.garage.overview.GarageOverviewFragment;
import com.maya.vgarages.fragments.profile.ProfileFragment;
import com.maya.vgarages.fragments.transactions.TransactionsFragment;
import com.maya.vgarages.fragments.vehicle.add.AddVehicleFragment;
import com.maya.vgarages.interfaces.activities.IActivity;
import com.maya.vgarages.interfaces.dialog.IAddReviewDialog;
import com.maya.vgarages.interfaces.dialog.IAddVehicleDialog;
import com.maya.vgarages.interfaces.dialog.IAppointmentDetailsDialog;
import com.maya.vgarages.interfaces.dialog.IReplaceCartDialog;
import com.maya.vgarages.interfaces.dialog.IToPickVehicle;
import com.maya.vgarages.models.Appointment;
import com.maya.vgarages.models.Garage;
import com.maya.vgarages.models.GarageService;
import com.maya.vgarages.models.Vehicle;
import com.maya.vgarages.utilities.Logger;
import com.maya.vgarages.utilities.Utility;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperActivity extends AppCompatActivity implements IActivity, PaymentResultListener, IAddReviewDialog {


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.llClose)
    LinearLayout llClose;

    @BindView(R.id.frameLayoutBottom)
    FrameLayout frameLayoutBottom;

   // public Cart cart;
    public Garage cartGarage;
    public Appointment appointment;



    @BindView(R.id.fragment_bottom_sheet)
    FrameLayout bottomSheetLayout;

    BottomSheetBehavior bottomSheetBehavior;

    GarageOverviewFragment garageOverviewFragment;


    public List<GarageService> cartGarageServices = new ArrayList<>();

    public boolean isBookmark = false;

    MenuItem menuAddReview, menuFav, menuCart;

    IAddReviewDialog iAddReviewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);

        initialize();
        setUpFragment();
    }



    private void initialize()
    {
        iAddReviewDialog = this;
        //cart = generateCart();


        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        llClose.setOnClickListener(click -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });

        Checkout.preload(getApplicationContext());


    }

    public void refreshCart()
    {
//        cartGarageServices = cart.listServices;
//        cartGarage = cart.garage;

        if(cartGarageServices!=null)
        updateCart(cartGarageServices.size());
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
                if(Utility.isNetworkAvailable(activity()))
                {
                    fetchOpcodesOfCart();
                }
                else
                {
                    finish();
                }
                fragment = garageOverviewFragment =  GarageOverviewFragment.newInstance((Garage) getIntent().getSerializableExtra("Garage"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    toolbar.setElevation(1);
                }
                changeTitle("");
            break;

            case 2223: // user vehicles
                fragment =  ProfileFragment.newInstance(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    toolbar.setElevation(1);
                }
                changeTitle("");
                break;
            case 2224:
                fragment =  AppointmentsOverviewFragment.newInstance(null,null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    toolbar.setElevation(1);
                }
                changeTitle("My Appointments");
                break;
            case 2225:
                fragment =  TransactionsFragment.newInstance(null,null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    toolbar.setElevation(1);
                }
                changeTitle("Transactions");
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
            case R.id.menu_cart:
                openCartCheckout();
                break;

            case R.id.menu_add_review:
                openAddReviewDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCartCheckout()
    {
        if(!Utility.getSharedPreferences().contains(Constants.DEFAULT_CAR_DATA))
        {
            pickVehicleDialog(garageOverviewFragment.iToPickVehicle);
            return;
        }
        if(cartGarageServices == null || cartGarageServices.size()==0)
        {
            garageOverviewFragment.showSnackBar("Cart is empty",2);
            return;
        }
        llClose.setVisibility(View.GONE);
        frameLayoutBottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutBottom, CheckOutFragment.newInstance(cartGarage,appointment)).commit();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void openAddVehicle()
    {
        llClose.setVisibility(View.VISIBLE);
        frameLayoutBottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutBottom, AddVehicleFragment.newInstance(null,null)).commit();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void requestCartReplace(IReplaceCartDialog iReplaceCartDialog,Garage current)
    {

        DisplayMetrics metrics = activity().getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.85);
        ReplaceCartDialog dialog = new ReplaceCartDialog(activity(),iReplaceCartDialog,current, cartGarage);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void startPayment(double value)
    {
        if(value <= 0)
        {
            showSnackBar(Constants.ERROR,2);
            return;
        }
        final Checkout checkout = new Checkout();
        try
        {
            JSONObject options = new JSONObject();
            options.put("name", "VGarage");
            options.put("description", "Garage service items");
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png"); //Constants.URL_APP_LOGO );
            options.put("currency", "INR");
            options.put("amount", ""+( value * Constants.RUPEE_PAISA));
            JSONObject preFill = new JSONObject();
            preFill.put("email", Utility.getString(Utility.getSharedPreferences(),Constants.USER_EMAIL));
            preFill.put("contact", "1234567890");
            options.put("prefill", preFill);
            checkout.open(activity(), options);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

                //updateCart(menuCart,8);
                refreshCart();
                hideMenuOption(R.id.menu_fav);
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
        try {
            switch (id) {
                case R.id.menu_add_review:
                    if (menuAddReview != null)
                        menuAddReview.setVisible(false);
                    break;

                case R.id.menu_cart:
                    if (menuCart != null)
                        menuCart.setVisible(false);
                    break;

                case R.id.menu_fav:
                    if (menuFav != null)
                    {
                        menuFav.setVisible(false);
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void visibleMenuOption(int id)
    {
        try {
            switch (id) {
                case R.id.menu_add_review:
                    if (menuAddReview != null)
                        menuAddReview.setVisible(true);
                    break;

                case R.id.menu_cart:
                    if (menuCart != null)
                        menuCart.setVisible(true);
                    break;

                case R.id.menu_fav:
                    if (menuFav != null)
                    {
                        //menuFav.setVisible(true);
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void openAddReviewDialog()
    {
        Garage garage = (Garage) getIntent().getSerializableExtra("Garage");
        AddReviewDialog dialog = new AddReviewDialog(activity(),garage,iAddReviewDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels - Utility.dpSize(activity(),40);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());


        int dialogWindowWidth = (int) (displayWidth * 0.99f);
        int dialogWindowHeight = Utility.dpSize(activity(),250);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);

    }

    @Override
    public Activity activity() {
        return this;
    }


    @Override
    public void onBackPressed()
    {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        else
        {
            finish();
        }

    }

    public void updateCart(int count)
    {
        if(menuCart == null) return;

//        if(count > 0)
//        {
            LayerDrawable icon = (LayerDrawable) menuCart.getIcon();
            setBadgeCount(this, icon, "" + count);
//        }
//        else
//        {
//            menuCart.setIcon(R.drawable.cart_with_badge);
//        }
    }


    public void refreshGarageServices()
    {
        garageOverviewFragment.updateGarageServices();
    }

    public void setBadgeCount(Context context, LayerDrawable icon, String count)
    {

        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    public void updateUserVehicles()
    {
        onBackPressed();
        try {
            ((ProfileFragment) getSupportFragmentManager().getFragments().get(0)).refreshVehicles();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s)
    {
        Logger.d("PAYMENT ID",s);
        onBackPressed();
        showSnackBar("Payment success",1);
    }

    @Override
    public void onPaymentError(int i, String s)
    {
        onBackPressed();
        showSnackBar("Payment failed",0);
    }

    public void openAppointmentDialog(Appointment appointment,IAppointmentDetailsDialog iAppointmentDetailsDialog)
    {
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        AppointmentDetailsDialog dialog = new AppointmentDetailsDialog(activity(),appointment, iAppointmentDetailsDialog );
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels - Utility.dpSize(activity(),40);
        int displayHeight = Utility.dpSize(activity(),470);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());


        int dialogWindowWidth = (int) (displayWidth * 0.99f);
        int dialogWindowHeight = (int) (displayHeight * 0.99f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);

    }

//    public void saveCart()
//    {
//        Gson gson = new Gson();
//        Type type = new TypeToken<Cart>()
//        {
//        }.getType();
//        cart.garage = cartGarage;
//        cart.listServices = cartGarageServices;
//        Utility.setString(Utility.getSharedPreferences(),Constants.CART_DATA,gson.toJson(cart,type));
//    }

//    public Cart generateCart()
//    {
//        Gson gson = new Gson();
//        Type type = new TypeToken<Cart>(){}.getType();
//        if(Utility.getSharedPreferences().contains(Constants.CART_DATA))
//        {
//            return gson.fromJson(Utility.getString(Utility.getSharedPreferences(),Constants.CART_DATA),type);
//        }
//        else
//        {
//            return new Cart();
//        }
//    }

    public void replaceCart(Garage garage)
    {
        cartGarageServices = new ArrayList<>();
        cartGarage = garage;
        //saveCart();
        updateCart(cartGarageServices.size());
    }


    public void pickVehicleDialog(IToPickVehicle iToPickVehicle)
    {
        DisplayMetrics metrics = activity().getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels);
        int height = (int) (metrics.heightPixels);
        ToPickVehicle dialog = new ToPickVehicle(activity(),iToPickVehicle);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(width, height);
    }


    public void goToUserVehicles(int code)
    {
        Intent intent = new Intent(activity(),HelperActivity.class);
        intent.putExtra(Constants.FRAGMENT_KEY,2223);
        startActivityForResult(intent,code);
    }

    public void confirmVehicleDialog(Vehicle vehicle, IAddVehicleDialog iAddVehicleDialog)
    {
        DisplayMetrics metrics = activity().getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.85);
        AddVehicleDialog dialog = new AddVehicleDialog(activity(),vehicle,iAddVehicleDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 18104: // add vehicle
                if(garageOverviewFragment!=null)
                {
                    if(data!=null)
                    {
                        garageOverviewFragment.updateCar();
                    }
                    else
                    {

                    }
                }
                break;
            case 18105: // change vehicle
                if(garageOverviewFragment!=null)
                {
                    if(data!=null)
                    {
                        garageOverviewFragment.updateCar();
                    }
                    else
                    {

                    }

                }
                break;
        }
    }

    @Override
    public void updateRatting()
    {
     if(garageOverviewFragment!=null)
     {
         garageOverviewFragment.updateRatting();
     }
    }



    public void deleteOpcodeFromCart(GarageService garageService)
    {
        if(garageService==null)
        return;

        String URL = Constants.URL_DELETE_CART_OPCODE + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&dealerId=" + garageService.DealerId + "&opCodeId==" +  garageService.OpCodeId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public void insertOpcodeToCart(GarageService garageService)
    {
        if(garageService==null)
        return;

        String URL = Constants.URL_INSERT_CART_OPCODE + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID)+
                "&dealerId=" + garageService.DealerId + "&opCodeId==" +  garageService.OpCodeId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public void fetchOpcodesOfCart()
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_USER_CART_OPCODES + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<GarageService>>() {
                }.getType();
                cartGarageServices = gson.fromJson(response, type);
                updateCart(cartGarageServices.size());
                if(cartGarageServices!=null && cartGarageServices.size()>0)
                {
                    fetchDealerById(cartGarageServices.get(0).DealerId);
                    refreshGarageServices();
                }

                Utility.closeProgressDialog(progressDialog);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                Utility.closeProgressDialog(progressDialog);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public void fetchDealerById(int dealerId)
    {
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        String URL = Constants.URL_GET_DEALER_DETAILS + "?dealerId="+dealerId;
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Garage>>() {
                }.getType();
                List<Garage> garages = gson.fromJson(response, type);
                cartGarage = garages.get(0);
                Utility.closeProgressDialog(progressDialog);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
                Utility.closeProgressDialog(progressDialog);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }



    public void saveUserAddress(String address)
    {

    }

    public void fetchUserAddress()
    {
        String URL = Constants.URL_USER_ADDRESS + "?userId="+Utility.getString(Utility.getSharedPreferences(),Constants.USER_ID);
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final Response.Listener<String> listener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.d("[response]", response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Logger.d("[response]",Constants.CONNECTION_ERROR);
                showSnackBar(Constants.CONNECTION_ERROR,2);
            }
        };
        volleyHelperLayer.startHandlerVolley(URL,null,listener,errorListener, Request.Priority.NORMAL,Constants.GET_REQUEST);
    }

    public String generateCartOpCodes()
    {
        String result = "";
        if(cartGarageServices!=null && cartGarageServices.size()>0)
        {
            for(int i=0;i<cartGarageServices.size();i++)
            {
                if(i==cartGarageServices.size()-1)
                {
                    result += cartGarageServices.get(i).OpCodeId;
                }
                else
                result += cartGarageServices.get(i).OpCodeId+ ",";
            }
        }

        return result;
    }


}
