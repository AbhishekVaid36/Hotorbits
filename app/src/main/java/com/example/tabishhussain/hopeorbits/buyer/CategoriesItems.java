package com.example.tabishhussain.hopeorbits.buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
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

public class CategoriesItems extends AppCompatActivity implements View.OnClickListener {
    GridView gridview;
    String itemID, itemName, itemImage, itemModelSet;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    TextView txtstorename, txtname, txtcredit,txtemptylist;
    DBHelper mydb;
    String PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price;
    RelativeLayout rlcart;
    ImageView imgcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_items);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        mydb = new DBHelper(CategoriesItems.this);
        gridview = (GridView) findViewById(R.id.gridview);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        txtemptylist=(TextView)findViewById(R.id.txtemptylist);
        rlcart = (RelativeLayout) findViewById(R.id.rlcart);
        imgcart = (ImageView) findViewById(R.id.imgcart);
        txtcredit.setText("Balance: \u20A8" + Home.amount);
        txtstorename = (TextView) findViewById(R.id.txtstorename);
        Intent in = getIntent();
        itemModelSet = in.getStringExtra("itemModelSet");
        PageId = in.getStringExtra("pageID");
        PageName = in.getStringExtra("pageName");
        CatId = in.getStringExtra("categoryID");
        CatName = in.getStringExtra("categoryName");
        txtstorename.setText(CatName);
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
                h.setQuantity("1");
                h.setSize("250 ml");
                h.setPrice("20\u20A8");
                list.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rlcart.setOnClickListener(this);
        if (list.size() > 0) {
            gridview.setAdapter(new MyCustomAdapter(CategoriesItems.this, list));
            txtemptylist.setVisibility(View.GONE);
        } else {
            txtemptylist.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlcart:
                Intent in = new Intent(CategoriesItems.this, BucketView.class);
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
                holder.checkbox = (CheckBox) paramView.findViewById(R.id.checkbox);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            StoreListHolder h = list.get(paramInt);
            String name = h.getItemName();
            holder.txtname.setText(name);
            holder.txtprice.setText("Price: " + h.getPrice());
            holder.txtquantity.setText("Quantity: " + h.getQuantity());
            if ((!h.getItemImage().equalsIgnoreCase("null")) && (!h.getItemImage().isEmpty())) {
                byte[] decodedString = Base64.decode(h.getItemImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgitems.setImageBitmap(decodedByte);
            }else {
                holder.imgitems.setBackgroundResource(R.mipmap.uploadimage);
            }
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
            holder.checkbox.setTag(paramInt);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                    ItemId = h1.getItemID();
                    ItemName = h1.getItemName();
                    Quantity = h1.getQuantity();
                    Size = h1.getSize();
                    Price = h1.getPrice();
                    new AddToCart().execute();
                }
            });
            return paramView;
        }
    }

    public class AddToCart extends AsyncTask<String, String, String> {
        ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(CategoriesItems.this);
            pdia.setMessage("Please wait..");
            pdia.show();

        }

        @Override
        protected String doInBackground(String... params) {
            mydb.addTocart(PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
        }
    }

}
