package softgalli.gurukulshikshalay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.activity.TakeAttendenceActivity;
import softgalli.gurukulshikshalay.adapter.StudentsListAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.StudentListByClassModel;
import softgalli.gurukulshikshalay.model.StudentListDataModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

@SuppressLint("ValidFragment")
public class StudentsFragment extends Fragment {
    String tabTitle = "", mClassName = "10", mSectionName = "A";
    private static final String TAG = TakeAttendenceActivity.class.getSimpleName();
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noRecordFoundCardView)
    CardView noRecordFoundCardView;
    @BindView(R.id.mainCardView)
    CardView mainCardView;
    @BindView(R.id.noRecordFoundTv)
    TextView noRecordFoundTv;
    private Activity mActivity;
    private boolean isCallingApi = false;
    private ArrayList<StudentListDataModel> studentListDataModelList;
    private RetrofitDataProvider retrofitDataProvider;

    public StudentsFragment(String tabTitle, String mClassName) {
        // Required empty public constructor
        this.tabTitle = tabTitle;
        this.mClassName = mClassName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_students, container, false);
        mActivity = getActivity();
        ButterKnife.bind(this, rootView);
        studentListDataModelList = new ArrayList();
        retrofitDataProvider = new RetrofitDataProvider(mActivity);

        initView();

        return rootView;
    }


    private void initView() {
        noRecordFoundTv.setText(mActivity.getResources().getString(R.string.no_record_found));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        manageVisibility();
        if (Utilz.isOnline(mActivity) && !isCallingApi) {
            getStudentListFromServer();
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void getStudentListFromServer() {
        isCallingApi = true;
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.getStudentsListByClassWise(mClassName, mSectionName, new DownlodableCallback<StudentListByClassModel>() {
            @Override
            public void onSuccess(final StudentListByClassModel result) {
                Utilz.closeDialog();
                isCallingApi = false;
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    studentListDataModelList.clear();
                    studentListDataModelList = result.getData();
                    mRecyclerView.setAdapter(new StudentsListAdapter(mActivity, studentListDataModelList));

                    manageVisibility();
                }
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
                isCallingApi = false;
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
                isCallingApi = false;
            }
        });
    }

    private void manageVisibility() {
        if (studentListDataModelList != null && studentListDataModelList.size() > 0 && noRecordFoundCardView != null && mainCardView != null) {
            noRecordFoundCardView.setVisibility(View.GONE);
            mainCardView.setVisibility(View.VISIBLE);
        } else if (noRecordFoundCardView != null && mainCardView != null) {
            noRecordFoundCardView.setVisibility(View.VISIBLE);
            mainCardView.setVisibility(View.GONE);
        }
    }
}
