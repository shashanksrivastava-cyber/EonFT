package in.eoninfotech.eontechnician;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.eoninfotech.eontechnician.activity.DevicedashboardDetail;
import in.eoninfotech.eontechnician.activity.Devicedashboards;
import in.eoninfotech.eontechnician.activity.ReceiveDeviceActivity;
import in.eoninfotech.eontechnician.databinding.ActivityVideoViewBinding;
import in.eoninfotech.eontechnician.databinding.DeviceDashboardActivityBinding;
import in.eoninfotech.eontechnician.responses.DeviceCount;
import in.eoninfotech.eontechnician.viewModel.ViewModelDeviceDashboard;

public class VideoViewActivity extends AppCompatActivity  {

    ActivityVideoViewBinding binding;
    private ExoPlayer player;
    String type;
    Uri uri;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        hideSystemUI();

        binding = ActivityVideoViewBinding.inflate(getLayoutInflater());

        // getting our root layout in our view.
        View view = binding.getRoot();
        // below line is to set
        // Content view for our layout.
        setContentView(view);

        type = getIntent().getStringExtra("type");


        player = new ExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(player);

        String videoUrl = "https://mis.eurotrack.in/eonmis/device_removal/GPS_Device_Removal(TM).mp4";
        String pumpUrl = "https://mis.eurotrack.in/eonmis/device_removal/GPS_Device_Removal(Pump).mp4";

        if(type.equalsIgnoreCase("tm")){
            uri = Uri.parse(videoUrl);
        }else {
            uri = Uri.parse(pumpUrl);
        }

        MediaItem mediaItem = MediaItem.fromUri(uri);

        // Prepare the player with the media item
        player.setMediaItem(mediaItem);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        binding.bufferingProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_READY:
                    case Player.STATE_ENDED:
                        binding.bufferingProgressBar.setVisibility(View.GONE);
                        break;
                    case Player.STATE_IDLE:
                        // Optional: you can hide or reset if needed
                        binding.bufferingProgressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });

        player.prepare();
        player.setPlayWhenReady(true); // Start playing when ready

    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
