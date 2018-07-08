package softgalli.gurukulshikshalay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.activity.AddStudent;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.model.StudentListDataModel;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.ViewHolder> {

    private List<StudentListDataModel> mStudentsList;
    private Activity mActivity;

    public StudentsListAdapter(Activity mActivity, List<StudentListDataModel> studentListDataModels) {
        this.mStudentsList = studentListDataModels;
        this.mActivity = mActivity;
    }

    // Create new views
    @Override
    public StudentsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if (mStudentsList != null && mStudentsList.size() > 0) {
            viewHolder.tvName.setText(mStudentsList.get(position).getStudentName());
            if (mStudentsList.get(position).getRollNo() > 0 && !TextUtils.isEmpty(mStudentsList.get(position).getSec())) {
                viewHolder.tvEmailId.setText(String.format(mActivity.getResources().getString(R.string.roll_no_with_value),
                        mStudentsList.get(position).getRollNo() + " (Sec - " + mStudentsList.get(position).getSec() + ")"));
            } else if (!TextUtils.isEmpty(mStudentsList.get(position).getStudentId())) {
                viewHolder.tvEmailId.setText(String.format(mActivity.getResources().getString(R.string.student_id_with_value), mStudentsList.get(position).getStudentId()));
            } else if (!TextUtils.isEmpty(mStudentsList.get(position).getStudent_id())) {
                viewHolder.tvEmailId.setText(String.format(mActivity.getResources().getString(R.string.student_id_with_value), mStudentsList.get(position).getStudent_id()));
            } else {
                viewHolder.tvEmailId.setText(String.format(mActivity.getResources().getString(R.string.roll_no_with_value), AppConstants.NA));
            }
            if (!TextUtils.isEmpty(mStudentsList.get(position).getPermanent_address()))
                viewHolder.tvAddress.setText(mStudentsList.get(position).getPermanent_address());
            if (!TextUtils.isEmpty(mStudentsList.get(position).getResidential_address().trim()))
                viewHolder.tvAddress.setText(mStudentsList.get(position).getResidential_address().trim());
            else
                viewHolder.tvAddress.setText("Address : N/A");
            viewHolder.mainView.setTag(mStudentsList.get(position));

            viewHolder.mainView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (v != null && v instanceof LinearLayout) {
                        LinearLayout cb = (LinearLayout) v;
                        ArrayList<StudentListDataModel> studentDetails = new ArrayList<>();
                        studentDetails.add((StudentListDataModel) cb.getTag());
                        Intent studentIntent = new Intent(mActivity, AddStudent.class);
                        studentIntent.putExtra(AppConstants.STUDENTS_DETAILS, studentDetails);
                        studentIntent.putExtra(AppConstants.POSITION, 0);
                        studentIntent.putExtra(AppConstants.IS_FOR_UPDATE, true);
                        mActivity.startActivity(studentIntent);
                    }
                }
            });
        }
    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mStudentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvEmailId;
        public TextView tvAddress;
        public LinearLayout mainView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = itemLayoutView.findViewById(R.id.tvName);
            tvEmailId = itemLayoutView.findViewById(R.id.tvEmailId);
            tvAddress = itemLayoutView.findViewById(R.id.tvAddress);
            mainView = itemLayoutView.findViewById(R.id.mainView);
        }

    }

}

