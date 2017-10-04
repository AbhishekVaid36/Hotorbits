package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tabishhussain.hopeorbits.R.id.amount;

public class CreditManagement extends Fragment {
    private String tophon;
    private String tamount;
    String Ids;
    RelativeLayout rl;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.dailog, container, false);

        final TextView idnum = (TextView) view.findViewById(R.id.idnum);

        final Button btnSave = (Button) view.findViewById(R.id.trans);
//      final Button btncncl = (Button) findViewById(R.id.cencelbtn);
        final EditText toget = (EditText) view.findViewById(R.id.toget);
        final EditText amtget = (EditText) view.findViewById(amount);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("phone", "");
        idnum.setText(Ids);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tophon = toget.getText().toString().trim();
                String amount = amtget.getText().toString().trim();
                if (tophon.equals("")) {
                    Toast.makeText(getActivity(), "Enter Phone", Toast.LENGTH_SHORT).show();
                } else if (amount.equals("")) {
                    Toast.makeText(getActivity(), "Enter Amount", Toast.LENGTH_SHORT).show();
                } else {
                    callingtransfer(tophon, amount);
                }
            }
        });

//        btncncl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    private void callingtransfer(String tophon, String amount) {
        this.tophon = tophon;
        this.tamount = amount;
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
        params.put("phoneNumber", Ids);
        params.put("toPhonenumber", tophon);
        params.put("amount", tamount);
        return params;
    }
}
