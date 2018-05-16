package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.SeeLeaveListAdapter;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.RequestedLeaveDataModel;
import softgalli.gurukulshikshalay.model.RequestedLeaveModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class SeeLeaveListActivity extends AppCompatActivity {

    Activity mActivity;
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.noRecordFoundTv)
    TextView noRecordFoundTv;
    @BindView(R.id.mainCardView)
    CardView mainCardView;
    @BindView(R.id.noRecordFoundCardView)
    CardView noRecordFoundCardView;
    private ArrayList<RequestedLeaveDataModel> mLeavesArrayList = new ArrayList();
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_list_activity);
        mActivity = this;
        ButterKnife.bind(this);
        noRecordFoundTv.setText(mActivity.getResources().getString(R.string.no_leave_requested_till_now));
        retrofitDataProvider = new RetrofitDataProvider(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initToolbar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilz.isOnline(mActivity)) {
            getRequestedLeaveList();
        } else {
            Toast.makeText(mActivity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void getRequestedLeaveList() {
        Utilz.showDailog(mActivity, getResources().getString(R.string.pleasewait));
        String loginedAs = ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS);
        String userId = ClsGeneral.getStrPreferences(AppConstants.USER_ID);
        retrofitDataProvider.requestedLeaveList(loginedAs, userId, new DownlodableCallback<RequestedLeaveModel>() {
            @Override
            public void onSuccess(final RequestedLeaveModel result) {
                Utilz.closeDialog();
                if (result != null && result.getStatus().contains(PreferenceName.TRUE)) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mLeavesArrayList.clear();
                        mLeavesArrayList.addAll(result.getData());
                    }
                    if (mLeavesArrayList != null && mLeavesArrayList.size() > 0) {
                        noRecordFoundCardView.setVisibility(View.GONE);
                        mainCardView.setVisibility(View.VISIBLE);

                        recyclerView.setAdapter(new SeeLeaveListAdapter(mActivity, mLeavesArrayList, new OnClickListener() {
                            @Override
                            public void onClick(int position) {
                                Intent mIntent = new Intent(mActivity, LeaveAppDisappActivity.class);
                                mIntent.putExtra(AppConstants.LEAVE_MODEL, mLeavesArrayList);
                                mIntent.putExtra(AppConstants.POSITION, position);
                                startActivity(mIntent);
                            }
                        }));
                    } else {
                        noRecordFoundCardView.setVisibility(View.VISIBLE);
                        mainCardView.setVisibility(View.GONE);
                    }
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

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Leave List");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
