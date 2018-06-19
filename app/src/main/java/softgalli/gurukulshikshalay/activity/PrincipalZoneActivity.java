package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
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
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.fragments.StudentsFragment;

public class PrincipalZoneActivity extends AppCompatActivity {
    private static final String TAG = PrincipalZoneActivity.class.getSimpleName();
    private Activity mActivity;
    private Toolbar toolbar;
    private ViewPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_zone);
        mActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(0);
        //Setting viewpager
        setupViewPager(mViewPager);
        //Setting tabs
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addStudentFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent studentIntent = new Intent(mActivity, AddStudent.class);
                studentIntent.putExtra(AppConstants.IS_FOR_UPDATE, false);
                startActivity(studentIntent);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                StudentsFragment mCurrentFragment = (StudentsFragment) pagerAdapter.getItem(position);
                if (mCurrentFragment != null && mCurrentFragment instanceof StudentsFragment) {
                  /*  if (position == 0)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "10");
                    else*/
                    if (position == 1)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "9");
                    else if (position == 2)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "8");
                    else if (position == 3)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "7");
                    else if (position == 4)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "6");
                    else if (position == 5)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "5");
                    else if (position == 6)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "4");
                    else if (position == 7)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "3");
                    else if (position == 8)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "2");
                    else if (position == 9)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "1");
                    else if (position == 10)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "UKG");
                    else if (position == 11)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "LKG");
                    else if (position == 12)
                        mCurrentFragment.callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(position)), "NURSERY");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 10");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 9");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 8");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 7");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 6");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 5");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 4");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 3");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 2");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - 1");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - UKG");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - LKG");
        pagerAdapter.addFrag(new StudentsFragment(), "Class - NURSERY");
        viewPager.setAdapter(pagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.manage_students);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager != null) {
            mViewPager.setCurrentItem(0);
        }
        if (pagerAdapter != null) {
            //For first fragment(class-10) calling api
            ((StudentsFragment) pagerAdapter.getItem(0)).callStudentListFromServer(String.valueOf(pagerAdapter.getPageTitle(0)), "10");
        }
    }
}