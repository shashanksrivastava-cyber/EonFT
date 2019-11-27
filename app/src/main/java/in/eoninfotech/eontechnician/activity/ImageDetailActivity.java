package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import in.eoninfotech.eontechnician.ImageUtils;
import in.eoninfotech.eontechnician.R;

import static com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture.DRAG;

/**
 * Created by root on 14/2/19.
 */

public class ImageDetailActivity extends AppCompatActivity {

    ImageView image,rotate;
    ProgressBar progressBar ;
    TextView date,remarks;
    LinearLayout linear;
    String month,years,months,activitymonth,activityyear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Image Detail");

        image = findViewById(R.id.image);
        progressBar  = findViewById(R.id.progressBar);
        date = findViewById(R.id.date);
        remarks = findViewById(R.id.remarks);
        rotate = findViewById(R.id.rotate);
        linear = findViewById(R.id.linear);

        Intent intent = getIntent();
        String images = intent.getStringExtra("Image");
        String dates  = intent.getStringExtra("Date");
        String remark = intent.getStringExtra("Remarks");
        String activityDate = intent.getStringExtra("Dates");

        if(!activityDate.equalsIgnoreCase(null)) {
            String def = activityDate;
            String[] separate = def.split("-");
            activitymonth = separate[1];
            String daate = separate[0];
            activityyear = separate[2];
            if (activitymonth.equals("01")) {
                months = "Jan";
            } else if (activitymonth.equals("02")) {
                months = "Feb";
            } else if (activitymonth.equals("03")) {
                months = "Mar";
            } else if (activitymonth.equals("04")) {
                months = "Apr";
            } else if (activitymonth.equals("05")) {
                months = "May";
            } else if (activitymonth.equals("06")) {
                months = "Jun";
            } else if (activitymonth.equals("07")) {
                months = "Jul";
            } else if (activitymonth.equals("08")) {
                months = "Aug";
            } else if (activitymonth.equals("09")) {
                months = "Sep";
            } else if (activitymonth.equals("10")) {
                months = "Oct";
            } else if (activitymonth.equals("11")) {
                months = "Nov";
            } else if (activitymonth.equals("12")) {
                months = "Dec";
            }
            String ddate = activityyear + "-" + months + "-" + daate;
            date.setText(ddate);
        }else {
            String abc = dates;
            String[] separated = abc.split("/");
            month = separated[1];
            String datess = separated[0];
            years = separated[2];
            if (month.equals("01")) {
                months = "Jan";
            } else if (month.equals("02")) {
                months = "Feb";
            } else if (month.equals("03")) {
                months = "Mar";
            } else if (month.equals("04")) {
                months = "Apr";
            } else if (month.equals("05")) {
                months = "May";
            } else if (month.equals("06")) {
                months = "Jun";
            } else if (month.equals("07")) {
                months = "Jul";
            } else if (month.equals("08")) {
                months = "Aug";
            } else if (month.equals("09")) {
                months = "Sep";
            } else if (month.equals("10")) {
                months = "Oct";
            } else if (month.equals("11")) {
                months = "Nov";
            } else if (month.equals("12")) {
                months = "Dec";
            }
            String datesss = datess + "-" + months + "-" + years;
            date.setText(datesss);
        }
        if(remark== null){
            remarks.setVisibility(View.GONE);
        }else{
            remarks.setText(remark);
        }
        ShowProgressBar(true);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopup(ImageDetailActivity.this, R.layout.popup_photo_full, image,images, null);
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setRotation(image.getRotation() + 90);
            }
        });

        ImageUtils.glideImageWithLoader(image, images);

        Glide.with(this)
                .load(images)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ShowProgressBar(false);
                        return false;
                    }
                })
                .into(image);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }}
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}