package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.UserDetailsDataModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class ViewTeacherProfileActivity extends AppCompatActivity {
    @BindView(R.id.userProfilePicIv)
    CircleImageView userProfilePicIv;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.userDesignationTv)
    TextView userDesignationTv;
    @BindView(R.id.userPhoneTv)
    TextView userPhoneTv;
    @BindView(R.id.callButton)
    Button callButton;
    @BindView(R.id.userEmailIdTv)
    TextView userEmailIdTv;
    @BindView(R.id.userQualificationTv)
    TextView userQualificationTv;
    @BindView(R.id.userTeachingTv)
    TextView userTeachingTv;
    @BindView(R.id.userAddressTv)
    TextView userAddressTv;
    private String TAG = ViewTeacherProfileActivity.class.getSimpleName();
    private Activity mActivity;
    private int mPosition;
    private ArrayList<UserDetailsDataModel> mTeachersArrayList = new ArrayList<>();
    boolean isToShowEditProfileIcon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_teacher_profile_activity);
        ButterKnife.bind(this);
        mActivity = this;
        initToolbar();
        getIntentData();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("My Profile");
    }

    private void getIntentData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(AppConstants.POSITION))
                mPosition = mBundle.getInt(AppConstants.POSITION);
            if (mBundle.containsKey(AppConstants.TEACHER_DETAILS))
                mTeachersArrayList = (ArrayList<UserDetailsDataModel>) mBundle.getSerializable(AppConstants.TEACHER_DETAILS);
        }
        if (mTeachersArrayList != null && mTeachersArrayList.size() > 0) {
            updateOnUI(mTeachersArrayList.get(mPosition));
            isToShowEditProfileIcon = false;
        } else {
            updateOnUICachedDetails();
            isToShowEditProfileIcon = true;
        }
    }

    private void updateOnUI(UserDetailsDataModel mTeacherListDataModel) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);
        requestOptions.fitCenter();
        Glide.with(mActivity)
                .load(ApiUrl.IMAGE_BASE_URL + mTeacherListDataModel.getProfile_pic())
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
        if (!TextUtils.isEmpty(mTeacherListDataModel.getName())) {
            userNameTv.setText(mTeacherListDataModel.getName());
        } else {
            userNameTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(mTeacherListDataModel.getDesignation())) {
            userDesignationTv.setText(mTeacherListDataModel.getDesignation());
        } else {
            userDesignationTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(mTeacherListDataModel.getMobile())) {
            userPhoneTv.setText(mTeacherListDataModel.getMobile());
        } else if (!TextUtils.isEmpty(mTeacherListDataModel.getAlternate_number())) {
            userPhoneTv.setText(mTeacherListDataModel.getAlternate_number());
        } else {
            userPhoneTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(mTeacherListDataModel.getEmail())) {
            userEmailIdTv.setText(mTeacherListDataModel.getEmail());
        } else {
            userEmailIdTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(mTeacherListDataModel.getQualification())) {
            userQualificationTv.setText(mTeacherListDataModel.getQualification());
        } else {
            userQualificationTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(mTeacherListDataModel.getWhat_teach())) {
            userTeachingTv.setText(mTeacherListDataModel.getWhat_teach());
        } else {
            userTeachingTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(mTeacherListDataModel.getAddress())) {
            userAddressTv.setText(mTeacherListDataModel.getAddress());
        } else {
            userAddressTv.setText(mActivity.getResources().getString(R.string.na));
        }
    }


    private void updateOnUICachedDetails() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);
        requestOptions.fitCenter();
        String imageUrl = ApiUrl.IMAGE_BASE_URL + ClsGeneral.getStrPreferences(AppConstants.PROFILE_PIC);
        Glide.with(mActivity)
                .load(imageUrl)
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
        } else {
            userNameTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.NAME))) {
            userDesignationTv.setText(ClsGeneral.getStrPreferences(AppConstants.DESIGNATION));
        } else {
            userDesignationTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO))) {
            userPhoneTv.setText(ClsGeneral.getStrPreferences(AppConstants.PHONE_NO));
        } else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER))) {
            userPhoneTv.setText(ClsGeneral.getStrPreferences(AppConstants.ALTERNTE_NUMBER));
        } else {
            userPhoneTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.EMAIL))) {
            userEmailIdTv.setText(ClsGeneral.getStrPreferences(AppConstants.EMAIL));
        } else {
            userEmailIdTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.QUALIFICATION))) {
            userQualificationTv.setText(ClsGeneral.getStrPreferences(AppConstants.QUALIFICATION));
        } else {
            userQualificationTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.WHAT_TEACH))) {
            userTeachingTv.setText(ClsGeneral.getStrPreferences(AppConstants.WHAT_TEACH));
        } else {
            userTeachingTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.ADDRESS))) {
            userAddressTv.setText(ClsGeneral.getStrPreferences(AppConstants.ADDRESS));
        } else if (!TextUtils.isEmpty(ClsGeneral.getStrPreferences(AppConstants.PERMANENT_ADDRESS))) {
            userAddressTv.setText(ClsGeneral.getStrPreferences(AppConstants.PERMANENT_ADDRESS));
        } else {
            userAddressTv.setText(mActivity.getResources().getString(R.string.na));
        }
    }

    @OnClick(R.id.callButton)
    public void onViewClicked() {
        if (mTeachersArrayList != null && mTeachersArrayList.size() > 0) {
            if (!TextUtils.isEmpty(mTeachersArrayList.get(mPosition).getMobile()))
                Utilz.openDialer(mActivity, mTeachersArrayList.get(mPosition).getMobile());
            else if (!TextUtils.isEmpty(mTeachersArrayList.get(mPosition).getAlternate_number()))
                Utilz.openDialer(mActivity, mTeachersArrayList.get(mPosition).getAlternate_number());
        } else {
            Toast.makeText(mActivity, R.string.phone_no_not_provide_yet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        if (mTeachersArrayList != null && mTeachersArrayList.size() > 0) {
            menu.findItem(R.id.editProfile).setVisible(false);
        } else {
            menu.findItem(R.id.editProfile).setVisible(true);
        }
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editProfile:
                startActivity(new Intent(mActivity, AddTeacher.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
