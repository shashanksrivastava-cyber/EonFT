package in.eoninfotech.eontechnician;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;

/**
 * Created by androidpc on 20/6/19.
 */

public class CustomTextInputLayout extends TextInputLayout {

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setErrorEnabled(boolean enabled){
        super.setErrorEnabled(enabled);
        if (!enabled){
            return;
        }
        try{
            Field field = TextInputLayout.class.getDeclaredField("mErrorView");
            field.setAccessible(true);
            TextView errorView = (TextView) field.get(this);
            if(errorView != null){
                errorView.setGravity(Gravity.RIGHT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.END;
                errorView.setLayoutParams(params);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
