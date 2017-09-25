package com.example.tabishhussain.hopeorbits.buyer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;

public class Categories extends AppCompatActivity {
    String[] title;
    int[] icon;
    public static MenuListAdapter mMenuAdapter;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        title = new String[]{"Category", "Category1", "Category2", "Category3", "Category4", "Category5", "Category6", "Category7", "Category8"};
        icon = new int[]{R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo, R.mipmap.finallogo};
        grid = (GridView) findViewById(R.id.productlist);
        mMenuAdapter = new MenuListAdapter(Categories.this, title, icon);
        grid.setAdapter(mMenuAdapter);
    }

    public class MenuListAdapter extends BaseAdapter {

        // Declare Variables
        Context context;
        String[] mTitle;
        //    String[] mSubTitle;
        int[] mIcon;
        LayoutInflater inflater;
//        View itemView;

        public MenuListAdapter(Context context, String[] title, int[] icon) {
            this.context = context;
            this.mTitle = title;
//        this.mSubTitle = subtitle;
            this.mIcon = icon;
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
        }

        class ViewHolder {
            TextView txtname;
            ImageView img;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Declare Variables
            ViewHolder holder;
            if (convertView == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                itemView = inflater.inflate(R.layout.drawer_list_item, parent,
//                        false);

                convertView = inflater.inflate(R.layout.categoriesitems, parent, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Set the results into TextViews
            holder.txtname.setText(mTitle[position]);

            holder.img.setTag(position);
            holder.img.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                }
            });
            return convertView;
        }
    }

}
