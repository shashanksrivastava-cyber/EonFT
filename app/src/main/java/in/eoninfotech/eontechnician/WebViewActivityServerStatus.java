package in.eoninfotech.eontechnician;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivityServerStatus extends AppCompatActivity {
    WebView myWebView;
    ProgressBar pbar;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        pbar = findViewById(R.id.progressBar1);
        myWebView = findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("http://mail.cybernetra.net:8080/mobile/chk_option.php?");
    }

    @Override
    public void onBackPressed() {
        this.count++;
        if (this.count <= 1) {
            // on back pressed action
        } else {
            super.onBackPressed();
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            pbar.setVisibility(View.GONE);
        }
    }
}
