package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;
import com.example.tabishhussain.hopeorbits.views.fragments.accounts.VerificationFragment;

/**
 * Created by Tabish Hussain on 7/10/2017.
 */

public class VerificationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new VerificationFragment()).commit();
        setUpToolbar("Phone Verification", true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}