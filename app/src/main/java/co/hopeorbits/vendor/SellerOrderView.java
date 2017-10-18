package co.hopeorbits.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.buyer.Container;
import co.hopeorbits.holder.OrderListHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 09-Oct-17.
 */

public class SellerOrderView extends Fragment {
    ListView orderlistview;
    TextView txtemptylist;
    MyCustomAdapter customAdapter;
    Fragment fragment;
    FragmentTransaction ft;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String UserId, orderId, pageId, pageName, itemId, itemName,date, address, quantity, orderStatus, price, size, creditInfo;
    ArrayList<OrderListHolder> orderlist = new ArrayList<OrderListHolder>();
RelativeLayout rlheader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.seller_orderview, container, false);
        orderlistview = (ListView) view.findViewById(R.id.orderlistview);
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        rlheader=(RelativeLayout)view.findViewById(R.id.rlheader);
        rlheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getOrderByUser();
        return view;
    }

    public void getOrderByUser() {
        retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.getOrdersByUserId(getParams());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());
                        if (jsonObj.has("orders")) {
                            JSONArray jsonArray = new JSONArray(jsonObj.getString("orders"));
                            int length = jsonArray.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                orderId = obj.getString("id");
                                pageId = obj.getString("pageId");
                                pageName = obj.getString("pageName");
                                itemId = obj.getString("itemId");
                                address = obj.getString("address");
                                itemName = obj.getString("itemName");
                                date=obj.getString("date");
                                quantity = obj.getString("quantity");
                                orderStatus = obj.getString("orderStatus");
                                price = obj.getString("price");
                                size = obj.getString("size");
                                creditInfo = obj.getString("creditInfo");

                                OrderListHolder h = new OrderListHolder();
                                h.setOrderId(orderId);
                                h.setPageId(pageId);
                                h.setPageName(pageName);
                                h.setItemId(itemId);
                                h.setItemName(itemName);
                                h.setDate(date);
                                h.setQuantity(quantity);
                                h.setOrderStatus(orderStatus);
                                h.setPrice(price);
                                h.setSize(size);
                                h.setCreditInfo(creditInfo);

                                orderlist.add(h);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (orderlist.size() > 0) {
                        customAdapter = new MyCustomAdapter(getActivity(), orderlist);
                        orderlistview.setAdapter(customAdapter);
                        txtemptylist.setVisibility(View.GONE);
                    } else {
                        txtemptylist.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        Log.d("Tag", response.errorBody().string());
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

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserId = sharedpreferences.getString("Id", "");
        params.put("userId", UserId);
        return params;
    }

    public class MyCustomAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<OrderListHolder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<OrderListHolder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }


        @Override

        public int getCount() {
            return list.size();
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
            TextView txtname, txtorderno, txtstatus, txtdate, txtcredit;
            RelativeLayout rlcart;
            ImageView imgshop;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyCustomAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.seller_orderview_list_item, parent, false);
                holder = new MyCustomAdapter.ViewHolder();

                holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
                holder.txtorderno = (TextView) convertView.findViewById(R.id.txtorderno);
                holder.txtstatus = (TextView) convertView.findViewById(R.id.txtstatus);
                holder.txtdate = (TextView) convertView.findViewById(R.id.txtdate);
                holder.txtcredit = (TextView) convertView.findViewById(R.id.txtcredit);
                holder.rlcart = (RelativeLayout) convertView.findViewById(R.id.rlcart);
                holder.imgshop=(ImageView)convertView.findViewById(R.id.imgshop);
                convertView.setTag(holder);
            } else {
                holder = (MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            OrderListHolder h = list.get(position);
            holder.txtname.setText("Name: "+h.getPageName());
            holder.txtorderno.setText("Order No: "+h.getOrderId());
            if(h.getOrderStatus().contains("0"))
                holder.txtstatus.setText("Status: Pending");
                else
            holder.txtstatus.setText("Status: Completed");
            holder.txtdate.setText("Date: "+h.getDate());
            holder.txtcredit.setText("Credit: "+h.getCreditInfo());

            holder.rlcart.setTag(position);
            holder.rlcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    OrderListHolder h1 = (OrderListHolder) list.get(pos1);
                    Bundle bundle = new Bundle();
                    bundle.putString("pageId", h1.getPageId());
                    bundle.putString("pageName", h1.getPageName());
                    fragment = new SellerOrderViewItems();
                    fragment.setArguments(bundle);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + Container.add);
                    ft.commit();
                    Container.add++;
                }
            });
            holder.imgshop.setBackgroundResource(R.mipmap.product);
            return convertView;
        }
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        Methodexecute();
//        if (cartlist.size() > 0) {
//            customAdapter = new MyCustomAdapter(getActivity(), cartlist);
//            cartlistview.setAdapter(customAdapter);
//            txtemptylist.setVisibility(View.GONE);
//        } else {
//            cartlistview.removeAllViews();
//            txtemptylist.setVisibility(View.VISIBLE);
//        }
//    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

