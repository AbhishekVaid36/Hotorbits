package com.example.tabishhussain.hopeorbits.buyer;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.activities.BaseActivity;

public class BuyerLogin extends BaseActivity implements View.OnClickListener{
RelativeLayout rllogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_login);
        setUpToolbar("Login", false);
        rllogin=(RelativeLayout)findViewById(R.id.rllogin);
        rllogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rllogin:

                break;
        }
    }
}
