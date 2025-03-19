package com.memaww.memaww.Activities;

import android.graphics.Bitmap;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class ReaderWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mUrlTextView;
    private ImageView mHttpsLockImageView, mBackImageView;
    private WebView mWebView;
    private ProgressBar mPageLoadingProgressBar;
    private String websiteUrl = "", domainName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_web_view);

        // BINDING VIEWS
        mUrlTextView = findViewById(R.id.activity_webview_constraint2_title_textview);
        mHttpsLockImageView = findViewById(R.id.activity_webview_padlock_imageView);
        mWebView = findViewById(R.id.activity_webview_webview);
        mBackImageView = findViewById(R.id.activity_webview_back_imageview);
        mPageLoadingProgressBar = findViewById(R.id.activity_webview_loader);

        if(getIntent().getExtras() !=null) {
            websiteUrl =(String) getIntent().getExtras().get(Config.WEBVIEW_KEY_URL);
            domainName = Config.getUrlComponent(websiteUrl, 1);
            domainName = Config.removeWwwAndHttpFromUrl(domainName);
            mPageLoadingProgressBar.setVisibility(View.GONE);
        } else {
            finish();
        }

        if(domainName.trim().equalsIgnoreCase("")){
            mUrlTextView.setText("External Link");
        } else {
            mUrlTextView.setText(domainName);
        }

        if(Config.getUrlComponent(websiteUrl, 2).trim().equalsIgnoreCase("https")){
            mHttpsLockImageView.setImageResource(R.drawable.closed_padlock);
            mHttpsLockImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_green_dark), PorterDuff.Mode.SRC_IN);
        } else {
            mHttpsLockImageView.setImageResource(R.drawable.open_padlock);
            mHttpsLockImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_gray), PorterDuff.Mode.SRC_IN);
        }
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl(websiteUrl);
        mWebView.setWebChromeClient(new WebChromeClient());

        mBackImageView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.activity_webview_back_imageview){
            onBackPressed();
        }
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mPageLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mPageLoadingProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
