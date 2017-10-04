package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.PageModelList;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    PageModelList modelList;
    ArrayList<PageModelList> modelLists = new ArrayList<>();
    ImageView refres, msg, altmsg, shoping;
    EditText pagename, details;
    Button go, check;
    String currency = "";
    String base64 = "";
    Spinner spinner;
    String id, Pageidswrongarray;
    String Ids;
    String findpagename;

    public static final String MyPREFERENCES = "MyPrefs";

    EditText add_search;
    Fragment fragment;
    FragmentTransaction ft;
    android.app.AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_home, container, false);
        modelList = new PageModelList();

////bind
//        refres = (ImageView) findViewById(R.id.refress);
//        msg = (ImageView) findViewById(R.id.msg);
//        altmsg = (ImageView) findViewById(R.id.msgalert);
//        shoping = (ImageView) findViewById(R.id.shoping);
        pagename = (EditText) view.findViewById(R.id.pagename);
        details = (EditText) view.findViewById(R.id.details);

        check = (Button) view.findViewById(R.id.check);
        check.setOnClickListener(this);
        go = (Button) view.findViewById(R.id.go);
        go.setOnClickListener(this);

        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("Id", "");
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setPrompt("Select an item");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Currency, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    //valide method inbuilt
    private boolean validateData() {

        if (TextUtils.isEmpty(pagename.getText().toString())) {
            pagename.setError("Please provide page name");
            return false;
        }
        if (TextUtils.isEmpty(details.getText().toString())) {
            details.setError("Please provide  details");
            return false;
        }
        if (currency.isEmpty()) {
            Toast.makeText(getActivity(), "Select currency...", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (base64.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Choose Image...", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }

    //click handle

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
                postingpage();
                break;
            case R.id.check:
                avaibilitycheck();
                break;
        }
    }

    private void avaibilitycheck() {

        findpagename = pagename.getText().toString();
        if (!findpagename.equals("")) {

            Call<ResponseBody> call = HopeOrbitApi.retrofit.checkavil(findpagename);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String MyResult = null;

                    try {

                        MyResult = response.body().string();
                        String s = new JSONObject(MyResult).getString("message");
                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Enter Page Name ", Toast.LENGTH_LONG).show();
        }
    }
//page post to server method

    private void postingpage() {

        if (validateData()) {

            modelLists.clear();
            modelList.setPageName(pagename.getText().toString());
            modelList.setCurrency(currency);
            modelList.setDetails(details.getText().toString());
            modelList.setPageImage("");
            modelLists.add(modelList);
            final ProgressDialog dialog = new ProgressDialog(getActivity());

            dialog.show();

            Call<JsonObject> call = HopeOrbitApi.retrofit.createpage(getParams());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JSONObject object;
                    JSONArray array;
                    if (response.isSuccess()) {
                        String respons = response.body().toString();
                        try {
                            object = new JSONObject(respons);
                            array = object.getJSONArray("pageModelList");
                            JSONObject object1 = array.getJSONObject(0);
                            String PageId = object1.getString("pageID");
                            String errorMessage = object1.getString("errorMessage");
                            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                                Toast.makeText(getActivity(), object1.getString("errorMessage"), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                //--------------decode base64------------


//                                String imgstr = object1.getString("pageImage");
//                                Log.d("errrorimage", imgstr.toString());
//                                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
//                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                dialog.dismiss();

                                Bundle bundle = new Bundle();
                                bundle.putString("pageId", PageId);
                                fragment = new MapFragment();
                                fragment.setArguments(bundle);
                                ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.add(R.id.frame, fragment);
                                ft.addToBackStack("add" + Container.add);
                                ft.commit();
                                Container.add++;


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }

                    } else {
                        try {
                            Log.d("errror", response.errorBody().string());
                            dialog.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println(t.toString());
                    dialog.dismiss();
                }
            });

        }
    }

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", Ids);
        params.put("pageModelList", modelLists);
        return params;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currency = spinner.getSelectedItem().toString();
        if (currency.equals("Currency")) {
            currency = "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

