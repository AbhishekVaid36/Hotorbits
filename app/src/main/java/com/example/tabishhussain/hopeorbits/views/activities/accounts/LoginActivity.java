package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;
import com.example.tabishhussain.hopeorbits.views.fragments.accounts.LoginFragment;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Tabish Hussain on 7/10/2017.
 */

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_content);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new LoginFragment()).commit();
        setUpToolbar("Login", true);
    }
}
