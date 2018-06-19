package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class AddTeacher extends AppCompatActivity {

    @BindView(R.id.teacher_id)
    TextView teacher_id;
    @BindView(R.id.input_name)
    EditText input_name;
    @BindView(R.id.input_qualification)
    EditText input_qualification;
    @BindView(R.id.input_mobile)
    EditText input_mobile;
    @BindView(R.id.input_alternatenumber)
    EditText input_alternatenumber;
    @BindView(R.id.input_emailid)
    EditText input_emailid;
    @BindView(R.id.input_classtea)
    EditText input_classtea;
    @BindView(R.id.input_joindate)
    EditText input_joindate;
    @BindView(R.id.input_address)
    EditText input_address;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.submitButtonLl)
    LinearLayout submitButtonLl;
    @BindView(R.id.updateButton)
    Button updateButton;
    @BindView(R.id.deleteButton)
    Button deleteButton;
    @BindView(R.id.updateButtonLl)
    LinearLayout updateButtonLl;
    private boolean isForUpdate;
    private Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteacher);
        ButterKnife.bind(this);
        mActivity = this;
        retrofitDataProvider = new RetrofitDataProvider(this);
        getIntentData();
        initToolbar();
        initView();
        handleClicks();

    }

    private void getIntentData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.IS_FOR_UPDATE))
                isForUpdate = mBundle.getBoolean(AppConstants.IS_FOR_UPDATE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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

    private void initView() {
        if (isForUpdate) {
            sendButton.setText(mActivity.getResources().getString(R.string.update_details));
            getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.update_details));
            updateButtonLl.setVisibility(View.VISIBLE);
            submitButtonLl.setVisibility(View.GONE);
            fillAllTeacherDetailsIntoFields();
        } else {
            sendButton.setText(mActivity.getResources().getString(R.string.add_teacher));
            updateButtonLl.setVisibility(View.GONE);
            submitButtonLl.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.add_teacher));
        }
    }

    private void fillAllTeacherDetailsIntoFields() {
        teacher_id.setText(ClsGeneral.getStrPreferences(AppConstants.USER_ID));
        input_name.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        input_qualification.setText(ClsGeneral.getStrPreferences(AppConstants.QUALIFICATION));
        input_mobile.setText(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO));
        input_alternatenumber.setText(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER));
        input_emailid.setText(ClsGeneral.getStrPreferences(AppConstants.EMAIL));
        input_classtea.setText(ClsGeneral.getStrPreferences(AppConstants.CLASS_TEACHER_FOR));
        input_joindate.setText(ClsGeneral.getStrPreferences(AppConstants.JOINING_DATE));

        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ADDRESS)))
            input_address.setText(ClsGeneral.getStrPreferences(AppConstants.ADDRESS));
        else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.PERMANENT_ADDRESS)))
            input_address.setText(ClsGeneral.getStrPreferences(AppConstants.PERMANENT_ADDRESS));
        else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.RESIDENTIAL_ADDRESS)))
            input_address.setText(ClsGeneral.getStrPreferences(AppConstants.RESIDENTIAL_ADDRESS));

    }

    private void handleClicks() {
        submitButtonLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpdateTeacherDetail();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUpdateTeacherDetail();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteStudentFromSchool();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        input_joindate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTeacher.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_joindate.setText(sdf.format(myCalendar.getTime()));
    }

    private void addUpdateTeacherDetail() {
        Utilz.showDailog(AddTeacher.this, getResources().getString(R.string.pleasewait));
        String teacherId = teacher_id.getText().toString().trim();
        String name = input_name.getText().toString().trim();
        String qualification = input_qualification.getText().toString().trim();
        String mobile = input_mobile.getText().toString().trim();
        String alternatenumber = input_alternatenumber.getText().toString().trim();
        String emailid = input_emailid.getText().toString().trim();
        String classtea = input_classtea.getText().toString().trim();
        String joindate = input_joindate.getText().toString().trim();
        String address = input_address.getText().toString().trim();
        retrofitDataProvider.addteacher(teacherId, name, qualification, mobile, alternatenumber, emailid, classtea, joindate, address, new DownlodableCallback<StuTeaModel>() {
            @Override
            public void onSuccess(final StuTeaModel result) {
                //  closeDialog();
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    if (isForUpdate)
                        Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.updated_successfully), "", AppConstants.OK);
                    else
                        Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.added_successfully), "", AppConstants.OK);
                }
            }

            @Override
            public void onFailure(String error) {
                // closeDialog();
                Utilz.closeDialog();
                Toast.makeText(AddTeacher.this, "" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
                Toast.makeText(AddTeacher.this, "Something went wrong, Please try again!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteStudentFromSchool() {
        if (TextUtils.isEmpty(teacher_id.getText().toString().trim())) {
            Toast.makeText(mActivity, "Invalid teacher registration id", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (Utilz.isOnline(mActivity)) {
                deleteTeacherFromDb();
            } else {
                Utilz.showNoInternetConnectionDialog(mActivity);
            }
        }
    }

    private void deleteTeacherFromDb() {
        String studentRegIdStr = teacher_id.getText().toString().trim();
        if (TextUtils.isEmpty(studentRegIdStr)) {
            return;
        }
        retrofitDataProvider.deleteStudent(studentRegIdStr, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
                //  Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.sent_successfully), AppConstants.OK, "");
                mActivity.startActivity(new Intent(mActivity, SeeLeaveListActivity.class));
                finish();
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
        });
    }
}
