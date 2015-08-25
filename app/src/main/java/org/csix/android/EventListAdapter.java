package org.csix.android;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.csix.android.data.CSixContract;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventListAdapter extends CursorAdapter {

    private final String LOG_TAG = EventListAdapter.class.getSimpleName();

    public static class ViewHolder {

        @Bind(R.id.tvHeader)
        TextView eventDate;
        @Bind(R.id.ivSpeaker)
        ImageView speakerImage;
        @Bind(R.id.tvSpeaker)
        TextView speaker;
        @Bind(R.id.tvTopic)
        TextView topic;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public EventListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Date date = new Date(cursor.getLong(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_DATE)));
        String strTime = DateUtils.getShortDayOfWeekString(date)
                + ", "
                + DateUtils.getShortMonthString(date)
                + " "
                + DateUtils.getDayString(date);

        viewHolder.eventDate.setText("" + strTime);
        viewHolder.speaker.setText("" + cursor.getString(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_SPEAKER)));
        viewHolder.topic.setText("" + cursor.getString(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_TOPIC)));

        String imageUrl = cursor.getString(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_IMAGE));
        if (imageUrl != null) {
            // Picasso.with(context).load(imageUrl).into(viewHolder.speakerImage);
            Glide.with(context).load(imageUrl).into(viewHolder.speakerImage);
        }
    }
}
