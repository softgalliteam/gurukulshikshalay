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
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.preference.MyPreference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_activity);
        mActivity = this;
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

    public void principalLogin(View view) {
        loginDialog("Principal");
    }

    public void teacherLogin(View view) {
        loginDialog("Teacher");
    }

    public void studentLogin(View view) {
        loginDialog("Student");
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
                            login(loginAs);
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


    public void login(final String loginAs) {
        if (Utilz.isOnline(mActivity)) {
            if (checkValidation()) {
                //loginFromServer();
                Toast.makeText(mActivity, "User Id or Password is incorrect, Please contact to your principal.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
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