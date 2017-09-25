package com.example.tabishhussain.hopeorbits.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;

public class BuyerRegister extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_register);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setUpToolbar("Register", false);
        rlregister = (RelativeLayout) findViewById(R.id.rlregister);
        rlregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlregister:
                startActivity(new Intent(BuyerRegister.this, BuyerVarification.class));
                break;
        }
    }
}
