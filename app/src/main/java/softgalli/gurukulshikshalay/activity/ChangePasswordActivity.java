package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
    @BindView(R.id.newPasswordEt)
    EditText newPasswordEt;
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
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.updateButtonLl)
    LinearLayout updateButtonLl;
    @BindView(R.id.oldPasswordEt)
    EditText oldPasswordEt;
    @BindView(R.id.confirmPasswordEt)
    EditText confirmPasswordEt;
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
        sendButton.setText(mActivity.getResources().getString(R.string.change_password));

        oldPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPasswordEt.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        newPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordEt.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        confirmPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordEt.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        //Hide show password
        oldPasswordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utilz.passIconTouchListener(mActivity, oldPasswordEt, motionEvent);
                return false;
            }
        });
        //Hide show password
        newPasswordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utilz.passIconTouchListener(mActivity, newPasswordEt, motionEvent);
                return false;
            }
        });
        //Hide show password
        confirmPasswordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utilz.passIconTouchListener(mActivity, confirmPasswordEt, motionEvent);
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
            userPhoneNumber = "+91-" + userPhoneNumber;
        regPhoneNoTv.setText(String.format(mActivity.getResources().getString(R.string.registered_phone_no), userPhoneNumber));

    }

    @OnClick({R.id.updateButtonLl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updateButtonLl:
                String oldPassword = oldPasswordEt.getText().toString().trim();
                String newPassword = newPasswordEt.getText().toString().trim();
                String confirmPassword = confirmPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(mActivity, R.string.please_enter_old_password, Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(mActivity, R.string.please_enter_new_password, Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(mActivity, R.string.please_enter_confirm_password, Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(mActivity, R.string.newpassword_and_confirmpassword_must_be_same, Toast.LENGTH_SHORT).show();
                } else {
                    //Calling change password API
                    if (Utilz.isOnline(mActivity)) {
                        callingChangePasswordAPI(oldPassword, newPassword);
                    } else {
                        Utilz.showNoInternetConnectionDialog(mActivity);
                    }
                }
                break;
        }
    }

    private void callingChangePasswordAPI(String oldPassword, String newPassword) {
        String loginType = ClsGeneral.getStrPreferences(AppConstants.LOGIN_AS),
                userId = ClsGeneral.getStrPreferences(AppConstants.USER_ID);
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.changePassword(userId, loginType, oldPassword, newPassword, new DownlodableCallback<CommonResponse>() {
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
}
