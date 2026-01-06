package in.eoninfotech.eontechnician.helper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
 // ← change to your app’s R

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.eoninfotech.eontechnician.R;

public class ImagePopupHelper {

    public static final int REQUEST_CODE_CAMERA = 1001;
    public static final int REQUEST_CODE_GALLERY = 1002;
    public static final String IMAGE_DIRECTORY_NAME = "MyAppImages";

    public interface ImagePopupCallback {
        void onCameraSelected(File outputFile);
        void onGallerySelected();
    }

    public static void showImagePopup(FragmentActivity activity, ImagePopupCallback callback) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = LayoutInflater.from(activity).inflate(R.layout.custom_popup_image, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ImageView cross = view.findViewById(R.id.cross);
        TextView gallery = view.findViewById(R.id.gallery);
        TextView camera = view.findViewById(R.id.cammera);

        gallery.setOnClickListener(v -> {
            dialog.dismiss();
            callback.onGallerySelected();
        });

        camera.setOnClickListener(v -> {
            dialog.dismiss();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            } else {
                File outputFile = createImageFile();
                callback.onCameraSelected(outputFile);
            }
        });

        cross.setOnClickListener(v -> dialog.dismiss());
    }

    private static File createImageFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    public static void openCamera(Activity activity, File outputFile) {
        if (outputFile == null) return;
        Uri uri = Uri.fromFile(outputFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public static void openGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }
}
