package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class UpdateStudentDetailActivity extends AppCompatActivity {

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
    private boolean isForUpdate;
    private Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;
    Calendar myCalendar = Calendar.getInstance();

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
            fillAllTeacherDetailsIntoFields();
        } else {
            sendButton.setText(mActivity.getResources().getString(R.string.add_teacher));
        }
    }

    private void fillAllTeacherDetailsIntoFields() {
        input_rollnumber.setText(ClsGeneral.getStrPreferences(AppConstants.USER_ID));
        input_name.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        input_rollnumber.setText(ClsGeneral.getStrPreferences(AppConstants.ROLL_NUMBER));
        input_email.setText(ClsGeneral.getStrPreferences(AppConstants.EMAIL));
        input_class.setText(ClsGeneral.getStrPreferences(AppConstants.CLASS_NAME));
        input_admission.setText(ClsGeneral.getStrPreferences(AppConstants.JOINING_DATE));

        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO)))
            input_mobile.setText(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO));
        else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER)))
            input_mobile.setText(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER));

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
                if (input_rollnumber.getText().toString().trim().equals("")) {
                    Toast.makeText(mActivity, "Enter Roll Number Please", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (input_name.getText().toString().trim().equals("")) {
                    Toast.makeText(mActivity, "Enter Name Please", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (input_class.getText().toString().trim().equals("")) {
                    Toast.makeText(mActivity, "Enter Class Please", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (input_address.getText().toString().trim().equals("")) {
                    Toast.makeText(mActivity, "Enter Address Please", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (Utilz.isInternetConnected(mActivity)) {
                        submitData();
                    } else {
                        Toast.makeText(mActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
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
                new DatePickerDialog(mActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_admission.setText(sdf.format(myCalendar.getTime()));
    }

    private void submitData() {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        String rollNumber = input_rollnumber.getText().toString().trim();
        String name = input_name.getText().toString().trim();
        String email = input_email.getText().toString().trim();
        String mobile = input_mobile.getText().toString().trim();
        String clas = input_class.getText().toString().trim();
        String sec = input_section.getText().toString().trim();
        String admission = input_admission.getText().toString().trim();
        String address = input_address.getText().toString().trim();
        retrofitDataProvider.addstudent(rollNumber, name, email, mobile, clas, sec, admission, address, new DownlodableCallback<StuTeaModel>() {
            @Override
            public void onSuccess(final StuTeaModel result) {
                //  closeDialog();

                Utilz.closeDialog();

                if (result.getStatus().contains(PreferenceName.TRUE)) {

                    Toast.makeText(mActivity, "" + result.getData(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(String error) {
                // closeDialog();
            }

            @Override
            public void onUnauthorized(int errorNumber) {

            }
        });
    }
}