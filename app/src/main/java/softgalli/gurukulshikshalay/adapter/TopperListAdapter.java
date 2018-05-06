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
import softgalli.gurukulshikshalay.model.GalleryDataModel;
import softgalli.gurukulshikshalay.model.TopperLisrModel;
import softgalli.gurukulshikshalay.model.TopperListDataModel;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class TopperListAdapter extends RecyclerView.Adapter<TopperListAdapter.MyViewHolder> {

    private List<TopperListDataModel> images;
    private Context mContext;
    private OnClickListener onClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageViewProfilePic1;
        public TextView studentNameTv;
        public TextView marksObtainedTv;
        public TextView classNameTv;
        public TextView subjectTv;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            imageViewProfilePic1 = (CircleImageView) view.findViewById(R.id.imageViewProfilePic1);
            studentNameTv = (TextView) view.findViewById(R.id.studentNameTv);
            marksObtainedTv = (TextView) view.findViewById(R.id.marksObtainedTv);
            classNameTv = (TextView) view.findViewById(R.id.classNameTv);
            subjectTv = (TextView) view.findViewById(R.id.subjectTv);
            progressBar = (ProgressBar)view.findViewById(R.id.progress);
        }
    }


    public TopperListAdapter(Context context, List<TopperListDataModel> images, OnClickListener onClickListener ) {
        mContext = context;
        this.images = images;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topperlist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TopperListDataModel image = images.get(position);

        if (image.getStatus().equalsIgnoreCase("1")) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.logo);
            requestOptions.error(R.drawable.logo);
            requestOptions.fitCenter();
            Glide.with(mContext)
                    .load(ApiUrl.IMAGE_BASE_URL+image.getUser_image())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.imageViewProfilePic1.setAlpha(0.9f);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.imageViewProfilePic1.setAlpha(0.9f);
                            return false;
                        }
                    })
                    .thumbnail(0.5f)
                    .into(holder.imageViewProfilePic1);


            holder.studentNameTv.setText(image.getName());
            holder.marksObtainedTv.setText("Marks - "+image.getPassing_number());
            holder.classNameTv.setText("Class -  "+image.getClas());
            holder.subjectTv.setText("Roll - "+image.getToper_id());
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
