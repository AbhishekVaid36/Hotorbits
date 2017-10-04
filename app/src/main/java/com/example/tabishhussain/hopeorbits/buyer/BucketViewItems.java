package com.example.tabishhussain.hopeorbits.buyer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;
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

public class BucketViewItems extends Fragment implements View.OnClickListener {
    DBHelper mydb;
    String PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price, UserId, message;
    Cursor c;
    int cart_length;
    public static ArrayList<StoreListHolder> cartlistitem = new ArrayList<StoreListHolder>();
    ListView bucketview;
    TextView txtcancel, txtremove, txtorder;
    ArrayList<String> selecteditems = new ArrayList<String>();
    ArrayList<String> selecteditemsIdfororder = new ArrayList<String>();
    ArrayList<String> selecteditemsQuanfororder = new ArrayList<String>();
    MyCustomAdapter customAdapter;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String[] list_designation;
    private ListPopupWindow lpw;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_bucket_view_items, container, false);
        bucketview = (ListView) view.findViewById(R.id.bucketview);
        txtcancel = (TextView) view.findViewById(R.id.txtcancel);
        txtremove = (TextView) view.findViewById(R.id.txtremove);
        txtorder = (TextView) view.findViewById(R.id.txtorder);
        Bundle bundle = this.getArguments();
        PageName = bundle.getString("pageName");
        PageId = bundle.getString("pageId");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserId = sharedpreferences.getString("Id", "");
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
        c = mydb.getallcartItemData(PageId);
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
            holder.txtprice.setText("Price: " + h.getPrice() + "\u20A8");

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
            holder.checkfororder.setTag(position);
            holder.checkfororder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    StoreListHolder h1 = (StoreListHolder) cartlistitem.get(pos1);
                    if (holder.checkfororder.isChecked()) {
                        selecteditemsIdfororder.add(h1.getItemID());
                        selecteditemsQuanfororder.add(holder.txtquantity.getText().toString());
                    } else {
                        selecteditemsIdfororder.remove(h1.getItemID());
                        selecteditemsQuanfororder.remove(holder.txtquantity.getText().toString());
                    }
                }
            });
            holder.txtquantity.setTag(position);
            holder.txtquantity.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (holder.checkfororder.isChecked()) {
                        Toast.makeText(getActivity(), "Please unselect first", Toast.LENGTH_LONG).show();
                    } else {
                        list_designation = getResources().getStringArray(R.array.item_quantity);
                        lpw = new ListPopupWindow(getActivity());
                        lpw.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_designation));
                        lpw.setAnchorView(holder.txtquantity);
                        lpw.setModal(true);
                        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = list_designation[position];
                                holder.txtquantity.setText(item);
                                lpw.dismiss();
                            }
                        });
                        lpw.show();
                    }
                    return false;
                }
            });

            return convertView;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtcancel:
                customAdapter.notifyDataSetChanged();
                break;
            case R.id.txtremove:
                boolean a = false;
                if (selecteditems.size() > 0)
                    a = mydb.delete(selecteditems);
                if (a) {
                    selecteditems.clear();
                    Methodexecute();
                    if (cartlistitem.size() > 0) {
                        customAdapter = new MyCustomAdapter(getActivity(), cartlistitem, false);
                        bucketview.setAdapter(customAdapter);
                    } else {
                        for(int i=0;i<2;i++)
                        {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.popBackStack();
                        }
                        fragment = new BucketView();
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + Container.add);
                        ft.commit();
                        Container.add++;
                    }
                } else
                    Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_LONG).show();

                break;
            case R.id.txtorder:
                if (selecteditemsIdfororder.size() > 0) {
                    new OrderProgress().execute();
                } else
                    Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_LONG).show();


                break;

        }
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
            JSONArray ja_inner = null;
            try {
                ja_outer = new JSONArray();
//            while (rs.next()) {
                JSONObject jo_page = new JSONObject();
                jo_page.put("pageId", PageId);

                JSONObject jo_item = null;
                ja_inner = new JSONArray();
                for (int i = 0; i < selecteditemsIdfororder.size(); i++) {
                    jo_item = new JSONObject();
                    jo_item.put("id", selecteditemsIdfororder.get(i));
                    jo_item.put("quantity", selecteditemsQuanfororder.get(i));
//                    ja_inner = new JSONArray();
                    ja_inner.put(jo_item);
                }

                jo_page.put("productItemslist", ja_inner);
                ja_outer.put(jo_page);
                JSONObject j = new JSONObject();
                j.put("userId", UserId);
                j.put("placedOrderList", ja_outer);
//            }
                try {
                    int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                    HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                    HttpClient client = new DefaultHttpClient(httpParams);
                    HttpPost request = new HttpPost("http://13.58.110.101:8080/hoprepositoryweb/manageOrder/placeOrder");
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");

                    request.setEntity(new StringEntity(j.toString()));
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
                    try {
                        JSONObject jsonObj = new JSONObject(sb.toString());
                        if (jsonObj.has("message")) {
                            message = jsonObj.getString("message");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            boolean a = false;
            if (selecteditemsIdfororder.size() > 0)
                a = mydb.delete(selecteditemsIdfororder);
            if (a) {
                selecteditemsIdfororder.clear();
                selecteditemsQuanfororder.clear();
            }
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();

        }

    }

}
