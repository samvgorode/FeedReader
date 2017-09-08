package com.example.who.feedreader.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.who.feedreader.R;
import com.example.who.feedreader.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.who.feedreader.global.Constants.URL_TO_LOAD;

/**
 * Created by who on 06.09.2017.
 */

public class NewsItem extends AppCompatActivity {

    @BindView(R.id.wwNewBody)
    public WebView wwNewBody;
    @BindView(R.id.pbNewBody)
    public ProgressBar pbNewBody;

    public static Intent getNewIntent(Context context, String url) {
        Intent intent = new Intent(context, NewsItem.class);
        intent.putExtra(URL_TO_LOAD, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        ButterKnife.bind(this);
        fetchIntent();
    }

    private void fetchIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra(URL_TO_LOAD)){
            String url = intent.getStringExtra(URL_TO_LOAD);
            if(!TextUtils.isEmpty(url)){
                wwNewBody.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        pbNewBody.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        pbNewBody.setVisibility(View.GONE);
                    }
                });
                wwNewBody.loadUrl(url);
            }
        }
    }
}
