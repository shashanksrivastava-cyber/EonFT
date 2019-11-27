package in.eoninfotech.eontechnician.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.Responses.ReplaceReasons;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.helper.EONUtil;
import in.eoninfotech.eontechnician.view.MySearchableSpinner;
import in.eoninfotech.eontechnician.view.MyTextView;
import in.eoninfotech.eontechnician.Responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 5/10/17.
 */
public class RetroOldInstallmentFragment extends Fragment {
    View v;
    String disttid;
    String uusername, version;
    CheckConnection chk;
    EditText e_new_device_id, e_old_device_id, e_new_drs_id, e_old_drs_id, e_remarks;
    String s_new_device_id = "0", s_reason_repla, s_old_device_id = "0", s_new_drs_id = "0", s_old_drs_id = "0", s_remarks = "0", s_clientid = "0", status, s_drs_status = "0";
    MyTextView t_drs_id, t_drs_old;
    Button update_dataa;
    MySearchableSpinner client, reason_replace;
    HashMap<String, String> client_hashMap = new HashMap<String, String>(); //hashmap used to prevent duplicate values for clientlist.
    ImageView upload_img_view;
    Bitmap bmp;
    int REQUEST_CODE_PERMISSION = 10;
    String img;
    private int REQUEST_CAMERA = 0;
    private int GALLERY_KITKAT_INTENT_CALLED = 300;
    private int REQUEST_CODE_GALLERY = 200;
    String mediaPath;
    ProgressDialog pDialog;
    private Uri fileUri;
    MultipartBody.Part fileToUpload;
    RequestBody filename;
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    ArrayList<ReplaceReasons> arr_replaceReasons = new ArrayList<>();
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ArrayList<String> arr_reasons_s = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    int PERMISSION_ALL = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_replace_install, container, false);
        chk = new CheckConnection(v.getContext());
        disttid = getArguments().getString("disttid");
        uusername = getArguments().getString("usernme");
        version = getArguments().getString("version");
        e_new_device_id = v.findViewById(R.id.old_new_device_id);
        e_old_device_id = v.findViewById(R.id.old_device_id);
        e_new_drs_id = v.findViewById(R.id.old_new_drs_id);
        e_old_drs_id = v.findViewById(R.id.old_drs_id);
        e_remarks = v.findViewById(R.id.old_remarks);
        t_drs_id = v.findViewById(R.id.t_drs_id);
        t_drs_old = v.findViewById(R.id.t_drs_old);
        upload_img_view = v.findViewById(R.id.upload_img);
        client = v.findViewById(R.id.old_s_client);
        reason_replace = v.findViewById(R.id.replace_reason);
        update_dataa = v.findViewById(R.id.old_update);
        Log.i("****stat dist n usr***", disttid + " " + uusername);
        client_hashMap = EONUtil.gettingData(getContext()); //get data from content provider
        addclients();
        fetchReasons();
        try {
            //checkWritingPermission();
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        reason_replace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_reason_repla = arr_replaceReasons.get(reason_replace.getSelectedItemPosition()).getR_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String st_name = String.valueOf(client.getSelectedItem());
                    String st_id = "";
                    for (Map.Entry<String, String> entry : client_hashMap.entrySet()) {
                        if (entry.getValue().equals(st_name.trim())) {
                            st_id = entry.getKey();
                            Log.e("", "KEY VALUE :::" + entry.getKey());
                        }
                    }
                    String CurrentString = st_id;
                    String[] separated = CurrentString.split(":");
                    s_clientid = separated[0].trim();
                    s_drs_status = separated[1].trim();
                    if (s_drs_status.equals("0")) {
                        e_new_drs_id.setVisibility(View.GONE);
                        t_drs_id.setVisibility(View.GONE);
                        e_old_drs_id.setVisibility(View.GONE);
                        t_drs_old.setVisibility(View.GONE);
                    } else {
                        e_new_drs_id.setVisibility(View.VISIBLE);
                        t_drs_id.setVisibility(View.VISIBLE);
                        e_old_drs_id.setVisibility(View.VISIBLE);
                        t_drs_old.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        upload_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImagePopup(getActivity(), "Would you like to upload a photo by taking a new photo with camera?", "", "CAMERA", "");

            }
        });
        update_dataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Loading...");
                pDialog.show();
                s_old_device_id = e_old_device_id.getText().toString();
                s_new_device_id = e_new_device_id.getText().toString();
                s_old_drs_id = e_old_drs_id.getText().toString();
                s_new_drs_id = e_new_drs_id.getText().toString();
                s_remarks = e_remarks.getText().toString();
                if (s_new_device_id.equals("") && s_new_drs_id.equals("")) {
                    Toast.makeText(getContext(), "please fill required fields", Toast.LENGTH_LONG).show();
                } else if (!s_old_drs_id.equals("") && s_new_drs_id.equals("")) {
                    e_new_drs_id.setError("fill field");
                } else if (!s_old_device_id.equals("") && s_new_device_id.equals("")) {
                    e_new_device_id.setError("fill field");
                } else if (s_clientid.equals("") || s_clientid.equals(null)) {
                    Toast.makeText(getContext(), "please select client", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        File file = new File(mediaPath);
                        Log.i("***img", mediaPath + "   " + file.getName());

                        // Parsing any Media type file
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                        fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                        filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                    RequestBody tech_name = RequestBody.create(MediaType.parse("text/plain"), uusername);
                    RequestBody old_vts = RequestBody.create(MediaType.parse("text/plain"), s_old_device_id);
                    RequestBody new_vts = RequestBody.create(MediaType.parse("text/plain"), s_new_device_id);
                    RequestBody old_drs = RequestBody.create(MediaType.parse("text/plain"), s_old_drs_id);
                    RequestBody new_drs = RequestBody.create(MediaType.parse("text/plain"), s_new_drs_id);
                    RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), s_remarks);
                    RequestBody client = RequestBody.create(MediaType.parse("text/plain"), s_clientid);
                    RequestBody reason = RequestBody.create(MediaType.parse("text/plain"), s_reason_repla);

                    ApiHolder uploadImage = ServiceConnection.getClient(version).create(ApiHolder.class);
                    Log.i("**file", "" + fileToUpload + "  " + filename);

                    Call<UpdateDataResponse> call = uploadImage.uploadFile(fileToUpload, tech_name, old_vts, new_vts, old_drs, new_drs, client, reason, remarks);
                    Log.i("****call", String.valueOf(call));
                    call.enqueue(new Callback<UpdateDataResponse>() {
                        @Override
                        public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                            UpdateDataResponse updateDataResponse = response.body();
                            Log.i("**respnse", " " + response.body());
                            if (updateDataResponse != null) {
                                Toast.makeText(getActivity(), updateDataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                assert updateDataResponse != null;
                                Log.v("Response", updateDataResponse.toString());
                            }
                            pDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                            t.printStackTrace();
                            pDialog.dismiss();
                            TSnackbar snackbar = TSnackbar.make(v, "Try Again Connection Timeout", TSnackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(Color.RED);
                            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        }
                    });
                    // new UpdateInstall().execute("abc");
                }
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT < 24) {
                try {
                    mediaPath = fileUri.getPath();
                    // Set the Image in ImageView for Previewing the Media
                    Log.i("****img onactresult", String.valueOf(mediaPath));
                    upload_img_view.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Click again", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
            if (resultCode == Activity.RESULT_OK) {

                //mediaPath = fileUri.getPath();
                Log.i("**onresult", mediaPath + "*** ");
                bmp = BitmapFactory.decodeFile(mediaPath);

                // upload_img_view.setImageURI(Uri.parse(mediaPath));
                upload_img_view.setImageBitmap(getResizedBitmap(bmp,70));

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User Cancelled the action
            }
        }

    }

    private void fetchReasons() {
        ApiHolder get_list = ServiceConnection.getClient(version).create(ApiHolder.class);
        Call<UpdateDataResponse> call = get_list.get_replacement_reasons("0");
        call.enqueue(new Callback<UpdateDataResponse>() {
            @Override
            public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                try {
                    arr_replaceReasons = response.body().getReplaceReasons();
                    try {
                        try {
                            arr_reasons_s.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < arr_replaceReasons.size(); i++) {
                            arr_reasons_s.add(arr_replaceReasons.get(i).getR_name());
                        }
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arr_reasons_s);
                        reason_replace.setAdapter(adapter);

                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
                }
                //pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateDataResponse> call, Throwable t) {
                t.printStackTrace();
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();

                }
            }
        });
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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /* private void ShowImagePopup(FragmentActivity activity, String s, String gallery, String camera, final String type) {

        View alet_view = null;
        final Dialog alertDialogBuilder = new Dialog(activity);
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater mInflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        alet_view = mInflater.inflate(R.layout.custom_popup_image, null);

        final ImageView cross = (ImageView) alet_view.findViewById(R.id.cross);
        TextView tv_msg = (TextView) alet_view.findViewById(R.id.tv_msg);
        TextView galary = (TextView) alet_view.findViewById(R.id.gallery);
        final TextView cammera = (TextView) alet_view.findViewById(R.id.cammera);
        tv_msg.setText(s);
        galary.setText(gallery);
        cammera.setText(camera);
        alertDialogBuilder.setContentView(alet_view);

        alertDialogBuilder.getWindow().setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        alertDialogBuilder.setCanceledOnTouchOutside(true);
        alertDialogBuilder.show();


        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("")) {
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE_GALLERY);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");
                        startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                    }
                    alertDialogBuilder.dismiss();
                }
            }
        });

        cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(intent, REQUEST_CAMERA);
                    alertDialogBuilder.dismiss();
                }
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogBuilder.dismiss();
            }
        });

    }
*/
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

    public void addclients() {
        List<String> name = new ArrayList<String>();
        Collection c = client_hashMap.values();
        Iterator itr = c.iterator();
        name.add(" SELECT CLIENT");
        while (itr.hasNext()) {
            String temp = itr.next().toString();
            Log.i("TEMP", "" + temp);
            name.add(temp);
        }
        Collections.sort(name);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, name);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        client.setAdapter(dataAdapter);
    }

    void showDialogg(String msg, int labl) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("DATA UPDATED");
        alertDialog.setMessage(msg);
        if (labl == 0) {
            alertDialog.setButton("OK", Message.obtain(handler, labl));
        }
        alertDialog.show();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    e_old_device_id.setText("");
                    e_new_device_id.setText("");
                    e_old_drs_id.setText("");
                    e_new_drs_id.setText("");
                    e_remarks.setText("");
                    break;
                default:
                    break;
            }
        }
    };


    private void ShowImagePopup(FragmentActivity activity, String s, String gallery, String camera, final String type) {

        View alet_view = null;
        final Dialog alertDialogBuilder = new Dialog(activity);
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater mInflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        alet_view = mInflater.inflate(R.layout.custom_popup_image, null);

        final ImageView cross = alet_view.findViewById(R.id.cross);
        TextView tv_msg = alet_view.findViewById(R.id.tv_msg);
        TextView galary = alet_view.findViewById(R.id.gallery);
        final TextView cammera = alet_view.findViewById(R.id.cammera);
        tv_msg.setText(s);
        galary.setText(gallery);
        cammera.setText(camera);
        alertDialogBuilder.setContentView(alet_view);
        alertDialogBuilder.getWindow().setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        alertDialogBuilder.setCanceledOnTouchOutside(true);
        alertDialogBuilder.show();

        cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("")) {
                    if (Build.VERSION.SDK_INT < 24) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        try {
                            startActivityForResult(intent, REQUEST_CAMERA);
                            alertDialogBuilder.dismiss();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            try {

                                if (EasyPermissions.hasPermissions(getActivity(), PERMISSIONS)) {

                                } else {
                                    EasyPermissions.requestPermissions(this, "Access for storage",
                                            101, PERMISSIONS);
                                }
                            } catch (Exception qe) {
                                qe.printStackTrace();
                            }
                            alertDialogBuilder.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialogBuilder.dismiss();
                        }
                    } else {
                        try {
                            //     choosePhoto = new ChoosePhoto(CallSheetActivity.this);
                            if (EasyPermissions.hasPermissions(getActivity(), PERMISSIONS)) {

                            } else {
                                EasyPermissions.requestPermissions(this, "Access for storage",
                                        101, PERMISSIONS);
                            }
                        } catch (Exception qe) {
                            qe.printStackTrace();
                        }
                        openCameraIntent();
                        alertDialogBuilder.dismiss();
                    }
                }
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogBuilder.dismiss();
            }
        });
    }
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                fileUri = FileProvider.getUriForFile(getActivity(), "in.eoninfotech.eontechnician.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mediaPath = image.getAbsolutePath();
        return image;
    }
}
