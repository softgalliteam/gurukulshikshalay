package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class UpdateTeacherDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.teacherIdTv)
    TextView teacherIdTv;
    @BindView(R.id.teacherNameTv)
    TextView teacherNameTv;
    @BindView(R.id.teacherQualTv)
    EditText teacherQualTv;
    @BindView(R.id.teacherPhoneTv)
    EditText teacherPhoneTv;
    @BindView(R.id.teacherAltPhomeTv)
    EditText teacherAltPhomeTv;
    @BindView(R.id.teacherEmailTv)
    EditText teacherEmailTv;
    @BindView(R.id.teacherOfClassTv)
    EditText teacherOfClassTv;
    @BindView(R.id.teacherJoinDateTv)
    EditText teacherJoinDateTv;
    @BindView(R.id.teacherAddressTv)
    EditText teacherAddressTv;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.submitButtonLl)
    LinearLayout submitButtonLl;
    private Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_teacher_activity);
        ButterKnife.bind(this);
        mActivity = this;
        retrofitDataProvider = new RetrofitDataProvider(this);
        sendButton.setText("Update");
        initToolbar();

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
        teacherJoinDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mActivity, date, myCalendar
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

        teacherJoinDateTv.setText(sdf.format(myCalendar.getTime()));
    }

    private void submitData() {
        Utilz.showDailog(mActivity, getResources().getString(R.string.pleasewait));
        String teacherId = teacherIdTv.getText().toString().trim();
        String teacherName = teacherNameTv.getText().toString().trim();
        String qualification = teacherQualTv.getText().toString().trim();
        String mobile = teacherPhoneTv.getText().toString().trim();
        String alternatenumber = teacherAltPhomeTv.getText().toString().trim();
        String emailid = teacherEmailTv.getText().toString().trim();
        String classteacherOf = teacherOfClassTv.getText().toString().trim();
        String joindate = teacherJoinDateTv.getText().toString().trim();
        String address = teacherAddressTv.getText().toString().trim();
        retrofitDataProvider.updateTeacher(teacherId, teacherName, mobile, alternatenumber, emailid, address, qualification, classteacherOf, joindate,
                new DownlodableCallback<CommonResponse>() {
                    @Override
                    public void onSuccess(final CommonResponse result) {
                        //  closeDialog();
                        Utilz.closeDialog();
                        Toast.makeText(mActivity, "Updated Successfully", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.submitButtonLl)
    public void onViewClicked() {
        if (Utilz.isInternetConnected(mActivity)) {
            submitData();
        } else {
            Toast.makeText(mActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
