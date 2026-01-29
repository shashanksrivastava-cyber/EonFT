package in.eoninfotech.eontechnician.activity;

import android.app.AlertDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import in.eoninfotech.eontechnician.service.SessionManager;

public class BaseActivity extends AppCompatActivity  {

    private boolean isLogoutDialogShowing = false;

    public void showForceLogoutDialog(String message) {

        if (isFinishing() || isDestroyed() || isLogoutDialogShowing) return;
        isLogoutDialogShowing = true;

        new AlertDialog.Builder(this)
                .setTitle("Session Expired")
                .setMessage(message != null && !message.isEmpty() ? message : "Your session has expired. Please login again.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    isLogoutDialogShowing = false;

                    SessionManager.clearSession(BaseActivity.this);

                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }
}
