package softgalli.gurukulshikshalay.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.common.Validation;
import softgalli.gurukulshikshalay.model.FeedBackModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;


/**
 * Created by Naveen on 1/18/2016.
 */
public class FeedbackActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    private static final String TAG = FeedbackActivity.class.getSimpleName();
    float selectedRating = 5.0f;
    String selectedIssue = "", errorMessage;
    EditText feedbackText;
    private Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        retrofitDataProvider = new RetrofitDataProvider(this);
        mActivity = this;

        initToolbar();

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        errorMessage = getString(R.string.error_msg);


        TextView submitFeedbackBtn = findViewById(R.id.submit_feedback_btn);
        feedbackText = findViewById(R.id.feedback_Txt);
        final EditText nameTv = findViewById(R.id.nameTv);
        final EditText feedbackEmail = findViewById(R.id.email_Txt);
        final EditText feedbackMobile = findViewById(R.id.mobile_no_Txt);
        ((RatingBar) findViewById(R.id.ratingBar1)).setOnRatingBarChangeListener(this);

        if (MyPreference.isLogined()) {
            feedbackEmail.setText(ClsGeneral.getStrPreferences(AppConstants.EMAIL));
            if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO)))
                feedbackMobile.setText(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO));
            else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER)))
                feedbackMobile.setText(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER));
            nameTv.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        } else {
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
            try {
                feedbackEmail.setText(accounts[0].name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = feedbackText.getText().toString();
                String email = feedbackEmail.getText().toString();
                String mobile = feedbackMobile.getText().toString();
                if (msg.isEmpty()) {
                    msg = selectedIssue;
                }

                Log.i(TAG, "**" + msg + "**" + email + "**" + mobile + "**" + selectedIssue + "**");
                //Toast.makeText(FeedbackActivity.this,  typetxt+"\n" + selectedIssue + "\n" + msg, Toast.LENGTH_LONG).show();
                if (Utilz.isOnline(mActivity)) {
                    if (msg.isEmpty()) {
                        if (errorMessage.equalsIgnoreCase(getString(R.string.error_msg))) {
                            feedbackText.setError(getString(R.string.write_your_feedback_txt));
                        } else {
                            feedbackText.setError(errorMessage);
                        }
                        Toast.makeText(FeedbackActivity.this, R.string.write_your_feedback_txt, Toast.LENGTH_LONG).show();
                    } else {
                        if (Validation.isPhoneNumber(feedbackMobile, true)) {
                            sendFeedback(email, mobile, msg, selectedRating);
                        }
                    }

                } else {
                    Toast.makeText(FeedbackActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void sendFeedback(String email, String mobile, String msg, float selectedRating) {
        Utilz.showDailog(FeedbackActivity.this, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.feedback(email, mobile, msg, "" + selectedRating, Utilz.getCurrentDate(), new DownlodableCallback<FeedBackModel>() {
            @Override
            public void onSuccess(final FeedBackModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    Toast.makeText(mActivity, R.string.sent_successfully, Toast.LENGTH_LONG).show();
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Feedback");
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

    /**
     * NotificationActivity that the rating has changed.
     **/
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        selectedRating = rating;
        //Log.i("rating" , " "+rating);
    }
}

