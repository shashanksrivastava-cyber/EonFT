package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.myprovider.StringUtils;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

/**
 * Created by root on 26/11/18.
 */

public class LiveDataWebView extends AppCompatActivity {

    WebView wv;
    String html;
    RelativeLayout progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_data_web_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Live Fault Details");
        wv = (WebView)findViewById(R.id.webview);
        progressBar = (RelativeLayout)findViewById(R.id.llayoutProgress);
        wv.getSettings().setJavaScriptEnabled(true);

        int DELAY = 1000;
        ShowProgressBar(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                html = intent.getStringExtra("name");
                String url = ServiceConnectionNewURL.BASE_URL+html;
                wv.loadUrl(url);
                ShowProgressBar(false);
            }
        }, DELAY);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
