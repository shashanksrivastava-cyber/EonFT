package in.eoninfotech.eontechnician.adapters;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.List;

import in.eoninfotech.eontechnician.view.MySearchableSpinner;

public class SpinnerBindingAdapter {

    // Set items list in Spinner
    @BindingAdapter("spinnerItems")
    public static void setSpinnerItems(MySearchableSpinner spinner, List<String> items) {
        if (items == null) return;

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(spinner.getContext(),
                        android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    // Bind selected value from ViewModel → Spinner
    @BindingAdapter("selectedValue")
    public static void setSelectedValue(MySearchableSpinner spinner, String value) {
        if (value == null) return;
        if (spinner.getAdapter() == null) return;

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);

        if (position >= 0 && spinner.getSelectedItemPosition() != position) {
            spinner.setSelection(position);
        }
    }

    // Bind Spinner → ViewModel (two-way)
    @InverseBindingAdapter(attribute = "selectedValue")
    public static String getSelectedValue(MySearchableSpinner spinner) {
        Object selected = spinner.getSelectedItem();
        return selected != null ? selected.toString() : "";
    }

    // Listen for changes
    @BindingAdapter("selectedValueAttrChanged")
    public static void setSpinnerListener(MySearchableSpinner spinner,
                                          final InverseBindingListener listener) {

        if (listener == null) return;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
