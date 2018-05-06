package softgalli.gurukulshikshalay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

import de.hdodenhof.circleimageview.CircleImageView;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.TeacherListDataModel;
import softgalli.gurukulshikshalay.model.TopperListDataModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<TeacherListDataModel> images;
    private Context mContext;
    private OnClickListener onClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView teacherProfilePicIv;
        public TextView qualificationTv;
        public TextView subjectTv;
        public TextView teacherName;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            teacherProfilePicIv = (ImageView) view.findViewById(R.id.teacherProfilePicIv);
            qualificationTv = (TextView) view.findViewById(R.id.qualificationTv);
            subjectTv = (TextView) view.findViewById(R.id.subjectTv);
            teacherName = (TextView) view.findViewById(R.id.teacherName);
          //  progressBar = (ProgressBar)view.findViewById(R.id.progress);
        }
    }


    public TeacherListAdapter(Context context, List<TeacherListDataModel> images, OnClickListener onClickListener ) {
        mContext = context;
        this.images = images;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacherlist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TeacherListDataModel image = images.get(position);

        if (image.getStatus().equalsIgnoreCase("1")) {
            Glide.with(mContext)
                    .load(ApiUrl.IMAGE_BASE_URL+image.getImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                           // holder.progressBar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                           // holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .thumbnail(0.5f)
                    .into(holder.teacherProfilePicIv);
            holder.qualificationTv.setText(image.getQualification());
            holder.subjectTv.setText(image.getSubject());
            holder.teacherName.setText(image.getName());
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
        return images.size();
    }



}
