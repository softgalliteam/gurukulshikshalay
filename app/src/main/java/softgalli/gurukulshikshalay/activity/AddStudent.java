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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.input_class)
    EditText input_class;
    @BindView(R.id.input_section)
    EditText input_section;
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
                studentRegIdTv.setText(studentListDataModel.getStudent_id());
                studentRegIdTv.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(studentListDataModel.getStudentId())) {
                studentRegIdTv.setText(studentListDataModel.getStudentId());
                studentRegIdTv.setVisibility(View.VISIBLE);
            } else {
                studentRegIdTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(studentListDataModel.getStudentName()))
                input_name.setText(studentListDataModel.getStudentName());

            if (!TextUtils.isEmpty(studentListDataModel.getStudent_id()))
                input_rollnumber.setText(studentListDataModel.getStudent_id());
            else if (!TextUtils.isEmpty(studentListDataModel.getStudentId()))
                input_rollnumber.setText(studentListDataModel.getStudentId());

            if (!TextUtils.isEmpty(studentListDataModel.getEmail()))
                input_email.setText(studentListDataModel.getEmail());

            if (!TextUtils.isEmpty(studentListDataModel.getClassName()))
                input_class.setText(studentListDataModel.getClassName());

            if (!TextUtils.isEmpty(studentListDataModel.getAdmissionDate()))
                input_admission.setText(studentListDataModel.getAdmissionDate());

            if (!TextUtils.isEmpty(studentListDataModel.getSec()))
                input_section.setText(studentListDataModel.getSec());

            if (!TextUtils.isEmpty(studentListDataModel.getMobile()))
                input_mobile.setText(studentListDataModel.getMobile());

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
        if (input_class.getText().toString().trim().equals("")) {
            Toast.makeText(AddStudent.this, "Enter Class Please", Toast.LENGTH_SHORT).show();
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
        String clas = input_class.getText().toString().trim();
        String sec = input_section.getText().toString().trim();
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
}