package com.test.foodzone.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
import com.test.foodzone.R;
import com.test.foodzone.constants.Constants;
import com.test.foodzone.fragments.start.StartFragment;
import com.test.foodzone.interfaces.activities.IActivity;
import com.test.foodzone.interfaces.activities.IStartActivity;
import com.test.foodzone.utils.Logger;
import com.test.foodzone.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements IActivity,IStartActivity
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

    @Override
    public void changeTitle(String title)
    {

    }

    @Override
    public void showSnackBar(String snackBarText,int type)
    {
        Utility.showSnackBar(this,coordinatorLayout,snackBarText,type);
    }

    @Override
    public Activity getActivity()
    {
       return StartActivity.this;
    }

    @Override
    public void doAccountKitLogin()
    {
        if(Utility.isNetworkAvailable(this))
        {
            login();
        }
        else
        {
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,2);
        }
    }



    private void login()
    {

        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE).setReadPhoneStateEnabled(true);
        UIManager uiManager;

        // Skin is CLASSIC, CONTEMPORARY, or TRANSLUCENT

        uiManager = new SkinManager(
                SkinManager.Skin.CLASSIC, ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        configurationBuilder.setUIManager(uiManager);
        // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, Constants.ACCOUNT_KIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("ACTIVITY RESULT");

        if (requestCode == Constants.ACCOUNT_KIT_REQUEST_CODE)
        { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            String toastMessage;
            if (loginResult.getError() != null)
            {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            }
            else if (loginResult.wasCancelled())
            {
                toastMessage = "Login Cancelled";
            }
            else
            {
                if (loginResult.getAccessToken() != null)
                {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    getAccount();
                }
                else
                {
                    toastMessage = String.format(
                            "Token Null Success:%s... ",
                            loginResult.getAuthorizationCode().toString());
                    Logger.d(loginResult.getAuthorizationCode().toString());
                }
            }

            // Surface the result to your user in an appropriate way.
           //showSnackBar(toastMessage,2);
           Logger.d("DATA",toastMessage);
        }
    }


    private void getAccount()
    {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = phoneNumber.toString();
                // Get email
                String email = account.getEmail();
                showSnackBar(phoneNumberString+" "+email+" "+accountKitId + " "+account, 2);
                Logger.d("DATA",phoneNumberString+" "+email+" "+accountKitId);

            }

            @Override
            public void onError(final AccountKitError error)
            {
                // Handle Error
                showSnackBar(Constants.ERROR,2);

                Logger.d("Error","Login Error");
            }
        });
    }
}
