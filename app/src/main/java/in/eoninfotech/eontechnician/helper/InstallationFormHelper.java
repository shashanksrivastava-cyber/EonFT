package in.eoninfotech.eontechnician.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import in.eoninfotech.eontechnician.R;

public class InstallationFormHelper {
    private final View rootView;

    public InstallationFormHelper(View rootView) {
        this.rootView = rootView;
    }

    public void resetAllFields() {
        // Reset radio groups
        resetRadioGroup(R.id.radiodeviceType1);
        resetRadioGroup(R.id.radiodrsInstall);
        resetRadioGroup(R.id.radiodeviceType);
        resetRadioGroup(R.id.is_demo);
        resetRadioGroup(R.id.radiogroup);
        resetRadioGroup(R.id.radiogroupDoor);
        resetRadioGroup(R.id.radioGroupCutoff);
        resetRadioGroup(R.id.radioGroupPanic);
        resetRadioGroup(R.id.radioGroupFuel);
        resetRadioGroup(R.id.radioGrouptilt);
        resetRadioGroup(R.id.radioGroupfuelSensor);
        resetRadioGroup(R.id.radioGrouptemp);
        resetRadioGroup(R.id.radioGrouptrans);
        resetRadioGroup(R.id.radioGroupSilo);
        resetRadioGroup(R.id.radioGroupLid);

        // Reset spinners
        resetSpinner(R.id.device1);
        resetSpinner(R.id.vltddevice);
        resetSpinner(R.id.sr_no);
        resetSpinner(R.id.new_in_vehicleType);

        // Reset text inputs
        resetTextInput(R.id.new_in_deviceid);
        resetTextInput(R.id.vts_sr_no);
        resetTextInput(R.id.vltd_sr_no);
        resetTextInput(R.id.con_vltd_sr_no);
        resetTextInput(R.id.new_in_reg_no);
        resetTextInput(R.id.con_in_reg_no);
        resetTextInput(R.id.accessory_reg_no);
        resetTextInput(R.id.accessory_sr_no);
        resetTextInput(R.id.installVoltage);

        // Reset specific radio buttons to default states
        setRadioButtonChecked(R.id.device11, true);
        setRadioButtonChecked(R.id.radionodrs, true);
        setRadioButtonChecked(R.id.voice, true);
        setRadioButtonChecked(R.id.is_demo_no, true);
        setRadioButtonChecked(R.id.l_in, true);
        setRadioButtonChecked(R.id.doorNo, true);
        setRadioButtonChecked(R.id.cutoffNo, true);
        setRadioButtonChecked(R.id.panicNo, true);
        setRadioButtonChecked(R.id.fuelNo, true);
        setRadioButtonChecked(R.id.tiltNo, true);
        setRadioButtonChecked(R.id.fuelSensorNewNo, true);
        setRadioButtonChecked(R.id.tempNo, true);
        setRadioButtonChecked(R.id.transNo, true);
        setRadioButtonChecked(R.id.siloNo, true);
        setRadioButtonChecked(R.id.lidNone, true);

        // Reset visibility of optional sections
        setViewVisibility(R.id.oldDeviceType1, View.VISIBLE);
        setViewVisibility(R.id.linear_accessory, View.GONE);
        setViewVisibility(R.id.linear_device_sr_no, View.GONE);
        setViewVisibility(R.id.linear_device_sr_no_e_series, View.GONE);
        setViewVisibility(R.id.vltdOptions, View.GONE);
        setViewVisibility(R.id.text_to_show, View.GONE);
    }

    private void resetRadioGroup(int radioGroupId) {
        RadioGroup radioGroup = rootView.findViewById(radioGroupId);
        if (radioGroup != null) {
            radioGroup.clearCheck();
        }
    }

    private void setRadioButtonChecked(int radioButtonId, boolean checked) {
        RadioButton radioButton = rootView.findViewById(radioButtonId);
        if (radioButton != null) {
            radioButton.setChecked(checked);
        }
    }

    private void resetSpinner(int spinnerId) {
        Spinner spinner = rootView.findViewById(spinnerId);
        if (spinner != null && spinner.getAdapter() != null && spinner.getAdapter().getCount() > 0) {
            spinner.setSelection(0);
        }
    }

    private void resetTextInput(int editTextId) {
        EditText editText = rootView.findViewById(editTextId);
        if (editText != null) {
            editText.setText("");

            // Clear error if any
            View parent = (View) editText.getParent();
            while (parent != null) {
                if (parent instanceof TextInputLayout) {
                    ((TextInputLayout) parent).setError(null);
                    break;
                }
                parent = (View) parent.getParent();
            }
        }
    }

    private void setViewVisibility(int viewId, int visibility) {
        View view = rootView.findViewById(viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    // For regular EditText fields (not in TextInputLayout)
    private void resetEditText(int editTextId) {
        EditText editText = rootView.findViewById(editTextId);
        if (editText != null) {
            editText.setText("");
        }
    }

    // For TextViews if needed
    private void resetTextView(int textViewId) {
        TextView textView = rootView.findViewById(textViewId);
        if (textView != null) {
            textView.setText("");
        }
    }
}
