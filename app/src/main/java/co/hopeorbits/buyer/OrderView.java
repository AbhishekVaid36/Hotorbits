package co.hopeorbits.buyer;

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
import co.hopeorbits.holder.OrderListHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderView extends Fragment {
    ListView orderlistview;
    TextView txtemptylist;
    MyCustomAdapter customAdapter;
    Fragment fragment;
    FragmentTransaction ft;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String UserId, orderId, pageId, pageName, itemId, itemName, address, quantity, orderStatus, price, size;
    ArrayList<OrderListHolder> orderlist = new ArrayList<OrderListHolder>();
    RelativeLayout rlheader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_order_view, container, false);
        orderlistview = (ListView) view.findViewById(R.id.orderlistview);
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        rlheader = (RelativeLayout) view.findViewById(R.id.rlheader);
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
                                quantity = obj.getString("quantity");
                                orderStatus = obj.getString("orderStatus");
                                price = obj.getString("price");
                                size = obj.getString("size");

                                OrderListHolder h = new OrderListHolder();
                                h.setOrderId(orderId);
                                h.setPageId(pageId);
                                h.setPageName(pageName);
                                h.setItemId(itemId);
                                h.setItemName(itemName);
                                h.setQuantity(quantity);
                                h.setOrderStatus(orderStatus);
                                h.setPrice(price);
                                h.setSize(size);

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
            TextView txtcartname;
            RelativeLayout rlcart;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.bucketview_list_item, parent, false);
                holder = new ViewHolder();

                holder.txtcartname = (TextView) convertView.findViewById(R.id.txtcartname);
                holder.rlcart = (RelativeLayout) convertView.findViewById(R.id.rlcart);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            OrderListHolder h = list.get(position);
            String storename = h.getPageName();
            holder.txtcartname.setText(storename);
            holder.rlcart.setTag(position);
            holder.rlcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    OrderListHolder h1 = (OrderListHolder) list.get(pos1);
                    Bundle bundle = new Bundle();
                    bundle.putString("pageId", h1.getPageId());
                    bundle.putString("pageName", h1.getPageName());
                    fragment = new OrderViewItems();
                    fragment.setArguments(bundle);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + Container.add);
                    ft.commit();
                    Container.add++;
                }
            });

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
