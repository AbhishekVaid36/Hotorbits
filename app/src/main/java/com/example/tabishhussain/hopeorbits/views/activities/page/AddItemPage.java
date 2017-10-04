package com.example.tabishhussain.hopeorbits.views.activities.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.holder.CategoryModelss;

public class AddItemPage extends AppCompatActivity {

    Button itemsave;
    EditText catname;
    ImageView catimg;
    String base64 = "";
    CategoryModelss modelss;
    PageModelLists modelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_page);
        catimg = (ImageView)findViewById(R.id.Additemimage);

    }
}
