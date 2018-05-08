package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class AddStudent extends AppCompatActivity {

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
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

        initToolbar();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_rollnumber.getText().toString().trim().equals("")) {
                    Toast.makeText(AddStudent.this, "Enter Rollnumber Please", Toast.LENGTH_SHORT).show();
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
                    if (Utilz.isInternetConnected(AddStudent.this)) {
                        submitData();
                    } else {
                        Toast.makeText(AddStudent.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(AddStudent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Student");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_admission.setText(sdf.format(myCalendar.getTime()));
    }

    private void submitData() {
        Utilz.showDailog(AddStudent.this, mActivity.getResources().getString(R.string.pleasewait));
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

                    Toast.makeText(AddStudent.this, "" + result.getData(), Toast.LENGTH_SHORT).show();
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