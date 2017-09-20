package com.example.tabishhussain.hopeorbits.views.fragments.dashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.databinding.FragmentMapViewBinding;
import com.example.tabishhussain.hopeorbits.views.fragments.BaseFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Tabish Hussain on 7/10/2017.
 */

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback {

    private FragmentMapViewBinding mBinding;
    private static final int REQUEST_CODE_PERMISSIONS = 111;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_view, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkForRequiredPermissions();
        mBinding.mapview.onCreate(savedInstanceState);
        bindView();
    }

    private void bindView() {
        mBinding.mapview.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        showLocationOnMap();
    }

    private void showLocationOnMap() {
//        GPSTracker gpsTracker = new GPSTracker(getContext());
//        if (googleMap == null) {
//            return;
//        }
//        googleMap.clear();
//        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//        googleMap.addMarker(new MarkerOptions()
//                .position(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }


    @Override
    public void onResume() {
        mBinding.mapview.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.mapview.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapview.onLowMemory();
    }

    @TargetApi(23)
    public boolean checkForRequiredPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSIONS);
            return false;
        } else {
            showLocationOnMap();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    showLocationOnMap();
                }
            }
        }
    }
}
