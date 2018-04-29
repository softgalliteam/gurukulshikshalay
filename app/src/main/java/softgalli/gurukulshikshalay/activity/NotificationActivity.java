package softgalli.gurukulshikshalay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.NotificationAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.NotificationDateList;
import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = "";
    private ArrayList<NotificationDateList> notification_list;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notification_list = new ArrayList<>();

        gwtCurrentDate();
        initWidgit();
        getNotificationList();
    }

    private void gwtCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        Log.e("Current date => ", formattedDate);
    }

    private void getNotificationList() {
        retrofitDataProvider.notification("apexschool_1001", new DownlodableCallback<NotificationModel>() {
            @Override
            public void onSuccess(final NotificationModel result) {
                //  closeDialog();


                if (result.getStatus().contains(PreferenceName.TRUE)) {

                    notification_list = result.getData();
                    setAdapter();

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

    private void setAdapter() {
        recyclerView.setAdapter(new NotificationAdapter(this, notification_list, R.layout.notification_item, new OnClickListener() {
            @Override
            public void onClick(int pos) {

            }
        }));
    }

    private void initWidgit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.text_notification));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
