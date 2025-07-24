package in.eoninfotech.eontechnician.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import in.eoninfotech.eontechnician.helper.K;

public class PermissionUtils {

    private static final int CAMERA_PERMISSION_CODE = 2001;

    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    public static void failureData(Activity activity) {
        try {
            Toast.makeText(activity, K.TRY_AGAIN, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            try {
                Toast.makeText(activity, K.TRY_AGAIN, Toast.LENGTH_LONG).show();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
