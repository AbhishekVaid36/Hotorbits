package com.example.tabishhussain.hopeorbits.views.fragments.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.databinding.FragmentAddPageBinding;
import com.example.tabishhussain.hopeorbits.views.activities.page.AddCategoryActivity;
import com.example.tabishhussain.hopeorbits.views.activities.page.AddItemActivity;
import com.example.tabishhussain.hopeorbits.views.fragments.BaseFragment;

/**
 * Created by Tabish Hussain on 7/19/2017.
 */

public class AddPageFragment extends BaseFragment implements View.OnClickListener {

    private FragmentAddPageBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_page, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
    }

    private void bindView() {
        mBinding.addCategory.setOnClickListener(this);
        mBinding.addItem.setOnClickListener(this);
        mBinding.addCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_item:
                startActivity(new Intent(getActivity(), AddItemActivity.class));
                break;
            case R.id.add_category:
                startActivity(new Intent(getActivity(), AddCategoryActivity.class));
                break;
            case R.id.ok:
                break;
        }
    }
}
