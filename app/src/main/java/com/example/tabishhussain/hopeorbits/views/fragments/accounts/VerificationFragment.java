package com.example.tabishhussain.hopeorbits.views.fragments.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.views.activities.MainActivity;
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
    EditText edtOtp;
    Button btnsubmit, btnresent;
    String message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_phone_verification, container, false);
        edtOtp = (EditText) view.findViewById(R.id.code);
        btnsubmit = (Button) view.findViewById(R.id.btnsubmit);
        btnresent = (Button) view.findViewById(R.id.btnresend);
        btnsubmit.setOnClickListener(this);
        btnresent.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(edtOtp.getText().toString())) {
            edtOtp.setError("Please enter the code then press Done");
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
                        editor.putBoolean("Login",true);
                        editor.commit();
                        startActivity(new Intent(getActivity(), MainActivity.class));
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
    }

    public HashMap<String, Object> getParams() {
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", sharedpreferences.getString("phone", ""));
        params.put("code", edtOtp.getText().toString());
        return params;
    }
}
