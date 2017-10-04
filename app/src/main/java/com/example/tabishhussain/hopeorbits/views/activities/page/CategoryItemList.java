package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.ItemModelSet;

import java.util.ArrayList;

/**
 * Created by abc on 9/29/2017.
 */

public class CategoryItemList extends Fragment{
    private TextView catiemeditbtn;
    String check="false";
    Button catiemremocat,catiemadditem;
    LinearLayout catiemtabbot;
    ArrayList<ItemModelSet> catiemegorylist = new ArrayList<>();
    RecyclerView catiemcategoriesrecycler;
    private CatiemCategoryListAdapter catiemadapter;
    String cat_id;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_category_itemmodelset, container, false);
        catiemeditbtn = (TextView)view.findViewById(R.id.catitemmodeleditbtn);
        catiemtabbot = (LinearLayout)view.findViewById(R.id.catitemmodeltabbot);
        catiemadditem = (Button) view.findViewById(R.id.catitemmodeladdcat);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cat_id = bundle.getString("categoryID");
        }

        catiemadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent catitem = new Intent(getActivity(),AddCatItemModel.class);
                catitem.putExtra("categoryid",cat_id);
                startActivity(catitem);

                Bundle b = new Bundle();
                b.putString("categoryid", cat_id);
                fragment = new AddCatItemModel();
                fragment.setArguments(b);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;

            }
        });

        Toast.makeText(getActivity(),"helloitemmodel",Toast.LENGTH_SHORT).show();
        catiemtabbot.setVisibility(View.GONE);

        catiemegorylist = (ArrayList<ItemModelSet>)  bundle.getSerializable("CategoryListItemModelset");

        catiemcategoriesrecycler = (RecyclerView) view.findViewById(R.id.catitemmodelcategoryrecycler);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        catiemcategoriesrecycler.setLayoutManager(horizontalLayoutManagaer);

        if(catiemegorylist.size() != 0){
            catiemadapter = new CatiemCategoryListAdapter(CategoryItemList.this, catiemegorylist, check);
            catiemcategoriesrecycler.setAdapter(catiemadapter);
        }

        catiemeditbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(check.equals("true")){
                    catiemeditbtn.setText("Edit");
                    check="false";
                    catiemtabbot.setVisibility(View.GONE);
                    if(catiemegorylist.size() != 0){
                        catiemadapter = new CatiemCategoryListAdapter(CategoryItemList.this, catiemegorylist, check);
                        catiemcategoriesrecycler.setAdapter(catiemadapter);
                    }

                }else if (check.equals("false")){
                    catiemeditbtn.setText("Save");
                    check="true";
                    catiemtabbot.setVisibility(View.VISIBLE);
                    if(catiemegorylist.size() != 0){
                        catiemadapter = new CatiemCategoryListAdapter(CategoryItemList.this, catiemegorylist, check);
                        catiemcategoriesrecycler.setAdapter(catiemadapter);
                    }
                }else {

                }
            }
        });
        return view;
    }
    public class CatiemCategoryListAdapter extends RecyclerView.Adapter<CatiemCategoryListAdapter.ViewHolder> {

        Activity activity;
        ArrayList<ItemModelSet> catlistitem = new ArrayList<>();
        String check;

        public CatiemCategoryListAdapter(CategoryItemList categoryItemList, ArrayList<ItemModelSet> catiemegorylist, String check) {
            this.catlistitem = catiemegorylist;
            this.check = check;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView itemID, itemName, itemPrice, itemSize, itemQuantity;
            ImageView itemImage;

            public ViewHolder(View view) {
                super(view);
//            itemID = (TextView) view.findViewById(R.id.itemID);
                itemName = (TextView) view.findViewById(R.id.itemName);
                itemPrice = (TextView) view.findViewById(R.id.itemPrice);
                itemSize = (TextView) view.findViewById(R.id.itemSize);
                itemQuantity = (TextView) view.findViewById(R.id.itemQuantity);
                itemImage = (ImageView) view.findViewById(R.id.itemImage);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.catitemview, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String s = catlistitem.get(position).getItemImage();

//        holder.itemID.setText(catlistitem.get(position).getItemID());
            holder.itemName.setText(catlistitem.get(position).getItemName());
            holder.itemPrice.setText(catlistitem.get(position).getItemPrice());
            holder.itemSize.setText(catlistitem.get(position).getItemSize());
            holder.itemQuantity.setText(catlistitem.get(position).getItemQuantity());

            if (s != null) {
                String imgstr = catlistitem.get(position).getItemImage();
                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.itemImage.setImageBitmap(decodedByte);
            }
        }

        @Override
        public int getItemCount() {
            return catlistitem.size();
        }

    }
}
