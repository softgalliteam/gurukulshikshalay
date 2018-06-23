package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.AsteriskPasswordTransformationMethod;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = ChangePasswordActivity.class.getSimpleName();
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    @BindView(R.id.newPasswordEt)
    EditText newPasswordEt;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.userProfilePicIv)
    CircleImageView userProfilePicIv;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.regIdTv)
    TextView regIdTv;
    @BindView(R.id.regPhoneNoTv)
    TextView regPhoneNoTv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.otpSentOnphoneNoMsgTv)
    TextView otpSentOnphoneNoMsgTv;
    @BindView(R.id.otpEt)
    EditText otpEt;
    @BindView(R.id.timerTv)
    TextView timerTv;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.updateButtonLl)
    LinearLayout updateButtonLl;
    private Activity mActivity;
    private String userPhoneNumber;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.change_password_activity);

        ButterKnife.bind(this);
        mActivity = this;
        retrofitDataProvider = new RetrofitDataProvider(this);
        initToolbar();
        initView();
        setAllFields();

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        manageOTPFlow();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.change_password));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        timerTv.setText(AppConstants.GET_OTP_AND_VERIFY);
        updateButtonLl.setVisibility(View.GONE);
        newPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordEt.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        sendButton.setText(mActivity.getResources().getString(R.string.change_password));

        //Hide show password
        newPasswordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utilz.passIconTouchListener(mActivity, newPasswordEt, motionEvent);
                return false;
            }
        });
    }

    private void setAllFields() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);
        requestOptions.fitCenter();
        String profilePicUrl = ClsGeneral.getStrPreferences(AppConstants.PROFILE_PIC);
        if (!TextUtils.isEmpty(profilePicUrl) && !(profilePicUrl.contains(AppConstants.HTTP) || profilePicUrl.contains(AppConstants.WWW))) {
            profilePicUrl = ApiUrl.IMAGE_BASE_URL + profilePicUrl;
        }
        Glide.with(mActivity)
                .load(profilePicUrl)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(userProfilePicIv);
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.NAME))) {
            userNameTv.setText(ClsGeneral.getStrPreferences(AppConstants.NAME));
        } else if (!TextUtils.isEmpty(MyPreference.getUserName())) {
            userNameTv.setText(MyPreference.getUserName());
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.USER_ID))) {
            regIdTv.setText(String.format(mActivity.getResources().getString(R.string.user_id_with_value), ClsGeneral.getStrPreferences(AppConstants.USER_ID)));
        } else if (!TextUtils.isEmpty(MyPreference.getUserId())) {
            regIdTv.setText(String.format(mActivity.getResources().getString(R.string.user_id_with_value), MyPreference.getUserId()));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO))) {
            userPhoneNumber = ClsGeneral.getStrPreferences(AppConstants.PHONE_NO);
        } else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER))) {
            userPhoneNumber = ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER);
        }
        if (!TextUtils.isEmpty(userPhoneNumber) && !userPhoneNumber.contains("+91"))
            userPhoneNumber = "+91" + userPhoneNumber;
        regPhoneNoTv.setText(String.format(mActivity.getResources().getString(R.string.registered_phone_no), userPhoneNumber));

        if (!TextUtils.isEmpty(userPhoneNumber)) {
            otpSentOnphoneNoMsgTv.setText(String.format(mActivity.getResources().getString(R.string.otp_will_be_sent_on_phone_msg), userPhoneNumber));
        }

    }

    @OnClick({R.id.timerTv, R.id.updateButtonLl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timerTv:
                if ((AppConstants.GET_OTP_AND_VERIFY).equalsIgnoreCase(timerTv.getText().toString().trim())) {
                    //Sending OTP to registerd phone number
                    timerTv.setText(AppConstants.SENDING);
                    sendVerificationCode();
                } else if ((AppConstants.RESEND).equalsIgnoreCase(timerTv.getText().toString().trim())) {
                    //Resending OTP to registerd phone number
                    timerTv.setText(AppConstants.SENDING);
                    resendVerificationCode(userPhoneNumber, mResendToken);
                } else if ((AppConstants.VERIFY).equalsIgnoreCase(timerTv.getText().toString().trim())) {
                    //Verifying OTP of phone number
                    timerTv.setText(AppConstants.VERIFYING);
                    verifyOTPCodeNumber();
                }
                break;
            case R.id.updateButtonLl:
                String newPassword = newPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(mActivity, "Please enter your new password", Toast.LENGTH_SHORT).show();
                } else {
                    //Calling change password API
                    if (Utilz.isOnline(mActivity)) {
                        callingChangePasswordAPI(newPassword);
                    } else {
                        Utilz.showNoInternetConnectionDialog(mActivity);
                    }
                }
                break;
        }
    }

    private void callingChangePasswordAPI(String newPassword) {
        String loginType = ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS),
                userId = ClsGeneral.getStrPreferences(AppConstants.USER_ID);
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.changePassword(userId, loginType, newPassword, new DownlodableCallback<CommonResponse>() {
            @Override
            public void onSuccess(final CommonResponse result) {
                Utilz.closeDialog();
                Utilz.showMessageOnDialog(mActivity, mActivity.getString(R.string.success), mActivity.getString(R.string.password_changed_successfully), "", AppConstants.OK);
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

    private void updateUI(String otpCodeStatus) {
        if (timerTv != null) {
            if (otpCodeStatus.equalsIgnoreCase(AppConstants.OTP_SENT)) {
                otpSentOnphoneNoMsgTv.setText(String.format(mActivity.getResources().getString(R.string.otp_sent_on_phone_msg), userPhoneNumber));
                timerTv.setText(AppConstants.VERIFY);
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.otp_sent_successfully), Toast.LENGTH_SHORT).show();
            } else if (otpCodeStatus.equalsIgnoreCase(AppConstants.FAILED_TO_VERIFY)) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.invalide_phone_no_msg), Toast.LENGTH_SHORT).show();
                timerTv.setText(AppConstants.RESEND);
            } else if (otpCodeStatus.equalsIgnoreCase(AppConstants.VERIFIED)) {
                timerTv.setText(AppConstants.VERIFIED);
                otpSentOnphoneNoMsgTv.setText(String.format(mActivity.getResources().getString(R.string.phone_no_verified_successfully), userPhoneNumber));
                if (updateButtonLl != null && newPasswordEt != null) {
                    updateButtonLl.setVisibility(View.VISIBLE);
                    newPasswordEt.setVisibility(View.VISIBLE);
                }
            }
        } else {
            Utilz.showMessageDialog(mActivity, otpCodeStatus);
        }
    }

    private void sendVerificationCode() {
        if (TextUtils.isEmpty(userPhoneNumber)) {
            Utilz.showMessageOnDialog(mActivity, mActivity.getResources().getString(R.string.invalid_phone_no),
                    mActivity.getResources().getString(R.string.invalide_phone_no_msg), "", AppConstants.OK);
        } else {
            startPhoneNumberVerification(userPhoneNumber.trim());
        }
    }

    private void verifyOTPCodeNumber() {
        String code = otpEt.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Utilz.showMessageOnDialog(mActivity, mActivity.getResources().getString(R.string.invalid_otp),
                    mActivity.getResources().getString(R.string.invalide_otp_msg), "", AppConstants.OK);
            return;
        }
        verifyPhoneNumberWithCode(mVerificationId, code);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }


    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    // [END resend_verification]
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            // [START_EXCLUDE]
                            updateUI(AppConstants.VERIFIED);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(AppConstants.FAILED_TO_VERIFY);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void manageOTPFlow() {
// [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.i(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(AppConstants.VERIFIED);
                // [END_EXCLUDE]
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Utilz.showMessageOnDialog(mActivity, mActivity.getResources().getString(R.string.invalid_phone_no),
                            mActivity.getResources().getString(R.string.invalide_phone_no_msg), "", AppConstants.OK);
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "OTP Message Quota Exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(AppConstants.FAILED_TO_VERIFY);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(AppConstants.OTP_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    /* This method will close the keyboard when the user will tap somewhere else on the screen*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
