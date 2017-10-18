package co.hopeorbits.vendor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by ADMIN on 09-Oct-17.
 */

public class SellerOrderViewItems extends Fragment implements View.OnClickListener {
    ListView orderlistview;
    TextView txtremove, txtok, txtoptions, txtname, txteditorder;
    ArrayList<String> selecteditems = new ArrayList<String>();
    MyCustomAdapter customAdapter;
    String userId, orderId, pageId, pageName, itemId, itemName, date, address, quantity, orderStatus, price, size;
    ArrayList<OrderListHolder> orderlist = new ArrayList<OrderListHolder>();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String UserId;
    PercentRelativeLayout rlbuttons;
    ArrayList<String> selectedItemId = new ArrayList<String>();
    RelativeLayout rlfirst;
    String select_type;
    String removeItemId, removeitemQuan, message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.sellerorderviewitem, container, false);
        orderlistview = (ListView) view.findViewById(R.id.orderlistview);
        txteditorder = (TextView) view.findViewById(R.id.txteditorder);
        txtremove = (TextView) view.findViewById(R.id.txtremove);
        txtok = (TextView) view.findViewById(R.id.txtok);
        txtoptions = (TextView) view.findViewById(R.id.txtoptions);
        rlbuttons = (PercentRelativeLayout) view.findViewById(R.id.rlbuttons);
        txtname = (TextView) view.findViewById(R.id.txtname);
        rlfirst = (RelativeLayout) view.findViewById(R.id.rlfirst);
        Bundle bundle = this.getArguments();
        pageName = bundle.getString("pageName");
        pageId = bundle.getString("pageId");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserId = sharedpreferences.getString("Id", "");
        txtname.setText(pageName);
        getOrderByPageId();

        txteditorder.setOnClickListener(this);
        txtremove.setOnClickListener(this);
        txtok.setOnClickListener(this);
        txtoptions.setOnClickListener(this);
        rlfirst.setOnClickListener(this);
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
                                userId = obj.getString("userId");
                                if (userId.equalsIgnoreCase(UserId)) {
                                    orderId = obj.getString("id");
                                    pageId = obj.getString("pageId");
                                    pageName = obj.getString("pageName");
                                    itemId = obj.getString("itemId");
                                    date = obj.getString("date");
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
                                    h.setDate(date);
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
                        customAdapter = new MyCustomAdapter(getActivity(), orderlist, false, false);
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

    public HashMap<String, Object> getremoveorderParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pageId", pageId);
        params.put("itemId", removeItemId);
        params.put("quantity", removeitemQuan);
        params.put("itemUnavailbleReason", select_type);
        params.put("deliveryNotPossibleReason", select_type);
        return params;
    }


    public class MyCustomAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<OrderListHolder> cartlistitem;
        Boolean edit, remove;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<OrderListHolder> list, Boolean edit, Boolean remove) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.cartlistitem = list;
            this.edit = edit;
            this.remove = remove;
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
            ImageView imgshop;
            Spinner sp_reason;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.sellerorderviewitem_list_item, parent, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
                holder.txtsize = (TextView) convertView.findViewById(R.id.txtsize);
                holder.txtquantity = (TextView) convertView.findViewById(R.id.txtquantity);
                holder.txtprice = (TextView) convertView.findViewById(R.id.txtprice);
                holder.checkforremove = (CheckBox) convertView.findViewById(R.id.checkforremove);
                holder.imgshop = (ImageView) convertView.findViewById(R.id.imgshop);
                holder.sp_reason = (Spinner) convertView.findViewById(R.id.sp_reason);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ArrayAdapter<CharSequence> CancelAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.cancel_order, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            CancelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            holder.sp_reason.setAdapter(CancelAdapter);
            holder.sp_reason.setTag(position);
            holder.sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    select_type = holder.sp_reason.getSelectedItem().toString();
                    if (select_type.equals("Select Reason")) {
                        select_type = "";
                    } else {
//                        int pos1 = (Integer) view.getTag();
                        OrderListHolder h1 = (OrderListHolder) cartlistitem.get(i);
                        removeItemId = h1.getItemId();
                        removeitemQuan = h1.getQuantity();
                        removeOrder();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            OrderListHolder h = cartlistitem.get(position);
            holder.txtname.setText("Item: " + h.getItemName());
            holder.txtsize.setText("Size: " + h.getSize());
            holder.txtquantity.setText(h.getQuantity());
            holder.txtprice.setText("Price: " + h.getPrice() + "\u20A8");
            if (edit) {
                holder.checkforremove.setVisibility(View.VISIBLE);
                holder.sp_reason.setVisibility(View.VISIBLE);
            } else if (remove) {
                holder.checkforremove.setVisibility(View.VISIBLE);
                holder.sp_reason.setVisibility(View.GONE);
            } else {
                holder.checkforremove.setVisibility(View.GONE);
                holder.sp_reason.setVisibility(View.GONE);
            }
            holder.imgshop.setBackgroundResource(R.mipmap.product);
            holder.checkforremove.setTag(position);
            holder.checkforremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    OrderListHolder h1 = (OrderListHolder) cartlistitem.get(pos1);
                    if (holder.checkforremove.isChecked()) {
                        selectedItemId.add(h1.getItemId());
                    } else {
                        selectedItemId.remove(h1.getItemId());
                    }
                }
            });
            return convertView;
        }
    }

    public void removeOrder() {
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        retrofit2.Call<JsonObject> call = HopeOrbitApi.retrofit.removeOrder(getremoveorderParams());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());
                        message = jsonObj.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Tag", t.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtoptions:
                rlbuttons.setVisibility(View.VISIBLE);
                txtoptions.setVisibility(View.GONE);
                customAdapter = new MyCustomAdapter(getActivity(), orderlist, false, true);
                orderlistview.setAdapter(customAdapter);
                break;
            case R.id.txteditorder:
                customAdapter = new MyCustomAdapter(getActivity(), orderlist, true, true);
                orderlistview.setAdapter(customAdapter);
                break;
            case R.id.txtok:
                selectedItemId.clear();
                txtoptions.setVisibility(View.VISIBLE);
                rlbuttons.setVisibility(View.GONE);
                customAdapter = new MyCustomAdapter(getActivity(), orderlist, false, false);
                orderlistview.setAdapter(customAdapter);
                break;
            case R.id.txtremove:
                if (selectedItemId.size() > 0)
                    myAlertDialog();
                else
                    Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_LONG).show();
                break;
            case R.id.rlfirst:
                break;
        }
    }

    private void myAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder
                .setMessage(" Are you sure you want to remove the selected category? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        new DeleteCatItemProgress().execute();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}

