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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SubcategoryList extends Fragment implements View.OnClickListener {
    private TextView editbtn;
    String check = "false", message;
    Button addsubcat, removesubcat, additem;
    LinearLayout tabbot;
    ArrayList<UserPageHolder> categorylist = new ArrayList<>();
    GridView subcategoriesrecycler;
    private SubCategoryListAdapter adapter;
    String cat_id, pageId;
    Fragment fragment;
    FragmentTransaction ft;
    ArrayList<String> selectedSubCatId = new ArrayList<String>();
TextView pagename;
    JSONArray jsonArray;
    String pageID, pageName, categoryID, categoryName, categoryImage, itemModelSet, categoryIntoCategoryList;
    public static JSONArray ItemModelSet;
    RelativeLayout holebody;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_subcategory_list, container, false);
        editbtn = (TextView) view.findViewById(R.id.subeditbtn);
        tabbot = (LinearLayout) view.findViewById(R.id.subtabbot);
        addsubcat = (Button) view.findViewById(R.id.subaddcat);
        removesubcat = (Button) view.findViewById(R.id.subremovecat);
        pagename=(TextView)view.findViewById(R.id.pagename);
        holebody=(RelativeLayout)view.findViewById(R.id.holebody);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cat_id = bundle.getString("subcatoritem");
            pageId = bundle.getString("pageId");
            jsonArray = CategoryList.SubCategoryListSet;
        }
        addsubcat.setOnClickListener(this);
        removesubcat.setOnClickListener(this);
        pagename.setOnClickListener(this);
        holebody.setOnClickListener(this);
//        addcat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
////                Intent is = new Intent(getActivity(),AddSubcategory.class);
////                is.putExtra("subcatoritemcategory",cat_id);
////                startActivity(is);
//
//                Bundle b = new Bundle();
//                b.putString("subcatoritemcategory", cat_id);
//                fragment = new AddSubcategory();
//                fragment.setArguments(b);
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + Container.add);
//                ft.commit();
//                Container.add++;
//
//            }
//        });

//        Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        tabbot.setVisibility(View.GONE);

//        categorylist = (ArrayList<CategoryIntoCategoryList>) bundle.getSerializable("CategoryList");

        subcategoriesrecycler = (GridView) view.findViewById(R.id.subcategoryrecycler);
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
//        subcategoriesrecycler.setLayoutManager(layoutManager);
        try {
//            JSONArray jsonArray = new JSONArray(categoryModels);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                categoryID = obj.getString("categoryID");
                categoryName = obj.getString("categoryName");
                categoryImage = obj.getString("categoryImage");
                itemModelSet = obj.getString("itemModelSet");
                categoryIntoCategoryList = obj.getString("categoryIntoCategoryList");
                UserPageHolder h = new UserPageHolder();
                h.setCategoryID(categoryID);
                h.setCategoryName(categoryName);
                h.setCategoryImage("http://13.58.110.101:8080" + categoryImage);
                h.setItemModelSet(itemModelSet);
                h.setCategoryIntoCategoryList(categoryIntoCategoryList);
                categorylist.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (categorylist.size() != 0) {
            adapter = new SubCategoryListAdapter(getActivity(), categorylist, check);
            subcategoriesrecycler.setAdapter(adapter);
        }

        editbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (check.equals("true")) {
                    editbtn.setText("Edit");
                    check = "false";
                    tabbot.setVisibility(View.GONE);
                    if (categorylist.size() != 0) {
                        adapter = new SubCategoryListAdapter(getActivity(), categorylist, check);
                        subcategoriesrecycler.setAdapter(adapter);
                    }

                } else if (check.equals("false")) {
                    editbtn.setText("Save");
                    check = "true";
                    tabbot.setVisibility(View.VISIBLE);
                    if (categorylist.size() != 0) {
                        adapter = new SubCategoryListAdapter(getActivity(), categorylist, check);
                        subcategoriesrecycler.setAdapter(adapter);
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
            case R.id.subaddcat:
                Bundle b = new Bundle();
                b.putString("subcatoritemcategory", cat_id);
                fragment = new AddSubcategory();
                fragment.setArguments(b);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
                break;
            case R.id.subremovecat:
                if(selectedSubCatId.size()>0)
                    myAlertDialog();
                else
                    Toast.makeText(getActivity(),"Please select at least one sub category",Toast.LENGTH_LONG).show();
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
                .setMessage(" Are you sure you want to remove the selected sub category? ")
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
                for (int i = 0; i < selectedSubCatId.size(); i++) {
                    jo_item = new JSONObject();
                    jo_item.put("categoryID", selectedSubCatId.get(i));
                    ja_inner.put(jo_item);
                }
                JSONObject j = new JSONObject();
                j.put("pageID", pageId);
                j.put("categoryModels", ja_inner);

                JSONParser jParser = new JSONParser();
                // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
                JSONObject json = jParser.getJSONFromUrlPost("http://13.58.110.101:8080/hoprepositoryweb/user/deleteCategory", j.toString());

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

            selectedSubCatId.clear();
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

    class SubCategoryListAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<UserPageHolder> list;
        String check;

        public SubCategoryListAdapter(FragmentActivity fragmentActivity, ArrayList<UserPageHolder> list, String check) {
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
            public TextView cattxt;
            public ImageView catimg;
            public RelativeLayout rlsubcategory;
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
                paramView = inflater.inflate(R.layout.subcateview, paramViewGroup, false);
                holder = new ViewHolder();

                holder.cattxt = (TextView) paramView.findViewById(R.id.subcattxt);
                holder.catimg = (ImageView) paramView.findViewById(R.id.subcatimg);
                holder.rlsubcategory = (RelativeLayout) paramView.findViewById(R.id.rlsubcategory);
                holder.catcheck = (CheckBox) paramView.findViewById(R.id.subcatcheck);

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
            final String ImagePath = h.getCategoryImage();
            if (ImagePath != null) {
                if (!ImagePath.isEmpty()) {

                    /*Picasso.with(getActivity())
                            .load("http://13.58.110.101:8080" + imgstr)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    holder.catimg.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
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
                    /*Picasso
                            .with(getActivity())
                            .load(ImagePath)
                            .placeholder(R.mipmap.uploadimage) // can also be a drawable
                            .into(holder.catimg, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    // once the image is loaded, load the next image
                                    Picasso
                                            .with(getActivity())
                                            .load(ImagePath)
//                                            .noPlaceholder() // but don't clear the imageview or set a placeholder; just leave the previous image in until the new one is ready
                                            .into(holder.catimg);
                                }

                                @Override
                                public void onError() {

                                }
                            });*/
                    boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                    if (tabletSize) {
                        Picasso.with(getActivity())
                                .load(ImagePath)
                                .placeholder(R.mipmap.category)   // optional
                                .error(R.mipmap.page)      // optional
                                .resize(150, 150)                        // optional
                                .into(holder.catimg);
                    } else {
                        Picasso.with(getActivity())
                                .load(ImagePath)
                                .placeholder(R.mipmap.category)   // optional
                                .error(R.mipmap.page)      // optional
                                .resize(100, 100)                        // optional
                                .into(holder.catimg);
                    }
                }
            } else {
                holder.catimg.setBackgroundResource(R.mipmap.category);
            }
            holder.cattxt.setText(h.getCategoryName());
            holder.rlsubcategory.setTag(paramInt);
            holder.rlsubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent sub = new Intent(activity, SubcategoryinItemmodelset.class);
//                    sub.putExtra("CategoryListItemmodelset", subcatlist.get(position).getItemModelSet());
//                    activity.startActivity(sub);
                    int pos1 = (Integer) view.getTag();
                    UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                    try {
                        ItemModelSet = new JSONArray(h1.getItemModelSet());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Bundle b = new Bundle();
//                    b.putSerializable("CategoryListItemmodelset", h1.getItemModelSet());
                    b.putString("categoryID", h1.getCategoryID());
                    fragment = new SubcategoryinItemmodelset();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + Container.add);
                    ft.commit();
                    Container.add++;

//                Intent sub = new Intent(context,AddSubcategory.class);
//                sub.putExtra("catsublist",catlis.get(position).getItemModelSet());
//                activity.startActivity(sub);
                }

            });
            holder.catcheck.setTag(paramInt);
            holder.catcheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                    if (holder.catcheck.isChecked()) {
                        selectedSubCatId.add(h1.getCategoryID());
                    } else {
                        selectedSubCatId.remove(h1.getCategoryID());
                    }
                }
            });
//            if (s != null) {
//
//                String imgstr = subcatlist.get(position).getCategoryImage();
//
//                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                holder.catimg.setImageBitmap(decodedByte);
//            }
            return paramView;
        }
    }

/*
    public class SubCategoryListAdapter extends RecyclerView.Adapter<SubCategoryListAdapter.ViewHolder> {

        Activity activity;
        ArrayList<CategoryIntoCategoryList> subcatlist = new ArrayList<>();
        String check;

        public SubCategoryListAdapter(SubcategoryList subcategoryList, ArrayList<CategoryIntoCategoryList> categorylist, String check) {
            this.subcatlist = categorylist;
            this.check = check;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcateview, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            String s;
            holder.cattxt.setText(subcatlist.get(position).getCategoryName());
            s = subcatlist.get(position).getCategoryImage();
            final String cattegoryId = subcatlist.get(position).getCategoryID();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent sub = new Intent(activity, SubcategoryinItemmodelset.class);
//                    sub.putExtra("CategoryListItemmodelset", subcatlist.get(position).getItemModelSet());
//                    activity.startActivity(sub);

                    Bundle b = new Bundle();
                    b.putSerializable("CategoryListItemmodelset", subcatlist.get(position).getItemModelSet());
                    b.putString("categoryid", subcatlist.get(position).getCategoryID());
                    fragment = new SubcategoryinItemmodelset();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + Container.add);
                    ft.commit();
                    Container.add++;

//                Intent sub = new Intent(context,AddSubcategory.class);
//                sub.putExtra("catsublist",catlis.get(position).getItemModelSet());
//                activity.startActivity(sub);
                }

            });
            holder.catcheck.setTag(position);
            holder.catcheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    if (holder.catcheck.isChecked()) {
                        selectedSubCatId.add(subcatlist.get(pos1).getCategoryID());
                    } else {
                        selectedSubCatId.remove(subcatlist.get(pos1).getCategoryID());
                    }
                }
            });
            if (s != null) {

                String imgstr = subcatlist.get(position).getCategoryImage();

                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.catimg.setImageBitmap(decodedByte);
            }
        }

        @Override
        public int getItemCount() {
            return subcatlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView cattxt;
            public ImageView catimg;
            public LinearLayout catliniar;
            public CheckBox catcheck;

            public ViewHolder(View itemView) {
                super(itemView);
                cattxt = (TextView) itemView.findViewById(R.id.subcattxt);
                catimg = (ImageView) itemView.findViewById(R.id.subcatimg);
                catliniar = (LinearLayout) itemView.findViewById(R.id.subcatliniar);
                catcheck = (CheckBox) itemView.findViewById(R.id.subcatcheck);

                if (check.equals("true")) {
                    catcheck.setVisibility(View.VISIBLE);
                } else {
                    catcheck.setVisibility(View.GONE);
                }
//                ViewGroup.LayoutParams params = catliniar.getLayoutParams();
//                params.height = getScreenWidth() / -5;
//                params.width = getScreenWidth() / 3 - 25;
//                catliniar.setLayoutParams(params);
            }
        }
    }
*/

//        //CategoryList
//        categorylist = (ArrayList<CategoryIntoCategoryList>) getIntent().getSerializableExtra("CategoryList");
//        Log.d("inhavearrauy",categorylist.toString());
//
//        subcategoriesrecycler = (RecyclerView) findViewById(R.id.subcategoryrecycler);
//        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
//        subcategoriesrecycler.setLayoutManager(layoutManager);
//
//        adapter = new SubCategoryListAdapter(SubcategoryList.this, categorylist);
//        subcategoriesrecycler.setAdapter(adapter);
//    }
}
