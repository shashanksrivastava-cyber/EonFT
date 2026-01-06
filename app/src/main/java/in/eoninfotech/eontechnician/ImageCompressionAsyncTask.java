package in.eoninfotech.eontechnician;

import android.os.AsyncTask;

import in.eoninfotech.eontechnician.utils.ImageUtil;

/**
 * Created by root on 24/1/19.
 */

public abstract class ImageCompressionAsyncTask extends AsyncTask<String, Void, byte[]> {

    @Override
    protected byte[] doInBackground(String... strings) {
        if(strings.length == 0 || strings[0] == null)
            return null;
        return ImageUtil.compressImage(strings[0]);
    }

    protected abstract void onPostExecute(byte[] imageBytes) ;
}
