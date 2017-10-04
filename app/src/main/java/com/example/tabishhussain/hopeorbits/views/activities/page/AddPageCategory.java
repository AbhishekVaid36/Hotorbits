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
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.CategoryModelss;
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

public class AddPageCategory extends Fragment implements View.OnClickListener {
    Button catsave;
    EditText catname;
    ImageView catimg;
    String base64 = "";
    CategoryModelss modelss;
    PageModelLists modelLists;
    ArrayList<PageModelLists> pagemlist = new ArrayList<>();
    ArrayList<CategoryModelss> categoryModelsses = new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs";
    String Ids;
    public static final int RESULT_OK = -1;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_add_category, container, false);
        catimg = (ImageView) view.findViewById(R.id.cateimage);
        catname = (EditText) view.findViewById(R.id.catnameed);
        catsave = (Button) view.findViewById(R.id.catsave);
        catimg.setOnClickListener(this);
        catsave.setOnClickListener(this);
        modelss = new CategoryModelss();
        modelLists = new PageModelLists();
        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("Id", "");
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cateimage:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 121);
                break;
            case R.id.catsave:
                postdatatoserver();
                break;
            default:
                break;
        }
    }

    private boolean validateData() {

        if (TextUtils.isEmpty(catname.getText().toString())) {
            catname.setError("Please provide page name");
            return false;
        }
//        if (base64.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Choose Image...", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }

    private void postdatatoserver() {

        if (validateData()) {
            categoryModelsses.clear();
            pagemlist.clear();
            pagemlist.clear();
            modelss.setCategoryName(catname.getText().toString());
            modelss.setCategoryImage("");
            categoryModelsses.add(modelss);
            modelLists.setCategoryModels(categoryModelsses);

            modelLists.setPageID(common.getPreferenceString(getActivity(), "PAGEID", ""));
            Log.d("printpageid",common.getPreferenceString(getActivity(), "PAGEID", ""));

            pagemlist.add(modelLists);

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.show();

            Call<JsonObject> call = HopeOrbitApi.retrofit.addpagecat(createcatepage());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.isSuccess()) {
                        try {
                            JSONObject respons = new JSONObject(response.body().toString());
                            Toast.makeText(getActivity(), respons.getString("message"), Toast.LENGTH_LONG).show();

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

//                            Intent is = new Intent(getActivity(), Yourpage.class);
//                            startActivity(is);
//                            getActivity().finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    } else {
                        try {
                            Log.d("errror", response.errorBody().string());
                            dialog.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println(t.toString());
                    dialog.dismiss();
                }
            });
        }
    }

    public HashMap<String, Object> createcatepage() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", Ids);
        params.put("pageModelList", pagemlist);
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 121 && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
                catimg.setImageBitmap(bitmap);
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





















