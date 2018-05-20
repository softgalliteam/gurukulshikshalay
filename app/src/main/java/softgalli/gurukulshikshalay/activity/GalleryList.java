package softgalli.gurukulshikshalay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.GalleryListAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.GalleryModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

/**
 * Created by Shankar on 4/6/2018.
 */

public class GalleryList extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        initToolbar();
        if (Utilz.isInternetConnected(GalleryList.this)) {
            callCatApi();
        } else {
            Utilz.showNoInternetConnectionDialog(this);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.gallery_album));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callCatApi() {
        Utilz.showDailog(GalleryList.this, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.galleryList(new DownlodableCallback<GalleryModel>() {
            @Override
            public void onSuccess(final GalleryModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {

                    recyclerView.setAdapter(new GalleryListAdapter(GalleryList.this, result.getData(), new OnClickListener() {
                        @Override
                        public void onClick(int pos) {
                            Intent intent = new Intent(GalleryList.this, Gallery.class);
                            intent.putParcelableArrayListExtra("images", result.getData().get(pos).getImages());
                            intent.putExtra("position", pos);
                            startActivity(intent);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
            }
        });
    }
}
