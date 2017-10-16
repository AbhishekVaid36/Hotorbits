package co.hopeorbits.views.activities.page;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.hopeorbits.R;
import co.hopeorbits.buyer.Container;

/**
 * Created by abc on 9/28/2017.
 */

public class Addsubcategoryoritem extends Fragment implements View.OnClickListener {
    String subcatoritemId;
    TextView itemorcat;
    Button catoritenadditem, catoritenaddcat;
    LinearLayout catoriten;
    String checkithave = "false";
//    ArrayList<ItemModelSet> catiemegorylist = new ArrayList<>();
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.addsubcategoryoritem, container, false);
        itemorcat = (TextView) view.findViewById(R.id.itemorcat);
        catoritenadditem = (Button) view.findViewById(R.id.catoritenadditem);
        catoritenaddcat = (Button) view.findViewById(R.id.catoritenaddcat);
        catoriten = (LinearLayout) view.findViewById(R.id.catoriten);

        itemorcat.setOnClickListener(this);
        catoritenadditem.setOnClickListener(this);
        catoritenaddcat.setOnClickListener(this);
        catoriten.setOnClickListener(this);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            subcatoritemId = bundle.getString("subcatoritemId");
            Log.d("ccattegoryId", subcatoritemId);
        }
//        Toast.makeText(getActivity(), "helloitemmodel", Toast.LENGTH_SHORT).show();
        catoriten.setVisibility(View.GONE);

//        catiemegorylist = (ArrayList<ItemModelSet>) bundle.getSerializable("CategoryListItemModelsetsubitem");
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.itemorcat:
                changemethod();
                break;
            case R.id.catoritenaddcat:
//                Log.d("ccattegoryId", subitem);
//                Intent adsub = new Intent(getActivity(), AddSubcategory.class);
//                adsub.putExtra("subcatoritemcategory", subitem);
//                startActivity(adsub);
//                getActivity().finish();

                Bundle b = new Bundle();
                b.putString("subcatoritemcategory", subcatoritemId);
                fragment = new AddSubcategory();
                fragment.setArguments(b);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;
                break;
            case R.id.catoritenadditem:
//                Intent catitem = new Intent(getActivity(), AddCatItemModel.class);
//                catitem.putExtra("categoryid", subitem);
//                startActivity(catitem);
//                getActivity().finish();


                Bundle bundle = new Bundle();
                bundle.putString("categoryid", subcatoritemId);
                fragment = new AddCatItemModel();
                fragment.setArguments(bundle);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + Container.add);
                ft.commit();
                Container.add++;break;
        }
    }

    private void changemethod() {
        if (checkithave.equals("false")) {
            checkithave = "true";
            itemorcat.setText("Save");
            catoriten.setVisibility(View.VISIBLE);
        } else if (checkithave.equals("true")) {
            checkithave = "false";
            itemorcat.setText("Edit");
            catoriten.setVisibility(View.GONE);
        }
    }

}
