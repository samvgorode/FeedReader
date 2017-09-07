package com.example.who.feedreader.events;

import com.example.who.feedreader.pojo.Item;

/**
 * Created by who on 07.09.2017.
 */

public class MessageEvent {

    public Item item;
    public String id;

    public MessageEvent(Item item, String id) {
        this.item = item;
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
