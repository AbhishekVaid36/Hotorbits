package co.hopeorbits.views.activities.accounts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import co.hopeorbits.buyer.Container;
import co.hopeorbits.buyer.JSONParser;
import co.hopeorbits.connectiondetector.ConnectionDetector;
import co.hopeorbits.holder.INPageModelList;
import co.hopeorbits.holder.UserPageHolder;
import co.hopeorbits.utils.common;
import co.hopeorbits.views.activities.page.CategoryList;
import co.hopeorbits.views.activities.page.HomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Yourpage extends Fragment implements View.OnClickListener {
    ListView recyclerpage;
    public static final String MyPREFERENCES = "MyPrefs";
    String Ids, message;
    Button createnew, remove;
    ArrayList<INPageModelList> modelLists = new ArrayList<>();
    //    private Inyourpagemodeadapter adapter;
    Fragment fragment;
    FragmentTransaction ft;
    ArrayList<String> selectedPageId = new ArrayList<String>();
    TextView textheader;
    android.app.AlertDialog.Builder alertDialog;
    android.app.AlertDialog mDialog2;
    String pageModelList, pageID, pageName, pageImage, categoryModels;
    ArrayList<UserPageHolder> list = new ArrayList<UserPageHolder>();
    TextView txtemptylist;
    public static JSONArray CategoryListSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_yourpage, container, false);
        createnew = (Button) view.findViewById(R.id.createnew);
        remove = (Button) view.findViewById(R.id.remove);
        textheader = (TextView) view.findViewById(R.id.textheader);
        txtemptylist = (TextView) view.findViewById(R.id.txtemptylist);
        remove.setOnClickListener(this);
        createnew.setOnClickListener(this);
        textheader.setOnClickListener(this);
        recyclerpage = (ListView) view.findViewById(R.id.recyclerpage);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerpage.setHasFixedSize(true);
//        recyclerpage.setLayoutManager(layoutManager);
        initpDialog();
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
            case R.id.textheader:
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
                        new DeletePageProgress().execute();
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
        showpDialog();
        Call<JsonObject> call = HopeOrbitApi.retrofit.getMyThing(Ids);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                /*if (response.isSuccess()) {
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
                }*/
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
//                                currency = ob.getString("currency");
//                                details = ob.getString("details");
                                pageImage = ob.getString("pageImage");
//                                errorMessage = ob.getString("errorMessage");
                                categoryModels = ob.getString("categoryModels");
                                UserPageHolder h = new UserPageHolder();
                                h.setPageID(pageID);
                                h.setPageName(pageName);
//                                h.setCurrency(currency);
//                                h.setDetails(details);
                                h.setPageImage("http://13.58.110.101:8080" + pageImage);
//                                h.setErrorMessage(errorMessage);
                                h.setCategoryModels(categoryModels);
                                list.add(h);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (list.size() > 0) {
                        recyclerpage.setAdapter(new MyCustomAdapter(getActivity(), list));
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
                Log.d("myjsonobj", t.toString());
                hidepDialog();
            }
        });
    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<UserPageHolder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<UserPageHolder> list) {
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
            public TextView productname;
            public CheckBox pagecheck;
            public ImageView imagelist;
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
                paramView = inflater.inflate(R.layout.youpageview, paramViewGroup, false);
                holder = new ViewHolder();

                holder.productname = (TextView) paramView.findViewById(R.id.pagetext);
                holder.pagecheck = (CheckBox) paramView.findViewById(R.id.pagecheck);
                holder.imagelist = (ImageView) paramView.findViewById(R.id.imagelist);
                holder.rlstore = (RelativeLayout) paramView.findViewById(R.id.rlstore);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            UserPageHolder h = list.get(paramInt);
            String s;
            holder.productname.setText(h.getPageName());
            s = h.getPageImage();
            if (s != null) {
                Log.d("errrorimage", s);
                final String ImagePath = h.getPageImage();
                if (!ImagePath.isEmpty()) {

                    /*Picasso
                            .with(getActivity())
                            .load(ImagePath)
                            .placeholder(R.mipmap.uploadimage) // can also be a drawable
                            .into(holder.imagelist, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    // once the image is loaded, load the next image
                                    Picasso
                                            .with(getActivity())
                                            .load(ImagePath)
                                            //.noPlaceholder() // but don't clear the imageview or set a placeholder; just leave the previous image in until the new one is ready
                                            .into(holder.imagelist);
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
                            .into(holder.imagelist);
                } else {
                    holder.imagelist.setBackgroundResource(R.mipmap.product);
                }
            }
            holder.rlstore.setTag(paramInt);
            holder.rlstore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) view.getTag();
                        UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                        try {
                            CategoryListSet = new JSONArray(h1.getCategoryModels());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        common.setPreferenceString(getActivity(), "PAGEID", h1.getPageID());
//                if (pageModelLists.get(position).getCategoryModels().size() == 0) {
//                    Intent intent = new Intent(context, CategoryList.class);
//                    intent.putExtra("mylist", pageModelLists.get(position).getCategoryModels());
//                    activity.startActivity(intent);

//                    b.putSerializable("mylist", h.getCategoryModels());
//                    b.putString("pageId",h.getPageID());
                        Bundle bundle = new Bundle();
                        bundle.putString("pageID", h1.getPageID());
                        bundle.putString("pageName", h1.getPageName());
                        fragment = new CategoryList();
                        fragment.setArguments(bundle);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + Container.add);
                        ft.commit();
                        Container.add++;
                    }
                }
            });
            holder.pagecheck.setTag(paramInt);
            holder.pagecheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    UserPageHolder h1 = (UserPageHolder) list.get(pos1);
                    if (holder.pagecheck.isChecked()) {
                        selectedPageId.add(h1.getPageID());
                    } else {
                        selectedPageId.remove(h1.getPageID());
                    }
                }
            });
            return paramView;
        }
    }

    /*private void getpageModelList(String respons) {
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
    }*/

    /*public class Inyourpagemodeadapter extends RecyclerView.Adapter<Inyourpagemodeadapter.ViewHolder> {
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
            public CircularImageView imagelist;

            public ViewHolder(View view) {
                super(view);
                productname = (TextView) view.findViewById(R.id.pagetext);
                pagecheck = (CheckBox) view.findViewById(R.id.pagecheck);
                imagelist = (CircularImageView) view.findViewById(R.id.imagelist);
            }
        }

        @Override
        public Inyourpagemodeadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.youpageview, parent, false);
            return new Inyourpagemodeadapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Inyourpagemodeadapter.ViewHolder holder, final int position) {
            String s;
            holder.productname.setText(pageModelLists.get(position).getPageName());
            s = pageModelLists.get(position).getPageImage();
            if (s != null) {
                Log.d("errrorimage", s);
                String imgstr = pageModelLists.get(position).getPageImage();
                final String ImagePath ="http://13.58.110.101:8080"+imgstr;
                if(!imgstr.isEmpty()) {
                    *//*Picasso.with(getActivity())
                            .load("http://13.58.110.101:8080"+imgstr)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                                holder.img.setImageBitmap(bitmap);
//                                    holder.imagelist.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                                    holder.imagelist.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });*//*

                    Picasso
                            .with(getActivity())
                            .load(ImagePath)
                            .placeholder(R.mipmap.uploadimage) // can also be a drawable
                            .into(holder.imagelist, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    // once the image is loaded, load the next image
                                    Picasso
                                            .with(getActivity())
                                            .load(ImagePath)
                                            .noPlaceholder() // but don't clear the imageview or set a placeholder; just leave the previous image in until the new one is ready
                                            .into(holder.imagelist);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
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
                    b.putString("pageId",pageModelLists.get(position).getPageID());
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
            holder.pagecheck.setTag(position);
            holder.pagecheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    if (holder.pagecheck.isChecked()) {
                        selectedPageId.add(pageModelLists.get(pos1).getPageID());
                    } else {
                        selectedPageId.remove(pageModelLists.get(pos1).getPageID());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return pageModelLists.size();
        }
    }*/

    public class DeletePageProgress extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONArray ja_inner = null;
            try {

                JSONObject jo_item = null;
                ja_inner = new JSONArray();
                for (int i = 0; i < selectedPageId.size(); i++) {
                    jo_item = new JSONObject();
                    jo_item.put("pageID", selectedPageId.get(i));
                    ja_inner.put(jo_item);
                }
                JSONObject j = new JSONObject();
                j.put("userId", Ids);
                j.put("pageModelList", ja_inner);

                JSONParser jParser = new JSONParser();
                // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
                JSONObject json = jParser.getJSONFromUrlPost("http://13.58.110.101:8080/hoprepositoryweb/user/deletePage", j.toString());

                try {

                    message = json.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            selectedPageId.clear();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
            fragment = new Yourpage();
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame, fragment);
            ft.addToBackStack("add" + Container.add);
            ft.commit();
            Container.add++;

        }

    }

    protected void initpDialog() {
        alertDialog = new android.app.AlertDialog.Builder(getActivity());
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
