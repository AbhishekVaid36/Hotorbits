package com.example.tabishhussain.hopeorbits.buyer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;

public class BuyerVarification extends BaseActivity implements View.OnClickListener {
    TextView txtverify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_varification);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setUpToolbar("OTP Verification", false);
//        txtverify = (TextView) findViewById(R.id.txtverify);
//        txtverify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.txtverify:
//                startActivity(new Intent(BuyerVarification.this, MainActivity.class));
//                break;
        }
    }
}
