package in.eoninfotech.eontechnician.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by android on 15/3/18.
 */

public class TotalTextWatcher implements TextWatcher {

    EditText et;
    int max;

    public TotalTextWatcher(EditText et, String vts_quantity) {
        this.et = et;
        max = Integer.parseInt(vts_quantity);
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
            Integer et_text = Integer.parseInt(s.toString());
            if (et_text > max) {
                et.setText(max);
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

    }
}
