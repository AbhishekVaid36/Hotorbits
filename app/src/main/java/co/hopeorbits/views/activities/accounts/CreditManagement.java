package co.hopeorbits.views.activities.accounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.buyer.Home;
import co.hopeorbits.connectiondetector.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditManagement extends Fragment {
    private String toPhone, amount, fromPhone, message;
    EditText txtamount, txtto;
    TextView txtfrom, txtmessage, txt;
    RelativeLayout rltransfer;
    public static final String MyPREFERENCES = "MyPrefs";
    Button rl_close, rl_submit;
    AlertDialog mDialog;
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.dailog, container, false);

        txtfrom = (TextView) view.findViewById(R.id.txtfrom);
        txtto = (EditText) view.findViewById(R.id.txtto);
        txtamount = (EditText) view.findViewById(R.id.txtamount);

        rltransfer = (RelativeLayout) view.findViewById(R.id.rltransfer);
        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        fromPhone = settings.getString("phone", "");
        txtfrom.setText("From  " + fromPhone);
        rltransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPhone = txtto.getText().toString().trim();
                amount = txtamount.getText().toString().trim();
                if (toPhone.equals("")) {
                    txtto.requestFocus();
                    txtto.setText("");
                    txtto.setError("Enter Phone");
                } else if (amount.equals("")) {
                    txtamount.requestFocus();
                    txtamount.setText("");
                    txtamount.setError("Enter Amount");
                } else {
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                        Toast.makeText(getActivity(), "Please connect to working Internet connection", Toast.LENGTH_LONG).show();
                    } else {
                        retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.findUserByNumber(getverificationParams());
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccess()) {
                                    try {
                                        JSONObject jsonObj = new JSONObject(response.body().toString());
                                        if (jsonObj.has("name")) {
                                            message = jsonObj.getString("name");
                                        } else {
                                            message = jsonObj.getString("message");
                                        }
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                        LayoutInflater inflater = getActivity().getLayoutInflater();
                                        View convertView = (View) inflater.inflate(R.layout.numberverification, null);
                                        rl_close = (Button) convertView.findViewById(R.id.btn_cancel);
                                        rl_submit = (Button) convertView.findViewById(R.id.btn_confirm);
                                        txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
                                        txt = (TextView) convertView.findViewById(R.id.txt);
                                        alertDialog.setView(convertView);
                                        alertDialog.setCancelable(false);
                                        if (message.equalsIgnoreCase("USER_NOT_FOUND")) {
                                            rl_submit.setVisibility(View.GONE);
                                            txt.setVisibility(View.GONE);
                                            txtmessage.setText(message);
                                        } else {
                                            rl_submit.setVisibility(View.VISIBLE);
                                            txt.setVisibility(View.VISIBLE);
                                            txtmessage.setText("Are you want to transfer " + amount + " rs to " + message + "?");
                                        }
                                        rl_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDialog.cancel();
                                            }
                                        });
                                        rl_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDialog.cancel();
                                                callingtransfer(toPhone, amount);
                                            }
                                        });
                                        mDialog = alertDialog.show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    try {
                                        Log.d("TAG", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.d("TAG", t.toString());
                            }
                        });
                    }
                }
            }
        });
        txtto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                txtto.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        txtamount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                txtamount.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        return view;
    }

    private void callingtransfer(String tophon, String amount) {
        this.toPhone = tophon;
        this.amount = amount;
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(false);
        dialog.setMessage("Loading...");
        dialog.show();
        Call<JsonObject> call = HopeOrbitApi.retrofit.credittransfer(getParams());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONArray object = null;
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());
                        Log.d("response", jsonObj.toString());
                        Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        if (jsonObj.getString("message").equalsIgnoreCase("INSUFFICIENT_BALANCE")) {

                        } else {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                                manager.popBackStack();
                            }
                            fragment = new Home();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame, fragment);
                            ft.commit();
                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Log.d("error", response.errorBody().string());
                        dialog.dismiss();
                    } catch (IOException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("error", t.toString());
                dialog.dismiss();
            }
        });
    }

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", fromPhone);
        params.put("toPhonenumber", toPhone);
        params.put("amount", amount);
        return params;
    }

    public HashMap<String, Object> getverificationParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", toPhone);
        return params;
    }
}
