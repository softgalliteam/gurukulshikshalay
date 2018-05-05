package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.Utilz;


public class ContactUsActivity extends AppCompatActivity {
    @BindView(R.id.callButton)
    Button callButton;
    @BindView(R.id.callSchoolButton)
    Button callSchoolButton;
    @BindView(R.id.emailButton)
    Button emailButton;
    @BindView(R.id.goButton)
    Button goButton;
    private String TAG = ContactUsActivity.class.getSimpleName();
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus_activity);
        ButterKnife.bind(this);
        mActivity = this;

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Contact Us");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.callButton, R.id.callSchoolButton, R.id.emailButton, R.id.goButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.callButton:
                Utilz.openDialer(mActivity, mActivity.getResources().getString(R.string.principal_phone));
                break;
            case R.id.callSchoolButton:
                Utilz.openDialer(mActivity, mActivity.getResources().getString(R.string.helpline_no1));
                break;
            case R.id.emailButton:
                Utilz.openMail(mActivity);
                break;
            case R.id.goButton:
                Utilz.openBrowser(mActivity, mActivity.getResources().getString(R.string.school_on_map_url));
                break;
        }
    }
}
