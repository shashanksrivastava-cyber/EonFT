package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.utils.ImageUtil;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.databinding.FragmentBillIntimationBinding;
import in.eoninfotech.eontechnician.helper.FileUtils;
import in.eoninfotech.eontechnician.helper.K;
import in.eoninfotech.eontechnician.helper.ProgressRequestBody;
import in.eoninfotech.eontechnician.utils.DatePickerUtil;
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


public class BillIntimationFragment extends Fragment implements ProgressRequestBody.UploadCallbacks   {

    FragmentBillIntimationBinding binding;
    Calendar calen = Calendar.getInstance();
    int year, month, day;
    String current_date, selected_todate, s_remarks = "", s_amount, s_from_date, s_to_date, version, username,bill_amt_limit="";
    View v;
    private ProgressDialog pDialog;
    File file;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA = 0;
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

    private MutableLiveData<String> fromDateObs = new MutableLiveData<>("");
    private MutableLiveData<String> toDateObs = new MutableLiveData<>("");
    private MutableLiveData<Bitmap> capturedBitmapObs = new MutableLiveData<>(null);

    // Existing logic variables

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bill_intimation, container, false);
        binding = FragmentBillIntimationBinding.inflate(getLayoutInflater(), container, false);

        sharedprefs = this.getActivity().getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        bill_amt_limit = sharedprefs.getString("bill_amt_limit", "");

        binding.linearBill.setVisibility(View.GONE);
        binding.fromDate.setInputType(InputType.TYPE_NULL);
        binding.fromDate.setFocusable(false);
        binding.fromDate.setFocusableInTouchMode(false);

        binding.toDate.setInputType(InputType.TYPE_NULL);
        binding.toDate.setFocusable(false);
        binding.toDate.setFocusableInTouchMode(false);

        binding.billNo.setInputType(InputType.TYPE_NULL);
        progressBar = v.findViewById(R.id.progressBar);
        binding.relImage.setVisibility(View.GONE);
        binding.image.setVisibility(View.VISIBLE);
        int bill_amt = Integer.parseInt(bill_amt_limit);
        binding.amount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(bill_amt) });

        binding.fromDate.setOnClickListener(v ->
                DatePickerUtil.showDatePicker(getContext(), binding.fromDate, 0, 12, false)
        );

        binding.toDate.setOnClickListener(v ->
                DatePickerUtil.showDatePicker(getContext(), binding.toDate, 0, 12, false)
        );

        setDate();

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.relImage.setVisibility(View.GONE);
                binding.image.setVisibility(View.VISIBLE);
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_from_date = binding.fromDate.getText().toString();
                s_to_date = binding.toDate.getText().toString();
                s_amount = binding.amount.getText().toString();
                s_remarks = binding.remarks.getText().toString();

                if (s_amount.equalsIgnoreCase("")) {
                    binding.amount.setError("Enter Amount");
                } else if(binding.relImage.getVisibility()!=View.VISIBLE){
                    submitData();
                    binding.image.setVisibility(View.GONE);
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

        binding.image.setOnClickListener(new View.OnClickListener() {
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

        return binding.getRoot();
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
                    binding.linearBill.setVisibility(View.VISIBLE);
                    binding.billNo.setText(updateDataResponse.getBill_no());
                    pDialog.dismiss();
                    pDialog.hide();
                    binding.relImage.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                    binding.remarks.setText("");
                    binding.amount.setText("");
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
        binding.fromDate.setText(current_date);
        binding.toDate.setText(current_date);
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
                binding.relImage.setVisibility(View.VISIBLE);
                binding.image.setVisibility(View.GONE);
                binding.Camera.setImageBitmap(bmp);
            }
        } else if (requestCode == SELECT_PHOTO)
            onSelectFromGalleryResult(data);
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            file = FileUtils.getFile(getActivity(), uri);
            path = file.getPath();
            ImageUtil.compressImage(path);
            bmp = BitmapFactory.decodeFile(path);
            binding.relImage.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.GONE);
            binding.Camera.setImageBitmap(bmp);
        }
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DCIM/Camera");
        if (!file.exists()) {
            file.mkdirs();
        }String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
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

//        ComposeView composeView = new ComposeView(requireContext());
//
//        // Clean up composition when fragment view is destroyed
//        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed.INSTANCE);
//
//        // Initial Data Setup
//        initData();
//
//        composeView.setContent(androidx.compose.runtime.internal.ComposableLambdaKt.composableLambdaInstance(-1, true, (composer, index) -> {
//            // Bridge: Calling the Kotlin Composable from Java
//            BillIntimationScreenKt.BillIntimationScreen(
//                    fromDateObs.getValue(),
//                    toDateObs.getValue(),
//                    capturedBitmapObs.getValue(),
//                    () -> { openDatePicker(true); return null; },
//                    () -> { openDatePicker(false); return null; },
//                    () -> { showImageSourceDialog(); return null; },
//                    () -> { capturedBitmapObs.setValue(null); bmp = null; return null; },
//                    (amount, remarks) -> { validateAndSubmit(amount, remarks); return null; },
//                    composer, 0
//            );
//            return null;
//        }));
//
//        return composeView;
//    }
//
//    private void initData() {
//        SharedPreferences sharedprefs = requireActivity().getSharedPreferences("login_user_pass", Context.MODE_PRIVATE);
//        username = sharedprefs.getString("s_uuser", "");
//        version = sharedprefs.getString("version", "");
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        current_date = sdf.format(new Date());
//        fromDateObs.setValue(current_date);
//        toDateObs.setValue(current_date);
//    }
//
//    private void openDatePicker(boolean isFrom) {
//        Calendar cal = Calendar.getInstance();
//        new DatePickerDialog(requireContext(), (view, y, m, d) -> {
//            String selected = d + "-" + (m + 1) + "-" + y;
//            if (isFrom) fromDateObs.setValue(selected);
//            else toDateObs.setValue(selected);
//        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//    private void showImageSourceDialog() {
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(R.layout.bottom_sheet_dialog_callsheet);
//
//        dialog.findViewById(R.id.menu_gallery).setOnClickListener(v -> {
//            dialog.dismiss();
//            galleryIntent();
//        });
//
//        dialog.findViewById(R.id.menu_camera).setOnClickListener(v -> {
//            dialog.dismiss();
//            openCameraIntent();
//        });
//
//        dialog.show();
//    }
//
//    private void validateAndSubmit(String amount, String remarks) {
//        if (amount.isEmpty()) {
//            Toast.makeText(getActivity(), "Enter Amount", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        MultipartBody.Part imagePart = null;
//        if (bmp != null) {
//            File final_file = bitmapToFile(bmp, "bill_image");
//            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), final_file);
//            imagePart = MultipartBody.Part.createFormData("image", final_file.getName(), fileBody);
//        }
//
//        submitData(amount, remarks, imagePart);
//    }
//
//    private void submitData(String amount, String remarks, MultipartBody.Part imagePart) {
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        // Use your existing Retrofit connection
//        // ApiHolder api = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
//        // ... Call enqueue logic here as per your original file
//    }
//
//    private void openCameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File photoFile = new File(requireActivity().getExternalFilesDir(null), "temp_bill.jpg");
//        path = photoFile.getAbsolutePath();
//        // Use your FileProvider here
//        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
//    }
//
//    private void galleryIntent() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_CAPTURE_IMAGE) {
//                bmp = BitmapFactory.decodeFile(path);
//                capturedBitmapObs.setValue(bmp);
//            } else if (requestCode == SELECT_PHOTO && data != null) {
//                try {
//                    uri = data.getData();
//                    bmp = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
//                    capturedBitmapObs.setValue(bmp);
//                } catch (Exception e) { e.printStackTrace(); }
//            }
//        }
//    }
//
//    private File bitmapToFile(Bitmap bitmap, String fileName) {
//        File imageFile = new File(requireActivity().getFilesDir(), fileName + ".jpg");
//        try (OutputStream os = new FileOutputStream(imageFile)) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
//            return imageFile;
//        } catch (Exception e) { return null; }
//    }
}