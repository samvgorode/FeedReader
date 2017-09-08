package com.example.who.feedreader.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.who.feedreader.interfaces.IlistOfNewsView;
import com.example.who.feedreader.pojo.Item;
import com.orhanobut.hawk.Hawk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static com.example.who.feedreader.global.Constants.FIRST_MAP_OF_ITEMS;
import static com.example.who.feedreader.global.Constants.ITEMS_DELETED;

/**
 * Created by who on 06.09.2017.
 */

public class ListOfNewsPresenter {

    private static final String TAG = ListOfNewsPresenter.class.getSimpleName();
    private Context mContext;
    private IlistOfNewsView view;

    public ListOfNewsPresenter(Context context, IlistOfNewsView view) {
        this.mContext = context;
        this.view = view;
        initList();
    }

    private void initList() {
        Hawk.init(mContext).build();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });

    }

    private void getData() {
        if (isNetworkConnected()) {
            new RetrieveFeedTask().execute();
        } else {
            List<Item> data = new ArrayList<>();
            Map<String, Item> mapFromInet = new HashMap<>();
            mapFromInet = Hawk.get(FIRST_MAP_OF_ITEMS);
            if (mapFromInet.size() > 0) {
                for (String key : mapFromInet.keySet()) {
                    data.add(mapFromInet.get(key));
                    view.setDataToAdapter(data);
                }
            } else {
                Toast.makeText(mContext, "No data offline", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RetrieveFeedTask extends AsyncTask<Void, Void, List<Item>> {

        List<Item> list = new ArrayList<>();
        Map<String, Item> mapFromInet = new HashMap<>();
        Map<String, Item> mapDeleted = new HashMap<>();
        private Exception exception;

        protected List<Item> doInBackground(Void... urls) {
            try {
                try {
                    Document doc = Jsoup.connect("http://www.cbc.ca/cmlink/rss-topstories").get();
                    Elements items = doc.select("item");
                    for (Element i : items) {
                        Elements ids = i.getElementsByAttribute("cbc:deptid");
                        String id = ids.attr("cbc:deptid");
                        Elements titles = i.getElementsByTag("title");
                        String title = titles.text();
                        Elements links = i.getElementsByTag("link");
                        String link = links.text();
                        Elements authors = i.getElementsByTag("author");
                        String author = authors.text();
                        Elements descriptions = i.getElementsByTag("description");
                        String imageLink = extractLinks(descriptions.text())[0];
                        Elements pubDates = i.getElementsByTag("pubDate");
                        String pubDate = pubDates.text();

                        Item item = new Item();
                        item.setId(id);
                        item.setTitle(title);
                        item.setLink(link);
                        item.setAuthor(author);
                        item.setImage(imageLink);
                        item.setPubDate(pubDate);
//                        list.add(item);

                        mapDeleted = Hawk.get(ITEMS_DELETED);
                        if (!mapDeleted.containsKey(id)) mapFromInet.put(id, item);

                    }
                    Hawk.put(FIRST_MAP_OF_ITEMS, mapFromInet);
                    for (String key : mapFromInet.keySet()) {
                        list.add(mapFromInet.get(key));
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return list;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(List<Item> feed) {
            view.setDataToAdapter(feed);
        }
    }

    private static String[] extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            Log.d(TAG, "URL extracted: " + url);
            links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}