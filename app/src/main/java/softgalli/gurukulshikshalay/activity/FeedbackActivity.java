package softgalli.gurukulshikshalay.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.common.Validation;
import softgalli.gurukulshikshalay.preference.MyPreference;


/**
 * Created by Naveen on 1/18/2016.
 */
public class FeedbackActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    private static final String TAG = FeedbackActivity.class.getSimpleName();
    float selectedRating = 5.0f;
    String selectedIssue = "", errorMessage;
    EditText feedbackText;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        mActivity = this;
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        errorMessage = getString(R.string.error_msg);


        TextView submitFeedbackBtn = (TextView) findViewById(R.id.submit_feedback_btn);
        feedbackText = (EditText) findViewById(R.id.feedback_Txt);
        final EditText feedbackEmail = (EditText) findViewById(R.id.email_Txt);
        final EditText feedbackMobile = (EditText) findViewById(R.id.mobile_no_Txt);
        ((RatingBar) findViewById(R.id.ratingBar1)).setOnRatingBarChangeListener(this);

        if (MyPreference.isLogined()) {
            feedbackEmail.setText(MyPreference.getEmailId());
            feedbackMobile.setText(MyPreference.getUserMobileNo());
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
                if (MyPreference.isLogined()) {
                    //Toast.makeText(FeedbackActivity.this,  typetxt+"\n" + selectedIssue + "\n" + msg, Toast.LENGTH_LONG).show();
                    if (Utilz.isOnline(mActivity)) {
                        if (msg.isEmpty()) {
                            if (errorMessage.equalsIgnoreCase(getString(R.string.error_msg))) {
                                feedbackText.setError(getString(R.string.write_your_feedback_txt));
                            } else {
                                feedbackText.setError(errorMessage);
                            }
                            //Toast.makeText(FeedbackActivity.this, R.string.write_your_feedback_txt, Toast.LENGTH_LONG).show();
                        } else {
                            if (Validation.isPhoneNumber(feedbackMobile, true)) {
                                //sendFeedback(selectedRating, msg, email, mobile);
                            }
                        }

                    } else {
                        Toast.makeText(FeedbackActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Utilz.showLoginFirstDialog(mActivity);
                }
            }
        });


        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("About Us");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

/*
    private void sendFeedback(float selectedRating, String msg, String email, String mobile) {
        final ProgressDialog dialog = ProgressDialog.show(FeedbackActivity.this, "", getString(R.string.pleasewait), true);
        dialog.setCancelable(false);
        final RequestParams params = new RequestParams();
        params.add("feelings", feel);
        if (selectedRating > 0.0) {
            msg = msg + " \nRating Star : " + selectedRating;
        }
        try {
            String manufacturer = Build.MANUFACTURER;

            msg = msg + " \nPhone Manufacturer : " + manufacturer;

            String model = Build.MODEL;
            msg = msg + " \nPhone Model : " + model;

            String androidOS = Build.VERSION.RELEASE;
            msg = msg + " \nOS Version : " + androidOS;

            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            msg = msg + " \nSSNow Apis Version : " + versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }

        params.add("feedback", msg);
        if (!mobile.isEmpty()) {
            params.add("email", email);
        } else {
            params.add("email", MyPreference.getEmailId());
        }
        if (!mobile.isEmpty()) {
            params.add("mobile_no", mobile);
        } else {
            params.add("mobile_no", MyPreference.getUserMobileNo());
        }
        Log.i("Feedback : ", "  selectedRating:" + selectedRating + "  msg:" + msg);
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.get(getApplicationContext(), Apis.SEND_FEEDBACK, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                dialog.dismiss();
                Log.i("TAG", statusCode + "Response : " + responseString);
                try {
                    if (statusCode == 200) {
                        Toast.makeText(FeedbackActivity.this, R.string.feedback_sent_successfully, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(FeedbackActivity.this, R.string.failed_to_send_feedback, Toast.LENGTH_LONG).show();
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
                Toast.makeText(FeedbackActivity.this, R.string.feedback_sent_successfully, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
*/

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

