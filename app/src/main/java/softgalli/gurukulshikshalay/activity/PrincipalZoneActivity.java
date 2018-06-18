package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.fragments.StudentsFragment;

public class PrincipalZoneActivity extends AppCompatActivity {
    private Activity mActivity;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_zone);
        mActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), mActivity);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addStudentFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(mActivity, AddStudent.class);
                startActivity(intent);
            }
        });
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{"Class - 10", "Class - 9", "Class - 8", "Class - 7", "Class - 6", "Class - 5",
                "Class - 4", "Class - 3", "Class - 2", "Class - 1", "Class - UKG", "Class - LKG", "Class - NURSERY"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new StudentsFragment(tabTitles[position], "10");
                case 1:
                    return new StudentsFragment(tabTitles[position], "9");
                case 2:
                    return new StudentsFragment(tabTitles[position], "8");
                case 3:
                    return new StudentsFragment(tabTitles[position], "7");
                case 4:
                    return new StudentsFragment(tabTitles[position], "6");
                case 5:
                    return new StudentsFragment(tabTitles[position], "5");
                case 6:
                    return new StudentsFragment(tabTitles[position], "4");
                case 7:
                    return new StudentsFragment(tabTitles[position], "3");
                case 8:
                    return new StudentsFragment(tabTitles[position], "2");
                case 9:
                    return new StudentsFragment(tabTitles[position], "1");
                case 10:
                    return new StudentsFragment(tabTitles[position], "UKG");
                case 11:
                    return new StudentsFragment(tabTitles[position], "LKG");
                case 12:
                    return new StudentsFragment(tabTitles[position], "NURSERY");
                default:
                    return new StudentsFragment(tabTitles[position], "10");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.tabTitleTv);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.principal_zone);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}