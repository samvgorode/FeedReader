package com.example.who.feedreader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.who.feedreader.R;
import com.example.who.feedreader.pojo.Item;
import com.example.who.feedreader.views.ListOfNewsItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by who on 06.09.2017.
 */

public class ListOfNewsAdapter extends RecyclerView.Adapter<ListOfNewsAdapter.MyViewHolder> {

    private Context context;
    private List<Item> data = new ArrayList<>();

    public ListOfNewsAdapter(Context context, List<Item> data) {
        this.context = context;
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ListOfNewsItemView itemView;

        public MyViewHolder(View view) {
            super(view);
            itemView = (ListOfNewsItemView) view;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        updateView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListOfNewsItemView itemView = (ListOfNewsItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new MyViewHolder(itemView);
    }

    private void updateView(ListOfNewsItemView itemView, int position) {
        Item model = getItem(position);
        itemView.setItem(model);
    }

    public Item getItem(int position) {
        return data.get(position);
    }
}