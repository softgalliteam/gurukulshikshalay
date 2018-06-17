package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.common.Validation;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;


/**
 * Created by Welcome on 2/26/2017.
 */
public class AdmissionActivity extends AppCompatActivity {
    Activity mActivity;
    @BindView(R.id.sname)
    EditText sname;
    @BindView(R.id.phoneNo)
    EditText phoneNo;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.className)
    EditText className;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.submitButtonLl)
    LinearLayout submitButtonLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.admission_form_activity);
        ButterKnife.bind(this);
        mActivity = this;

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Take Admission");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.submitButtonLl)
    public void onViewClicked() {
        String commentStr = comment.getText().toString();
        String userEmail = emailId.getText().toString();
        String userMobile = phoneNo.getText().toString();
        String userName = sname.getText().toString();
        String classNameStr = className.getText().toString();

        //Toast.makeText(mActivity,  typetxt+"\n" + selectedIssue + "\n" + msg, Toast.LENGTH_LONG).show();
        if (Utilz.isOnline(mActivity)) {
            if (isValidAllFields()) {
                takeAdmissionOnline(userName, userMobile, userEmail, classNameStr, commentStr);
            }
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private boolean isValidAllFields() {
        clearAllErrors();
        boolean isValidAllFields = true;
        if (!Validation.hasText(sname)) {
            sname.setError("Please enter student's full name");
            isValidAllFields = false;
        } else if (!Validation.isPhoneNumber(phoneNo, true)) {
            phoneNo.setError("Please enter your valid phone number.");
            isValidAllFields = false;
        } else if (!Validation.isEmailAddress(emailId, true)) {
            emailId.setError("Please enter your email id.");
            isValidAllFields = false;
        } else if (!Validation.hasText(className)) {
            className.setError("Please enter your class name.");
            isValidAllFields = false;
        } else {
            isValidAllFields = true;
        }
        return isValidAllFields;
    }

    private void clearAllErrors() {
        sname.setError(null);
        phoneNo.setError(null);
        emailId.setError(null);
        className.setError(null);
    }


    private void takeAdmissionOnline(String userName, String userMobile, String userEmail, String classNameStr, String commentStr) {
        final ProgressDialog dialog = ProgressDialog.show(mActivity, "", getString(R.string.pleasewait), true);
        dialog.setCancelable(false);
        final RequestParams params = new RequestParams();
        params.add("name", userName);
        params.add("phone", userMobile);
        params.add("email", userEmail);
        params.add("class", classNameStr);
        params.add("address", commentStr);
        String finalReqUrl = ApiUrl.MAIN_URL + ApiUrl.ADMISSION_API;
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(mActivity, finalReqUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                dialog.dismiss();
                Log.i("TAG", statusCode + "Response : " + responseString);
                try {
                    if (statusCode == 200) {
                        Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.admission_successfully), "", AppConstants.OK);
                        emptyAllFields();
                    } else {
                        Toast.makeText(mActivity, R.string.something_went_wrong_error_message, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(mActivity, getString(R.string.something_went_wrong_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void emptyAllFields() {
        sname.setText("");
        phoneNo.setText("");
        emailId.setText("");
        className.setText("");
        comment.setText("");
    }
}
