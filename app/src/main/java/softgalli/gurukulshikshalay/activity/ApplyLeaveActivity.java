package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import softgalli.gurukulshikshalay.adapter.ClassTeacherAdapter;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.common.Validation;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.TeacherListDataModel;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;


/**
 * Created by Welcome on 2/1/2018.
 */

public class ApplyLeaveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.sname)
    EditText studentName;
    @BindView(R.id.fromDateTv)
    TextView fromDateTv;
    @BindView(R.id.toDateTv)
    TextView toDateTv;
    @BindView(R.id.classTeacherNameSpinner)
    Spinner classTeacherNameSpinner;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.submitButtonLl)
    LinearLayout submitButtonLl;
    private RetrofitDataProvider retrofitDataProvider;
    ArrayList<TeacherListDataModel> teachersArrayList = new ArrayList<>();
    private Activity mActivity;
    private String mStrClassTeacherId = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.apply_leave_activity);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
        mActivity = this;
        // Spinner click listener
        classTeacherNameSpinner.setOnItemSelectedListener(this);
        studentName.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        initToolbar();
        //get teacher list
        if (Utilz.isOnline(mActivity)) {
            getTeacherList();
        } else {
            Toast.makeText(mActivity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void getTeacherList() {
        Utilz.showDailog(mActivity, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.teacherList(new DownlodableCallback<TeacherListModel>() {
            @Override
            public void onSuccess(final TeacherListModel result) {
                Utilz.closeDialog();
                if (result != null && result.getStatus().contains(PreferenceName.TRUE)) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        teachersArrayList.clear();
                        TeacherListDataModel model = new TeacherListDataModel();
                        model.setName("Select your class teacher");
                        teachersArrayList.add(model);
                        teachersArrayList.addAll(result.getData());
                    }
                    updateTeacherListInSpinner(teachersArrayList);
                }
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

    private void updateTeacherListInSpinner(ArrayList<TeacherListDataModel> teachersArrayList) {
        if (teachersArrayList != null && teachersArrayList.size() > 0) {
            // Creating adapter for spinner
            ClassTeacherAdapter dataAdapter = new ClassTeacherAdapter(mActivity, teachersArrayList);

            // attaching data adapter to spinner
            classTeacherNameSpinner.setAdapter(dataAdapter);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Apply Leave");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isValidAllFields() {
        clearAllErrors();
        int itemPos = classTeacherNameSpinner.getSelectedItemPosition();
        boolean isValidAllFields;
        if (!Validation.hasText(studentName)) {
            studentName.setError("Please enter student's full name");
            isValidAllFields = false;
        } else if (TextUtils.isEmpty(fromDateTv.getText().toString().trim())) {
            fromDateTv.setError("Please select start date");
            isValidAllFields = false;
        } else if (TextUtils.isEmpty(fromDateTv.getText().toString().trim())) {
            toDateTv.setError("Please select end date");
            isValidAllFields = false;
        } else if (itemPos <= 0) {
            Toast.makeText(mActivity, "Please select your class teacher", Toast.LENGTH_SHORT).show();
            isValidAllFields = false;
        } else if (!Validation.hasText(comment)) {
            comment.setError("Please enter reason for leave");
            isValidAllFields = false;
        } else {
            isValidAllFields = true;
        }
        return isValidAllFields;
    }

    private void clearAllErrors() {
        studentName.setError(null);
        toDateTv.setError(null);
        fromDateTv.setError(null);
    }

    private void applyLeave(String fromDateTvStr, String toDateTvStr, String commentStr) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        String studentId = MyPreference.getUserId();
        if (!TextUtils.isEmpty(studentId)) {
            studentId = ClsGeneral.getStrPreferences(AppConstants.USER_ID);
        }
        retrofitDataProvider.requestLeave(studentId, fromDateTvStr, toDateTvStr, mStrClassTeacherId, commentStr, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
                Toast.makeText(mActivity, R.string.sent_successfully, Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        if (teachersArrayList != null && teachersArrayList.size() > 0 && teachersArrayList.size() > position && position > 0)
            mStrClassTeacherId = teachersArrayList.get(position).getUserId();
        ((TextView) parent.getChildAt(0).findViewById(R.id.teacherName)).setTextColor(ContextCompat.getColor(mActivity, R.color.color_de00000));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick({R.id.fromDateTv, R.id.toDateTv, R.id.submitButtonLl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fromDateTv:
                openDatePicker(true);
                break;
            case R.id.toDateTv:
                openDatePicker(false);
                break;
            case R.id.submitButtonLl:
                String commentStr = comment.getText().toString();
                String fromDateTvStr = fromDateTv.getText().toString();
                String toDateTvStr = toDateTv.getText().toString();
                if (Utilz.isOnline(mActivity)) {
                    if (isValidAllFields()) {
                        applyLeave(fromDateTvStr, toDateTvStr, commentStr);
                    }
                } else {
                    Toast.makeText(mActivity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void openDatePicker(final boolean isFromDate) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (isFromDate) {
                            fromDateTv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        } else {
                            toDateTv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }
                }, mYear, mMonth, mDay);
        if (isFromDate) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else {
            String givenDateString = fromDateTv.getText().toString().trim();
            if (!TextUtils.isEmpty(givenDateString)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                try {
                    Date mDate = sdf.parse(givenDateString);
                    long timeInMilliseconds = mDate.getTime();
                    datePickerDialog.getDatePicker().setMinDate(timeInMilliseconds-1000);
                } catch (ParseException e) {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
            } else
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        datePickerDialog.show();
    }
}
