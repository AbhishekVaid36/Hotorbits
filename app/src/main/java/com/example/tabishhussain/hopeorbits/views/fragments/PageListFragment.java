package com.example.tabishhussain.hopeorbits.views.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.Constant;
import com.example.tabishhussain.hopeorbits.connectiondetector.ConnectionDetector;
import com.example.tabishhussain.hopeorbits.connectiondetector.CustomRequest;
import com.example.tabishhussain.hopeorbits.holder.Holder;
import com.example.tabishhussain.hopeorbits.views.activities.page.AddPageActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tabish Hussain on 7/11/2017.
 */

public class PageListFragment extends BaseFragment implements View.OnClickListener, Constant {

    private static final int REQUEST_CODE_PERMISSIONS = 222;
    View view;
    ListView listView;

    JSONArray totalshop = null;
    int shoplength;
    String Id, Name, Phone, Email, City, State, Country, Address, Zipcode, Distance, Lat, Longi, Image;
    ArrayList<Holder> list = new ArrayList<Holder>();
    private static final long MIN_TIME_BW_UPDATES = 1;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    public static Location clocation = null;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    JSONObject jsonresponce;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        checkForRequiredPermissions();
//        fab.setOnClickListener(this);
//        bindView();
        initialize();
        getlistData();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), AddPageActivity.class));
                break;
        }
    }

    @TargetApi(23)
    public boolean checkForRequiredPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSIONS);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {

            }
        }
    }

    public void getlistData() {
        showpDialog();
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_SIGNUP, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonresponce = response;
                        otherThread();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                otherThread();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lati", "30.7333148");
                params.put("logi", "76.7794179");
                return params;
            }
        };
        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {
        listView.setAdapter(new MyCustomAdapter(getActivity(), list));
        //			ListUtils.setDynamicHeight(gridview);
        if (shoplength == 0) {
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
        }
    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<Holder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<Holder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }

        class ViewHolder {
            TextView txtname, txtaddress, txtemail;
            CircularImageView imgshop, imgdummy;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.list_fragment_listitem, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) paramView.findViewById(R.id.txtname);
                holder.txtaddress = (TextView) paramView.findViewById(R.id.txtaddress);
                holder.txtemail = (TextView) paramView.findViewById(R.id.txtemail);
                holder.imgshop = (CircularImageView) paramView.findViewById(R.id.imgshop);
                holder.imgdummy = (CircularImageView) paramView.findViewById(R.id.imgdummy);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            Holder h = list.get(paramInt);
            holder.txtname.setText(h.getName());
            holder.txtaddress.setText(h.getAddress() + ", " + h.getCity() + ", " + h.getState() + ", " + h.getCountry() + "," + h.getZipcode());
            holder.txtemail.setText(h.getEmail());
            Bitmap bitmap = h.getBitmap();
            if (!h.getImage().equalsIgnoreCase("")) {
                holder.imgshop.setImageBitmap(bitmap);
                holder.imgshop.setVisibility(View.VISIBLE);
                holder.imgdummy.setVisibility(View.GONE);
            } else {
                holder.imgshop.setVisibility(View.GONE);
                holder.imgdummy.setVisibility(View.VISIBLE);
                holder.imgdummy.setBackgroundResource(R.mipmap.ic_launcher);
            }

            return paramView;
        }
    }


    public void otherThread() {


        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //http://www.puyangan.com/api/category.php?cid=178
                    // Getting Array of Employee
                    totalshop = jsonresponce.getJSONArray("shops");
                    shoplength = totalshop.length();
                    for (int i = 0; i < shoplength; i++) {
                        JSONObject c = totalshop.getJSONObject(i);
                        //					Bitmap myBitmap = null;
                        InputStream input = null;
                        Id = c.getString("id");
                        Name = c.getString("name");
                        Phone = c.getString("phone");
                        Email = c.getString("email");
                        City = c.getString("city");
                        State = c.getString("state");
                        Country = c.getString("country");
                        Address = c.getString("address");
                        Zipcode = c.getString("zipcode");
                        Distance = c.getString("distance");
                        Longi = c.getString("longitude");
                        Lat = c.getString("latitude");
                        Image = c.getString("photo");
                        Holder h = new Holder();
                        if (Image.equalsIgnoreCase("")) {

                        } else {

                            URL url = new URL(Image);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.setInstanceFollowRedirects(false);
                            connection.setRequestMethod("GET");
                            connection.connect();
                            input = connection.getInputStream();

                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            // opts.inJustDecodeBounds = true;
                            opts.inSampleSize = 2;
                            Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
                            //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                            //myBitmap.recycle();

                            //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            h.setBitmap(myBitmap);
                            connection.disconnect();
                        }


                        h.setId(Id);
                        h.setName(Name);
                        h.setPhone(Phone);
                        h.setEmail(Email);
                        h.setCity(City);
                        h.setState(State);
                        h.setCountry(Country);
                        h.setAddress(Address);
                        h.setZipcode(Zipcode);
                        h.setDistance(Distance);
                        h.setLat(Lat);
                        h.setLongi(Longi);
                        h.setImage(Image);

                        list.add(h);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                } finally {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hidepDialog();
                            loadingComplete();
                        }
                    });
                }
            }
        };

        thread.start();
    }

    public void initialize() {
        pDialog = new ProgressDialog(getActivity());
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
