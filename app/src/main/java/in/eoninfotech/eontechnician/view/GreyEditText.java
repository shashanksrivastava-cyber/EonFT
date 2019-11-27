package in.eoninfotech.eontechnician.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

import in.eoninfotech.eontechnician.R;

/**
 * Created by root on 19/2/18.
 */
public class GreyEditText extends EditText {

    public GreyEditText(Context context) {
        super(context);
        this.setTextColor(Color.parseColor("#000000"));
        this.setBackgroundResource(R.drawable.black_edittext);
        this.setTextSize(18);
        this.setPadding(5,5,5,5);
    }

    public GreyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextColor(Color.parseColor("#000000"));
        this.setBackgroundResource(R.drawable.black_edittext);
        this.setTextSize(18);
        this.setPadding(5,5,5,5);
    }

    public GreyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTextColor(Color.parseColor("#000000"));
        this.setBackgroundResource(R.drawable.black_edittext);
        this.setTextSize(18);
        this.setPadding(5,5,5,5);
    }
}
