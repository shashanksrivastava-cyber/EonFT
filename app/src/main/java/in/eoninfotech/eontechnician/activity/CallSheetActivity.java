package in.eoninfotech.eontechnician.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.MainActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thefinestartist.utils.content.ContextUtil.getApplicationContext;
import static com.thefinestartist.utils.content.ContextUtil.getContentResolver;
import static com.thefinestartist.utils.content.ContextUtil.getExternalFilesDir;
import static com.thefinestartist.utils.content.ContextUtil.getPackageManager;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class CallSheetActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id, version, buttonPressed="0";
    String selected_todate, s_update_date;
    int year, month, day;
    Calendar calen;
    String filenames;
    EditText remarks;
    TextView datee;
    CircleImageView ivProfile;
    File file;
    String path;
    Uri uri;
    Button update_dataa;
    private final int SELECT_PHOTO = 1;
    String mediaPath;
    ProgressDialog pDialog;
    MultipartBody.Part fileToUpload;
    RequestBody filename;
    int REQUEST_CODE_PERMISSION = 10;
    ImageView upload_img_view;
    public static final String IMAGE_DIRECTORY_NAME = "android_file";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    int PERMISSION_ALL = 1;
    File final_file;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String img;
    RequestBody requestBody;
    Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sheet);
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("CALL SHEET");

        datee = findViewById(R.id.date);
        update_dataa = findViewById(R.id.update_data);
        upload_img_view = findViewById(R.id.upload_img);
        ivProfile = findViewById(R.id.ivProfile);
        remarks = findViewById(R.id.remarks);

//        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);
//        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
//        spaceNavigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_home));
//        spaceNavigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_incentive ));
//
//        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
//            @Override
//            public void onCentreButtonClick() {
//                View alet_view = null;
//                final Dialog alertDialogBuilder = new Dialog(CallSheetActivity.this,R.style.DialogSlideAnim);
//                getWindow().setGravity(Gravity.BOTTOM);
//                alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                LayoutInflater mInflater = (LayoutInflater) CallSheetActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//                alet_view = mInflater.inflate(R.layout.custom_popup_image, null);
//                final ImageView cross = (ImageView) alet_view.findViewById(R.id.cross);
//                final TextView galary = (TextView) alet_view.findViewById(R.id.gallery);
//                final TextView cammera = (TextView) alet_view.findViewById(R.id.cammera);
//                alertDialogBuilder.setContentView(alet_view);
//                alertDialogBuilder.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                alertDialogBuilder.setCanceledOnTouchOutside(true);
//                alertDialogBuilder.show();

//                BottomSheetMenuDialog dialog = new BottomSheetBuilder(CallSheetActivity.this, R.style.AppTheme_BottomSheetDialog)
//                        .addTitleItem("Select")
//                        .setTitleTextColor(getResources().getColor(R.color.black))
//                        .setMode(BottomSheetBuilder.MODE_LIST)
//                        .setMenu(R.menu.menu_bottom_simple_sheet)
//                        .setItemClickListener(new BottomSheetItemClickListener() {
//                            @Override
//                            public void onBottomSheetItemClick(MenuItem item) {
//
//                            }
//                        })
//                        .createDialog();
//
//                dialog.show();

//                new BottomSheet.Builder(CallSheetActivity.this)
//                        .title("Select Image ")
//                        .sheet(R.menu.menu_bottom_simple_sheet).listener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (which == R.id.menu_camera) {
//                            buttonPressed = "1";
//                            if (Build.VERSION.SDK_INT < 24) {
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                uri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                                try {
//                                    openCameraIntent();
//                                } catch (SecurityException e) {
//                                    e.printStackTrace();
//                                    try {
//                                        if (hasPermissions(CallSheetActivity.this, PERMISSIONS)) {
//                                        } else {
//                                            EasyPermissions.requestPermissions(this, "Access for storage",101, PERMISSIONS);
//                                        }
//                                    } catch (Exception qe) {
//                                        qe.printStackTrace();
//                                    }
//                                    openCameraIntent();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                try {
//                                    if (hasPermissions(CallSheetActivity.this, PERMISSIONS)) {
//                                    } else {
//                                        EasyPermissions.requestPermissions(this, "Access for storage",101, PERMISSIONS);
//                                    }
//                                } catch (Exception qe) {
//                                    qe.printStackTrace();
//                                }
//                                openCameraIntent();
//                            }
//                        }
//                    else if(which == R.id.menu_gallery){
//                            buttonPressed="2";
//                            galleryIntent();
//                        }
//                    }
//                }).show();
//            }
//
//            @Override
//            public void onItemClick(int itemIndex, String itemName) {
//            }
//            @Override
//            public void onItemReselected(int itemIndex, String itemName) {
//            }
//        });

        calen = Calendar.getInstance();
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            selected_todate = day + "-0" + +month + "-" + year;
        } else {
            selected_todate = day + "-" + month + "-" + year;
        }try {
            //checkWritingPermission();
            if (!hasPermissions(CallSheetActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(CallSheetActivity.this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        datee.setText(selected_todate);
        datee.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            DatePickerDialog dpdd = new DatePickerDialog(CallSheetActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                }datee.setText(selected_todate);
            }, year, month - 1, day);
            dpdd.show();
        });
        upload_img_view.setOnClickListener(v -> {
            Intent intent = new Intent(CallSheetActivity.this,ImageDetailCallSheet.class);
            intent.putExtra("Image",path);
            startActivity(intent);
        });

        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(CallSheetActivity.this);
                pDialog.setMessage("Uploading...");
                pDialog.setCancelable(false);
                pDialog.show();
                s_update_date = datee.getText().toString();
                String callSheetRemark = remarks.getText().toString();
                try {
//                    if (mediaPath.equals("") || mediaPath.equals(null)) {
//                        Toast.makeText(CallSheetActivity.this, "Please Click image", Toast.LENGTH_LONG).show();
//                        pDialog.dismiss();
//                    } else {
                    if (buttonPressed.equals("1")) {
                        try {
                            final_file = bitmapToFile(bmp, "image_call");
                            requestBody = RequestBody.create(MediaType.parse("*/*"), final_file);
                            fileToUpload = MultipartBody.Part.createFormData("image", final_file.getName(), requestBody);
                            filename = RequestBody.create(MediaType.parse("text/plain"), final_file.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if(buttonPressed.equals("2")){
                        try {
                            final_file = new File(path);
                            long length = final_file.length();
                           // ProgressRequestBody fileBody = new ProgressRequestBody(final_file, this);
                            fileToUpload = MultipartBody.Part.createFormData("image", path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                        ApiHolder uploadImage = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
                        RequestBody tech_name = RequestBody.create(MediaType.parse("text/plain"), username);
                        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), s_update_date);
                        RequestBody remark = RequestBody.create(MediaType.parse("text/plain"),callSheetRemark);
                        Call<UpdateDataResponse> call = uploadImage.call_sheet(fileToUpload, tech_name, date,remark);
                        call.enqueue(new Callback<UpdateDataResponse>() {
                            @Override
                            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                                UpdateDataResponse updateDataResponse = response.body();
                                Log.i("**respnse", " " + response.body());
                                if (updateDataResponse != null) {
                                    Toast.makeText(CallSheetActivity.this, updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (updateDataResponse.getType() == 1) {
                                    }
                                } else {
                                    assert updateDataResponse != null;
                                }
                                pDialog.dismiss();
                            }
                            @Override
                            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                                t.printStackTrace();
                                pDialog.dismiss();
                                Toast.makeText(CallSheetActivity.this, "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
                            }
                        });
                  //  }
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                    Toast.makeText(CallSheetActivity.this, "Please click image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void galleryIntent() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_PHOTO);
    }
    File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
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
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT < 24) {
                try {
                    path = uri.getPath();
                    file = new File(path);
                    String abc = path;
                    filenames=abc.substring(abc.lastIndexOf("/")+1);
                    //imageName.setText(filename);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CallSheetActivity.this, "Click Again", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                bmp = BitmapFactory.decodeFile(path);
                ivProfile.setImageBitmap(bmp);
                String abc = path;
                filenames=abc.substring(abc.lastIndexOf("/")+1);
               // imageName.setText(filename);
            }
        }else if (requestCode == SELECT_PHOTO)
            onSelectFromGalleryResult(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
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
            file = FileUtils.getFile(CallSheetActivity.this,uri);
            path = file.getPath();
            compressImage(path);
            File file = new File(path);
           // imageName.setText(file.getName());
            bmp = BitmapFactory.decodeFile(path);
            ivProfile.setImageBitmap(bmp);
        }
    }
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(CallSheetActivity.this, "in.eoninfotech.eontechnician.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
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
            }scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }FileOutputStream out = null;
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
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName,/* prefix */".jpg",/* suffix */storageDir/* directory */);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = image.getAbsolutePath();
        return image;
    }
    private File bitmapToFile(Bitmap bitmap,String fileName){
        File filesDir = getApplicationContext().getFilesDir();
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

    @Override
    public Dialog onCreateDialog(int id) {
        return null;
    }

//    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//        float bitmapRatio = (float) width / (float) height;
//        if (bitmapRatio > 0) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }
//
//    private File bitmapToFile(Bitmap bitmap,String fileName){
//        File filesDir = getApplicationContext().getFilesDir();
//        File imageFile = new File(filesDir, fileName + ".jpg");
//        OutputStream os;
//        try {
//            os = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
//            os.flush();
//            os.close();
//            return imageFile;
//        } catch (Exception e) {
//            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
//        }
//        return null;
//    }


}