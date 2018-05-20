package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
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
import softgalli.gurukulshikshalay.adapter.TopperListAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.TopperLisrModel;
import softgalli.gurukulshikshalay.model.TopperListDataModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class TopperListActivity extends AppCompatActivity {

    Activity mActivity;
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<TopperListDataModel> mStudentsArrayList = new ArrayList();
    private RetrofitDataProvider retrofitDataProvider;

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
        Utilz.showDailog(TopperListActivity.this, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.topperlist(new DownlodableCallback<TopperLisrModel>() {
            @Override
            public void onSuccess(final TopperLisrModel result) {
                Utilz.closeDialog();
                if (result != null && result.getStatus().contains(PreferenceName.TRUE)) {
                    if (result.getData() != null && result.getData().size() > 0)
                        mStudentsArrayList.addAll(result.getData());
                    recyclerView.setAdapter(new TopperListAdapter(TopperListActivity.this, mStudentsArrayList, new OnClickListener() {
                        @Override
                        public void onClick(int position) {

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
        getSupportActionBar().setTitle("Topper List");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
