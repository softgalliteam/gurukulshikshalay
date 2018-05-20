package softgalli.gurukulshikshalay.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.Utilz;

/**
 * Created by Welcome on 12/27/2016.
 */
public class ParticularImage extends AppCompatActivity implements View.OnClickListener {
    String ImageUrl = "";
    AQuery aq;
    ImageView shareParticularImageButton, saveParticularImageButton, imageView;
    Bitmap bitmapf;
    Bitmap placeholderIcon;
    private ScaleGestureDetector scaleGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particular_image);
        if (getIntent().getExtras() != null) {
            ImageUrl = getIntent().getStringExtra("particularImage");
        }
        aq = new AQuery(this);
        try {
            aq.hardwareAccelerated11();
            placeholderIcon = aq.getCachedImage(R.drawable.logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        shareParticularImageButton = (ImageView) findViewById(R.id.shareParticularImageButton);
        shareParticularImageButton.setOnClickListener(this);
        saveParticularImageButton = (ImageView) findViewById(R.id.saveParticularImageButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        saveParticularImageButton.setOnClickListener(this);

        if (ImageUrl != null && !ImageUrl.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.logo);
            requestOptions.error(R.drawable.logo);
            requestOptions.fitCenter();
            final String imgUrl = ImageUrl;
            Glide.with(ParticularImage.this)
                    .load(imgUrl)
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .thumbnail(0.5f)
                    .into(imageView);
        }

        try {
            aq.ajax(ImageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap bm, AjaxStatus status) {
                    // do whatever you want with bm (the image)
                    bitmapf = bm;
                }
            });
        } catch (Exception e) {

        }
        scaleGestureDetector = new ScaleGestureDetector(
                this, new MySimpleOnScaleGestureListener(imageView));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.saveParticularImageButton:

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File folder = new File(Environment.getExternalStorageDirectory() + "/School_App");
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdir();
                        }
                        if (success) {
                            // Do something on success
                        } else {
                            // Do something else on failure
                        }

                        File file;
                        try {
                            file = new File(Environment.getExternalStorageDirectory().getPath() + "/School_App/" + ImageUrl.substring(ImageUrl.indexOf("/"), ImageUrl.length()));
                        } catch (Exception e) {
                            file = new File(Environment.getExternalStorageDirectory().getPath() + "/School_App/" + Utilz.getRandomName() + ".png");
                        }
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmapf.compress(Bitmap.CompressFormat.PNG, 75, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, "Image Saved !!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.shareParticularImageButton:
                try {
                    if (bitmapf != null) {
                        String pathofBmp = MediaStore.Images.Media.insertImage(ParticularImage.this.getContentResolver(), bitmapf, "title", null);
                        Uri bmpUri = Uri.parse(pathofBmp);
                        final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
                        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        emailIntent1.setType("image/png");
                        ParticularImage.this.startActivity(emailIntent1);
                    } else {
                        Toast.makeText(ParticularImage.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
        //return super.onTouchEvent(event);
    }

    private class MySimpleOnScaleGestureListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        ImageView viewMyImage;

        float factor;

        public MySimpleOnScaleGestureListener(ImageView iv) {
            super();
            viewMyImage = iv;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            factor = 1.0f;
            return true;
            //return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float scaleFactor = detector.getScaleFactor() - 1;
            factor += scaleFactor;

            viewMyImage.setScaleX(factor);
            viewMyImage.setScaleY(factor);
            return true;
            //return super.onScale(detector);
        }
    }
}