package com.example.tabishhussain.hopeorbits.views.fragments.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Home;
import com.example.tabishhussain.hopeorbits.views.activities.MainActivity;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.SmsListener;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.SmsReceiver;
import com.example.tabishhussain.hopeorbits.views.fragments.BaseFragment;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tabish Hussain on 7/9/2017.
 */

public class VerificationFragment extends BaseFragment implements View.OnClickListener {
    View view;
    EditText edtotpcode;
    TextView txtverify;
    //    Button btnsubmit, btnresent;
    String message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    RelativeLayout rlmessage, rlcall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_phone_verification, container, false);
        edtotpcode = (EditText) view.findViewById(R.id.edtotpcode);
        txtverify = (TextView) view.findViewById(R.id.txtverify);
//        btnsubmit = (Button) view.findViewById(R.id.btnsubmit);
//        btnresent = (Button) view.findViewById(R.id.btnresend);
        rlmessage = (RelativeLayout) view.findViewById(R.id.rlmessage);
        rlcall = (RelativeLayout) view.findViewById(R.id.rlcall);
        rlmessage.setOnClickListener(this);
        rlcall.setOnClickListener(this);
        txtverify.setOnClickListener(this);
//        btnsubmit.setOnClickListener(this);
//        btnresent.setOnClickListener(this);
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                try {

                    Log.d("Text", messageText);
//                    Toast.makeText(getActivity(), "Message: " + messageText, Toast.LENGTH_LONG).show();
                    edtotpcode.setText(messageText);
                    Toast.makeText(getActivity(), "Message: " + messageText, Toast.LENGTH_LONG).show();
                    if (edtotpcode.getText().toString().length() == 6) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtverify.performClick();
                            }
                        }, 5000);
                    }
                } catch (Exception e) {

                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtverify:
                if (TextUtils.isEmpty(edtotpcode.getText().toString())) {
                    edtotpcode.setError("Please enter the code then press Submit");
                } else {
                    Call<JsonObject> call = HopeOrbitApi.retrofit.verifyUSer(getParams());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccess()) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response.body().toString());

                                    message = jsonObj.getString("message");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Log.d(TAG, response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (message.equalsIgnoreCase("ACCOUNT_VERIFIED")) {
                                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean("Login", true);
                                editor.commit();

                                Intent in = new Intent(getActivity(), Home.class);
//                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                                getActivity().finish();

                            } else {
                                Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d(TAG, t.toString());
                        }
                    });
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                break;
            case R.id.rlmessage:
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+923339000859", null, "Please verify my number:" + sharedpreferences.getString("phone", ""), null, null);
                    Toast.makeText(getActivity(), "Message Sent successfully.", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    getActivity().finishAffinity();
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }

                break;
            case R.id.rlcall:
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:923339000859"));
                startActivity(intent2);
                getActivity().finish();
                getActivity().finishAffinity();
                break;
        }
    }

    public HashMap<String, Object> getParams() {
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", sharedpreferences.getString("phone", ""));
        params.put("code", edtotpcode.getText().toString());
        return params;
    }
}
