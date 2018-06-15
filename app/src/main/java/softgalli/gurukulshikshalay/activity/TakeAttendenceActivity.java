package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import softgalli.gurukulshikshalay.adapter.AttendenceAdapter;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.AttendanceStatusModel;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.InsertAttendanceModel;
import softgalli.gurukulshikshalay.model.StudentListByClassModel;
import softgalli.gurukulshikshalay.model.StudentListDataModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class TakeAttendenceActivity extends AppCompatActivity {
    private static final String TAG = TakeAttendenceActivity.class.getSimpleName();
    @BindView(R.id.totalStudentCount)
    TextView totalStudentCount;
    @BindView(R.id.presentStudentCount)
    TextView presentStudentCount;
    @BindView(R.id.absentStudentCount)
    TextView absentStudentCount;
    @BindView(R.id.uploadAttendenceBtn)
    TextView uploadAttendenceBtn;
    @BindView(R.id.seeAttendenceBtn)
    TextView seeAttendenceBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_common)
    RecyclerView mRecyclerView;
    @BindView(R.id.userProfilePicIv)
    CircleImageView userProfilePicIv;
    @BindView(R.id.classTeacherNameTv)
    TextView classTeacherNameTv;
    @BindView(R.id.totalAbsentPresentCardView)
    LinearLayout totalAbsentPresentCardView;
    private Activity mActivity;
    private ArrayList<StudentListDataModel> studentListDataModelList;
    private String className = "", sectionName = "";
    public boolean isAttendenceTakenAndSaved = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance_activity);
        ButterKnife.bind(this);
        mActivity = this;
        retrofitDataProvider = new RetrofitDataProvider(mActivity);
        getIntentData();
        initToolbar();
        initView();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (Utilz.isOnline(mActivity)) {
            getStudentListFromServer();
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void getIntentData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.CLASS_NAME))
                className = mBundle.getString(AppConstants.CLASS_NAME);
            if (mBundle.containsKey(AppConstants.SECTION_NAME))
                sectionName = mBundle.getString(AppConstants.SECTION_NAME);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendenceBeforeLeave();
            }
        });
    }


    private void initView() {
        mActivity = this;
        studentListDataModelList = new ArrayList();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendence Class-" + className + " (" + Utilz.getCurrentDayNameAndDate() + ")");

        mRecyclerView.setNestedScrollingEnabled(false);

        classTeacherNameTv.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
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
                .into(userProfilePicIv);
    }


    private RetrofitDataProvider retrofitDataProvider;

    private void getStudentListFromServer() {
        Utilz.showDailog(TakeAttendenceActivity.this, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.getStudentsListByClassWise(className, sectionName, new DownlodableCallback<StudentListByClassModel>() {
            @Override
            public void onSuccess(final StudentListByClassModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    studentListDataModelList.clear();
                    studentListDataModelList = result.getData();
                    mRecyclerView.setAdapter(new AttendenceAdapter(studentListDataModelList, mActivity));
                }
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAttendenceBeforeLeave();
    }

    private void saveAttendenceBeforeLeave() {
        if (!isAttendenceTakenAndSaved) {
            Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.warning), mActivity.getString(R.string.attendance_taken_not_saved_msg), AppConstants.NO, AppConstants.YES);
        }
    }

    @OnClick({R.id.uploadAttendenceBtn, R.id.seeAttendenceBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uploadAttendenceBtn:
                if (Utilz.isOnline(mActivity)) {
                    uploadAttendence();
                } else {
                    Utilz.showNoInternetConnectionDialog(mActivity);
                }
                break;
            case R.id.seeAttendenceBtn:
                Intent mIntent = new Intent(mActivity, SeeAttendenceActivity.class);
                mIntent.putExtra(AppConstants.CLASS_NAME, className);
                mIntent.putExtra(AppConstants.SECTION_NAME, sectionName);
                mActivity.startActivity(mIntent);
                break;
        }
    }

    private void uploadAttendence() {
        Utilz.showDailog(TakeAttendenceActivity.this, getResources().getString(R.string.pleasewait));
        InsertAttendanceModel insertAttendanceModel = getListOfStudentAttendance();
        retrofitDataProvider.attendance(insertAttendanceModel, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
                isAttendenceTakenAndSaved = true;
                Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.attendance_taken_success_msg), "", AppConstants.OK);
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
            }
        });


    }

    private InsertAttendanceModel getListOfStudentAttendance() {
        InsertAttendanceModel insertAttendanceModel = new InsertAttendanceModel();
        insertAttendanceModel.setClas(className);
        insertAttendanceModel.setSec(sectionName);
        insertAttendanceModel.setDate(Utilz.getCurrentDate());
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.USER_ID)))
            insertAttendanceModel.setTeacher_id(ClsGeneral.getStrPreferences(AppConstants.USER_ID));
        else
            insertAttendanceModel.setTeacher_id(MyPreference.getUserId());
        ArrayList<AttendanceStatusModel> list = new ArrayList<>();
        //Create loop here and add student present/Absent status to AttendanceStatusModel
        if (studentListDataModelList != null && studentListDataModelList.size() > 0) {
            for (int i = 0; i < studentListDataModelList.size(); i++) {
                if (studentListDataModelList.get(i).isSelected()) {
                    if (!TextUtils.isEmpty(studentListDataModelList.get(i).getStudentId())) {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getStudentId(), "Present"));
                    } else {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getId(), "Present"));
                    }
                } else {
                    if (!TextUtils.isEmpty(studentListDataModelList.get(i).getStudentId())) {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getStudentId(), "Absent"));
                    } else {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getId(), "Absent"));
                    }
                }
            }
        }

        insertAttendanceModel.setAttendance(list);
        //End loop

        return insertAttendanceModel;
    }

    public void manageAbsentPresentCount(int mIntTotalStudentCount, int mIntAbsentStudentCount) {
        if (totalStudentCount != null)
            totalStudentCount.setText("Total-" + mIntTotalStudentCount);
        if (presentStudentCount != null) {
            presentStudentCount.setText((mIntTotalStudentCount - mIntAbsentStudentCount) + "");
        }
        if (absentStudentCount != null)
            absentStudentCount.setText(mIntAbsentStudentCount + "");

        if (totalAbsentPresentCardView != null) {
            if ((mIntTotalStudentCount - mIntAbsentStudentCount) > 0) {
                totalAbsentPresentCardView.setVisibility(View.VISIBLE);
                isAttendenceTakenAndSaved = false;
            } else {
                isAttendenceTakenAndSaved = true;
                totalAbsentPresentCardView.setVisibility(View.GONE);
            }
        }
    }
}
