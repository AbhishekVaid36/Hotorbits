package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.views.activities.accounts.Yourpage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 04-Oct-17.
 */

public class MapFragment extends Fragment implements View.OnClickListener, LocationListener, OnMapReadyCallback {
    Button skip, btngo;
    EditText add_search;
    Fragment fragment;
    FragmentTransaction ft;
    String pageId, Pageidswrongarray;
    private LatLng latLng;
    private double latitute, lontitute;
    private final int REQUEST_FINE_LOCATION = 1234;
    private GoogleMap mMap;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.dialogmap, container, false);
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        btngo = (Button) view.findViewById(R.id.mapgo);
        skip = (Button) view.findViewById(R.id.skipbtn);
        add_search = (EditText) view.findViewById(R.id.add_search);
        btngo.setOnClickListener(this);
        skip.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        pageId = bundle.getString("pageId");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("gps", "Location permission granted");
                    try {
//                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    } catch (SecurityException ex) {
                        Log.d("gps", "Location permission did not work!");
                    }
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        Log.d("latlong", latLng.toString());
        latitute = latitude;
        lontitute = longitude;
        this.latLng = latLng;
    }

    private void moveMap(LatLng latLng) {
        mMap.clear();
//        Toast.makeText(getContext(),"+123"+String.valueOf(latLng),Toast.LENGTH_LONG).show();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("My Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitute + "," + lontitute + "&key=AIzaSyBuSw82w1QDff56O7lDi9y9Uq8NA3QDFos",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject parent = new JSONObject(response);
                            String status = String.valueOf(parent.get("status"));

                            if (status.equals("OK")) {
                                JSONArray array = parent.getJSONArray("results");
                                JSONObject object = array.getJSONObject(0);
                                Log.d("inderaavyu", object.getString("formatted_address"));
                                add_search.setText(object.getString("formatted_address"));
//                                EditUClass.countryname.setText(address[0]);
//                                CurrentLocation.country.setText(address[0]);
                                Log.d("myaddress", add_search.getText().toString().trim());
                            } else {
                                Toast.makeText(getActivity(), "Invalide Address", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Check your Internet" + error, Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (latLng != null) {
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            moveMap(latLng);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            alertDialogBuilder
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mapgo:
                addresssave(pageId);
                goPage();
                break;
            case R.id.skipbtn:
                goPage();
                break;

        }
    }

    public void goPage() {
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
    }

    private void addresssave(String pageId) {
        Pageidswrongarray = pageId;
        Call<JSONObject> call = HopeOrbitApi.retrofit.saveaddress(getaddress());
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                JSONObject object;
                JSONArray array;
                if (response.isSuccess()) {
                    String respons = response.body().toString();
                    Log.d("addressres", respons);
//                    Intent i = new Intent(getActivity(), Yourpage.class);
//                    startActivity(i);
//                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });

    }

    public HashMap<String, Object> getaddress() {
        HashMap<String, Object> params = new HashMap<>();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String UserId = sharedpreferences.getString("Id", "");
        params.put("userId", UserId);
        params.put("pageid", Pageidswrongarray);
        params.put("address", add_search.getText().toString());
        params.put("latitude", latitute);
        params.put("longitude", lontitute);
        return params;
    }

}
