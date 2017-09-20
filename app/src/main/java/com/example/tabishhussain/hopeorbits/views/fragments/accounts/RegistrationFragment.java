package com.example.tabishhussain.hopeorbits.views.fragments.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.VerificationActivity;
import com.example.tabishhussain.hopeorbits.views.fragments.BaseFragment;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tabish Hussain on 7/9/2017.
 */

public class RegistrationFragment extends BaseFragment implements View.OnClickListener {
    View view;
    TextView edtfirstname, edtemail, edtphone, edtpass, edtrepass;
    Button btnsubmit;
    String message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        edtfirstname = (TextView) view.findViewById(R.id.fname);
        edtemail = (TextView) view.findViewById(R.id.email);
        edtphone = (TextView) view.findViewById(R.id.phone);
        edtpass = (TextView) view.findViewById(R.id.password);
        edtrepass = (TextView) view.findViewById(R.id.re_password);
        btnsubmit = (Button) view.findViewById(R.id.register);
        btnsubmit.setOnClickListener(this);

        edtfirstname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtfirstname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        edtemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtemail.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

        edtpass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtpass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        edtrepass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtrepass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        return view;
    }

    private boolean validateData() {
        if (TextUtils.isEmpty(edtfirstname.getText().toString())) {
            edtfirstname.setError("Please provide first name");
            return false;
        }
        if (TextUtils.isEmpty(edtemail.getText().toString())) {
            edtemail.setError("Please provide email");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).find()) {
            edtemail.setError("Please provide a valid email");
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        if (TextUtils.isEmpty(edtphone.getText().toString())) {
            edtphone.setError("Please provide phone number");
            return false;
        }
        if (TextUtils.isEmpty(edtpass.getText().toString())) {
            edtpass.setError("Please provide password");
            return false;
        }
        if (!pattern.matcher(edtpass.getText().toString()).find()) {
            edtpass.setError("Password is too weak");
            return false;
        }
        if (TextUtils.isEmpty(edtrepass.getText().toString())) {
            edtrepass.setError("Please re enter password");
            return false;
        }
        if (!edtpass.getText().toString().equalsIgnoreCase(edtrepass.getText().toString())) {
            edtrepass.setError("Password mismatch");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                if (validateData()) {
                    retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.registeredUser(getParams());
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
                            if (message.equalsIgnoreCase("USER_REGISTER_SUCCESSFULLY")) {
                                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("phone",edtphone.getText().toString());
                                editor.commit();

                                startActivity(new Intent(getActivity(), VerificationActivity.class));
                            } else {
                                Toast.makeText(getActivity(), "Phone Number allready exist", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d(TAG, t.toString());
                        }
                    });
                }
                break;
        }
    }

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", edtfirstname.getText().toString());
        params.put("userEmail", edtemail.getText().toString());
        params.put("userPassword", edtpass.getText().toString());
        params.put("phoneNumber", edtphone.getText().toString());
        params.put("verified", false);
        params.put("authyId", "1");
        return params;
    }
}
