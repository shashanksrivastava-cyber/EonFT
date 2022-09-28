package in.eoninfotech.eontechnician.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import in.eoninfotech.eontechnician.ImageUtils;
import in.eoninfotech.eontechnician.R;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by root on 26/4/19.
 */

public class MyBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener {

    private ImageView ivAvatar;
    private ImageView ivClose;
    private TextView tvTitle;
    private TextView tvSubTitle,leave,tvleaveType;
    private Context context;
    GifImageView giffy;

    @SuppressLint("StaticFieldLeak")
    private static MyBottomSheetDialog instance;

    public static MyBottomSheetDialog getInstance(@NonNull Context context) {
        return instance == null ? new MyBottomSheetDialog(context) : instance;
    }

    public MyBottomSheetDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        create();
    }

    public void create() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // do something
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // do something
            }
        };

        ivAvatar = (ImageView) bottomSheetView.findViewById(R.id.ivAvatar);
        tvTitle = (TextView) bottomSheetView.findViewById(R.id.tvTitle);
        tvSubTitle = (TextView) bottomSheetView.findViewById(R.id.tvSubTitle);
        leave = (TextView)bottomSheetView.findViewById(R.id.leave);
        tvleaveType = (TextView)bottomSheetView.findViewById(R.id.tvleaveType);
        ivAvatar.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
    }

    public void setIvAvatar(String url) {
        ImageUtils.glideImage(ivAvatar,url,R.drawable.user);
    }

    public void setGif(Drawable url) {
        this.setGif(url);
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle.setText(tvTitle);
    }

    public void setTvSubTitle(String tvSubTitle) {
        this.tvSubTitle.setText(tvSubTitle);
    }
    public void setLeave(String leave) {

        this.leave.setText(leave);
    }
    public void setLeaveType(String leaveType) {

        this.tvleaveType.setText(leaveType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTitle:
//                hide();
                break;
            case R.id.ivAvatar:
//                hide();
                break;
        }
    }
}
