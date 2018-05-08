package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.StudentDetailsDataModel;
import softgalli.gurukulshikshalay.model.TeacherListDataModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class LoginScreenActivity extends AppCompatActivity implements KenBurnsView.TransitionListener {
    ProgressDialog dialog;
    EditText userId, password;
    EditText newPassword;
    EditText emailIDFP;
    Button senndOTP;
    RelativeLayout otpRL;
    boolean isNotification = false, isWelcomeNotification = false;
    Activity mActivity;
    private static final int TRANSITIONS_TO_SWITCH = 3;
    private ViewSwitcher mViewSwitcher;
    private int mTransitionsCount = 0;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_activity);
        mActivity = this;
        retrofitDataProvider = new RetrofitDataProvider(this);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

        KenBurnsView img1 = (KenBurnsView) findViewById(R.id.img1);
        img1.setTransitionListener(this);

        KenBurnsView img2 = (KenBurnsView) findViewById(R.id.img2);
        img2.setTransitionListener(this);

    }

    public void skipLoginSignup(View view) {
        MyPreference.setSignupSkipped(true);
        startActivity(new Intent(mActivity, HomeScreenActivity.class));
        this.finish();
    }

    public void principalLoginClick(View view) {
        loginDialog(AppConstants.PRINCIPAL);
    }

    public void teacherLoginClick(View view) {
        loginDialog(AppConstants.TEACHER);
    }

    public void studentLoginClick(View view) {
        loginDialog(AppConstants.STUDENT);
    }

    public void loginDialog(final String loginAs) {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.login_dialog);
            dialog.setTitle(null);
            dialog.setCanceledOnTouchOutside(true);


            userId = (EditText) dialog.findViewById(R.id.userId);
            password = (EditText) dialog.findViewById(R.id.password);


            (dialog.findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utilz.isOnline(mActivity)) {
                        if (checkValidation()) {
                            dialog.dismiss();
                            if (loginAs.equalsIgnoreCase(AppConstants.PRINCIPAL))
                                principalLogin(AppConstants.PRINCIPAL, userId.getText().toString().trim(), password.getText().toString().trim());
                            else if (loginAs.equalsIgnoreCase(AppConstants.TEACHER))
                                teacherLogin(AppConstants.TEACHER, userId.getText().toString().trim(), password.getText().toString().trim());
                            else
                                studentLogin(AppConstants.STUDENT, userId.getText().toString().trim(), password.getText().toString().trim());
                        }
                    } else {
                        Toast.makeText(mActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            (dialog.findViewById(R.id.forgotPassword)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    //forgotPassword();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void principalLogin(final String loginAs, final String userIdStr, final String passwordStr) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.teacherLogin(loginAs, userIdStr, passwordStr, Utilz.getCurrentDate(), new DownlodableCallback<TeacherListDataModel>() {
            @Override
            public void onSuccess(final TeacherListDataModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    MyPreference.setLoginedAs(loginAs);
                    Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_LONG).show();
                    saveTeacherDetailsLocally(result);
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

    private void saveTeacherDetailsLocally(TeacherListDataModel result) {

    }

    public void teacherLogin(final String loginAs, final String userIdStr, final String passwordStr) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.teacherLogin(loginAs, userIdStr, passwordStr, Utilz.getCurrentDate(), new DownlodableCallback<TeacherListDataModel>() {
            @Override
            public void onSuccess(final TeacherListDataModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    MyPreference.setLoginedAs(loginAs);
                    Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_LONG).show();
                    saveTeacherDetailsLocally(result);
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

    public void studentLogin(final String loginAs, final String userIdStr, final String passwordStr) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.studentLogin(loginAs, userIdStr, passwordStr, Utilz.getCurrentDate(), new DownlodableCallback<StudentDetailsDataModel>() {
            @Override
            public void onSuccess(final StudentDetailsDataModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    MyPreference.setLoginedAs(loginAs);
                    Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_LONG).show();
                    saveStudentDetailsLocally(result);
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

    private void saveStudentDetailsLocally(StudentDetailsDataModel result) {

    }

    public boolean checkValidation() {
        boolean ret = true;
        if (userId.getText().toString().trim().isEmpty()) {
            Toast.makeText(mActivity, "Please enter mobile no", Toast.LENGTH_LONG).show();
            ret = false;
        } else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(mActivity, "Please enter password", Toast.LENGTH_LONG).show();
            ret = false;
        }
        return ret;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }


    @Override
    public void onTransitionEnd(Transition transition) {
        mTransitionsCount++;
        if (mTransitionsCount == TRANSITIONS_TO_SWITCH) {
            mViewSwitcher.showNext();
            mTransitionsCount = 0;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
        currentImage.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
        currentImage.resume();
    }
}