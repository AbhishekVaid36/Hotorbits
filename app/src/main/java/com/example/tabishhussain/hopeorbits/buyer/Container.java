package com.example.tabishhussain.hopeorbits.buyer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Container extends AppCompatActivity implements View.OnClickListener {

    //Defining Variables
    public static Toolbar toolbar;

    String[] yon;
    public static int add = 0;
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment;
    FragmentTransaction ft;
    TextView txtname, txtcredit, txtHome, txtOptions;
    RelativeLayout rlcart, rlorder;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog2;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static int amount;
CircularImageView profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        txtname = (TextView) findViewById(R.id.txtname);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        rlcart = (RelativeLayout) findViewById(R.id.rlcart);
        rlorder = (RelativeLayout) findViewById(R.id.rlorder);
        profile_pic=(CircularImageView)findViewById(R.id.profile_pic);
        rlcart.setOnClickListener(this);
        rlorder.setOnClickListener(this);
        profile_pic.setOnClickListener(this);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("sdaf");
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        View logoView = getToolbarLogoIcon(toolbar);

        txtHome = (TextView) toolbar.findViewById(R.id.txthome);
        txtOptions = (TextView) toolbar.findViewById(R.id.txtoptions);
        txtHome.setOnClickListener(this);
        txtOptions.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.tool_bar, null);
        actionBar.setCustomView(customView);
//        Toolbar parent =(Toolbar) customView.getParent();
        toolbar.setPadding(10, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);
        initpDialog();
        getBalance();


//        // Generate icon
        fragment = new Home();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlcart:
                Intent intent = new Intent(Container.this, BucketView.class);
                startActivity(intent);
                break;
            case R.id.rlorder:
                Intent in = new Intent(Container.this, OrderView.class);
                startActivity(in);
                break;
            case R.id.txthome:
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                        manager.popBackStack();
                    }
                }
                break;
            case R.id.txtoptions:
                break;
            case R.id.profile_pic:
                break;
        }
    }

    public void getBalance() {
        showpDialog();
        Call<JsonObject> call = HopeOrbitApi.retrofit.showCredit(getcreditParams());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());

                        amount = jsonObj.getInt("amount");
                        txtcredit.setText("Balance: \u20A8" + amount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    try {
                        Log.d("Tag", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Tag", t.toString());
                hidepDialog();
            }
        });
    }

    public HashMap<String, Object> getcreditParams() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", sharedpreferences.getString("phone", ""));
        return params;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }

    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(Container.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Double screenInches = Math.sqrt(x + y);

        Integer inch = screenInches.intValue();
        if (inch >= 5) {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        } else {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
    }
}