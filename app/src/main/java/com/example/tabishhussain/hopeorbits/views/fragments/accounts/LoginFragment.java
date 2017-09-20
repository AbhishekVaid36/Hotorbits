package com.example.tabishhussain.hopeorbits.views.fragments.accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.views.activities.MainActivity;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.SignUpActivity;
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

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    View view;
    EditText edtphone, edtpass;
    TextView txtforgot, txtnotregister;
    Button btnRegister, btnLogin;

    AlertDialog mDialog;
    Button rl_close, rl_submit;
    EditText edt_forget_email;
    TextView txttitle, txtmessages;
    String id, name, email, authyId, phone, verified,message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_login, container, false);
        edtphone = (EditText) view.findViewById(R.id.phone);
        edtpass = (EditText) view.findViewById(R.id.password);
        txtforgot = (TextView) view.findViewById(R.id.forgot_password);
        txtnotregister = (TextView) view.findViewById(R.id.forgot_password);
        btnLogin = (Button) view.findViewById(R.id.login);
        btnRegister = (Button) view.findViewById(R.id.register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        txtforgot.setOnClickListener(this);
        return view;
    }

    private boolean validateData() {
        if (TextUtils.isEmpty(edtphone.getText().toString())) {
            edtphone.setError("Please provide phone number");
            return false;
        }
//        if (Patterns.PHONE.matcher(edtphone.getText().toString()).find()) {
//            edtphone.setError("Invalid phone number");
//            return false;
//        }
        if (TextUtils.isEmpty(edtpass.getText().toString())) {
            edtpass.setError("Please provide pa ssword");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (validateData()) {
                    retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.loginUser(getParams());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccess()) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response.body().toString());
                                    if(jsonObj.has("id")) {
                                        id = jsonObj.getString("id");
                                        name = jsonObj.getString("name");
                                        email = jsonObj.getString("email");
                                        phone = jsonObj.getString("phoneNumber");
                                        authyId = jsonObj.getString("authyId");
                                        verified = jsonObj.getString("verified");
                                    }
                                    else
                                    {
                                        message = jsonObj.getString("message");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(id!=null)
                                {
                                    Toast.makeText(getActivity(),"Login successfully",Toast.LENGTH_LONG).show();
                                    sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean("Login",true);
                                    editor.putString("Id",id);
                                    editor.commit();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Invalid credential",Toast.LENGTH_LONG).show();
                                }

                            } else {
                                try {
                                    Log.d(TAG, response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d(TAG, t.toString());
                        }
                    });
                }
//                startActivity(new Intent(getActivity(), BusinessPage.class));
                break;
            case R.id.register:
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
            case R.id.forgot_password:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.forgotpassword, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_cancel);
                rl_submit = (Button) convertView.findViewById(R.id.btn_confirm);
                edtphone = (EditText) convertView.findViewById(R.id.edtphone);
                alertDialog.setView(convertView);
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                rl_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(edtphone.getText().toString())) {
                            edtphone.requestFocus();
                            edtphone.setText("");
                            edtphone.setError("Please fill number");
                        } else {
//                            if (Patterns.PHONE.matcher(edtphone.getText().toString()).find()) {
//                                edtphone.setError("Invalid phone number");
//                            } else {
                            mDialog.cancel();
                            retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.forgotpass(getforgotParams());
                            call.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccess()) {
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                    } else {
                                        try {
                                            Log.d(TAG, response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Log.d(TAG, t.toString());
                                }
                            });
                        }

//                        }
                    }
                });
                edtphone.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        // put the code of save Database here
                        edtphone.setError(null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                mDialog = alertDialog.show();
                break;
        }
    }

    public HashMap<String, Object> getforgotParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", edtphone.getText().toString());
        return params;
    }

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", edtphone.getText().toString());
        params.put("userPassword", edtpass.getText().toString());
        return params;
    }

}
