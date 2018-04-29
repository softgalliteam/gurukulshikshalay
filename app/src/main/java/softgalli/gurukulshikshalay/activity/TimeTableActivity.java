package softgalli.gurukulshikshalay.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 3/22/2018.
 */

public class TimeTableActivity extends AppCompatActivity {
   /* @BindView(R.id.toolbar)
    Toolbar toolbar;
    private HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        ButterKnife.bind(this);
        initWidgit();
    }

    private void initWidgit() {


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

    *//* homeworkAdapter = new ExamScheduleAdapter(ExamScheduleActivity.this, tableModels, R.layout.timetable_row, new OnClickListener() {
         @Override
         public void onclick(int position) {

         }
     });
     rv_timetable.setAdapter(homeworkAdapter);
*//*
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        *//** start before 1 month from now *//*
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayNameFormat("EEEEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(12.8f, 12.8f, 12.8f)
                .showDayName(true)
                .showMonthName(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(date);
                //   Toast.makeText(ExamScheduleActivity.this, formattedDate + " is selected!", Toast.LENGTH_SHORT).show();
               // callExamScedule(formattedDate);
            }

        });

        horizontalCalendar.goToday(false);


}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }*/
}

