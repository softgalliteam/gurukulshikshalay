package softgalli.gurukulshikshalay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class PrincipalZoneActivity extends AppCompatActivity {
    private static final String TAG = PrincipalZoneActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.manageStudentRl)
    RelativeLayout manageStudentRl;
    @BindView(R.id.manageTeacherRl)
    RelativeLayout manageTeacherRl;
    @BindView(R.id.addEventsRl)
    RelativeLayout addEventsRl;
    @BindView(R.id.manageBusRl)
    RelativeLayout manageBusRl;
    @BindView(R.id.userProfilePicIv)
    CircleImageView userProfilePicIv;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.noticeTitleEt)
    EditText noticeTitleEt;
    @BindView(R.id.noticeDescEt)
    EditText noticeDescEt;
    @BindView(R.id.publishNoticeBtn)
    Button publishNoticeBtn;
    @BindView(R.id.publishNoticeLl)
    LinearLayout publishNoticeLl;
    @BindView(R.id.eventTitleEt)
    EditText eventTitleEt;
    @BindView(R.id.eventDescEt)
    EditText eventDescEt;
    @BindView(R.id.addEventBtn)
    Button addEventBtn;
    @BindView(R.id.addEventsLl)
    LinearLayout addEventsLl;
    @BindView(R.id.sendMessageLl)
    LinearLayout sendMessageLl;
    @BindView(R.id.publishNoticeTab)
    Button publishNoticeTab;
    @BindView(R.id.addEventTab)
    Button addEventTab;
    private Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_zone_activity);
        ButterKnife.bind(this);
        mActivity = this;
        retrofitDataProvider = new RetrofitDataProvider(this);
        initToolbar();
        updateOnUI();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updateOnUI() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);
        requestOptions.fitCenter();
        Glide.with(mActivity)
                .load(ApiUrl.IMAGE_BASE_URL + ClsGeneral.getStrPreferences(AppConstants.PROFILE_PIC))
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(userProfilePicIv);
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.NAME))) {
            userNameTv.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        } else {
            userNameTv.setText(mActivity.getResources().getString(R.string.na));
        }
    }

    @OnClick({R.id.manageTeacherRl, R.id.addEventsRl, R.id.manageStudentRl, R.id.manageBusRl, R.id.publishNoticeBtn,
            R.id.addEventBtn, R.id.sendMessageLl, R.id.publishNoticeTab, R.id.addEventTab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.manageTeacherRl:
                startActivity(new Intent(mActivity, TeacherListActivity.class));
                break;
            case R.id.addEventsRl:
                Toast.makeText(mActivity, "Coming Soon...", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(mActivity, AdmissionActivity.class));
                break;
            case R.id.manageBusRl:
                Toast.makeText(mActivity, "Coming Soon...", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(mActivity, AdmissionActivity.class));
                break;
            case R.id.manageStudentRl:
                startActivity(new Intent(mActivity, ManageStudentActivity.class));
                break;
            case R.id.publishNoticeBtn:
                publishNotice();
                break;
            case R.id.addEventBtn:
                addEvent();
                break;
            case R.id.sendMessageLl:
                Toast.makeText(mActivity, "Coming Soon...", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(mActivity, AdmissionActivity.class));
                break;
            case R.id.publishNoticeTab:
                publishNoticeTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked_circle, 0);
                addEventTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unchecked_circled, 0);
                publishNoticeLl.setVisibility(View.VISIBLE);
                addEventsLl.setVisibility(View.GONE);
                break;
            case R.id.addEventTab:
                publishNoticeTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unchecked_circled, 0);
                addEventTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked_circle, 0);
                publishNoticeLl.setVisibility(View.GONE);
                addEventsLl.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void publishNotice() {
        String message = noticeDescEt.getText().toString();
        String title = noticeTitleEt.getText().toString();
        String date = Utilz.getCurrentDate();
        String posted_by = ClsGeneral.getStrPreferences(AppConstants.DESIGNATION) + "\n(" + ClsGeneral.getStrPreferences(AppConstants.NAME) + ")";
        Log.i(TAG, "**" + title + "**" + message + "**" + date + "**" + posted_by + "**");
        if (Utilz.isOnline(mActivity)) {
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.please_enter_title), Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(message)) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.please_enter_message), Toast.LENGTH_LONG).show();
            } else {
                publishNoticOnNoticeBoard(title, message, date, posted_by, "1", false);
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void publishNoticOnNoticeBoard(String title, String message, String date, String posted_by, String status, boolean isToAddEvent) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        DownlodableCallback downlodableCallback = new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
                Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.sent_successfully), "", AppConstants.OK);
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
                Toast.makeText(mActivity, R.string.something_went_wrong_error_message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
                Toast.makeText(mActivity, R.string.something_went_wrong_error_message, Toast.LENGTH_LONG).show();
            }
        };
        if (isToAddEvent)
            retrofitDataProvider.addEvent(title, message, date, posted_by, status, downlodableCallback);
        else
            retrofitDataProvider.publishNotice(title, message, date, posted_by, status, downlodableCallback);
    }

    private void addEvent() {
        String message = eventDescEt.getText().toString();
        String title = eventTitleEt.getText().toString();
        String date = Utilz.getCurrentDate();
        String posted_by = ClsGeneral.getStrPreferences(AppConstants.DESIGNATION) + "\n(" + ClsGeneral.getStrPreferences(AppConstants.NAME) + ")";
        Log.i(TAG, "**" + title + "**" + message + "**" + date + "**" + posted_by + "**");
        if (Utilz.isOnline(mActivity)) {
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.please_enter_title), Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(message)) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.please_enter_message), Toast.LENGTH_LONG).show();
            } else {
                publishNoticOnNoticeBoard(title, message, date, posted_by, "1", true);
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }
}