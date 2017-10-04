package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.IntoItemModelSet;

import java.util.ArrayList;

public class SubcategoryinItemmodelset extends Fragment {

    private SubCategoryItemkSetAdapter subCategoryItemkSetAdapter;
    private TextView editbtn;
    String check = "false";
    Button addcat, additem;
    LinearLayout tabbot;

    RecyclerView subcatinitemsetrecycler;
    ArrayList<IntoItemModelSet> categorylistitemset = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_subcategoryin_itemmodelset, container, false);
        editbtn = (TextView) view.findViewById(R.id.subitemeditbtn);
        tabbot = (LinearLayout) view.findViewById(R.id.subitemtabbot);
        addcat = (Button) view.findViewById(R.id.subitemaddcat);

        addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent is = new Intent(getActivity(), AddCatItemModel.class);
                startActivity(is);
                ////////////////////////work is pending///////
            }
        });

        tabbot.setVisibility(View.GONE);
        Bundle b = this.getArguments();
        categorylistitemset = (ArrayList<IntoItemModelSet>) b.getSerializable("CategoryListItemmodelset");

        subcatinitemsetrecycler = (RecyclerView) view.findViewById(R.id.subitemcategoryrecycler);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        subcatinitemsetrecycler.setLayoutManager(horizontalLayoutManagaer);

        if (categorylistitemset.size() != 0) {
            subCategoryItemkSetAdapter = new SubCategoryItemkSetAdapter(SubcategoryinItemmodelset.this, categorylistitemset, check);
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
                        subCategoryItemkSetAdapter = new SubCategoryItemkSetAdapter(SubcategoryinItemmodelset.this, categorylistitemset, check);
                        subcatinitemsetrecycler.setAdapter(subCategoryItemkSetAdapter);
                    }

                } else if (check.equals("false")) {
                    editbtn.setText("Save");
                    check = "true";
                    tabbot.setVisibility(View.VISIBLE);
                    if (categorylistitemset.size() != 0) {
                        subCategoryItemkSetAdapter = new SubCategoryItemkSetAdapter(SubcategoryinItemmodelset.this, categorylistitemset, check);
                        subcatinitemsetrecycler.setAdapter(subCategoryItemkSetAdapter);
                    }
                } else {

                }
            }
        });
        return view;
    }
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
                catcheck = (CheckBox) itemView.findViewById(R.id.catcheck);
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
        public void onBindViewHolder(SubCategoryItemkSetAdapter.ViewHolder holder, int position) {
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
        }

        @Override
        public int getItemCount() {
            return subitemset.size();
        }
    }

}
