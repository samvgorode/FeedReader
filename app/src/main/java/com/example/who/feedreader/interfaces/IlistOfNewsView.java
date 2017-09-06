package com.example.who.feedreader.interfaces;

import com.example.who.feedreader.pojo.Item;

import java.util.List;

/**
 * Created by who on 06.09.2017.
 */

public interface IlistOfNewsView {
    void updateAdapter();

    void onBack();

    void setDataToAdapter(List<Item> data);
}
