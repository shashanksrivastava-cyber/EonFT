package in.eoninfotech.eontechnician.myprovider;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by harsh_jatinder on 11-12-2017.
 */

public final class StringUtils {

    public static boolean isEmpty(String s) {
        return (s == null || s.isEmpty() || s.equals("null"));
    }

    public static Spanned stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
