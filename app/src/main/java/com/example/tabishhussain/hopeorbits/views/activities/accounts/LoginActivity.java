package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.vendor.BusinessPage;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;
import com.example.tabishhussain.hopeorbits.views.fragments.accounts.LoginFragment;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Tabish Hussain on 7/10/2017.
 */

public class LoginActivity extends BaseActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Boolean Login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_content);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Login = sharedpreferences.getBoolean("login", false);
        if (Login) {
            startActivity(new Intent(LoginActivity.this, BusinessPage.class));
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, new LoginFragment()).commit();
            setUpToolbar("Login", false);
        }


    }
}
