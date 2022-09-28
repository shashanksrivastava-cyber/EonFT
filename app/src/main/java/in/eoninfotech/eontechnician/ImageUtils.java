package in.eoninfotech.eontechnician;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by harsh_jatinder on 05-12-2017.
 */

public final class ImageUtils {

    static ArrayList<String> wishlistImageUri = new ArrayList<>();

    public static void glideImage(@NonNull final ImageView imageView, @NonNull final String url) {

        Glide.with(imageView).load(url).apply(new RequestOptions().dontAnimate()).into(imageView);
    }

    public static void glideImage(@NonNull final ImageView imageView, @NonNull final Drawable drawable) {
        Glide.with(imageView).load(drawable).apply(new RequestOptions().dontAnimate()).into(imageView);
    }

    public static void glideImage(@NonNull final ImageView imageView, @NonNull final int resId) {
        Glide.with(imageView).load(resId).apply(new RequestOptions().dontAnimate()).into(imageView);
    }

    public static void catchImages(@NonNull final View view,
                                   @NonNull final ArrayList<String> urls) {
        if (urls.isEmpty()) {
            return;
        }
        for (String url : urls) {
            Glide.with(view).load(url).downloadOnly(1280, 720);
        }
    }

    public static void glideImage(@NonNull final ImageView imageView, @NonNull final String url, @Nullable final Integer placeHolderId) {
        Glide.with(imageView)
                .load(url)
                .apply(new RequestOptions().placeholder(placeHolderId).dontAnimate())
                .into(imageView);
    }

    public static void glideImageWithLoader(@NonNull final ImageView imageView, @NonNull final String url) {
        glideImage(imageView, url);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        return stream.toByteArray();
    }

    public byte[] getBytesFromBitmapCamera(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
