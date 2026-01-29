package in.eoninfotech.eontechnician;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import in.eoninfotech.eontechnician.databinding.ActivityDeviceMaintenanceBinding;
import in.eoninfotech.eontechnician.databinding.ActivityVideoViewBinding;


public class DeviceMaintenance extends AppCompatActivity  {

    ActivityDeviceMaintenanceBinding binding;
    String type;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityDeviceMaintenanceBinding.inflate(getLayoutInflater());

        type = getIntent().getStringExtra("type");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(type.equalsIgnoreCase("maint")){
            actionBar.setTitle("Device Maintenance");
        }else if(type.equalsIgnoreCase("knowldge")){
            actionBar.setTitle("Knowldge Base");
        }else {
            actionBar.setTitle("FAQs");
        }

        // getting our root layout in our view.
        View view = binding.getRoot();
        // below line is to set
        // Content view for our layout.
        setContentView(view);

        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient());

        binding.bufferingProgressBar.setVisibility(View.VISIBLE);

        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Hide progress bar when page is fully loaded
                binding.bufferingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                binding.bufferingProgressBar.setVisibility(View.GONE); // Hide on error too
                super.onReceivedError(view, request, error);
            }
        });

        String pdf = "https://mis.eurotrack.in/eonmis/device_removal/GPS_Device_Maintenance_Manual.pdf";

        if(type.equalsIgnoreCase("maint")){
            binding.webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        }else if(type.equalsIgnoreCase("knowldge")){
            binding.webview.loadUrl("https://mis.eurotrack.in/eonmis/operations/knowledge-base-app.php");
        }else {
            binding.webview.loadUrl("https://mis.eurotrack.in/eonmis/operations/faq-app.php");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
