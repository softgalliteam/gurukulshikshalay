package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.Utilz;

/**
 * Created by Welcome on 2/12/2018.
 */

public class SeeAttendenceActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.attendanceDateTv)
    TextView attendanceDateTv;
    private String TAG = SeeAttendenceActivity.class.getSimpleName();
    @BindView(R.id.totalStudentCount)
    TextView totalStudentCount;
    @BindView(R.id.presentStudentCount)
    TextView presentStudentCount;
    @BindView(R.id.absentStudentCount)
    TextView absentStudentCount;
    private Activity mActivity;
    int mYear, mMonth, mDay;
    private String mClassName = "", mSectionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_attendence_activity);
        mActivity = this;
        ButterKnife.bind(this);
        initToolbar();
        initView();
        getIntentData();
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
                    Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.attendance_not_take_till_now));
                }
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void initView() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        String mStrCurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
        String mStrDayName = Utilz.getDayNameFromDate(mStrCurrentDate);
        if (!TextUtils.isEmpty(mStrDayName))
            mStrDayName = mStrDayName + ", " + mStrCurrentDate;
        attendanceDateTv.setText(mStrDayName);
        getAttendenceByDateClassSec(mStrCurrentDate);
    }

    private void getIntentData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.CLASS_NAME))
                mClassName = mBundle.getString(AppConstants.CLASS_NAME);
            if (mBundle.containsKey(AppConstants.SECTION_NAME))
                mSectionName = mBundle.getString(AppConstants.SECTION_NAME);
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

        Toast.makeText(mActivity, "API is calling for getting attendance of " + mStrDate + " date", Toast.LENGTH_SHORT).show();
        //Show here present students count and absent students count for example
        manageAbsentPresentCount(15, 2);
    }

    public void manageAbsentPresentCount(int mIntTotalStudentCount, int mIntAbsentStudentCount) {
        if (totalStudentCount != null)
            totalStudentCount.setText(mIntTotalStudentCount + "\nTotal Students");
        if (presentStudentCount != null && mIntTotalStudentCount > mIntAbsentStudentCount)
            presentStudentCount.setText((mIntTotalStudentCount - mIntAbsentStudentCount) + "\nPresent");
        if (absentStudentCount != null)
            absentStudentCount.setText(mIntAbsentStudentCount + "\nAbsent");
    }

    @OnClick(R.id.attendanceDateTv)
    public void onViewClicked() {
        openDatePicker();
    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mStrSelectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
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
