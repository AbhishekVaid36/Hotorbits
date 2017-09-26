package com.example.tabishhussain.hopeorbits.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.connectiondetector.ConnectionDetector;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesItems extends AppCompatActivity {
    GridView gridview;
    String itemID, itemName, itemImage, itemModelSet;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    TextView txtstorename,txtname, txtcredit;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_items);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        gridview = (GridView) findViewById(R.id.gridview);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        txtcredit.setText("Balance: \u20A8" + Home.amount);
        txtstorename = (TextView) findViewById(R.id.txtstorename);
        Intent in = getIntent();
        itemModelSet = in.getStringExtra("itemModelSet");
        txtstorename.setText(in.getStringExtra("categoryName"));
        try {
            JSONArray jsonArray = new JSONArray(itemModelSet);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                itemID = obj.getString("itemID");
                itemName = obj.getString("itemName");
                itemImage = obj.getString("itemImage");
                StoreListHolder h = new StoreListHolder();
                h.setItemID(itemID);
                h.setItemName(itemName);
                h.setItemImage(itemImage);
                list.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridview.setAdapter(new MyCustomAdapter(CategoriesItems.this, list));
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
            TextView txtname, txtprice, txtquantity;
            CircularImageView imgitems;
            RelativeLayout rlitems;
            CheckBox checkbox;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.categoriesitems_list_item, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) paramView.findViewById(R.id.txtname);
                holder.txtprice = (TextView) paramView.findViewById(R.id.txtprice);
                holder.txtquantity = (TextView) paramView.findViewById(R.id.txtquantity);
                holder.imgitems = (CircularImageView) paramView.findViewById(R.id.imgitems);
                holder.rlitems = (RelativeLayout) paramView.findViewById(R.id.rlitems);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            StoreListHolder h = list.get(paramInt);
            String name = h.getItemName();
            holder.txtname.setText(name);
            holder.txtprice.setText("Price:20\u20A8");
            holder.txtquantity.setText("Quantity: 1");

            holder.rlitems.setTag(paramInt);
            holder.rlitems.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(CategoriesItems.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
//                        int pos1 = (Integer) vv.getTag();
//                        StoreListHolder h1 = (StoreListHolder) list.get(pos1);
//                        categoryModels = h1.getCategoryModels();
//                        Intent in = new Intent(CategoriesItems.this, Categories.class);
//                        in.putExtra("categoryModels", categoryModels);
//                        startActivity(in);
                        Toast.makeText(CategoriesItems.this, "I'm Working on these phase.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            return paramView;
        }
    }

}
