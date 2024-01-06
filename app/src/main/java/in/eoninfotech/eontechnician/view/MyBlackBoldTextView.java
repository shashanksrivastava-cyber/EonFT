package in.eoninfotech.eontechnician.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by root on 19/2/18.
 */
public class MyBlackBoldTextView extends TextView {
    public MyBlackBoldTextView(Context context) {
        super(context);
        this.setTextColor(Color.parseColor("#000000"));
        this.setTextSize(18);
        this.setPadding(5,3,3,3);
        this.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public MyBlackBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextColor(Color.parseColor("#000000"));
        this.setTextSize(18);
        this.setPadding(5,3,3,3);
        this.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public MyBlackBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTextColor(Color.parseColor("#000000"));
        this.setTextSize(18);
        this.setPadding(5,3,3,3);
        this.setTypeface(Typeface.DEFAULT_BOLD);
    }

}
