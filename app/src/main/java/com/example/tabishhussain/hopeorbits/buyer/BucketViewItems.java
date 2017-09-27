package com.example.tabishhussain.hopeorbits.buyer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class BucketViewItems extends AppCompatActivity {
    DBHelper mydb;
    String PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price;
    RelativeLayout rlcart;
    ImageView imgcart;
    Cursor c;
    int cart_length;
    public static ArrayList<StoreListHolder> cartlistitem = new ArrayList<StoreListHolder>();
    ListView bucketview;
TextView txtcredit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_view_items);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        bucketview = (ListView) findViewById(R.id.bucketview);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        txtcredit.setText("Balance: \u20A8" + Home.amount);
        Intent in = getIntent();
        PageName = in.getStringExtra("pageName");
        PageId = in.getStringExtra("pageId");
        mydb = new DBHelper(BucketViewItems.this);
        Methodexecute();
        bucketview.setAdapter(new MyCustomAdapter(BucketViewItems.this, cartlistitem));

    }

    public void Methodexecute() {
        c = mydb.getallcartItemData();
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

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<StoreListHolder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.cartlistitem = list;
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
            holder.txtname.setText(h.getPageName());
            holder.txtsize.setText(h.getSize());
            holder.txtquantity.setText(h.getQuantity());
            holder.txtprice.setText(h.getPrice());

            return convertView;
        }
    }

}
