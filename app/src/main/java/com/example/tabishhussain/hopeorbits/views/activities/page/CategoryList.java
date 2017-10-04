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
import android.util.Log;
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
import com.example.tabishhussain.hopeorbits.holder.CategoryModels;

import java.util.ArrayList;


public class CategoryList extends Fragment {
    RecyclerView categoriesrecycler;
    ArrayList<CategoryModels> list = new ArrayList<>();
    private CategoryListAdapter adapter;
    private TextView editbtn;
    String check="false";
    Button addcat,additem;
    LinearLayout tabbot;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_category_list, container, false);
        editbtn = (TextView)view.findViewById(R.id.editbtn);
        tabbot = (LinearLayout)view.findViewById(R.id.tabbot);
        addcat = (Button) view.findViewById(R.id.addcat);

        addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent is = new Intent(getActivity(),AddPageCategory.class);
//                startActivity(is);

                fragment = new AddPageCategory();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
            }
        });
        Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
        tabbot.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();

        list = (ArrayList<CategoryModels>) bundle.getSerializable("mylist");

        categoriesrecycler = (RecyclerView) view.findViewById(R.id.categoriesrecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        categoriesrecycler.setLayoutManager(layoutManager);
        Log.d("arrayempty",list.toString());

        if(list.size() != 0){
            adapter = new CategoryListAdapter(CategoryList.this, list, check);
            categoriesrecycler.setAdapter(adapter);
        }

        Toast.makeText(getActivity(),"hello2",Toast.LENGTH_SHORT).show();
        editbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(check.equals("true")){
                    editbtn.setText("Edit");
                    check="false";
                    tabbot.setVisibility(View.GONE);
                    if(list.size() != 0){
                        adapter = new CategoryListAdapter(CategoryList.this, list, check);
                        categoriesrecycler.setAdapter(adapter);
                    }

                }else if (check.equals("false")){
                    editbtn.setText("Save");
                    check="true";
                    tabbot.setVisibility(View.VISIBLE);
                    if(list.size() != 0){
                        adapter = new CategoryListAdapter(CategoryList.this, list, check);
                        categoriesrecycler.setAdapter(adapter);
                    }
                }else {

                }
            }
        });
        return view;
    }
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
            public ImageView catimg;
            public LinearLayout catliniar;
            public CheckBox catcheck;

            public ViewHolder(View view) {

                super(view);
                cattxt = (TextView) view.findViewById(R.id.cattxt);
                catimg = (ImageView) view.findViewById(R.id.catimg);
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
        public void onBindViewHolder(ViewHolder holder, final int position) {
            String s;
            holder.cattxt.setText(catlis.get(position).getCategoryName());
            s = catlis.get(position).getCategoryImage();
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
}
