package in.eoninfotech.eontechnician.adapters;

import android.widget.ArrayAdapter;

import androidx.databinding.BindingAdapter;

import java.util.List;

import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.R;

public class SpinnerBindingAdapters {

    @BindingAdapter("entries")
    public static void setSpinnerEntries(
            MySearchableSpinner spinner,
            List<String> entries) {

        if (entries == null || entries.isEmpty()) return;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                R.layout.simple_custom_spinner_item,
                entries
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
    }
}
