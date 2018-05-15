package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.RequestedLeaveDataModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class LeaveAppDisappActivity extends AppCompatActivity {
    @BindView(R.id.sname)
    EditText sname;
    @BindView(R.id.fromDateTv)
    TextView fromDateTv;
    @BindView(R.id.toDateTv)
    TextView toDateTv;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.teacherComment)
    EditText teacherComment;
    @BindView(R.id.approveButton)
    Button approveButton;
    @BindView(R.id.disApproveButton)
    Button disApproveButton;
    @BindView(R.id.buttonContainerCardView)
    CardView buttonContainerCardView;
    private RetrofitDataProvider retrofitDataProvider;
    private int mPosition;
    private ArrayList<RequestedLeaveDataModel> mLeavesArrayList = new ArrayList<>();
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.leave_app_disapp_activity);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
        mActivity = this;
        initToolbar();
        getIntentsData();
        if (AppConstants.LOGIN_AS.equalsIgnoreCase(ClsGeneral.getStrPreferences(AppConstants.STUDENT))) {
            teacherComment.setClickable(false);
            teacherComment.setFocusable(false);
        } else {
            teacherComment.setClickable(true);
            teacherComment.setFocusable(true);
        }

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Approve/Disapprove Leave");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getIntentsData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.POSITION))
                mPosition = mBundle.getInt(AppConstants.POSITION);
            if (mBundle.containsKey(AppConstants.TEACHER_DETAILS))
                mLeavesArrayList = (ArrayList<RequestedLeaveDataModel>) mBundle.getSerializable(AppConstants.LEAVE_MODEL);
        }
        if (mLeavesArrayList != null && mLeavesArrayList.size() > 0 && mLeavesArrayList.get(mPosition) != null) {
            updateOnUI(mLeavesArrayList.get(mPosition));
        }
    }

    private void updateOnUI(RequestedLeaveDataModel requestedLeaveDataModel) {
        if (ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS).equalsIgnoreCase(AppConstants.STUDENT)) {
            buttonContainerCardView.setVisibility(View.GONE);
            teacherComment.setVisibility(View.GONE);
        } else {
            buttonContainerCardView.setVisibility(View.VISIBLE);
            teacherComment.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(requestedLeaveDataModel.getUserName()))
            sname.setText(requestedLeaveDataModel.getUserName());
        else
            sname.setText(mActivity.getResources().getString(R.string.na));
        if (!TextUtils.isEmpty(requestedLeaveDataModel.getFrom_date()))
            fromDateTv.setText(requestedLeaveDataModel.getFrom_date());
        else
            fromDateTv.setText(mActivity.getResources().getString(R.string.na));
        if (!TextUtils.isEmpty(requestedLeaveDataModel.getTo_date()))
            toDateTv.setText(requestedLeaveDataModel.getTo_date());
        else
            toDateTv.setText(mActivity.getResources().getString(R.string.na));

        if (!TextUtils.isEmpty(requestedLeaveDataModel.getDescription()))
            comment.setText(requestedLeaveDataModel.getDescription());
        else
            comment.setText(mActivity.getResources().getString(R.string.na));

        if (!TextUtils.isEmpty(requestedLeaveDataModel.getTeacherCommeent()))
            teacherComment.setText(requestedLeaveDataModel.getTeacherCommeent());
        else
            teacherComment.setText(mActivity.getResources().getString(R.string.na));
    }

    private void appDisappLeave(String status, String commentStr) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        String userId = MyPreference.getUserId();
        if (!TextUtils.isEmpty(userId)) {
            userId = ClsGeneral.getStrPreferences(AppConstants.USER_ID);
        }
        retrofitDataProvider.updateRequestedLeave(status, userId, commentStr, new DownlodableCallback<CommonResponse>() {
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

    @OnClick({R.id.approveButton, R.id.disApproveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.approveButton:
                String teacherCommentStr = teacherComment.getText().toString().trim();
                if (TextUtils.isEmpty(teacherCommentStr))
                    Toast.makeText(mActivity, "Please enter your comment", Toast.LENGTH_SHORT).show();
                else appDisappLeave(1 + "", teacherCommentStr);
                break;
            case R.id.disApproveButton:
                String teacherCommentStr1 = teacherComment.getText().toString().trim();
                if (TextUtils.isEmpty(teacherCommentStr1))
                    Toast.makeText(mActivity, "Please enter your comment", Toast.LENGTH_SHORT).show();
                else appDisappLeave(2 + "", teacherCommentStr1);
                break;
        }
    }
}