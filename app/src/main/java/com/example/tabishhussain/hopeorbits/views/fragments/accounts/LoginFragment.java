package com.example.tabishhussain.hopeorbits.views.fragments.accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Home;
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
    EditText edtcode, edtphone, edtpass, edtphoneforgot;
    RelativeLayout rlforgotpass, rllogin;
    AlertDialog mDialog;
    Button rl_close, rl_submit;
    String id, name, email, authyId, phone, message, countryCode;
    boolean verified = false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String[] list_designation;
    private ListPopupWindow lpw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_login, container, false);
        edtcode = (EditText) view.findViewById(R.id.edtcode);
        edtphone = (EditText) view.findViewById(R.id.edtphone);
        edtpass = (EditText) view.findViewById(R.id.edtpass);
        rllogin = (RelativeLayout) view.findViewById(R.id.rllogin);
        rlforgotpass = (RelativeLayout) view.findViewById(R.id.rlforgotpass);
        rllogin.setOnClickListener(this);
        rlforgotpass.setOnClickListener(this);
        edtcode.setInputType(InputType.TYPE_NULL);
        edtcode.setText("PAK");
        countryCode = "+92";
        list_designation = new String[]{"IND", "PAK", "USA"};
        lpw = new ListPopupWindow(getActivity());
        lpw.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_designation));
        lpw.setAnchorView(edtcode);
        lpw.setModal(true);
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list_designation[position];
                edtcode.setText(item);
                if (position == 0)
                    countryCode = "+91";
                else if (position == 1)
                    countryCode = "+92";
                else if (position == 2)
                    countryCode = "+1";
                lpw.dismiss();
            }
        });
        edtcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                lpw.show();
                return false;
            }
        });

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
            case R.id.rllogin:
                if (validateData()) {
                    retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.loginUser(getParams());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccess()) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response.body().toString());
                                    if (jsonObj.has("id")) {
                                        id = jsonObj.getString("id");
                                        name = jsonObj.getString("name");
                                        email = jsonObj.getString("email");
                                        phone = jsonObj.getString("phoneNumber");
                                        authyId = jsonObj.getString("authyId");
                                        verified = jsonObj.getBoolean("verified");
                                    } else {
                                        message = jsonObj.getString("message");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (id != null) {
//                                    if (verified) {
                                    Toast.makeText(getActivity(), "Login successfully", Toast.LENGTH_LONG).show();
                                    sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean("Login", true);
                                    editor.putString("Id", id);
                                    editor.putString("phone",phone);
                                    editor.commit();
                                    Intent in = new Intent(getActivity(), Home.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    getActivity().finish();

//                                    } else {
//                                        Toast.makeText(getActivity(), "Account not verified", Toast.LENGTH_LONG).show();
//                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Invalid credential", Toast.LENGTH_LONG).show();
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
            case R.id.rlforgotpass:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.forgotpassword, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_cancel);
                rl_submit = (Button) convertView.findViewById(R.id.btn_confirm);
                edtphoneforgot = (EditText) convertView.findViewById(R.id.edtphone);
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
                        if (TextUtils.isEmpty(edtphoneforgot.getText().toString())) {
                            edtphoneforgot.requestFocus();
                            edtphoneforgot.setText("");
                            edtphoneforgot.setError("Please fill number");
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
                                        try {
                                            JSONObject jsonObj = new JSONObject(response.body().toString());

                                            message = jsonObj.getString("message");
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

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
                edtphoneforgot.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        // put the code of save Database here
                        edtphoneforgot.setError(null);
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
        params.put("phoneNumber", edtphoneforgot.getText().toString());
        return params;
    }

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", countryCode + edtphone.getText().toString());
        params.put("userPassword", edtpass.getText().toString());
        params.put("verified","verified");
        return params;
    }

}
