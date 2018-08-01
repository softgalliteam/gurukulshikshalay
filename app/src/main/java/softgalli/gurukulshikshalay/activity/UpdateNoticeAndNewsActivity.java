package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.UpcomingActivityModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class UpdateNoticeAndNewsActivity extends AppCompatActivity {
    private static final String TAG = UpdateNoticeAndNewsActivity.class.getSimpleName();
    private Activity mActivity;
    private String mApiUrl;
    private EditText mTitleEt, mDescriptionEt;
    private ArrayList<UpcomingActivityModel> mNoticeNeventArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_notice_events_activity);
        mActivity = this;
        mNoticeNeventArrayList = new ArrayList();

        initToolbar();
        initViews();
        getIntentData();
        Utilz.hideKeyBoard(mActivity);
    }

    private void initViews() {
        LinearLayout submitButtonLl = findViewById(R.id.submitButtonLl);
        ((Button) findViewById(R.id.sendButton)).setText(mActivity.getResources().getString(R.string.update));
        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);

        submitButtonLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTitleStr = mTitleEt.getText().toString().trim();
                String mDescriptionStr = mDescriptionEt.getText().toString();

                if (Utilz.isOnline(mActivity)) {
                    if (TextUtils.isEmpty(mTitleStr)) {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.please_enter_title), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(mDescriptionStr)) {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.please_enter_message), Toast.LENGTH_LONG).show();
                    } else {
                        if (mNoticeNeventArrayList != null && mNoticeNeventArrayList.size() > 0) {
                            mNoticeNeventArrayList.get(0).setUpcomingEvetTitle(mTitleStr);
                            mNoticeNeventArrayList.get(0).setUpcomingEvetDescription(mDescriptionStr);
                            String date = Utilz.getCurrentDate();
                            String posted_by = ClsGeneral.getStrPreferences(AppConstants.DESIGNATION) + "\n(" + ClsGeneral.getStrPreferences(AppConstants.NAME) + ")";
                            mNoticeNeventArrayList.get(0).setUpcomingEventPostedBy(posted_by);
                            mNoticeNeventArrayList.get(0).setUpcomingEventDate(date);
                            Utilz.callUpdateDeleteApi(mActivity, mNoticeNeventArrayList.get(0), mApiUrl, true);
                        }
                    }
                } else {
                    Utilz.showNoInternetConnectionDialog(mActivity);
                }
            }
        });
    }

    private void getIntentData() {
        boolean isForNoticeBoard = false;
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.IS_FOR_NOTICE))
                isForNoticeBoard = mBundle.getBoolean(AppConstants.IS_FOR_NOTICE, false);
            if (mBundle.containsKey(AppConstants.ARRAYLIST))
                mNoticeNeventArrayList = (ArrayList<UpcomingActivityModel>) mBundle.getSerializable(AppConstants.ARRAYLIST);
        }
        if (isForNoticeBoard) {
            mApiUrl = ApiUrl.EDIT_DELETE_NOTICE;
            getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.update) +" "+ AppConstants.NOTICE);
        } else {
            mApiUrl = ApiUrl.EDIT_DELETE_EVENT;
            getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.update) +" "+ AppConstants.EVENT);
        }
        if (mNoticeNeventArrayList != null && mNoticeNeventArrayList.size() > 0) {
            updateOnUI(mNoticeNeventArrayList.get(0));
        }
    }

    private void updateOnUI(UpcomingActivityModel upcomingActivityModel) {
        if (upcomingActivityModel != null) {
            mTitleEt.setText(upcomingActivityModel.getUpcomingEvetTitle());
            mDescriptionEt.setText(upcomingActivityModel.getUpcomingEvetDescription());
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.update));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
