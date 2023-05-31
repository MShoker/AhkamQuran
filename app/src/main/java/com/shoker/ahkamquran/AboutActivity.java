package com.shoker.ahkamquran;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class AboutActivity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String s = "file:///android_asset/"+31+".html";
         progressBar = findViewById(R.id.pb_about);
        WebView webView =findViewById(R.id.wv_about);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(final WebView view, String url) {

                progressBar.setVisibility(View.GONE);
            }
        } );
        webView.loadUrl(s);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
    }
}
