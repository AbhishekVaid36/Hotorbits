package com.example.tabishhussain.hopeorbits.views.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tabishhussain.hopeorbits.R;
import com.example.tabishhussain.hopeorbits.views.fragments.BaseFragment;
import com.example.tabishhussain.hopeorbits.views.fragments.PageListFragment;
import com.example.tabishhussain.hopeorbits.views.fragments.credit_management.TransferCreditFragment;
import com.example.tabishhussain.hopeorbits.views.fragments.dashboard.MapViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setUpToolbar("Home", false);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PageListFragment(), "List");
        adapter.addFragment(new MapViewFragment(), "Map View");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.transfer_credit:
                TransferCreditFragment transferCreditFragment
                        = TransferCreditFragment.newInstance(4);
                transferCreditFragment.show(getSupportFragmentManager(), "Transfer Credit Fragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private List<BaseFragment> fragments = new ArrayList<>();
        private List<String> fragmentsNames = new ArrayList<>();

        MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        void addFragment(BaseFragment fragment, String name) {
            fragments.add(fragment);
            fragmentsNames.add(name);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsNames.get(position);
        }
    }

}
