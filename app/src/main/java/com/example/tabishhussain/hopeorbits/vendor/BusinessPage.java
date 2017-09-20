package com.example.tabishhussain.hopeorbits.vendor;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;

public class BusinessPage extends AppCompatActivity {
    String[] spvalue1;
    Spinner spiner1;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buessiness_page);
        spvalue1 = new String[]{"Currency","INR India Rupee", "AED	United Arab Emirates Dirham", "AFN	Afghanistan Afghani", "ALL	Albania Lek", "AMD	Armenia Dram" +
                "ANG	Netherlands Antilles Guilder", "AOA	Angola Kwanza", "ARS	Argentina Peso", "AUD	Australia Dollar", "AWG	Aruba Guilder" +
                "AZN	Azerbaijan Manat", "BAM	Bosnia and Herzegovina Convertible Marka", "BBD	Barbados Dollar", "BDT	Bangladesh Taka", "BGN	Bulgaria Lev"};

        spiner1 = (Spinner) findViewById(R.id.simpleSpinner1);
        spiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BusinessPage.this, spvalue1[i], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        customAdapter = new CustomAdapter(BusinessPage.this, spvalue1);
        spiner1.setAdapter(customAdapter);
    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        String[] countryNames;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, String[] countryNames) {
            this.context = applicationContext;
            this.countryNames = countryNames;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return countryNames.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.custom_spinner_items, null);
            TextView names = (TextView) view.findViewById(R.id.textView);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
            names.setText(countryNames[i]);
            if (i == 0) {
                rl.setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                rl.setBackgroundColor(getResources().getColor(R.color.spinnerbackground));
            }
            return view;
        }
    }

}
