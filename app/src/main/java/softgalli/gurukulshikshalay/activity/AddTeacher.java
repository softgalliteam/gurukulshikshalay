package softgalli.gurukulshikshalay.activity;

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

public class AddTeacher extends AppCompatActivity {

    @BindView(R.id.input_rollnumber)
    EditText input_rollnumber;
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
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RetrofitDataProvider retrofitDataProvider;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtea);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);

        initToolbar();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilz.isInternetConnected(AddTeacher.this)) {
                    submitData();
                } else {
                    Toast.makeText(AddTeacher.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
        input_joindate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTeacher.this, date, myCalendar
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
        getSupportActionBar().setTitle("Add Teacher");
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

        input_joindate.setText(sdf.format(myCalendar.getTime()));
    }

    private void submitData() {
        Utilz.showDailog(AddTeacher.this, getResources().getString(R.string.pleasewait));
        String rollNumber = input_rollnumber.getText().toString().trim();
        String name = input_name.getText().toString().trim();
        String qualification = input_qualification.getText().toString().trim();
        String mobile = input_mobile.getText().toString().trim();
        String alternatenumber = input_alternatenumber.getText().toString().trim();
        String emailid = input_emailid.getText().toString().trim();
        String classtea = input_classtea.getText().toString().trim();
        String joindate = input_joindate.getText().toString().trim();
        String address = input_address.getText().toString().trim();
        retrofitDataProvider.addteacher(rollNumber, name, qualification, mobile, alternatenumber, emailid, classtea, joindate, address, new DownlodableCallback<StuTeaModel>() {
            @Override
            public void onSuccess(final StuTeaModel result) {
                //  closeDialog();
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    Toast.makeText(AddTeacher.this, "" + result.getData(), Toast.LENGTH_SHORT).show();
                    finish();
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
}
