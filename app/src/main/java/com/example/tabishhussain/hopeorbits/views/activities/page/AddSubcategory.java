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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.SubCategoryIntoCategoryList;
import com.example.tabishhussain.hopeorbits.holder.SubCategoryModels;
import com.example.tabishhussain.hopeorbits.holder.SubpageModelList;
import com.example.tabishhussain.hopeorbits.utils.common;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.Yourpage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSubcategory extends Fragment implements View.OnClickListener {


    String subcatbase64 = "";
    ImageView subcateimage;
    EditText subcatnameed;
    Button subcatsave;

    SubpageModelList pageModelList;
    SubCategoryModels subCategoryModels;
    SubCategoryIntoCategoryList SubCategoryIntoCategoryList;

    ArrayList<SubpageModelList> pagemodellist = new ArrayList<>();
    ArrayList<SubCategoryModels> subCategoryModelses = new ArrayList<>();
    ArrayList<SubCategoryIntoCategoryList> subCategoryIntoCategoryLists = new ArrayList<>();
    String listcatid;


    public static final String MyPREFERENCES = "MyPrefs";
    String Ids;
    public static final int RESULT_OK = -1;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_add_subcategory, container, false);
        subcateimage = (ImageView) view.findViewById(R.id.subcateimageadd);
        subcatnameed = (EditText) view.findViewById(R.id.subcatnameedadd);
        subcatsave = (Button) view.findViewById(R.id.subcatsave);
        subcateimage.setOnClickListener(this);
        subcatsave.setOnClickListener(this);

        SubCategoryIntoCategoryList = new SubCategoryIntoCategoryList();
        subCategoryModels = new SubCategoryModels();
        pageModelList = new SubpageModelList();

        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("Id", "");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listcatid = bundle.getString("subcatoritemcategory");
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subcateimageadd:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1234);
                break;
            case R.id.subcatsave:
                createPageChildcat();
                break;
        }
    }
    private boolean validateData() {

        if (TextUtils.isEmpty(subcatnameed.getText().toString())) {
            subcatnameed.setError("Please provide page name");
            return false;
        }
//        if (subcatbase64.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Choose Image...", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }
    private void createPageChildcat() {
        if (validateData()) {
            subCategoryModelses.clear();
            subCategoryModelses.clear();
            pagemodellist.clear();

            SubCategoryIntoCategoryList.setCategoryName(subcatnameed.getText().toString());
            SubCategoryIntoCategoryList.setCategoryImage("");
            subCategoryIntoCategoryLists.add(SubCategoryIntoCategoryList);

            subCategoryModels.setCategoryID(listcatid);
            subCategoryModels.setCategoryIntoCategoryList(subCategoryIntoCategoryLists);
            subCategoryModelses.add(subCategoryModels);

            pageModelList.setPageID(common.getPreferenceString(getActivity(),"PAGEID",""));
            pageModelList.setCategoryModels(subCategoryModelses);
            pagemodellist.add(pageModelList);

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.show();
            Call<JsonObject> call = HopeOrbitApi.retrofit.createPageChildCategories(childcategory());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        if(response.isSuccess()){
                            JSONObject object = new JSONObject(response.body().toString());
                            dialog.dismiss();
//                            Intent i =   new Intent(AddSubcategory.this,Yourpage.class);
//                            startActivity(i);
//                            AddSubcategory.this.finish();
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
        params.put("pageModelList",pagemodellist );
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
                subcatbase64 = getEncoded64ImageStringFromBitmap(bitmap);
                //Setting the Bitmap to ImageView
                subcateimage.setImageBitmap(bitmap);
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
        return imgString;
    }
}
