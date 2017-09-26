package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Splash extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlLogin, rlSignup;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    boolean Login;
    String Id;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        setUpToolbar("Hoporbits", false);
        rlLogin = (RelativeLayout) findViewById(R.id.rllogin);
        rlSignup = (RelativeLayout) findViewById(R.id.rlsignup);
        rlLogin.setOnClickListener(this);
        rlSignup.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Login = sharedpreferences.getBoolean("Login", false);
        Id = sharedpreferences.getString("Id", "");
//        if (Login) {
//            Intent in = new Intent(Splash.this, Home.class);
////            Intent in = new Intent(Splash.this, BusinessPage.class);
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(in);
//            Splash.this.finish();
//        }
        checkAndRequestPermissions();
    }
    private boolean checkAndRequestPermissions() {
        int recievesms = ContextCompat.checkSelfPermission(Splash.this, android.Manifest.permission.RECEIVE_SMS);
        int readsms = ContextCompat.checkSelfPermission(Splash.this, android.Manifest.permission.READ_SMS);
        int sendsms = ContextCompat.checkSelfPermission(Splash.this, android.Manifest.permission.SEND_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (recievesms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (readsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (sendsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Splash.this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rllogin:
                startActivity(new Intent(Splash.this, LoginActivity.class));
                break;
            case R.id.rlsignup:
                startActivity(new Intent(Splash.this, SignUpActivity.class));
                break;
        }

    }
}
