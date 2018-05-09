package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.preference.MyPreference;

/**
 * Created by Welcome on 2/12/2018.
 */

public class SeeAttendenceActivity extends AppCompatActivity {
    private String TAG = SeeAttendenceActivity.class.getSimpleName();
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.totalStudentCount)
    TextView totalStudentCount;
    @BindView(R.id.presentStudentCount)
    TextView presentStudentCount;
    @BindView(R.id.absentStudentCount)
    TextView absentStudentCount;
    private Activity mActivity;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_attendence_activity);
        mActivity = this;
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null && getIntent().hasExtra("ClassName"))
            className = getIntent().getStringExtra("ClassName");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("See Attendence " + className);

        manageCalenderView();

        getStudentsAttendance(12);
    }

    private void manageCalenderView() {
        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.logo));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, 2);
        events.add(new EventDay(calendar1, R.drawable.logo));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 5);
        events.add(new EventDay(calendar2, R.drawable.logo));

        com.applandeo.materialcalendarview.CalendarView calendarView = findViewById(R.id.calendarView);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -3);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 0);

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);

        calendarView.setEvents(events);


    }

    private void getStudentsAttendance(int date) {
        //get attendence of that particular date
        if (Utilz.isOnline(mActivity)) {

            if (MyPreference.getLoginedAs().equalsIgnoreCase(AppConstants.STUDENT)) {

            } else {
                //Show here present students count and absent students count for example
                manageAbsentPresentCount(15, 2);
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    public void manageAbsentPresentCount(int mIntTotalStudentCount, int mIntAbsentStudentCount) {
        if (totalStudentCount != null)
            totalStudentCount.setText(mIntTotalStudentCount + "\nTotal Students");
        if (presentStudentCount != null)
            presentStudentCount.setText((mIntTotalStudentCount - mIntAbsentStudentCount) + "\nPresent");
        if (absentStudentCount != null)
            absentStudentCount.setText(mIntAbsentStudentCount + "\nAbsent");
    }
}
