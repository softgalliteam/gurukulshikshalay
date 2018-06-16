package softgalli.gurukulshikshalay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.activity.UpdateAttendanceActivity;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.model.StudentListDataModel;

public class UpdateAttendanceAdapter extends RecyclerView.Adapter<UpdateAttendanceAdapter.ViewHolder> {

    private ArrayList<StudentListDataModel> mStudentsList;
    private Activity mActivity;
    private int mIntTotalStudentCount;
    private int mIntAbsentStudentCount;

    public UpdateAttendanceAdapter(Activity mActivity, ArrayList<StudentListDataModel> studentListDataModels) {
        this.mStudentsList = studentListDataModels;
        this.mActivity = mActivity;
        mIntTotalStudentCount = 0;
        if (studentListDataModels != null && studentListDataModels.size() > 0) {
            mIntTotalStudentCount = mStudentsList.size();
            mIntAbsentStudentCount = ((UpdateAttendanceActivity) mActivity).getAbsentStudentCount(mStudentsList);
        }
        ((UpdateAttendanceActivity) mActivity).manageAbsentPresentCount(mIntTotalStudentCount, mIntAbsentStudentCount);
    }

    // Create new views
    @Override
    public UpdateAttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_itemview, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final int pos = position;
        if (mStudentsList != null && mStudentsList.size() > 0) {
            viewHolder.tvName.setText(mStudentsList.get(position).getStudentName());
            viewHolder.tvEmailId.setText(mStudentsList.get(position).getStudentId());
            viewHolder.presentButton.setTag(mStudentsList.get(position));
            viewHolder.absentButton.setTag(mStudentsList.get(position));

            viewHolder.presentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, true);

                    if (v != null && v instanceof TextView) {
                        TextView cb = (TextView) v;
                        StudentListDataModel contact = (StudentListDataModel) cb.getTag();
                        if (!contact.isSelected()) {
                            mIntAbsentStudentCount = mIntAbsentStudentCount - 1;
                            ((UpdateAttendanceActivity) mActivity).manageAbsentPresentCount(mIntTotalStudentCount, mIntAbsentStudentCount);
                        }
                        contact.setSelected(true);
                    }
                    mStudentsList.get(pos).setSelected(true);
                }
            });
            viewHolder.absentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, false);
                    if (v != null && v instanceof TextView) {
                        TextView cb = (TextView) v;
                        StudentListDataModel contact = (StudentListDataModel) cb.getTag();

                        if (contact.isSelected()) {
                            mIntAbsentStudentCount = mIntAbsentStudentCount + 1;
                            ((UpdateAttendanceActivity) mActivity).manageAbsentPresentCount(mIntTotalStudentCount, mIntAbsentStudentCount);
                        }
                        contact.setSelected(false);
                    }
                    mStudentsList.get(pos).setSelected(false);
                }
            });

            if (mStudentsList.get(position).getStatus().equalsIgnoreCase(AppConstants.PRESENT)) {
                mStudentsList.get(position).setSelected(true);//means present
                managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, true);
            } else {
                managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, false);
                mStudentsList.get(position).setSelected(false);//means absent
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvEmailId;
        public TextView absentButton, presentButton;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = itemLayoutView.findViewById(R.id.tvName);
            tvEmailId = itemLayoutView.findViewById(R.id.tvEmailId);
            absentButton = itemLayoutView.findViewById(R.id.absentButton);
            presentButton = itemLayoutView.findViewById(R.id.presentButton);
        }

    }

}
