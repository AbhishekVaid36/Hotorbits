package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.CategoryModelsCatItem;
import com.example.tabishhussain.hopeorbits.holder.ItemModelSetCatItems;
import com.example.tabishhussain.hopeorbits.holder.PageModelListCatItem;
import com.example.tabishhussain.hopeorbits.utils.common;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.Yourpage;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abc on 9/29/2017.
 */

public class AddCatItemModel extends Fragment implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    String Ids;
    String categoryid;
    EditText cat_item_name,cat_item_size,item_prize,item_Quantity;
    ImageView Add_item_image;
    Button item_save_cat;
    String base64 = "";

    PageModelListCatItem pageModelList;
    CategoryModelsCatItem categoryModels;
    ItemModelSetCatItems itemModelSet;

    ArrayList<PageModelListCatItem> pageModelListCatItems = new ArrayList<>();
    ArrayList<CategoryModelsCatItem> categoryModelsCatItems = new ArrayList<>();
    ArrayList<ItemModelSetCatItems>  itemModelSetCatItemses = new ArrayList<>();
    public static final int RESULT_OK = -1;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_addcat_item, container, false);

        cat_item_name = (EditText)view.findViewById(R.id.cat_item_name);
        cat_item_size = (EditText)view.findViewById(R.id.cat_item_size);
        item_prize = (EditText)view.findViewById(R.id.item_prize);
        item_Quantity = (EditText)view.findViewById(R.id.item_Quantity);
        Add_item_image = (ImageView)view.findViewById(R.id.Add_item_image);
        item_save_cat = (Button)view.findViewById(R.id.item_save_cat);
        Add_item_image.setOnClickListener(this);
        item_save_cat.setOnClickListener(this);

        itemModelSet = new ItemModelSetCatItems();
        categoryModels = new CategoryModelsCatItem();
        pageModelList = new PageModelListCatItem();
        Bundle bundle = this.getArguments();
        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("Id", "");
        if (bundle != null) {
            categoryid = bundle.getString("categoryid");
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Add_item_image:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 404);
                break;
            case R.id.item_save_cat:
                PostingdatacatItem();
                break;
        }
    }


    private boolean validateData() {

        if (TextUtils.isEmpty(cat_item_name.getText().toString())) {
            cat_item_name.setError("Please provide item name");
            return false;
        }if (TextUtils.isEmpty(cat_item_size.getText().toString())) {
            cat_item_size.setError("Please provide item size");
            return false;
        }if (TextUtils.isEmpty(item_prize.getText().toString())) {
            item_prize.setError("Please provide item prize");
            return false;
        }if (TextUtils.isEmpty(item_Quantity.getText().toString())) {
            item_Quantity.setError("Please provide item quantity");
            return false;
        }
//        if (subcatbase64.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Choose Image...", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }

    private void PostingdatacatItem() {
        if (validateData()) {
            itemModelSetCatItemses.clear();
            categoryModelsCatItems.clear();
            pageModelListCatItems.clear();

            itemModelSet.setItemImage("");
            itemModelSet.setItemName(cat_item_name.getText().toString());
            itemModelSet.setItemSize(cat_item_size.getText().toString());
            itemModelSet.setItemPrice(item_prize.getText().toString());
            itemModelSet.setItemQuantity(item_Quantity.getText().toString());
            itemModelSetCatItemses.add(itemModelSet);
            categoryModels.setCategoryID(categoryid);
            categoryModels.setItemModelSet(itemModelSetCatItemses);
            categoryModelsCatItems.add(categoryModels);
            pageModelList.setPageID(common.getPreferenceString(getActivity(),"PAGEID",""));
            pageModelList.setCategoryModels(categoryModelsCatItems);
            pageModelListCatItems.add(pageModelList);

            JSONArray array = new JSONArray(pageModelListCatItems);
            Log.d("myjsonArray",array.toString());

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.show();
            Call<JsonObject> call = HopeOrbitApi.retrofit.createcategoryitemset(childcategory());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        if(response.isSuccess()){
                            JSONObject object = new JSONObject(response.body().toString());
                            Log.d("myitemdata",object.toString());
                            dialog.dismiss();
//                            Intent i =   new Intent(AddCatItemModel.this,Yourpage.class);
//                            startActivity(i);
//                            AddCatItemModel.this.finish();
                            for (int i = 0; i < 2; i++) {
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                manager.popBackStack();
                            }
                            fragment = new Yourpage();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
                            Container.add++;
                        }else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }
    }

    public HashMap<String, Object> childcategory() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", Ids);
        params.put("pageModelList",pageModelListCatItems );
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 404 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            123);
                    return;
                }
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                base64 = getEncoded64ImageStringFromBitmap(bitmap);
                //Setting the Bitmap to ImageView
                Add_item_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        System.out.println(imgString);
        return imgString;
    }
}
