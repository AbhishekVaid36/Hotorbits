package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;

public class Splash extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlLogin, rlSignup;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    boolean Login;
    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        setUpToolbar("Home", false);
        rlLogin = (RelativeLayout) findViewById(R.id.rllogin);
        rlSignup = (RelativeLayout) findViewById(R.id.rlsignup);
        rlLogin.setOnClickListener(this);
        rlSignup.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Login = sharedpreferences.getBoolean("Login", false);
        Id = sharedpreferences.getString("Id", "");

//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//            String mPhoneNumber = tMgr.getLine1Number();
//        } else {
//            //TODO
//        }
//
//
//        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String mPhoneNumber = tMgr.getLine1Number();
//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//
//        // get IMEI
//        String imei = tm.getDeviceId();
//
//        // get SimSerialNumber
//        String simSerialNumber = tm.getSimSerialNumber();
//
//        AccountManager am = AccountManager.get(this);
//        Account[] accounts = am.getAccounts();
//
//        for (Account ac : accounts) {
//            String acname = ac.name;
//            String actype = ac.type;
//            // Take your time to look at all available accounts
//            System.out.println("Accounts : " + acname + ", " + actype);
//        }


        if (Login) {
//            Intent in = new Intent(Splash.this, MainActivity.class);
            Intent in = new Intent(Splash.this, WelCome.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            Splash.this.finish();
        }
//        Intent in = new Intent(Splash.this, MainActivity.class);
//        startActivity(in);
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
