package com.example.who.feedreader.views;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.who.feedreader.R;
import com.example.who.feedreader.events.MessageEvent;
import com.example.who.feedreader.pojo.Item;
import com.example.who.feedreader.ui.NewsItem;
import com.example.who.feedreader.utils.TimeUtils;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.who.feedreader.global.Constants.FIRST_MAP_OF_ITEMS;
import static com.example.who.feedreader.global.Constants.ITEMS_DELETED;

/**
 * Created by who on 06.09.2017.
 */

public class ListOfNewsItemView extends RelativeLayout {

    public static final String TAG = ListOfNewsItemView.class.getSimpleName();

    @BindView(R.id.rootSwipeLayout)
    SwipeLayout rootSwipeLayout;
    @BindView(R.id.rlBottomWrapper)
    RelativeLayout rlBottomWrapper;
    @BindView(R.id.ivChannelsContactProfile)
    ImageView ivImageOfNew;
    @BindView(R.id.ivChannelsContactProfileBottom)
    ImageView ivImageOfNewBottom;
    @BindView(R.id.tvChannelsContactName)
    TextView tvTitleOfNew;
    @BindView(R.id.tvChannelsContactNameBottom)
    TextView tvTitleOfNewBottom;
    @BindView(R.id.tvChannelsContactLastMessage)
    TextView tvAuthorOfNew;
    @BindView(R.id.tvChannelsContactLastMessageBottom)
    TextView tvAuthorOfNewBottom;
    @BindView(R.id.tvChannelsTimeOfMessage)
    TextView tvTimeOfNew;
    @BindView(R.id.tvChannelsTimeOfMessageBottom)
    TextView tvTimeOfNewBottom;
    @BindView(R.id.wrap)
    RelativeLayout wrap;

    private String newId;
    private Item item;

    public ListOfNewsItemView(Context context) {
        super(context);
        init();
    }

    public ListOfNewsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListOfNewsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.list_of_news_item_view, this);
        ButterKnife.bind(this);
    }

    void setImageOfNew(String src) {
        List<ImageView> list = new ArrayList<>();
        list.add(0, ivImageOfNew);
        list.add(1, ivImageOfNewBottom);
        if (isNetworkConnected()) {
            new DownloadImageTask(list).execute(src);
        } else {
            ivImageOfNew.setImageBitmap(getImageFromSD());
            ivImageOfNewBottom.setImageBitmap(getImageFromSD());
        }
    }

    void setTitleOfNew(String name) {
        tvTitleOfNew.setText(name);
        tvTitleOfNewBottom.setText(name);
    }

    void setAuthorOfNew(String message) {
        tvAuthorOfNew.setText(message);
        tvAuthorOfNewBottom.setText(message);
    }

    void setTimeOfNew(String time) {
        tvTimeOfNew.setText(time);
    }

    public static ListOfNewsItemView inflate(ViewGroup parent) {
        ListOfNewsItemView itemView = (ListOfNewsItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return itemView;
    }

    public void setItem(Item item) {
        if (item != null) {
            ListOfNewsItemView.this.item = item;
            newId = item.getId();
            final String imageUri = item.getImage();
            final String titleOfNew = item.getTitle();
            final String authorOfNew = item.getAuthor();
            final String timeOfNew = item.getPubDate();
            final String url = item.getLink();
            rootSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            rootSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, rlBottomWrapper);
            rootSwipeLayout.setOnTouchListener(null);
            rootSwipeLayout.getSurfaceView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(url))
                        getContext().startActivity(NewsItem.getNewIntent(getContext(), url));
                    else
                        Toast.makeText(getContext(), "No URL from item!", Toast.LENGTH_SHORT).show();
                }
            });
            setImageOfNew(imageUri);
            setTitleOfNew(titleOfNew);
            setAuthorOfNew(authorOfNew);
            setTimeOfNew(TimeUtils.getNormalizedTime(timeOfNew));
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        List<ImageView> bmImageList = new ArrayList<>();

        public DownloadImageTask(List<ImageView> bmImageList) {
            this.bmImageList = bmImageList;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(result);
            bmImageList.get(0).setImageBitmap(result);
            bmImageList.get(1).setImageBitmap(result);
        }
    }

    @OnClick(R.id.ivRemoveItem)
    public void removeItem() {
        EventBus.getDefault().post(new MessageEvent(item, newId));
    }

    private void saveImage(Bitmap finalBitmap) {

        File file = new File(getPrivateDirectory(getContext()), newId);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImageFromSD() {
        File file = new File(getPrivateDirectory(getContext()), newId);
        String photoPath = file.getAbsolutePath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        return bitmap;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static File getPrivateDirectory(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("story", Context.MODE_PRIVATE);
        //File directory = context.getExternalFilesDir(DIRECTORY_PICTURES);
        if (!directory.exists()) directory.mkdir();
        return directory;
    }
}
