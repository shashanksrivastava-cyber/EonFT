package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static in.eoninfotech.eontechnician.fragments.CallSheetFragment.IMAGE_DIRECTORY_NAME;


public class BillIntimationFragment extends Fragment implements ProgressRequestBody.UploadCallbacks {

    EditText fromDate, toDate, amount, remarks, billNo,edit_image;
    Calendar calen = Calendar.getInstance();
    int year, month, day;
    RelativeLayout rel_image;
    ImageView Camera,btnCancel;
    String current_date, selected_todate, s_remarks = "", s_amount, s_from_date, s_to_date, version, username,bill_amt_limit="";
    View v;
    private ProgressDialog pDialog;
    File file;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA = 0;
    LinearLayout submit, linearBill;
    ProgressBar progressBar;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    MultipartBody.Part image;
    long length;
    File final_file;
    int REQUEST_CODE_PERMISSION = 10;
    private final int SELECT_PHOTO = 1;
    String path;
    Uri uri;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    Bitmap bmp;
    int PERMISSION_ALL = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bill_intimation, container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        bill_amt_limit = sharedprefs.getString("bill_amt_limit", "");

        fromDate = v.findViewById(R.id.fromDate);
        toDate = v.findViewById(R.id.toDate);
        amount = v.findViewById(R.id.amount);
        remarks = v.findViewById(R.id.remarks);
        billNo = v.findViewById(R.id.billNo);
        submit = v.findViewById(R.id.submit);
        edit_image = v.findViewById(R.id.image);
        linearBill = v.findViewById(R.id.linearBill);
        rel_image = v.findViewById(R.id.rel_image);
        Camera = v.findViewById(R.id.Camera);
        btnCancel = v.findViewById(R.id.btnCancel);
        linearBill.setVisibility(View.GONE);
        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);
        billNo.setInputType(InputType.TYPE_NULL);
        progressBar = v.findViewById(R.id.progressBar);
        rel_image.setVisibility(View.GONE);
        edit_image.setVisibility(View.VISIBLE);
        int bill_amt = Integer.parseInt(bill_amt_limit);
        amount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(bill_amt) });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromDate();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToDate();
            }
        });

        setDate();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_image.setVisibility(View.GONE);
                edit_image.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_from_date = fromDate.getText().toString();
                s_to_date = toDate.getText().toString();
                s_amount = amount.getText().toString();
                s_remarks = remarks.getText().toString();

                if (s_amount.equalsIgnoreCase("")) {
                    amount.setError("Enter Amount");
                } else if(rel_image.getVisibility()!=View.VISIBLE){
                    submitData();
                    edit_image.setVisibility(View.GONE);
                }
                else {
                    final_file = bitmapToFile(bmp, "image_call");
                    length = final_file.length();
                    ProgressRequestBody fileBody = new ProgressRequestBody(final_file, this);
                    image = MultipartBody.Part.createFormData("image", final_file.getName(), fileBody);
                    submitData();
                }
            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    galleryIntent();
                });
                cammera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
            }
        });

        return v;
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
    }

    private void submitData() {

        try {
            pDialog = K.createProgressDialog(getActivity());
            pDialog.setMessage("Loading");
            pDialog.show();
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        RequestBody user_name = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody from_date = RequestBody.create(MediaType.parse("text/plain"), s_from_date);
        RequestBody to_date = RequestBody.create(MediaType.parse("text/plain"),s_to_date);
        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"),s_amount);
        RequestBody remark = RequestBody.create(MediaType.parse("text/plain"),s_remarks);
        Call<MainResponse> call = log_att.submit_bill(user_name,from_date,to_date,amount,remark,image);
        Log.i("****call", String.valueOf(call));
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                MainResponse updateDataResponse = response.body();
                Log.i("**respnse", " " + response.body().getType());

                if (updateDataResponse.getType() == 1) {
                    linearBill.setVisibility(View.VISIBLE);
                    billNo.setText(updateDataResponse.getBill_no());
                    pDialog.dismiss();
                    pDialog.hide();
                }else {
                    Toast.makeText(getActivity(), ""+updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                pDialog.hide();
                pDialog.dismiss();
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
                ShowProgressBar(false);
                Toast.makeText(getActivity(), "Try Again-Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
        fromDate.setText(current_date);
        toDate.setText(current_date);
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

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getToDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                toDate.setText(selected_todate);
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void getFromDate() {
        final DatePickerDialog dpdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // TODO Auto-generated method stub
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear + 1 < 10) {
                    selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                } else {
                    selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
                fromDate.setText(selected_todate);
            }
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
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
                rel_image.setVisibility(View.VISIBLE);
                edit_image.setVisibility(View.GONE);
                Camera.setImageBitmap(bmp);
            }
        } else if (requestCode == SELECT_PHOTO)
            onSelectFromGalleryResult(data);
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            file = FileUtils.getFile(getActivity(), uri);
            path = file.getPath();
            compressImage(path);
            bmp = BitmapFactory.decodeFile(path);
            rel_image.setVisibility(View.VISIBLE);
            edit_image.setVisibility(View.GONE);
            Camera.setImageBitmap(bmp);
        }
    }

    private String compressImage(String imageUri) {
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

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DCIM/Camera");
        if (!file.exists()) {
            file.mkdirs();
        }String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
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