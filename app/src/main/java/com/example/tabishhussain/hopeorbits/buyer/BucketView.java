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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;

import java.util.ArrayList;

public class BucketView extends AppCompatActivity {
    DBHelper mydb;
    String PageName, PageId;
    RelativeLayout rlcart;
    ImageView imgcart;
    Cursor c;
    int cart_length;
    public static ArrayList<StoreListHolder> cartlist = new ArrayList<StoreListHolder>();
    ListView cartlistview;
    TextView txtemptylist, txtcredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_view);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        cartlistview = (ListView) findViewById(R.id.cartlistview);
        txtemptylist = (TextView) findViewById(R.id.txtemptylist);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        txtcredit.setText("Balance: \u20A8" + Home.amount);
        mydb = new DBHelper(BucketView.this);
        Methodexecute();
        if (cartlist.size() > 0) {
            cartlistview.setAdapter(new MyCustomAdapter(BucketView.this, cartlist));
            txtemptylist.setVisibility(View.GONE);
        } else {
            txtemptylist.setVisibility(View.VISIBLE);
        }


    }

    public void Methodexecute() {
        c = mydb.getallcartData();
        cart_length = c.getCount();

        if (c != null) {
            cartlist.clear();
            c.moveToFirst();
        }
        do {
            try {

                PageName = c.getString(c.getColumnIndex("pageName"));
                PageId = c.getString(c.getColumnIndex("pageId"));

                StoreListHolder h = new StoreListHolder();


                h.setPageName(PageName);
                h.setPageID(PageId);
                cartlist.add(h);
            } catch (IndexOutOfBoundsException e) {

            } catch (OutOfMemoryError e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (c.moveToNext());

    }

    public class MyCustomAdapter extends BaseAdapter {
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

            StoreListHolder h = list.get(position);
            String storename = h.getPageName();
            holder.txtcartname.setText(storename);
            holder.rlcart.setTag(position);
            holder.rlcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                    Intent in = new Intent(BucketView.this, BucketViewItems.class);
                    in.putExtra("pageId", h1.getPageID());
                    in.putExtra("pageName", h1.getPageName());
                    startActivity(in);
                }
            });

            return convertView;
        }
    }

}
