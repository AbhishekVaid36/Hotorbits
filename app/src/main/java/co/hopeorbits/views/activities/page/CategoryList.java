package co.hopeorbits.views.activities.page;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import co.hopeorbits.connectiondetector.ConnectionDetector;
import co.hopeorbits.holder.UserPageHolder;
import co.hopeorbits.views.activities.accounts.Yourpage;


public class CategoryList extends Fragment implements View.OnClickListener {
    GridView categoriesrecycler;
    ArrayList<UserPageHolder> list = new ArrayList<>();
    private CategoryListAdapter adapter;
    private TextView editbtn;
    String check = "false", message, pageId;
    Button addcat, removecat;
    LinearLayout tabbot;
    Fragment fragment;
    FragmentTransaction ft;
    ArrayList<String> selectedCatId = new ArrayList<String>();
    TextView pagename;
    JSONArray jsonArray;
    String pageID, pageName, categoryID, categoryName, categoryImage, itemModelSet, categoryIntoCategoryList;
    public static JSONArray ItemModelSet;
    public static JSONArray SubCategoryListSet;
RelativeLayout holebody;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_category_list, container, false);
        editbtn = (TextView) view.findViewById(R.id.editbtn);
        tabbot = (LinearLayout) view.findViewById(R.id.tabbot);
        addcat = (Button) view.findViewById(R.id.addcat);
        removecat = (Button) view.findViewById(R.id.removecat);
        pagename = (TextView) view.findViewById(R.id.pagename);
        holebody=(RelativeLayout)view.findViewById(R.id.holebody);
        pagename.setOnClickListener(this);
        addcat.setOnClickListener(this);
        removecat.setOnClickListener(this);
        holebody.setOnClickListener(this);

//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
        tabbot.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        jsonArray = Yourpage.CategoryListSet;
        pageName = bundle.getString("pageName");
        pageId = bundle.getString("pageID");

//        list = (ArrayList<CategoryModels>) bundle.getSerializable("mylist");
//        pageId=bundle.getString("pageId");
        categoriesrecycler = (GridView) view.findViewById(R.id.categoriesrecycler);

//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
//        categoriesrecycler.setLayoutManager(layoutManager);
//        Log.d("arrayempty",list.toString());
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
                list.add(h);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (list.size() != 0) {
            adapter = new CategoryListAdapter(getActivity(), list, check);
            categoriesrecycler.setAdapter(adapter);
        }

//        Toast.makeText(getActivity(),"hello2",Toast.LENGTH_SHORT).show();
        editbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (check.equals("true")) {
                    editbtn.setText("Edit");
                    check = "false";
                    tabbot.setVisibility(View.GONE);
                    if (list.size() != 0) {
                        adapter = new CategoryListAdapter(getActivity(), list, check);
                        categoriesrecycler.setAdapter(adapter);
                    }

                } else if (check.equals("false")) {
                    editbtn.setText("Save");
                    check = "true";
                    tabbot.setVisibility(View.VISIBLE);
                    if (list.size() != 0) {
                        adapter = new CategoryListAdapter(getActivity(), list, check);
                        categoriesrecycler.setAdapter(adapter);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addcat:
                fragment = new AddPageCategory();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
                break;
            case R.id.removecat:
                if(selectedCatId.size()>0)
                myAlertDialog();
                else
                    Toast.makeText(getActivity(),"Please select at least one category",Toast.LENGTH_LONG).show();
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
                .setMessage(" Are you sure you want to remove the selected category? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteCatProgress().execute();
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

    public class DeleteCatProgress extends AsyncTask<String, String, String> {
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
                for (int i = 0; i < selectedCatId.size(); i++) {
                    jo_item = new JSONObject();
                    jo_item.put("categoryID", selectedCatId.get(i));
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

            selectedCatId.clear();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                manager.popBackStack();
            }

            fragment = new Yourpage();
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
//            ft.addToBackStack("add" + Container.add);
            ft.commit();
//            Container.add++;

        }

    }

    class CategoryListAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<UserPageHolder> list;
        String check;

        public CategoryListAdapter(FragmentActivity fragmentActivity, ArrayList<UserPageHolder> list, String check) {
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
            public RelativeLayout rlcategory;
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
                paramView = inflater.inflate(R.layout.catviewpage, paramViewGroup, false);
                holder = new ViewHolder();

                holder.cattxt = (TextView) paramView.findViewById(R.id.cattxt);
                holder.catimg = (ImageView) paramView.findViewById(R.id.catimg);
                holder.rlcategory = (RelativeLayout) paramView.findViewById(R.id.rlcategory);
                holder.catcheck = (CheckBox) paramView.findViewById(R.id.catcheck);
                if (check.equals("true")) {
                    holder.catcheck.setVisibility(View.VISIBLE);
                } else {
                    holder.catcheck.setVisibility(View.GONE);
                }
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            final UserPageHolder h = list.get(paramInt);
            holder.cattxt.setText(h.getCategoryName());
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


            final String cattegoryId = h.getCategoryID();
            Log.d("ccattegoryId", cattegoryId);
            holder.rlcategory.setTag(paramInt);
            holder.rlcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (h.getCategoryIntoCategoryList().length() == 2) {
                        if (h.getItemModelSet().length() == 2) {
                            Bundle b = new Bundle();
//                            b.putSerializable("CategoryListItemModelsetsubitem", h.getItemModelSet());
                            b.putString("subcatoritemId", cattegoryId);
//                            b.putString("pageId", pageId);
                            fragment = new Addsubcategoryoritem();
                            fragment.setArguments(b);
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
                            Container.add++;
                        } else {
                            if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                                Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                            } else {
                                int pos1 = (Integer) view.getTag();
                                UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                                try {
                                    ItemModelSet = new JSONArray(h1.getItemModelSet());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Bundle b = new Bundle();
                                b.putString("categoryID", cattegoryId);
//                                b.putSerializable("CategoryListItemModelset", h.getItemModelSet());
                                fragment = new CategoryItemList();
                                fragment.setArguments(b);
                                ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.add(R.id.frame, fragment);
                                ft.addToBackStack("add" + Container.add);
                                ft.commit();
                                Container.add++;
                            }

//                            Intent sub = new Intent(activity, CategoryItemList.class);
//                            sub.putExtra("categoryID",cattegoryId);
//                            sub.putExtra("CategoryListItemModelset", catlis.get(position).getItemModelSet());
//                            activity.startActivity(sub);
                        }
                    } else {
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            int pos1 = (Integer) view.getTag();
                            UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                            try {
                                SubCategoryListSet = new JSONArray(h1.getCategoryIntoCategoryList());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Bundle b = new Bundle();
//                            b.putSerializable("CategoryList", h.getCategoryIntoCategoryList());
                            b.putString("subcatoritem", cattegoryId);
                            b.putString("pageId", pageId);
                            fragment = new SubcategoryList();
                            fragment.setArguments(b);
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
                            Container.add++;
                        }
                    }
                }
            });
            holder.catcheck.setTag(paramInt);
            holder.catcheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    if (holder.catcheck.isChecked()) {
                        selectedCatId.add(h.getCategoryID());
                    } else {
                        selectedCatId.remove(h.getCategoryID());
                    }
                }
            });
            return paramView;
        }
    }

/*
    public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

        Activity activity;
        ArrayList<CategoryModels> catlis = new ArrayList<>();
        String check;

        public CategoryListAdapter(CategoryList categoryList, ArrayList<CategoryModels> list, String check) {
            this.catlis = list;
            this.check = check;

            Log.d("myarraylist",categoryList.toString());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.catviewpage, parent, false);
            return new ViewHolder(itemView);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView cattxt;
            public CircularImageView catimg;
            public LinearLayout catliniar;
            public CheckBox catcheck;

            public ViewHolder(View view) {

                super(view);
                cattxt = (TextView) view.findViewById(R.id.cattxt);
                catimg = (CircularImageView) view.findViewById(R.id.catimg);
                catliniar = (LinearLayout) view.findViewById(R.id.catliniar);
                catcheck = (CheckBox) view.findViewById(R.id.catcheck);
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

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            String s;
            holder.cattxt.setText(catlis.get(position).getCategoryName());
            s = catlis.get(position).getCategoryImage();
            if (s != null) {
                Log.d("errrorimage", s);
                String imgstr = catlis.get(position).getCategoryImage();
                if(!imgstr.isEmpty()) {

                    Picasso.with(getActivity())
                            .load("http://13.58.110.101:8080"+imgstr)
                            .centerCrop()
                            .resize(200, 200)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                    holder.catimg.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                                    holder.catimg.setImageBitmap(bitmap);
                                    Toast.makeText(getActivity(), "empty image"+ bitmap, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    Toast.makeText(getActivity(), "image failed", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Toast.makeText(getActivity(), "image failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

            final String cattegoryId = catlis.get(position).getCategoryID();
            Log.d("ccattegoryId",cattegoryId);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (catlis.get(position).getCategoryIntoCategoryList().size() == 0) {
                        if(catlis.get(position).getItemModelSet().size() == 0) {
                            Bundle b = new Bundle();
                            b.putSerializable("CategoryListItemModelsetsubitem", catlis.get(position).getItemModelSet());
                            b.putString("subcatoritem", cattegoryId);
                            b.putString("pageId", pageId);
                            fragment = new Addsubcategoryoritem();
                            fragment.setArguments(b);
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
                            Container.add++;



//                            Intent sub = new Intent(activity, Addsubcategoryoritem.class);
//                            sub.putExtra("CategoryListItemModelsetsubitem", catlis.get(position).getItemModelSet());
//                            sub.putExtra("subcatoritem",cattegoryId);
//                            activity.startActivity(sub);
                        }else{
                            Bundle b = new Bundle();
                            b.putString("categoryID", cattegoryId);
                            b.putSerializable("CategoryListItemModelset", catlis.get(position).getItemModelSet());
                            fragment = new CategoryItemList();
                            fragment.setArguments(b);
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
                            Container.add++;



//                            Intent sub = new Intent(activity, CategoryItemList.class);
//                            sub.putExtra("categoryID",cattegoryId);
//                            sub.putExtra("CategoryListItemModelset", catlis.get(position).getItemModelSet());
//                            activity.startActivity(sub);
                        }
                    }else {

                        Bundle b = new Bundle();
                        b.putSerializable("CategoryList", catlis.get(position).getCategoryIntoCategoryList());
                        b.putString("subcatoritem", cattegoryId);
                        fragment = new SubcategoryList();
                        fragment.setArguments(b);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + Container.add);
                        ft.commit();
                        Container.add++;



//                        Intent sub = new Intent(activity,SubcategoryList.class);
//                        sub.putExtra("CategoryList", catlis.get(position).getCategoryIntoCategoryList());
//                        sub.putExtra("subcatoritem",cattegoryId);
//                        activity.startActivity(sub);
                    }

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
                        selectedCatId.add(catlis.get(position).getCategoryID());
                    } else {
                        selectedCatId.remove(catlis.get(position).getCategoryID());
                    }
                }
            });

            if (s != null) {

                String imgstr = catlis.get(position).getCategoryImage();

                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.catimg.setImageBitmap(decodedByte);
            }
        }

        @Override
        public int getItemCount() {
            return catlis.size();
        }

//        public static int getScreenWidth() {
//            return Resources.getSystem().getDisplayMetrics().widthPixels;
//        }
//
//        public static int getScreenHeight() {
//            return Resources.getSystem().getDisplayMetrics().heightPixels;
//        }
    }
*/
}
