package softgalli.gurukulshikshalay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.StudentsListAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.StudentListByClassModel;
import softgalli.gurukulshikshalay.model.StudentListDataModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

@SuppressLint("ValidFragment")
public class StudentsFragment extends Fragment {
    private static final String TAG = StudentsFragment.class.getSimpleName();
    String tabTitle = "", mClassName = "10", mSectionName = "";
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
        Log.i(TAG, "initView method is called : " + tabTitle + ", " + mClassName);
        if (tabTitle.equalsIgnoreCase("Class - 10") && mClassName.equalsIgnoreCase("10"))
            callStudentListFromServer(tabTitle, mClassName);
    }

    private void getStudentListFromServer() {
        isCallingApi = true;
        showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.getStudentsListByClassWise(mClassName, mSectionName, new DownlodableCallback<StudentListByClassModel>() {
            @Override
            public void onSuccess(final StudentListByClassModel result) {
                closeDialog();
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
                closeDialog();
                isCallingApi = false;
                manageVisibility();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                closeDialog();
                isCallingApi = false;
                manageVisibility();
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

    public void callStudentListFromServer(String tabTitle, String mClassName) {
        this.tabTitle = tabTitle;
        this.mClassName = mClassName;
        manageVisibility();
        Log.i(TAG, "callStudentListFromServer method is called : " + tabTitle + ", " + mClassName);
        if (Utilz.isOnline(mActivity) && !isCallingApi) {
            getStudentListFromServer();
        } else {
            Utilz.showNoInternetConnectionDialog(mActivity);
        }
    }

    public void showDailog(Context c, String msg) {
        dialog = new ProgressDialog(c);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    public void closeDialog() {
        if (dialog != null)
            dialog.cancel();
    }

    ProgressDialog dialog;
}
