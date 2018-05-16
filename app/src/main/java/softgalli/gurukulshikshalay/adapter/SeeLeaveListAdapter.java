package softgalli.gurukulshikshalay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.RequestedLeaveDataModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class SeeLeaveListAdapter extends RecyclerView.Adapter<SeeLeaveListAdapter.MyViewHolder> {

    private List<RequestedLeaveDataModel> mLeaveArrayList;
    private Context mActivity;
    private OnClickListener onClickListener;

    public SeeLeaveListAdapter(Context context, List<RequestedLeaveDataModel> mLeaveArrayList, OnClickListener onClickListener) {
        mActivity = context;
        this.mLeaveArrayList = mLeaveArrayList;
        this.onClickListener = onClickListener;
    }

    @Override
    public SeeLeaveListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaves_rowitem, parent, false);

        return new SeeLeaveListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SeeLeaveListAdapter.MyViewHolder holder, final int position) {
        RequestedLeaveDataModel requestedLeaveDataModel = mLeaveArrayList.get(position);

        String imageUrl = "", userName = "";
        if (requestedLeaveDataModel.getUserDetails() != null && requestedLeaveDataModel.getUserDetails().size() > 0) {
            imageUrl = requestedLeaveDataModel.getUserDetails().get(0).getProfile_pic();
            userName = requestedLeaveDataModel.getUserDetails().get(0).getName();
        }
        holder.itemView.setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user);
        requestOptions.error(R.drawable.user);
        requestOptions.fitCenter();
        Glide.with(mActivity)
                .load(ApiUrl.IMAGE_BASE_URL + imageUrl)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.teacherProfilePicIv);
        if (!TextUtils.isEmpty(userName)) {
            holder.studentName.setText(userName);
        } else {
            holder.studentName.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(requestedLeaveDataModel.getFrom_date())) {
            holder.leaveFromToDateTv.setText("From " + requestedLeaveDataModel.getFrom_date() + " to " + requestedLeaveDataModel.getTo_date());
        } else {
            holder.leaveFromToDateTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(requestedLeaveDataModel.getDescription())) {
            holder.reasonOfLeaveTv.setText(requestedLeaveDataModel.getDescription());
        } else {
            holder.reasonOfLeaveTv.setText(mActivity.getResources().getString(R.string.na));
        }
        if (!TextUtils.isEmpty(requestedLeaveDataModel.getStatus())) {
            if (requestedLeaveDataModel.getStatus().equalsIgnoreCase("2")) {
                holder.leaveStatusTv.setText(mActivity.getResources().getString(R.string.disapproved));
                holder.leaveStatusTv.setTextColor(ContextCompat.getColor(mActivity, R.color.red));
            } else if (requestedLeaveDataModel.getStatus().equalsIgnoreCase("1")) {
                holder.leaveStatusTv.setText(mActivity.getResources().getString(R.string.approved));
                holder.leaveStatusTv.setTextColor(ContextCompat.getColor(mActivity, R.color.green));
            } else {
                holder.leaveStatusTv.setText(mActivity.getResources().getString(R.string.pending));
                holder.leaveStatusTv.setTextColor(ContextCompat.getColor(mActivity, R.color.yellow));
            }
        } else {
            holder.leaveStatusTv.setText(mActivity.getResources().getString(R.string.na));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLeaveArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView teacherProfilePicIv;
        public TextView leaveFromToDateTv;
        public TextView reasonOfLeaveTv;
        public TextView studentName;
        public TextView leaveStatusTv;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            teacherProfilePicIv = (ImageView) view.findViewById(R.id.teacherProfilePicIv);
            leaveFromToDateTv = (TextView) view.findViewById(R.id.leaveFromToDateTv);
            reasonOfLeaveTv = (TextView) view.findViewById(R.id.reasonOfLeaveTv);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            studentName = view.findViewById(R.id.studentName);
            leaveStatusTv = view.findViewById(R.id.leaveStatusTv);
        }
    }
}
