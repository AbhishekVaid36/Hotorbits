package co.hopeorbits.buyer;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;

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

public class OrderViewItems extends Fragment implements View.OnClickListener {
    ListView orderlistview;
//    TextView txtcancel, txtremove, txtorder;
    ArrayList<String> selecteditems = new ArrayList<String>();
    MyCustomAdapter customAdapter;
    String userId,orderId, pageId, pageName, itemId, itemName, address, quantity, orderStatus, price, size;
    ArrayList<OrderListHolder> orderlist = new ArrayList<OrderListHolder>();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String UserId;
    TextView txtname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_order_view_items, container, false);
        orderlistview = (ListView) view.findViewById(R.id.orderlistview);
        txtname=(TextView)view.findViewById(R.id.txtname);
        Bundle bundle = this.getArguments();
        pageName = bundle.getString("pageName");
        pageId = bundle.getString("pageId");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserId = sharedpreferences.getString("Id", "");
        txtname.setText(pageName);
        getOrderByPageId();

//        txtcancel.setOnClickListener(this);
//        txtremove.setOnClickListener(this);
//        txtorder.setOnClickListener(this);
        return view;
    }

    public void getOrderByPageId() {
        retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.getOrdersByPageId(getParams());
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
                                userId= obj.getString("userId");
                                if(userId.equalsIgnoreCase(UserId))
                                {
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (orderlist.size() > 0) {
                        customAdapter = new MyCustomAdapter(getActivity(), orderlist, false);
                        orderlistview.setAdapter(customAdapter);
                    } else {
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
        params.put("pageId", pageId);
        return params;
    }


    public class MyCustomAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<OrderListHolder> cartlistitem;
        Boolean flag;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<OrderListHolder> list, Boolean flag) {
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
            CheckBox checkforremove;
            CircularImageView imgshop;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.orderviewitem_list_item, parent, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
                holder.txtsize = (TextView) convertView.findViewById(R.id.txtsize);
                holder.txtquantity = (TextView) convertView.findViewById(R.id.txtquantity);
                holder.txtprice = (TextView) convertView.findViewById(R.id.txtprice);
                holder.checkforremove = (CheckBox) convertView.findViewById(R.id.checkforremove);
                holder.imgshop = (CircularImageView) convertView.findViewById(R.id.imgshop);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            OrderListHolder h = cartlistitem.get(position);
            holder.txtname.setText("Item: " + h.getItemName());
            holder.txtsize.setText("Size: " + h.getSize());
            holder.txtquantity.setText(h.getQuantity());
            holder.txtprice.setText("Price: " + h.getPrice() + "\u20A8");
            if (!flag) {
                holder.checkforremove.setChecked(false);
            }

            holder.checkforremove.setTag(position);
            holder.checkforremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    OrderListHolder h1 = (OrderListHolder) cartlistitem.get(pos1);
                    if (holder.checkforremove.isChecked()) {
                        selecteditems.add(h1.getItemId());
                    } else {
                        selecteditems.remove(h1.getItemId());
                    }
                }
            });
            holder.imgshop.setBackgroundResource(R.mipmap.product);
            return convertView;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
