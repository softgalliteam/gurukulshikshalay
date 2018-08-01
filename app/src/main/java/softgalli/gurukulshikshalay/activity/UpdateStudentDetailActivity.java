package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class UpdateStudentDetailActivity extends AppCompatActivity {

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
    @BindView(R.id.classNameSpinner)
    Spinner classNameSpinner;
    @BindView(R.id.sectionNameSpinner)
    Spinner sectionNameSpinner;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.submitButtonLl)
    LinearLayout submitButtonLl;
    @BindView(R.id.updateButtonLl)
    LinearLayout updateButtonLl;
    private Activity mActivity;
    private String mStrUserId = "";
    private RetrofitDataProvider retrofitDataProvider;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);
        mActivity = this;
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
        initToolbar();
        initView();
        setAllUserDetailsIntoFields();
        handleClicks();

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
    }

    private void setAllUserDetailsIntoFields() {
        submitButtonLl.setVisibility(View.VISIBLE);
        updateButtonLl.setVisibility(View.GONE);
        input_rollnumber.setVisibility(View.GONE);
        sendButton.setText(mActivity.getResources().getString(R.string.update_details));
        getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.update_details));
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.USER_ID))) {
            studentRegIdTv.setText(ClsGeneral.getStrPreferences(AppConstants.USER_ID));
            studentRegIdTv.setVisibility(View.VISIBLE);
            mStrUserId = ClsGeneral.getStrPreferences(AppConstants.USER_ID);
        } else if (!TextUtils.isEmpty(MyPreference.getUserId())) {
            studentRegIdTv.setText(MyPreference.getUserId());
            studentRegIdTv.setVisibility(View.VISIBLE);
        } else {
            studentRegIdTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ROLL_NUMBER)))
            input_rollnumber.setText(ClsGeneral.getStrPreferences(AppConstants.ROLL_NUMBER));
        else {
            input_rollnumber.setVisibility(View.VISIBLE);
        }
        input_name.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        input_email.setText(ClsGeneral.getStrPreferences(AppConstants.EMAIL));
        input_admission.setText(ClsGeneral.getStrPreferences(AppConstants.JOINING_DATE));
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.CLASS_NAME))) {
            String classNameStr = ClsGeneral.getStrPreferences(AppConstants.CLASS_NAME);
            if (classNameStr.equalsIgnoreCase("10"))
                classNameSpinner.setSelection(1);
            else if (classNameStr.equalsIgnoreCase("9"))
                classNameSpinner.setSelection(2);
            else if (classNameStr.equalsIgnoreCase("8"))
                classNameSpinner.setSelection(3);
            else if (classNameStr.equalsIgnoreCase("7"))
                classNameSpinner.setSelection(4);
            else if (classNameStr.equalsIgnoreCase("6"))
                classNameSpinner.setSelection(5);
            else if (classNameStr.equalsIgnoreCase("5"))
                classNameSpinner.setSelection(6);
            else if (classNameStr.equalsIgnoreCase("4"))
                classNameSpinner.setSelection(7);
            else if (classNameStr.equalsIgnoreCase("3"))
                classNameSpinner.setSelection(8);
            else if (classNameStr.equalsIgnoreCase("2"))
                classNameSpinner.setSelection(9);
            else if (classNameStr.equalsIgnoreCase("1"))
                classNameSpinner.setSelection(10);
            else if (classNameStr.equalsIgnoreCase("LKG"))
                classNameSpinner.setSelection(11);
            else if (classNameStr.equalsIgnoreCase("UKG"))
                classNameSpinner.setSelection(12);
            else if (classNameStr.equalsIgnoreCase("NURSERY"))
                classNameSpinner.setSelection(13);
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.CLASS_NAME)) && sectionNameSpinner != null) {
            String sectionNameStr = ClsGeneral.getStrPreferences(AppConstants.CLASS_NAME);
            if (sectionNameStr.equalsIgnoreCase("A"))
                sectionNameSpinner.setSelection(1);
            else if (sectionNameStr.equalsIgnoreCase("B"))
                sectionNameSpinner.setSelection(2);
            else if (sectionNameStr.equalsIgnoreCase("C"))
                sectionNameSpinner.setSelection(3);
            else if (sectionNameStr.equalsIgnoreCase("D"))
                sectionNameSpinner.setSelection(4);
        }
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
                studentRegIdTv.setText(mStrUserId);
                addUpdateStudentsDetail();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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

    private void addUpdateStudentsDetail() {
        if (input_rollnumber.getText().toString().trim().equals("")) {
            Toast.makeText(mActivity, "Enter Roll Number Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input_name.getText().toString().trim().equals("")) {
            Toast.makeText(mActivity, "Enter Name Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (classNameSpinner != null && classNameSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Select Class Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sectionNameSpinner != null && sectionNameSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Select Section Please", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input_address.getText().toString().trim().equals("")) {
            Toast.makeText(mActivity, "Enter Address Please", Toast.LENGTH_SHORT).show();
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
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        if (TextUtils.isEmpty(mStrUserId)) {
            mStrUserId = Utilz.getRandomUserIdFromName(mActivity);
        }
        final String rollNumber = input_rollnumber.getText().toString().trim();
        final String name = input_name.getText().toString().trim();
        final String email = input_email.getText().toString().trim();
        final String mobile = input_mobile.getText().toString().trim();
        final String clas = classNameSpinner.getSelectedItem().toString().trim();
        final String sec = sectionNameSpinner.getSelectedItem().toString().trim();
        final String admission = input_admission.getText().toString().trim();
        final String address = input_address.getText().toString().trim();
        retrofitDataProvider.addstudent(mStrUserId, rollNumber, name, email, mobile, clas, sec, admission, address, new DownlodableCallback<StuTeaModel>() {
            @Override
            public void onSuccess(final StuTeaModel result) {
                //  closeDialog();
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.updated_successfully), "", AppConstants.OK);

                    MyPreference.setUserName(name);
                    ClsGeneral.setPreferences(AppConstants.NAME, name);
                    ClsGeneral.setPreferences(AppConstants.EMAIL, email);
                    ClsGeneral.setPreferences(AppConstants.CLAS, clas);
                    ClsGeneral.setPreferences(AppConstants.SEC, sec);
                    ClsGeneral.setPreferences(AppConstants.JOINING_DATE, admission);
                    ClsGeneral.setPreferences(AppConstants.RESIDENTIAL_ADDRESS, address);
                    ClsGeneral.setPreferences(AppConstants.PERMANENT_ADDRESS, address);
                    ClsGeneral.setPreferences(AppConstants.PHONE_NO, mobile);
                    ClsGeneral.setPreferences(AppConstants.ADDRESS, address);
                    ClsGeneral.setPreferences(AppConstants.ROLL_NUMBER, rollNumber);
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

}