package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
/**
 * Created by root on 25/2/19.
 */

public class CallSheetFragment extends Fragment implements ProgressRequestBody.UploadCallbacks {

    View v;
    ProgressBar mProgress;
    int pStatus = 0;
    int id = 1;
    private Handler handler = new Handler();
    RelativeLayout circularRelative;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id, version, buttonPressed = "0";
    int year, month, day;
    String current_date, selected_todate, s_date,months,currents_date,selecteds_todate;
    private AlertDialog progressDialog;
    Calendar calen = Calendar.getInstance();
    TextView preview,tv;
    ImageView ivProfile;
    File file;
    String path;
    Uri uri;
    EditText datee,remarks;
    Button update_dataa;
    private final int SELECT_PHOTO = 1;
    ProgressDialog pDialog;
    MultipartBody.Part image;
    int REQUEST_CODE_PERMISSION = 10;
    Button upload_img_view;
    public static final String IMAGE_DIRECTORY_NAME = "android_file";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    long length;
    File final_file;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    Bitmap bmp;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout update;
    int PERMISSION_ALL = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_call_sheet, container, false);
        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        datee = v.findViewById(R.id.date);
        upload_img_view = v.findViewById(R.id.upload_img);
        ivProfile = v.findViewById(R.id.ivProfile);
        remarks = v.findViewById(R.id.remarks);
        update = v.findViewById(R.id.update);
        datee.setInputType(InputType.TYPE_NULL);
        circularRelative = v.findViewById(R.id.circularRelative);
        recyclerView = v.findViewById(R.id.recyclerView);
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomCallSheet);
        setDateAndTime();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonPressed.equals("0")) {
                    Toast.makeText(getActivity(), "Please Upload image", Toast.LENGTH_SHORT).show();
                } else if (buttonPressed.equals("1")) {
                    try {
                        final_file = bitmapToFile(bmp, "image_call");
                        length = final_file.length();
                        ProgressRequestBody fileBody = new ProgressRequestBody(final_file, this);
                        image = MultipartBody.Part.createFormData("image", final_file.getName(), fileBody);
                        uploadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (buttonPressed.equals("2")) {
                    try {
                        final_file = new File(path);
                        length = final_file.length();
                        ProgressRequestBody fileBody = new ProgressRequestBody(final_file, this);
                        image = MultipartBody.Part.createFormData("image", path, fileBody);
                        uploadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        upload_img_view.setOnClickListener(v -> {

            View alet_view = null;
            final Dialog alertDialogBuilder = new Dialog(getActivity());
            alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            alet_view = mInflater.inflate(R.layout.bottom_sheet_dialog_callsheet, null);
            final TextView galary = alet_view.findViewById(R.id.menu_gallery);
            final TextView cammera = alet_view.findViewById(R.id.menu_camera);
            alertDialogBuilder.setContentView(alet_view);
            alertDialogBuilder.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            alertDialogBuilder.setCanceledOnTouchOutside(true);
            alertDialogBuilder.show();
            galary.setOnClickListener((View view) -> {
                alertDialogBuilder.dismiss();
                buttonPressed = "2";
                galleryIntent();
            });
            cammera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPressed = "1";
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                        if (Build.VERSION.SDK_INT < 24) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            uri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            try {
                                openCameraIntent();
                                alertDialogBuilder.dismiss();
                            } catch (SecurityException e) {
                                e.printStackTrace();
                                try {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                                        // permission wasn't granted
                                    } else {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                                    }
                                } catch (Exception qe) {
                                    qe.printStackTrace();
                                }openCameraIntent();
                                alertDialogBuilder.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                alertDialogBuilder.dismiss();
                            }
                        } else {
                            try {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                                    // permission wasn't granted
                                } else {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                                }
                            } catch (Exception qe) {
                                qe.printStackTrace();
                            }
                            openCameraIntent();
                            alertDialogBuilder.dismiss();
                        }
                    }
                }
                File getOutputMediaFile(int type) {
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d("****", "Oops! Failed create "+ IMAGE_DIRECTORY_NAME +" directory");
                            return null;
                        }
                    }String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File mediaFile;
                    if (type == MEDIA_TYPE_IMAGE) {
                        mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");
                        Log.d("****", "");
                    } else {
                        return null;
                    }
                    return mediaFile;
                }
            });
        });
        return v;
    }

    private void setDateAndTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }if(month==1){
            months = "Jan";
        }else if(month==2){
            months = "Feb";
        }else if(month==3){
            months = "Mar";
        }else if(month==4){
            months = "Apr";
        }else if(month==5){
            months = "May";
        }else if(month==6){
            months = "Jun";
        }else if(month==7){
            months = "Jul";
        }else if(month==8){
            months = "Aug";
        }else if(month==9){
            months = "Sep";
        }else if(month==10){
            months = "Oct";
        }else if(month==11){
            months = "Nov";
        }else if(month==12){
            months = "Dec";
        }
        currents_date = months+ " " +day + " , " + year;
        datee.setText(currents_date);
        //String abc = datee.getText().toString();
        String[] separated = current_date.split("-");
        String date =  separated[0];
        String month = separated[1];
        String years = separated[2];
        String dates = years + "-" + month + "-" + date;
        s_date = dates;
        datee.setOnClickListener(v -> getDate());
    }
    private void getDate() {
        // TODO Auto-generated method stub
        final DatePickerDialog dpdd = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            if (monthOfYear + 1 < 10) {
                selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
            } else {
                selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            }
            if((monthOfYear + 1)==1){
                months = "Jan";
            }else if((monthOfYear + 1)==2){
                months = "Feb";
            }else if((monthOfYear + 1)==3){
                months = "Mar";
            }else if((monthOfYear + 1)==4){
                months = "Apr";
            }else if((monthOfYear + 1)==5){
                months = "May";
            }else if((monthOfYear + 1)==6){
                months = "Jun";
            }else if((monthOfYear + 1)==7){
                months = "Jul";
            }else if((monthOfYear + 1)==8){
                months = "Aug";
            }else if((monthOfYear + 1)==9){
                months = "Sep";
            }else if((monthOfYear + 1)==10){
                months = "Oct";
            }else if((monthOfYear + 1)==11){
                months = "Nov";
            }else if((monthOfYear + 1)==12){
                months = "Dec";
            }
            selecteds_todate = months+ " " +dayOfMonth + " , " + year;
            datee.setText(selecteds_todate);
           // String abc = datee.getText().toString();
            String[] separated = selected_todate.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            String dates = years + "-" + month + "-" + date;
            s_date = dates;
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void uploadData() {
        String callSheetRemark = remarks.getText().toString();
        progressDialog.show();
        ApiHolder uploadImage = ServiceConnectionNewURL.getClient().create(ApiHolder.class);
        RequestBody tech_name = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), s_date);
        RequestBody remark = RequestBody.create(MediaType.parse("text/plain"),callSheetRemark);
        Call<UpdateDataResponse> call = uploadImage.call_sheet(image, tech_name, date,remark);
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                UpdateDataResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body());
                if (updateDataResponse != null) {
                    if (updateDataResponse.getType() == 1) {
                        Toast.makeText(getActivity(), "" + updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        datee.setText(current_date);
                        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.noimage));
                        remarks.setText("");
                        buttonPressed="0";
                    }
                } else {
                    assert updateDataResponse != null;
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                progressDialog.hide();
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("****", "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            Log.d("****", "");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT < 24) {
                try {
                    path = uri.getPath();
                    file = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Click Again", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                bmp = BitmapFactory.decodeFile(path);
                ivProfile.setImageBitmap(bmp);
            }
        } else if (requestCode == SELECT_PHOTO)
            onSelectFromGalleryResult(data);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
            } else {
                // permission wasn't granted
            }
        }
    }
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            file = FileUtils.getFile(getActivity(), uri);
            path = file.getPath();
            compressImage(path);
            bmp = BitmapFactory.decodeFile(path);
            ivProfile.setImageBitmap(bmp);
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(getActivity(), "in.eoninfotech.eontechnician.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    // permission wasn't granted
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                    startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                }
            }
        }
    }
    public String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        path = getFilename();
        try {
            out = new FileOutputStream(path);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DCIM/Camera");
        if (!file.exists()) {
            file.mkdirs();
        }String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName,/* prefix */".jpg",/* suffix */storageDir/* directory */);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = image.getAbsolutePath();
        return image;
    }
    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File filesDir = getActivity().getFilesDir();
        File imageFile = new File(filesDir, fileName + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(int percentage) {
    }
    @Override
    public void onError() {
    }
    @Override
    public void onFinish() {
    }

}
