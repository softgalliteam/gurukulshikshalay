package softgalli.gurukulshikshalay.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.model.StudentListDataModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class AddStudent extends AppCompatActivity {

    @BindView(R.id.studentRegIdTv)
    TextView studentRegIdTv;
    @BindView(R.id.input_rollnumber)
    EditText input_rollnumber;
    @BindView(R.id.input_name)
    EditText input_name;
    @BindView(R.id.input_email)
    EditText input_email;
    @BindView(R.id.input_mobile)
    EditText input_mobile;
    @BindView(R.id.input_admission)
    EditText input_admission;
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
    @BindView(R.id.classNameSpinner)
    Spinner classNameSpinner;
    @BindView(R.id.sectionNameSpinner)
    Spinner sectionNameSpinner;
    @BindView(R.id.buttonLl)
    RelativeLayout buttonLl;
    private String mStrClassName, mStrSectionName, mStrMessage, mStrPhoneNo;
    private boolean isForUpdate;
    private Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;
    Calendar myCalendar = Calendar.getInstance();
    ArrayList<StudentListDataModel> mStudentsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);
        mActivity = this;
        ButterKnife.bind(this);
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
            if (mBundle.containsKey(AppConstants.STUDENTS_DETAILS))
                mStudentsArrayList = (ArrayList<StudentListDataModel>) mBundle.getSerializable(AppConstants.STUDENTS_DETAILS);
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
        List<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();

        classList.addAll(Utilz.getClassList());

        sectionList.addAll(Utilz.getSectionList());

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_dropdown_item_1line, classList);
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_dropdown_item_1line, sectionList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        classNameSpinner.setAdapter(classAdapter);
        sectionNameSpinner.setAdapter(sectionAdapter);
        sendButton.setText(mActivity.getResources().getString(R.string.add_student));

        if (isForUpdate) {
            updateButtonLl.setVisibility(View.VISIBLE);
            submitButtonLl.setVisibility(View.GONE);
            getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.update_details));
            if (mStudentsArrayList != null && mStudentsArrayList.size() > 0)
                fillAllStudentsDetailsIntoFields(mStudentsArrayList.get(0));
        } else {
            updateButtonLl.setVisibility(View.GONE);
            submitButtonLl.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.add_student));
        }
    }

    private void fillAllStudentsDetailsIntoFields(StudentListDataModel studentListDataModel) {
        if (studentListDataModel != null) {
            if (!TextUtils.isEmpty(studentListDataModel.getStudent_id())) {
                studentRegIdTv.setText("Id : " + studentListDataModel.getStudent_id() + " & Password : " + studentListDataModel.getPassword());
                studentRegIdTv.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(studentListDataModel.getStudentId())) {
                studentRegIdTv.setText("Id : " + studentListDataModel.getStudentId() + " & Password : " + studentListDataModel.getPassword());
                studentRegIdTv.setVisibility(View.VISIBLE);
            } else {
                studentRegIdTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(studentListDataModel.getStudentName()))
                input_name.setText(studentListDataModel.getStudentName());

            if (studentListDataModel.getRollNo() > 0)
                input_rollnumber.setText(studentListDataModel.getRollNo() + "");

            if (!TextUtils.isEmpty(studentListDataModel.getEmail()))
                input_email.setText(studentListDataModel.getEmail());

            if (!TextUtils.isEmpty(studentListDataModel.getClassName())) {
                if (studentListDataModel.getClassName().equalsIgnoreCase("10"))
                    classNameSpinner.setSelection(1);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("9"))
                    classNameSpinner.setSelection(2);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("8"))
                    classNameSpinner.setSelection(3);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("7"))
                    classNameSpinner.setSelection(4);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("6"))
                    classNameSpinner.setSelection(5);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("5"))
                    classNameSpinner.setSelection(6);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("4"))
                    classNameSpinner.setSelection(7);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("3"))
                    classNameSpinner.setSelection(8);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("2"))
                    classNameSpinner.setSelection(9);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("1"))
                    classNameSpinner.setSelection(10);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("LKG"))
                    classNameSpinner.setSelection(11);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("UKG"))
                    classNameSpinner.setSelection(12);
                else if (studentListDataModel.getClassName().equalsIgnoreCase("NURSERY"))
                    classNameSpinner.setSelection(13);

                if (!studentListDataModel.getClassName().equalsIgnoreCase("Select Class"))
                    mStrClassName = studentListDataModel.getClassName();
            }
            if (!TextUtils.isEmpty(studentListDataModel.getAdmissionDate()))
                input_admission.setText(studentListDataModel.getAdmissionDate());

            if (!TextUtils.isEmpty(studentListDataModel.getSec()) && sectionNameSpinner != null) {
                if (studentListDataModel.getSec().equalsIgnoreCase("A"))
                    sectionNameSpinner.setSelection(1);
                else if (studentListDataModel.getSec().equalsIgnoreCase("B"))
                    sectionNameSpinner.setSelection(2);
                else if (studentListDataModel.getSec().equalsIgnoreCase("C"))
                    sectionNameSpinner.setSelection(3);
                else if (studentListDataModel.getSec().equalsIgnoreCase("D"))
                    sectionNameSpinner.setSelection(4);

                if (!studentListDataModel.getSec().equalsIgnoreCase("Select Section"))
                    mStrSectionName = studentListDataModel.getSec();
            }
            if (!TextUtils.isEmpty(studentListDataModel.getMobile())) {
                input_mobile.setText(studentListDataModel.getMobile());
                mStrPhoneNo = studentListDataModel.getMobile();
            }
            if (!TextUtils.isEmpty(studentListDataModel.getPermanent_address()))
                input_address.setText(studentListDataModel.getResidential_address());
            else if (!TextUtils.isEmpty(studentListDataModel.getPermanent_address()))
                input_address.setText(studentListDataModel.getPermanent_address());
        }
    }

    private void handleClicks() {
        submitButtonLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = Utilz.getRandomUserIdFromName(input_name.getText().toString().trim());
                studentRegIdTv.setText(userId);
                addUpdateStudentsDetail();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUpdateStudentsDetail();
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

        input_admission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddStudent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void deleteStudentFromSchool() {
        if (TextUtils.isEmpty(studentRegIdTv.getText().toString().trim())) {
            Toast.makeText(mActivity, "Invalid student registration id", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (Utilz.isOnline(mActivity)) {
                deleteStudentFromDb();
            } else {
                Utilz.showNoInternetConnectionDialog(mActivity);
            }
        }
    }

    private void addUpdateStudentsDetail() {
        if (input_rollnumber.getText().toString().trim().equals("")) {
            Toast.makeText(AddStudent.this, "Enter Roll Number Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input_name.getText().toString().trim().equals("")) {
            Toast.makeText(AddStudent.this, "Enter Name Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (classNameSpinner != null && classNameSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(AddStudent.this, "Select Class Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sectionNameSpinner != null && sectionNameSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(AddStudent.this, "Select Section Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input_address.getText().toString().trim().equals("")) {
            Toast.makeText(AddStudent.this, "Enter Address Please", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (Utilz.isOnline(mActivity)) {
                addNewStudentInDb();
            } else {
                Utilz.showNoInternetConnectionDialog(mActivity);
            }
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_admission.setText(sdf.format(myCalendar.getTime()));
    }

    private void addNewStudentInDb() {
        Utilz.showDailog(AddStudent.this, mActivity.getResources().getString(R.string.pleasewait));
        String userId = studentRegIdTv.getText().toString().trim();
        if (TextUtils.isEmpty(userId)) {
            userId = Utilz.getRandomUserIdFromName(input_name.getText().toString().trim());
        }
        String rollNumber = input_rollnumber.getText().toString().trim();
        String name = input_name.getText().toString().trim();
        String email = input_email.getText().toString().trim();
        String mobile = input_mobile.getText().toString().trim();
        String clas = classNameSpinner.getSelectedItem().toString().trim();
        String sec = sectionNameSpinner.getSelectedItem().toString().trim();
        String admission = input_admission.getText().toString().trim();
        String address = input_address.getText().toString().trim();
        retrofitDataProvider.addstudent(userId, rollNumber, name, email, mobile, clas, sec, admission, address, new DownlodableCallback<StuTeaModel>() {
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
                Utilz.closeDialog();
                Toast.makeText(mActivity, "" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
                Toast.makeText(mActivity, "Something went wrong, Please try again!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteStudentFromDb() {
        String studentUserIdStr = studentRegIdTv.getText().toString().trim();
        if (TextUtils.isEmpty(studentUserIdStr)) {
            return;
        }
        retrofitDataProvider.deleteStudent(studentUserIdStr, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
                Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.deleted_successfully), "", AppConstants.OK);
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

    @OnClick({R.id.seeAttendanceButton, R.id.sendMessageButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.seeAttendanceButton:
                seeAttendance();
                break;
            case R.id.sendMessageButton:
                Utilz.showMessageDialog(mActivity, "This feature is coming soon...");
                //for phone messaging
                //sendSMSMessage();
                //for using sms gateway to send sms
                //Utilz.setMessage(mActivity, mStrPhoneNo, mStrMessage, true);
                break;
        }
    }

    private void seeAttendance() {
        if (!TextUtils.isEmpty(mStrClassName) && !TextUtils.isEmpty(mStrSectionName)) {
            Intent mIntent = new Intent(mActivity, SeeAttendenceActivity.class);
            mIntent.putExtra(AppConstants.CLASS_NAME, mStrClassName);
            mIntent.putExtra(AppConstants.SECTION_NAME, mStrSectionName);
            startActivity(mIntent);
        } else {
            Toast.makeText(mActivity, R.string.class_or_section_is_missing_msg, Toast.LENGTH_SHORT).show();
        }
    }

   /* private void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.SEND_SMS}, AppConstants.MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Utilz.setMessage(mActivity, mStrPhoneNo, mStrMessage, true);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.sms_failed_to_send, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }*/
}