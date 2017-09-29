package com.example.tabishhussain.hopeorbits.buyer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    TextView txtemptylist;
    String pageModelList, pageID, pageName, currency, details, pageImage, errorMessage, categoryModels;
    String categoryID, categoryName, error, categoryImage, itemModelSet;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    ListView storelistview;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog2;
    //    public static String CategoryListSet;
    public static JSONArray CategoryListSet;
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_home, container, false);

        initpDialog();
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        storelistview = (ListView) view.findViewById(R.id.storelistview);

        getStores();
//        new GetAllPages().execute();
        return view;
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
            storelistview.setAdapter(new MyCustomAdapter(getActivity(), list));
        }

    }

    public void getStores() {
        showpDialog();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Call<JsonObject> call = HopeOrbitApi.retrofit.getAllPages();
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
                                storelistview.setAdapter(new MyCustomAdapter(getActivity(), list));
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
            } else {
                holder.imgstore.setBackgroundResource(R.mipmap.uploadimage);
            }

            holder.rlstore.setTag(paramInt);
            holder.rlstore.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) vv.getTag();
                        StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                        try {
                            CategoryListSet = new JSONArray(h1.getCategoryModels());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("pageID", h1.getPageID());
                        bundle.putString("pageName", h1.getPageName());
                        fragment = new Categories();
                        fragment.setArguments(bundle);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + Container.add);
                        ft.commit();
                        Container.add++;


//                        Intent in = new Intent(Home.this, Categories.class);
//                        in.putExtra("categoryModels", categoryModels);
//                        in.putExtra("pageID", h1.getPageID());
//                        in.putExtra("pageName", h1.getPageName());
//                        startActivity(in);
                    }
                }
            });
            return paramView;
        }
    }

    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Double screenInches = Math.sqrt(x + y);

        Integer inch = screenInches.intValue();
        if (inch >= 5) {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        } else {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
    }
}