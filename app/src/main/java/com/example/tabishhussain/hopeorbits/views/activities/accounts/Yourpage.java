package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.api.HopeOrbitApi;
import com.example.tabishhussain.hopeorbits.buyer.Container;
import com.example.tabishhussain.hopeorbits.holder.INPageModelList;
import com.example.tabishhussain.hopeorbits.utils.common;
import com.example.tabishhussain.hopeorbits.views.activities.page.CategoryList;
import com.example.tabishhussain.hopeorbits.views.activities.page.HomeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Yourpage extends Fragment implements View.OnClickListener {
    RecyclerView recyclerpage;
    public static final String MyPREFERENCES = "MyPrefs";
    String Ids;
    Button createnew, remove;
    ArrayList<INPageModelList> modelLists = new ArrayList<>();
    private Inyourpagemodeadapter adapter;
    private ProgressBar yourpageerror;
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_yourpage, container, false);
        createnew = (Button) view.findViewById(R.id.createnew);
        remove = (Button) view.findViewById(R.id.remove);
        yourpageerror = (ProgressBar) view.findViewById(R.id.yourpageerror);

        remove.setOnClickListener(this);
        createnew.setOnClickListener(this);
        yourpageerror.setVisibility(View.VISIBLE);
        recyclerpage = (RecyclerView) view.findViewById(R.id.recyclerpage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerpage.setHasFixedSize(true);
        recyclerpage.setLayoutManager(layoutManager);

        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Ids = settings.getString("Id", "");
        getpagelist();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createnew:
                fragment = new HomeActivity();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
                break;
            case R.id.remove:
                myAlertDialog();
                break;
        }
    }

    private void myAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder
                .setMessage(" Are you sure you want to remove the selected page? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void getpagelist() {

        Call<JsonObject> call = HopeOrbitApi.retrofit.getMyThing(Ids);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccess()) {
                    try {
                        String respons = response.body().toString();
                        getpageModelList(respons);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Log.d("errro", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("myjsonobj", t.toString());
            }
        });
    }

    private void getpageModelList(String respons) {
        JSONObject object;
        JSONArray array;
        try {
            object = new JSONObject(respons);
            array = object.getJSONArray("pageModelList");
            INPageModelList list;
            Gson gson = new Gson();
            for (int i = 0; i < array.length(); i++) {
                list = gson.fromJson(array.get(i).toString(), INPageModelList.class);
                modelLists.add(list);
                yourpageerror.setVisibility(View.GONE);
            }

            adapter = new Inyourpagemodeadapter(getActivity(), modelLists);
            recyclerpage.setAdapter(adapter);
        } catch (Exception e) {
            yourpageerror.setVisibility(View.GONE);
        }
    }

    public class Inyourpagemodeadapter extends RecyclerView.Adapter<Inyourpagemodeadapter.ViewHolder> {
        Context context;
        Activity activity;
        ArrayList<INPageModelList> pageModelLists = new ArrayList<>();
        Fragment fragment;
        FragmentTransaction ft;

        public Inyourpagemodeadapter(Activity context, ArrayList<INPageModelList> arrayList) {
            this.context = context;
            this.activity = context;
            this.pageModelLists = arrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView productname;
            public CheckBox pagecheck;
            public ImageView imagelist;

            public ViewHolder(View view) {
                super(view);
                productname = (TextView) view.findViewById(R.id.pagetext);
                pagecheck = (CheckBox) view.findViewById(R.id.pagecheck);
                imagelist = (ImageView) view.findViewById(R.id.imagelist);
            }
        }

        @Override
        public Inyourpagemodeadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.youpageview, parent, false);
            return new Inyourpagemodeadapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Inyourpagemodeadapter.ViewHolder holder, final int position) {
            String s;
            holder.productname.setText(pageModelLists.get(position).getPageName());
            s = pageModelLists.get(position).getPageImage();
            if (s != null) {
                Log.d("errrorimage", s);
                String imgstr = pageModelLists.get(position).getPageImage();
                Log.d("errrorimage", imgstr.toString());
                byte[] decodedString = Base64.decode(imgstr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imagelist.setImageBitmap(decodedByte);
            }

            Log.e("arrrrrrray", pageModelLists.get(position).getCategoryModels().toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    common.setPreferenceString(context, "PAGEID", pageModelLists.get(position).getPageID());
                    Log.d("Pageidc", pageModelLists.get(position).getPageID());
//                if (pageModelLists.get(position).getCategoryModels().size() == 0) {
//                    Intent intent = new Intent(context, CategoryList.class);
//                    intent.putExtra("mylist", pageModelLists.get(position).getCategoryModels());
//                    activity.startActivity(intent);

                    Bundle b = new Bundle();
                    b.putSerializable("mylist", pageModelLists.get(position).getCategoryModels());
                    fragment = new CategoryList();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + Container.add);
                    ft.commit();
                    Container.add++;
//                }
                }
            });
        }

        @Override
        public int getItemCount() {
            return pageModelLists.size();
        }
    }
}
