package in.eoninfotech.eontechnician.utils;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;
public class DialogUtils {

    public interface OnDateSelected {
        void onDate(int year, int month, int day);
    }

    public interface OnTimeSelected {
        void onTime(int hour, int minute);
    }

    public static void showAlert(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showConfirmation(Context context, String title, String message,
                                        DialogInterface.OnClickListener onYes,
                                        DialogInterface.OnClickListener onNo) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", onYes)
                .setNegativeButton("No", onNo)
                .show();
    }

    public static ProgressDialog showLoading(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void showDatePicker(Context context, final OnDateSelected listener) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    if (listener != null) listener.onDate(year1, monthOfYear + 1, dayOfMonth);
                }, year, month, day);
        datePickerDialog.show();
    }

    public static void showTimePicker(Context context, final OnTimeSelected listener) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hourOfDay, minute1) -> {
                    if (listener != null) listener.onTime(hourOfDay, minute1);
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public static void showConfirmationDialog(
            Context context,
            String title,
            String message,
            String positiveText,
            String negativeText,
            final Runnable onPositiveClick,
            final Runnable onNegativeClick
    ) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText != null ? positiveText : "Yes", (dialog, which) -> {
                    dialog.dismiss();
                    if (onPositiveClick != null) onPositiveClick.run();
                })
                .setNegativeButton(negativeText != null ? negativeText : "Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    if (onNegativeClick != null) onNegativeClick.run();
                })
                .setCancelable(false);


        builder.show();
    }

    // Overloaded method with default values (for simpler usage)
    public static void showConfirmationDialog(
            Context context,
            String title,
            String message,
            Runnable onPositiveClick
    ) {
        showConfirmationDialog(
                context,
                title,
                message,
                "Yes", // positiveText (default)
                "Cancel", // negativeText (default)
                onPositiveClick,
                null  // onNegativeClick (null = no action)
        );
    }
}
