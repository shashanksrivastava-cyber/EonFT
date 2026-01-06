package in.eoninfotech.eontechnician.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class CameraHelper {

    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int GALLERY_REQUEST_CODE = 200;

    // Launch camera
    public static void openCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    // Launch gallery
    public static void openGallery(Activity activity) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
    }

    // Example method to save image to file or convert to Base64 if needed
    public static Uri saveCapturedImage(Intent data) {
        return data.getData(); // Customize later for real saving
    }
}
