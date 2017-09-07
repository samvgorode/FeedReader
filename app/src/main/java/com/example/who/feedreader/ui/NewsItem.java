package com.example.who.feedreader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
//        wwNewBody = (WebView)findViewById(R.id.wwNewBody);
        fetchIntent();
    }

    private void fetchIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra(URL_TO_LOAD)){
            String url = intent.getStringExtra(URL_TO_LOAD);
            if(!TextUtils.isEmpty(url)){
                wwNewBody.setWebViewClient(new WebViewClient());
                wwNewBody.loadUrl(url);
            }
        }
    }
}
