package com.example.planetgreece;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class BrowserActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private WebViewClient webViewClient;
    private ProgressBar progressBar;
    private String link;
    private ImageButton btnBack;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        link = getIntent().getStringExtra("LINK");

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.wvBrowser);
        webView.loadUrl(link);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        webViewClient = new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        };
        webView.setWebViewClient(webViewClient);

        webSettings.setBuiltInZoomControls(true);
    }
}