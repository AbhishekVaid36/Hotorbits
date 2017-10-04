package com.example.tabishhussain.hopeorbits.buyer;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewItems extends Fragment implements View.OnClickListener {
    DBHelper mydb;
    String PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price;
    Cursor c;
    int cart_length;
    public static ArrayList<StoreListHolder> cartlistitem = new ArrayList<StoreListHolder>();
    ListView bucketview;
    TextView txtcancel, txtremove, txtorder;
    ArrayList<String> selecteditems = new ArrayList<String>();
    MyCustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_order_view_items, container, false);
        bucketview = (ListView) view.findViewById(R.id.bucketview);
        txtcancel = (TextView) view.findViewById(R.id.txtcancel);
        txtremove = (TextView) view.findViewById(R.id.txtremove);
        txtorder = (TextView) view.findViewById(R.id.txtorder);
        Bundle bundle = this.getArguments();
        PageName = bundle.getString("pageName");
        PageId = bundle.getString("pageId");
        mydb = new DBHelper(getActivity());
        Methodexecute();
        customAdapter = new MyCustomAdapter(getActivity(), cartlistitem, false);
        bucketview.setAdapter(customAdapter);
        txtcancel.setOnClickListener(this);
        txtremove.setOnClickListener(this);
        txtorder.setOnClickListener(this);
        return view;
    }

    public void Methodexecute() {
        c = mydb.getallcartItemDataDummy(PageId);
        cart_length = c.getCount();

        if (c != null) {
            cartlistitem.clear();
            c.moveToFirst();
        }
        do {
            try {

                PageName = c.getString(c.getColumnIndex("pageName"));
                PageId = c.getString(c.getColumnIndex("pageId"));
                CatName = c.getString(c.getColumnIndex("catName"));
                CatId = c.getString(c.getColumnIndex("catId"));
                ItemName = c.getString(c.getColumnIndex("productName"));
                ItemId = c.getString(c.getColumnIndex("productId"));
                Quantity = c.getString(c.getColumnIndex("quantity"));
                Size = c.getString(c.getColumnIndex("size"));
                Price = c.getString(c.getColumnIndex("price"));

                StoreListHolder h = new StoreListHolder();


                h.setPageName(PageName);
                h.setPageID(PageId);
                h.setCategoryName(CatName);
                h.setCategoryID(CatId);
                h.setItemName(ItemName);
                h.setItemID(ItemId);
                h.setQuantity(Quantity);
                h.setSize(Size);
                h.setPrice(Price);


                cartlistitem.add(h);
            } catch (IndexOutOfBoundsException e) {

            } catch (OutOfMemoryError e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (c.moveToNext());

    }

    public class MyCustomAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<StoreListHolder> cartlistitem;
        Boolean flag;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<StoreListHolder> list, Boolean flag) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.cartlistitem = list;
            this.flag = flag;
        }


        @Override

        public int getCount() {
            return cartlistitem.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView txtname, txtsize, txtquantity, txtprice;
            CheckBox checkfororder, checkforremove;
            CircularImageView imgshop;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.bucketviewitems_list_item, parent, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
                holder.txtsize = (TextView) convertView.findViewById(R.id.txtsize);
                holder.txtquantity = (TextView) convertView.findViewById(R.id.txtquantity);
                holder.txtprice = (TextView) convertView.findViewById(R.id.txtprice);
                holder.checkfororder = (CheckBox) convertView.findViewById(R.id.checkfororder);
                holder.checkforremove = (CheckBox) convertView.findViewById(R.id.checkforremove);
                holder.imgshop = (CircularImageView) convertView.findViewById(R.id.imgshop);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StoreListHolder h = cartlistitem.get(position);
            holder.txtname.setText("Item: " + h.getItemName());
            holder.txtsize.setText("Size: " + h.getSize());
            holder.txtquantity.setText(h.getQuantity());
            holder.txtprice.setText("Price: " + h.getPrice()+ "\u20A8");
            if (!flag) {
                holder.checkforremove.setChecked(false);
                holder.checkfororder.setChecked(false);
            }

            holder.checkforremove.setTag(position);
            holder.checkforremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    StoreListHolder h1 = (StoreListHolder) cartlistitem.get(pos1);
                    if (holder.checkforremove.isChecked()) {
                        selecteditems.add(h1.getItemID());
                    } else {
                        selecteditems.remove(h1.getItemID());
                    }
                }
            });
            return convertView;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtcancel:
//                customAdapter.notifyDataSetChanged();
                break;
            case R.id.txtremove:
//                boolean a = false;
//                if (selecteditems.size() > 0)
//                    a = mydb.delete(selecteditems);
//                if (a) {
//                    selecteditems.clear();
//                    Methodexecute();
//                    if (cartlistitem.size() > 0) {
//                        customAdapter = new MyCustomAdapter(getActivity(), cartlistitem, false);
//                        bucketview.setAdapter(customAdapter);
//                    } else {
//                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        manager.popBackStack();
//                    }
//                } else
//                    Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_LONG).show();

                break;
            case R.id.txtorder:
//                orderPlace();
//                new OrderProgress().execute();
                break;

        }
    }

    public void orderPlace() {
//        showpDialog();
        Call<JsonObject> call = HopeOrbitApi.retrofit.orderPlace(getorderPlaceParams());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());

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
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Tag", t.toString());
//                hidepDialog();
            }
        });
    }

    public HashMap<String, Object> getorderPlaceParams() {
//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        HashMap<String, Object> params = new HashMap<String, Object>();
//        try
//        {
//            JSONObject page = new JSONObject();
//            try {
//                page.put("pageId",PageId);
//
//                JSONObject items = new JSONObject();
//                try {
//                    items.put("id", "Grocery9");
//                    items.put("quantity", "1");
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                JSONArray ItemsList = new JSONArray();
//                ItemsList.put(items);
//                page.put("productItemslist",ItemsList.toString());
//
//                JSONArray pageList = new JSONArray();
//                pageList.put(page);
//
//                params.put("manageOrder",pageList.toString());
//
//
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        } catch (Exception e){
//        }
        JSONArray ja_outer;
        JSONArray ja_inner;
        try {
            ja_outer = new JSONArray();
//            while (rs.next()) {
            JSONObject jo_page = new JSONObject();
            jo_page.put("pageId", PageId);

            JSONObject jo_item = new JSONObject();
            jo_item.put("id", "21");
            jo_item.put("quantity", "1");

            ja_inner = new JSONArray();
            ja_inner.put(jo_item);
            jo_page.put("productItemslist", ja_inner);
            ja_outer.put(jo_page);
//            }

            params.put("manageOrder", ja_outer.toString());
            StringEntity entity = new StringEntity(ja_outer.toString());
        } catch (Exception e) {
        }
        return params;
    }

    public class OrderProgress extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONArray ja_outer;
            JSONArray ja_inner;
            try {
                ja_outer = new JSONArray();
//            while (rs.next()) {
                JSONObject jo_page = new JSONObject();
                jo_page.put("pageId", "123678");

                JSONObject jo_item = new JSONObject();
                jo_item.put("id", "1237");
                jo_item.put("quantity", "15");

                ja_inner = new JSONArray();
                ja_inner.put(jo_item);
                jo_page.put("productItemslist", ja_inner);
                ja_outer.put(jo_page);
//            }
                try {
                    int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                    HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                    HttpClient client = new DefaultHttpClient(httpParams);
                    HttpPost request = new HttpPost("http://13.58.110.101:8080/hoprepositoryweb/manageOrder");
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");

                    request.setEntity(new StringEntity(ja_outer.toString()));
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    response.getEntity().getContentLength();  //it should not be 0


                    StringBuilder sb = new StringBuilder();
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(entity.getContent()), 65728);
                        String line = null;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    System.out.println("finalResult " + sb.toString());

                } catch (Exception e) {
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

}
