package com.example.who.feedreader.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.who.feedreader.R;
import com.example.who.feedreader.pojo.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    ImageView ivChannelsContactProfile;
    @BindView(R.id.ivChannelsContactProfileBottom)
    ImageView ivChannelsContactProfileBottom;
    @BindView(R.id.tvChannelsContactName)
    TextView tvChannelsContactName;
    @BindView(R.id.tvChannelsContactNameBottom)
    TextView tvChannelsContactNameBottom;
    @BindView(R.id.tvChannelsContactLastMessage)
    TextView tvChannelsContactLastMessage;
    @BindView(R.id.tvChannelsContactLastMessageBottom)
    TextView tvChannelsContactLastMessageBottom;
    @BindView(R.id.tvChannelsTimeOfMessage)
    TextView tvChannelsTimeOfMessage;
    @BindView(R.id.wrap)
    RelativeLayout wrap;

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

    void setUserImage(String src) {

    }

    void setUserName(String name) {
        tvChannelsContactName.setText(name);
        tvChannelsContactNameBottom.setText(name);
    }

    void setLastMessage(String message) {
        tvChannelsContactLastMessage.setText(message);
        tvChannelsContactLastMessageBottom.setText(message);
    }

    void setTimeOfMessage(String time) {
        tvChannelsTimeOfMessage.setText(time);
    }

//    public static ChannelsItemView inflate(ViewGroup parent) {
//        ChannelsItemView itemView = (ChannelsItemView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.channels_item, parent, false);
//        return itemView;
//    }

    public void setItem(Item item) {
        if (item != null) {
           /* final int senderID = item.getLastMessage().getSender().getId();
            final String senderName = "" + item.getLastMessage().getSender().getFirstName() + " " + item.getLastMessage().getSender().getLastName();
            final String senderPhoto = item.getLastMessage().getSender().getPhoto();
            rootSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            rootSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, rlBottomWrapper);
            rootSwipeLayout.setOnTouchListener(null);
            rootSwipeLayout.getSurfaceView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ListOfNews.class);
                    i.putExtra(SENDER_ID, senderID);
                    i.putExtra(SENDER_NAME, senderName);
                    i.putExtra(SENDER_PHOTO, senderPhoto);
                    getContext().startActivity(i);
                }
            });
            setUserName(senderName);
            setLastMessage("" + item.getLastMessage().getText());
            setNumberUnread(item.getUnreadMessagesCount());
            setUserImage(senderPhoto);
            String d = item.getLastMessage().getCreateDate();
            setTimeOfMessage(TimeUtils.getNormalizedTime(d));*/
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}
