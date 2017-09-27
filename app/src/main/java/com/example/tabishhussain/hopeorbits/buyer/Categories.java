package com.example.tabishhussain.hopeorbits.buyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class Categories extends AppCompatActivity implements View.OnClickListener{
    GridView gridview;
    String categoryModels;
    String pageID, pageName, categoryID, categoryName, error, categoryImage, itemModelSet;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    TextView txtstorename, txtname, txtcredit, txtemptylist;
    RelativeLayout rlcart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        gridview = (GridView) findViewById(R.id.gridview);
        txtstorename = (TextView) findViewById(R.id.txtstorename);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        txtemptylist = (TextView) findViewById(R.id.txtemptylist);
        rlcart = (RelativeLayout) findViewById(R.id.rlcart);
        txtcredit.setText("Balance: \u20A8" + Home.amount);
        Intent in = getIntent();
        categoryModels = in.getStringExtra("categoryModels");
        pageName = in.getStringExtra("pageName");
        pageID = in.getStringExtra("pageID");
        txtstorename.setText(pageName);
        try {
            JSONArray jsonArray = new JSONArray(categoryModels);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                categoryID = obj.getString("categoryID");
                categoryName = obj.getString("categoryName");
                error = obj.getString("error");
                categoryImage = obj.getString("categoryImage");
                itemModelSet = obj.getString("itemModelSet");
                StoreListHolder h = new StoreListHolder();
                h.setCategoryID(categoryID);
                h.setCategoryName(categoryName);
                h.setError(error);
                h.setCategoryImage(categoryImage);
                h.setItemModelSet(itemModelSet);
                list.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            gridview.setAdapter(new MyCustomAdapter(Categories.this, list));
            txtemptylist.setVisibility(View.GONE);
        } else {
            txtemptylist.setVisibility(View.VISIBLE);
        }
        rlcart.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlcart:
                Intent in = new Intent(Categories.this, BucketView.class);
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
            TextView txtcategoryname;
            CircularImageView imgcategory;
            RelativeLayout rlcategory;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.categories_list_items, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txtcategoryname = (TextView) paramView.findViewById(R.id.txtcategoryname);
                holder.imgcategory = (CircularImageView) paramView.findViewById(R.id.imgcategory);
                holder.rlcategory = (RelativeLayout) paramView.findViewById(R.id.rlcategory);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            StoreListHolder h = list.get(paramInt);
            String name = h.getCategoryName();
            holder.txtcategoryname.setText(name);
            if ((!h.getCategoryImage().equalsIgnoreCase("null")) && (!h.getCategoryImage().isEmpty())) {
                byte[] decodedString = Base64.decode(h.getCategoryImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgcategory.setImageBitmap(decodedByte);
            }else {
                holder.imgcategory.setBackgroundResource(R.mipmap.uploadimage);
            }
            holder.rlcategory.setTag(paramInt);
            holder.rlcategory.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(Categories.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) vv.getTag();
                        StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                        itemModelSet = h1.getItemModelSet();
                        Intent in = new Intent(Categories.this, CategoriesItems.class);
                        in.putExtra("itemModelSet", itemModelSet);
                        in.putExtra("pageID", pageID);
                        in.putExtra("pageName", pageName);
                        in.putExtra("categoryID", h1.getCategoryID());
                        in.putExtra("categoryName", h1.getCategoryName());
                        startActivity(in);
                    }
                }
            });
            return paramView;
        }
    }

}
