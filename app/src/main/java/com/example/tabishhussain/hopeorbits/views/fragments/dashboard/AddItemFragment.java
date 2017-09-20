package com.example.tabishhussain.hopeorbits.views.fragments.dashboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.databinding.FragmentAddItemBinding;
import com.example.tabishhussain.hopeorbits.views.fragments.BaseFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tabish Hussain on 7/18/2017.
 */

public class AddItemFragment extends BaseFragment implements View.OnClickListener {


    private static final int REQUEST_CODE_UPLOAD_IMAGE = 111;
    private static final String IMAGE_URI = "image_uri";
    private FragmentAddItemBinding mBinding;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_item, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
    }

    private void bindView() {
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.temp_categpries));

        mBinding.categories.setAdapter(categoriesAdapter);
        mBinding.addImage.setOnClickListener(this);
    }

    private void handleEditPhotoClick() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        String pickTitle = "Select or Take a new picture";
        Intent chooserIntent = Intent.createChooser(galleryIntent, pickTitle);
        final List<Intent> cameraIntents;
        imageUri = getUniqueImageUri("ShipMat" + "vessel.png");
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit().putString(IMAGE_URI, imageUri.toString()).apply();
        cameraIntents = getCameraIntents(imageUri);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, REQUEST_CODE_UPLOAD_IMAGE);
    }

    public Uri getUniqueImageUri(String name) {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                new SimpleDateFormat("yyyy_MM_dd_HHmmss",
                        Locale.getDefault()).format(Calendar.getInstance().getTime()) + name));
    }

    @NonNull
    private List<Intent> getCameraIntents(Uri uri) {
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            cameraIntents.add(intent);
        }
        return cameraIntents;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_UPLOAD_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = data == null || data.getData() == null ?
                            Uri.parse(PreferenceManager.getDefaultSharedPreferences(getContext())
                                    .getString(IMAGE_URI, null))
                            : data.getData();
                    View view = LayoutInflater
                            .from(getContext())
                            .inflate(R.layout.item_image_thumbnail, mBinding.container, false);
                    Picasso.with(getContext()).load(imageUri)
                            .placeholder(android.R.drawable.ic_menu_camera)
                            .into((ImageView) view.findViewById(R.id.thumbnail));
                    mBinding.container.addView(view, 0);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_image:
                handleEditPhotoClick();
                break;
            case R.id.add:
                break;
            case R.id.cancel:
                break;
        }
    }
}
