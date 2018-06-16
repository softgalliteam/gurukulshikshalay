package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.UpdateAttendanceAdapter;
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
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

/**
 * Created by Welcome on 2/12/2018.
 */

public class UpdateAttendanceActivity extends AppCompatActivity {
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.submitButtonLl)
    LinearLayout submitButtonLl;
    private String TAG = UpdateAttendanceActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.attendanceDateTv)
    TextView attendanceDateTv;
    @BindView(R.id.totalAbsentPresentCardView)
    LinearLayout totalAbsentPresentCardView;
    @BindView(R.id.mRecyclerView)
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
    }

    private void getAttendenceByDateClassSec(String mStrDateOfAtt) {
        //get attendence of that particular date
        if (Utilz.isOnline(mActivity)) {
            if (TextUtils.isEmpty(mClassName)) {
                Toast.makeText(mActivity, "Please select class", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mClassName)) {
                Toast.makeText(mActivity, "Please select section", Toast.LENGTH_SHORT).show();
            } else {
                getStudentsAttendance(mStrDateOfAtt.trim());
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void initView() {
        sendButton.setText(mActivity.getResources().getString(R.string.update_attendance));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        manageAbsentPresentCount(0, 0);
        attendanceDateTv.setText(Utilz.getCurrentDayNameAndDate());
        getAttendenceByDateClassSec(Utilz.getCurrentDate());
    }

    private void getIntentData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.CLASS_NAME))
                mClassName = mBundle.getString(AppConstants.CLASS_NAME);
            if (mBundle.containsKey(AppConstants.SECTION_NAME))
                mSectionName = mBundle.getString(AppConstants.SECTION_NAME);
            getSupportActionBar().setTitle("Update Attendence of Class-" + mClassName);
        }

        if (MyPreference.isLogined() && MyPreference.getLoginedAs().equalsIgnoreCase(AppConstants.STUDENT))
            mStudentId = MyPreference.getUserId();
        if (TextUtils.isEmpty(mStudentId) && MyPreference.isLogined() && MyPreference.getLoginedAs().equalsIgnoreCase(AppConstants.STUDENT))
            mStudentId = ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                    mRecyclerView.setAdapter(new UpdateAttendanceAdapter(mActivity, studentListDataModelList));

                    if (studentListDataModelList != null && studentListDataModelList.size() > 0) {
                        //Show here present students count and absent students count for example
                        manageAbsentPresentCount(studentListDataModelList.size(), getAbsentStudentCount(studentListDataModelList));
                    } else
                        manageAbsentPresentCount(0, 0);
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

    public int getAbsentStudentCount(ArrayList<StudentListDataModel> studentListDataModelList) {
        int totalPresentStudentsCount = 0;
        for (int i = 0; i < studentListDataModelList.size(); i++) {
            if (studentListDataModelList.get(i).isSelected() || (studentListDataModelList.get(i).getStatus()).equalsIgnoreCase(AppConstants.PRESENT)) {
                totalPresentStudentsCount = totalPresentStudentsCount + 1;
                studentListDataModelList.get(i).setSelected(true);
                studentListDataModelList.get(i).setStatus(AppConstants.PRESENT);
            } else {
                studentListDataModelList.get(i).setSelected(false);
                studentListDataModelList.get(i).setStatus(AppConstants.ABSENT);
            }
        }
        return (studentListDataModelList.size() - totalPresentStudentsCount);
    }

    public void manageAbsentPresentCount(int mIntTotalStudentCount, int mIntAbsentStudentCount) {
        if (totalStudentCount != null)
            totalStudentCount.setText("Total-" + mIntTotalStudentCount);
        if (presentStudentCount != null) {
            presentStudentCount.setText((mIntTotalStudentCount - mIntAbsentStudentCount) + "");
        }
        if (absentStudentCount != null)
            absentStudentCount.setText(mIntAbsentStudentCount + "");

        if (totalAbsentPresentCardView != null && submitButtonLl != null) {
            if ((mIntTotalStudentCount - mIntAbsentStudentCount) > 0) {
                totalAbsentPresentCardView.setVisibility(View.VISIBLE);
                submitButtonLl.setVisibility(View.VISIBLE);
            } else {
                totalAbsentPresentCardView.setVisibility(View.GONE);
                submitButtonLl.setVisibility(View.GONE);
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
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = df.parse(mStrSelectedDate);
                            mStrSelectedDate = df.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

    @OnClick({R.id.attendanceDateTv, R.id.submitButtonLl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.attendanceDateTv:
                openDatePicker();
                break;
            case R.id.submitButtonLl:
                if (Utilz.isOnline(mActivity)) {
                    uploadAttendence();
                } else {
                    Utilz.showNoInternetConnectionDialog(mActivity);
                }
                break;
        }
    }

    private void uploadAttendence() {
        Utilz.showDailog(mActivity, getResources().getString(R.string.pleasewait));
        InsertAttendanceModel insertAttendanceModel = getListOfStudentAttendance();
        retrofitDataProvider.attendance(insertAttendanceModel, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
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
        insertAttendanceModel.setClas(mClassName);
        insertAttendanceModel.setSec(mSectionName);
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
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getStudentId(), studentListDataModelList.get(i).getStudentName(), "Present"));
                    } else {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getStudent_id(), studentListDataModelList.get(i).getStudentName(), "Present"));
                    }
                } else {
                    if (!TextUtils.isEmpty(studentListDataModelList.get(i).getStudentId())) {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getStudentId(), studentListDataModelList.get(i).getStudentName(), "Absent"));
                    } else {
                        list.add(new AttendanceStatusModel(studentListDataModelList.get(i).getStudent_id(), studentListDataModelList.get(i).getStudentName(), "Absent"));
                    }
                }
            }
        }

        insertAttendanceModel.setAttendance(list);
        //End loop

        return insertAttendanceModel;
    }

}
