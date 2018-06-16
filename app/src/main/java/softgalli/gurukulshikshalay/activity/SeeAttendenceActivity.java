package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.AttendenceAdapter;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.StudentListByClassModel;
import softgalli.gurukulshikshalay.model.StudentListDataModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

/**
 * Created by Welcome on 2/12/2018.
 */

public class SeeAttendenceActivity extends AppCompatActivity {
    private String TAG = SeeAttendenceActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.attendanceDateTv)
    TextView attendanceDateTv;
    @BindView(R.id.totalAbsentPresentCardView)
    LinearLayout totalAbsentPresentCardView;
    @BindView(R.id.rv_common)
    RecyclerView mRecyclerView;
    @BindView(R.id.totalStudentCount)
    TextView totalStudentCount;
    @BindView(R.id.presentStudentCount)
    TextView presentStudentCount;
    @BindView(R.id.absentStudentCount)
    TextView absentStudentCount;
    @BindView(R.id.noRecordFoundCardView)
    CardView noRecordFoundCardView;
    @BindView(R.id.mainCardView)
    CardView mainCardView;
    private Activity mActivity;
    private String mClassName = "", mSectionName = "", mStudentId = "";
    private RetrofitDataProvider retrofitDataProvider;
    private ArrayList<StudentListDataModel> studentListDataModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_attendence_activity);
        mActivity = this;
        studentListDataModelList = new ArrayList();

        retrofitDataProvider = new RetrofitDataProvider(mActivity);

        ButterKnife.bind(this);
        initToolbar();
        getIntentData();
        initView();
        if (MyPreference.isLogined() && MyPreference.getLoginedAs().equalsIgnoreCase(AppConstants.STUDENT))
            mStudentId = MyPreference.getUserId();
        if (TextUtils.isEmpty(mStudentId) && MyPreference.isLogined() && MyPreference.getLoginedAs().equalsIgnoreCase(AppConstants.STUDENT))
            mStudentId = ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS);
    }

    private void getAttendenceByDateClassSec(String mStrDateOfAtt) {
        //get attendence of that particular date
        if (Utilz.isOnline(mActivity)) {
            if (TextUtils.isEmpty(mClassName)) {
                Toast.makeText(mActivity, "Please select class", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mClassName)) {
                Toast.makeText(mActivity, "Please select section", Toast.LENGTH_SHORT).show();
            } else {
                if (Utilz.isAttendanceTakenToday(mStrDateOfAtt)) {
                    getStudentsAttendance(mStrDateOfAtt.trim());
                } else {
                    Utilz.showMessageOnDialog(mActivity, "", mActivity.getResources().getString(R.string.attendance_not_take_till_now), AppConstants.OK, "");
                }
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void initView() {
        attendanceDateTv.setText(Utilz.getCurrentDayNameAndDate());
        getAttendenceByDateClassSec(Utilz.getCurrentDate());

        manageAbsentPresentCount(studentListDataModelList.size(), getAbsentStudentCount(studentListDataModelList));
    }

    private void getIntentData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.CLASS_NAME))
                mClassName = mBundle.getString(AppConstants.CLASS_NAME);
            if (mBundle.containsKey(AppConstants.SECTION_NAME))
                mSectionName = mBundle.getString(AppConstants.SECTION_NAME);
            getSupportActionBar().setTitle("Attendence of Class-" + mClassName);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("See Attendence");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getStudentsAttendance(String mStrDate) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.getStudentsAttendance(mClassName, mSectionName, mStudentId, mStrDate, new DownlodableCallback<StudentListByClassModel>() {
            @Override
            public void onSuccess(final StudentListByClassModel result) {
                Utilz.closeDialog();
                if (result != null && result.getStatus().contains(PreferenceName.TRUE)) {
                    studentListDataModelList.clear();
                    studentListDataModelList = result.getData();
                    mRecyclerView.setAdapter(new AttendenceAdapter(mActivity, studentListDataModelList, false));

                    if (studentListDataModelList != null && studentListDataModelList.size() > 0) {
                        //Show here present students count and absent students count for example
                        manageAbsentPresentCount(studentListDataModelList.size(), getAbsentStudentCount(studentListDataModelList));
                    }
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

    private int getAbsentStudentCount(ArrayList<StudentListDataModel> studentListDataModelList) {
        int totalAbsentStudentsCount = 0;
        for (int i = 0; i < studentListDataModelList.size(); i++) {
            if (!studentListDataModelList.get(i).isSelected())
                totalAbsentStudentsCount = totalAbsentStudentsCount + 1;
        }
        return totalAbsentStudentsCount;
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
            } else {
                totalAbsentPresentCardView.setVisibility(View.GONE);
            }
        }
        if (mIntTotalStudentCount > 0 && noRecordFoundCardView != null && mainCardView != null) {
            noRecordFoundCardView.setVisibility(View.GONE);
            mainCardView.setVisibility(View.VISIBLE);
        } else if (noRecordFoundCardView != null && mainCardView != null) {
            noRecordFoundCardView.setVisibility(View.VISIBLE);
            mainCardView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.attendanceDateTv)
    public void onViewClicked() {
        openDatePicker();
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mStrSelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        String mStrDayName = Utilz.getDayNameFromDate(mStrSelectedDate);
                        if (!TextUtils.isEmpty(mStrDayName))
                            mStrDayName = mStrDayName + ", " + mStrSelectedDate;
                        attendanceDateTv.setText(mStrDayName);
                        getAttendenceByDateClassSec(mStrSelectedDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

}
