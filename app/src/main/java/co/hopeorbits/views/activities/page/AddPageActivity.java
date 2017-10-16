package co.hopeorbits.views.activities.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import co.hopeorbits.R;
import co.hopeorbits.views.activities.BaseActivity;
import co.hopeorbits.views.fragments.dashboard.AddPageFragment;

/**
 * Created by Tabish Hussain on 7/19/2017.
 */

public class AddPageActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setUpToolbar("Add Page", true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new AddPageFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
