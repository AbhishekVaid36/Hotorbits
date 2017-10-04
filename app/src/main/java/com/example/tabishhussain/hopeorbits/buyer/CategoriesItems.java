package com.example.tabishhussain.hopeorbits.buyer;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.StoreListHolder;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesItems extends Fragment implements View.OnClickListener {
    GridView gridview;
    String itemID, itemName, itemImage, itemPrice, itemSize, itemQuantity;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    TextView txtstorename, txtemptylist;
    DBHelper mydb;
    String PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price;
    Fragment fragment;
    FragmentTransaction ft;
    JSONArray jsonArray;
    RelativeLayout rlfinish;
    private String[] list_designation;
    private ListPopupWindow lpw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_categories_items, container, false);
        mydb = new DBHelper(getActivity());
        gridview = (GridView) view.findViewById(R.id.gridview);
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        txtstorename = (TextView) view.findViewById(R.id.txtstorename);
        rlfinish = (RelativeLayout) view.findViewById(R.id.rlfinish);
        rlfinish.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        jsonArray = Categories.ItemModelSet;
        PageId = bundle.getString("pageID");
        PageName = bundle.getString("pageName");
        CatId = bundle.getString("categoryID");
        CatName = bundle.getString("categoryName");
        txtstorename.setText(CatName);
        try {
//            JSONArray jsonArray = new JSONArray(itemModelSet);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                itemID = obj.getString("itemID");
                itemName = obj.getString("itemName");
                itemImage = obj.getString("itemImage");
                itemPrice = obj.getString("itemPrice");
                itemSize = obj.getString("itemSize");
                itemQuantity = obj.getString("itemQuantity");
                StoreListHolder h = new StoreListHolder();
                h.setItemID(itemID);
                h.setItemName(itemName);
                h.setItemImage(itemImage);
                h.setQuantity(itemQuantity);
                h.setSize(itemSize);
                h.setPrice(itemPrice);
                list.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            gridview.setAdapter(new MyCustomAdapter(getActivity(), list));
            txtemptylist.setVisibility(View.GONE);
        } else {
            txtemptylist.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlfinish:
                fragment = new BucketView();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
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

            final ViewHolder holder;
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
            holder.txtname.setText(name + " " + h.getSize());
            holder.txtprice.setText("Price: " + h.getPrice() + "\u20A8");
            holder.txtquantity.setText(h.getQuantity());
            if ((!h.getItemImage().equalsIgnoreCase("null")) && (!h.getItemImage().isEmpty())) {
                byte[] decodedString = Base64.decode(h.getItemImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgitems.setImageBitmap(decodedByte);
            } else {
                holder.imgitems.setBackgroundResource(R.mipmap.uploadimage);
            }
            holder.rlitems.setTag(paramInt);
            holder.rlitems.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
//                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
//
//                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
//
//                    } else {
////                        int pos1 = (Integer) vv.getTag();
////                        StoreListHolder h1 = (StoreListHolder) list.get(pos1);
////                        categoryModels = h1.getCategoryModels();
////                        Intent in = new Intent(CategoriesItems.this, Categories.class);
////                        in.putExtra("categoryModels", categoryModels);
////                        startActivity(in);
//                        Toast.makeText(getActivity(), "I'm Working on these phase.", Toast.LENGTH_SHORT).show();
//
//                    }
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
                    Quantity =  holder.txtquantity.getText().toString();
                    Size = h1.getSize();
                    Price = h1.getPrice();
                    new AddToCart().execute();
                }
            });
            holder.txtquantity.setTag(paramInt);
            holder.txtquantity.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
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
                    return false;
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
//            pdia = new ProgressDialog(getActivity());
//            pdia.setMessage("Please wait..");
//            pdia.show();

        }

        @Override
        protected String doInBackground(String... params) {
            mydb.addTocart(PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pdia.dismiss();
            Toast.makeText(getActivity(), "Item added in cart", Toast.LENGTH_LONG).show();
        }
    }

}
