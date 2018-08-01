package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.NoticeBoardAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.EventsAndNoticeLisrModel;
import softgalli.gurukulshikshalay.model.NotificationDateList;
import softgalli.gurukulshikshalay.model.UpcomingActivityModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NoticeBoardActivity extends AppCompatActivity {
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Activity mActivity;
    private static final String TAG = NoticeBoardActivity.class.getSimpleName();
    private ArrayList<NotificationDateList> notification_list;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board_activity);
        mActivity = this;
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notification_list = new ArrayList<>();

        initWidgit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilz.isOnline(mActivity)) {
            getNotificationList();
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void getNotificationList() {
        final ProgressDialog dialog = ProgressDialog.show(mActivity, "", getString(R.string.pleasewait), true);
        dialog.setCancelable(false);
        retrofitDataProvider.getEventsOrNoticeList(false, new DownlodableCallback<EventsAndNoticeLisrModel>() {
            @Override
            public void onSuccess(final EventsAndNoticeLisrModel result) {
                dialog.dismiss();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    ArrayList<UpcomingActivityModel> upcomingEventsArrayList = result.getData();
                    if (!upcomingEventsArrayList.isEmpty())
                        recyclerView.setAdapter(new NoticeBoardAdapter(mActivity, upcomingEventsArrayList));
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.something_went_wrong_error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                dialog.dismiss();
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.something_went_wrong_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.notice_board));
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
