package co.hopeorbits.buyer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import co.hopeorbits.R;
import co.hopeorbits.api.HopeOrbitApi;
import co.hopeorbits.connectiondetector.ConnectionDetector;
import co.hopeorbits.holder.StoreListHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    TextView txtemptylist;
    String pageModelList, pageID, pageName, currency, details, pageImage, errorMessage, categoryModels;
    String categoryID, categoryName, error, categoryImage, itemModelSet;
    ArrayList<StoreListHolder> list = new ArrayList<StoreListHolder>();
    ListView storelistview;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog2;
    //    public static String CategoryListSet;
    public static JSONArray CategoryListSet;
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.buyer_home, container, false);

        initpDialog();
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        storelistview = (ListView) view.findViewById(R.id.storelistview);

        getStores();
        return view;
    }

    public void getStores() {
        showpDialog();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Call<JsonObject> call = HopeOrbitApi.retrofit.getAllPages();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());
                        if ((jsonObj.has("pageModelList")) && (!jsonObj.getString("pageModelList").equals("null"))) {
                            pageModelList = jsonObj.getString("pageModelList");
                            JSONArray jsonArray = new JSONArray(pageModelList);
                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject ob = jsonArray.getJSONObject(i);
                                pageID = ob.getString("pageID");
                                pageName = ob.getString("pageName");
                                currency = ob.getString("currency");
                                details = ob.getString("details");
                                pageImage = ob.getString("pageImage");
                                errorMessage = ob.getString("errorMessage");
                                categoryModels = ob.getString("categoryModels");
                                StoreListHolder h = new StoreListHolder();
                                h.setPageID(pageID);
                                h.setPageName(pageName);
                                h.setCurrency(currency);
                                h.setDetails(details);
                                h.setPageImage("http://13.58.110.101:8080" + pageImage);
                                h.setErrorMessage(errorMessage);
                                h.setCategoryModels(categoryModels);
                                list.add(h);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (list.size() > 0) {
                        storelistview.setAdapter(new MyCustomAdapter(getActivity(), list));
                        txtemptylist.setVisibility(View.GONE);
                    } else {
                        txtemptylist.setVisibility(View.VISIBLE);
                    }

                } else {
                    try {
                        Log.d("Tag", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Tag", t.toString());
                hidepDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<StoreListHolder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<StoreListHolder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }

        class ViewHolder {
            TextView txtstorename, txtsortdesc;
            ImageView imgstore;
            RelativeLayout rlstore;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.store_list_items, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txtstorename = (TextView) paramView.findViewById(R.id.txtstorename);
                holder.txtsortdesc = (TextView) paramView.findViewById(R.id.txtsortdesc);
                holder.imgstore = (ImageView) paramView.findViewById(R.id.imgstore);
                holder.rlstore = (RelativeLayout) paramView.findViewById(R.id.rlstore);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            StoreListHolder h = list.get(paramInt);
            String name = h.getPageName();
            holder.txtstorename.setText(name);
            final String ImagePath = h.getPageImage();
            if ((!ImagePath.equalsIgnoreCase("null")) && (!ImagePath.isEmpty())) {
                /*Picasso
                        .with(getActivity())
                        .load(ImagePath)
                        .placeholder(R.mipmap.uploadimage) // can also be a drawable
                        .into(holder.imgstore, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                // once the image is loaded, load the next image
                                Picasso
                                        .with(getActivity())
                                        .load(ImagePath)
//                                        .noPlaceholder() // but don't clear the imageview or set a placeholder; just leave the previous image in until the new one is ready
                                        .into(holder.imgstore);
                            }

                            @Override
                            public void onError() {

                            }
                        });*/
                Picasso.with(getActivity())
                        .load(ImagePath)
                        .placeholder(R.mipmap.page)   // optional
                        .error(R.mipmap.page)      // optional
                        .resize(100, 100)                        // optional
                        .into(holder.imgstore);
            } else {
                holder.imgstore.setBackgroundResource(R.mipmap.product);
            }

            holder.rlstore.setTag(paramInt);
            holder.rlstore.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) vv.getTag();
                        StoreListHolder h1 = (StoreListHolder) list.get(pos1);
                        try {
                            CategoryListSet = new JSONArray(h1.getCategoryModels());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("pageID", h1.getPageID());
                        bundle.putString("pageName", h1.getPageName());
                        fragment = new Categories();
                        fragment.setArguments(bundle);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + Container.add);
                        ft.commit();
                        Container.add++;


//                        Intent in = new Intent(Home.this, Categories.class);
//                        in.putExtra("categoryModels", categoryModels);
//                        in.putExtra("pageID", h1.getPageID());
//                        in.putExtra("pageName", h1.getPageName());
//                        startActivity(in);
                    }
                }
            });
            return paramView;
        }
    }

    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Double screenInches = Math.sqrt(x + y);

        Integer inch = screenInches.intValue();
        if (inch >= 5) {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        } else {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
    }
}