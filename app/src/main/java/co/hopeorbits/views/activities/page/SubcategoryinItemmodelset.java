package co.hopeorbits.views.activities.page;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import co.hopeorbits.buyer.Container;
import co.hopeorbits.buyer.JSONParser;
import co.hopeorbits.holder.UserPageHolder;
import co.hopeorbits.views.activities.accounts.Yourpage;

public class SubcategoryinItemmodelset extends Fragment implements View.OnClickListener {

    private SubCategoryItemkSetAdapter subCategoryItemkSetAdapter;
    private TextView editbtn;
    String check = "false", message;
    Button addcat, subremovecatitem, additem;
    LinearLayout tabbot;

    ListView subcatinitemsetrecycler;
    ArrayList<UserPageHolder> categorylistitemset = new ArrayList<>();
    Fragment fragment;
    FragmentTransaction ft;
    String catId;
    ArrayList<String> selectedSubCatItemId = new ArrayList<String>();
    TextView pagename;
    JSONArray jsonArray;
    String itemID, itemName, itemImage, itemPrice, itemSize, itemQuantity;
    RelativeLayout holebody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_subcategoryin_itemmodelset, container, false);
        editbtn = (TextView) view.findViewById(R.id.subitemeditbtn);
        tabbot = (LinearLayout) view.findViewById(R.id.subitemtabbot);
        addcat = (Button) view.findViewById(R.id.subitemaddcat);
        subremovecatitem = (Button) view.findViewById(R.id.subitemremovecat);
        pagename = (TextView) view.findViewById(R.id.pagename);
        holebody = (RelativeLayout) view.findViewById(R.id.holebody);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
//            cat_id = bundle.getString("categoryID");
            jsonArray = SubcategoryList.ItemModelSet;
//            PageId = bundle.getString("pageID");
//            PageName = bundle.getString("pageName");
            catId = bundle.getString("categoryID");
//            CatName = bundle.getString("categoryName");
        }
//        categorylistitemset = (ArrayList<IntoItemModelSet>) b.getSerializable("CategoryListItemmodelset");
        addcat.setOnClickListener(this);
        subremovecatitem.setOnClickListener(this);
        pagename.setOnClickListener(this);
        holebody.setOnClickListener(this);

//        addcat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent is = new Intent(getActivity(), AddCatItemModel.class);
////                startActiv         ity(is);
//                ////////////////////////work is pending///////
//                Bundle b = new Bundle();
//                b.putString("categoryid", catId);
//                fragment = new AddCatItemModel();
//                fragment.setArguments(b);
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + Container.add);
//                ft.commit();
//                Container.add++;
//            }
//        });

        tabbot.setVisibility(View.GONE);

        subcatinitemsetrecycler = (ListView) view.findViewById(R.id.subitemcategoryrecycler);
//        LinearLayoutManager horizontalLayoutManagaer
//                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        subcatinitemsetrecycler.setLayoutManager(horizontalLayoutManagaer);
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
                UserPageHolder h = new UserPageHolder();
                h.setItemID(itemID);
                h.setItemName(itemName);
                h.setItemImage("http://13.58.110.101:8080" + itemImage);
                h.setItemQuantity(itemQuantity);
                h.setItemSize(itemSize);
                h.setItemPrice(itemPrice);
                categorylistitemset.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (categorylistitemset.size() != 0) {
            subCategoryItemkSetAdapter = new SubCategoryItemkSetAdapter(getActivity(), categorylistitemset, check);
            subcatinitemsetrecycler.setAdapter(subCategoryItemkSetAdapter);
        }
        editbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (check.equals("true")) {
                    editbtn.setText("Edit");
                    check = "false";
                    tabbot.setVisibility(View.GONE);
                    if (categorylistitemset.size() != 0) {
                        subCategoryItemkSetAdapter = new SubCategoryItemkSetAdapter(getActivity(), categorylistitemset, check);
                        subcatinitemsetrecycler.setAdapter(subCategoryItemkSetAdapter);
                    }

                } else if (check.equals("false")) {
                    editbtn.setText("Save");
                    check = "true";
                    tabbot.setVisibility(View.VISIBLE);
                    if (categorylistitemset.size() != 0) {
                        subCategoryItemkSetAdapter = new SubCategoryItemkSetAdapter(getActivity(), categorylistitemset, check);
                        subcatinitemsetrecycler.setAdapter(subCategoryItemkSetAdapter);
                    }
                } else {

                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.subitemaddcat:
                Bundle b = new Bundle();
                b.putString("categoryid", catId);
                fragment = new AddCatItemModel();
                fragment.setArguments(b);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
                break;
            case R.id.subitemremovecat:
                if (selectedSubCatItemId.size() > 0)
                    myAlertDialog();
                else
                    Toast.makeText(getActivity(), "Please select at least one sub category item", Toast.LENGTH_LONG).show();
                break;
            case R.id.pagename:
                break;
            case R.id.holebody:
                break;
        }
    }

    private void myAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder
                .setMessage(" Are you sure you want to remove the selected sub category item? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteCatItemProgress().execute();
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

    public class DeleteCatItemProgress extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONArray ja_inner = null;
            try {

                JSONObject jo_item = null;
                ja_inner = new JSONArray();
                for (int i = 0; i < selectedSubCatItemId.size(); i++) {
                    jo_item = new JSONObject();
                    jo_item.put("itemID", selectedSubCatItemId.get(i));
                    ja_inner.put(jo_item);
                }
                JSONObject j = new JSONObject();
                j.put("categoryID", catId);
                j.put("itemModelSet", ja_inner);

                JSONParser jParser = new JSONParser();
                // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
                JSONObject json = jParser.getJSONFromUrlPost("http://13.58.110.101:8080/hoprepositoryweb/user/deleteItem", j.toString());

                try {

                    message = json.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            selectedSubCatItemId.clear();
            for (int i = 0; i < 3; i++) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
            }

            fragment = new Yourpage();
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame, fragment);
            ft.addToBackStack("add" + Container.add);
            ft.commit();
            Container.add++;

        }

    }

    class SubCategoryItemkSetAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<UserPageHolder> list;
        String check;

        public SubCategoryItemkSetAdapter(FragmentActivity fragmentActivity, ArrayList<UserPageHolder> list, String check) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
            this.check = check;
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
            TextView itemID, itemName, itemPrice, itemSize, itemQuantity;
            ImageView itemImage;
            public LinearLayout catliniar;
            public CheckBox catcheck;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.catitemview, paramViewGroup, false);
                holder = new ViewHolder();

                holder.itemName = (TextView) paramView.findViewById(R.id.itemName);
                holder.itemPrice = (TextView) paramView.findViewById(R.id.itemPrice);
                holder.itemSize = (TextView) paramView.findViewById(R.id.itemSize);
                holder.itemQuantity = (TextView) paramView.findViewById(R.id.itemQuantity);
                holder.itemImage = (ImageView) paramView.findViewById(R.id.itemImage);
                holder.catcheck = (CheckBox) paramView.findViewById(R.id.catitemcheck);
                if (check.equals("true")) {
                    holder.catcheck.setVisibility(View.VISIBLE);
                } else {
                    holder.catcheck.setVisibility(View.GONE);
                }
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            UserPageHolder h = list.get(paramInt);


//        holder.itemID.setText(catlistitem.get(position).getItemID());
            holder.itemName.setText("Item: " + h.getItemName());
            holder.itemPrice.setText("Price: " + h.getItemPrice() + "\u20A8");
            holder.itemSize.setText("Size: " + h.getItemSize());
            holder.itemQuantity.setText("Quantity: " + h.getItemQuantity());
            final String ImagePath = h.getItemImage();
            if (ImagePath != null) {
                if (!ImagePath.isEmpty()) {

                    /*Picasso.with(getActivity())
                            .load("http://13.58.110.101:8080"+imgstr)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    holder.itemImage.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
//                                    holder.catimg.setImageBitmap(bitmap);
//                                    Toast.makeText(getActivity(), "empty image"+ bitmap, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
//                                    Toast.makeText(getActivity(), "image failed", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                                    Toast.makeText(getActivity(), "image failed", Toast.LENGTH_LONG).show();
                                }
                            });*/
                   /* Picasso
                            .with(getActivity())
                            .load(ImagePath)
                            .placeholder(R.mipmap.uploadimage) // can also be a drawable
                            .into(holder.itemImage, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    // once the image is loaded, load the next image
                                    Picasso
                                            .with(getActivity())
                                            .load(ImagePath)
//                                            .noPlaceholder() // but don't clear the imageview or set a placeholder; just leave the previous image in until the new one is ready
                                            .into(holder.itemImage);
                                }

                                @Override
                                public void onError() {

                                }
                            });*/
                    boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                    if (tabletSize) {
                        Picasso.with(getActivity())
                                .load(ImagePath)
                                .placeholder(R.mipmap.product)   // optional
                                .error(R.mipmap.page)      // optional
                                .resize(150, 150)                        // optional
                                .into(holder.itemImage);
                    } else {
                        Picasso.with(getActivity())
                                .load(ImagePath)
                                .placeholder(R.mipmap.product)   // optional
                                .error(R.mipmap.page)      // optional
                                .resize(100, 100)                        // optional
                                .into(holder.itemImage);
                    }
                }
            } else {
                holder.itemImage.setBackgroundResource(R.mipmap.product);
            }
            holder.catcheck.setTag(paramInt);
            holder.catcheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                    if (holder.catcheck.isChecked()) {
                        selectedSubCatItemId.add(h1.getItemID());
                    } else {
                        selectedSubCatItemId.remove(h1.getItemID());
                    }
                }
            });
            return paramView;
        }
    }

/*
    public class SubCategoryItemkSetAdapter extends RecyclerView.Adapter<SubCategoryItemkSetAdapter.ViewHolder> {

        Activity activity;
        ArrayList<IntoItemModelSet> subitemset = new ArrayList<>();
        String check;

        public SubCategoryItemkSetAdapter(SubcategoryinItemmodelset subcategoryinItemmodelset, ArrayList<IntoItemModelSet> categorylistitemset, String check) {
            this.subitemset = categorylistitemset;
            this.check = check;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView itemID, itemName, itemPrice, itemSize, itemQuantity;
            ImageView itemImage;
            public LinearLayout catliniar;
            public CheckBox catcheck;

            public ViewHolder(View view) {
                super(view);
                itemName = (TextView) view.findViewById(R.id.itemName);
                itemPrice = (TextView) view.findViewById(R.id.itemPrice);
                itemSize = (TextView) view.findViewById(R.id.itemSize);
                itemQuantity = (TextView) view.findViewById(R.id.itemQuantity);
                itemImage = (ImageView) view.findViewById(R.id.itemImage);
                catcheck = (CheckBox) itemView.findViewById(R.id.catitemcheck);
                if (check.equals("true")) {
                    catcheck.setVisibility(View.VISIBLE);
                } else {
                    catcheck.setVisibility(View.GONE);
                }
//            ViewGroup.LayoutParams params = catliniar.getLayoutParams();
//            params.height = getScreenWidth() / -5;
//            params.width = getScreenWidth() / 3 - 25;
//            catliniar.setLayoutParams(params);
            }
        }

        @Override
        public SubCategoryItemkSetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.catitemview, parent, false);
            return new SubCategoryItemkSetAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String s = subitemset.get(position).getItemImage();

//        holder.itemID.setText(catlistitem.get(position).getItemID());
            holder.itemName.setText(subitemset.get(position).getItemName());
            holder.itemPrice.setText(subitemset.get(position).getItemPrice());
            holder.itemSize.setText(subitemset.get(position).getItemSize());
            holder.itemQuantity.setText(subitemset.get(position).getItemQuantity());

            if (s != null) {
                String imgstr = subitemset.get(position).getItemImage();
                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.itemImage.setImageBitmap(decodedByte);
            }
            holder.catcheck.setTag(position);
            holder.catcheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    if (holder.catcheck.isChecked()) {
                        selectedSubCatItemId.add(subitemset.get(pos1).getItemID());
                    } else {
                        selectedSubCatItemId.remove(subitemset.get(pos1).getItemID());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return subitemset.size();
        }
    }
*/

}
