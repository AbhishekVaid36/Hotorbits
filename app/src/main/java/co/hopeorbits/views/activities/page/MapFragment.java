package co.hopeorbits.views.activities.page;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.buyer.Container;
import co.hopeorbits.views.activities.accounts.Yourpage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 04-Oct-17.
 */

public class MapFragment extends Fragment implements View.OnClickListener, LocationListener, OnMapReadyCallback {
    Button skip, btngo, btnsave;
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
    public static GoogleMap mGoogleMap;
    private static final long MIN_TIME_BW_UPDATES = 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    public static Location location = null;
    String adrress;
    MarkerOptions markerOptions;
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
        btnsave = (Button) view.findViewById(R.id.btnsave);
        add_search = (EditText) view.findViewById(R.id.add_search);
        btngo.setOnClickListener(this);
        skip.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        pageId = bundle.getString("pageId");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
//        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String bestProvider = locationManager.getBestProvider(criteria, true);
//        Location location = locationManager.getLastKnownLocation(bestProvider);
//        if (location != null) {
//            onLocationChanged(location);
//        }
//        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment
//            SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapp);
//
//            mGoogleMap = fragment.getMap();
            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.googleMap);
            if (fragment == null) {
                fragment = SupportMapFragment.newInstance();
                fm.beginTransaction().replace(R.id.googleMap, fragment).commit();
            } else {
                mGoogleMap = fragment.getMap();
            }
//            mGoogleMap = mapView.getMap();
//            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//            mGoogleMap.setMyLocationEnabled(true);

             markerOptions = new MarkerOptions();

            try {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    //this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    mLatitude = location.getLatitude();
                                    mLongitude = location.getLongitude();
                                } else {
                                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();
                                    //				alertDialog.setTitle("Your phone does not support GPS or NETWORK access, you can still use the application but some features may not work properly.");
                                    alertDialog.setMessage("Your phone does not find current location.");//but some features may not work properly
                                    //alertDialog.setIcon(R.drawable.ic_launcher);
                                    alertDialog.setButton("QUIT", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
//                                            MapActivity.this.finish();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (location != null) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                latLng = new LatLng(mLatitude, mLongitude);
//                getCompleteAddressString(mLatitude,mLongitude);
                markerOptions.title("Your current Location:");
                markerOptions.snippet(getCompleteAddressString(mLatitude, mLongitude));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.redmarker));
                markerOptions.position(latLng);
                markerOptions.draggable(true);
                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
                onLocationChanged(location);
            }
//            JSONAsyncTaskMap task = new JSONAsyncTaskMap();
//            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                mLatitude = arg0.getPosition().latitude;
                mLongitude = arg0.getPosition().longitude;
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                mGoogleMap.clear();
                 latLng = new LatLng(mLatitude, mLongitude);
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory .fromResource(R.mipmap.redmarker)).draggable(true));

            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });
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
                String address = add_search.getText().toString();

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address,getActivity(), new Handler());
                break;
            case R.id.skipbtn:
                goPage();
                break;
            case R.id.btnsave:
                addresssave(pageId);
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


    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        Log.d("latlong", latLng.toString());
        latitute = mLatitude;
        lontitute = mLongitude;
        this.latLng = latLng;

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
//                Log.w("My Current loction address","" + strReturnedAddress.toString());
            } else {
//                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
    public class GeocodingLocation {

        private static final String TAG = "GeocodingLocation";

        public void getAddressFromLocation(final String locationAddress,final Context context, final Handler handler) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List addressList = geocoder.getFromLocationName(locationAddress, 1);
                        if (addressList != null && addressList.size() > 0) {

                          Address address = (Address) addressList.get(0);

                            if (address != null) {

                                mLatitude = address.getLatitude();
                                mLongitude = address.getLongitude();
                                latLng = new LatLng(mLatitude, mLongitude);
                                adrress=address.getAddressLine(0);

                                Handler mainHandler = new Handler(Looper.getMainLooper());

                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        mGoogleMap.clear();
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.title(adrress);
                                        markerOptions.snippet(getCompleteAddressString(mLatitude, mLongitude));
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.redmarker));
                                        markerOptions.position(latLng);
                                        markerOptions.draggable(true);
                                        mGoogleMap.addMarker(markerOptions);
                                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
                                    }
                                };
                                mainHandler.post(myRunnable);
                            }


                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to connect to Geocoder", e);
                    }
                }
            };
            thread.start();
        }

    }
}
