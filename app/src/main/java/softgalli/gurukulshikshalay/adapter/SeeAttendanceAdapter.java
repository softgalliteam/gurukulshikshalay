package softgalli.gurukulshikshalay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.model.StudentListDataModel;

public class SeeAttendanceAdapter extends RecyclerView.Adapter<SeeAttendanceAdapter.MyViewHolder> {

    private List<StudentListDataModel> mStudentsList;
    private Activity mActivity;

    public SeeAttendanceAdapter(Activity mActivity, List<StudentListDataModel> studentListDataModels) {
        this.mStudentsList = studentListDataModels;
        this.mActivity = mActivity;
    }

    // Create new views
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_itemview, parent, false);

        // create ViewHolder
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
        if (mStudentsList != null && mStudentsList.size() > 0) {
            viewHolder.tvName.setText(mStudentsList.get(position).getStudentName());
            viewHolder.tvEmailId.setText(mStudentsList.get(position).getStudent_id());
            viewHolder.presentButton.setTag(mStudentsList.get(position));
            viewHolder.absentButton.setTag(mStudentsList.get(position));
            if (mStudentsList.get(position).getStatus().equalsIgnoreCase(AppConstants.PRESENT)) {
                managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, true);
            } else {
                managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, false);
            }
        }
    }

    @SuppressLint("NewApi")
    private void managePresentAbsent(TextView presentBtn, TextView absentBtn, boolean isPresent) {
        if (presentBtn != null && absentBtn != null) {
            if (isPresent) {
                presentBtn.setTextColor(mActivity.getResources().getColor(R.color.colorTextWhite));
                presentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.present_checkbox_image));
                absentBtn.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                absentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.border));
            } else {
                presentBtn.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                presentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.border));
                absentBtn.setTextColor(mActivity.getResources().getColor(R.color.colorTextWhite));
                absentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.absent_checkbox_image));
            }
        }
    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mStudentsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvEmailId;
        public TextView absentButton, presentButton;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = itemLayoutView.findViewById(R.id.tvName);
            tvEmailId = itemLayoutView.findViewById(R.id.tvEmailId);
            absentButton = itemLayoutView.findViewById(R.id.absentButton);
            presentButton = itemLayoutView.findViewById(R.id.presentButton);
        }
    }
}
