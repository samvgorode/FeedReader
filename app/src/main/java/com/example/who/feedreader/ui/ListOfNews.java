package com.example.who.feedreader.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.who.feedreader.R;
import com.example.who.feedreader.adapters.ListOfNewsAdapter;
import com.example.who.feedreader.events.MessageEvent;
import com.example.who.feedreader.interfaces.IlistOfNewsView;
import com.example.who.feedreader.pojo.Item;
import com.example.who.feedreader.presenters.ListOfNewsPresenter;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.who.feedreader.global.Constants.FIRST_MAP_OF_ITEMS;
import static com.example.who.feedreader.global.Constants.ITEMS_DELETED;

public class ListOfNews extends AppCompatActivity implements IlistOfNewsView {


    @BindView(R.id.rvListOfNews)
    public RecyclerView rvListOfNews;
    @BindView(R.id.btnAbort)
    public Button btnAbort;

    private ListOfNewsAdapter adapter;
    private ListOfNewsPresenter presenter;
    private Runnable runnable;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_news_titles);
        ButterKnife.bind(this);
        initHawk();
        presenter = new ListOfNewsPresenter(ListOfNews.this, this);
    }

    private void initHawk() {
        Hawk.init(this).build();
        Map<String, Item> mapFromInet = new HashMap<>();
        Map<String, Item> mapDeleted = new HashMap<>();
        if (!Hawk.contains(FIRST_MAP_OF_ITEMS)) Hawk.put(ITEMS_DELETED, mapFromInet);
        if (!Hawk.contains(ITEMS_DELETED)) Hawk.put(ITEMS_DELETED, mapDeleted);
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBack() {

    }

    public void cancelDeleting(View view){
        Toast.makeText(this, "Cancel deleting", Toast.LENGTH_SHORT).show();
        handler.removeCallbacksAndMessages(null);
        btnAbort.setVisibility(View.GONE);
    }

    // Get ID of view to delete
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final MessageEvent event) {
        Toast.makeText(this, "Click delete", Toast.LENGTH_SHORT).show();
        btnAbort.setVisibility(View.VISIBLE);
        runnable = new Runnable() {
            @Override
            public void run() {
                btnAbort.setVisibility(View.GONE);
                Map<String, Item> mapDeleted = new HashMap<>();
                Map<String, Item> mapFromInet = new HashMap<>();
                if (!Hawk.contains(ITEMS_DELETED)) Hawk.put(ITEMS_DELETED, mapDeleted);
                else {
                    mapDeleted = Hawk.get(ITEMS_DELETED);
                    mapDeleted.put(event.getId(), event.getItem());
                    Hawk.put(ITEMS_DELETED, mapDeleted);
                    mapFromInet = Hawk.get(FIRST_MAP_OF_ITEMS);
                    if (mapFromInet.containsKey(event.getId())) {
                        mapFromInet.remove(event.getId());
                        Hawk.put(FIRST_MAP_OF_ITEMS, mapFromInet);
                    }
                }
                List<Item> data = new ArrayList<>();
                for (String key: mapFromInet.keySet()) {
                    data.add(mapFromInet.get(key));
                } setDataToAdapter(data);
            }
        };
        handler.postDelayed(runnable, 5000);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setDataToAdapter(List<Item> data) {
        adapter = new ListOfNewsAdapter(this, data);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(rvListOfNews!=null){
        rvListOfNews.setLayoutManager(layoutManager);
        rvListOfNews.setAdapter(adapter);}
    }
}
