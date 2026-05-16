package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.utils.ImageUtils;
import in.eoninfotech.eontechnician.R;

public class ImageDetailCallSheet extends AppCompatActivity  {

    ImageView image,rotate;
    ProgressBar progressBar ;
    TextView date,remarks;
    LinearLayout linear;
    String month,years,months;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Call Sheet Detail");

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

        String abc = dates;
        String[] separated = abc.split("/");
        month = separated[1];
        String datess = separated[0];
        years = separated[2];
        if(month.equals("01")){
            months = "Jan";
        }else if(month.equals("02")){
            months = "Feb";
        }else if(month.equals("03")){
            months = "Mar";
        }else if(month.equals("04")){
            months = "Apr";
        }else if(month.equals("05")){
            months = "May";
        }else if(month.equals("06")){
            months = "Jun";
        }else if(month.equals("07")){
            months = "Jul";
        }else if(month.equals("08")){
            months = "Aug";
        }else if(month.equals("09")){
            months = "Sep";
        }else if(month.equals("10")){
            months = "Oct";
        }else if(month.equals("11")){
            months = "Nov";
        }else if(month.equals("12")){
            months = "Dec";
        }
        String datesss  = datess+"-"+months+"-"+years;

        date.setText(datesss);
        if(remark== null){
            remarks.setVisibility(View.GONE);
        }else{
            remarks.setText(remark);
        }
        //remarks.setText(remark);
        ShowProgressBar(true);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopup(ImageDetailCallSheet.this, R.layout.popup_photo_full, image,images, null);
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
        getOnBackPressedDispatcher().onBackPressed();
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
