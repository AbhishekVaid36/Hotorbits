package com.example.tabishhussain.hopeorbits.views.fragments.accounts;

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
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tabish Hussain on 7/9/2017.
 */

public class RegistrationFragment extends BaseFragment implements View.OnClickListener {
    View view;
    EditText edtname, edtcountry, edtcode,edtphone, edtpass, edtconpass;
    RelativeLayout rlRegister;
    String message, countryCode;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String[] list_designation;
    private ListPopupWindow lpw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        edtname = (EditText) view.findViewById(R.id.edtname);
        edtcountry = (EditText) view.findViewById(R.id.edtcountry);
        edtcode= (EditText) view.findViewById(R.id.edtcode);
        edtphone = (EditText) view.findViewById(R.id.edtphone);
        edtpass = (EditText) view.findViewById(R.id.edtpass);
        edtconpass = (EditText) view.findViewById(R.id.edtconphone);
        rlRegister = (RelativeLayout) view.findViewById(R.id.rlregister);
        rlRegister.setOnClickListener(this);

        edtcountry.setInputType(InputType.TYPE_NULL);
        edtcode.setInputType(InputType.TYPE_NULL);
        edtcountry.setText("Pakistan");
        edtcode.setText("+92");
        countryCode = "+92";
        list_designation = new String[]{"India", "Pakistan", "United States of America"};
        lpw = new ListPopupWindow(getActivity());
        lpw.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_designation));
        lpw.setAnchorView(edtcountry);
        lpw.setModal(true);
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list_designation[position];
                edtcountry.setText(item);
                if (position == 0)
                    countryCode = "+91";
                else if (position == 1)
                    countryCode = "+92";
                else if (position == 2)
                    countryCode = "+1";

                edtcode.setText(countryCode);
                lpw.dismiss();
            }
        });

        edtcountry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                lpw.show();


                return false;
            }
        });

        edtname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        edtcountry.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtcountry.setError(null);
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
//                if (!s.toString().contains(countryCode)) {
//                    edtphone.setText(countryCode);
//                    SpannableString spannablecontent = new SpannableString(edtphone.getText().toString());
//                    Selection.setSelection(spannablecontent, edtphone.getText().length());
//
//                }

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

        edtconpass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtconpass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        return view;
    }

    private boolean validateData() {
        if (TextUtils.isEmpty(edtname.getText().toString())) {
            edtname.setError("Please provide name");
            return false;
        }
        if (TextUtils.isEmpty(edtcountry.getText().toString())) {
            edtcountry.setError("Please provide country");
            return false;
        }
//        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        if (TextUtils.isEmpty(edtphone.getText().toString())) {
            edtphone.setError("Please provide phone number");
            return false;
        }
        if (TextUtils.isEmpty(edtpass.getText().toString())) {
            edtpass.setError("Please provide password");
            return false;
        }
//        if (!pattern.matcher(edtpass.getText().toString()).find()) {
//            edtpass.setError("Password is too weak");
//            return false;
//        }
        if (TextUtils.isEmpty(edtconpass.getText().toString())) {
            edtconpass.setError("Please re enter password");
            return false;
        }
        if (!edtpass.getText().toString().equalsIgnoreCase(edtconpass.getText().toString())) {
            edtconpass.setError("Password mismatch");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlregister:
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
                                editor.putString("phone", edtcode.getText().toString()+edtphone.getText().toString());
                                editor.commit();

                                Intent in = new Intent(getActivity(), VerificationActivity.class);
                                startActivity(in);
                                getActivity().finish();

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
        params.put("name", edtname.getText().toString());
        params.put("userEmail", edtcountry.getText().toString());
        params.put("userPassword", edtpass.getText().toString());
        params.put("phoneNumber",edtcode.getText().toString()+ edtphone.getText().toString());
        params.put("verified", false);
        params.put("authyId", "1");
        return params;
    }
}
