package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.TeacherListAdapter;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.TeacherListDataModel;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.model.UserDetailsDataModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class TeacherListActivity extends AppCompatActivity {
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Activity mActivity;
    private RetrofitDataProvider retrofitDataProvider;
    ArrayList<TeacherListDataModel> teachersArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topperlist);
        mActivity = this;
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initToolbar();
        if (Utilz.isOnline(mActivity)) {
            getTopperList();
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void getTopperList() {
        Utilz.showDailog(TeacherListActivity.this, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.teacherList(new DownlodableCallback<TeacherListModel>() {
            @Override
            public void onSuccess(final TeacherListModel result) {
                Utilz.closeDialog();
                if (result != null && result.getStatus().contains(PreferenceName.TRUE)) {
                    if (result.getData() != null && result.getData().size() > 0)
                        teachersArrayList.addAll(result.getData());
                    recyclerView.setAdapter(new TeacherListAdapter(TeacherListActivity.this, teachersArrayList, new OnClickListener() {
                        @Override
                        public void onClick(int position) {
                            if (teachersArrayList != null && teachersArrayList.size() > 0) {
                                //Coppying data from Teacher model to User model
                                ArrayList<UserDetailsDataModel> mTeachersArrayList = new ArrayList<>();
                                for (int i = 0; i < teachersArrayList.size(); i++) {
                                    TeacherListDataModel model = teachersArrayList.get(position);
                                    if (model != null) {
                                        mTeachersArrayList.add(new UserDetailsDataModel(
                                                model.getId(), model.getName(), model.getEmail_id(), model.getMobile(), model.getJoining_date(), model.getImage(), model.getStatus(),
                                                model.getQualification(), model.getAlternate_number(), model.getClassteacher_for(), model.getAddress(), model.getFacebook_id(), model.getWhat_teach(), model.getDesignation()
                                        ));
                                    }
                                }
                                Intent teacherIntent = new Intent(mActivity, ViewTeacherProfileActivity.class);
                                teacherIntent.putExtra(AppConstants.TEACHER_DETAILS, mTeachersArrayList);
                                teacherIntent.putExtra(AppConstants.POSITION, position);
                                startActivity(teacherIntent);
                            }
                        }
                    }));
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Teacher List");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
