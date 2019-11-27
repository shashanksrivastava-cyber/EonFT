package in.eoninfotech.eontechnician.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by android on 13/3/18.
 */

public class MyTextWatcher implements TextWatcher {
    private EditText mEditText;
    double taxes;

    public MyTextWatcher(EditText e,double tax) {
        mEditText = e;
        taxes = tax;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            Double value = Double.parseDouble(s.toString());
            Double tax = Double.valueOf(taxes*value);
            mEditText.setText(""+tax);
        }catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            mEditText.setText("0");
        }
        }
}
