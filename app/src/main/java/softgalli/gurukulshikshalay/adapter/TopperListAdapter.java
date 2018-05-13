package softgalli.gurukulshikshalay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.TopperListDataModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class TopperListAdapter extends RecyclerView.Adapter<TopperListAdapter.MyViewHolder> {

    private List<TopperListDataModel> images = new ArrayList<>();
    private Context mContext;
    private OnClickListener onClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageViewProfilePic1;
        public TextView studentNameTv;
        public TextView marksObtainedTv;
        public TextView classNameTv,subjectTv;
        private ProgressBar progressBar;
        private LinearLayout mainLl;

        public MyViewHolder(View view) {
            super(view);
            imageViewProfilePic1 = (CircleImageView) view.findViewById(R.id.imageViewProfilePic1);
            studentNameTv = (TextView) view.findViewById(R.id.studentNameTv);
            marksObtainedTv = (TextView) view.findViewById(R.id.marksObtainedTv);
            classNameTv = (TextView) view.findViewById(R.id.classNameTv);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            mainLl = (LinearLayout) view.findViewById(R.id.mainLl);
            imageViewProfilePic1 = view.findViewById(R.id.imageViewProfilePic1);
            studentNameTv = view.findViewById(R.id.studentNameTv);
            marksObtainedTv = view.findViewById(R.id.marksObtainedTv);
            classNameTv = view.findViewById(R.id.classNameTv);
            subjectTv = view.findViewById(R.id.subjectTv);
            progressBar = view.findViewById(R.id.progress);
        }
    }


    public TopperListAdapter(Context context, List<TopperListDataModel> images, OnClickListener onClickListener) {
        mContext = context;
        removeStudentsHavingZeroStatus(images);
        this.onClickListener = onClickListener;
    }

    private void removeStudentsHavingZeroStatus(List<TopperListDataModel> topperListDataModelList) {
        if (topperListDataModelList != null) {
            for (int i = 0; i < topperListDataModelList.size(); i++) {
                if (topperListDataModelList.get(i).getStatus().equalsIgnoreCase("1")) {
                    images.add(topperListDataModelList.get(i));
                }
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topperlist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TopperListDataModel topperListDataModel = images.get(position);
        if (topperListDataModel != null && topperListDataModel.getStatus().equalsIgnoreCase("1")) {
            holder.itemView.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user);
            requestOptions.error(R.drawable.user);
            requestOptions.fitCenter();
            Glide.with(mContext)
                    .load(ApiUrl.IMAGE_BASE_URL + topperListDataModel.getUser_image())
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
                    .into(holder.imageViewProfilePic1);


            holder.studentNameTv.setText(topperListDataModel.getName());
            if (!TextUtils.isEmpty(topperListDataModel.getPassing_number())) {
                holder.marksObtainedTv.setVisibility(View.VISIBLE);
                holder.marksObtainedTv.setText("Marks-" + topperListDataModel.getPassing_number());
            } else {
                holder.marksObtainedTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(topperListDataModel.getClas())) {
                holder.classNameTv.setVisibility(View.VISIBLE);
                holder.classNameTv.setText("Class-" + topperListDataModel.getClas());
            } else {
                holder.classNameTv.setVisibility(View.GONE);
            }
        } else {
            holder.itemView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onClickListener.onClick(position);
                if (topperListDataModel != null) {
                    Toast.makeText(mContext, topperListDataModel.getName() + " is topper of " +
                            topperListDataModel.getClas() + " class and got " +
                            topperListDataModel.getPassing_number() + " marks.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


}
