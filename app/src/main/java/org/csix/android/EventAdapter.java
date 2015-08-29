package org.csix.android;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private final String LOG_TAG = EventAdapter.class.getSimpleName();

    private Cursor mCursor;
    private final Context mContext;
    private final EventAdapterOnClickHandler mClickHandler;

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tvHeader)
        TextView eventDate;
        @Bind(R.id.ivSpeaker)
        ImageView speakerImage;
        @Bind(R.id.tvSpeaker)
        TextView speaker;
        @Bind(R.id.tvTopic)
        TextView topic;

        public EventAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(EventsFragment.COL_EVENT_ID), this);
        }
    }

    public interface EventAdapterOnClickHandler {
        void onClick(Long eventId, EventAdapterViewHolder vh);
    }

    public EventAdapter(Context context, EventAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public EventAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewTyped) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card, viewGroup, false);
            view.setFocusable(true);
            return new EventAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(EventAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        Date date = new Date(mCursor.getLong(EventsFragment.COL_EVENT_DATE));
        String strTime = DateUtils.getShortDayOfWeekString(date)
                + ", "
                + DateUtils.getShortMonthString(date)
                + " "
                + DateUtils.getDayString(date);

        holder.eventDate.setText("" + strTime);
        holder.speaker.setText("" + mCursor.getString(EventsFragment.COL_EVENT_SPEAKER));
        holder.topic.setText("" + mCursor.getString(EventsFragment.COL_EVENT_TOPIC));

        String imageUrl = mCursor.getString(EventsFragment.COL_EVENT_IMAGE);
        if (imageUrl != null) {
            // The reason I did not use Picasso
            /*
            java.lang.NoSuchMethodError: No virtual method setConnectTimeout(JLjava/util/concurrent/TimeUnit;)V in class Lcom/squareup/okhttp/OkHttpClient; or its super classes (declaration of 'com.squareup.okhttp.OkHttpClient' appears in /data/app/org.csix.android-1/base.apk)
            at com.squareup.picasso.OkHttpDownloader.defaultOkHttpClient(OkHttpDownloader.java:32)
            at com.squareup.picasso.OkHttpDownloader.<init>(OkHttpDownloader.java:76)
            at com.squareup.picasso.OkHttpDownloader.<init>(OkHttpDownloader.java:55)
            at com.squareup.picasso.OkHttpDownloader.<init>(OkHttpDownloader.java:45)
            at com.squareup.picasso.Utils$OkHttpLoaderCreator.create(Utils.java:424)
            at com.squareup.picasso.Utils.createDefaultDownloader(Utils.java:250)
            at com.squareup.picasso.Picasso$Builder.build(Picasso.java:832)
            at com.squareup.picasso.Picasso.with(Picasso.java:662)
            at org.csix.android.EventListAdapter.bindView(EventListAdapter.java:72)
            */
            // Picasso.with(context).load(imageUrl).into(viewHolder.speakerImage);

            // Use Glide instead of image loading
            Glide.with(mContext).load(imageUrl).into(holder.speakerImage);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
