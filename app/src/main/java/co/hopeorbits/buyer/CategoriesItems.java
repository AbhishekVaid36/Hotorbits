package co.hopeorbits.buyer;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.hopeorbits.R;
import co.hopeorbits.holder.StoreListHolder;

public class CategoriesItems extends Fragment implements View.OnClickListener {
    ListView gridview;
    String itemID, itemName, itemImage, itemPrice, itemSize, itemQuantity;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    TextView txtstorename, txtemptylist;
    DBHelper mydb;
    String PageId, PageName, CatId, CatName, page, ItemId, ItemName, Quantity, Size, Price, ItemImage;
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
        gridview = (ListView) view.findViewById(R.id.gridview);
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        txtstorename = (TextView) view.findViewById(R.id.txtstorename);
        rlfinish = (RelativeLayout) view.findViewById(R.id.rlfinish);
        rlfinish.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        PageId = bundle.getString("pageID");
        PageName = bundle.getString("pageName");
        CatId = bundle.getString("categoryID");
        CatName = bundle.getString("categoryName");
        page = bundle.getString("page");
        if (page.equalsIgnoreCase("Categories"))
            jsonArray = Categories.ItemModelSet;
        else
            jsonArray = SubCategory.ItemModelSet;

        txtstorename.setText(PageName + "(" + CatName + ")");

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
                h.setItemImage("http://13.58.110.101:8080" + itemImage);
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
                FragmentManager manager =getActivity().getSupportFragmentManager();
                for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                    manager.popBackStack();
                }
                fragment = new BucketView();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
//                ft.addToBackStack("add" + Container.add);
                ft.commit();
//                Container.add++;
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
            TextView txtname, txtprice, txtquantity, txtsize;
            ImageView imgitems;
            RelativeLayout rlitems,rlquanborder;
            TextView txtaddtocart;
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
                holder.txtsize = (TextView) paramView.findViewById(R.id.txtsize);
                holder.imgitems = (ImageView) paramView.findViewById(R.id.imgitems);
                holder.rlitems = (RelativeLayout) paramView.findViewById(R.id.rlitems);
                holder.rlquanborder = (RelativeLayout) paramView.findViewById(R.id.rlquanborder);
                holder.txtaddtocart = (TextView) paramView.findViewById(R.id.txtaddtocart);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            StoreListHolder h = list.get(paramInt);
            String name = h.getItemName();
            holder.txtname.setText(name);
            holder.txtsize.setText("Size: " + h.getSize());
            holder.txtprice.setText("Price: " + h.getPrice() + "\u20A8");
            holder.txtquantity.setText(h.getQuantity());
            final String ImagePath = h.getItemImage();
            if ((!ImagePath.equalsIgnoreCase("null")) && (!ImagePath.isEmpty())) {
                /*Picasso.with(getActivity())
                        .load(ImagePath)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                                holder.img.setImageBitmap(bitmap);
                                holder.imgitems.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
//                                holder.imgstore.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/
               /* Picasso
                        .with(getActivity())
                        .load(ImagePath)
                        .placeholder(R.mipmap.uploadimage) // can also be a drawable
                        .into(holder.imgitems, new Callback() {
                            @Override
                            public void onSuccess() {
                                // once the image is loaded, load the next image
                                Picasso
                                        .with(getActivity())
                                        .load(ImagePath)
//                                        .noPlaceholder() // but don't clear the imageview or set a placeholder; just leave the previous image in until the new one is ready
                                        .into(holder.imgitems);
                            }

                            @Override
                            public void onError() {

                            }
                        });*/
                Picasso.with(getActivity())
                        .load(ImagePath)
                        .placeholder(R.mipmap.product)   // optional
                        .error(R.mipmap.product)      // optional
                        .resize(100, 100)                        // optional
                        .into(holder.imgitems);
            } else {
                holder.imgitems.setBackgroundResource(R.mipmap.product);
            }
            holder.rlitems.setTag(paramInt);
            holder.rlitems.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                }
            });
            holder.txtaddtocart.setTag(paramInt);
            holder.txtaddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                    ItemId = h1.getItemID();
                    ItemName = h1.getItemName();
                    Quantity = holder.txtquantity.getText().toString();
                    Size = h1.getSize();
                    Price = h1.getPrice();
                    ItemImage = h1.getItemImage();
                    new AddToCart().execute();
                }
            });
            holder.rlquanborder.setTag(paramInt);
            holder.rlquanborder.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    list_designation = getResources().getStringArray(R.array.item_quantity);
                    lpw = new ListPopupWindow(getActivity());
                    lpw.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_designation));
                    lpw.setAnchorView(holder.txtquantity);
                    lpw.setWidth(50);
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
            mydb.addTocart(PageId, PageName, CatId, CatName, ItemId, ItemName, Quantity, Size, Price, ItemImage);
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
