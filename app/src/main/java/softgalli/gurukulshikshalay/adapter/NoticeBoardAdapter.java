package softgalli.gurukulshikshalay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.UpcomingActivityModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder> {

    private List<UpcomingActivityModel> mStudentsList;
    private Activity mActivity;

    public NoticeBoardAdapter(Activity mActivity, List<UpcomingActivityModel> studentListDataModels) {
        this.mStudentsList = studentListDataModels;
        this.mActivity = mActivity;
    }

    // Create new views
    @Override
    public NoticeBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_board_rowitem, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if (mStudentsList != null && mStudentsList.size() > 0) {
            final UpcomingActivityModel model = mStudentsList.get(position);
            if (model != null) {

                if (!TextUtils.isEmpty(model.getUpcomingEvetTitle()))
                    viewHolder.titleTv.setText(model.getUpcomingEvetTitle());
                else
                    viewHolder.titleTv.setText("N/A");

                if (!TextUtils.isEmpty(model.getUpcomingEvetDescription().trim())) {
                    viewHolder.shortDescriptionTv.setText(model.getUpcomingEvetDescription().trim());
                    viewHolder.longDescriptionTv.setText(model.getUpcomingEvetDescription().trim());
                } else {
                    viewHolder.shortDescriptionTv.setText("N/A");
                    viewHolder.longDescriptionTv.setText("N/A");
                }
                if (!TextUtils.isEmpty(model.getUpcomingEventDate().trim()))
                    viewHolder.dateTv.setText(model.getUpcomingEventDate().trim());
                else
                    viewHolder.dateTv.setText("N/A");

                if (!TextUtils.isEmpty(model.getUpcomingEventPostedBy().trim()))
                    viewHolder.postedByTv.setText("Posted By : " + model.getUpcomingEventPostedBy().trim());
                else
                    viewHolder.postedByTv.setText("N/A");

                viewHolder.mainLL.setTag(viewHolder);

                viewHolder.mainLL.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(View v) {
                        if (!model.isDescShown()) {
                            model.setIsDescShown(true);
                            viewHolder.shortDescriptionTv.setVisibility(View.GONE);
                            viewHolder.longDescriptionTv.setVisibility(View.VISIBLE);
                            viewHolder.moreOptionIv.setVisibility(View.VISIBLE);
                            viewHolder.titleTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                        } else {
                            model.setIsDescShown(false);
                            viewHolder.shortDescriptionTv.setVisibility(View.VISIBLE);
                            viewHolder.longDescriptionTv.setVisibility(View.GONE);
                            viewHolder.moreOptionIv.setVisibility(View.GONE);
                            viewHolder.titleTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
                        }
                    }
                });

                viewHolder.moreOptionIv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String shareMessageStr = model.getUpcomingEvetDescription() + "\n\nBy : " +
                                model.getUpcomingEventPostedBy();
                        Utilz.openMoreOptionPopupWindow(viewHolder.moreOptionIv, mActivity, model, model.getUpcomingEvetTitle(), shareMessageStr, true);
                    }
                });
            }
        }
    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mStudentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTv;
        public TextView longDescriptionTv;
        public TextView shortDescriptionTv;
        public TextView dateTv;
        public TextView postedByTv;
        public LinearLayout mainLL;
        public ImageView moreOptionIv;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            titleTv = itemLayoutView.findViewById(R.id.titleTv);
            shortDescriptionTv = itemLayoutView.findViewById(R.id.shortDescriptionTv);
            longDescriptionTv = itemLayoutView.findViewById(R.id.longDescriptionTv);
            dateTv = itemLayoutView.findViewById(R.id.dateTv);
            postedByTv = itemLayoutView.findViewById(R.id.postedByTv);
            mainLL = itemLayoutView.findViewById(R.id.mainLL);
            moreOptionIv = itemLayoutView.findViewById(R.id.moreOptionIv);
        }

    }

}

