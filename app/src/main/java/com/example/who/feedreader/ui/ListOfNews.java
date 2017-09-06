package com.example.who.feedreader.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.who.feedreader.R;
import com.example.who.feedreader.adapters.ListOfNewsAdapter;
import com.example.who.feedreader.interfaces.IlistOfNewsView;
import com.example.who.feedreader.pojo.Item;
import com.example.who.feedreader.presenters.ListOfNewsPresenter;

import java.util.List;

import butterknife.BindView;

public class ListOfNews extends AppCompatActivity implements IlistOfNewsView {

    @BindView(R.id.rvListOfNews)
    android.support.v7.widget.RecyclerView rvListOfNews;

    private ListOfNewsAdapter adapter;
    private ListOfNewsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_news_titles);
        presenter = new ListOfNewsPresenter(ListOfNews.this, this);

    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBack() {

    }

    @Override
    public void setDataToAdapter(List<Item> data) {
        adapter = new ListOfNewsAdapter(this, data);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListOfNews.setLayoutManager(layoutManager);
        rvListOfNews.setAdapter(adapter);
    }
}
