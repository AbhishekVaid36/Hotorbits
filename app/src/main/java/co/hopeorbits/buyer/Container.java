package co.hopeorbits.buyer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.vendor.SellerOrderView;
import co.hopeorbits.views.activities.accounts.CreditManagement;
import co.hopeorbits.views.activities.accounts.Splash;
import co.hopeorbits.views.activities.accounts.Yourpage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Container extends AppCompatActivity implements View.OnClickListener {

    //Defining Variables
    public static Toolbar toolbar;

    String[] yon;
    public static int add = 0;
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment;
    FragmentTransaction ft;
    TextView txtname, txtcredit, txtHome, txtOptions;
    RelativeLayout rlpic;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog2;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static int amount;
    ImageView profile_pic, imgcart,imgorder;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private static final int CAMERA_REQUEST = 1888;
    private Uri mImageCaptureUri;
    //    private File outPutFile = null;
    private String[] list_designation;
    private ListPopupWindow lpw;
    private String selectedPhoto;
    Bitmap bitmapPath = null;
    String profileImage,profileName, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        txtname = (TextView) findViewById(R.id.txtname);
        txtcredit = (TextView) findViewById(R.id.txtcredit);
        imgcart = (ImageView) findViewById(R.id.imgcart);
        imgorder = (ImageView) findViewById(R.id.imgorder);
        rlpic = (RelativeLayout) findViewById(R.id.rlpic);
        profile_pic = (ImageView) findViewById(R.id.imgpic);
        imgcart.setOnClickListener(this);
        imgorder.setOnClickListener(this);
        rlpic.setOnClickListener(this);
        profile_pic.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        profileImage = sharedpreferences.getString("ProfileImage", "");
        profileName = sharedpreferences.getString("name", "");
        txtname.setText(profileName);
        if (!profileImage.equalsIgnoreCase("")) {
            new UploadImage().execute();
        }
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("sdaf");
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        View logoView = getToolbarLogoIcon(toolbar);

        txtHome = (TextView) toolbar.findViewById(R.id.txthome);
        txtOptions = (TextView) toolbar.findViewById(R.id.txtoptions);
        txtHome.setOnClickListener(this);
        txtOptions.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.tool_bar, null);
        actionBar.setCustomView(customView);
//        Toolbar parent =(Toolbar) customView.getParent();
        toolbar.setPadding(10, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);
        initpDialog();
        getBalance();
        fragment = new Home();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager;
        switch (view.getId()) {
            case R.id.imgcart:
                 manager =getSupportFragmentManager();
                for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                    manager.popBackStack();
                }
                fragment = new BucketView();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
//                ft.addToBackStack("add" + Container.add);
                ft.commit();
//                Container.add++;

                break;
            case R.id.imgorder:
                 manager =getSupportFragmentManager();
                for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                    manager.popBackStack();
                }
                fragment = new OrderView();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
//                ft.addToBackStack("add" + Container.add);
                ft.commit();
//                Container.add++;

                break;
            case R.id.txthome:
                 manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                        manager.popBackStack();
                    }
                }
                fragment = new Home();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commit();
                break;
            case R.id.txtoptions:
                list_designation = getResources().getStringArray(R.array.options);
                lpw = new ListPopupWindow(Container.this);
                lpw.setAdapter(new ArrayAdapter<String>(Container.this, android.R.layout.simple_list_item_1, list_designation));
                lpw.setAnchorView(txtOptions);
                lpw.setModal(true);
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = list_designation[position];
                         if (position == 0) {
                            FragmentManager manager =getSupportFragmentManager();
                            for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                                manager.popBackStack();
                            }
                            fragment = new Yourpage();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame, fragment);
//                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
//                            Container.add++;
                        } else if (position == 1) {
                            FragmentManager manager =getSupportFragmentManager();
                            for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                                manager.popBackStack();
                            }
                            fragment = new CreditManagement();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame, fragment);
//                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
//                            Container.add++;
                        } else if (position == 2) {
                            FragmentManager manager =getSupportFragmentManager();
                            for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                                manager.popBackStack();
                            }
                            fragment = new SellerOrderView();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame, fragment);
//                            ft.addToBackStack("add" + Container.add);
                            ft.commit();
//                            Container.add++;
                        } else if (position == 3) {
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("Login", false);
                            editor.putString("Id", "0");
                            editor.commit();

                            FragmentManager manager = getSupportFragmentManager();
                            if (manager.getBackStackEntryCount() > 0) {
                                for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
                                    manager.popBackStack();
                                }
                            }
                            Container.this.finish();
                            Intent in = new Intent(Container.this, Splash.class);
                            startActivity(in);
                        }
                        lpw.dismiss();
                    }
                });
                lpw.show();
                break;
            case R.id.imgpic:
                photo();
                break;
            case R.id.rlpic:
                photo();
                break;
        }
    }

    public void photo() {
//        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Container.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Capture Photo")) {

//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
//                    mImageCaptureUri = Uri.fromFile(f);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                    startActivityForResult(intent, CAMERA_CODE);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                } else if (items[item].equals("Choose from Gallery")) {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(Container.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(mImageCaptureUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            // String selectedPhoto contains the path of selected Image
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            selectedPhoto = cursor.getString(columnIndex);
//            photoName = selectedPhoto.substring(selectedPhoto.lastIndexOf("/") + 1);
            cursor.close();

        }
        else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    filePathColumn, null, null, null);
            int column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToLast();

            selectedPhoto = cursor.getString(column_index_data);
            cursor.close();
        }

        try {
            if (!selectedPhoto.isEmpty()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(selectedPhoto);
                profile_pic.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), myBitmap));
                rlpic.setVisibility(View.GONE);
                new ImageProgress().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    private void CropingIMG() {

        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image*/
/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROPING_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
//                    break;
                }

                CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Crop");
                builder.setCancelable(false);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
*/

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    public void getBalance() {
        showpDialog();
        Call<JsonObject> call = HopeOrbitApi.retrofit.showCredit(getcreditParams());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());

                        amount = jsonObj.getInt("amount");
                        txtcredit.setText("Balance: \u20A8" + amount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    try {
                        Log.d("Tag", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Tag", t.toString());
                hidepDialog();
            }
        });
    }

    public HashMap<String, Object> getcreditParams() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", sharedpreferences.getString("phone", ""));
        return params;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }

    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(Container.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Double screenInches = Math.sqrt(x + y);

        Integer inch = screenInches.intValue();
        if (inch >= 5) {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        } else {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
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
            HttpPost httppost = new HttpPost("http://13.58.110.101:8080/hoprepositoryweb/user/uploadProfileImage");

            File sourceFile = new File(selectedPhoto);

            try {

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.STRICT);
                FileBody fileBody = new FileBody(sourceFile);
                builder.addPart("file", fileBody);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                builder.addPart("userid", new StringBody(sharedpreferences.getString("Id", ""), ContentType.TEXT_PLAIN));
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
            Toast.makeText(Container.this, s, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObj = new JSONObject(s.toString());
                if (jsonObj.has("message")) {
                    message = jsonObj.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("ProfileImage", "http://13.58.110.101:8080" + message);
            editor.commit();


        }

    }

    public class UploadImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                InputStream input = null;
                URL url = new URL(profileImage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.connect();
                input = connection.getInputStream();

                BitmapFactory.Options opts = new BitmapFactory.Options();
                // opts.inJustDecodeBounds = true;
                opts.inSampleSize = 1;
                bitmapPath = BitmapFactory.decodeStream(input, null, opts);
                //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                //myBitmap.recycle();

                //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                connection.disconnect();
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            profile_pic.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bitmapPath));
            rlpic.setVisibility(View.GONE);
        }
    }
}