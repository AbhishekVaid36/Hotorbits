package com.example.tabishhussain.hopeorbits.vendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.Constant;
import com.example.tabishhussain.hopeorbits.holder.PageModelList;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tabishhussain.hopeorbits.api.HopeOrbitApi.retrofit;

public class BusinessPage extends AppCompatActivity implements View.OnClickListener, Constant {
    String[] spvalue1;
    Spinner spiner1;
    CustomAdapter customAdapter;
    RelativeLayout rlsubmit;
    private ProgressDialog pDialog;
    private String userChoosenTask, selectedPhoto;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CircularImageView img_pic;
    String base64Image;
    PageModelList modelList;
    ArrayList<PageModelList> modelLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buessiness_page);
        rlsubmit = (RelativeLayout) findViewById(R.id.rlsubmit);
        img_pic = (CircularImageView) findViewById(R.id.profile_pic);
        spvalue1 = new String[]{"Currency", "INR", "PKR", "USD"};

        spiner1 = (Spinner) findViewById(R.id.simplecurrency);
        spiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BusinessPage.this, spvalue1[i], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        customAdapter = new CustomAdapter(BusinessPage.this, spvalue1);
        spiner1.setAdapter(customAdapter);
        rlsubmit.setOnClickListener(this);
        img_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        modelList = new PageModelList();
        initialize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessPage.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(BusinessPage.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlsubmit:
                postData();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = BusinessPage.this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                // String selectedPhoto contains the path of selected Image
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedPhoto = cursor.getString(columnIndex);
                cursor.close();
// convert image to base64
                Bitmap bm = BitmapFactory.decodeFile(selectedPhoto);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();

                base64Image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] byteArrayImage = bytes.toByteArray();

        base64Image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        selectedPhoto = destination.toString();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        img_pic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(BusinessPage.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        img_pic.setImageBitmap(bm);
    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        String[] countryNames;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, String[] countryNames) {
            this.context = applicationContext;
            this.countryNames = countryNames;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return countryNames.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.custom_spinner_items, null);
            TextView names = (TextView) view.findViewById(R.id.textView);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
            names.setText(countryNames[i]);
            if (i == 0) {
                rl.setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                rl.setBackgroundColor(getResources().getColor(R.color.spinnerbackground));
            }
            return view;
        }
    }

    public void postData() {
        showpDialog();
        /*final String URL = "http://13.58.110.101:8080/hoprepositoryweb/user/createPage";
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(BusinessPage.this, error.toString(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", "c5e5870e-1e89-4a6e-b087-a129ba4fd85d");
                JSONObject user = new JSONObject();
                try {
                    user.put("pageName", "Grocery");
                    user.put("currency", "IN");
                    user.put("details", "welcome");
                    user.put("pageImage", base64Image);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray notebookUsers = new JSONArray();
                notebookUsers.put(user);
                params.put("pageModelList",notebookUsers.toString());
                System.out.println("the JSON ARRAY is" + notebookUsers);
                return params;
            }
        };
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);
        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
*/

// Post params to be sent to the server
       /* HashMap<String, String> params = new HashMap<String, String>();
        try {
            params.put("userId", "580ccfc3-c675-4d5c-a58c-3882e509b55a8");
            JSONObject user = new JSONObject();
            try {
                user.put("pageName", "Grocery");
                user.put("currency", "IN");
                user.put("details", "welcome");
                user.put("pageImage","");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONArray notebookUsers = new JSONArray();
            notebookUsers.put(user);
            params.put("pageModelList", notebookUsers.toString());
            System.out.println("the JSON ARRAY is" + notebookUsers);
        } catch (Exception e) {
        }


        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           Toast.makeText(BusinessPage.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        });

// add the request object to the queue to be executed
        int socketTimeout = 60000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        request_json.setRetryPolicy(policy);
        ConnectionDetector.getInstance().addToRequestQueue(request_json);*/

        Call<JSONObject> calls = retrofit.createpage(getposting());
        calls.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.toString());
                        Log.e("error", jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        Log.e("error1234556", jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "unsuccess", Toast.LENGTH_LONG).show();
                }
                Log.e("errormyerror", response.toString());

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("errorqqq", t.toString());
            }
        });
    }
    private HashMap<String, Object> getposting() {
        HashMap<String, Object> givepostpage = new HashMap<>();
        givepostpage.put("userId","245fdc99-8466-4b54-9b4c-21904094b39e");
        modelLists.clear();
        modelList.setPageName("rtyr");
        modelList.setCurrency("IN");
        modelList.setDetails("dsf");
        modelList.setPageImage("");
        modelLists.add(modelList);
        givepostpage.put("pageModelList", modelLists);
        return givepostpage;
    }
    public void initialize() {
        pDialog = new ProgressDialog(BusinessPage.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
