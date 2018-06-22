package softgalli.gurukulshikshalay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.HomeCategoryAdapter;
import softgalli.gurukulshikshalay.chatting.ChattingActivity;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.UpcomingActivityModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.rv_common)
    RecyclerView recyclerview;
    @BindView(R.id.upcomingActContainerLl)
    LinearLayout upcomingActContainerLl;
    @BindView(R.id.admissionRl)
    RelativeLayout admissionRl;
    @BindView(R.id.galleryRl)
    RelativeLayout galleryRl;
    @BindView(R.id.aboutUsRl)
    RelativeLayout aboutUsRl;
    @BindView(R.id.contactUsRl)
    RelativeLayout contactUsRl;
    private boolean isBackPressed;
    private Activity mActivity;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        initWidgit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting Profile
        manageNavHeaderView();

        //Upcoming Activity Handling
        upcomingActivityHandling();
        if (Utilz.isOnline(mActivity)) {
            //Showing update your app popup
            Utilz.genericAPI(mActivity);
        }
    }

    private void upcomingActivityHandling() {
        //TODO call api and get upcoming activities details from server and remove this hardcoded list.
        ArrayList<UpcomingActivityModel> upcomingActivityModelArrayList = new ArrayList<>();
        upcomingActivityModelArrayList.add(new UpcomingActivityModel("Swachh Bharat Swasth Bharat Abhiyan", "20 July 2018"));
        upcomingActivityModelArrayList.add(new UpcomingActivityModel("Independence Day(15 August) Celebration", "15 August 2018"));
        upcomingActivityModelArrayList.add(new UpcomingActivityModel("Rakshabandhan Celebration", "26 August 2018"));

        for (UpcomingActivityModel eachUpcmgActvty : upcomingActivityModelArrayList) {
            View child = getLayoutInflater().inflate(R.layout.upcoming_activity_row, null);
            upcomingActContainerLl.addView(child);
            TextView titleTv = child.findViewById(R.id.upcomingActTitleTv);
            titleTv.setText(eachUpcmgActvty.getUpcomingActivityTitle());
            TextView dateTimeTv = child.findViewById(R.id.upcomingActDateTv);
            dateTimeTv.setText(eachUpcmgActvty.getUpcomingActivityDate());
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /*if (id == R.id.about_us) {
            startActivity(new Intent(mActivity, AboutUsActivity.class));
        } else if (id == R.id.contact_us) {
            startActivity(new Intent(mActivity, ContactUsActivity.class));
        } else*/
        if (id == R.id.principal_zone) {
            if (MyPreference.isLogined()) {
                if (AppConstants.PRINCIPAL.equalsIgnoreCase(MyPreference.getLoginedAs()) ||
                        AppConstants.VICE_PRINCIPAL.equalsIgnoreCase(MyPreference.getLoginedAs()) ||
                        AppConstants.TEACHER.equalsIgnoreCase(MyPreference.getLoginedAs())) {
                    startActivity(new Intent(mActivity, PrincipalZoneActivity.class));
                } else {
                    Toast.makeText(mActivity, "Sorry, This feature is not for students.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Utilz.showLoginFirstDialog(mActivity);
            }
        } else if (id == R.id.feedback) {
            startActivity(new Intent(mActivity, FeedbackActivity.class));
        } else if (id == R.id.share) {
            String msg = "Download & install " + mActivity.getResources().getString(R.string.app_name) + " app.\nClick here to install" + mActivity.getResources().getString(R.string.app_name) + " : " + ApiUrl.PLAYSTORE_LINK;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/html");
            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
            startActivity(Intent.createChooser(shareIntent, "Share Using"));
        } else if (id == R.id.fb_like) {
            try {
                getPackageManager().getPackageInfo("com.facebook.katana", 0);
                Uri uri = Uri.parse(ApiUrl.FACEBOOK_LINK);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUrl.FACEBOOK_LINK)));
            }
        } else if (id == R.id.rate_app) {
            try {
                Uri uri = Uri.parse(ApiUrl.PLAYSTORE_LINK);
                Intent in = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(in);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.change_password) {
            startActivity(new Intent(mActivity, ChangePasswordActivity.class));
        } else if (id == R.id.logout) {
            Utilz.logout(mActivity);
        } else if (id == R.id.nav_website) {
            Utilz.openBrowser(mActivity, ApiUrl.MAIN_URL);
        } else if (id == R.id.nav_map) {
            Utilz.openBrowser(mActivity, mActivity.getResources().getString(R.string.school_on_map_url));
        } else if (id == R.id.softgalli) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mActivity.getResources().getString(R.string.softgalli_website_link)));
            mActivity.startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initWidgit() {
        String categoryList[];
        int iconList[], backgroundColor[];
        recyclerview.setLayoutManager(new GridLayoutManager(HomeScreenActivity.this, 3));
        categoryList = getResources().getStringArray(R.array.studenthomecategory);
        iconList = getStudentTeacherIcon();
        backgroundColor = getStudentTeacherColor();

        recyclerview.setAdapter(new HomeCategoryAdapter(HomeScreenActivity.this, categoryList, iconList, backgroundColor, R.layout.homecategory_row, new OnClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    startActivity(new Intent(HomeScreenActivity.this, TeacherListActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(HomeScreenActivity.this, TopperListActivity.class));
                } else if (position == 2) {
                    startActivity(new Intent(HomeScreenActivity.this, NotificationActivity.class));
                } else if (position == 3) {
                    if (MyPreference.isLogined()) {
                        if (AppConstants.STUDENT.equalsIgnoreCase(ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS))) {
                            Intent mIntent = new Intent(mActivity, SeeAttendenceActivity.class);
                            mIntent.putExtra(AppConstants.CLASS_NAME, ClsGeneral.getStrPreferences(AppConstants.CLAS));
                            mIntent.putExtra(AppConstants.SECTION_NAME, ClsGeneral.getStrPreferences(AppConstants.SEC));
                            startActivity(mIntent);
                        } else {
                            Utilz.showAttendanceMgmtDialog(mActivity);
                        }
                    } else {
                        Utilz.showLoginFirstDialog(mActivity);
                    }
                } else if (position == 4) {
                    if (MyPreference.isLogined()) {
                        if (AppConstants.STUDENT.equalsIgnoreCase(ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS))) {
                            Utilz.showLeaveMgmtDialog(mActivity);
                        } else {
                            startActivity(new Intent(HomeScreenActivity.this, SeeLeaveListActivity.class));
                        }
                    } else {
                        Utilz.showLoginFirstDialog(mActivity);
                    }
                } else if (position == 5) {
                    if (MyPreference.isLogined()) {
                        startActivity(new Intent(HomeScreenActivity.this, TimeTableActivity.class));
                    } else {
                        Utilz.showLoginFirstDialog(mActivity);
                    }
                } else if (position == 6) {
                    if (MyPreference.isLogined()) {
                        //startActivity(new Intent(HomeScreenActivity.this, TimeTableActivity.class));
                        Toast.makeText(mActivity, "Sorry no data is available at this time", Toast.LENGTH_SHORT).show();
                    } else {
                        Utilz.showLoginFirstDialog(mActivity);
                    }
                } else if (position == 7) {
                    if (MyPreference.isLogined()) {
                        startActivity(new Intent(HomeScreenActivity.this, SyllabusActivity.class));
                    } else {
                        Utilz.showLoginFirstDialog(mActivity);
                    }
                } else if (position == 8) {
                    if (MyPreference.isLogined()) {
                        startActivity(new Intent(HomeScreenActivity.this, ChattingActivity.class));
                    } else {
                        Utilz.showLoginFirstDialog(mActivity);
                    }
                } else {
//                    Utilz.showLoginFirstDialog(mActivity);
                    Toast.makeText(mActivity, "Coming Soon!!", Toast.LENGTH_SHORT).show();
                }
            }
        }));

    }

    private int[] getStudentTeacherColor() {
        int color[] = {R.drawable.grid_bg1, R.drawable.grid_bg2, R.drawable.grid_bg3,
                R.drawable.grid_bg4, R.drawable.grid_bg5, R.drawable.grid_bg6,
                R.drawable.grid_bg7, R.drawable.grid_bg8, R.drawable.grid_bg9};
        return color;
    }

    private int[] getStudentTeacherIcon() {
        int icon[] = {R.drawable.teacherlist, R.drawable.topperlist, R.drawable.news,
                R.drawable.attendance, R.drawable.leave, R.drawable.timetable,
                R.drawable.result, R.drawable.syllabus, R.drawable.quiz};
        return icon;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            isBackPressed = false;
        } else if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 3000);
        } else {
            super.onBackPressed();
        }
    }

    private TextView userName;
    private CircleImageView imageViewProfilePic;
    NavigationView navigationView;
    AQuery aq;

    private void manageNavHeaderView() {
        aq = new AQuery(mActivity);
        try {
            aq.hardwareAccelerated11();
        } catch (Exception e) {
            e.printStackTrace();
        }

        View navHeaderView = getLayoutInflater().inflate(R.layout.nav_header_main, navigationView, false);

        int a = Build.VERSION.SDK_INT;
        if (a > 20) {
            (navHeaderView.findViewById(R.id.spaceHeader)).setVisibility(View.VISIBLE);
        } else {
            (navHeaderView.findViewById(R.id.spaceHeader)).setVisibility(View.GONE);
        }
        View headerview = navigationView.getHeaderView(0);
        imageViewProfilePic = headerview.findViewById(R.id.imageViewProfilePic1);
        userName = headerview.findViewById(R.id.userName);
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (MyPreference.isLogined()) {
                    startActivity(new Intent(mActivity, ViewTeacherProfileActivity.class));
                } else {
                    Utilz.showLoginFirstDialog(mActivity);
                }
            }
        });

        setNameAndPic();


        Menu menu = navigationView.getMenu();

        MenuItem logoutMenuItem = menu.findItem(R.id.logout);

        if (MyPreference.isLogined())
            logoutMenuItem.setVisible(true);
        else
            logoutMenuItem.setVisible(false);

        MenuItem changePassMenuItem = menu.findItem(R.id.change_password);

        if (MyPreference.isLogined())
            changePassMenuItem.setVisible(true);
        else
            changePassMenuItem.setVisible(false);
    }

    private void setNameAndPic() {
        String userNameStr = "User";
        if (!TextUtils.isEmpty(MyPreference.getUserName())) {
            userNameStr = MyPreference.getUserName();
        } else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.NAME))) {
            userNameStr = ClsGeneral.getStrPreferences(AppConstants.NAME);
        }
        userName.setText(userNameStr);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user);
        requestOptions.error(R.drawable.user);
        requestOptions.fitCenter();
        String imageUrl = ApiUrl.IMAGE_BASE_URL + ClsGeneral.getStrPreferences(AppConstants.PROFILE_PIC);
        Glide.with(mActivity)
                .load(imageUrl)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageViewProfilePic);
    }

    @OnClick({R.id.admissionRl, R.id.galleryRl, R.id.aboutUsRl, R.id.contactUsRl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.admissionRl:
                startActivity(new Intent(HomeScreenActivity.this, AdmissionActivity.class));
                break;
            case R.id.galleryRl:
                startActivity(new Intent(HomeScreenActivity.this, GalleryList.class));
                break;
            case R.id.aboutUsRl:
                startActivity(new Intent(HomeScreenActivity.this, AboutUsActivity.class));
                break;
            case R.id.contactUsRl:
                startActivity(new Intent(HomeScreenActivity.this, ContactUsActivity.class));
                break;
        }
    }
}
