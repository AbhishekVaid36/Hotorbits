package co.hopeorbits.views.activities.page;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.buyer.Container;
import co.hopeorbits.holder.PageModelList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    PageModelList modelList;
    ArrayList<PageModelList> modelLists = new ArrayList<>();
    EditText pagename;
    String currency = "", pageType = "";
    Spinner sp_currency, sp_page_type;
    String id;
    String Ids;
    String findpagename;

    public static final String MyPREFERENCES = "MyPrefs";

    EditText add_search;
    Fragment fragment;
    FragmentTransaction ft;
    ImageView img;
    public static final int RESULT_OK = -1;
    private String selectedPhoto, photoName;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    TextView txtcheck, txtsubmit;
    RelativeLayout rlpic;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_home, container, false);
        txtcheck = (TextView) view.findViewById(R.id.txtcheck);
        txtsubmit = (TextView) view.findViewById(R.id.txtsubmit);
        img = (ImageView) view.findViewById(R.id.img);
        rlpic = (RelativeLayout) view.findViewById(R.id.rlpic);
        modelList = new PageModelList();
////bind
//        refres = (ImageView) findViewById(R.id.refress);
//        msg = (ImageView) findViewById(R.id.msg);
//        altmsg = (ImageView) findViewById(R.id.msgalert);
//        shoping = (ImageView) findViewById(R.id.shoping);
        pagename = (EditText) view.findViewById(R.id.pagename);

        txtcheck.setOnClickListener(this);
        txtsubmit.setOnClickListener(this);
        rlpic.setOnClickListener(this);

        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("Id", "");
        sp_currency = (Spinner) view.findViewById(R.id.sp_currency);
        sp_page_type = (Spinner) view.findViewById(R.id.sp_page_type);
//        spinner.setPrompt("Select an item");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> CurrencyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Currency, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> pageTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.page_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        CurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp_currency.setAdapter(CurrencyAdapter);
        sp_page_type.setAdapter(pageTypeAdapter);
        sp_currency.setOnItemSelectedListener(this);
//        sp_page_type.setOnItemSelectedListener(this);
        sp_page_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pageType = sp_page_type.getSelectedItem().toString();
                if (pageType.equals("Page type")) {
                    pageType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img.setOnClickListener(this);
        return view;
    }

    //valide method inbuilt
    private boolean validateData() {

        if (TextUtils.isEmpty(pagename.getText().toString())) {
            pagename.setError("Please provide page name");
            return false;
        }

        if (currency.isEmpty()) {
            Toast.makeText(getActivity(), "Select currency...", Toast.LENGTH_LONG).show();
            return false;
        }
        if (pageType.isEmpty()) {
            Toast.makeText(getActivity(), "Select page type...", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //click handle

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtsubmit:
                postingpage();
//                UploadPhoto uploadProfilePhoto = new UploadPhoto();
//                uploadProfilePhoto.execute();
                break;
            case R.id.txtcheck:
                avaibilitycheck();
                break;
            case R.id.rlpic:
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 121);
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_CODE);
                break;
            case R.id.img:
//                Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(in, GALLERY_CODE);
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
//                base64 = getEncoded64ImageStringFromBitmap(bitmap);
                //Setting the Bitmap to ImageView
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                new ImageProgress().execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

  /*  public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        System.out.println(imgString);
        return imgString;
    }*/

    public class ImageProgress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Uploading...");
            pdia.show();
            pdia.setCancelable(false);
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
            pdia.dismiss();
            pdia = null;
        }

    }

    private void avaibilitycheck() {

        findpagename = pagename.getText().toString();
        if (!findpagename.equals("")) {

            Call<ResponseBody> call = HopeOrbitApi.retrofit.checkavil(findpagename);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String MyResult = null;

                    try {

                        MyResult = response.body().string();
                        String s = new JSONObject(MyResult).getString("message");
                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Enter Page Name ", Toast.LENGTH_LONG).show();
        }
    }
//page post to server method

    private void postingpage() {

        if (validateData()) {
            try {
                File sourceFile = new File(selectedPhoto);
                FileBody fileBody = new FileBody(sourceFile);
                modelLists.clear();
                modelList.setPageName(pagename.getText().toString());
                modelList.setCurrency(currency);
                modelList.setDetails(pageType);
                modelList.setPageImage(photoName);
                modelLists.add(modelList);
            } catch (Exception e) {
                Log.e("sdf", e.toString());
            }
            final ProgressDialog dialog = new ProgressDialog(getActivity());

            dialog.show();

            Call<JsonObject> call = HopeOrbitApi.retrofit.createpage(getParams());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JSONObject object;
                    JSONArray array;
                    if (response.isSuccess()) {
                        String respons = response.body().toString();
                        try {
                            object = new JSONObject(respons);
                            array = object.getJSONArray("pageModelList");
                            JSONObject object1 = array.getJSONObject(0);
                            String PageId = object1.getString("pageID");
                            String errorMessage = object1.getString("errorMessage");
                            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                                Toast.makeText(getActivity(), object1.getString("errorMessage"), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                //--------------decode base64------------


//                                String imgstr = object1.getString("pageImage");
//                                Log.d("errrorimage", imgstr.toString());
//                                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
//                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                dialog.dismiss();

                                Bundle bundle = new Bundle();
                                bundle.putString("pageId", PageId);
                                fragment = new MapFragment();
                                fragment.setArguments(bundle);
                                ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.add(R.id.frame, fragment);
                                ft.addToBackStack("add" + Container.add);
                                ft.commit();
                                Container.add++;


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }

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

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", Ids);
        params.put("pageModelList", modelLists);
        return params;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currency = sp_currency.getSelectedItem().toString();
        if (currency.equals("Currency")) {
            currency = "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

