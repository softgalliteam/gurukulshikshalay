package softgalli.gurukulshikshalay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.intrface.OnClickListener;

/**
 * Created by Shankar on 3/31/2018.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private Context context;
    private String itemList[];
    private OnClickListener clickPosition;
    private int item_row, iconList[], backgroundColor[];

    public HomeCategoryAdapter(Context context, String[] itemList, int[] iconList, int[] backgroundColor, int item_row, OnClickListener clickPosition) {
        this.context = context;
        this.itemList = itemList;
        this.item_row = item_row;
        this.iconList = iconList;
        this.backgroundColor = backgroundColor;
        this.clickPosition = clickPosition;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(item_row,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.cat_image.setBackgroundResource(iconList[position]);
        holder.ll_background.setBackgroundResource(backgroundColor[position]);
        holder.cat_name.setText(itemList[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.length;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cat_name;
        private ImageView cat_image;
        private LinearLayout ll_background;

        public ViewHolder(View itemView) {
            super(itemView);
            cat_name = (TextView)itemView.findViewById(R.id.cat_name);
            cat_image = (ImageView) itemView.findViewById(R.id.cat_image);
            ll_background = (LinearLayout) itemView.findViewById(R.id.ll_background);

        }
    }
}

