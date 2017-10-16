package co.hopeorbits.views.fragments.credit_management;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.hopeorbits.R;

/**
 * Created by Tabish Hussain on 8/3/2017.
 */

public class TransferCreditFragment extends DialogFragment {

    int mNum;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static TransferCreditFragment newInstance(int num) {
        TransferCreditFragment f = new TransferCreditFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_transfer_credit, container, false);
        return v;
    }
}
