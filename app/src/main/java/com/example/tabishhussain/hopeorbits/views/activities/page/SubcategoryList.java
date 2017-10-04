package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.CategoryIntoCategoryList;

import java.util.ArrayList;

public class SubcategoryList extends Fragment {
    private TextView editbtn;
    String check="false";
    Button addcat,additem;
    LinearLayout tabbot;
    ArrayList<CategoryIntoCategoryList> categorylist = new ArrayList<>();
    RecyclerView subcategoriesrecycler;
    private SubCategoryListAdapter adapter;
    String cat_id;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_subcategory_list, container, false);
        editbtn = (TextView)view.findViewById(R.id.subeditbtn);
        tabbot = (LinearLayout)view.findViewById(R.id.subtabbot);
        addcat = (Button) view.findViewById(R.id.subaddcat);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cat_id = bundle.getString("subcatoritem");
        }

        addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Intent is = new Intent(getActivity(),AddSubcategory.class);
//                is.putExtra("subcatoritemcategory",cat_id);
//                startActivity(is);

                Bundle b = new Bundle();
                b.putString("subcatoritemcategory", cat_id);
                fragment = new AddSubcategory();
                fragment.setArguments(b);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;

            }
        });

        Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
        tabbot.setVisibility(View.GONE);

        categorylist = (ArrayList<CategoryIntoCategoryList>) bundle.getSerializable("CategoryList");

        subcategoriesrecycler = (RecyclerView) view.findViewById(R.id.subcategoryrecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        subcategoriesrecycler.setLayoutManager(layoutManager);

        if(categorylist.size() != 0){
            adapter = new SubCategoryListAdapter(SubcategoryList.this, categorylist, check);
            subcategoriesrecycler.setAdapter(adapter);
        }

        editbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(check.equals("true")){
                    editbtn.setText("Edit");
                    check="false";
                    tabbot.setVisibility(View.GONE);
                    if(categorylist.size() != 0){
                        adapter = new SubCategoryListAdapter(SubcategoryList.this, categorylist, check);
                        subcategoriesrecycler.setAdapter(adapter);
                    }

                }else if (check.equals("false")){
                    editbtn.setText("Save");
                    check="true";
                    tabbot.setVisibility(View.VISIBLE);
                    if(categorylist.size() != 0){
                        adapter = new SubCategoryListAdapter(SubcategoryList.this, categorylist , check);
                        subcategoriesrecycler.setAdapter(adapter);
                    }
                }else {

                }
            }
        });
        return view;
    }
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
        public void onBindViewHolder(ViewHolder holder, final int position) {
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
                    b.putSerializable("CategoryListItemmodelset",subcatlist.get(position).getItemModelSet());
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
