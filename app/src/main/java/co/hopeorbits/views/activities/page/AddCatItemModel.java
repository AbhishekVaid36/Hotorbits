package co.hopeorbits.views.activities.page;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.buyer.Container;
import co.hopeorbits.holder.CategoryModelsCatItem;
import co.hopeorbits.holder.ItemModelSetCatItems;
import co.hopeorbits.holder.PageModelListCatItem;
import co.hopeorbits.utils.common;
import co.hopeorbits.views.activities.accounts.Yourpage;
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
    private String selectedPhoto,photoName;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
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
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 404);
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_CODE);
                break;
            case R.id.item_save_cat:
                PostingdatacatItem();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
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

            itemModelSet.setItemImage(photoName);
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

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {
            Uri filePath = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(filePath,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            // String selectedPhoto contains the path of selected Image
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            selectedPhoto = cursor.getString(columnIndex);
            photoName = selectedPhoto.substring(selectedPhoto.lastIndexOf("/") + 1);
            cursor.close();


            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            123);
//                    return;
//                }

                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                base64 = getEncoded64ImageStringFromBitmap(bitmap);
                //Setting the Bitmap to ImageView
                Add_item_image.setImageBitmap(bitmap);
                new ImageProgress().execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class ImageProgress extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://13.58.110.101:8080/hoprepositoryweb/user/uploadFile");

            File sourceFile = new File(selectedPhoto);

            try {

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.STRICT);
                FileBody fileBody = new FileBody(sourceFile);
                builder.addPart("file", fileBody);
                HttpEntity entity = builder.build();

                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {

                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {

                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }

            } catch (ClientProtocolException e) {

                responseString = e.toString();

            } catch (IOException e) {

                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
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
