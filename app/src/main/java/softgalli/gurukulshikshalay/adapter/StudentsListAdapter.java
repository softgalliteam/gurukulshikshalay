package softgalli.gurukulshikshalay.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import softgalli.gurukulshikshalay.R;
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
            viewHolder.tvEmailId.setText("Student Id : " + mStudentsList.get(position).getStudentId());
            if (!TextUtils.isEmpty(mStudentsList.get(position).getPermanent_address()))
                viewHolder.tvAddress.setText(mStudentsList.get(position).getPermanent_address());
            if (!TextUtils.isEmpty(mStudentsList.get(position).getResidential_address()))
                viewHolder.tvAddress.setText(mStudentsList.get(position).getResidential_address());
            else
                viewHolder.tvAddress.setText("Address : N/A");
            viewHolder.mainView.setTag(mStudentsList.get(position));

            viewHolder.mainView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (v != null && v instanceof TextView) {
                        TextView cb = (TextView) v;
                        StudentListDataModel contact = (StudentListDataModel) cb.getTag();
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

