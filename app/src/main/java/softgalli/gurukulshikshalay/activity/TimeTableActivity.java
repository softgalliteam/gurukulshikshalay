package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apextechies.eretort.adapter.TimeTableAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.TimeTableDataModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

/**
 * Created by Shankar on 3/22/2018.
 */

public class TimeTableActivity extends AppCompatActivity {
    @BindView(R.id.classNameSpinner)
    Spinner classNameSpinner;
    @BindView(R.id.sectionNameSpinner)
    Spinner sectionNameSpinner;
    @BindView(R.id.dateTv)
    TextView dateTv;
    @BindView(R.id.tv_monday)
    TextView tvMonday;
    @BindView(R.id.tv_tuesday)
    TextView tvTuesday;
    @BindView(R.id.tv_wednesday)
    TextView tvWednesday;
    @BindView(R.id.tv_thursday)
    TextView tvThursday;
    @BindView(R.id.tv_friday)
    TextView tvFriday;
    @BindView(R.id.tv_saturday)
    TextView tvSaturday;
    @BindView(R.id.rv_common)
    RecyclerView mTimeTableRecyclerView;
    ArrayList<TimeTableDataModel> tableModels = new ArrayList<>();
    @BindView(R.id.classSectionLl)
    LinearLayout classSectionLl;
    private TimeTableAdapter timeTableAdapter;
    private List<String> dateList;
    private int datePos;
    private int mYear, mMonth, mDay;
    private String mStrClass = "", mStrSection = "";
    private Activity mActivity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        ButterKnife.bind(this);
        mActivity = this;

        initToolbar();

        manageSelectingClassAndSec();

        getTodayday();

        initWidgit();

        callTimeTableApi(dateList.get(datePos));
    }

    private void manageSelectingClassAndSec() {
        if (AppConstants.STUDENT.equalsIgnoreCase(MyPreference.getLoginedAs())) {
            classSectionLl.setVisibility(View.GONE);
            mStrClass = ClsGeneral.getStrPreferences(AppConstants.CLAS);
            mStrSection = ClsGeneral.getStrPreferences(AppConstants.SEC);
            if (TextUtils.isEmpty(mStrClass) || TextUtils.isEmpty(mStrSection)) {
                makeVisibleClassAndSecToSelect();
            }
        } else {
            makeVisibleClassAndSecToSelect();
        }
    }

    private void makeVisibleClassAndSecToSelect() {
        classSectionLl.setVisibility(View.VISIBLE);
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
        classNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mStrClass = Utilz.getSelectedClass(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sectionNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mStrClass = Utilz.getSelectedSection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void openDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateTv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.text_timetable));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getTodayday() {
        String dayOfTheWeek = Utilz.todaysday(TimeTableActivity.this);
        if (dayOfTheWeek.equalsIgnoreCase("Monday")) {
            tvMonday.setBackgroundResource(R.drawable.circle);
            tvMonday.setTextColor(getResources().getColor(R.color.colorTextWhite));
            datePos = 0;
        } else if (dayOfTheWeek.equalsIgnoreCase("Tuesday")) {
            tvTuesday.setBackgroundResource(R.drawable.circle);
            tvTuesday.setTextColor(getResources().getColor(R.color.colorTextWhite));
            datePos = 1;
        } else if (dayOfTheWeek.equalsIgnoreCase("Wednesday")) {
            tvWednesday.setBackgroundResource(R.drawable.circle);
            tvWednesday.setTextColor(getResources().getColor(R.color.colorTextWhite));
            datePos = 2;
        } else if (dayOfTheWeek.equalsIgnoreCase("Thursday")) {
            tvThursday.setBackgroundResource(R.drawable.circle);
            tvThursday.setTextColor(getResources().getColor(R.color.colorTextWhite));
            datePos = 3;
        } else if (dayOfTheWeek.equalsIgnoreCase("Friday")) {
            tvFriday.setBackgroundResource(R.drawable.circle);
            tvFriday.setTextColor(getResources().getColor(R.color.colorTextWhite));
            datePos = 4;
        } else if (dayOfTheWeek.equalsIgnoreCase("Saturday")) {
            tvSaturday.setBackgroundResource(R.drawable.circle);
            tvSaturday.setTextColor(getResources().getColor(R.color.colorTextWhite));
            datePos = 5;
        }

    }

    private void callTimeTableApi(String mStrDate) {
        if (TextUtils.isEmpty(mStrClass)) {
            Toast.makeText(mActivity, "Please select class", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mStrSection)) {
            Toast.makeText(mActivity, "Please select section", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mStrDate)) {
            Toast.makeText(mActivity, "Please select a date", Toast.LENGTH_SHORT).show();
        } else {
            Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
            final RequestParams params = new RequestParams();
            params.add("class", "7");
            params.add("sec", "A");
            params.add("date", mStrDate);
            String finalReqUrl = ApiUrl.MAIN_URL + ApiUrl.TIME_TABLE;
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(mActivity, finalReqUrl, params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    Utilz.closeDialog();
                    if (response != null && response.length() > 0) {
                        tableModels.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optString("status").equals("true")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    tableModels.add(new TimeTableDataModel(jo.optString("id"), jo.optString("schoolName"),
                                            jo.optString("class"), jo.optString("sec"), jo.optString("date"),
                                            jo.optString("from_time"), jo.optString("to_time"), jo.optString("subject"), jo.optString("teacher_name")));
                                }
                                timeTableAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(TimeTableActivity.this, "" + object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Utilz.closeDialog();
                    Toast.makeText(mActivity, getString(R.string.something_went_wrong_error_message), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initWidgit() {

        mTimeTableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeTableAdapter = new TimeTableAdapter(TimeTableActivity.this, tableModels, R.layout.timetable_row, new OnClickListener() {
            @Override
            public void onClick(int pos) {

            }
        });
        mTimeTableRecyclerView.setAdapter(timeTableAdapter);

        dateList = Utilz.getCalendar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeBackNText(TextView tv_day) {
        tvMonday.setBackgroundResource(R.drawable.circle);
        tvMonday.setTextColor(getResources().getColor(R.color.textColor));

        tvTuesday.setBackgroundResource(R.drawable.circle);
        tvTuesday.setTextColor(getResources().getColor(R.color.textColor));

        tvWednesday.setBackgroundResource(R.drawable.circle);
        tvWednesday.setTextColor(getResources().getColor(R.color.textColor));

        tvThursday.setBackgroundResource(R.drawable.circle);
        tvThursday.setTextColor(getResources().getColor(R.color.textColor));

        tvFriday.setBackgroundResource(R.drawable.circle);
        tvFriday.setTextColor(getResources().getColor(R.color.textColor));

        tvSaturday.setBackgroundResource(R.drawable.circle);
        tvSaturday.setTextColor(getResources().getColor(R.color.textColor));

        tv_day.setBackgroundResource(R.drawable.ss_hollow_rect_green);
        tv_day.setTextColor(getResources().getColor(R.color.colorAccent));

    }

    @OnClick({R.id.dateTv, R.id.tv_monday, R.id.tv_tuesday, R.id.tv_wednesday, R.id.tv_thursday, R.id.tv_friday, R.id.tv_saturday})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_monday:
                changeBackNText(tvMonday);
                callTimeTableApi(dateList.get(0));
                break;
            case R.id.tv_tuesday:
                changeBackNText(tvTuesday);
                callTimeTableApi(dateList.get(1));
                break;
            case R.id.tv_wednesday:
                changeBackNText(tvWednesday);
                callTimeTableApi(dateList.get(2));
                break;
            case R.id.tv_thursday:
                changeBackNText(tvThursday);
                callTimeTableApi(dateList.get(3));
                break;
            case R.id.tv_friday:
                changeBackNText(tvFriday);
                callTimeTableApi(dateList.get(4));
                break;
            case R.id.tv_saturday:
                changeBackNText(tvSaturday);
                callTimeTableApi(dateList.get(5));
                break;
            case R.id.dateTv:
                openDatePicker();
                break;
        }
    }

}

