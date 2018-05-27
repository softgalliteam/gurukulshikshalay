package softgalli.gurukulshikshalay.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.AttendenceAdapter;
import softgalli.gurukulshikshalay.calender.CsvFileWriter;
import softgalli.gurukulshikshalay.calender.RealMController;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.common.WeekCalendarOptions;
import softgalli.gurukulshikshalay.fragments.WeekCalendarFragment;
import softgalli.gurukulshikshalay.intrface.CalenderListener;
import softgalli.gurukulshikshalay.model.AttendanceStatusModel;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.InsertAttendanceModel;
import softgalli.gurukulshikshalay.model.StudentListByClassModel;
import softgalli.gurukulshikshalay.model.StudentListDataModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class TakeAttendenceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 301;
    private static final String TAG = TakeAttendenceActivity.class.getSimpleName();
    public static String[] mStrArrExternalStorageReadWritePermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.totalStudentCount)
    TextView totalStudentCount;
    @BindView(R.id.presentStudentCount)
    TextView presentStudentCount;
    @BindView(R.id.absentStudentCount)
    TextView absentStudentCount;
    @BindView(R.id.filePath)
    TextView filePath;
    @BindView(R.id.uploadAttendenceBtn)
    TextView uploadAttendenceBtn;
    @BindView(R.id.seeAttendenceBtn)
    TextView seeAttendenceBtn;
    @BindView(R.id.mDateSelectedTv)
    TextView mDateSelectedTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_common)
    RecyclerView mRecyclerView;
    @BindView(R.id.userProfilePicIv)
    CircleImageView userProfilePicIv;
    @BindView(R.id.classTeacherNameTv)
    TextView classTeacherNameTv;
    private WeekCalendarFragment mWeekCalendarFragment;
    private String fileName = "";
    private Activity mActivity;
    private Realm realm;
    private AttendenceAdapter mAdapter;
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
        mWeekCalendarFragment = (WeekCalendarFragment) getSupportFragmentManager()
                .findFragmentByTag(WeekCalendarFragment.class.getSimpleName());

        getIntentData();
        initToolbar();

        initView();

        manageWeekCalenderView();

        initReam();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (Utilz.isOnline(mActivity)) {
            getStudentListFromServer();
        } else if (MyPreference.isPreLoaded()) {
            RealMController realMController = RealMController.with(mActivity);
            if (realMController != null)
                studentListDataModelList.addAll(realMController.getStudentsList());
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }


        manageCreateAndUploadAttendence();

        checkPermissionForReadStorage();

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
        getSupportActionBar().setTitle("Take Attendance");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initReam() {
        //get realm instance
        this.realm = RealMController.with(this).getRealm();

        // refresh the realm instance
        RealMController.with(this).refresh();
    }

    private void initView() {
        mActivity = this;
        studentListDataModelList = new ArrayList();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Take Attendence Class " + className);

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

    private void manageCreateAndUploadAttendence() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Attendence");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        Log.i(TAG, "var : " + var);

        fileName = folder.toString() + "/Class_" + className + "_" + formattedDate + ".csv";
        filePath.setText(fileName);
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
                //saving loaded data from server to realm
                if (studentListDataModelList != null && studentListDataModelList.size() > 0)
                    saveStudentListToRealM();
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

    private void saveStudentListToRealM() {
        //saving loaded data from server to realm
        if (studentListDataModelList != null && studentListDataModelList.size() > 0) {
            for (StudentListDataModel studentListDataModel : studentListDataModelList) {
                // Persist your data easily
                realm.beginTransaction();
                realm.copyToRealm(studentListDataModel);
                realm.commitTransaction();
            }
            MyPreference.setPreLoad(true);
        }
    }

    public void seeAttendence() {
        try {
            List<StudentListDataModel> studentsList = (mAdapter).getStudentist();
            if (!TextUtils.isEmpty(fileName) && studentsList != null && studentsList.size() > 0) {
                CsvFileWriter.writeCsvFile(fileName, studentsList);
            } else {
                Toast.makeText(mActivity, "Something went wrong while creating attendence csv file", Toast.LENGTH_SHORT).show();
            }
            showAttendence();
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong while creating attendence csv file,\nReason : " + e);
        }
    }

    public void showAttendence() {
        // Create URI
        File file = new File(fileName);
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/vnd.ms-excel");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(pdfIntent);
            } catch (Exception e) {
                Utilz.showMessageOnDialog(mActivity, "Install MS-Excel", "Please install MS-Excel app to view the file.");
            }
        }
    }

    /*******************************************************************************
     * Method Name : checkPermissionForReadStorage
     * Description : This method will request  Permission  for read and Write Storage
     */
    public void checkPermissionForReadStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, mStrArrExternalStorageReadWritePermissions,
                    REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            //For simple deny permission
                            //showPopupForCameraPermission();
                        } else if (!showRationale) {
                            // for NEVER ASK AGAIN deny permission
                        }
                    }
                }
            }
        }
    }

    /*###############################################################################################################################################################################
    ###############################################################################################################################################################################
    ###############################################################################################################################################################################*/


    private void manageWeekCalenderView() {
        if (mWeekCalendarFragment == null) {
            mWeekCalendarFragment = new WeekCalendarFragment();

            Bundle args = new Bundle();

            /* Must add this attribute if using ARGUMENT_NOW_BACKGROUND or ARGUMENT_SELECTED_DATE_BACKGROUND*/
            args.putString(WeekCalendarFragment.ARGUMENT_PACKAGE_NAME
                    , getApplicationContext().getPackageName());

            // Sets color to the primary views (Month name and dates)
            args.putInt(WeekCalendarFragment.ARGUMENT_PRIMARY_TEXT_COLOR
                    , ContextCompat.getColor(this, R.color.colorTextPrimary));

            // Picks between three or one date header letters ex. "Sun" or "S"
            // two options:
            // 1. WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS
            // 2. WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER
            args.putString(WeekCalendarFragment.ARGUMENT_DAY_HEADER_LENGTH
                    , WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER);

            // Days that have events
            ArrayList<Calendar> eventDays = new ArrayList<>();
            eventDays.add(Calendar.getInstance());
            long[] eventLong = new long[10];
            for (int i = 0; i < eventDays.size(); i++) {
                eventLong[i] = eventDays.get(i).getTimeInMillis();
            }
            args.putSerializable(WeekCalendarFragment.ARGUMENT_EVENT_DAYS, eventLong);

            mWeekCalendarFragment.setArguments(args);

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.container, mWeekCalendarFragment, WeekCalendarFragment.class.getSimpleName());
            t.commit();
        }

        CalenderListener listener = new CalenderListener() {
            @Override
            public void onSelectPicker() {
                //User can use any type of pickers here the below picker is only Just a example
                DatePickerDialog.newInstance(TakeAttendenceActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        .show(getFragmentManager(), "datePicker");
            }

            @Override
            public void onSelectDate(LocalDateTime mSelectedDate) {
                int sunday = mSelectedDate.getDayOfWeek();
                if (sunday == Calendar.SATURDAY) {
                    mDateSelectedTv.setText("Its Sunday !!");
                } else {
                    //callback when a date is selcted
                    mDateSelectedTv.setText(""
                            + mSelectedDate.getDayOfMonth()
                            + "-"
                            + mSelectedDate.getMonthOfYear()
                            + "-"
                            + mSelectedDate.getYear());
                }
            }
        };
        //setting the listener
        mWeekCalendarFragment.setCalenderListener(listener);

        mWeekCalendarFragment.setPreSelectedDate(Calendar.getInstance());
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        //This is the call back from picker used in the sample. You can use custom or any other picker
        //IMPORTANT: get the year,month and date from picker you using and call setDateWeek method
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        mWeekCalendarFragment.setDateWeek(calendar);//Sets the selected date from Picker
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                saveAttendenceBeforeLeave();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAttendenceBeforeLeave();
    }

    private void saveAttendenceBeforeLeave() {
        if (isAttendenceTakenAndSaved) {
            Toast.makeText(mActivity, "Attendence Taken", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(mActivity, "Are you sure?", Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.uploadAttendenceBtn, R.id.seeAttendenceBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uploadAttendenceBtn:
                uploadAttendence();
                break;
            case R.id.seeAttendenceBtn:
                seeAttendence();
                break;
        }
    }

    private void uploadAttendence() {
        /*if (MyPreference.isPreLoaded()) {
            RealMController realMController = RealMController.with(mActivity);
            if (realMController != null)
                studentListDataModelList.addAll(realMController.getStudentsList());
            Log.d("Attendence Taken", ""+studentListDataModelList);
        }*/

        Utilz.showDailog(TakeAttendenceActivity.this, getResources().getString(R.string.pleasewait));
        InsertAttendanceModel insertAttendanceModel = getListOfStudentAttendance();
        retrofitDataProvider.attendance(insertAttendanceModel, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
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
        insertAttendanceModel.setDate(Utilz.getCurrentDate(TakeAttendenceActivity.this));
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.USER_ID)))
            insertAttendanceModel.setTeacher_id(ClsGeneral.getStrPreferences(AppConstants.USER_ID));
        else
            insertAttendanceModel.setTeacher_id(ClsGeneral.getStrPreferences(AppConstants.ID));
        ArrayList<AttendanceStatusModel> list = new ArrayList<>();
        //Create loop here and add student present/Absent status to AttendanceStatusModel
        for (int i = 0; i < 100; i++) {
            list.add(new AttendanceStatusModel("1056", ("present" + i)));
        }

        insertAttendanceModel.setAttendance(list);
        //End loop

        return insertAttendanceModel;
    }

    public void manageAbsentPresentCount(int mIntTotalStudentCount, int mIntAbsentStudentCount) {
        if (totalStudentCount != null)
            totalStudentCount.setText(mIntTotalStudentCount + "\nTotal Students");
        if (presentStudentCount != null)
            presentStudentCount.setText((mIntTotalStudentCount - mIntAbsentStudentCount) + "\nPresent");
        if (absentStudentCount != null)
            absentStudentCount.setText(mIntAbsentStudentCount + "\nAbsent");
    }
}
