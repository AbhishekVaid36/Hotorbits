package com.example.tabishhussain.hopeorbits.buyer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;

import java.util.ArrayList;

public class BucketView extends Fragment {
    DBHelper mydb;
    String PageName, PageId;
    Cursor c;
    int cart_length;
    public static ArrayList<StoreListHolder> cartlist = new ArrayList<StoreListHolder>();
    ListView cartlistview;
    TextView txtemptylist;
    MyCustomAdapter customAdapter;
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_bucket_view, container, false);
        cartlistview = (ListView) view.findViewById(R.id.cartlistview);
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        mydb = new DBHelper(getActivity());
        Methodexecute();
        if (cartlist.size() > 0) {
            customAdapter = new MyCustomAdapter(getActivity(), cartlist);
            cartlistview.setAdapter(customAdapter);
            txtemptylist.setVisibility(View.GONE);
        } else {
            txtemptylist.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void Methodexecute() {
        c = mydb.getallcartData();
        cart_length = c.getCount();
        cartlist.clear();
        if (cart_length > 0) {
            if (c != null) {
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
                    Bundle bundle = new Bundle();
                    bundle.putString("pageId", h1.getPageID());
                    bundle.putString("pageName", h1.getPageName());
                    fragment = new BucketViewItems();
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
