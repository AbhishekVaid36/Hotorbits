package com.example.tabishhussain.hopeorbits.buyer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.connectiondetector.ConnectionDetector;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends BaseActivity implements View.OnClickListener{
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static int amount;
    TextView txtname, txtcredit, txtemptylist;
    String pageModelList, pageID, pageName, currency, details, pageImage, errorMessage, categoryModels;
    String categoryID, categoryName, error, categoryImage, itemModelSet;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    ListView storelistview;
    RelativeLayout rlcart;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setUpToolbar("Home", false);
        initpDialog();
        txtname = (TextView) findViewById(R.id.txtname);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        txtemptylist = (TextView) findViewById(R.id.txtemptylist);
        rlcart = (RelativeLayout) findViewById(R.id.rlcart);
        storelistview = (ListView) findViewById(R.id.storelistview);
        rlcart.setOnClickListener(this);

        getBalance();
//        getStores();
//        new GetAllPages().execute();
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
//                hidepDialog();
                getStores();
//                new GetAllPages().execute();
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

    public class GetAllPages extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "getAllPages");
            try {
                JSONArray jsonArray = new JSONArray(json.toString());
                int length = jsonArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject ob = jsonArray.getJSONObject(i);
                    pageID = ob.getString("pageID");
                    pageName = ob.getString("pageName");
                    currency = ob.getString("currency");
                    details = ob.getString("details");
                    pageImage = ob.getString("pageImage");
                    errorMessage = ob.getString("errorMessage");
                    categoryModels = ob.getString("categoryModels");
                    StoreListHolder h = new StoreListHolder();
                    h.setPageID(pageID);
                    h.setPageName(pageName);
                    h.setCurrency(currency);
                    h.setDetails(details);
                    h.setPageImage(pageImage);
                    h.setErrorMessage(errorMessage);
                    h.setCategoryModels(categoryModels);
                    list.add(h);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hidepDialog();
            storelistview.setAdapter(new MyCustomAdapter(Home.this, list));
        }

    }

    public void getStores() {
//        showpDialog();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Call<JsonObject> call = HopeOrbitApi.retrofit.getStores("245fdc99-8466-4b54-9b4c-21904094b39e");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());
                        if (jsonObj.has("pageModelList")) {
                            pageModelList = jsonObj.getString("pageModelList");
                            JSONArray jsonArray = new JSONArray(pageModelList);
                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject ob = jsonArray.getJSONObject(i);
                                pageID = ob.getString("pageID");
                                pageName = ob.getString("pageName");
                                currency = ob.getString("currency");
                                details = ob.getString("details");
                                pageImage = ob.getString("pageImage");
                                errorMessage = ob.getString("errorMessage");
                                categoryModels = ob.getString("categoryModels");
                                StoreListHolder h = new StoreListHolder();
                                h.setPageID(pageID);
                                h.setPageName(pageName);
                                h.setCurrency(currency);
                                h.setDetails(details);
                                h.setPageImage(pageImage);
                                h.setErrorMessage(errorMessage);
                                h.setCategoryModels(categoryModels);
                                list.add(h);

//                                JSONArray jsonArray1 = new JSONArray(categoryModels);
//                                int length1 = jsonArray.length();
//                                for (int j = 0; j < length; j++) {
//                                    JSONObject obj = jsonArray.getJSONObject(i);
//                                    categoryID = obj.getString("categoryID");
//                                    categoryName = obj.getString("categoryName");
//                                    error = obj.getString("error");
//                                    categoryImage = obj.getString("categoryImage");
//                                    itemModelSet = obj.getString("itemModelSet");
//                                    StoreListHolder sh = new StoreListHolder();
//                                    sh.setPageID(pageID);
//                                    sh.setPageName(pageName);
//                                    sh.setCurrency(currency);
//                                    sh.setDetails(details);
//                                    sh.setPageImage(pageImage);
//                                    sh.setErrorMessage(errorMessage);
//                                    list.add(h);
//
//                                }
                            }
                            if (list.size() > 0) {
                                storelistview.setAdapter(new MyCustomAdapter(Home.this, list));
                                txtemptylist.setVisibility(View.GONE);
                            } else {
                                txtemptylist.setVisibility(View.VISIBLE);
                            }

                        }

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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlcart:
                Intent in = new Intent(Home.this, BucketView.class);
                startActivity(in);
                break;
        }
    }
    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<StoreListHolder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<StoreListHolder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }

        class ViewHolder {
            TextView txtstorename;
            ImageView imgstore;
            RelativeLayout rlstore;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.store_list_items, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txtstorename = (TextView) paramView.findViewById(R.id.txtstorename);
                holder.imgstore = (ImageView) paramView.findViewById(R.id.imgstore);
                holder.rlstore = (RelativeLayout) paramView.findViewById(R.id.rlstore);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            StoreListHolder h = list.get(paramInt);
            String name = h.getPageName();
            holder.txtstorename.setText(name);
            if ((!h.getPageImage().equalsIgnoreCase("null")) && (!h.getPageImage().isEmpty())) {
                byte[] decodedString = Base64.decode(h.getPageImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgstore.setImageBitmap(decodedByte);
            }
            else {
                holder.imgstore.setBackgroundResource(R.mipmap.uploadimage);
            }

            holder.rlstore.setTag(paramInt);
            holder.rlstore.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(Home.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) vv.getTag();
                        StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                        categoryModels = h1.getCategoryModels();
                        Intent in = new Intent(Home.this, Categories.class);
                        in.putExtra("categoryModels", categoryModels);
                        in.putExtra("pageID", h1.getPageID());
                        in.putExtra("storename", h1.getPageName());
                        startActivity(in);
                    }
                }
            });
            return paramView;
        }
    }
    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        Double screenInches = Math.sqrt(x+y);

        Integer inch = screenInches.intValue();
        if(inch >= 5)
        {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        }
        else
        {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
    }
}